package net.andreask.banking.integration.db.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TRANSACTION", schema = "HV")
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * Datum der Wertstellung
     */
    public Date valuta;
    /**
     * Buchungsdatum
     */
    public Date bdate;

    /**
     * Gebuchter Betrag
     */
    public long value;

    /**
     * Handelt es sich um eine Storno-Buchung?
     */
    public boolean isStorno;

    /**
     * Der Saldo <em>nach</em> dem Buchen des Betrages <code>value</code>
     */
    public long saldo;

    /**
     * Kundenreferenz
     */
    public String customerref;

    /**
     * Kreditinstituts-Referenz
     */
    public String instref;

    /**
     * Betrag für Gebühren des Geldverkehrs (optional)
     */
    public long charge_value;

    /**
     * Art der Buchung (bankinterner Code). Nur wenn hier ein Wert ungleich
     * <code>999</code> drinsteht, enthalten die Attribute <code>text</code>,
     * <code>primanota</code>, <code>usage</code>, <code>other</code> und
     * <code>addkey</code> sinnvolle Werte. Andernfalls sind all diese
     * Informationen möglicherweise im Feld <code>additional</code> enthalten,
     * allerdings in einem nicht definierten Format (siehe auch
     * <code>additional</code>).
     */
    public String gvcode;

    /**
     * <p>Zusatzinformationen im Rohformat. Wenn Zusatzinformationen zu dieser
     * Transaktion in einem unbekannten Format vorliegen, dann enthält dieser
     * String diese Daten (u.U. ist dieser String leer, aber nicht <code>null</code>).
     * Das ist genau dann der Fall, wenn der Wert von  <code>gvcode</code> gleich <code>999</code> ist.</p>
     * <p>Wenn die Zusatzinformationen aber ausgewertet werden können (und <code>gvcode!=999</code>),
     * so ist dieser String <code>null</code>, und die Felder <code>text</code>, <code>primanota</code>,
     * <code>usage</code>, <code>other</code> und <code>addkey</code>
     * enthalten die entsprechenden Werte (siehe auch <code>gvcode</code>)</p>
     */
    public String additional;

    /**
     * Beschreibung der Art der Buchung (optional).
     * Nur wenn <code>gvcode!=999</code>! (siehe auch <code>additional</code>
     * und <code>gvcode</code>)
     */
    public String text;
    /**
     * Primanotakennzeichen (optional).
     * Nur wenn <code>gvcode!=999</code>! (siehe auch <code>additional</code>
     * und <code>gvcode</code>)
     */
    public String primanota;
    /**
     * Liste von Strings mit den Verwendungszweckzeilen.
     * Nur wenn <code>gvcode!=999</code>! (siehe auch <code>additional</code>
     * und <code>gvcode</code>)
     */
    public String usage;

    /**
     * Erweiterte Informationen zur Art der Buchung (bankintern, optional).
     * Nur wenn <code>gvcode!=999</code>! (siehe auch <code>additional</code>
     * und <code>gvcode</code>)
     */
    public String addkey;

    /**
     * Gibt an, ob ein Umsatz ein SEPA-Umsatz ist
     **/
    public boolean isSepa;

    public int getId() {
        return this.id;
    }

    public AccountTransaction setId(int id) {
        this.id = id;
        return this;
    }

    public Date getValuta() {
        return this.valuta;
    }

    public AccountTransaction setValuta(Date valuta) {
        this.valuta = valuta;
        return this;
    }

    public Date getBdate() {
        return this.bdate;
    }

    public AccountTransaction setBdate(Date bdate) {
        this.bdate = bdate;
        return this;
    }

    public long getValue() {
        return this.value;
    }

    public AccountTransaction setValue(long value) {
        this.value = value;
        return this;
    }

    public boolean getIsStorno() {
        return this.isStorno;
    }

    public AccountTransaction setIsStorno(boolean isStorno) {
        this.isStorno = isStorno;
        return this;
    }

    public long getSaldo() {
        return this.saldo;
    }

    public AccountTransaction setSaldo(long saldo) {
        this.saldo = saldo;
        return this;
    }

    public String getCustomerref() {
        return this.customerref;
    }

    public AccountTransaction setCustomerref(String customerref) {
        this.customerref = customerref;
        return this;
    }

    public String getInstref() {
        return this.instref;
    }

    public AccountTransaction setInstref(String instref) {
        this.instref = instref;
        return this;
    }

    public long getCharge_value() {
        return this.charge_value;
    }

    public AccountTransaction setCharge_value(long charge_value) {
        this.charge_value = charge_value;
        return this;
    }

    public String getGvcode() {
        return this.gvcode;
    }

    public AccountTransaction setGvcode(String gvcode) {
        this.gvcode = gvcode;
        return this;
    }

    public String getAdditional() {
        return this.additional;
    }

    public AccountTransaction setAdditional(String additional) {
        this.additional = additional;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public AccountTransaction setText(String text) {
        this.text = text;
        return this;
    }

    public String getPrimanota() {
        return this.primanota;
    }

    public AccountTransaction setPrimanota(String primanota) {
        this.primanota = primanota;
        return this;
    }

    public String getUsage() {
        return this.usage;
    }

    public AccountTransaction setUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public String getAddkey() {
        return this.addkey;
    }

    public AccountTransaction setAddkey(String addkey) {
        this.addkey = addkey;
        return this;
    }

    public boolean getIsSepa() {
        return this.isSepa;
    }

    public AccountTransaction setIsSepa(boolean isSepa) {
        this.isSepa = isSepa;
        return this;
    }
}
