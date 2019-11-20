package fr.paris.lutece.plugins.budgetparticipatif.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.leaflet.business.GeolocItem;
import fr.paris.lutece.plugins.leaflet.service.IconService;
import fr.paris.lutece.plugins.search.solr.business.SolrSearchEngine;
import fr.paris.lutece.plugins.search.solr.business.SolrSearchResult;
import fr.paris.lutece.plugins.search.solr.indexer.SolrItem;
import fr.paris.lutece.plugins.search.solr.service.ISolrSearchAppAddOn;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ProjectOPSolrAddon implements ISolrSearchAppAddOn {
    
    //Parameters copied from SolrSearchApp
    private static final String SOLRSEARCHAPP_PARAMETER_CONF             = "conf";
    private static final String SOLRSEARCHAPP_MARK_POINTS_GEOJSON        = "geojson";
    private static final String SOLRSEARCHAPP_MARK_POINTS_ID             = "id";
    private static final String SOLRSEARCHAPP_MARK_POINTS_FIELDCODE      = "code";
    private static final String SOLRSEARCHAPP_MARK_POINTS_TYPE           = "type";
    private static final String SOLRSEARCHAPP_PROPERTY_SOLR_RESPONSE_MAX = "solr.reponse.max";
    private static final int    SOLRSEARCHAPP_SOLR_RESPONSE_MAX          = Integer.parseInt(AppPropertiesService.getProperty(SOLRSEARCHAPP_PROPERTY_SOLR_RESPONSE_MAX, "50"));

    private static final String PARAMETER_CONF_MAP_PROJETS  = "map_projects";  // Vue du suivi des realisations

    private static final String PROPERTY_PROJECTS_OP_FQ_NOT_CANCELLED_WITH_ID = "budgetparticipatif.projects_op.fq.notCancelledWithId";
    private static final String PROPERTY_PROJECTS_OP_FQ_NOT_CANCELLED         = "budgetparticipatif.projects_op.fq.notCancelled";

    private static final String SOLR_QUERY_ALL                            = "*:*";
    private static final String SOLR_FQ_PROJECTS_OP_NOT_CANCELLED         = AppPropertiesService.getProperty(PROPERTY_PROJECTS_OP_FQ_NOT_CANCELLED        , "type:Projet 2015 OP\" AND -statut_text:\"Abandonné");
    private static final String SOLR_FQ_PROJECTS_OP_NOT_CANCELLED_WITH_ID = AppPropertiesService.getProperty(PROPERTY_PROJECTS_OP_FQ_NOT_CANCELLED_WITH_ID, "type:Projet 2015 OP\" AND -statut_text:Abandonné AND document_suivi_synt_text_text:\"");
    
    // Freemarker model properties
    private static final String MARK_PROJECTS_POINTS    = "points";
    private static final String MARK_PROJECTS_OP_POINTS = "projects_op_points";

	/**
	 * Ajoute les chantiers operationnels.
	 */
	public void buildPageAddOn(Map<String, Object> model, HttpServletRequest request) {
	    if ( PARAMETER_CONF_MAP_PROJETS.equals(request.getParameter( SOLRSEARCHAPP_PARAMETER_CONF )) ) {

	    	// Markers list
		    List<HashMap<String, Object>> points = new ArrayList<HashMap<String, Object>>( );

		    SolrSearchEngine engine = SolrSearchEngine.getInstance();

		    // Try to know if a SOLR filter was applicated. If no, getting all not cancelled ProjectOP to simplify processing (and optimize time response).  
		    // FIXME: better way of doing this ?
		    String[] fqRequestParams = request.getParameterValues("fq");
		    boolean filteredProjects =
		    	   containsSubString(fqRequestParams, "localisation_text")
		    	|| containsSubString(fqRequestParams, "thematique_text")
		    	|| containsSubString(fqRequestParams, "campagne_text")
		    	|| containsSubString(fqRequestParams, "step1_date")
		    	|| containsSubString(fqRequestParams, "step2_date")
		    	|| containsSubString(fqRequestParams, "step3_date")
		    	|| containsSubString(fqRequestParams, "step4_date")
		    	|| containsSubString(fqRequestParams, "step5_date")
		    	|| containsSubString(fqRequestParams, "statut_eudo_text")
		    	|| StringUtils.isNotBlank(request.getParameter("query"));
		    
		    if ( !filteredProjects )
		    {
			    // Searching all ProjectOP
			    List<SolrSearchResult> listResultsGeolocNotCancelled = engine.getGeolocSearchResults( 
		    		SOLR_QUERY_ALL,
		    		new String[]  { SOLR_FQ_PROJECTS_OP_NOT_CANCELLED }, 
		    		SOLRSEARCHAPP_SOLR_RESPONSE_MAX 
			    );
			    points.addAll(getGeolocModel( listResultsGeolocNotCancelled, null ));
		    }
		    else
		    {
			    // Searching Projects OP of each Project
			    List<HashMap<String, Object>> projectPoints = (List<HashMap<String, Object>>) model.get(MARK_PROJECTS_POINTS);
			    if ( projectPoints != null )
			    {
			    	for (HashMap<String, Object> result : projectPoints) {
			    		String projectId = (String) result.get("id");
				    	
					    List<SolrSearchResult> listResultsGeolocNotCancelled = engine.getGeolocSearchResults( 
				    		SOLR_QUERY_ALL,
				    		new String[]  { SOLR_FQ_PROJECTS_OP_NOT_CANCELLED_WITH_ID + projectId }, 
				    		SOLRSEARCHAPP_SOLR_RESPONSE_MAX 
					    );
					    
					    points.addAll(getGeolocModel( listResultsGeolocNotCancelled, null ));
					}
			    }
		    }
            
            model.put( MARK_PROJECTS_OP_POINTS, points );
        }
	}

	/**
	 * Returns true if a String[] contains a string as a substring, else returns false.
	 */
	private boolean containsSubString(String[] strs, String strToFind) {
		for (String str : strs)
			if ( StringUtils.contains(str, strToFind) )
				return true;
		return false;
	}
	
    /**
     * CopyPasted from SolrSearchApp to have the same freemarkers as if it was a search
     */
    private static List<HashMap<String, Object>> getGeolocModel( List<SolrSearchResult> listResultsGeoloc, String icon ) {
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

                        if ( icon != null ) {
                        	strIcon = icon;
                        }
                        else if ( iconKeysCache.containsKey( geolocItem.getIcon(  ) ) )
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
