package net.andreask.banking;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import net.andreask.banking.integration.hbci.HbciFacade;
import net.andreask.banking.integration.hbci.hbci4java.HbciSession;
import net.andreask.banking.model.AccountConnection;
import net.andreask.banking.model.AccountTransaction;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.List;
import java.util.Properties;


@Named
@RequestScoped
@Alternative
public class HbciFacadeTestAlternative implements HbciFacade {


    public AccountConnection getAccountConnection() {
        return null;
    }

    public HbciFacadeTestAlternative setAccountConnection(AccountConnection accountConnection) {

        return this;
    }

    public HbciFacadeTestAlternative init() {

        return this;
    }

    @Inject
    Properties testProperties = App.provideProperties();

    public List<AccountTransaction> acquireTransactions() {
        LogManager.getLogger(HbciFacadeTestAlternative.class).info("acquireTransactions Alternative");
        XStream xStream = new XStream(new StaxDriver());
        //noinspection unchecked
        return (List<AccountTransaction>) xStream.fromXML(new File(testProperties.getProperty("hbcimock.data.file")));

    }

    public long acquireBalance() {
        return 100;
    }
}
