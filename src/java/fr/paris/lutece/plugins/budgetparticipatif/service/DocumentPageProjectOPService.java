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
package fr.paris.lutece.plugins.budgetparticipatif.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.leaflet.business.GeolocItem;
import fr.paris.lutece.plugins.leaflet.service.IconService;
import fr.paris.lutece.plugins.search.solr.business.SolrSearchEngine;
import fr.paris.lutece.plugins.search.solr.business.SolrSearchResult;
import fr.paris.lutece.plugins.search.solr.indexer.SolrItem;
import fr.paris.lutece.portal.business.resourceenhancer.IResourceDisplayManager;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * Manager for add on display
 * TODO : move this class into a document specific class !
 */
public class DocumentPageProjectOPService implements IResourceDisplayManager
{
    private static final String SOLR_QUERY_ALL = "*:*";
	private static final String SOLR_FQ_PROJECTS_OP_SUIVI_SYNT = "document_suivi_synt_text_text:";
	private static final String SOLR_FQ_PROJECTS_OP_STATUT     = "-statut_text:";

	private static final String SOLRSEARCHAPP_PROPERTY_SOLR_RESPONSE_MAX = "solr.reponse.max";
    private static final int SOLRSEARCHAPP_SOLR_RESPONSE_MAX = Integer.parseInt(AppPropertiesService.getProperty(SOLRSEARCHAPP_PROPERTY_SOLR_RESPONSE_MAX, "50"));

    private static final String MARK_STATUS = "status_document";
    private static final String MARK_STATUS_VOTED = "SUIVI";

    private static final String MARK_PROJECTS_OP_POINTS = "projects_op_points";

    private static final String SOLRSEARCHAPP_MARK_POINTS_GEOJSON = "geojson";
    private static final String SOLRSEARCHAPP_MARK_POINTS_ID = "id";
    private static final String SOLRSEARCHAPP_MARK_POINTS_FIELDCODE = "code";
    private static final String SOLRSEARCHAPP_MARK_POINTS_TYPE = "type";

    @Override
    public void getXmlAddOn( StringBuffer strXml, String strResourceType, int nResourceId )
    {
    	return ;
    }

    @Override
    public void buildPageAddOn( Map<String, Object> model, String strResourceType, int nIdResource,
        String strPortletId, HttpServletRequest request )
    {
    	// Getting "Projet 2015 OP" child documents from root "Projet 2015" document
    	List<HashMap<String, Object>> points = null;

    	// Only for voted project !
    	Object isVotedProject = model.get(MARK_STATUS);
    	if ( MARK_STATUS_VOTED.equals(isVotedProject) )
    	{
	    	String[] strQuery = { SOLR_FQ_PROJECTS_OP_SUIVI_SYNT + nIdResource , SOLR_FQ_PROJECTS_OP_STATUT + "Obsolète" , SOLR_FQ_PROJECTS_OP_STATUT + "Abandonné" , SOLR_FQ_PROJECTS_OP_STATUT + "Suppression" , SOLR_FQ_PROJECTS_OP_STATUT + "Reliquat" };
	        SolrSearchEngine engine = SolrSearchEngine.getInstance();
	        List<SolrSearchResult> results = engine.getGeolocSearchResults(SOLR_QUERY_ALL, strQuery, SOLRSEARCHAPP_SOLR_RESPONSE_MAX);
	        points = getGeolocModel(results);
    	}
    	else
    	{
    		points = new ArrayList<HashMap<String, Object>>( );
    	}
    	
        // Putting them in freemarker model
        model.put( MARK_PROJECTS_OP_POINTS, points );
	}

    /**
     * CopyPasted from ProjectOPSolrAddon to have the same freemarkers as if it was a search
     */
    private static List<HashMap<String, Object>> getGeolocModel( List<SolrSearchResult> listResultsGeoloc ) {
        List<HashMap<String, Object>> points = new ArrayList<HashMap<String, Object>>( listResultsGeoloc.size(  ) );
        HashMap<String, String> iconKeysCache = new HashMap<String, String>(  );
 
        for ( SolrSearchResult result : listResultsGeoloc )
        {
            Map<String, Object> dynamicFields = result.getDynamicFields(  );

            for ( String key : dynamicFields.keySet(  ) )
            {
                if ( key.endsWith( SolrItem.DYNAMIC_GEOJSON_FIELD_SUFFIX ) )
                {
                    HashMap<String, Object> h = new HashMap<String, Object>(  );
                    String strJson = (String) dynamicFields.get( key );
                    GeolocItem geolocItem = null;

                    try
                    {
                        geolocItem = GeolocItem.fromJSON( strJson );
                    }
                    catch ( IOException e )
                    {
                        AppLogService.error( "SolrSearchApp: error parsing geoloc JSON: " + strJson +
                            ", exception " + e );
                    }

                    if ( geolocItem != null )
                    {
                        String strType = result.getId(  ).substring( result.getId(  ).lastIndexOf( "_" ) + 1 );
                        String strIcon;

                        if ( iconKeysCache.containsKey( geolocItem.getIcon(  ) ) )
                        {
                            strIcon = iconKeysCache.get( geolocItem.getIcon(  ) );
                        }
                        else
                        {
                            strIcon = IconService.getIcon( strType, geolocItem.getIcon(  ) );
                            iconKeysCache.put( geolocItem.getIcon(  ), strIcon );
                        }

                        geolocItem.setIcon( strIcon );
                        h.put( SOLRSEARCHAPP_MARK_POINTS_GEOJSON, geolocItem.toJSON(  ) );
                        h.put( SOLRSEARCHAPP_MARK_POINTS_ID,
                            result.getId(  )
                                  .substring( result.getId(  ).indexOf( "_" ) + 1,
                                result.getId(  ).lastIndexOf( "_" ) ) );
                        h.put( SOLRSEARCHAPP_MARK_POINTS_FIELDCODE, key.substring( 0, key.lastIndexOf( "_" ) ) );
                        if(strType.equals("doc")){
                        	
                        	h.put( SOLRSEARCHAPP_MARK_POINTS_TYPE, "gagnant" );
                        
                        }
                        points.add( h );
                    }
                }
            }
        }
        return points;
    }
  
}
