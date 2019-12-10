package fr.paris.lutece.plugins.participatorybudget.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.paris.lutece.plugins.participatorybudget.business.MyInfosForm;
import fr.paris.lutece.plugins.participatorybudget.service.MyInfosService;
import fr.paris.lutece.portal.service.prefs.IPortalUserPreferencesService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.security.LuteceUser;

public class BudgetUtils {

	private static final String PREF_KEY_ARRONDISSEMENT_VOTE = "participatorybudget.arrondissement";
	
	public  static final String ARRONDISSEMENT_USER = "arrondUser";

	public  static final String MARK_VOTE_VALIDATED   = "voteValidated";
	public  static final String MARK_CAMPAGNE_SERVICE = "campagneService";
	
	/**
	 * Get arrondissement for display
	 * 
	 * @param user
	 * @return string for arrondissement display
	 */
	public static String getArrondissementDisplay( LuteceUser user ) 
	{
		MyInfosForm myInfo = MyInfosService.loadUserInfos(user);
		return myInfo.getArrondissement();
	}
	
	/**
	 * Get arrondissement from user preference
	 * 
	 * @param strLuteceUserName
	 * @return arrondissement from user preference
	 */
	public static String getArrondissement( String strLuteceUserName )
	{
		IPortalUserPreferencesService instance = UserPreferencesService.instance();
		return instance.get( strLuteceUserName, PREF_KEY_ARRONDISSEMENT_VOTE, "" );
	}

	/**
	 * Set arrondissement
	 * 
	 * @param strLuteceUserName
	 * @param arrondissement
	 * @return
	 */
	public static void setArrondissement( String strLuteceUserName, String arrondissement ) 
	{
		IPortalUserPreferencesService instance = UserPreferencesService.instance();
		instance.put( strLuteceUserName, PREF_KEY_ARRONDISSEMENT_VOTE, arrondissement );
	}
}
