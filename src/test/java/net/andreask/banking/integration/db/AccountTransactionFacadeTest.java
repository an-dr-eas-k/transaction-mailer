package net.andreask.banking.integration.db;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import net.andreask.banking.integration.db.model.AccountTransactionDE;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author andreask
 */

@RunWith(Arquillian.class)
public class AccountTransactionFacadeTest {
    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive ja = ShrinkWrap.create(WebArchive.class)
                .addPackage(AccountTransactionFacade.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(ja.toString(true));
        return ja;
    }

    @PersistenceContext
    EntityManager em;

    @Inject
    AccountTransactionFacade atf;

    @Inject
    UserTransaction utx;
    private static final String[] TRANSACTION_TEXTS = {
            "Super Mario Brothers",
            "Mario Kart",
            "F-Zero"
    };

    @Test
    public void testCreate() throws Exception {
        utx.begin();
        atf.create(new AccountTransactionDE().setText("foobar"));
        utx.commit();
    }

    @Test
    public void checkDBContent() {
        System.out.println(atf
                .findAllDE()
                .stream()
                .map(AccountTransactionDE::getText)
                .reduce((a, b) -> a.concat("\n").concat(b)));
    }

    @Before
    public void preparePersistenceTest() throws Exception {
        clearData();
        insertData();

    }

    private void clearData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from AccountTransactionDE").executeUpdate();
        utx.commit();
    }

    private void insertData() throws Exception {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : TRANSACTION_TEXTS) {
            AccountTransactionDE game = new AccountTransactionDE().setText(title);
            em.persist(game);
        }
        utx.commit();
        // clear the persistence context (first-level cache)
        em.clear();
    }

}
