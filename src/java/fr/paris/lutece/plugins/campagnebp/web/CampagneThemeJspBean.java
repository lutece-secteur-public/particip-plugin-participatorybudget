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

 
package fr.paris.lutece.plugins.campagnebp.web;

import fr.paris.lutece.plugins.campagnebp.business.CampagneTheme;
import fr.paris.lutece.plugins.campagnebp.business.CampagneThemeHome;
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
 * This class provides the user interface to manage CampagneTheme features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCampagneThemes.jsp", controllerPath = "jsp/admin/plugins/ideation/", right = "CAMPAGNEBP_MANAGEMENT" )
public class CampagneThemeJspBean extends ManageCampagnebpJspBean
{

    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_CAMPAGNETHEMES = "/admin/plugins/campagnebp/manage_campagnethemes.html";
    private static final String TEMPLATE_CREATE_CAMPAGNETHEME = "/admin/plugins/campagnebp/create_campagnetheme.html";
    private static final String TEMPLATE_MODIFY_CAMPAGNETHEME = "/admin/plugins/campagnebp/modify_campagnetheme.html";


    // Parameters
    private static final String PARAMETER_ID_CAMPAGNETHEME = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNETHEMES = "campagnebp.manage_campagnethemes.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNETHEME = "campagnebp.modify_campagnetheme.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAGNETHEME = "campagnebp.create_campagnetheme.pageTitle";

    // Markers
    private static final String MARK_CAMPAGNETHEME_LIST = "campagnetheme_list";
    private static final String MARK_CAMPAGNETHEME = "campagnetheme";

    private static final String JSP_MANAGE_CAMPAGNETHEMES = "jsp/admin/plugins/campagnebp/ManageCampagneThemes.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAGNETHEME = "campagnebp.message.confirmRemoveCampagneTheme";
    private static final String PROPERTY_DEFAULT_LIST_CAMPAGNETHEME_PER_PAGE = "campagnebp.listCampagneThemes.itemsPerPage";
 
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "campagnebp.model.entity.campagnetheme.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAGNETHEMES = "manageCampagneThemes";
    private static final String VIEW_CREATE_CAMPAGNETHEME = "createCampagneTheme";
    private static final String VIEW_MODIFY_CAMPAGNETHEME = "modifyCampagneTheme";

    // Actions
    private static final String ACTION_CREATE_CAMPAGNETHEME = "createCampagneTheme";
    private static final String ACTION_MODIFY_CAMPAGNETHEME = "modifyCampagneTheme";
    private static final String ACTION_REMOVE_CAMPAGNETHEME = "removeCampagneTheme";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAGNETHEME = "confirmRemoveCampagneTheme";

    // Infos
    private static final String INFO_CAMPAGNETHEME_CREATED = "campagnebp.info.campagnetheme.created";
    private static final String INFO_CAMPAGNETHEME_UPDATED = "campagnebp.info.campagnetheme.updated";
    private static final String INFO_CAMPAGNETHEME_REMOVED = "campagnebp.info.campagnetheme.removed";
    
    // Session variable to store working values
    private CampagneTheme _campagnetheme;
    
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAGNETHEMES, defaultView = true )
    public String getManageCampagneThemes( HttpServletRequest request )
    {
        _campagnetheme = null;
        List<CampagneTheme> listCampagneThemes = (List<CampagneTheme>) CampagneThemeHome.getCampagneThemesList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAGNETHEME_LIST, listCampagneThemes, JSP_MANAGE_CAMPAGNETHEMES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNETHEMES, TEMPLATE_MANAGE_CAMPAGNETHEMES, model );
    }

    /**
     * Returns the form to create a campagnetheme
     *
     * @param request The Http request
     * @return the html code of the campagnetheme form
     */
    @View( VIEW_CREATE_CAMPAGNETHEME )
    public String getCreateCampagneTheme( HttpServletRequest request )
    {
        _campagnetheme = ( _campagnetheme != null ) ? _campagnetheme : new CampagneTheme(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_CAMPAGNETHEME, _campagnetheme );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAGNETHEME, TEMPLATE_CREATE_CAMPAGNETHEME, model );
    }

    /**
     * Process the data capture form of a new campagnetheme
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAGNETHEME )
    public String doCreateCampagneTheme( HttpServletRequest request )
    {
        populate( _campagnetheme, request );

        // Check constraints
        if ( !validateBean( _campagnetheme, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAGNETHEME );
        }

        CampagneThemeHome.create( _campagnetheme );
        addInfo( INFO_CAMPAGNETHEME_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNETHEMES );
    }

    /**
     * Manages the removal form of a campagnetheme whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAGNETHEME )
    public String getConfirmRemoveCampagneTheme( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNETHEME ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAGNETHEME ) );
        url.addParameter( PARAMETER_ID_CAMPAGNETHEME, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAGNETHEME,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campagnetheme
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage campagnethemes
     */
    @Action( ACTION_REMOVE_CAMPAGNETHEME )
    public String doRemoveCampagneTheme( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNETHEME ) );
        CampagneThemeHome.remove( nId );
        addInfo( INFO_CAMPAGNETHEME_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNETHEMES );
    }

    /**
     * Returns the form to update info about a campagnetheme
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAGNETHEME )
    public String getModifyCampagneTheme( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNETHEME ) );

        if ( _campagnetheme == null || ( _campagnetheme.getId(  ) != nId ))
        {
            _campagnetheme = CampagneThemeHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_CAMPAGNETHEME, _campagnetheme );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNETHEME, TEMPLATE_MODIFY_CAMPAGNETHEME, model );
    }

    /**
     * Process the change form of a campagnetheme
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAGNETHEME )
    public String doModifyCampagneTheme( HttpServletRequest request )
    {
        populate( _campagnetheme, request );

        // Check constraints
        if ( !validateBean( _campagnetheme, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAGNETHEME, PARAMETER_ID_CAMPAGNETHEME, _campagnetheme.getId( ) );
        }

        CampagneThemeHome.update( _campagnetheme );
        addInfo( INFO_CAMPAGNETHEME_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNETHEMES );
    }
}
