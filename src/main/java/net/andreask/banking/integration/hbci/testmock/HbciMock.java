package net.andreask.banking.integration.hbci.testmock;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import net.andreask.banking.integration.Development;
import net.andreask.banking.integration.hbci.HbciAccess;
import net.andreask.banking.integration.hbci.HbciFacade;
import net.andreask.banking.model.AccountTransaction;

@Development
public class HbciMock implements HbciFacade {
  private Properties testProperties;

  public HbciMock() {
    this.testProperties = provideProperties();
  }

  public Properties provideProperties() {
    try {
      Properties properties = new Properties();
      properties.load(this.getClass().getClassLoader().getResourceAsStream("hv.properties"));
      return properties;
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  public HbciFacade setAccountConnection(HbciAccess accountConnection) {
    return this;
  }

  @Override
  public HbciFacade init() {
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<AccountTransaction> acquireTransactions() {
    LogManager.getLogger(this.getClass()).info("acquireTransactions Alternative");
    XStream xStream = new XStream(new StaxDriver());
    return (List<AccountTransaction>) xStream.fromXML(new File(testProperties.getProperty("hbcimock.data.file")));
  }

  @Override
  public long acquireBalance() {
    return 987l;
  }

}
