package net.andreask.transactionmailer.integration.ui;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

@ManagedBean
@SessionScoped
public class User {
  private String name;
  private String password, nameError;

  public String getName() {
    return name;
  }

  public void setName(String newValue) {
    name = newValue;
  }

  public void setNameError(String error) {
    nameError = error;
  }

  public String getNameError() {
    return nameError;
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
    name = password = nameError = null;
    return "/views/login";
  }

  public void validateName(ValueChangeEvent vce) {
  }
}
