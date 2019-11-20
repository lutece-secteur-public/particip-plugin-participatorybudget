package fr.paris.lutece.plugins.budgetparticipatif.service;

import fr.paris.lutece.plugins.budgetparticipatif.business.UserAccessVote;
import fr.paris.lutece.plugins.budgetparticipatif.business.UserAccessVoteHome;
import fr.paris.lutece.plugins.budgetparticipatif.business.VotePerLocation;
import fr.paris.lutece.plugins.budgetparticipatif.business.VotePerLocationHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class VoteParArrandissementService implements IVoteParArrandissementService

{
	private static final String BEAN_VOTE_PAR_ARRAND_SERVICE="budgetparticipatif.voteParArrandService";
	private static IVoteParArrandissementService _singleton;
	
	
	public static IVoteParArrandissementService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_VOTE_PAR_ARRAND_SERVICE );
           
          }

        return _singleton;
    }

	@Override
	public VotePerLocation selectVotePerLocation( String strArrd ) 
	{
		return VotePerLocationHome.selectVotePerLocation( strArrd );
	}

	@Override
	public boolean isUserAccessVote( String strIdUser )
	{
		if(UserAccessVoteHome.selectUserAccessVote( strIdUser )!=null){
			
			return UserAccessVoteHome.selectUserAccessVote( strIdUser ).isHasAccessVote();
		}
				
		return false;
				
	}
	
	@Override
	public void updateUserAccessVote( UserAccessVote userAccessVote )
	{
		UserAccessVoteHome.updateUserAccessVote( userAccessVote );
	}
	
	@Override
	public void insertUserAccessVote( UserAccessVote userAccessVote )
	{
		UserAccessVoteHome.insertUserAccessVote( userAccessVote );
	}

	@Override
	public void setAccessVote(boolean bool, String userId) {
	
		UserAccessVote user= new UserAccessVote();
		user.setHasAccessVote(bool);
		user.setId(userId);
		
		insertUserAccessVote(user);
	}
	
	

}



