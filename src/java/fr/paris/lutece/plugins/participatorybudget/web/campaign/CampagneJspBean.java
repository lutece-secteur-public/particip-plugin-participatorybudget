/*
 * Copyright (c) 2002-2015, Mairie de Paris
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

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneHome;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampagnesService;
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
 * This class provides the user interface to manage Campagne features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCampagnes.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/campaign/", right = "CAMPAGNEBP_MANAGEMENT" )
public class CampagneJspBean extends ManageCampagnebpJspBean
{

    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_CAMPAGNES = "/admin/plugins/participatorybudget/campaign/manage_campagnes.html";
    private static final String TEMPLATE_CREATE_CAMPAGNE = "/admin/plugins/participatorybudget/campaign/create_campagne.html";
    private static final String TEMPLATE_MODIFY_CAMPAGNE = "/admin/plugins/participatorybudget/campaign/modify_campagne.html";


    // Parameters
    private static final String PARAMETER_ID_CAMPAGNE = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNES = "campagnebp.manage_campagnes.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNE = "campagnebp.modify_campagne.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAGNE = "campagnebp.create_campagne.pageTitle";

    // Markers
    private static final String MARK_CAMPAGNE_LIST = "campagne_list";
    private static final String MARK_CAMPAGNE = "campagne";

    private static final String JSP_MANAGE_CAMPAGNES = "jsp/admin/plugins/participatorybudget/campaign/ManageCampagnebp.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAGNE = "campagnebp.message.confirmRemoveCampagne";
    private static final String PROPERTY_DEFAULT_LIST_CAMPAGNE_PER_PAGE = "campagnebp.listCampagnes.itemsPerPage";
 
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "campagnebp.model.entity.campagne.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAGNES = "manageCampagnes";
    private static final String VIEW_CREATE_CAMPAGNE = "createCampagne";
    private static final String VIEW_MODIFY_CAMPAGNE = "modifyCampagne";

    // Actions
    private static final String ACTION_CREATE_CAMPAGNE = "createCampagne";
    private static final String ACTION_MODIFY_CAMPAGNE = "modifyCampagne";
    private static final String ACTION_REMOVE_CAMPAGNE = "removeCampagne";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAGNE = "confirmRemoveCampagne";

    // Infos
    private static final String INFO_CAMPAGNE_CREATED = "campagnebp.info.campagne.created";
    private static final String INFO_CAMPAGNE_UPDATED = "campagnebp.info.campagne.updated";
    private static final String INFO_CAMPAGNE_REMOVED = "campagnebp.info.campagne.removed";
    
    // Session variable to store working values
    private Campagne _campagne;
    
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAGNES, defaultView = true )
    public String getManageCampagnes( HttpServletRequest request )
    {
        _campagne = null;
        List<Campagne> listCampagnes = (List<Campagne>) CampagneHome.getCampagnesList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAGNE_LIST, listCampagnes, JSP_MANAGE_CAMPAGNES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNES, TEMPLATE_MANAGE_CAMPAGNES, model );
    }

    /**
     * Returns the form to create a campagne
     *
     * @param request The Http request
     * @return the html code of the campagne form
     */
    @View( VIEW_CREATE_CAMPAGNE )
    public String getCreateCampagne( HttpServletRequest request )
    {
        _campagne = ( _campagne != null ) ? _campagne : new Campagne(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_CAMPAGNE, _campagne );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAGNE, TEMPLATE_CREATE_CAMPAGNE, model );
    }

    /**
     * Process the data capture form of a new campagne
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAGNE )
    public String doCreateCampagne( HttpServletRequest request )
    {
        populate( _campagne, request );

        // Check constraints
        if ( !validateBean( _campagne, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAGNE );
        }

        CampagneHome.create( _campagne );
        addInfo( INFO_CAMPAGNE_CREATED, getLocale(  ) );

        CampagnesService.getInstance().reset();
        
        return redirectView( request, VIEW_MANAGE_CAMPAGNES );
    }

    /**
     * Manages the removal form of a campagne whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAGNE )
    public String getConfirmRemoveCampagne( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAGNE ) );
        url.addParameter( PARAMETER_ID_CAMPAGNE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAGNE,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campagne
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage campagnes
     */
    @Action( ACTION_REMOVE_CAMPAGNE )
    public String doRemoveCampagne( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNE ) );
        CampagneHome.remove( nId );
        addInfo( INFO_CAMPAGNE_REMOVED, getLocale(  ) );

        CampagnesService.getInstance().reset();
        
        return redirectView( request, VIEW_MANAGE_CAMPAGNES );
    }

    /**
     * Returns the form to update info about a campagne
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAGNE )
    public String getModifyCampagne( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNE ) );

        if ( _campagne == null || ( _campagne.getId(  ) != nId ))
        {
            _campagne = CampagneHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_CAMPAGNE, _campagne );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNE, TEMPLATE_MODIFY_CAMPAGNE, model );
    }

    /**
     * Process the change form of a campagne
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAGNE )
    public String doModifyCampagne( HttpServletRequest request )
    {
        populate( _campagne, request );

        // Check constraints
        if ( !validateBean( _campagne, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAGNE, PARAMETER_ID_CAMPAGNE, _campagne.getId( ) );
        }

        CampagneHome.update( _campagne );
        addInfo( INFO_CAMPAGNE_UPDATED, getLocale(  ) );

        CampagnesService.getInstance().reset();
        
        return redirectView( request, VIEW_MANAGE_CAMPAGNES );
    }
}
