package fr.paris.lutece.plugins.budgetparticipatif.service.bizstat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import fr.paris.lutece.plugins.budgetparticipatif.service.ProjectService;
import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.plugins.ideation.service.IdeeService;
import fr.paris.lutece.plugins.ideation.service.subscription.IdeationSubscriptionProviderService;
import fr.paris.lutece.plugins.search.solr.business.SolrServerService;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class BizStatService
{
	
    private static final String SOLR_QUERY_ALL             = "*:*";
    private static final String SOLR_FACET_ALL_PROJECTS    = "type:\"Projet 2015\"";
    private static final String SOLR_FACET_WINNER_PROJECTS = "statut_project_text:\"SUIVI\" AND type:\"Projet 2015\"";

    // *********************************************************************************************
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // *********************************************************************************************

    private static final String BEAN_BIZ_STAT="budgetparticipatif.bizStatService";
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
    // * PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITION *
    // * PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITIONS PROPOSITION *
    // *********************************************************************************************

	/**
	 * Returns the list of all propositions, with user mails.
	 */
	@BizStatDescription (value = "IDEATION - Liste des propositions déposées, avec mail du dépositaire")
	public List<String[]> export_AllPropositionsWithUserMails( ) throws Exception
	{
		List<String[]> rows = new ArrayList<String[]>();
		
		rows.add("Id Tech¤Campagne¤Id metier¤Titre¤Localisation¤Thematique¤Statut¤Id user¤Email¤Pseudo actuel¤Address¤Civility¤Birthdate".split("¤"));
		
		// Getting all propositions
		Collection<Idee> idees = IdeeHome.getIdeesList();
		
		for (Idee idee : idees) {
            // Adding final row
	        rows.add( (	idee.getId() + "¤" + 
	        			idee.getCodeCampagne()     + "¤" + 
	        			idee.getCodeIdee()    + "¤" + 
	        			idee.getTitre() + "¤" + 
	        			( StringUtils.isBlank(idee.getLocalisationArdt()) ? "Tout Paris" : idee.getLocalisationArdt() ) + "¤" +
	        			idee.getCodeTheme() + "¤" +
	        			idee.getStatusPublic() + "¤" +
	        			idee.getLuteceUserName() + "¤" +
	        			UserPreferencesService.instance().get( idee.getLuteceUserName(), "budgetparticipatif.email", "?") + "¤" + 
	        			UserPreferencesService.instance().get( idee.getLuteceUserName(), "portal.nickname", "?") + "¤" + 
	        			UserPreferencesService.instance().get( idee.getLuteceUserName(), "budgetparticipatif.address", "?") + "¤" + 
	        			UserPreferencesService.instance().get( idee.getLuteceUserName(), "budgetparticipatif.civility", "?") + "¤" + 
	        			UserPreferencesService.instance().get( idee.getLuteceUserName(), "budgetparticipatif.birthdate", "?") 
                      ).split("¤"));
		}
		
		return rows;
	}

    // *********************************************************************************************
    // * PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS *
    // * PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS PROJECTS *
    // *********************************************************************************************

	/**
	 * Returns the list of depositaries/followers associated to each submitted projet.
	 */
	@TimeConsuming
	@BizStatDescription (value = "PROJECTS - Liste des dépositaires et associés des projets soumis au vote (temps de traitement > 20 mn)")
	public List<String[]> export_AllDepositariesAndFollowersOfSubmittedProjectsAndAssociatedPropositions( ) throws Exception
	{
		List<String[]> rows = new ArrayList<String[]>();
		
		rows.add((
			  "Idée - Id tech¤Idée - Campagne¤Idée - Id métier¤Idée - Titre¤Idée - Type dépositaire¤Idée - Nom dépositaire¤Idée - Localisation¤Idée - Thematique¤Idée - QPop¤Idée - Statut¤"
			+ "Id user¤Email¤Pseudo actuel¤Address¤Civility¤Birthdate¤Type usager¤"
			+ "Projet - Id Doc Lutèce¤Projet - Titre¤Projet - Statut¤Projet - Thématique¤Projet - Localisation¤Projet - QPop¤"
			+ "Projet - Début études¤Projet - Début procédures¤Projet - Début travaux¤Projet - Début réalisation¤Projet - Fin réalisation"
		).split("¤"));
		
		// Getting winner projects using SOLR search
		
		SolrQuery query = new SolrQuery();
		query.setQuery( SOLR_QUERY_ALL );
		query.addFilterQuery( SOLR_FACET_ALL_PROJECTS );
		query.addField("uid");
		query.addField("num_idea_long");
		query.setRows(10000);
		
		SolrClient solrServer = SolrServerService.getInstance(  ).getSolrServer(  );
        QueryResponse response = solrServer.query( query );
        SolrDocumentList documentList = response.getResults();

		// Adding depositaries and followers of RETENU / REGROUPE propositions

        for (SolrDocument solrDocument : documentList) {
        	
			String            projectId     = ((String) solrDocument.getFieldValue("uid")).split("_")[1]; // Assuming the format is 'Lutece_3789_doc'
            Document          project       = DocumentHome.findByPrimaryKey( Integer.parseInt(projectId) );
			DocumentAttribute projectStatus = project.getAttribute("statut_project");
			
			// Getting list of propositions
            List<Integer> propIds = ProjectService.getInstance().getProjectSubPropositionIds( project, IdeeHome.GetSubIdeesMethod.ALL_FAMILY );
            if ( propIds != null )
            {
	            for (Integer propId : propIds) {
				
	            	Idee idee = IdeeHome.findByPrimaryKey( propId );
		            if ( idee != null )
		            {
			            // Adding depositary row
				        rows.add( (	idee.getId() + "¤" + 
				        			idee.getCodeCampagne()     + "¤" + 
				        			idee.getCodeIdee()    + "¤" + 
				        			idee.getTitre() + "¤" + 
				        			idee.getDepositaireType() + "¤" + 
				        			idee.getDepositaire() + "¤" + 
				        			( StringUtils.isBlank(idee.getLocalisationArdt()) ? "Tout Paris" : idee.getLocalisationArdt() ) + "¤" +
				        			idee.getCodeTheme() + "¤" +
				        			( StringUtils.isBlank(idee.getIdQpvQva()) ? "" : "(" + idee.getIdQpvQva() + ") " + idee.getLibelleQpvQva() ) + "¤" +
				        			idee.getStatusPublic() + "¤" +
				        			idee.getLuteceUserName() + "¤" +
				        			UserPreferencesService.instance().get( idee.getLuteceUserName(), "budgetparticipatif.email", "?") + "¤" + 
				        			UserPreferencesService.instance().get( idee.getLuteceUserName(), "portal.nickname", "?") + "¤" + 
				        			UserPreferencesService.instance().get( idee.getLuteceUserName(), "budgetparticipatif.address", "?") + "¤" + 
				        			UserPreferencesService.instance().get( idee.getLuteceUserName(), "budgetparticipatif.civility", "?") + "¤" + 
				        			UserPreferencesService.instance().get( idee.getLuteceUserName(), "budgetparticipatif.birthdate", "?") + "¤" +
				        			"Dépositaire de l'idée" + "¤" +
				        			project.getId() + "¤" +
				        			project.getTitle() + "¤" +
				        			( projectStatus == null ? "null" : projectStatus.getTextValue() ) + "¤" +
				        			getReliableAttributeTextValue( project, "thematique"   ) + "¤" +
				        			getReliableAttributeTextValue( project, "localisation" ) + "¤" +
				        			getReliableAttributeTextValue( project, "pop_district" ) + "¤" +
				        			getReliableAttributeTextValue( project, "step1"        ) + "¤" +
				        			getReliableAttributeTextValue( project, "step2"        ) + "¤" +
				        			getReliableAttributeTextValue( project, "step3"        ) + "¤" +
				        			getReliableAttributeTextValue( project, "step4"        ) + "¤" +
				        			getReliableAttributeTextValue( project, "step5"        ) + "¤"
			                      ).split("¤"));
				        
				        // Adding followers of proposition(s) rows
				        Set<String> guids = IdeeService.getInstance().getUniqueUserGuidsIdeesFollowers( Arrays.asList(propId) );
				        for (String guid : guids) {
					        rows.add( (	idee.getId() + "¤" + 
				        			idee.getCodeCampagne()     + "¤" + 
				        			idee.getCodeIdee()    + "¤" + 
				        			idee.getTitre() + "¤" + 
				        			idee.getDepositaireType() + "¤" + 
				        			idee.getDepositaire() + "¤" + 
				        			( StringUtils.isBlank(idee.getLocalisationArdt()) ? "Tout Paris" : idee.getLocalisationArdt() ) + "¤" +
				        			idee.getCodeTheme() + "¤" +
				        			( StringUtils.isBlank(idee.getIdQpvQva()) ? "" : "(" + idee.getIdQpvQva() + ") " + idee.getLibelleQpvQva() ) + "¤" +
				        			idee.getStatusPublic() + "¤" +
				        			idee.getLuteceUserName() + "¤" +
				        			UserPreferencesService.instance().get( guid, "budgetparticipatif.email", "?") + "¤" + 
				        			UserPreferencesService.instance().get( guid, "portal.nickname", "?") + "¤" + 
				        			UserPreferencesService.instance().get( guid, "budgetparticipatif.address", "?") + "¤" + 
				        			UserPreferencesService.instance().get( guid, "budgetparticipatif.civility", "?") + "¤" + 
				        			UserPreferencesService.instance().get( guid, "budgetparticipatif.birthdate", "?") + "¤" +
				        			"Follower de l'idée" + "¤" +
				        			project.getId() + "¤" +
				        			project.getTitle() + "¤" +
				        			( projectStatus == null ? "null" : projectStatus.getTextValue() ) + "¤" +
				        			getReliableAttributeTextValue( project, "thematique"   ) + "¤" +
				        			getReliableAttributeTextValue( project, "localisation" ) + "¤" +
				        			getReliableAttributeTextValue( project, "pop_district" ) + "¤" +
				        			getReliableAttributeTextValue( project, "step1"        ) + "¤" +
				        			getReliableAttributeTextValue( project, "step2"        ) + "¤" +
				        			getReliableAttributeTextValue( project, "step3"        ) + "¤" +
				        			getReliableAttributeTextValue( project, "step4"        ) + "¤" +
				        			getReliableAttributeTextValue( project, "step5"        ) + "¤"
			                      ).split("¤"));
						}
		            }
		            else
		            {
		            	AppLogService.error("Unable to load idee with id #" + propId + " !");
		            }
				}
            }
            else
            {
            	AppLogService.error("No idees found for document #" + project.getId() + " !");
            }
    	
            // Adding followers of projects rows

            Set<String> guids = ProjectService.getInstance().getUniqueUserGuidsProjectFollowers( project );

	        for (String guid : guids) {
		        rows.add( (	"¤¤¤¤¤¤¤¤¤¤¤" +
	        			UserPreferencesService.instance().get( guid, "budgetparticipatif.email", "?") + "¤" + 
	        			UserPreferencesService.instance().get( guid, "portal.nickname", "?") + "¤" + 
	        			UserPreferencesService.instance().get( guid, "budgetparticipatif.address", "?") + "¤" + 
	        			UserPreferencesService.instance().get( guid, "budgetparticipatif.civility", "?") + "¤" + 
	        			UserPreferencesService.instance().get( guid, "budgetparticipatif.birthdate", "?") + "¤" +
	        			"Follower du projet" + "¤" +
	        			project.getId() + "¤" +
	        			project.getTitle() + "¤" +
	        			( projectStatus == null ? "null" : projectStatus.getTextValue() ) + "¤" +
	        			getReliableAttributeTextValue( project, "thematique"   ) + "¤" +
	        			getReliableAttributeTextValue( project, "localisation" ) + "¤" +
	        			getReliableAttributeTextValue( project, "pop_district" ) + "¤" +
	        			getReliableAttributeTextValue( project, "step1"        ) + "¤" +
	        			getReliableAttributeTextValue( project, "step2"        ) + "¤" +
	        			getReliableAttributeTextValue( project, "step3"        ) + "¤" +
	        			getReliableAttributeTextValue( project, "step4"        ) + "¤" +
	        			getReliableAttributeTextValue( project, "step5"        ) + "¤"
                      ).split("¤"));
			}

        }	
		
		return rows;
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
		
		rows.add("Id Doc Lutece¤Nb depositaires idees¤Nb followers idees¤Nb followers projet gagnant¤Nb total dedoublonne".split("¤"));
		
		// Getting only once subscriptions
		
		SubscriptionFilter filterSubUpdate = new SubscriptionFilter();
		filterSubUpdate.setSubscriptionProvider( IdeationSubscriptionProviderService.getService().getProviderName()     );
		filterSubUpdate.setSubscriptionKey     ( IdeationSubscriptionProviderService.SUBSCRIPTION_UPDATE_ON_REALIZATION );
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
			
			Set<String> propNotifiableDeposits     = new HashSet<String>();
			Set<String> propNotifiableFollowers    = new HashSet<String>();
			Set<String> projectNotifiableFollowers = new HashSet<String>();
			Set<String> uniqueNotifiable           = new HashSet<String>();

			// Getting list of propositions
            Document document = DocumentHome.findByPrimaryKey( Integer.parseInt(docId) );
            List<Integer> propIds = ProjectService.getInstance().getProjectSubPropositionIds( document, IdeeHome.GetSubIdeesMethod.ALL_FAMILY );
            
			// Adding props depositaries
            propNotifiableDeposits.addAll( getActiveSubscribersGuid ( listSubUpdate, IdeeService.getInstance().getUniqueUserGuidsIdeesDepositaires( propIds ) ) );
            
			// Adding props followers
            propNotifiableFollowers.addAll( getActiveSubscribersGuid ( listSubUpdate, IdeeService.getInstance().getUniqueUserGuidsIdeesFollowers( propIds ) ) );
            
	        
			// Adding project followers
            projectNotifiableFollowers.addAll( getActiveSubscribersGuid( listSubUpdate, ProjectService.getInstance().getUniqueUserGuidsProjectFollowers( document ) ) );	
            
            // Calculating unique notifiable users
            uniqueNotifiable.addAll( propNotifiableDeposits );
            uniqueNotifiable.addAll( propNotifiableFollowers );
            uniqueNotifiable.addAll( projectNotifiableFollowers );

            // Adding final row
	        rows.add( (	docId + "¤" + 
                       	propNotifiableDeposits.size()     + "¤" + 
                       	propNotifiableFollowers.size()    + "¤" + 
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
     * Returns a Set wchich contains only user the notify of whom is activated.
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
    private String getReliableAttributeTextValue ( Document document, String attributeCode ) {

    	if ( document == null ) return "(document is null)";
    	
    	DocumentAttribute documentAttribute = document.getAttribute( attributeCode );
    	
    	if ( documentAttribute == null ) return "(attribute is null)";
    	
    	return StringUtils.isBlank( documentAttribute.getTextValue() ) ? "" : documentAttribute.getTextValue();
    }
    
}



