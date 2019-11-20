package fr.paris.lutece.plugins.budgetparticipatif.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.follow.service.extender.FollowResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.plugins.ideation.business.IdeeHome.GetSubIdeesMethod;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class ProjectService
{
	private static final String BEAN_PROJECT_SERVICE="budgetparticipatif.projectService";
	private static ProjectService _singleton;
	
    @Inject
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;
	
	public static ProjectService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_PROJECT_SERVICE );
        }

        return _singleton;
    }

    // *********************************************************************************************
    // * PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITION *
    // * PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITION *
    // *********************************************************************************************

	/**
	 * Returns id of initial propositions and sub-propositions, as a list of int.
	 */
	public List<Integer> getProjectSubPropositionIds ( Document project, GetSubIdeesMethod method) {
		List<Integer> propIds = null;
		
        DocumentAttribute urlProjectAttr = project.getAttribute("num_idea");
        if ( urlProjectAttr != null ) // Idea of campain "A" and "B" does not exists.
        {
        	String numIdea = urlProjectAttr.getTextValue();
        	if ( numIdea != "" && StringUtils.isNumeric( numIdea ) )
        	{
        		propIds = IdeeHome.getSubIdeesId( Integer.parseInt(numIdea) , IdeeHome.GetSubIdeesMethod.ALL_FAMILY );
        	}
        	else
        	{
        		AppLogService.error("ERROR : The value of 'num_idea' of document ¤'" + project.getId() + "' is not a number : '" + numIdea + "' !" );
        	}
        }    

        if ( propIds == null )
        {
            propIds = new ArrayList<Integer>();
        }
        
        return propIds;
	}
	
    // *********************************************************************************************
    // * FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLO *
    // * FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLO *
    // *********************************************************************************************

    /**
     * Returns a Set containing guid of followers of a document project.
     */
    public Set<String> getUniqueUserGuidsProjectFollowers ( Document project ) {
    	Set<String> userGuids = new HashSet<String>();

        ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter( );
        
        filter.setExtenderType          ( FollowResourceExtender.RESOURCE_EXTENDER );
        filter.setExtendableResourceType( "document"                               );
        filter.setIdExtendableResource  ( String.valueOf( project.getId( ) )      );

        List<ResourceExtenderHistory> listHistories = _resourceExtenderHistoryService.findByFilter( filter );

        for ( ResourceExtenderHistory followerHistory : listHistories )
        {
       		userGuids.add( followerHistory.getUserGuid() );
        }

        return userGuids;
    }
    
}