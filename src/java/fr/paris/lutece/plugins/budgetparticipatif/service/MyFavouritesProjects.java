package fr.paris.lutece.plugins.budgetparticipatif.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.favorite.service.extender.FavoriteResourceExtender;
import fr.paris.lutece.plugins.extend.modules.follow.service.extender.FollowResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.service.RatingAddOnService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.portal.service.security.LuteceUser;

public class MyFavouritesProjects {
	
	 @Inject
	 private IResourceExtenderHistoryService _resourceExtenderHistoryService;
	 
	 public static final String BEAN_NAME = "budgetparticipatif.myFavouritesProjects";

	
	
	public List<Document> getFavouritesProjects( LuteceUser user )
    {
        
		List<ResourceExtenderHistory> listResourceHistory = getFavorisListResourceExtender( user );
        List<Document> listProjects = new ArrayList<Document>( listResourceHistory.size(  ) );

        for ( ResourceExtenderHistory history : listResourceHistory )
        {
            if ( StringUtils.isNumeric( history.getIdExtendableResource(  ) ) )
            {
                int nIdDocument = Integer.parseInt( history.getIdExtendableResource(  ) );
                Document document = DocumentHome.findByPrimaryKeyWithoutBinaries( nIdDocument );
                listProjects.add( document );
            }
        }
        
        return listProjects;
    }
	
	public List<ResourceExtenderHistory> getFavorisListResourceExtender(LuteceUser user)
	{
		
		List<ResourceExtenderHistory> listResourceHistory = new ArrayList<ResourceExtenderHistory>( );
		ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );
        filter.setExtenderType( FavoriteResourceExtender.RESOURCE_EXTENDER );
        filter.setExtendableResourceType( RatingAddOnService.PROPERTY_RESOURCE_TYPE );
        filter.setUserGuid( user.getName(  ) );

        listResourceHistory = _resourceExtenderHistoryService.findByFilter( filter );
        
        return listResourceHistory;
	}
	
	public List<Document> getFollowersProjects( LuteceUser user )
    {
        
		List<ResourceExtenderHistory> listResourceHistory = getFollowerListResourceExtender( user );
        List<Document> listProjects = new ArrayList<Document>( listResourceHistory.size(  ) );

        for ( ResourceExtenderHistory history : listResourceHistory )
        {
            if ( StringUtils.isNumeric( history.getIdExtendableResource(  ) ) )
            {
                int nIdDocument = Integer.parseInt( history.getIdExtendableResource(  ) );
                Document document = DocumentHome.findByPrimaryKeyWithoutBinaries( nIdDocument );
                listProjects.add( document );
            }
        }
        
        return listProjects;
    }
	
	public List<ResourceExtenderHistory> getFollowerListResourceExtender(LuteceUser user)
	{
		
		List<ResourceExtenderHistory> listResourceHistory = new ArrayList<ResourceExtenderHistory>( );
		ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );
        filter.setExtenderType( FollowResourceExtender.RESOURCE_EXTENDER );
        filter.setExtendableResourceType( RatingAddOnService.PROPERTY_RESOURCE_TYPE );
        filter.setUserGuid( user.getName(  ) );

        listResourceHistory = _resourceExtenderHistoryService.findByFilter( filter );
        
        return listResourceHistory;
	}

}
