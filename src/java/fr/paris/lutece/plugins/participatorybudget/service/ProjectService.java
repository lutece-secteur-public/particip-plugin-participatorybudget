package fr.paris.lutece.plugins.participatorybudget.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.follow.service.extender.FollowResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class ProjectService
{
	private static final String BEAN_PROJECT_SERVICE="participatorybudget.projectService";
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