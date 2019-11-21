package fr.paris.lutece.plugins.participatorybudget.service;

import fr.paris.lutece.plugins.participatorybudget.business.UserAccessVote;
import fr.paris.lutece.plugins.participatorybudget.business.VotePerLocation;

public interface IVoteParArrandissementService 
{
	 VotePerLocation selectVotePerLocation( String strArrd ) ;
	 
	 boolean isUserAccessVote ( String strIdUser ) ;

	 void updateUserAccessVote( UserAccessVote userAccessVote );

	 void insertUserAccessVote( UserAccessVote userAccessVote );
	 
	 void setAccessVote(boolean bool, String userId);
}