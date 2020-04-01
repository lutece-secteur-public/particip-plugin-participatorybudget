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
package fr.paris.lutece.plugins.participatorybudget.web.vote;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.participatorybudget.service.vote.VoteStatService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

/**
 * This class provides the user interface to produce various business statistics
 */

@Controller( controllerJsp = "VoteDashboard.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/vote/", right = "VOTE_DASHBOARD" )
public class VoteDashboardJspBean extends MVCAdminJspBean
{

    private static final long serialVersionUID = 1L;

    // RIGHTS
    public static final String RIGHT_MANAGE_VOTE = "VOTE_DASHBOARD";

    // TEMPLATES
    private static final String TEMPLATE_MANAGE_VOTE_DASHBOARD = "admin/plugins/participatorybudget/vote/vote_dashboard.html";

    // PROPERTIES
    private static final String PROPERTY_PAGE_TITLE_VOTE_DASHBOARD = "participatorybudget.voteDashboard.pageTitle";

    // PARAMETERS
    // private static final String PARAMETER_METHOD_NAME = "method_name";
    // private static final String PARAMETER_REASON = "reason";
    // private static final String PARAMETER_EXPORT_ID = "export_id";

    // MARKERS
    private static final String MARK_PAGE_TITLE = "pageTitle";
    private static final String MARK_NB_VOTES_BY_CAMPAIGN_CODE_MAP = "nbVotesByCampaignCodeMap";
    private static final String MARK_NB_VOTES_BY_DATE_ALL_CAMPAIGNS_MAP = "nbVotesByDateAllCampaignsMap";

    // VIEWS
    private static final String VIEW_MANAGE_VOTE_DASHBOARD = "manageVoteDashboard";

    // ACTIONS
    // private static final String ACTION_DO_EXPORT_CSV = "doExportCsv";
    // private static final String ACTION_DO_DOWNLOAD_CSV = "doDownloadCsv";

    // *********************************************************************************************
    // * VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW *
    // * VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW *
    // *********************************************************************************************

    /**
     * Displaying vote dashboard
     */
    @View( value = VIEW_MANAGE_VOTE_DASHBOARD, defaultView = true )
    public String getManageVoteDashboard( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );

        // Add statistics
        model.put( MARK_NB_VOTES_BY_CAMPAIGN_CODE_MAP, VoteStatService.getInstance( ).getNbVoteByCampaign( ) );
        model.put( MARK_NB_VOTES_BY_DATE_ALL_CAMPAIGNS_MAP, VoteStatService.getInstance( ).getNbVoteByDateAllCampaigns( ) );

        model.put( MARK_PAGE_TITLE, AppPropertiesService.getProperty( PROPERTY_PAGE_TITLE_VOTE_DASHBOARD, "Vote Dashboard" ) );

        return getPage( PROPERTY_PAGE_TITLE_VOTE_DASHBOARD, TEMPLATE_MANAGE_VOTE_DASHBOARD, model );
    }

}
