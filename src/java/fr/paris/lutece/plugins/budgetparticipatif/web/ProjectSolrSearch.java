package fr.paris.lutece.plugins.budgetparticipatif.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import fr.paris.lutece.plugins.campagnebp.business.MyInfosForm;
import fr.paris.lutece.plugins.campagnebp.service.MyInfosService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ProjectSolrSearch extends HttpServlet {

	  
	 	private static final long serialVersionUID = -806886865678792152L;
	    private static final String MARK_THEMATIQUE = "thematique";
	    private static final String MARK_ARRONDISSEMENT = "arrondissement";
	    private static final String MARK_ARROND_USER = "user_arrondissement";
	    private static final String MARK_CATEGORY = "categorie";
	    private static final String MARK_QUERY = "query";
	    private static final String MARK_BUDGET = "budget";
	    private static final String MARK_SORT = "sort";
		private static final String MARK_SORT_ORDER = "sort_order";
	    private static final String MARK_SORT_ALPHABETIQUE = "alpha";
	    private static final String MARK_SORT_DATE = "date";
	    private static final String MARK_TYPE_SEARCH = "type_search";
	    private static final String SEARCH_SIMPLE = "simple";
	    private static final String SEARCH_AVANCEE = "avancee";
	    private static final String SEARCH_THEMATIQUE ="thematique";
	    private static final String SEARCH_ARRONDISSEMENT ="arrondissement";
	    
	    private static final Logger LOGGER = Logger.getLogger( ProjectSolrSearch.class );
	    
	    
	    public static final String PROJECT = AppPropertiesService.getProperty( "budgetparticipatif.type.project" );
	    public static final String THEMATIQUE = AppPropertiesService.getProperty( "budgetparticipatif.name.thematique" );
	    public static final String LOCALISATION = AppPropertiesService.getProperty( "budgetparticipatif.name.localisation_text" );
	    public static final String BUDGET = AppPropertiesService.getProperty( "budgetparticipatif.name.budget" );
	    /**
	     * Get Project specific parameters and call Solr Module.
	     *
	     * @param request the request
	     * @param response the response
	     * @throws ServletException the servlet exception
	     * @throws IOException Signals that an I/O exception has occurred.
	     */
	    @Override
	    protected void doGet( HttpServletRequest request, HttpServletResponse response )
	        throws ServletException, IOException
	    {
	    	LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
	    	boolean byProject = false; 
	    	StringBuilder sbReq = new StringBuilder( "Portal.jsp?page=search-solr" );
	    	
	        // Sorts 
	        if (StringUtils.isNotEmpty(request.getParameter( MARK_SORT )))
	        {
	        	sbReq.append("&sort_name="+request.getParameter( MARK_SORT ));
	        }
			if (StringUtils.isNotEmpty(request.getParameter( MARK_SORT_ORDER )))
	        {
	        	sbReq.append("&sort_order="+request.getParameter( MARK_SORT_ORDER ));
	        }
					
	        //By Input search
	        if (StringUtils.isNotEmpty(request.getParameter( MARK_QUERY )))
	        {
	        	String strQuery= request.getParameter( MARK_QUERY ).replaceAll(":", "");
	        	sbReq.append( "&query=title:"+strQuery+" OR summary:"+strQuery+" OR identifiant_text:"+strQuery);
	        	sbReq.append( "&fq=type:"+PROJECT );
	        	byProject = true;
	        }
	      //By Budget
	        if (StringUtils.isNotEmpty(request.getParameter( MARK_BUDGET )))
	        {
	        	sbReq.append("&query="+BUDGET+":"+request.getParameter( MARK_BUDGET ));
	        	if (!byProject)
	        	{
	        		byProject = true;
	        		sbReq.append( "&fq=type:"+PROJECT );
	        	}
	        	
	        }// By Arrondissement
	        if (StringUtils.isNotEmpty(getArrondissement(request, user)) && !request.getParameter( MARK_ARRONDISSEMENT ).isEmpty( ))
	        {
	        	sbReq.append( "&fq="+LOCALISATION+":"+getArrondissement(request, user));
        		if (!byProject)
        		{
        			byProject = true;

        			sbReq.append( "&fq=type:"+PROJECT );
        		}
	        } else {
	        	if (!byProject)
	        		sbReq.append( "&query=*:*&fq=type:"+PROJECT);
	        }
	        //By THEMATIQUE
	        
	        LOGGER.debug( "Requête SOLR de date, redirection vers " + sbReq.toString(  ) );
	        
	        UriComponents uriComponents = UriComponentsBuilder.fromUriString( sbReq.toString( ) ).build( );
	        String strEncodedUri = uriComponents.encode( "UTF-8" ).toUriString();

	        // Make the redirection
	        response.sendRedirect( strEncodedUri );
	    }
	    /**
	     * Get Project specific parameters and call Solr Module.
	     *
	     * @param request the request
	     * @param response the response
	     * @throws ServletException the servlet exception
	     * @throws IOException Signals that an I/O exception has occurred.
	     */
	    protected void doGetOld( HttpServletRequest request, HttpServletResponse response )
	        throws ServletException, IOException
	    {
	    	LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
	    	StringBuilder sbReq = new StringBuilder( "Portal.jsp?page=search-solr" );

	        String sTypeSearch = request.getParameter( MARK_TYPE_SEARCH );
	        String sTypeSort = request.getParameter( MARK_SORT );

	        StringBuilder sbFilter = new StringBuilder( "" );
	        StringBuilder sbSort = null;
	        String sQuery = request.getParameter( MARK_QUERY );

	        if ( StringUtils.isNotEmpty( sTypeSort ) && MARK_SORT_ALPHABETIQUE.equals( sTypeSort ) )
	        {
	            // sort by title
	            sbSort = new StringBuilder( "&sort_name=title&sort_order=asc" );
	        }
	        else
	        {
	            // Sort by date
	            sTypeSort = MARK_SORT_DATE;
	            sbSort = new StringBuilder( "&sort_order=desc" );
	        }

	        if ( (StringUtils.isEmpty( sTypeSearch ) || SEARCH_SIMPLE.equals( sTypeSearch )) && user == null )
	        {
	            // simple search with facets 
	            sTypeSearch = SEARCH_SIMPLE;
	            sQuery= sQuery.replaceAll(":", "");
	            
	            if ( StringUtils.isNotEmpty( sQuery ) &&   StringUtils.isNotEmpty(sQuery.trim()) && StringUtils.isNotBlank((sQuery.trim())))
	            {
	                sbReq.append( "&query=title:" + sQuery +" OR summary:"+ sQuery +" OR identifiant_text:"+sQuery);
	            }
	            sbFilter.append( "&fq=type:"+PROJECT );

	        }else if(SEARCH_THEMATIQUE.equals( sTypeSearch )  && user == null ){
	        	
	        	 String sThematique = request.getParameter( MARK_THEMATIQUE );
	        	 if ( StringUtils.isNotEmpty(sThematique))
		            {		           
			            //sbReq.append( "&query=*:*" );
			            sbFilter.append( "&fq="+THEMATIQUE+":"+sThematique);
		            }
		            sbFilter.append( "&fq=type:"+PROJECT);
	        }else if(SEARCH_ARRONDISSEMENT.equals( sTypeSearch )  ){
	        	
	        	 sbFilter.append( "&fq="+LOCALISATION+":"+getArrondissement(request, user));
		         sbFilter.append( "&fq=type:"+PROJECT );
	        }else if ( SEARCH_AVANCEE.equals( sTypeSearch ) )
	        {
	            SimpleDateFormat sdfXml = new SimpleDateFormat(  "yyyy-MM-dd'T'HH:mm:ss'Z'");
	            String sThematique = request.getParameter( MARK_THEMATIQUE );
	            String[] categories = request.getParameterValues( MARK_CATEGORY );

	            String sShowDateStart = request.getParameter( "show_date_start" );
	            String sShowDateEnd = request.getParameter( "show_date_end" );

	            if ( StringUtils.isNotEmpty(sThematique))
	            {
	            	sbReq.append( "&query="+THEMATIQUE+":" + sQuery);
	            }
	            sbFilter.append( "&fq=type:"+PROJECT );
	            

	            if ( StringUtils.isNotEmpty( sQuery ) && ArrayUtils.isEmpty( categories ) )
	            {
	                sbReq.append( "&query=" + sQuery );
	            }
	            else if ( StringUtils.isNotEmpty( sQuery ) && !ArrayUtils.isEmpty( categories ) )
	            {
	                sbReq.append( "&query=(" + sQuery );

	                sbFilter.append( " AND categorie:(" );

	                int i = 1;

	                for ( String categorie : categories )
	                {
	                    if ( i < categories.length )
	                    {
	                        sbFilter.append( categorie.replace( " ", "*" ) ).append( " OR " );
	                    }
	                    else
	                    {
	                        sbFilter.append( categorie.replace( " ", "*" ) );
	                    }

	                    i++;
	                }

	                sbFilter.append( "))" );
	            }
	            else if ( !ArrayUtils.isEmpty( categories ) )
	            {
	                sbFilter.append( "&query=categorie:(" );

	                int i = 1;

	                for ( String categorie : categories )
	                {
	                    if ( i < categories.length )
	                    {
	                        sbFilter.append( categorie.replace( " ", "*" ) ).append( " OR " );
	                    }
	                    else
	                    {
	                        sbFilter.append( categorie.replace( " ", "*" ) );
	                    }

	                    i++;
	                }

	                sbFilter.append( ")" );
	            }

	            if ( StringUtils.isNotEmpty( sShowDateStart ) )
	            {
	              //  Timestamp showDateStart = DateUtils.getDate( sShowDateStart, true );
	               // String sXmlShowDateStart = sdfXml.format( showDateStart );
	               // sbFilter.append( "&fq=end_date:[" ).append( sXmlShowDateStart ).append( " TO *]" );
	            }

	            if ( StringUtils.isNotEmpty( sShowDateEnd ) )
	            {
	               // Timestamp showDateEnd = DateUtils.getDate( sShowDateEnd, true );
	               // String sXmlShowDateEnd = sdfXml.format( showDateEnd );

	                //sbFilter.append( "&fq=start_date:[* TO " ).append( sXmlShowDateEnd ).append( "]" );
	            }
	        }

	        StringBuilder sbType = new StringBuilder( "&type_search=" + sTypeSearch );

	        if ( sbFilter.toString(  ).isEmpty(  ) && StringUtils.isEmpty( sQuery ) )
	        {
	            // Create default filter
	            sbReq.append( "&query=*:*" );
	        }
	        else
	        {
	            sbReq.append( sbFilter.toString(  ) );
	        }

	        sbReq.append( sbSort.toString(  ) );
	        sbReq.append( sbType.toString(  ) );

	        LOGGER.debug( "Requête SOLR de date, redirection vers " + sbReq.toString(  ) );

            UriComponents uriComponents = UriComponentsBuilder.fromUriString( sbReq.toString( ) ).build( );
            String strEncodedUri = uriComponents.encode( "UTF-8" ).toUriString();

            // Make the redirection
            response.sendRedirect( strEncodedUri );
	    }
	    /**
	     * Get arrondissement
	     * @param request
	     * @param user
	     * @return
	     */
	    private static String getArrondissement ( HttpServletRequest request, LuteceUser user )
	    {
	    	String strArrondissement = request.getParameter( MARK_ARRONDISSEMENT );
        	if ((StringUtils.isEmpty(strArrondissement) || strArrondissement.equals(MARK_ARROND_USER)) && 
        		user != null )
            {
               
                MyInfosForm myInfo = MyInfosService.loadUserInfos( user );
                Pattern p = Pattern.compile("75(020|00[1-9]|116|01[0-9])$");
                Matcher m = p.matcher(myInfo.getArrondissement());
                if (m.matches())
                	strArrondissement =Integer.valueOf(myInfo.getArrondissement().substring(2))+"e "+MARK_ARRONDISSEMENT;
                	                 
            }else if(strArrondissement!= null && strArrondissement.equals(MARK_ARROND_USER) && user == null){
            	
            	strArrondissement= StringUtils.EMPTY;
            }
        	return strArrondissement;
	    }
}
