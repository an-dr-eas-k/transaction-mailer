package net.andreask.transactionmailer.integration.hbci;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import net.andreask.transactionmailer.business.Configuration;
import net.andreask.transactionmailer.domain.AccountTransaction;
import net.andreask.transactionmailer.integration.hbci.testmock.HbciMock;

@RunWith(MockitoJUnitRunner.class)
public class HbciMockTest {

  @Mock
  Configuration co;

  @InjectMocks
  HbciMock testObject;

  @Test
  public void testSetAccountConnection() {
    fail("Not yet implemented");
  }

  @Test
  public void testInit() {
    fail("Not yet implemented");
  }

  @Test
  public void testAcquireTransactions() {

    long startTime = System.currentTimeMillis();
    Mockito.when(co.getProperty(anyString())).thenReturn("hbci-mock-data.xml");
    List<AccountTransaction> ts = testObject.acquireTransactions();
    Assert.assertEquals(19, ts.size());
    Assert.assertEquals(6, ts.get(0).getUsage().split("-")[0].length());
    long duration = System.currentTimeMillis() - startTime;
    Assert.assertTrue(duration < 10 * 1000);
  }

  @Test
  public void testAcquireBalance() {
    fail("Not yet implemented");
  }

  @Test
  public void testClose() {
    fail("Not yet implemented");
  }

}
