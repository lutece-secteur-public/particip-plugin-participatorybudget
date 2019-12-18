/*
 * Copyright (c) 2002-2019, Mairie de Paris
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

import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneArea;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneAreaHome;
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
 * This class provides the user interface to manage CampagneArea features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCampagneAreas.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/campaign/", right = "CAMPAGNEBP_MANAGEMENT" )
public class CampagneAreaJspBean extends ManageCampagnebpJspBean
{

    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_CAMPAGNEAREAS = "/admin/plugins/participatorybudget/campaign/manage_campagneareas.html";
    private static final String TEMPLATE_CREATE_CAMPAGNEAREA = "/admin/plugins/participatorybudget/campaign/create_campagnearea.html";
    private static final String TEMPLATE_MODIFY_CAMPAGNEAREA = "/admin/plugins/participatorybudget/campaign/modify_campagnearea.html";


    // Parameters
    private static final String PARAMETER_ID_CAMPAGNEAREA = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNEAREAS = "participatorybudget.manage_campagnethemes.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNEAREA = "participatorybudget.modify_campagnetheme.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAGNEAREA = "participatorybudget.create_campagnetheme.pageTitle";

    // Markers
    private static final String MARK_CAMPAGNEAREA_LIST = "campagnearea_list";
    private static final String MARK_CAMPAGNEAREA = "campagnearea";

    private static final String JSP_MANAGE_CAMPAGNEAREAS = "jsp/admin/plugins/participatorybudget/campaign/ManageCampagneAreas.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAGNEAREA = "participatorybudget.message.confirmRemoveCampagneTheme";
    private static final String PROPERTY_DEFAULT_LIST_CAMPAGNEAREA_PER_PAGE = "participatorybudget.listCampagneThemes.itemsPerPage";
 
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatorybudget.model.entity.campagnetheme.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAGNEAREAS = "manageCampagneAreas";
    private static final String VIEW_CREATE_CAMPAGNEAREA = "createCampagneArea";
    private static final String VIEW_MODIFY_CAMPAGNEAREA = "modifyCampagneArea";

    // Actions
    private static final String ACTION_CREATE_CAMPAGNEAREA = "createCampagneArea";
    private static final String ACTION_MODIFY_CAMPAGNEAREA = "modifyCampagneArea";
    private static final String ACTION_REMOVE_CAMPAGNEAREA = "removeCampagneArea";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAGNEAREA = "confirmRemoveCampagneArea";

    // Infos
    private static final String INFO_CAMPAGNEAREA_CREATED = "participatorybudget.info.campagnetheme.created";
    private static final String INFO_CAMPAGNEAREA_UPDATED = "participatorybudget.info.campagnetheme.updated";
    private static final String INFO_CAMPAGNEAREA_REMOVED = "participatorybudget.info.campagnetheme.removed";
    
    // Session variable to store working values
    private CampagneArea _campagnearea;
    
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAGNEAREAS, defaultView = true )
    public String getManageCampagneAreas( HttpServletRequest request )
    {
        _campagnearea = null;
        List<CampagneArea> listCampagneAreas = (List<CampagneArea>) CampagneAreaHome.getCampagneAreasList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAGNEAREA_LIST, listCampagneAreas, JSP_MANAGE_CAMPAGNEAREAS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNEAREAS, TEMPLATE_MANAGE_CAMPAGNEAREAS, model );
    }

    /**
     * Returns the form to create a campagnearea
     *
     * @param request The Http request
     * @return the html code of the campagnearea form
     */
    @View( VIEW_CREATE_CAMPAGNEAREA )
    public String getCreateCampagneArea( HttpServletRequest request )
    {
        _campagnearea = ( _campagnearea != null ) ? _campagnearea : new CampagneArea(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_CAMPAGNEAREA, _campagnearea );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAGNEAREA, TEMPLATE_CREATE_CAMPAGNEAREA, model );
    }

    /**
     * Process the data capture form of a new campagnearea
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAGNEAREA )
    public String doCreateCampagneArea( HttpServletRequest request )
    {
        populate( _campagnearea, request );

        // Check constraints
        if ( !validateBean( _campagnearea, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAGNEAREA );
        }
        if ( _campagnearea.getType().equals("whole") && CampagnesService.getInstance( ).hasWholeArea(_campagnearea.getCodeCampagne())) {
            addError("#i18n{participatorybudget.validation.campagnearea.Type.alreadyWhole}");
        	return redirectView( request, VIEW_CREATE_CAMPAGNEAREA );
        }

        CampagneAreaHome.create( _campagnearea );
        addInfo( INFO_CAMPAGNEAREA_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNEAREAS );
    }

    /**
     * Manages the removal form of a campagnearea whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAGNEAREA )
    public String getConfirmRemoveCampagneArea( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEAREA ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAGNEAREA ) );
        url.addParameter( PARAMETER_ID_CAMPAGNEAREA, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAGNEAREA,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campagnearea
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage campagneareas
     */
    @Action( ACTION_REMOVE_CAMPAGNEAREA )
    public String doRemoveCampagneArea( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEAREA ) );
        CampagneAreaHome.remove( nId );
        addInfo( INFO_CAMPAGNEAREA_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNEAREAS );
    }

    /**
     * Returns the form to update info about a campagnearea
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAGNEAREA )
    public String getModifyCampagneArea( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEAREA ) );

        if ( _campagnearea == null || ( _campagnearea.getId(  ) != nId ))
        {
            _campagnearea = CampagneAreaHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_CAMPAGNEAREA, _campagnearea );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNEAREA, TEMPLATE_MODIFY_CAMPAGNEAREA, model );
    }

    /**
     * Process the change form of a campagnearea
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAGNEAREA )
    public String doModifyCampagneArea( HttpServletRequest request )
    {
        populate( _campagnearea, request );

        // Check constraints
        if ( !validateBean( _campagnearea, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAGNEAREA, PARAMETER_ID_CAMPAGNEAREA, _campagnearea.getId( ) );
        }
        if ( _campagnearea.getType().equals("whole") && CampagnesService.getInstance( ).hasWholeArea(_campagnearea.getCodeCampagne(), _campagnearea.getId())) {
            addError("#i18n{participatorybudget.validation.campagnearea.Type.alreadyWhole}");
        	return redirectView( request, VIEW_CREATE_CAMPAGNEAREA );
        }

        CampagneAreaHome.update( _campagnearea );
        addInfo( INFO_CAMPAGNEAREA_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNEAREAS );
    }
}
