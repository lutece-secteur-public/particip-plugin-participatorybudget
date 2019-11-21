package fr.paris.lutece.plugins.campagnebp.business;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * MyAccount
 *
 */
public class MyAccount {


	@NotEmpty( message = "budgetparticipatif.validation.login.notEmpty" )
    private String _strLogin;
	@NotEmpty( message = "budgetparticipatif.validation.password.notEmpty" )
    private String _strPassword;
	@NotEmpty( message = "budgetparticipatif.validation.confirmationPassword.notEmpty" )
    private String _strConfirmationPassword;
	
	public String getLogin() {
		return _strLogin;
	}
	public void setLogin(String _strLogin) {
		this._strLogin = _strLogin;
	}
	public String getPassword() {
		return _strPassword;
	}
	public void setPassword(String _strPassword) {
		this._strPassword = _strPassword;
	}
	public String getConfirmationPassword() {
		return _strConfirmationPassword;
	}
	public void setConfirmationPassword(String _strConfirmationPassword) {
		this._strConfirmationPassword = _strConfirmationPassword;
	}
	
}
