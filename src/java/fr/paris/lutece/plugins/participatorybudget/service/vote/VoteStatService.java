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

import java.util.Map;

import fr.paris.lutece.plugins.participatorybudget.business.vote.VoteHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class VoteStatService
{

    // *********************************************************************************************
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // *********************************************************************************************

    private static final String BEAN_VOTE_STAT = "participatorybudget.voteStatService";

    private static VoteStatService _singleton;

    public static VoteStatService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_VOTE_STAT );
        }
        return _singleton;
    }

    // *********************************************************************************************
    // * STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS *
    // * STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS STATS *
    // *********************************************************************************************

    /**
     * Return number of votes by campaign
     * 
     * @return A map with the campaign code as key and number of votes as value
     */
    public Map<String, Integer> getNbVoteByCampaign( )
    {
        return VoteHome.countNbVotesByCampaignCode( );
    }

    /**
     * Return number of votes by campaign then by date
     * 
     * @return A map with the campaign code as key and a map as value containing the date as key and the number of votes as value
     */
    public Map<String, Map<String, Integer>> getNbVoteByDateAllCampaigns( )
    {
        return VoteHome.countNbVoteByDateAllCampaigns( );
    }

}
