package net.andreask.transactionmailer.integration.ui;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "user")
@SessionScoped
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  private String name;
  private String password;

  public String getName() {
    return name;
  }

  public void setName(String newValue) {
    name = newValue;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String newValue) {
    password = newValue;
  }

  public String login() {
    return "/view/accountList";
  }

  public String logout() {
    name = password = null;
    return "/views/login";
  }

  public void validateName(ValueChangeEvent vce) {
  }
}
