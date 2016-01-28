package net.andreask.banking;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import net.andreask.banking.business.Configuration;
import net.andreask.banking.integration.hbci.HbciAccessImpl;
import net.andreask.banking.integration.hbci.hbci4java.HbciFacadeImpl;
import net.andreask.banking.model.AccountTransaction;

/**
 * Hello world!
 */
public class PrepareHBCIMock {

  public static void main(String[] args) throws IOException {
    Configuration c = new Configuration();
    System.out.println("Starting ...");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("PIN: ");

    List<AccountTransaction> result = new HbciFacadeImpl()
        .setAccountConnection(
            new HbciAccessImpl()
                .setBankCode("70090500")
                .setCustomerId("3964620")
                .setAccountNumber("103964620")
                .setPin(br.readLine())
                .setHbciVersion("300")
                .setUrl("fints.bankingonline.de/fints/FinTs30PinTanHttpGate")
                .setCountryCode("DE"))
        .init()
        .acquireTransactions();

    XStream xstream = new XStream(new StaxDriver());
    Document doc = new DocumentImpl();
    xstream.marshal(result, new DomWriter(doc));
    FileWriter fw = new FileWriter(c.getProperty("hbcimock.data.file"));
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
