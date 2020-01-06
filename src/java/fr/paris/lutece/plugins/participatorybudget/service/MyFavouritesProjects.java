/*
 * Copyright (c) 2002-2020, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.participatorybudget.service;

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
	 
	 public static final String BEAN_NAME = "participatorybudget.myFavouritesProjects";

	
	
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
