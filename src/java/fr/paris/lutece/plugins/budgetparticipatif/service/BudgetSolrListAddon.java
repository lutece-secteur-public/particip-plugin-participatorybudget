package fr.paris.lutece.plugins.budgetparticipatif.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.budgetparticipatif.util.BudgetUtils;
import fr.paris.lutece.plugins.campagnebp.business.Campagne;
import fr.paris.lutece.plugins.campagnebp.business.CampagneHome;
import fr.paris.lutece.plugins.campagnebp.service.CampagnesService;
import fr.paris.lutece.plugins.search.solr.service.ISolrSearchAppAddOn;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class BudgetSolrListAddon implements ISolrSearchAppAddOn {
	
    private static final String MARK_ARRONDISSEMENT_VOTE_USER = "arrondissementVote";
    private static final String PARAMETER_REMOVE_ARR_FILTER = "remove_arr";
    private static final String MARK_REMOVE_ARR_FILTER = "remove_arr";	
    
	@Override
	public void buildPageAddOn(Map<String, Object> model, HttpServletRequest request) {

		MyVoteService _myVoteService = SpringContextService.getBean(MyVoteService.BEAN_NAME);
		String arrondissement= null;
		
	    boolean isValidated= false;
		LuteceUser user = SecurityService.getInstance().getRegisteredUser( request );		
		if ( user != null )
		{
			arrondissement = BudgetUtils.getArrondissementDisplay(user);
			isValidated= _myVoteService.isUserVoteValidated(user.getName( ));
		}
		
		if ( arrondissement != null )
		{			
	        model.put( MARK_ARRONDISSEMENT_VOTE_USER, arrondissement );
		}
		else
		{
	        model.put( MARK_ARRONDISSEMENT_VOTE_USER, "notConnected" );
		}
		
		model.put( BudgetUtils.MARK_VOTE_VALIDATED, isValidated );
		model.put( MARK_REMOVE_ARR_FILTER, request.getParameter( PARAMETER_REMOVE_ARR_FILTER ) );	
		
		model.put( BudgetUtils.MARK_CAMPAGNE_SERVICE, CampagnesService.getInstance() );
	}

}
