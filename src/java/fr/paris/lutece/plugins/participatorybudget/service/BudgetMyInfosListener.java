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

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campaign;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignPhase;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignPhaseHome;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.plugins.participatorybudget.service.vote.MyVoteService;
import fr.paris.lutece.plugins.participatorybudget.util.BudgetUtils;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class BudgetMyInfosListener implements IMyInfosListener
{

    private MyVoteService _myVoteService = SpringContextService.getBean( MyVoteService.BEAN_NAME );

    @Override
    public void updateNickName( String strLuteceUserName, String strNickName )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void createNickName( String strLuteceUserName, String strNickName )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int canChangeArrond( LuteceUser user )
    {
        // Vote arrondissement can be changed only during submission or vote phases, and during vote only is the user has not validated his votes

        boolean isValidated = false;
        if ( user != null )
        {
            isValidated = _myVoteService.isUserVoteValidated( user.getName( ) );
        }

        if ( CampaignService.getInstance( ).isDuring( "VOTE" ) && isValidated )
        {
            return -1; // Vote opened but vote already validated
        }

        if ( CampaignService.getInstance( ).isBeforeBeginning( "SUBMIT" ) || CampaignService.getInstance( ).isAfterEnd( "VOTE" ) )
        {
            return 2; // Not in submission or vote phase
        }

        if ( CampaignService.getInstance( ).isDuring( "VOTE" ) )
        {
            return 1; // In vote phase, need confirmatinon of votes suppression
        }

        return 0; // In submission phase, basic confirmation
    }

    public String deleteVotes( HttpServletRequest request )
    {
        return _myVoteService.cancelVote( request );
    }

}
