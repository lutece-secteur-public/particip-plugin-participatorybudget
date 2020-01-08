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
package fr.paris.lutece.plugins.participatorybudget.web;

import fr.paris.lutece.plugins.participatorybudget.business.UserAccessVote;
import fr.paris.lutece.plugins.participatorybudget.business.UserAccessVoteHome;
import fr.paris.lutece.plugins.participatorybudget.business.VotePerLocation;
import fr.paris.lutece.plugins.participatorybudget.business.VotePerLocationHome;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * This class provides the user interface to manage the launching of the indexing of the site pages
 */

@Controller( controllerJsp = "VoteParArrandissement.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/", right = "VOTE_PAR_ARRANDISSEMENT" )
public class VoteParArrandJspBean extends MVCAdminJspBean
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    // //////////////////////////////////////////////////////////////////////////
    // Constantes
    /**
     * Right to manage indexation
     */
    public static final String RIGHT_INDEXER = "/";
    private static final String TEMPLATE_MANAGE_VOTES_PAR_ARRAND = "admin/plugins/participatorybudget/vote_par_arrandissement.html";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_VOTE_PAR_ARRAND = "participatorybudget.vote_par_arrd.pageTitle";

    // Parameters
    private static final String PARAMETER_NB = "nb_";

    // Mark
    private static final String MARK_LIST_VOTES = "listVotes";

    // views
    private static final String VIEW_MANAGE_VOTE_PAR_ARRAND = "manageVoteParArrand";

    // Actions
    private static final String ACTION_DO_UPDATE = "update";

    /**
     * Displays the indexing parameters
     *
     * @param request
     *            the http request
     * @return the html code which displays the parameters page
     * @throws UserNotSignedException
     */
    @View( value = VIEW_MANAGE_VOTE_PAR_ARRAND, defaultView = true )
    public String getManageVoteParArrand( HttpServletRequest request ) throws UserNotSignedException
    {

        List<VotePerLocation> listVotes = VotePerLocationHome.getListVotesPerLocation( );

        Map<String, Object> model = getModel( );

        model.put( MARK_LIST_VOTES, listVotes );

        return getPage( PROPERTY_PAGE_TITLE_VOTE_PAR_ARRAND, TEMPLATE_MANAGE_VOTES_PAR_ARRAND, model );
    }

    /**
     * Calls the indexing process
     *
     * @param request
     *            the http request
     * @return the result of the indexing process
     * @throws IOException
     */
    @Action( ACTION_DO_UPDATE )
    public String doUpdate( HttpServletRequest request ) throws IOException
    {
        List<VotePerLocation> listVotes = VotePerLocationHome.getListVotesPerLocation( );

        for ( int i = 1; i <= listVotes.size( ); i++ )
        {
            VotePerLocation vote = null;
            String strVal = request.getParameter( PARAMETER_NB + i ) == null ? StringUtils.EMPTY : request.getParameter( PARAMETER_NB + i );
            if ( StringUtils.isEmpty( strVal ) || !StringUtils.isNumeric( strVal ) )
            {
                if ( i == 21 )
                {
                    addError( "Champs Tout Paris n'est pas remplis" );
                    return redirectView( request, VIEW_MANAGE_VOTE_PAR_ARRAND );
                }
                else
                {
                    addError( "Champs Paris " + i + " Arrandissement n'est pas remplis" );
                    return redirectView( request, VIEW_MANAGE_VOTE_PAR_ARRAND );
                }
            }
            vote = listVotes.get( i - 1 );
            vote.setNbVotes( Integer.parseInt( strVal ) );
        }

        for ( VotePerLocation vpl : listVotes )
        {
            VotePerLocationHome.updateVotesPerLocation( vpl );
        }
        addInfo( "Votes par Arrandissemente modifié" );
        return redirectView( request, VIEW_MANAGE_VOTE_PAR_ARRAND );
    }
}
