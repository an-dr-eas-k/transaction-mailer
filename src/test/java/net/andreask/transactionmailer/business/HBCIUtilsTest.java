package net.andreask.transactionmailer.business;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.kapott.hbci.callback.HBCICallback;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.HBCIPassport;

@RunWith(JUnit4.class)
public class HBCIUtilsTest {

  @Test
  public void testHbciUtils() {
    HBCIUtils.init(null, new HBCICallback() {

      @Override
      public void callback(HBCIPassport arg0, int arg1, String arg2, int arg3, StringBuffer arg4) {
        // TODO Auto-generated method stub

      }

      @Override
      public void log(String arg0, int arg1, Date arg2, StackTraceElement arg3) {
        // TODO Auto-generated method stub

      }

      @Override
      public void status(HBCIPassport arg0, int arg1, Object[] arg2) {
        // TODO Auto-generated method stub

      }

      @Override
      public void status(HBCIPassport arg0, int arg1, Object arg2) {
        // TODO Auto-generated method stub

      }

      @Override
      public boolean useThreadedCallback(HBCIPassport arg0, int arg1, String arg2, int arg3, StringBuffer arg4) {
        // TODO Auto-generated method stub
        return false;
      }

    });
    // HBCIUtils.refreshBLZList(HBCIUtils.class.getClassLoader());
    HBCIUtils.setParam("log.loglevel.default", "5");
    Assert.assertEquals("300", HBCIUtils.getPinTanVersionForBLZ("70090500"));

  }
}
