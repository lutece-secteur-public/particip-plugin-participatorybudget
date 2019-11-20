package fr.paris.lutece.plugins.budgetparticipatif.service;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.budgetparticipatif.util.BudgetUtils;
import fr.paris.lutece.plugins.campagnebp.business.Campagne;
import fr.paris.lutece.plugins.campagnebp.business.CampagneHome;
import fr.paris.lutece.plugins.campagnebp.business.CampagnePhase;
import fr.paris.lutece.plugins.campagnebp.business.CampagnePhaseHome;
import fr.paris.lutece.plugins.campagnebp.service.CampagnesService;
import fr.paris.lutece.plugins.campagnebp.service.IMyInfosListener;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class BudgetMyInfosListener implements IMyInfosListener {

	private MyVoteService _myVoteService = SpringContextService.getBean(MyVoteService.BEAN_NAME);
	
	@Override
	public void updateNickName(String strLuteceUserName, String strNickName) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createNickName(String strLuteceUserName, String strNickName) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int canChangeArrond( LuteceUser user) 
	{
		// Vote arrondissement can be changed only during submission or vote phases, and during vote only is the user has not validated his votes
		
    	boolean isValidated = false;
		if ( user != null )
		{
			isValidated= _myVoteService.isUserVoteValidated( user.getName( ) );
		}

    	if ( CampagnesService.getInstance().isDuring("VOTE") && isValidated)
    	{
    		return -1; // Vote opened but vote already validated
    	}
    	
    	if ( CampagnesService.getInstance().isBeforeBeginning("SUBMIT") || CampagnesService.getInstance().isAfterEnd("VOTE") )
    	{
    		return 2; // Not in submission or vote phase
    	}
    	
    	if ( CampagnesService.getInstance().isDuring("VOTE") )
    	{
    		return 1; // In vote phase, need confirmatinon of votes suppression 
    	}
    	
   		return 0; // In submission phase, basic confirmation 
	}
	
	public String deleteVotes( HttpServletRequest request )
	{
		return _myVoteService.cancelVote(request) ;
	}

}
