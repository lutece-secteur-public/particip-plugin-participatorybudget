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
package fr.paris.lutece.plugins.participatorybudget.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.search.solr.business.SolrFacetedResult;
import fr.paris.lutece.plugins.search.solr.business.SolrSearchEngine;
import fr.paris.lutece.plugins.search.solr.business.SolrSearchResult;
import fr.paris.lutece.plugins.search.solr.service.ISolrSearchAppAddOn;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ProjectLaureatAddOn implements ISolrSearchAppAddOn
{

    // Parameters copied from SolrSearchApp
    private static final String PARAMETER_SORT_NAME = "sort_name";
    private static final String PARAMETER_SORT_ORDER = "sort_order";
    private static final String SOLRSEARCHAPP_PARAMETER_CONF = "conf";
    private static final String MARK_PROJECTS_TOUT_PARIS = "projectLaureatToutParis";
    private static final String PARAMETER_CONF_ELECTED_PROJECT = "elected_projects";
    private static final String PROPERTY_PROJECTS_FQ = "participatorybudget.projects.laureat.fq";
    private static final String [ ] SOLR_FQ_PROJECTS_TOUT_PARIS = {
        AppPropertiesService.getProperty( PROPERTY_PROJECTS_FQ, "(statut_project_text:GAGNANT') AND localisation_text:'whole_city' AND type:'PB Project" )
    };

    private static final String PROPERTY_SOLR_RESPONSE_MAX = "solr.reponse.max";
    private static final int SOLR_RESPONSE_MAX = Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_SOLR_RESPONSE_MAX, "100" ) );

    @Override
    public void buildPageAddOn( Map<String, Object> model, HttpServletRequest request )
    {

        if ( PARAMETER_CONF_ELECTED_PROJECT.equals( request.getParameter( SOLRSEARCHAPP_PARAMETER_CONF ) ) )
        {
            SolrSearchEngine engine = SolrSearchEngine.getInstance( );
            SolrFacetedResult facetedResult = engine.getFacetedSearchResults( "*:*", SOLR_FQ_PROJECTS_TOUT_PARIS, request.getParameter( PARAMETER_SORT_NAME ),
                    request.getParameter( PARAMETER_SORT_ORDER ), SOLR_RESPONSE_MAX, 1, SOLR_RESPONSE_MAX );
            List<SolrSearchResult> listResults = facetedResult.getSolrSearchResults( );

            model.put( MARK_PROJECTS_TOUT_PARIS, listResults );
        }

    }

}
