/*
 * Copyright (c) 2002-2020, Mairie de Paris
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
package fr.paris.lutece.plugins.participatorybudget.web.bizstat;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.participatorybudget.business.bizstat.BizStatFile;
import fr.paris.lutece.plugins.participatorybudget.business.bizstat.BizStatFileHome;
import fr.paris.lutece.plugins.participatorybudget.business.rgpd.RGPDTreatmentLogHome;
import fr.paris.lutece.plugins.participatorybudget.service.bizstat.BizStatDescription;
import fr.paris.lutece.plugins.participatorybudget.service.bizstat.BizStatService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

/**
 * This class provides the user interface to produce various business statistics
 */

@Controller( controllerJsp = "BizStat.jsp", controllerPath = "jsp/admin/plugins/participatorybudget/bizstat/", right = "BIZ_STAT" )
public class BizStatJspBean extends MVCAdminJspBean
{

    private static final long serialVersionUID = 1L;

    // RIGHTS
    public static final String RIGHT_MANAGE_BIZ_STAT = "BIZ_STAT";

    // TEMPLATES
    private static final String TEMPLATE_MANAGE_BIZ_STAT = "admin/plugins/participatorybudget/bizstat/biz_stat.html";

    // PROPERTIES
    private static final String PROPERTY_PAGE_TITLE_BIZ_STAT = "participatorybudget.bizStat.pageTitle";

    // PARAMETERS
    private static final String PARAMETER_METHOD_NAME = "method_name";
    private static final String PARAMETER_REASON = "reason";
    private static final String PARAMETER_EXPORT_ID = "export_id";

    // MARKERS
    private static final String MARK_PAGE_TITLE = "pageTitle";
    private static final String MARK_AVAILABLE_FILES = "files";
    private static final String MARK_LIST_METHOD = "methods";

    // VIEWS
    private static final String VIEW_MANAGE_BIZ_STAT = "manageBizStat";

    // ACTIONS
    private static final String ACTION_DO_EXPORT_CSV = "doExportCsv";
    private static final String ACTION_DO_DOWNLOAD_CSV = "doDownloadCsv";

    // *********************************************************************************************
    // * VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW *
    // * VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW VIEW *
    // *********************************************************************************************

    /**
     * Displaying business statistic forms
     */
    @View( value = VIEW_MANAGE_BIZ_STAT, defaultView = true )
    public String getManageBizStat( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );

        model.put( MARK_PAGE_TITLE, AppPropertiesService.getProperty( PROPERTY_PAGE_TITLE_BIZ_STAT, "Business Statistics" ) );

        // List of export methods from BizStatService (sorted by description)
        List<Method> methods = Arrays.asList( BizStatService.class.getDeclaredMethods( ) );
        methods.sort( new Comparator<Method>( )
        {
            @Override
            public int compare( Method o1, Method o2 )
            {
                BizStatDescription [ ] a1 = o1.getAnnotationsByType( BizStatDescription.class );
                BizStatDescription [ ] a2 = o2.getAnnotationsByType( BizStatDescription.class );

                String v1 = ( a1.length == 0 ? "" : a1 [0].value( ) );
                String v2 = ( a2.length == 0 ? "" : a2 [0].value( ) );

                return v1.compareTo( v2 );
            }
        } );
        model.put( MARK_LIST_METHOD, methods );

        // List of export files (sorted by creation_date)
        List<BizStatFile> files = BizStatFileHome.findAllWithoutBinaryContent( );
        files.sort( new Comparator<BizStatFile>( )
        {
            @Override
            public int compare( BizStatFile o1, BizStatFile o2 )
            {
                return o1.getCreationDate( ).compareTo( o2.getCreationDate( ) );
            }
        } );
        model.put( MARK_AVAILABLE_FILES, files );

        return getPage( PROPERTY_PAGE_TITLE_BIZ_STAT, TEMPLATE_MANAGE_BIZ_STAT, model );
    }

    // *********************************************************************************************
    // * EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPOR *
    // * EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPOR *
    // *********************************************************************************************

    /**
     * Generate CSV from requested stat.
     */
    @Action( ACTION_DO_EXPORT_CSV )
    public String doExportCSV( HttpServletRequest request ) throws Exception
    {
        String methodName = request.getParameter( PARAMETER_METHOD_NAME );
        String reason = request.getParameter( PARAMETER_REASON );
        if ( StringUtils.isBlank( methodName ) )
        {
            addError( "Veuillez sélectionner un type d'export." );
        }
        else
            if ( StringUtils.isBlank( reason ) )
            {
                addError( "Veuillez préciser la raison de la demande d'export." );
            }
            else
            {
                BizStatFile file = new BizStatFile( );
                file.setFileName( methodName );

                Method method = BizStatService.class.getMethod( methodName, null );
                if ( method != null )
                {
                    file.setDescription( method.getAnnotation( BizStatDescription.class ).value( ) );
                    file.setStatus( BizStatFile.STATUS_REQUESTED );
                    file.setReason( reason );

                    AdminUser user = getUser( );
                    file.setIdAdminUser( user.getUserId( ) );
                    file.setAdminUserAccessCode( user.getAccessCode( ) );
                    file.setAdminUserEmail( user.getEmail( ) );
                }
                else
                {
                    file.setError( "No such export method : '" + methodName + "' !" );
                    file.setStatus( BizStatFile.STATUT_ERROR );
                }

                BizStatFileHome.create( file );
            }

        return redirectView( request, VIEW_MANAGE_BIZ_STAT );
    }

    // *********************************************************************************************
    // * DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD *
    // * DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD DOWNLOAD *
    // *********************************************************************************************

    /**
     * Download CSV content from DB.
     */
    @Action( ACTION_DO_DOWNLOAD_CSV )
    public void doDownloadCSV( HttpServletRequest request ) throws Exception
    {
        String exportId = request.getParameter( PARAMETER_EXPORT_ID );
        if ( !StringUtils.isNumeric( exportId ) || StringUtils.isBlank( exportId ) )
        {
            addError( "Veuillez sélectionner l'export à télécharger." );
        }
        else
        {
            BizStatFile file = BizStatFileHome.findByPrimaryKey( Integer.parseInt( exportId ) );

            download( file.getValue( ), file.getFileName( ) + ".csv", "text/csv" );

            // [TODO] Should be logged when trying to access generated file !
            // RGPD : Logging treatment
            String strFile = new String( file.getValue( ) );
            String columnsLine = strFile.substring( 0, strFile.indexOf( 13 ) );
            RGPDTreatmentLogHome.create( getUser( ), "export", file.getFileName( ), columnsLine );
        }
    }

}
