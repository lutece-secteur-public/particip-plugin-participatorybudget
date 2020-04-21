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

import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignArea;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignAreaHome;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage CampaignArea features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCampaignAreas.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/campaign/", right = "CAMPAIGN_MANAGEMENT" )
public class CampaignAreaJspBean extends ManageCampaignJspBean
{

    private static final long serialVersionUID = -1820764320140603066L;

    // //////////////////////////////////////////////////////////////////////////
    // Constants
    // templates
    private static final String TEMPLATE_MANAGE_CAMPAIGNAREAS = "/admin/plugins/participatorybudget/campaign/manage_campaignareas.ftl";
    private static final String TEMPLATE_CREATE_CAMPAIGNAREA = "/admin/plugins/participatorybudget/campaign/create_campaignarea.ftl";
    private static final String TEMPLATE_MODIFY_CAMPAIGNAREA = "/admin/plugins/participatorybudget/campaign/modify_campaignarea.ftl";

    // Parameters
    private static final String PARAMETER_ID_CAMPAIGNAREA = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNAREAS = "participatorybudget.manage_campaignthemes.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGNAREA = "participatorybudget.modify_campaigntheme.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAIGNAREA = "participatorybudget.create_campaigntheme.pageTitle";

    // Markers
    private static final String MARK_CAMPAIGNAREA_LIST = "campaignarea_list";
    private static final String MARK_CAMPAIGNAREA = "campaignarea";

    private static final String JSP_MANAGE_CAMPAIGNAREAS = "jsp/admin/plugins/participatorybudget/campaign/ManageCampaignAreas.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAIGNAREA = "participatorybudget.message.confirmRemoveCampaignTheme";

    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatorybudget.model.entity.campaigntheme.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAIGNAREAS = "manageCampaignAreas";
    private static final String VIEW_CREATE_CAMPAIGNAREA = "createCampaignArea";
    private static final String VIEW_MODIFY_CAMPAIGNAREA = "modifyCampaignArea";

    // Actions
    private static final String ACTION_CREATE_CAMPAIGNAREA = "createCampaignArea";
    private static final String ACTION_MODIFY_CAMPAIGNAREA = "modifyCampaignArea";
    private static final String ACTION_REMOVE_CAMPAIGNAREA = "removeCampaignArea";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAIGNAREA = "confirmRemoveCampaignArea";

    // Infos
    private static final String INFO_CAMPAIGNAREA_CREATED = "participatorybudget.info.campaigntheme.created";
    private static final String INFO_CAMPAIGNAREA_UPDATED = "participatorybudget.info.campaigntheme.updated";
    private static final String INFO_CAMPAIGNAREA_REMOVED = "participatorybudget.info.campaigntheme.removed";

    // Session variable to store working values
    private CampaignArea _campaignarea;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAIGNAREAS, defaultView = true )
    public String getManageCampaignAreas( HttpServletRequest request )
    {
        _campaignarea = null;
        List<CampaignArea> listCampaignAreas = (List<CampaignArea>) CampaignAreaHome.getCampaignAreasList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAIGNAREA_LIST, listCampaignAreas, JSP_MANAGE_CAMPAIGNAREAS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNAREAS, TEMPLATE_MANAGE_CAMPAIGNAREAS, model );
    }

    /**
     * Returns the form to create a campaignarea
     *
     * @param request
     *            The Http request
     * @return the html code of the campaignarea form
     */
    @View( VIEW_CREATE_CAMPAIGNAREA )
    public String getCreateCampaignArea( HttpServletRequest request )
    {
        _campaignarea = ( _campaignarea != null ) ? _campaignarea : new CampaignArea( );

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGNAREA, _campaignarea );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAIGNAREA, TEMPLATE_CREATE_CAMPAIGNAREA, model );
    }

    /**
     * Process the data capture form of a new campaignarea
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAIGNAREA )
    public String doCreateCampaignArea( HttpServletRequest request )
    {
        populate( _campaignarea, request );

        // Check constraints
        if ( !validateBean( _campaignarea, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAIGNAREA );
        }
        if ( _campaignarea.getType( ).equals( "whole" ) && CampaignService.getInstance( ).hasWholeArea( _campaignarea.getCodeCampaign( ) ) )
        {
            addError( "#i18n{participatorybudget.validation.campaignarea.Type.alreadyWhole}" );
            return redirectView( request, VIEW_CREATE_CAMPAIGNAREA );
        }

        CampaignAreaHome.create( _campaignarea );
        addInfo( INFO_CAMPAIGNAREA_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNAREAS );
    }

    /**
     * Manages the removal form of a campaignarea whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAIGNAREA )
    public String getConfirmRemoveCampaignArea( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNAREA ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAIGNAREA ) );
        url.addParameter( PARAMETER_ID_CAMPAIGNAREA, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAIGNAREA, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campaignarea
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage campaignareas
     */
    @Action( ACTION_REMOVE_CAMPAIGNAREA )
    public String doRemoveCampaignArea( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNAREA ) );
        CampaignAreaHome.remove( nId );
        addInfo( INFO_CAMPAIGNAREA_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNAREAS );
    }

    /**
     * Returns the form to update info about a campaignarea
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAIGNAREA )
    public String getModifyCampaignArea( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNAREA ) );

        if ( _campaignarea == null || ( _campaignarea.getId( ) != nId ) )
        {
            _campaignarea = CampaignAreaHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGNAREA, _campaignarea );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGNAREA, TEMPLATE_MODIFY_CAMPAIGNAREA, model );
    }

    /**
     * Process the change form of a campaignarea
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAIGNAREA )
    public String doModifyCampaignArea( HttpServletRequest request )
    {
        populate( _campaignarea, request );

        // Check constraints
        if ( !validateBean( _campaignarea, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAIGNAREA, PARAMETER_ID_CAMPAIGNAREA, _campaignarea.getId( ) );
        }
        if ( _campaignarea.getType( ).equals( "whole" )
                && CampaignService.getInstance( ).hasWholeArea( _campaignarea.getCodeCampaign( ), _campaignarea.getId( ) ) )
        {
            addError( "#i18n{participatorybudget.validation.campaignarea.Type.alreadyWhole}" );
            return redirectView( request, VIEW_CREATE_CAMPAIGNAREA );
        }

        CampaignAreaHome.update( _campaignarea );
        addInfo( INFO_CAMPAIGNAREA_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNAREAS );
    }
}
