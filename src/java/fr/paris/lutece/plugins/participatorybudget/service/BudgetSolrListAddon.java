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
package fr.paris.lutece.plugins.participatorybudget.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campaign;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignHome;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.plugins.participatorybudget.service.vote.MyVoteService;
import fr.paris.lutece.plugins.participatorybudget.util.BudgetUtils;
import fr.paris.lutece.plugins.search.solr.service.ISolrSearchAppAddOn;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class BudgetSolrListAddon implements ISolrSearchAppAddOn
{

    private static final String MARK_ARRONDISSEMENT_VOTE_USER = "arrondissementVote";
    private static final String PARAMETER_REMOVE_ARR_FILTER = "remove_arr";
    private static final String MARK_REMOVE_ARR_FILTER = "remove_arr";

    @Override
    public void buildPageAddOn( Map<String, Object> model, HttpServletRequest request )
    {

        MyVoteService _myVoteService = SpringContextService.getBean( MyVoteService.BEAN_NAME );
        String arrondissement = null;

        boolean isValidated = false;
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        if ( user != null )
        {
            arrondissement = BudgetUtils.getArrondissementDisplay( user );
            isValidated = _myVoteService.isUserVoteValidated( user.getName( ) );
        }

        if ( arrondissement != null )
        {
            model.put( MARK_ARRONDISSEMENT_VOTE_USER, arrondissement );
        }
        else
        {
            model.put( MARK_ARRONDISSEMENT_VOTE_USER, "notConnected" );
        }

        model.put( BudgetUtils.MARK_VOTE_VALIDATED, isValidated );
        model.put( MARK_REMOVE_ARR_FILTER, request.getParameter( PARAMETER_REMOVE_ARR_FILTER ) );

        model.put( BudgetUtils.MARK_CAMPAIGN_SERVICE, CampaignService.getInstance( ) );
    }

}
