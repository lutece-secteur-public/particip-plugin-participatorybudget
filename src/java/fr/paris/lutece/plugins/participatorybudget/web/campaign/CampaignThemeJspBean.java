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

import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignTheme;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignThemeHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage CampaignTheme features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCampaignThemes.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/campaign/", right = "CAMPAIGNBP_MANAGEMENT" )
public class CampaignThemeJspBean extends ManageCampaignJspBean
{

    // //////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_CAMPAIGNTHEMES = "/admin/plugins/participatorybudget/campaign/manage_campaignthemes.html";
    private static final String TEMPLATE_CREATE_CAMPAIGNTHEME = "/admin/plugins/participatorybudget/campaign/create_campaigntheme.html";
    private static final String TEMPLATE_MODIFY_CAMPAIGNTHEME = "/admin/plugins/participatorybudget/campaign/modify_campaigntheme.html";

    // Parameters
    private static final String PARAMETER_ID_CAMPAIGNTHEME = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNTHEMES = "participatorybudget.manage_campaignthemes.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGNTHEME = "participatorybudget.modify_campaigntheme.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAIGNTHEME = "participatorybudget.create_campaigntheme.pageTitle";

    // Markers
    private static final String MARK_CAMPAIGNTHEME_LIST = "campaigntheme_list";
    private static final String MARK_CAMPAIGNTHEME = "campaigntheme";

    private static final String JSP_MANAGE_CAMPAIGNTHEMES = "jsp/admin/plugins/participatorybudget/campaign/ManageCampaignThemes.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAIGNTHEME = "participatorybudget.message.confirmRemoveCampaignTheme";
    private static final String PROPERTY_DEFAULT_LIST_CAMPAIGNTHEME_PER_PAGE = "participatorybudget.listCampaignThemes.itemsPerPage";

    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatorybudget.model.entity.campaigntheme.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAIGNTHEMES = "manageCampaignThemes";
    private static final String VIEW_CREATE_CAMPAIGNTHEME = "createCampaignTheme";
    private static final String VIEW_MODIFY_CAMPAIGNTHEME = "modifyCampaignTheme";

    // Actions
    private static final String ACTION_CREATE_CAMPAIGNTHEME = "createCampaignTheme";
    private static final String ACTION_MODIFY_CAMPAIGNTHEME = "modifyCampaignTheme";
    private static final String ACTION_REMOVE_CAMPAIGNTHEME = "removeCampaignTheme";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAIGNTHEME = "confirmRemoveCampaignTheme";

    // Infos
    private static final String INFO_CAMPAIGNTHEME_CREATED = "participatorybudget.info.campaigntheme.created";
    private static final String INFO_CAMPAIGNTHEME_UPDATED = "participatorybudget.info.campaigntheme.updated";
    private static final String INFO_CAMPAIGNTHEME_REMOVED = "participatorybudget.info.campaigntheme.removed";

    // Session variable to store working values
    private CampaignTheme _campaigntheme;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAIGNTHEMES, defaultView = true )
    public String getManageCampaignThemes( HttpServletRequest request )
    {
        _campaigntheme = null;
        List<CampaignTheme> listCampaignThemes = (List<CampaignTheme>) CampaignThemeHome.getCampaignThemesList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAIGNTHEME_LIST, listCampaignThemes, JSP_MANAGE_CAMPAIGNTHEMES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNTHEMES, TEMPLATE_MANAGE_CAMPAIGNTHEMES, model );
    }

    /**
     * Returns the form to create a campaigntheme
     *
     * @param request
     *            The Http request
     * @return the html code of the campaigntheme form
     */
    @View( VIEW_CREATE_CAMPAIGNTHEME )
    public String getCreateCampaignTheme( HttpServletRequest request )
    {
        _campaigntheme = ( _campaigntheme != null ) ? _campaigntheme : new CampaignTheme( );

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGNTHEME, _campaigntheme );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAIGNTHEME, TEMPLATE_CREATE_CAMPAIGNTHEME, model );
    }

    /**
     * Process the data capture form of a new campaigntheme
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAIGNTHEME )
    public String doCreateCampaignTheme( HttpServletRequest request )
    {
        populate( _campaigntheme, request );

        // Check constraints
        if ( !validateBean( _campaigntheme, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAIGNTHEME );
        }

        CampaignThemeHome.create( _campaigntheme );
        addInfo( INFO_CAMPAIGNTHEME_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNTHEMES );
    }

    /**
     * Manages the removal form of a campaigntheme whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAIGNTHEME )
    public String getConfirmRemoveCampaignTheme( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNTHEME ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAIGNTHEME ) );
        url.addParameter( PARAMETER_ID_CAMPAIGNTHEME, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAIGNTHEME, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campaigntheme
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage campaignthemes
     */
    @Action( ACTION_REMOVE_CAMPAIGNTHEME )
    public String doRemoveCampaignTheme( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNTHEME ) );
        CampaignThemeHome.remove( nId );
        addInfo( INFO_CAMPAIGNTHEME_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNTHEMES );
    }

    /**
     * Returns the form to update info about a campaigntheme
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAIGNTHEME )
    public String getModifyCampaignTheme( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNTHEME ) );

        if ( _campaigntheme == null || ( _campaigntheme.getId( ) != nId ) )
        {
            _campaigntheme = CampaignThemeHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGNTHEME, _campaigntheme );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGNTHEME, TEMPLATE_MODIFY_CAMPAIGNTHEME, model );
    }

    /**
     * Process the change form of a campaigntheme
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAIGNTHEME )
    public String doModifyCampaignTheme( HttpServletRequest request )
    {
        populate( _campaigntheme, request );

        // Check constraints
        if ( !validateBean( _campaigntheme, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAIGNTHEME, PARAMETER_ID_CAMPAIGNTHEME, _campaigntheme.getId( ) );
        }

        CampaignThemeHome.update( _campaigntheme );
        addInfo( INFO_CAMPAIGNTHEME_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNTHEMES );
    }
}
