package fr.paris.lutece.plugins.budgetparticipatif.service;

import fr.paris.lutece.plugins.budgetparticipatif.business.UserAccessVote;
import fr.paris.lutece.plugins.budgetparticipatif.business.VotePerLocation;

public interface IVoteParArrandissementService 
{
	 VotePerLocation selectVotePerLocation( String strArrd ) ;
	 
	 boolean isUserAccessVote ( String strIdUser ) ;

	 void updateUserAccessVote( UserAccessVote userAccessVote );

	 void insertUserAccessVote( UserAccessVote userAccessVote );
	 
	 void setAccessVote(boolean bool, String userId);
}