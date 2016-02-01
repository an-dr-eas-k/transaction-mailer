package net.andreask.transactionmailer.integration.hbci.hbci4java;

import org.kapott.hbci.passport.HBCIPassportPinTan;

/**
 * @author aki
 */
public class HBCIPassportNonPersistentPinTan extends HBCIPassportPinTan {

    public HBCIPassportNonPersistentPinTan(Object initObject) {
        super(initObject);
        askForMissingData(true, true, true, true, true, true, true);
    }

    @Override
    public void saveChanges() {
        System.gc();
    }
}
