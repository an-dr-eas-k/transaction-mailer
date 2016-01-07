package net.andreask.banking.integration.db;

import net.andreask.banking.integration.db.model.AccountTransaction;
import org.jboss.arquillian.container.test.api.Deployment;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author andreask
 */

@RunWith(Arquillian.class)
public class AccountTransactionFacadeTest {
    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive ja = ShrinkWrap.create(JavaArchive.class)
                .addPackage(AccountTransactionFacade.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(ja.toString(true));
        return ja;
    }

    @PersistenceContext
    EntityManager em;

    @Test
    public void should_create_greeting() {
        Assert.fail("Not yet implemented");
    }

    @Inject
    AccountTransactionFacade atf;

    @Test
    public void testCreate() throws Exception {

        atf.create(new AccountTransaction().setText("foobar"));


    }
}
