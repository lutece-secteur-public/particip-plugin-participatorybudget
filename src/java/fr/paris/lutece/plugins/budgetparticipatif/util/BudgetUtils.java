package fr.paris.lutece.plugins.budgetparticipatif.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.paris.lutece.plugins.campagnebp.business.MyInfosForm;
import fr.paris.lutece.plugins.campagnebp.service.MyInfosService;
import fr.paris.lutece.portal.service.prefs.IPortalUserPreferencesService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.security.LuteceUser;

public class BudgetUtils {

	private static final String PREF_KEY_ARRONDISSEMENT_VOTE = "budgetparticipatif.arrondissement";
	
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
		String strArrondissement = "";
		
		MyInfosForm myInfo = MyInfosService.loadUserInfos(user);
		Pattern p = Pattern.compile("75(020|00[1-9]|116|01[0-9])$");
		Matcher m = p.matcher(myInfo.getArrondissement());
		if ( m.matches() )
			strArrondissement = Integer.valueOf(myInfo.getArrondissement().substring(2)) + "e arrondissement";
		return strArrondissement;
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
