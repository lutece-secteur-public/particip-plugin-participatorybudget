package fr.paris.lutece.plugins.campagnebp.service;


import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.campagnebp.business.MyAccount;
import fr.paris.lutece.plugins.campagnebp.business.MyInfosForm;
import fr.paris.lutece.plugins.mylutece.modules.openam.authentication.OpenamUser;
import fr.paris.lutece.plugins.mylutece.modules.openam.service.OpenamService;
import fr.paris.lutece.plugins.openamidentityclient.business.Account;
import fr.paris.lutece.plugins.openamidentityclient.business.CreateAccountResult;
import fr.paris.lutece.plugins.openamidentityclient.business.Identity;
import fr.paris.lutece.plugins.openamidentityclient.business.SearchListAccountResult;
import fr.paris.lutece.plugins.openamidentityclient.service.OpenamIdentityException;
import fr.paris.lutece.plugins.openamidentityclient.service.OpenamIdentityService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.LocalVariables;

public class AccountService {

	
	/**
	 * return null if no error appear otherwise the error
	 * @param formInfos the formInfos
	 * @param formMyAccount the form Account
	 * @param locale the locale 
	 * @param request the request
	 * @return return null if no error appear otherwise the error
	 */
	public static CreateAccountResult createAccountAndIdentity(MyInfosForm formInfos,
			MyAccount formMyAccount) throws OpenamIdentityException{

		// create account
		CreateAccountResult accountResult;
			// create account
			accountResult = OpenamIdentityService.getService().createAccount(
					formMyAccount.getLogin(), formMyAccount.getPassword());
			try {
			// create identity
			if(accountResult!=null)
			{		
				OpenamIdentityService.getService().createIdentity(
						convertMyInfosFormToIdentity(formInfos,accountResult.getUid()));
			}

		} catch (OpenamIdentityException e) {

			// rall back create account
			try {
				OpenamIdentityService.getService().deleteAccount(
						accountResult.getUid());
			} catch (OpenamIdentityException e1) {
				AppLogService.error(e1);
			}
			throw e;

		}
		// auto login the user
		HttpServletResponse response = LocalVariables.getResponse();
		OpenamService.getInstance().setConnectionCookie(
				accountResult.getTokenId(), (HttpServletResponse) response);
		return accountResult;
	}	
	
	public static String getAccountUidByEmail(String strEmail)
	{
	
		SearchListAccountResult searchListAccountResult=null;
		try {
			searchListAccountResult = OpenamIdentityService.getService(  )
			        .getAccountList( strEmail, null,null, null, null );
		} catch (OpenamIdentityException e) {
			AppLogService.error(e);
		}
		
		
		if ( searchListAccountResult != null && searchListAccountResult.getUsers(  ).size(  ) > 0 )
		{
			return searchListAccountResult.getUsers(  ).get( 0 ).getUid();
		}
		
		return null;
				
	}

	/**
	 * 
	 * @param form
	 * @return
	 */
	private static Identity convertMyInfosFormToIdentity(MyInfosForm form,String strUid) {
		Identity identity = new Identity();
		identity.setUid(strUid);
		identity.setFirstname(form.getFirstname());
		identity.setLastname(form.getLastname());
		identity.setBirthday(form.getBirthdate());
		//identity.setPostalCode(form.getArrondissement());
		//identity.setStreet(form.getCodePostalResidence());

		return identity;

	}
	
	

}
