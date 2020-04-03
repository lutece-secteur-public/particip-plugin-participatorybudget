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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campaign;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignAreaHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignImageHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignPhaseHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignThemeHome;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.event.CampaignEvent;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.event.CampaignEventListernersManager;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage Campaign features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCampaignbp.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/campaign/", right = "CAMPAIGNBP_MANAGEMENT" )
public class CampaignJspBean extends ManageCampaignJspBean
{
    private static final long serialVersionUID = 7914078112474732968L;

    // //////////////////////////////////////////////////////////////////////////
    // Constants

    /**
     * 
     */
    // templates
    private static final String TEMPLATE_MANAGE_CAMPAIGNS = "/admin/plugins/participatorybudget/campaign/manage_campaigns.html";
    private static final String TEMPLATE_CREATE_CAMPAIGN = "/admin/plugins/participatorybudget/campaign/create_campaign.html";
    private static final String TEMPLATE_MODIFY_CAMPAIGN = "/admin/plugins/participatorybudget/campaign/modify_campaign.html";

    // Parameters
    private static final String PARAMETER_ID_CAMPAIGN = "id";
    private static final String PARAMETER_CAMPAIGN_CODE = "code";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNS = "participatorybudget.manage_campaigns.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGN = "participatorybudget.modify_campaign.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAIGN = "participatorybudget.create_campaign.pageTitle";

    // Markers
    private static final String MARK_CAMPAIGN_LIST = "campaign_list";
    private static final String MARK_CAMPAIGN = "campaign";

    private static final String JSP_MANAGE_CAMPAIGNS = "jsp/admin/plugins/participatorybudget/campaign/ManageCampaignbp.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAIGN = "participatorybudget.message.confirmRemoveCampaign";

    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatorybudget.model.entity.campaign.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAIGNS = "manageCampaigns";
    private static final String VIEW_CREATE_CAMPAIGN = "createCampaign";
    private static final String VIEW_MODIFY_CAMPAIGN = "modifyCampaign";

    // Actions
    private static final String ACTION_CREATE_CAMPAIGN = "createCampaign";
    private static final String ACTION_MODIFY_CAMPAIGN = "modifyCampaign";
    private static final String ACTION_REMOVE_CAMPAIGN = "removeCampaign";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAIGN = "confirmRemoveCampaign";

    // Infos
    private static final String INFO_CAMPAIGN_CREATED = "participatorybudget.info.campaign.created";
    private static final String INFO_CAMPAIGN_UPDATED = "participatorybudget.info.campaign.updated";
    private static final String INFO_CAMPAIGN_REMOVED = "participatorybudget.info.campaign.removed";

    // Session variable to store working values
    private Campaign _campaign;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAIGNS, defaultView = true )
    public String getManageCampaigns( HttpServletRequest request )
    {
        _campaign = null;
        List<Campaign> listCampaigns = (List<Campaign>) CampaignHome.getCampaignsList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAIGN_LIST, listCampaigns, JSP_MANAGE_CAMPAIGNS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNS, TEMPLATE_MANAGE_CAMPAIGNS, model );
    }

    /**
     * Returns the form to create a campaign
     *
     * @param request
     *            The Http request
     * @return the html code of the campaign form
     */
    @View( VIEW_CREATE_CAMPAIGN )
    public String getCreateCampaign( HttpServletRequest request )
    {
        _campaign = ( _campaign != null ) ? _campaign : new Campaign( );

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGN, _campaign );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAIGN, TEMPLATE_CREATE_CAMPAIGN, model );
    }

    /**
     * Process the data capture form of a new campaign
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAIGN )
    public String doCreateCampaign( HttpServletRequest request )
    {
        populate( _campaign, request );

        // Check constraints
        if ( !validateBean( _campaign, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAIGN );
        }

        CampaignHome.create( _campaign );
        addInfo( INFO_CAMPAIGN_CREATED, getLocale( ) );

        CampaignService.getInstance( ).reset( );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNS );
    }

    /**
     * Manages the removal form of a campaign whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAIGN )
    public String getConfirmRemoveCampaign( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGN ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAIGN ) );
        url.addParameter( PARAMETER_ID_CAMPAIGN, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAIGN, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campaign
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage campaigns
     */
    @Action( ACTION_REMOVE_CAMPAIGN )
    public String doRemoveCampaign( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGN ) );
        CampaignHome.remove( nId );
        addInfo( INFO_CAMPAIGN_REMOVED, getLocale( ) );

        CampaignService.getInstance( ).reset( );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNS );
    }

    /**
     * Returns the form to update info about a campaign
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAIGN )
    public String getModifyCampaign( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGN ) );

        if ( _campaign == null || ( _campaign.getId( ) != nId ) )
        {
            _campaign = CampaignHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGN, _campaign );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGN, TEMPLATE_MODIFY_CAMPAIGN, model );
    }

    /**
     * Process the change form of a campaign
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAIGN )
    public String doModifyCampaign( HttpServletRequest request )
    {
        // If code modification, verify if authorized.
        int oldCampaignId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGN ) );
        Campaign oldCampaign = CampaignHome.findByPrimaryKey( oldCampaignId );
        String oldCampaignCode = oldCampaign.getCode( );
        String newCode = request.getParameter( PARAMETER_CAMPAIGN_CODE );

        if ( !oldCampaignCode.equals( newCode ) )
        {
            CampaignEvent event = new CampaignEvent( oldCampaign, null, CampaignEvent.CAMPAIGN_CODE_MODIFICATION_AUTHORISATION );
            List<String> results = CampaignEventListernersManager.getInstance( ).notifyListeners( event );
            if ( CollectionUtils.isNotEmpty( results ) )
            {
                // Unauthorized campaign code modification.
                addError( "Unauthorized campaign code modification : " + StringUtils.join( results, ", " ) );
                return redirectView( request, VIEW_MANAGE_CAMPAIGNS );
            }
        }

        // Authorized campaign modification.
        populate( _campaign, request );

        // Check constraints
        if ( !validateBean( _campaign, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAIGN, PARAMETER_ID_CAMPAIGN, _campaign.getId( ) );
        }

        // Update the campaign
        CampaignHome.update( _campaign );
        addInfo( INFO_CAMPAIGN_UPDATED, getLocale( ) );

        // If code modification, inform listeners and update local data.
        if ( !oldCampaignCode.equals( newCode ) )
        {
            // Update local data
            CampaignPhaseHome.changeCampainCode( oldCampaignCode, newCode );
            CampaignThemeHome.changeCampainCode( oldCampaignCode, newCode );
            CampaignAreaHome.changeCampainCode( oldCampaignCode, newCode );
            CampaignImageHome.changeCampainCode( oldCampaignCode, newCode );

            // Inform listeners
            CampaignEvent event = new CampaignEvent( oldCampaign, _campaign, CampaignEvent.CAMPAIGN_CODE_MODIFIED );
            CampaignEventListernersManager.getInstance( ).notifyListeners( event );
        }

        CampaignService.getInstance( ).reset( );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNS );
    }
}
