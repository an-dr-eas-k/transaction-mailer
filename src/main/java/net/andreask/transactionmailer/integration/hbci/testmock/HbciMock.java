package net.andreask.transactionmailer.integration.hbci.testmock;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import net.andreask.transactionmailer.business.Configuration;
import net.andreask.transactionmailer.domain.AccountTransaction;
import net.andreask.transactionmailer.integration.Development;
import net.andreask.transactionmailer.integration.hbci.HbciAccess;
import net.andreask.transactionmailer.integration.hbci.HbciFacade;

@Development
public class HbciMock implements HbciFacade {

  @Inject
  private Configuration configuration;

  private Logger logger = LogManager.getLogger(this.getClass());

  public HbciMock() {

    logger.warn("using development mock");
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
    try {
      Thread.sleep((long) Math.ceil(Math.random() * 10 * 1000));
    } catch (Exception e) {
    }
    List<AccountTransaction> result = ((List<AccountTransaction>) xStream
        .fromXML(new File(configuration.getProperty("hbcimock.data.file"))))
            .stream()
            .map(at -> {
              at.setUsage(String.format("%06.0f-%s", (Math.random() * 1000000), at.getUsage()));
              return at;
            })
            .collect(Collectors.toList());

    LogManager.getLogger(this.getClass()).info("found {} transactions", result.size());
    return result;
  }

  @Override
  public long acquireBalance() {
    return 987l;
  }

  @Override
  public void close() {

  }

}
