package fr.paris.lutece.plugins.budgetparticipatif.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

public interface IUserAccessVoteDAO 
{
	void insert( UserAccessVote userVote, Plugin plugin  );
	
	void update( UserAccessVote userVote, Plugin plugin  );
	
	void delete( String strId, Plugin plugin  );
	
	UserAccessVote select( String strId, Plugin plugin  );
}
