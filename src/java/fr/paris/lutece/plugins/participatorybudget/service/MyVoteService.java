/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentFilter;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpace;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpaceHome;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.service.IRatingService;
import fr.paris.lutece.plugins.extend.modules.rating.service.RatingAddOnService;
import fr.paris.lutece.plugins.extend.modules.rating.service.extender.RatingResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.service.security.IRatingSecurityService;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.participatorybudget.Constants;
import fr.paris.lutece.plugins.participatorybudget.business.MyVote;
import fr.paris.lutece.plugins.participatorybudget.business.Vote;
import fr.paris.lutece.plugins.participatorybudget.business.VoteHome;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampagnesService;
import fr.paris.lutece.plugins.participatorybudget.service.rating.BudgetRatingService;
import fr.paris.lutece.plugins.participatorybudget.util.BudgetUtils;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonUtil;


/**
 * Service to get ratings of users
 */
public class MyVoteService
{
    /**
     * Name of the bean of this service
     */
    public static final String BEAN_NAME = "participatorybudget.myVoteService";
    @Inject
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;
    @Inject
    private IRatingService _ratingService;
    @Inject
    @Named( RatingConstants.BEAN_CONFIG_SERVICE )
    private IResourceExtenderConfigService _configService;
    @Inject
    private IRatingSecurityService _ratingSecurityService;
    
    private static AbstractCacheableService _nbProjetcache = new NbProjetArrCacheService(  );

    /**
     * Get the list of ratings of a user
     * @param user The user to get the list of ratings of
     * @return The list of ratings of the user
     */
    public List<MyVote> getUserVote( LuteceUser user )
    {
        ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );
        filter.setExtenderType( RatingResourceExtender.RESOURCE_EXTENDER );
        filter.setExtendableResourceType( RatingAddOnService.PROPERTY_RESOURCE_TYPE );
        filter.setUserGuid( user.getName(  ) );

        List<ResourceExtenderHistory> listResourceHistory = _resourceExtenderHistoryService.findByFilter( filter );
        List<MyVote> listVotes = new ArrayList<MyVote>( listResourceHistory.size(  ) );

        for ( ResourceExtenderHistory history : listResourceHistory )
        {
            if ( StringUtils.isNumeric( history.getIdExtendableResource(  ) ) )
            {
                int nIdDocument = Integer.parseInt( history.getIdExtendableResource(  ) );
                Document document = DocumentHome.findByPrimaryKeyWithoutBinaries( nIdDocument );
                 Rating rating = _ratingService.findByResource( history.getIdExtendableResource(  ),
                        history.getExtendableResourceType(  ) );
                MyVote myVote = new MyVote( document, history.getDateCreation(  ), rating.getVoteCount(  ) );
                listVotes.add( myVote );
            }
        }
        
        return listVotes;
    }
    
    
    
    /**
     * Get the list of ratings of a user
     * @param user The user to get the list of ratings of
     * @return The list of ratings of the user
     */
    public int getNbUserVote( LuteceUser user )
    {
        
    	int nbCount=0;
    	ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );
        filter.setExtenderType( RatingResourceExtender.RESOURCE_EXTENDER );
        filter.setExtendableResourceType( RatingAddOnService.PROPERTY_RESOURCE_TYPE );
        filter.setUserGuid( user.getName(  ) );
        
        List<ResourceExtenderHistory> listResourceHistory = _resourceExtenderHistoryService.findByFilter(filter);
        
        if(listResourceHistory!=null)
        {
        	nbCount=listResourceHistory.size();
        }
        return nbCount;
    }

    /**
     * 
     * @param idUser
     * @return
     */
    
    public MyVote getUserVote( String idUser )
    {
    	MyVote myVote= new MyVote();
    	int nbrVoteArrdt = VoteHome.getVoteUserNotLocalisation(idUser, 75000);
		int nbrVoteParis = VoteHome.getVoteUserArrondissement(idUser, 75000);
		List<Vote> listvote = VoteHome.getVoteUser(idUser);
		for(Vote vote: listvote){
			if(vote.getArrondissement( ) != 75000){
				
				myVote.setArrondissementUser(getArrondissement(String.valueOf(vote.getArrondissement( ))));				
			
			}
		}
				
		int nbrVoteTotal = nbrVoteArrdt + nbrVoteParis;
			
		myVote.setNbTotVotes(nbrVoteTotal);
		myVote.setNbTotVotesArrondissement(nbrVoteArrdt);
		myVote.setNbTotVotesToutParis(nbrVoteParis);
    	
		return myVote;
    }
    /**
     * GET ARRONDISSEMENT
     * @param arrondissement
     * @return
     */
    private static String getArrondissement(String arrondissement) {
		
    	String strArrondissement = "";
		
		Pattern p = Pattern.compile("75(020|00[1-9]|116|01[0-9])$");
		Matcher m = p.matcher(arrondissement);
		if (m.matches())
			strArrondissement = Integer.valueOf(arrondissement
					.substring(2)) + "e " + "arrondissement";

		return strArrondissement;
	}
    /**
     * 
     * @param nSpaceId
     * @return
     */
   public int getNumberDocumentSpace(int nSpaceId){
    	

	   int nbrDoc = 0;
	   String strSpaceCacheKey=""+nSpaceId;
	   Integer nbDocInSpaceCache= (Integer)_nbProjetcache.getFromCache(strSpaceCacheKey);
	   if(nbDocInSpaceCache != null)
	   {
		   
		   return nbDocInSpaceCache;
	   }
	   else
	   {
		   List<DocumentSpace> docSpace= DocumentSpaceHome.findChilds(nSpaceId);
	    	DocumentFilter filter= new DocumentFilter();
	    	filter.setIsPublished(true);
	    	filter.setCodeDocumentType("project_2015");
	    	for(DocumentSpace ds:docSpace){
	    		
	    		filter.setIdSpace(ds.getId( ));
	    		nbrDoc= nbrDoc + DocumentHome.findByFilter(filter, new Locale("fr","FR")).size();
	    	}
	    	
	    	_nbProjetcache.putInCache(strSpaceCacheKey, nbrDoc);
	   }
	    	
	   	return nbrDoc;
    }
    
   /**
    * Valdate Votes
    * @param userId
    * @param status
    */
    public void validateVotes(String userId, int status){
    	
    	VoteHome.validateVote(userId, status);
    	
    } 
    /**
     * 
     * @param userId
     * @return
     */
    public boolean isUserVoteValidated(String userId){
    	
    	List<Vote> listvote= VoteHome.getVoteUser(userId, Vote.Status.STATUS_VALIDATED.getValeur());
    	if(listvote == null || listvote.isEmpty( )){
    		
    		return false;
    	}
    	
    	return true;
    }
    
    public String cancelVote ( HttpServletRequest request)
    {
		
		String strExtendableResourceType = request
				.getParameter(RatingConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE);
		LuteceUser user = SecurityService.getInstance().getRegisteredUser(
				request);
		
		if ( !CampagnesService.getInstance().isDuring("VOTE") || isUserVoteValidated(user.getName()))
		{
        	try {
				SiteMessageService.setMessage( request, RatingConstants.MESSAGE_CANNOT_VOTE, SiteMessage.TYPE_STOP );
			} catch (SiteMessageException e) {
				return JsonUtil.buildJsonResponse( new ErrorJsonResponse( AppPathService.getSiteMessageUrl( request ) ) );
			}
          }
	
		List<Vote> listVote = VoteHome.getVoteUser(user.getName());

		for (Vote vote : listVote) {
			//cancel all vote for stat export, and after recreate vote TP
			_ratingService.doCancelVote(user,
					String.valueOf(vote.getProjetId()),
					strExtendableResourceType);
			if (vote.getLocalisation() == "whole_city")
			{
				request.setAttribute( Constants.PROJECT_THEMATIQUE, vote.getThematique( ) );
				request.setAttribute( Constants.PROJECT_TITLE, vote.getTitle( ));
				request.setAttribute( Constants.PROJECT_LOCALISATION, Constants.LOCALISATION_PARIS );
				_ratingService.doVote( String.valueOf( vote.getProjetId( ) ), strExtendableResourceType, BudgetRatingService.VOTE_VALUE, request );				
			}
		}

		//return MyInfosXPage.getMyInfosPanelForAjax(request);
		return "";
	}
}
