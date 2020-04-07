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
package fr.paris.lutece.plugins.participatorybudget.service.rating;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.extend.modules.rating.service.RatingPlugin;
import fr.paris.lutece.plugins.extend.modules.rating.service.RatingService;
import fr.paris.lutece.plugins.participatorybudget.business.MyInfosForm;
import fr.paris.lutece.plugins.participatorybudget.business.vote.Vote;
import fr.paris.lutece.plugins.participatorybudget.business.vote.VoteHistoryHome;
import fr.paris.lutece.plugins.participatorybudget.business.vote.VoteHome;
import fr.paris.lutece.plugins.participatorybudget.service.MyInfosService;
import fr.paris.lutece.plugins.participatorybudget.util.ParticipatoryBudgetConstants;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class BudgetRatingService extends RatingService
{
    public static final int VOTE_VALUE = AppPropertiesService.getPropertyInt( "participatorybudget.defaultVoteValue", 0 );

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    public synchronized void doVote( String strIdExtendableResource, String strExtendableResourceType, double dVoteValue, HttpServletRequest request )
    {
        super.doVote( strIdExtendableResource, strExtendableResourceType, dVoteValue, request );

        if ( !isBudgetResource( strIdExtendableResource, strExtendableResourceType ) )
        {
            return;
        }

        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        doVote( user, strIdExtendableResource, request );
        doVoteHistory( user, strIdExtendableResource, request, 1 );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    public synchronized void doCancelVote( LuteceUser user, String strIdExtendableResource, String strExtendableResourceType )
    {
        super.doCancelVote( user, strIdExtendableResource, strExtendableResourceType );

        if ( !isBudgetResource( strIdExtendableResource, strExtendableResourceType ) )
        {
            return;
        }

        doCancelHistoryVote( user, strIdExtendableResource );
        doCancelVote( user, strIdExtendableResource );
    }

    /**
     * Save the vote
     * 
     * @param user
     *            The User
     * @param strProjectId
     *            The project Id
     * @param strIpAddress
     *            The user Ip
     */
    private void doVote( LuteceUser user, String strProjectId, HttpServletRequest request )
    {

        Vote vote = new Vote( );
        vote.setUserId( user.getName( ) );
        vote.setProjetId( Integer.parseInt( strProjectId ) );
        vote.setIpAddress( request.getRemoteAddr( ) );

        String strTheme = request.getParameter( ParticipatoryBudgetConstants.PROJECT_THEME );
        if ( strTheme == null )
        {
            strTheme = (String) request.getAttribute( ParticipatoryBudgetConstants.PROJECT_THEME );
        }
        vote.setTheme( strTheme );

        String strTitle = request.getParameter( ParticipatoryBudgetConstants.PROJECT_TITLE );
        if ( strTitle == null )
        {
            strTitle = (String) request.getAttribute( ParticipatoryBudgetConstants.PROJECT_TITLE );
        }
        vote.setTitle( strTitle );

        String strLocation = request.getParameter( ParticipatoryBudgetConstants.PROJECT_LOCATION );
        if ( strLocation == null )
        {
            strLocation = (String) request.getAttribute( ParticipatoryBudgetConstants.PROJECT_LOCATION );
        }
        vote.setLocation( strLocation );

        MyInfosForm myInfos = MyInfosService.loadUserInfos( user );
        String strBirthDate = null;
        String strArrondissement = null;
        if ( myInfos != null )
        {
            strBirthDate = myInfos.getBirthdate( );
            strArrondissement = myInfos.getArrondissement( );
        }

        if ( StringUtils.isNotEmpty( strBirthDate ) )
        {
            vote.setBirthDate( strBirthDate );
            try
            {
                vote.setAge( MyInfosService.getAge( strBirthDate ) );
            }
            catch( ParseException ex )
            {
                AppLogService.error( "Error storing vote " + ex.getMessage( ), ex );
            }
        }

        if ( StringUtils.isNotEmpty( strArrondissement ) )
        {
            try
            {
                vote.setArrondissement( Integer.parseInt( strArrondissement ) );
            }
            catch( NumberFormatException ex )
            {
                AppLogService.error( "Error storing vote " + ex.getMessage( ), ex );
            }

        }
        VoteHome.create( vote );
    }

    /**
     * Cancel a vote
     * 
     * @param user
     *            The User
     * @param strProjetId
     *            The project ID
     */
    private void doCancelVote( LuteceUser user, String strProjetId )
    {
        int nProjetId = 0;

        try
        {
            nProjetId = Integer.parseInt( strProjetId );
            VoteHome.remove( user.getName( ), nProjetId );
        }
        catch( NumberFormatException ex )
        {
            AppLogService.error( "Error canceling vote userId=" + user.getName( ) + " - projetId=" + nProjetId + " Error : " + ex.getMessage( ), ex );
        }
    }

    /**
     * Save the vote
     * 
     * @param user
     *            The User
     * @param strProjectId
     *            The project Id
     * @param strIpAddress
     *            The user Ip
     */

    private void doVoteHistory( LuteceUser user, String strProjectId, HttpServletRequest request, int status )
    {
        Vote vote = new Vote( );
        vote.setUserId( user.getName( ) );
        vote.setProjetId( Integer.parseInt( strProjectId ) );
        vote.setIpAddress( request.getRemoteAddr( ) );

        String strTheme = request.getParameter( ParticipatoryBudgetConstants.PROJECT_THEME );
        if ( strTheme == null )
        {
            strTheme = (String) request.getAttribute( ParticipatoryBudgetConstants.PROJECT_THEME );
        }
        vote.setTheme( strTheme );

        String strTitle = request.getParameter( ParticipatoryBudgetConstants.PROJECT_TITLE );
        if ( strTitle == null )
        {
            strTitle = (String) request.getAttribute( ParticipatoryBudgetConstants.PROJECT_TITLE );
        }
        vote.setTitle( strTitle );

        String strLocation = request.getParameter( ParticipatoryBudgetConstants.PROJECT_LOCATION );
        if ( strLocation == null )
        {
            strLocation = (String) request.getAttribute( ParticipatoryBudgetConstants.PROJECT_LOCATION );
        }
        vote.setLocation( strLocation );

        vote.setStatus( status );

        MyInfosForm myInfos = MyInfosService.loadUserInfos( user );
        String strBirthDate = null;
        String strArrondissement = null;
        if ( myInfos != null )
        {
            strBirthDate = myInfos.getBirthdate( );
            strArrondissement = myInfos.getArrondissement( );
        }

        if ( StringUtils.isNotEmpty( strBirthDate ) )
        {
            vote.setBirthDate( strBirthDate );
            try
            {
                vote.setAge( MyInfosService.getAge( strBirthDate ) );
            }
            catch( ParseException ex )
            {
                AppLogService.error( "Error storing vote " + ex.getMessage( ), ex );
            }
        }

        if ( StringUtils.isNotEmpty( strArrondissement ) )
        {
            try
            {
                vote.setArrondissement( Integer.parseInt( strArrondissement ) );
            }
            catch( NumberFormatException ex )
            {
                AppLogService.error( "Error storing vote " + ex.getMessage( ), ex );
            }

        }
        VoteHistoryHome.create( vote );
    }

    /**
     * add a vote in history
     * 
     * @param user
     *            The User
     * @param strProjetId
     *            The project ID
     */
    private void doCancelHistoryVote( LuteceUser user, String strProjetId )
    {
        int nUserId = 0;
        int nProjetId = 0;

        try
        {

            nProjetId = Integer.parseInt( strProjetId );
            Vote vote = VoteHome.getVote( user.getName( ), nProjetId );
            Vote vte = VoteHistoryHome.getVoteUser( user.getName( ), nProjetId );

            if ( vote != null && vte != null )
            {
                vote.setStatus( -1 );
                Calendar c = Calendar.getInstance( );
                Date date = c.getTime( );
                vote.setDateVote( new Timestamp( date.getTime( ) ) );
                VoteHistoryHome.create( vote );
            }
        }
        catch( NumberFormatException ex )
        {
            AppLogService.error( "Error canceling vote userId=" + nUserId + " - projetId=" + nProjetId + " Error : " + ex.getMessage( ), ex );
        }
    }

    public static boolean isBudgetResource( String strIdExtendableResource, String strExtendableResourceType )
    {
        // We may also check in the document for normal documents vs budget documents ?
        return Document.PROPERTY_RESOURCE_TYPE.equals( strExtendableResourceType );
    }
}
