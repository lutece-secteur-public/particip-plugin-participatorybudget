/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.participatorybudget.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.participatorybudget.service.MyFavouritesProjects;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.plugins.participatorybudget.service.vote.MyVoteService;
import fr.paris.lutece.plugins.participatorybudget.util.BudgetUtils;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;

/**
 * This class provides the user interface to view Proposal xpages
 */

@Controller( xpageName = "myFavourites", pageTitleI18nKey = "participatorybudget.xpage.projectsFavourite.pageTitle", pagePathI18nKey = "participatorybudget.xpage.projectsFavourite.pagePathLabel" )
public class FavouritesProjectsXPage extends MVCApplication
{
    /**
     *
     */
    private static final long serialVersionUID = 2703580251118435168L;

    private static final String TEMPLATE_VIEW_FAVOURITE_PROJECT = "/skin/plugins/participatorybudget/view_favourites_projects.ftl";

    // Markers

    private static final String MARK_PROJECTS_FAVOURITE = "favouriteProjects";
    private static final String MARK_PROJECTS_FOLLOWERS = "followerProjects";
    private static final String MARK_USER_ARRONDISSEMENT = "userArrondissement";
    private static final String MARK_ARRONDISSEMENT = "arrondissement";

    // Properties

    // Views

    private static final String VIEW_FAVOURITES_PROJECTS = "myFavourites";

    // Actions

    private MyFavouritesProjects _myFavouriteService = SpringContextService.getBean( MyFavouritesProjects.BEAN_NAME );
    private MyVoteService _myVoteService = SpringContextService.getBean( MyVoteService.BEAN_NAME );

    /**
     * Returns the favourite projects for user
     *
     * @param request
     *            The Http request
     * @return The HT
     */

    @View( value = VIEW_FAVOURITES_PROJECTS, defaultView = true )
    public XPage getFavouritesProject( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = checkUserAuthorized( request );
        List<Document> listFavouris = _myFavouriteService.getFavouritesProjects( user );
        List<Document> listFollowers = _myFavouriteService.getFollowersProjects( user );
        String arrondissement = BudgetUtils.getArrondissementDisplay( user );
        boolean isValidated = _myVoteService.isUserVoteValidated( user.getName( ) );

        Map<String, Object> model = getModel( );
        model.put( MARK_USER_ARRONDISSEMENT, arrondissement );
        model.put( MARK_PROJECTS_FAVOURITE, listFavouris );
        model.put( MARK_PROJECTS_FOLLOWERS, listFollowers );
        model.put( BudgetUtils.MARK_VOTE_VALIDATED, isValidated );

        model.put( BudgetUtils.MARK_CAMPAIGN_SERVICE, CampaignService.getInstance( ) );

        return getXPage( TEMPLATE_VIEW_FAVOURITE_PROJECT, request.getLocale( ), model );
    }

    /**
     * Check User
     * 
     * @param request
     * @return
     * @throws UserNotSignedException
     */
    private LuteceUser checkUserAuthorized( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = null;
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            user = SecurityService.getInstance( ).getRemoteUser( request );
            if ( user == null )
            {
                throw new UserNotSignedException( );
            }
        }
        return user;
    }

}
