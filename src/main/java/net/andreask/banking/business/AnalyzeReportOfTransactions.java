
/*  $Id: AnalyzeReportOfTransactions.java,v 1.1 2011/05/04 22:37:45 willuhn Exp $

    This file is part of HBCI4Java
    Copyright (C) 2001-2008  Stefan Palme

    HBCI4Java is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    HBCI4Java is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package net.andreask.banking.business;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.GV_Result.GVRKUms.UmsLine;
import org.kapott.hbci.callback.HBCICallback;
import org.kapott.hbci.callback.HBCICallbackConsole;
import org.kapott.hbci.callback.HBCICallbackUnsupported;
import org.kapott.hbci.concurrent.DefaultHBCIPassportFactory;
import org.kapott.hbci.concurrent.HBCIPassportFactory;
import org.kapott.hbci.concurrent.HBCIRunnable;
import org.kapott.hbci.concurrent.HBCIThreadFactory;
import org.kapott.hbci.manager.FileSystemClassLoader;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;


public final class AnalyzeReportOfTransactions 
{
    private static class MyHBCICallback
        extends HBCICallbackConsole
    {
        public void callback(HBCIPassport passport,int reason,String msg,int dataType,StringBuffer retData)
        {
            System.out.println("Callback für folgendes Passport: "+passport.getClientData("init").toString());
            super.callback(passport,reason,msg,dataType,retData);
        }
    }
    
    public static void main(String[] args)
        throws Exception
    {
//        HBCIPassportPinTan passport = new HBCIPassportPinTan();
        // HBCI Objekte
        HBCIPassport passport   = null;
        HBCIHandler  hbciHandle = null;

        // HBCI4Java initialisieren
        HBCIUtils.init(HBCIUtils.loadPropertiesFile(new FileSystemClassLoader(),
                "sparda.properties"),
                       new MyHBCICallback());

        // Nutzer-Passport initialisieren
        Object passportDescription="Passport für Kontoauszugs-Demo";
        passport=AbstractHBCIPassport.getInstance(passportDescription);

        try {
            // ein HBCI-Handle für einen Nutzer erzeugen
            String version=passport.getHBCIVersion();
            hbciHandle=new HBCIHandler((version.length()!=0)?version:"300",passport);

            // Kontoauszüge auflisten
            analyzeReportOfTransactions(passport, hbciHandle);

        } finally {
            if (hbciHandle!=null) {
                hbciHandle.close();
            } else if (passport!=null) {
                passport.close();
            }
        }
    }

    public static void main_multithreaded(String[] args)
        throws Exception
    {

        // Da im main-Thread keine HBCI Aktionen laufen sollen, reicht es hier, die Umgebung
        // nur "notdürftig" zu initialisieren. Leere Konfiguration, und keine Callback-Unterstützung.
        HBCIUtils.init(new Properties(), new HBCICallbackUnsupported());

        // Die Verwendung der HBCIThreadFactory ist für die korrekte Funktionsweise von HBCI4Java zwingend erforderlich
        // (Alternativ müsste manuell sichergestellt werden, dass jeder Thread in einer eigenen Thread-Gruppe läuft.)
        ExecutorService executor = Executors.newCachedThreadPool(new HBCIThreadFactory());

        // Einstellungen für die Aufgabe erstellen
        Properties properties = HBCIUtils.loadPropertiesFile(new FileSystemClassLoader(),"/home/stefan.palme/temp/a.props");
        HBCICallback callback = new MyHBCICallback();
        HBCIPassportFactory passportFactory = new DefaultHBCIPassportFactory((Object) "Passport für Kontoauszugs-Demo");

        // Aufgabe implementieren. Die HBCIRunnable übernimmt Initialisierung
        // und Schließen von Passport und Handler automatisch.
        Runnable runnable = new HBCIRunnable(properties, callback, passportFactory) {
            @Override
            protected void execute() throws Exception {

                // Kontoauszüge auflisten
                analyzeReportOfTransactions(passport, handler);

            }
        };

        // Aufgabe ausführen
        executor.submit(runnable);

        // Executor runterfahren und warten, bis alle Aufgaben fertig sind
        executor.shutdown();
        while (!executor.isTerminated()) {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }

        // Haupt-Thread beenden
        HBCIUtils.done();

    }

    private static void analyzeReportOfTransactions(HBCIPassport hbciPassport, HBCIHandler hbciHandle) {
        // auszuwertendes Konto automatisch ermitteln (das erste verfügbare HBCI-Konto)
        Konto myaccount=hbciPassport.getAccounts()[0];
        // wenn der obige Aufruf nicht funktioniert, muss die abzufragende
        // Kontoverbindung manuell gesetzt werden:
        // Konto myaccount=new Konto("DE","86055592","1234567890");

        // Job zur Abholung der Kontoauszüge erzeugen
        HBCIJob auszug=hbciHandle.newJob("KUmsAll");
        auszug.setParam("my",myaccount);
        // evtl. Datum setzen, ab welchem die Auszüge geholt werden sollen
        // job.setParam("startdate","21.5.2003");
        auszug.addToQueue();

        // alle Jobs in der Job-Warteschlange ausführen
        HBCIExecStatus ret=hbciHandle.execute();

        GVRKUms result=(GVRKUms)auszug.getJobResult();
        // wenn der Job "Kontoauszüge abholen" erfolgreich ausgeführt wurde
        if (result.isOK()) {
            // kompletten kontoauszug als string ausgeben:
            System.out.println(result.toString());

            // kontoauszug durchlaufen, jeden eintrag einmal anfassen:

            List<UmsLine> lines=result.getFlatData();
            // int  numof_lines=lines.size();

            for (Iterator<UmsLine> j=lines.iterator(); j.hasNext(); ) { // alle Umsatzeinträge durchlaufen
                UmsLine entry= j.next();

                // für jeden Eintrag ein Feld mit allen Verwendungszweckzeilen extrahieren
                List<String> usages=entry.usage;
                // int  numof_usagelines=usages.size();

                for (Iterator<String> k=usages.iterator(); k.hasNext(); ) { // alle Verwendungszweckzeilen durchlaufen
                    String usageline= k.next();

                    // ist eine bestimmte Rechnungsnummer gefunden (oder welche
                    // Kriterien hier auch immer anzuwenden sind), ...
                    if (usageline.equals("Rechnung 12345")) {
                        // hier diesen Umsatzeintrag (<entry>) auswerten

                        // entry.bdate enthält Buchungsdatum
                        // entry.value enthält gebuchten Betrag
                        // entry.usage enthält die Verwendungszweck-zeilen
                        // mehr Informationen sie Dokumentation zu
                        //   org.kapott.hbci.GV_Result.GVRKUms
                    }
                }
            }

        } else {
            // Fehlermeldungen ausgeben
            System.out.println("Job-Error");
            System.out.println(result.getJobStatus().getErrorString());
            System.out.println("Global Error");
            System.out.println(ret.getErrorString());
        }
    }

}
