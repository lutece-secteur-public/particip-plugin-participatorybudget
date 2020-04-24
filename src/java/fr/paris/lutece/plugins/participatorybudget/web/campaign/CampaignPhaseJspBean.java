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
package fr.paris.lutece.plugins.participatorybudget.web.campaign;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignPhase;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignPhaseHome;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage CampaignPhase features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCampaignPhases.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/campaign/", right = "CAMPAIGN_MANAGEMENT" )
public class CampaignPhaseJspBean extends ManageCampaignJspBean
{

    // //////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_CAMPAIGNPHASES = "/admin/plugins/participatorybudget/campaign/manage_campaignphases.html";
    private static final String TEMPLATE_CREATE_CAMPAIGNPHASE = "/admin/plugins/participatorybudget/campaign/create_campaignphase.html";
    private static final String TEMPLATE_MODIFY_CAMPAIGNPHASE = "/admin/plugins/participatorybudget/campaign/modify_campaignphase.html";

    // Parameters
    private static final String PARAMETER_ID_CAMPAIGNPHASE = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNPHASES = "participatorybudget.manage_campaignphases.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGNPHASE = "participatorybudget.modify_campaignphase.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAIGNPHASE = "participatorybudget.create_campaignphase.pageTitle";

    // Markers
    private static final String MARK_CAMPAIGNPHASE_LIST = "campaignphase_list";
    private static final String MARK_CAMPAIGNPHASE = "campaignphase";

    private static final String JSP_MANAGE_CAMPAIGNPHASES = "jsp/admin/plugins/participatorybudget/campaign/ManageCampaignPhases.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAIGNPHASE = "participatorybudget.message.confirmRemoveCampaignPhase";
    private static final String MESSAGE_CONFIRM_TARGET_CAMPAIGNPHASE = "participatorybudget.message.confirmTargetCampaignPhase";

    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatorybudget.model.entity.campaignphase.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAIGNPHASES = "manageCampaignPhases";
    private static final String VIEW_CREATE_CAMPAIGNPHASE = "createCampaignPhase";
    private static final String VIEW_MODIFY_CAMPAIGNPHASE = "modifyCampaignPhase";

    // Actions
    private static final String ACTION_CREATE_CAMPAIGNPHASE = "createCampaignPhase";
    private static final String ACTION_MODIFY_CAMPAIGNPHASE = "modifyCampaignPhase";
    private static final String ACTION_REMOVE_CAMPAIGNPHASE = "removeCampaignPhase";
    private static final String ACTION_TARGET_CAMPAIGNPHASE = "targetCampaignPhase";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAIGNPHASE = "confirmRemoveCampaignPhase";
    private static final String ACTION_CONFIRM_TARGET_CAMPAIGNPHASE = "confirmTargetCampaignPhase";

    // Infos
    private static final String INFO_CAMPAIGNPHASE_CREATED = "participatorybudget.info.campaignphase.created";
    private static final String INFO_CAMPAIGNPHASE_UPDATED = "participatorybudget.info.campaignphase.updated";
    private static final String INFO_CAMPAIGNPHASE_REMOVED = "participatorybudget.info.campaignphase.removed";
    private static final String INFO_CAMPAIGNPHASE_TARGETED = "participatorybudget.info.campaignphase.targeted";

    // Session variable to store working values
    private CampaignPhase _campaignphase;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAIGNPHASES, defaultView = true )
    public String getManageCampaignPhases( HttpServletRequest request )
    {
        _campaignphase = null;
        List<CampaignPhase> listCampaignPhases = (List<CampaignPhase>) CampaignPhaseHome.getCampaignPhasesList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAIGNPHASE_LIST, listCampaignPhases, JSP_MANAGE_CAMPAIGNPHASES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNPHASES, TEMPLATE_MANAGE_CAMPAIGNPHASES, model );
    }

    /**
     * Returns the form to create a campaignphase
     *
     * @param request
     *            The Http request
     * @return the html code of the campaignphase form
     */
    @View( VIEW_CREATE_CAMPAIGNPHASE )
    public String getCreateCampaignPhase( HttpServletRequest request )
    {
        _campaignphase = ( _campaignphase != null ) ? _campaignphase : new CampaignPhase( );

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGNPHASE, _campaignphase );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAIGNPHASE, TEMPLATE_CREATE_CAMPAIGNPHASE, model );
    }

    /**
     * Process the data capture form of a new campaignphase
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAIGNPHASE )
    public String doCreateCampaignPhase( HttpServletRequest request )
    {
        populate( _campaignphase, request );

        // Check constraints
        if ( !validateBean( _campaignphase, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAIGNPHASE );
        }

        CampaignPhaseHome.create( _campaignphase );
        addInfo( INFO_CAMPAIGNPHASE_CREATED, getLocale( ) );

        CampaignService.getInstance( ).reset( );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNPHASES );
    }

    /**
     * Manages the removal form of a campaignphase whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAIGNPHASE )
    public String getConfirmRemoveCampaignPhase( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNPHASE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAIGNPHASE ) );
        url.addParameter( PARAMETER_ID_CAMPAIGNPHASE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAIGNPHASE, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Manages the target form of a campaignphase whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_TARGET_CAMPAIGNPHASE )
    public String getConfirmTargetCampaignPhase( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNPHASE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_TARGET_CAMPAIGNPHASE ) );
        url.addParameter( PARAMETER_ID_CAMPAIGNPHASE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_TARGET_CAMPAIGNPHASE, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campaignphase
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage campaignphases
     */
    @Action( ACTION_REMOVE_CAMPAIGNPHASE )
    public String doRemoveCampaignPhase( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNPHASE ) );
        CampaignPhaseHome.remove( nId );
        addInfo( INFO_CAMPAIGNPHASE_REMOVED, getLocale( ) );

        CampaignService.getInstance( ).reset( );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNPHASES );
    }

    /**
     * Handles the target form of a campaignphase
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage campaignphases
     */
    @Action( ACTION_TARGET_CAMPAIGNPHASE )
    public String doTargetCampaignPhase( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNPHASE ) );
        CampaignPhase targetedPhase = CampaignPhaseHome.findByPrimaryKey( nId );

        List<CampaignPhase> phases = CampaignPhaseHome.getCampaignPhasesOrderedList( );

        // Search the position of the targeted hase in the list
        int index = 0;
        for ( index = 0; index < phases.size( ); index++ )
        {
            if ( targetedPhase.getId( ) == phases.get( index ).getId( ) )
            {
                break;
            }
        }

        // Calculate the date of the very first phase
        Calendar cal = Calendar.getInstance( );
        cal.setTimeInMillis( System.currentTimeMillis( ) );
        cal.add( Calendar.HOUR, -( 2 * 24 ) );
        cal.add( Calendar.DAY_OF_YEAR, -( index * 7 ) );

        // Set each phase begin/end dates
        for ( CampaignPhase phase : phases )
        {
            phase.setStart( new Timestamp( cal.getTime( ).getTime( ) ) );

            cal.add( Calendar.DAY_OF_YEAR, 7 );
            cal.add( Calendar.SECOND, -1 );
            phase.setEnd( new Timestamp( cal.getTime( ).getTime( ) ) );

            CampaignPhaseHome.update( phase );

            cal.add( Calendar.SECOND, 1 );
        }

        addInfo( INFO_CAMPAIGNPHASE_TARGETED, getLocale( ) );

        CampaignService.getInstance( ).reset( );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNPHASES );
    }

    /**
     * Returns the form to update info about a campaignphase
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAIGNPHASE )
    public String getModifyCampaignPhase( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNPHASE ) );

        if ( _campaignphase == null || ( _campaignphase.getId( ) != nId ) )
        {
            _campaignphase = CampaignPhaseHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGNPHASE, _campaignphase );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGNPHASE, TEMPLATE_MODIFY_CAMPAIGNPHASE, model );
    }

    /**
     * Process the change form of a campaignphase
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAIGNPHASE )
    public String doModifyCampaignPhase( HttpServletRequest request )
    {
        populate( _campaignphase, request );

        // Check constraints
        if ( !validateBean( _campaignphase, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAIGNPHASE, PARAMETER_ID_CAMPAIGNPHASE, _campaignphase.getId( ) );
        }

        CampaignPhaseHome.update( _campaignphase );
        addInfo( INFO_CAMPAIGNPHASE_UPDATED, getLocale( ) );

        CampaignService.getInstance( ).reset( );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNPHASES );
    }
}
