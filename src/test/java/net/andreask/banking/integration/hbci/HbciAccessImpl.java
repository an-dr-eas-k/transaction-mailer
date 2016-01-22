
package net.andreask.banking.integration.hbci;

import net.andreask.banking.business.Encryptor;
import net.andreask.banking.model.AccountConnection;

public class HbciAccessImpl extends AccountConnection <HbciAccessImpl> implements HbciAccess{

    @Override
    public String getPin() {
        return Encryptor.decrypt("RandomInitVector", "Bar12345Bar12345", super.getEncryptedPin());
    }
}
