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
package fr.paris.lutece.plugins.participatorybudget.web;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import fr.paris.lutece.plugins.participatorybudget.business.MyInfosForm;
import fr.paris.lutece.plugins.participatorybudget.service.MyInfosService;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.plugins.participatorybudget.util.BudgetUtils;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.xpages.XPage;

/**
 * This class provides the user interface to manage ParisConnectUser xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = ProjectsXpage.PAGE_SOLR_PROJECT_SEARCH, pageTitleI18nKey = "participatorybudget.xpage.solrProjectSearch.pageTitle", pagePathI18nKey = "participatorybudget.xpage.solrProjectSearch.pagePathLabel" )
public class ProjectsXpage extends MVCApplication
{

    private static final long serialVersionUID = -4316691400124512416L;
    private static final Logger LOGGER = Logger.getLogger( ProjectsXpage.class );

    // Properties
    public static final String PROJECT = AppPropertiesService.getProperty( "participatorybudget.type.project" );
    public static final String LOCALISATION = AppPropertiesService.getProperty( "participatorybudget.name.localisation_text" );

    // Views
    public static final String PAGE_SOLR_PROJECT_SEARCH = "solrProjectSearch";

    // Parameters
    private static final String PARAMETER_REMOVE_ARR_FILTER = "remove_arr";
    private static final String PARAMETER_FACET_QUERY = "fq";
    private static final String PARAMETER_SORT_ORDER = "sort_order";
    private static final String PARAMETER_SORT_NAME = "sort_name";
    private static final String PARAMETER_QUERY = "query";

    // Marks
    private static final String MARK_CONF = "conf";

    // Constants
    private static final String strSearchSolrUrl = "Portal.jsp?page=search-solr";
    private static final String strArrondissementPattern = "75(020|00[1-9]|116|01[0-9])$";
    private static final String strUserRemoveArrFilterSelected = "on";
    private static final String strAmpSymbol = "&";
    private static final String strEqualSymbol = "=";
    private static final String strDoublePointSymbol = ":";

    @View( value = PAGE_SOLR_PROJECT_SEARCH, defaultView = true )
    public XPage solrProjectSearch( HttpServletRequest request ) throws ServletException, IOException
    {
        StringBuilder sbReq = new StringBuilder( strSearchSolrUrl );

        // Retrieve the search configuration
        String strConf = request.getParameter( MARK_CONF );
        if ( StringUtils.isNotBlank( strConf ) )
        {
            sbReq.append( strAmpSymbol + MARK_CONF + strEqualSymbol + strConf );
        }

        // Retrieve facet query from request
        String [ ] facetQuery = request.getParameterValues( PARAMETER_FACET_QUERY );
        boolean locationFilterActivated = false;
        if ( facetQuery != null && facetQuery.length > 0 )
        {
            for ( String strFacet : facetQuery )
            {
                if ( !locationFilterActivated )
                {
                    locationFilterActivated = ( StringUtils.isNotBlank( strFacet ) && strFacet.contains( LOCALISATION ) ) ? true : false;
                }
                sbReq.append( strAmpSymbol + "fq" + strEqualSymbol + strFacet );
            }
        }

        // Check if the user has remove the filter on his borough
        String strRemoveUserArrFilter = request.getParameter( PARAMETER_REMOVE_ARR_FILTER );
        boolean removeUserArrFilterSelected = ( StringUtils.isNotBlank( strRemoveUserArrFilter ) && strUserRemoveArrFilterSelected
                .equals( strRemoveUserArrFilter ) ) ? Boolean.TRUE : Boolean.FALSE;
        if ( !locationFilterActivated && removeUserArrFilterSelected )
        {
            sbReq.append( strAmpSymbol + PARAMETER_REMOVE_ARR_FILTER + strEqualSymbol + strRemoveUserArrFilter );
        }

        // Check if user is connected and which project location he wants to see.
        // Filtering projects only :
        // - User connected
        // - User do not clicked on "remove arrdt filter" case
        // - User do not use "localisation" SOLR facet
        // - Campagne is in SUBMIT / VOTE phase.
        // - SOLR page is "projects_mdp"
        // - User has a vote arrdt
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        if ( user != null && !removeUserArrFilterSelected && !locationFilterActivated && "projects_mdp".equals( strConf )
                && CampaignService.getInstance( ).isAfterBeginning( "SUBMIT" ) && CampaignService.getInstance( ).isBeforeEnd( "VOTE" ) )
        {
            String strArrt = getArrondissement( user );
            if ( StringUtils.isNotBlank( strArrt ) )
            {
                sbReq.append( "&fq=" + LOCALISATION + strDoublePointSymbol + strArrt + "\" " + "OR" + " " + LOCALISATION + strDoublePointSymbol + "\""
                        + "Whole city" );
            }

        }

        // Mange the sort of the query
        String strSortOrder = request.getParameter( PARAMETER_SORT_ORDER );
        if ( StringUtils.isNotBlank( strSortOrder ) )
        {
            sbReq.append( "&sort_order=" + strSortOrder );
        }
        else
        {
            sbReq.append( "&sort_order=asc" );
        }

        String strSortName = request.getParameter( PARAMETER_SORT_NAME );
        if ( StringUtils.isNotBlank( strSortName ) )
        {
            sbReq.append( "&sort_name=" + strSortName );
        }
        else
        {
            sbReq.append( "&sort_name=title" );
        }

        // Manage the query
        String strQuery = request.getParameter( PARAMETER_QUERY );
        if ( StringUtils.isNotBlank( strQuery ) )
        {
            sbReq.append( "&query=" + strQuery );
        }
        else
        {
            sbReq.append( "&query=*:*" );
        }

        LOGGER.debug( "Requête SOLR de date, redirection vers " + sbReq.toString( ) );

        UriComponents uriComponents = UriComponentsBuilder.fromUriString( sbReq.toString( ) ).build( );
        String strEncodedUri = uriComponents.encode( "UTF-8" ).toUriString( );

        // Make the redirection
        HttpServletResponse response = LocalVariables.getResponse( );
        response.sendRedirect( strEncodedUri );

        return new XPage( );
    }

    /**
     * Get arrondissement of a user
     * 
     * @param request
     * @return
     */
    private static String getArrondissement( LuteceUser user )
    {
        if ( user != null )
        {
            MyInfosForm myInfo = MyInfosService.loadUserInfos( user );
            Pattern p = Pattern.compile( strArrondissementPattern );
            Matcher m = p.matcher( myInfo.getArrondissement( ) );
            if ( m.matches( ) )
            {
                return Integer.valueOf( myInfo.getArrondissement( ).substring( 2 ) ) + "e arrondissement";
            }
        }
        return StringUtils.EMPTY;
    }

}
