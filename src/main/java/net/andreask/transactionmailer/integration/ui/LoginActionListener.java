package net.andreask.transactionmailer.integration.ui;

import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class LoginActionListener implements ActionListener {

  public void processAction(ActionEvent e)
      throws AbortProcessingException {

    FacesContext fc = FacesContext.getCurrentInstance();
    ELResolver elResolver = fc.getApplication().getELResolver();

    User user = (User) elResolver.getValue(
        fc.getELContext(), null, "user");

    if (!user.getName().equals("andreask")) {
      throw new IllegalAccessError("wrong user");
    }
    System.out.println("logging in ...........");
  }
}