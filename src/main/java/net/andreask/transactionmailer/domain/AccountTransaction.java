package net.andreask.transactionmailer.domain;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.inject.Model;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Model
@Entity
@NamedQuery(
    name = "findFromTemplate",
    query = "SELECT at FROM AccountTransaction at WHERE at.usage = :usage AND at.value = :value")
public class AccountTransaction implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  private AccountConnection accountConnection;

  /**
   * Datum der Wertstellung
   */
  private Date valuta;
  /**
   * Buchungsdatum
   */
  private Date bdate;

  /**
   * Gebuchter Betrag
   */
  private long value;

  /**
   * Handelt es sich um eine Storno-Buchung?
   */
  private boolean isStorno;

  /**
   * Der Saldo <em>nach</em> dem Buchen des Betrages <code>value</code>
   */
  private long saldo;

  /**
   * Kundenreferenz
   */
  private String customerref;

  /**
   * Kreditinstituts-Referenz
   */
  private String instref;

  /**
   * Betrag für Gebühren des Geldverkehrs (optional)
   */
  private long charge_value;

  /**
   * Art der Buchung (bankinterner Code). Nur wenn hier ein Wert ungleich
   * <code>999</code> drinsteht, enthalten die Attribute <code>text</code>,
   * <code>primanota</code>, <code>usage</code>, <code>other</code> und
   * <code>addkey</code> sinnvolle Werte. Andernfalls sind all diese
   * Informationen möglicherweise im Feld <code>additional</code> enthalten,
   * allerdings in einem nicht definierten Format (siehe auch
   * <code>additional</code>).
   */
  private String gvcode;

  /**
   * <p>
   * Zusatzinformationen im Rohformat. Wenn Zusatzinformationen zu dieser
   * Transaktion in einem unbekannten Format vorliegen, dann enthält dieser
   * String diese Daten (u.U. ist dieser String leer, aber nicht
   * <code>null</code>). Das ist genau dann der Fall, wenn der Wert von
   * <code>gvcode</code> gleich <code>999</code> ist.
   * </p>
   * <p>
   * Wenn die Zusatzinformationen aber ausgewertet werden können (und
   * <code>gvcode!=999</code>), so ist dieser String <code>null</code>, und
   * die Felder <code>text</code>, <code>primanota</code>, <code>usage</code>,
   * <code>other</code> und <code>addkey</code> enthalten die entsprechenden
   * Werte (siehe auch <code>gvcode</code>)
   * </p>
   */
  private String additional;

  /**
   * Beschreibung der Art der Buchung (optional). Nur wenn
   * <code>gvcode!=999</code>! (siehe auch <code>additional</code> und
   * <code>gvcode</code>)
   */
  private String text;
  /**
   * Primanotakennzeichen (optional). Nur wenn <code>gvcode!=999</code>!
   * (siehe auch <code>additional</code> und <code>gvcode</code>)
   */
  private String primanota;
  /**
   * Liste von Strings mit den Verwendungszweckzeilen. Nur wenn
   * <code>gvcode!=999</code>! (siehe auch <code>additional</code> und
   * <code>gvcode</code>)
   */
  private String usage;

  /**
   * Erweiterte Informationen zur Art der Buchung (bankintern, optional). Nur
   * wenn <code>gvcode!=999</code>! (siehe auch <code>additional</code> und
   * <code>gvcode</code>)
   */
  private String addkey;

  /**
   * Gibt an, ob ein Umsatz ein SEPA-Umsatz ist
   **/
  private boolean isSepa;
  private Konto other;

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

  public AccountTransaction setOther(Konto other) {
    this.other = other;
    return this;
  }

  public Konto getOther() {
    return other;
  }

  public AccountConnection getAccountConnection() {
    return accountConnection;
  }

  public AccountTransaction setAccountConnection(AccountConnection accountConnection) {
    this.accountConnection = accountConnection;
    return this;
  }

  @Override
  public String toString() {
    return "AccountTransaction [id=" + id + ", accountConnection=" + accountConnection + ", valuta=" + valuta
        + ", bdate=" + bdate + ", value=" + value + ", isStorno=" + isStorno + ", saldo=" + saldo + ", customerref="
        + customerref + ", instref=" + instref + ", charge_value=" + charge_value + ", gvcode=" + gvcode
        + ", additional=" + additional + ", text=" + text + ", primanota=" + primanota + ", usage=" + usage
        + ", addkey=" + addkey + ", isSepa=" + isSepa + ", other=" + other + "]";
  }

  /**
   * Created by andreask on 1/18/16.
   */
  public static class Konto implements Serializable {
    private String country;
    private String blz;
    private String number;
    private String subnumber;
    private String acctype;
    private String type;
    private String curr;
    private String customerid;
    private String name;
    private String name2;
    private String bic;
    private String iban;

    public String getCountry() {
      return this.country;
    }

    public Konto setCountry(String country) {
      this.country = country;
      return this;
    }

    public String getBlz() {
      return this.blz;
    }

    public Konto setBlz(String blz) {
      this.blz = blz;
      return this;
    }

    public String getNumber() {
      return this.number;
    }

    public Konto setNumber(String number) {
      this.number = number;
      return this;
    }

    public String getSubnumber() {
      return this.subnumber;
    }

    public Konto setSubnumber(String subnumber) {
      this.subnumber = subnumber;
      return this;
    }

    public String getAcctype() {
      return this.acctype;
    }

    public Konto setAcctype(String acctype) {
      this.acctype = acctype;
      return this;
    }

    public String getType() {
      return this.type;
    }

    public Konto setType(String type) {
      this.type = type;
      return this;
    }

    public String getCurr() {
      return this.curr;
    }

    public Konto setCurr(String curr) {
      this.curr = curr;
      return this;
    }

    public String getCustomerid() {
      return this.customerid;
    }

    public Konto setCustomerid(String customerid) {
      this.customerid = customerid;
      return this;
    }

    public String getName() {
      return this.name;
    }

    public Konto setName(String name) {
      this.name = name;
      return this;
    }

    public String getName2() {
      return this.name2;
    }

    public Konto setName2(String name2) {
      this.name2 = name2;
      return this;
    }

    public String getBic() {
      return this.bic;
    }

    public Konto setBic(String bic) {
      this.bic = bic;
      return this;
    }

    public String getIban() {
      return this.iban;
    }

    public Konto setIban(String iban) {
      this.iban = iban;
      return this;
    }

    @Override
    public String toString() {
      return "Konto [country=" + country + ", blz=" + blz + ", number=" + number + ", subnumber=" + subnumber
          + ", acctype=" + acctype + ", type=" + type + ", curr=" + curr + ", customerid=" + customerid + ", name="
          + name + ", name2=" + name2 + ", bic=" + bic + ", iban=" + iban + "]";
    }

  }
}
