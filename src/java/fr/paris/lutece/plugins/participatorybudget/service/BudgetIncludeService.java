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
package fr.paris.lutece.plugins.participatorybudget.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.participatorybudget.business.MyInfosForm;
import fr.paris.lutece.plugins.participatorybudget.business.vote.VoteHome;
import fr.paris.lutece.plugins.participatorybudget.service.vote.IVoteParArrandissementService;
import fr.paris.lutece.plugins.participatorybudget.service.vote.MyVoteService;
import fr.paris.lutece.plugins.participatorybudget.service.vote.VoteParArrandissementService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * BudgetIncludeService
 *
 */
public class BudgetIncludeService
{

    // Template
    private static final String TEMPLATE_HEADER_INCLUDE = "skin/plugins/participatorybudget/header_include.html";

    // Properties

    // Mark

    private static final String MARK_HEADER_COLOR = "header_color";
    private static final String MARK_USER_VOTES_PARIS = "user_votes_paris";
    private static final String MARK_USER_VOTES_ARRDT = "user_votes_arrdt";
    private static final String MARK_NUMBER_FAVOURITES_PROJECT = "numberFavouritesProject";
    private static final String MARK_INFOS_USER = "infos_user";
    private static final String MARK_MAX_VOTES_ARROND = "maxVotesArrond";
    private static final String MARK_MAX_VOTES_TOUT_PARIS = "maxVotesToutParis";
    private static final String MARK_TOUT_PARIS = "whole_city";

    private static final String CLASS_CSS_OUT = "logged-out";
    private static final String CLASS_CSS_IN = "logged-in";

    private static MyFavouritesProjects _myFavouriteService = SpringContextService.getBean( MyFavouritesProjects.BEAN_NAME );
    private static IVoteParArrandissementService _nbrVoteService = VoteParArrandissementService.getInstance( );

    /**
     * 
     * @param request
     * @param myVoteService
     * @return
     */
    public static String getHeaderTemplate( HttpServletRequest request, MyVoteService myVoteService )
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        Locale locale = ( request == null ) ? null : request.getLocale( );

        LuteceUser user = ( request != null ) ? SecurityService.getInstance( ).getRegisteredUser( request ) : null;
        int nMaxVotesArrond = 0, nMaxVotesToutParis = 0;

        model.put( MARK_HEADER_COLOR, CLASS_CSS_OUT );

        if ( user != null )
        {

            model.put( MARK_HEADER_COLOR, CLASS_CSS_IN );
            model.put( MARK_NUMBER_FAVOURITES_PROJECT, _myFavouriteService.getFavorisListResourceExtender( user ).size( ) );

            model.put( MARK_USER_VOTES_ARRDT, VoteHome.getVoteUserNotLocalisation( user.getName( ), 75000 ) );
            model.put( MARK_USER_VOTES_PARIS, VoteHome.getVoteUserArrondissement( user.getName( ), 75000 ) );

            MyInfosForm myInfos = MyInfosService.loadUserInfos( user );

            if ( _nbrVoteService.selectVotePerLocation( myInfos.getArrondissement( ) ) != null )
            {
                nMaxVotesArrond = _nbrVoteService.selectVotePerLocation( myInfos.getArrondissement( ) ).getNbVotes( );
            }
            nMaxVotesToutParis = _nbrVoteService.selectVotePerLocation( MARK_TOUT_PARIS ).getNbVotes( );

            model.put( MARK_INFOS_USER, myInfos );
            model.put( MARK_MAX_VOTES_ARROND, nMaxVotesArrond );
            model.put( MARK_MAX_VOTES_TOUT_PARIS, nMaxVotesToutParis );

        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_HEADER_INCLUDE, locale, model );

        return template.getHtml( );
    }

}
