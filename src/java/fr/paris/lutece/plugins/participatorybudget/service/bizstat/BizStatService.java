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
package fr.paris.lutece.plugins.participatorybudget.service.bizstat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.participatorybudget.service.ProjectService;
import fr.paris.lutece.plugins.search.solr.business.SolrServerService;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class BizStatService
{
	
    private static final String SOLR_QUERY_ALL             = "*:*";
    private static final String SOLR_FACET_ALL_PROJECTS    = "type:\"PB Project\"";
    private static final String SOLR_FACET_WINNER_PROJECTS = "statut_project_text:\"SUIVI\" AND type:\"PB Project\"";

    // *********************************************************************************************
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // *********************************************************************************************

    private static final String BEAN_BIZ_STAT="participatorybudget.bizStatService";
	private static BizStatService _singleton;
	
	public static BizStatService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_BIZ_STAT );
        }
        return _singleton;
    }

    // *********************************************************************************************
    // * REALIZATION REALIZATION REALIZATION REALIZATION REALIZATION REALIZATION REALIZATION REALI *
    // * REALIZATION REALIZATION REALIZATION REALIZATION REALIZATION REALIZATION REALIZATION REALI *
    // *********************************************************************************************

	/**
	 * Returns the number of notifiable users when updating and notifying realization.
	 * 
	 * Column ¤1 : Id Doc Lutece of winner project
	 * Column ¤2 : nb of depositaries of initial ideas
	 * Column ¤3 : nb of followers of initial ideas
	 * Column ¤4 : nb of followers of winner project
	 */
	@BizStatDescription (value = "SUIVI DE PROJET - Nombre d'usagers ciblés lors d'une notification")
	public List<String[]> export_NumberOfNotifiableUsersWhenNotifyingOnRealization( ) throws Exception
	{
		List<String[]> rows = new ArrayList<String[]>();
		
		rows.add("Id Doc Lutece¤Nb followers projet gagnant¤Nb total dedoublonne".split("¤"));
		
		// Getting only once subscriptions
		
		SubscriptionFilter filterSubUpdate = new SubscriptionFilter();
		filterSubUpdate.setSubscriptionProvider( "participatoryideation.subscriptionProviderName" );
		filterSubUpdate.setSubscriptionKey     ( "updateOnRealization"               );
		List<Subscription> listSubUpdate = SubscriptionService.getInstance().findByFilter( filterSubUpdate );
		
		// Getting winner projects using SOLR search
		
		SolrQuery query = new SolrQuery();
		query.setQuery( SOLR_QUERY_ALL );
		query.addFilterQuery( SOLR_FACET_WINNER_PROJECTS );
		query.addField("uid");
		query.addField("num_idea_long");
		query.setRows(10000);
		
		SolrClient solrServer = SolrServerService.getInstance(  ).getSolrServer(  );
        QueryResponse response = solrServer.query( query );
        SolrDocumentList documentList = response.getResults();

        // Processing each winner project
        
        for (SolrDocument solrDocument : documentList) {
        	
			String docId = ((String) solrDocument.getFieldValue("uid")).split("_")[1]; // Assuming the format is 'Lutece_3789_doc'
			
			Set<String> projectNotifiableFollowers = new HashSet<String>();
			Set<String> uniqueNotifiable           = new HashSet<String>();

			// Adding project followers
            Document document = DocumentHome.findByPrimaryKey( Integer.parseInt(docId) );
            projectNotifiableFollowers.addAll( getActiveSubscribersGuid( listSubUpdate, ProjectService.getInstance().getUniqueUserGuidsProjectFollowers( document ) ) );	
            
            // Calculating unique notifiable users
            uniqueNotifiable.addAll( projectNotifiableFollowers );

            // Adding final row
	        rows.add( (	docId + "¤" + 
                       	projectNotifiableFollowers.size() + "¤" + 
                       	uniqueNotifiable.size()
                      ).split("¤"));

		}	    
		
		return rows;
	}
	
	// *********************************************************************************************
	// * SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER S *
	// * SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER S *
	// *********************************************************************************************

    /**
     * Returns a 'Set' which contains only users the notify of whom is activated.
     */
    private Set<String> getActiveSubscribersGuid ( List<Subscription> listSubUpdate, Set<String> userGuids ) {

	    Set<String> activeSubscriberGuids = new HashSet<String>();
	    
		for ( Subscription subscription : listSubUpdate )
	    {
			if ( userGuids.contains( subscription.getUserId() ) )
			{
				activeSubscriberGuids.add( subscription.getUserId() );
			}
	    }
		
		return activeSubscriberGuids;
    }
    
	// *********************************************************************************************
	// * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS *
	// * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS *
	// *********************************************************************************************

    /**
     * Returns text value of attribute, else "(... is null)" if document or attribute is null.
     */
    private String getAttributeTextValueSafe ( Document document, String attributeCode ) {

    	if ( document == null ) return "(document is null)";
    	
    	DocumentAttribute documentAttribute = document.getAttribute( attributeCode );
    	
    	if ( documentAttribute == null ) return "(attribute is null)";
    	
    	return StringUtils.isBlank( documentAttribute.getTextValue() ) ? "" : documentAttribute.getTextValue();
    }
    
}



