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
package fr.paris.lutece.plugins.participatorybudget.web.includes;

import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneHome;
import fr.paris.lutece.plugins.participatorybudget.service.BudgetIncludeService;
import fr.paris.lutece.plugins.participatorybudget.service.MyVoteService;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampagnesService;
import fr.paris.lutece.plugins.participatorybudget.util.BudgetUtils;
import fr.paris.lutece.plugins.participatorybudget.web.MyInfosXPage;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * Page include to add the
 */
public class HeaderInclude implements PageInclude
{
	//Marks
	public  static final String MARK_HEADER_INCLUDE = "header_include";
	public  static final String MARK_HEADER_CONNEXION = "header_connexion";
	public  static final String MARK_USER = "user";
	public  static final String MARK_RANDOM = "RANDOM";
	private static final String MARK_URL_DO_LOGIN = "url_dologin";
    private static final String MARK_URL_DO_LOGOUT = "url_dologout";
    private static final String MARK_URL_MONCOMPTE = "url_moncompte";
    private static final String MARK_CAMPAGNE_SERVICE = "campagneService";
    
	// TODO : delete following mark 
	public static final String MARK_USER_ARRONDISSEMENT_VOTE = "user_arr_vote";

    // Properties
    private static final String PROPERTY_URL_MONCOMPTE = "budgetparticipatif.include.url.moncompte";
	
	//Services
    private MyVoteService _myVoteService = SpringContextService.getBean( MyVoteService.BEAN_NAME );
    
    //Templates
    private static final String TEMPLATE_CONNEXION = "skin/plugins/mylutece/includes/user_login_title.html";

    private static final Random _random = new Random(  );
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {                        
		if ( request != null )
		{ 
		    rootModel.put( MARK_HEADER_INCLUDE, BudgetIncludeService.getHeaderTemplate(request, _myVoteService));
		    rootModel.put( MARK_URL_DO_LOGIN  , SecurityService.getInstance().getDoLoginUrl() );
		    rootModel.put( MARK_URL_DO_LOGOUT , SecurityService.getInstance().getDoLogoutUrl() );
		 
			LuteceUser user = null;
			try 
			{
				user = SecurityService.getInstance().getRemoteUser(request);
				if(user!=null)
				{
					rootModel.put(             MARK_USER_ARRONDISSEMENT_VOTE, BudgetUtils.getArrondissement( user.getName( ) ) );
					rootModel.put( BudgetUtils.MARK_VOTE_VALIDATED          , _myVoteService.isUserVoteValidated( user.getName( ) ) );
				}
			} 
			catch (UserNotSignedException e) 
			{
				AppLogService.error( e.getMessage( ) ) ;
			}
			
			rootModel.put( MARK_USER, user);
			
			rootModel.put(SecurityTokenService.MARK_TOKEN,SecurityTokenService.getInstance().getToken(request, MyInfosXPage.TOKEN_DO_CREATE_MY_INFOS));
			
			rootModel.put( BudgetUtils.MARK_CAMPAGNE_SERVICE, CampagnesService.getInstance() );
			
			HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONNEXION, request.getLocale(), rootModel );
			
	        rootModel.put( MARK_USER, user);
	        rootModel.put( MARK_HEADER_CONNEXION, template.getHtml(  ) );
	        rootModel.put( MARK_RANDOM, Math.abs( _random.nextLong( ) ) );
	        rootModel.put( MARK_URL_MONCOMPTE, AppPropertiesService.getProperty( PROPERTY_URL_MONCOMPTE ) );
	        rootModel.put( MARK_CAMPAGNE_SERVICE, CampagnesService.getInstance() );
		}
	}
}
