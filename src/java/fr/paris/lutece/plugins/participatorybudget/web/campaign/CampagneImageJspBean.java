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

import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneImage;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneImageHome;
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
 * This class provides the user interface to manage CampagneImage features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCampagneImages.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/campaign/", right = "CAMPAGNEBP_MANAGEMENT" )
public class CampagneImageJspBean extends ManageCampagnebpJspBean
{

    // //////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_CAMPAGNEIMAGES = "/admin/plugins/participatorybudget/campaign/manage_campagneimages.html";
    private static final String TEMPLATE_CREATE_CAMPAGNEIMAGE = "/admin/plugins/participatorybudget/campaign/create_campagneimage.html";
    private static final String TEMPLATE_MODIFY_CAMPAGNEIMAGE = "/admin/plugins/participatorybudget/campaign/modify_campagneimage.html";

    // Parameters
    private static final String PARAMETER_ID_CAMPAGNEIMAGE = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNEIMAGES = "participatorybudget.manage_campagneimages.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNEIMAGE = "participatorybudget.modify_campagneimage.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAGNEIMAGE = "participatorybudget.create_campagneimage.pageTitle";

    // Markers
    private static final String MARK_CAMPAGNEIMAGE_LIST = "campagneimage_list";
    private static final String MARK_CAMPAGNEIMAGE = "campagneimage";

    private static final String JSP_MANAGE_CAMPAGNEIMAGES = "jsp/admin/plugins/participatorybudget/campaign/ManageCampagneImages.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAGNEIMAGE = "participatorybudget.message.confirmRemoveCampagneImage";
    private static final String PROPERTY_DEFAULT_LIST_CAMPAGNEIMAGE_PER_PAGE = "participatorybudget.listCampagneImages.itemsPerPage";

    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatorybudget.model.entity.campagneimage.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAGNEIMAGES = "manageCampagneImages";
    private static final String VIEW_CREATE_CAMPAGNEIMAGE = "createCampagneImage";
    private static final String VIEW_MODIFY_CAMPAGNEIMAGE = "modifyCampagneImage";

    // Actions
    private static final String ACTION_CREATE_CAMPAGNEIMAGE = "createCampagneImage";
    private static final String ACTION_MODIFY_CAMPAGNEIMAGE = "modifyCampagneImage";
    private static final String ACTION_REMOVE_CAMPAGNEIMAGE = "removeCampagneImage";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAGNEIMAGE = "confirmRemoveCampagneImage";

    // Infos
    private static final String INFO_CAMPAGNEIMAGE_CREATED = "participatorybudget.info.campagneimage.created";
    private static final String INFO_CAMPAGNEIMAGE_UPDATED = "participatorybudget.info.campagneimage.updated";
    private static final String INFO_CAMPAGNEIMAGE_REMOVED = "participatorybudget.info.campagneimage.removed";

    // Session variable to store working values
    private CampagneImage _campagneimage;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAGNEIMAGES, defaultView = true )
    public String getManageCampagneImages( HttpServletRequest request )
    {
        _campagneimage = null;
        List<CampagneImage> listCampagneImages = (List<CampagneImage>) CampagneImageHome.getCampagneImagesList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAGNEIMAGE_LIST, listCampagneImages, JSP_MANAGE_CAMPAGNEIMAGES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNEIMAGES, TEMPLATE_MANAGE_CAMPAGNEIMAGES, model );
    }

    /**
     * Returns the form to create a campagneimage
     *
     * @param request
     *            The Http request
     * @return the html code of the campagneimage form
     */
    @View( VIEW_CREATE_CAMPAGNEIMAGE )
    public String getCreateCampagneImage( HttpServletRequest request )
    {
        _campagneimage = ( _campagneimage != null ) ? _campagneimage : new CampagneImage( );

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAGNEIMAGE, _campagneimage );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAGNEIMAGE, TEMPLATE_CREATE_CAMPAGNEIMAGE, model );
    }

    /**
     * Process the data capture form of a new campagneimage
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAGNEIMAGE )
    public String doCreateCampagneImage( HttpServletRequest request )
    {
        populate( _campagneimage, request );

        // Check constraints
        if ( !validateBean( _campagneimage, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAGNEIMAGE );
        }

        CampagneImageHome.create( _campagneimage );
        addInfo( INFO_CAMPAGNEIMAGE_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNEIMAGES );
    }

    /**
     * Manages the removal form of a campagneimage whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAGNEIMAGE )
    public String getConfirmRemoveCampagneImage( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEIMAGE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAGNEIMAGE ) );
        url.addParameter( PARAMETER_ID_CAMPAGNEIMAGE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAGNEIMAGE, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campagneimage
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage campagneimages
     */
    @Action( ACTION_REMOVE_CAMPAGNEIMAGE )
    public String doRemoveCampagneImage( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEIMAGE ) );
        CampagneImageHome.remove( nId );
        addInfo( INFO_CAMPAGNEIMAGE_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNEIMAGES );
    }

    /**
     * Returns the form to update info about a campagneimage
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAGNEIMAGE )
    public String getModifyCampagneImage( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEIMAGE ) );

        if ( _campagneimage == null || ( _campagneimage.getId( ) != nId ) )
        {
            _campagneimage = CampagneImageHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAGNEIMAGE, _campagneimage );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNEIMAGE, TEMPLATE_MODIFY_CAMPAGNEIMAGE, model );
    }

    /**
     * Process the change form of a campagneimage
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAGNEIMAGE )
    public String doModifyCampagneImage( HttpServletRequest request )
    {
        populate( _campagneimage, request );

        // Check constraints
        if ( !validateBean( _campagneimage, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAGNEIMAGE, PARAMETER_ID_CAMPAGNEIMAGE, _campagneimage.getId( ) );
        }

        CampagneImageHome.update( _campagneimage );
        addInfo( INFO_CAMPAGNEIMAGE_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNEIMAGES );
    }
}
