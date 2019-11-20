package fr.paris.lutece.plugins.budgetparticipatif.business;

import fr.paris.lutece.plugins.budgetparticipatif.Constants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides instances management methods (create, find, ...) for Vote objects
 */
public class UserAccessVoteHome 

{
	
	 // Static variable pointed at the DAO instance
    private static IUserAccessVoteDAO _dao = (IUserAccessVoteDAO) SpringContextService.getBean( "budgetparticipatif.userAccessVoteDAO" );
    private static Plugin _plugin = PluginService.getPlugin( Constants.PLUGIN_NAME );
    
    /**
     * Private constructor - this class need not be instantiated
     */
    private UserAccessVoteHome(  )
    {
    }
    
    /**
     * Insert User access vote
     * @param userAccess
     */
    public static void insertUserAccessVote( UserAccessVote userAccess )
    {
    	_dao.insert( userAccess, _plugin) ;
    }
    /**
     * Update user accesss vote
     * @param userAccess the UserAccessVote
     */
    public static void updateUserAccessVote( UserAccessVote userAccess )
    {
    	_dao.update( userAccess, _plugin) ;
    }
    
    /**
     * Delete a user access
     * @param strIdUser the Id user
     */
    public static void deleteUserAccessVote( String strIdUser )
    {
    	_dao.delete( strIdUser, _plugin) ;
    }
    
    /**
     * Select a User access vote
     * @param strIdUser the Id user access
     */
    public static UserAccessVote selectUserAccessVote( String strIdUser )
    {
    	return _dao.select( strIdUser , _plugin) ; 
    }
    
}
