package fr.paris.lutece.plugins.budgetparticipatif.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class UserAccessVoteDAO implements IUserAccessVoteDAO
{
	// Constants
    private static final String SQL_QUERY_INSERT = "INSERT INTO budgetparticipatif_user_access_vote ( id_user, has_acces_vote ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE budgetparticipatif_user_access_vote SET has_acces_vote = ? WHERE id_user = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM budgetparticipatif_user_access_vote WHERE id_user = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_user, has_acces_vote FROM budgetparticipatif_user_access_vote";
    private static final String SQl_QUERY_SELECT = SQL_QUERY_SELECTALL + " where id_user= ?";
    
	@Override
	public void insert( UserAccessVote userVote, Plugin plugin ) 
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
		daoUtil.setString( 1, userVote.getId( ) );
	    daoUtil.setBoolean( 2, userVote.isHasAccessVote() );
	      	        
	    daoUtil.executeUpdate(  );
	    daoUtil.free(  );
		
	}

	@Override
	public void update( UserAccessVote userVote, Plugin plugin  ) 
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
		
	    daoUtil.setBoolean( 1, userVote.isHasAccessVote() );
	    daoUtil.setString( 2, userVote.getId( ) );
	      	        
	    daoUtil.executeUpdate(  );
	    daoUtil.free(  );
	}

	@Override
	public void delete( String strUserId, Plugin plugin  ) 
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setString( 1, strUserId );
        
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
	}

	@Override
	public UserAccessVote select( String strUserId, Plugin plugin  ) 
	{
		UserAccessVote userAccessVote= null;
		DAOUtil daoUtil = new DAOUtil( SQl_QUERY_SELECT, plugin );
		daoUtil.setString( 1, strUserId );
		daoUtil.executeQuery(  );

	        if ( daoUtil.next(  ) )
	        {
	        	userAccessVote = new UserAccessVote();
	        	userAccessVote.setId( daoUtil.getString( 1 ) );
	        	userAccessVote.setHasAccessVote( daoUtil.getBoolean( 2 ) );
	           
	        }
	        daoUtil.free(  );
	        return userAccessVote;
	}

}
