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
package fr.paris.lutece.plugins.participatorybudget.service.vote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.participatorybudget.service.IDocumentBodyService;
import fr.paris.lutece.plugins.participatorybudget.service.DocumentBodyService;
import fr.paris.lutece.plugins.search.solr.business.SolrSearchResult;
import fr.paris.lutece.plugins.search.solr.service.ISolrSearchAppAddOn;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.portal.PortalMenuService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class VotesSolrAddon implements ISolrSearchAppAddOn
{

    private static final String MARK_RESULTS_LIST = "results_list";
    private static final String MARK_RESULTS_PROPOSALS_MAP = "results_proposal_map";
    private static final String PARAMETER_PREV_ITEMS_PER_PAGE = "prev_items_per_page";
    private static final String MARK_PREV_ITEMS_PER_PAGE = "prev_items_per_page";

    @Override
    public void buildPageAddOn( Map<String, Object> model, HttpServletRequest request )
    {
        Map<String, Object> mapAdditionalInfos = new HashMap<String, Object>( );
        List<SolrSearchResult> listResults = (List<SolrSearchResult>) model.get( MARK_RESULTS_LIST );

        IDocumentBodyService documentBodyService = SpringContextService.getBean( DocumentBodyService.BEAN_NAME );

        for ( SolrSearchResult solrSearchResult : listResults )
        {
            String solrDocPortletId = solrSearchResult.getDocPortletId( );
            String parsedSolrDocPortletId[] = solrDocPortletId.split( "&" );
            AppLogService.debug( "participatorybudget, fetching " + solrDocPortletId );
            if ( parsedSolrDocPortletId.length == 2 )
            {
                try
                {
                    mapAdditionalInfos.put( solrSearchResult.getId( ),
                            documentBodyService.getPage( request, parsedSolrDocPortletId [0], parsedSolrDocPortletId [1], PortalMenuService.MODE_NORMAL ) );
                }
                catch( UserNotSignedException e )
                {
                    AppLogService.error( "participatorybudget, SolrVoteAddon, got exception " + e, e );
                }
                catch( SiteMessageException e )
                {
                    AppLogService.error( "participatorybudget, SolrVoteAddon, got exception " + e, e );
                }
            }
            else
            {
                AppLogService.error( "participatorybudget, VotesSolrSaddon, Error parsing DocPortletId " + solrDocPortletId );
            }
        }
        model.put( MARK_RESULTS_PROPOSALS_MAP, mapAdditionalInfos );

        model.put( MARK_PREV_ITEMS_PER_PAGE, request.getParameter( PARAMETER_PREV_ITEMS_PER_PAGE ) );
    }
}
