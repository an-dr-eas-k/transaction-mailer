package net.andreask.banking.business;

import java.io.StringWriter;

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

public class XMapper {

  static private XStream xStream = new XStream(new StaxDriver());

  public static String toXml(Object o) {
    return toXml(o, false);
  }

  public static String toXml(Object o, boolean formated) {
    if (o == null) {
      return null;
    }
    if (formated) {
      Document doc = new DocumentImpl();
      xStream.marshal(o, new DomWriter(doc));
      return format(doc);
    } else {
      return xStream.toXML(o);
    }
  }

  public static String format(Document input) {
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

  @SuppressWarnings("unchecked")
  public static <T> T fromXml(String other, Class<T> type) {
    if (other == null) {
      return null;
    }
    return (T) xStream.fromXML(other);
  }
}
