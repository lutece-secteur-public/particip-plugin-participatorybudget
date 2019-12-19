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


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagnePhase;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagnePhaseHome;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampagnesService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;


/**
 * This class provides the user interface to manage CampagnePhase features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCampagnePhases.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/campaign/", right = "CAMPAGNEBP_MANAGEMENT" )
public class CampagnePhaseJspBean extends ManageCampagnebpJspBean
{

    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_CAMPAGNEPHASES = "/admin/plugins/participatorybudget/campaign/manage_campagnephases.html";
    private static final String TEMPLATE_CREATE_CAMPAGNEPHASE = "/admin/plugins/participatorybudget/campaign/create_campagnephase.html";
    private static final String TEMPLATE_MODIFY_CAMPAGNEPHASE = "/admin/plugins/participatorybudget/campaign/modify_campagnephase.html";


    // Parameters
    private static final String PARAMETER_ID_CAMPAGNEPHASE = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNEPHASES = "participatorybudget.manage_campagnephases.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNEPHASE = "participatorybudget.modify_campagnephase.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAGNEPHASE = "participatorybudget.create_campagnephase.pageTitle";

    // Markers
    private static final String MARK_CAMPAGNEPHASE_LIST = "campagnephase_list";
    private static final String MARK_CAMPAGNEPHASE = "campagnephase";

    private static final String JSP_MANAGE_CAMPAGNEPHASES = "jsp/admin/plugins/participatorybudget/campaign/ManageCampagnePhases.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAGNEPHASE = "participatorybudget.message.confirmRemoveCampagnePhase";
    private static final String PROPERTY_DEFAULT_LIST_CAMPAGNEPHASE_PER_PAGE = "participatorybudget.listCampagnePhases.itemsPerPage";
 
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatorybudget.model.entity.campagnephase.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAGNEPHASES = "manageCampagnePhases";
    private static final String VIEW_CREATE_CAMPAGNEPHASE = "createCampagnePhase";
    private static final String VIEW_MODIFY_CAMPAGNEPHASE = "modifyCampagnePhase";

    // Actions
    private static final String ACTION_CREATE_CAMPAGNEPHASE = "createCampagnePhase";
    private static final String ACTION_MODIFY_CAMPAGNEPHASE = "modifyCampagnePhase";
    private static final String ACTION_REMOVE_CAMPAGNEPHASE = "removeCampagnePhase";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAGNEPHASE = "confirmRemoveCampagnePhase";

    // Infos
    private static final String INFO_CAMPAGNEPHASE_CREATED = "participatorybudget.info.campagnephase.created";
    private static final String INFO_CAMPAGNEPHASE_UPDATED = "participatorybudget.info.campagnephase.updated";
    private static final String INFO_CAMPAGNEPHASE_REMOVED = "participatorybudget.info.campagnephase.removed";
    
    // Session variable to store working values
    private CampagnePhase _campagnephase;
    
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAGNEPHASES, defaultView = true )
    public String getManageCampagnePhases( HttpServletRequest request )
    {
        _campagnephase = null;
        List<CampagnePhase> listCampagnePhases = (List<CampagnePhase>) CampagnePhaseHome.getCampagnePhasesList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAGNEPHASE_LIST, listCampagnePhases, JSP_MANAGE_CAMPAGNEPHASES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNEPHASES, TEMPLATE_MANAGE_CAMPAGNEPHASES, model );
    }

    /**
     * Returns the form to create a campagnephase
     *
     * @param request The Http request
     * @return the html code of the campagnephase form
     */
    @View( VIEW_CREATE_CAMPAGNEPHASE )
    public String getCreateCampagnePhase( HttpServletRequest request )
    {
        _campagnephase = ( _campagnephase != null ) ? _campagnephase : new CampagnePhase(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_CAMPAGNEPHASE, _campagnephase );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAGNEPHASE, TEMPLATE_CREATE_CAMPAGNEPHASE, model );
    }

    /**
     * Process the data capture form of a new campagnephase
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAGNEPHASE )
    public String doCreateCampagnePhase( HttpServletRequest request )
    {
        populate( _campagnephase, request );

        // Check constraints
        if ( !validateBean( _campagnephase, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAGNEPHASE );
        }

        CampagnePhaseHome.create( _campagnephase );
        addInfo( INFO_CAMPAGNEPHASE_CREATED, getLocale(  ) );

        CampagnesService.getInstance().reset();
        
        return redirectView( request, VIEW_MANAGE_CAMPAGNEPHASES );
    }

    /**
     * Manages the removal form of a campagnephase whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAGNEPHASE )
    public String getConfirmRemoveCampagnePhase( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEPHASE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAGNEPHASE ) );
        url.addParameter( PARAMETER_ID_CAMPAGNEPHASE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAGNEPHASE,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campagnephase
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage campagnephases
     */
    @Action( ACTION_REMOVE_CAMPAGNEPHASE )
    public String doRemoveCampagnePhase( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEPHASE ) );
        CampagnePhaseHome.remove( nId );
        addInfo( INFO_CAMPAGNEPHASE_REMOVED, getLocale(  ) );

        CampagnesService.getInstance().reset();
        
        return redirectView( request, VIEW_MANAGE_CAMPAGNEPHASES );
    }

    /**
     * Returns the form to update info about a campagnephase
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAGNEPHASE )
    public String getModifyCampagnePhase( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEPHASE ) );

        if ( _campagnephase == null || ( _campagnephase.getId(  ) != nId ))
        {
            _campagnephase = CampagnePhaseHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_CAMPAGNEPHASE, _campagnephase );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNEPHASE, TEMPLATE_MODIFY_CAMPAGNEPHASE, model );
    }

    /**
     * Process the change form of a campagnephase
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAGNEPHASE )
    public String doModifyCampagnePhase( HttpServletRequest request )
    {
        populate( _campagnephase, request );

        // Check constraints
        if ( !validateBean( _campagnephase, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAGNEPHASE, PARAMETER_ID_CAMPAGNEPHASE, _campagnephase.getId( ) );
        }

        CampagnePhaseHome.update( _campagnephase );
        addInfo( INFO_CAMPAGNEPHASE_UPDATED, getLocale(  ) );

        CampagnesService.getInstance().reset();
        
        return redirectView( request, VIEW_MANAGE_CAMPAGNEPHASES );
    }
}
