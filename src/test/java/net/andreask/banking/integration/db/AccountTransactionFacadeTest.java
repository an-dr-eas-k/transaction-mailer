package net.andreask.banking.integration.db;

import net.andreask.banking.integration.db.model.AccountTransaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author andreask
 */

@RunWith(MockitoJUnitRunner.class)
public class AccountTransactionFacadeTest{

    @InjectMocks
    AccountTransactionFacade atf;
    @Test
    public void testCreate() throws Exception {

        atf.create(new AccountTransaction().setText("foobar"));


    }
}
