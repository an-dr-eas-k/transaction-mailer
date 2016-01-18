package net.andreask.banking;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import net.andreask.banking.integration.hbci.HbciFacade;
import net.andreask.banking.integration.hbci.hbci4java.HbciFacadeImpl;
import net.andreask.banking.model.AccountConnection;
import net.andreask.banking.model.AccountTransaction;
import org.dom4j.dom.DOMDocument;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Hello world!
 */
public class PrepareHBCIMock {

    @Inject
    private static Properties testProperties = App.provideProperties();



    public static void main(String[] args) throws IOException {
        System.out.println("Starting ...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("PIN: ");

        List<AccountTransaction> result = new HbciFacadeImpl()
                .setAccountConnection(
                        new AccountConnection()
                                .setBankCode("70090500")
                                .setCustomerId("3964620")
                                .setAccountNumber("103964620")
                                .setPin(Integer.parseInt(br.readLine()))
                                .setHbciVersion("300")
                                .setUrl("fints.bankingonline.de/fints/FinTs30PinTanHttpGate")
                                .setCountryCode("DE"))
                .init()
                .acquireTransactions();

        XStream xstream = new XStream(new StaxDriver());
        Document doc = new DocumentImpl();
        xstream.marshal(result, new DomWriter(doc));
        FileWriter fw = new FileWriter(testProperties.getProperty("hbcimock.data.file"));
        fw.write(format(doc));
        fw.close();
    }

    private static String format(Document input) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(input);
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch (Exception e) {
            return "";
        }

    }



}
