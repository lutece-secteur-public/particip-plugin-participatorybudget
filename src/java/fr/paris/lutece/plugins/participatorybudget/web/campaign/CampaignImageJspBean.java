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

import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignImage;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignImageHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage CampaignImage features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCampaignImages.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/campaign/", right = "CAMPAIGN_MANAGEMENT" )
public class CampaignImageJspBean extends ManageCampaignJspBean
{

    // //////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_CAMPAIGNIMAGES = "/admin/plugins/participatorybudget/campaign/manage_campaignimages.html";
    private static final String TEMPLATE_CREATE_CAMPAIGNIMAGE = "/admin/plugins/participatorybudget/campaign/create_campaignimage.html";
    private static final String TEMPLATE_MODIFY_CAMPAIGNIMAGE = "/admin/plugins/participatorybudget/campaign/modify_campaignimage.html";

    // Parameters
    private static final String PARAMETER_ID_CAMPAIGNIMAGE = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNIMAGES = "participatorybudget.manage_campaignimages.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGNIMAGE = "participatorybudget.modify_campaignimage.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAIGNIMAGE = "participatorybudget.create_campaignimage.pageTitle";

    // Markers
    private static final String MARK_CAMPAIGNIMAGE_LIST = "campaignimage_list";
    private static final String MARK_CAMPAIGNIMAGE = "campaignimage";

    private static final String JSP_MANAGE_CAMPAIGNIMAGES = "jsp/admin/plugins/participatorybudget/campaign/ManageCampaignImages.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAIGNIMAGE = "participatorybudget.message.confirmRemoveCampaignImage";
    private static final String PROPERTY_DEFAULT_LIST_CAMPAIGNIMAGE_PER_PAGE = "participatorybudget.listCampaignImages.itemsPerPage";

    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatorybudget.model.entity.campaignimage.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAIGNIMAGES = "manageCampaignImages";
    private static final String VIEW_CREATE_CAMPAIGNIMAGE = "createCampaignImage";
    private static final String VIEW_MODIFY_CAMPAIGNIMAGE = "modifyCampaignImage";

    // Actions
    private static final String ACTION_CREATE_CAMPAIGNIMAGE = "createCampaignImage";
    private static final String ACTION_MODIFY_CAMPAIGNIMAGE = "modifyCampaignImage";
    private static final String ACTION_REMOVE_CAMPAIGNIMAGE = "removeCampaignImage";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAIGNIMAGE = "confirmRemoveCampaignImage";

    // Infos
    private static final String INFO_CAMPAIGNIMAGE_CREATED = "participatorybudget.info.campaignimage.created";
    private static final String INFO_CAMPAIGNIMAGE_UPDATED = "participatorybudget.info.campaignimage.updated";
    private static final String INFO_CAMPAIGNIMAGE_REMOVED = "participatorybudget.info.campaignimage.removed";

    // Session variable to store working values
    private CampaignImage _campaignimage;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAIGNIMAGES, defaultView = true )
    public String getManageCampaignImages( HttpServletRequest request )
    {
        _campaignimage = null;
        List<CampaignImage> listCampaignImages = (List<CampaignImage>) CampaignImageHome.getCampaignImagesList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAIGNIMAGE_LIST, listCampaignImages, JSP_MANAGE_CAMPAIGNIMAGES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNIMAGES, TEMPLATE_MANAGE_CAMPAIGNIMAGES, model );
    }

    /**
     * Returns the form to create a campaignimage
     *
     * @param request
     *            The Http request
     * @return the html code of the campaignimage form
     */
    @View( VIEW_CREATE_CAMPAIGNIMAGE )
    public String getCreateCampaignImage( HttpServletRequest request )
    {
        _campaignimage = ( _campaignimage != null ) ? _campaignimage : new CampaignImage( );

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGNIMAGE, _campaignimage );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAIGNIMAGE, TEMPLATE_CREATE_CAMPAIGNIMAGE, model );
    }

    /**
     * Process the data capture form of a new campaignimage
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAIGNIMAGE )
    public String doCreateCampaignImage( HttpServletRequest request )
    {
        populate( _campaignimage, request );

        // Check constraints
        if ( !validateBean( _campaignimage, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAIGNIMAGE );
        }

        CampaignImageHome.create( _campaignimage );
        addInfo( INFO_CAMPAIGNIMAGE_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNIMAGES );
    }

    /**
     * Manages the removal form of a campaignimage whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAIGNIMAGE )
    public String getConfirmRemoveCampaignImage( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNIMAGE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAIGNIMAGE ) );
        url.addParameter( PARAMETER_ID_CAMPAIGNIMAGE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAIGNIMAGE, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campaignimage
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage campaignimages
     */
    @Action( ACTION_REMOVE_CAMPAIGNIMAGE )
    public String doRemoveCampaignImage( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNIMAGE ) );
        CampaignImageHome.remove( nId );
        addInfo( INFO_CAMPAIGNIMAGE_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNIMAGES );
    }

    /**
     * Returns the form to update info about a campaignimage
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAIGNIMAGE )
    public String getModifyCampaignImage( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNIMAGE ) );

        if ( _campaignimage == null || ( _campaignimage.getId( ) != nId ) )
        {
            _campaignimage = CampaignImageHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGNIMAGE, _campaignimage );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGNIMAGE, TEMPLATE_MODIFY_CAMPAIGNIMAGE, model );
    }

    /**
     * Process the change form of a campaignimage
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAIGNIMAGE )
    public String doModifyCampaignImage( HttpServletRequest request )
    {
        populate( _campaignimage, request );

        // Check constraints
        if ( !validateBean( _campaignimage, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAIGNIMAGE, PARAMETER_ID_CAMPAIGNIMAGE, _campaignimage.getId( ) );
        }

        CampaignImageHome.update( _campaignimage );
        addInfo( INFO_CAMPAIGNIMAGE_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNIMAGES );
    }
}
