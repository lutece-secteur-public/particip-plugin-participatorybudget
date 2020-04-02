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
package fr.paris.lutece.plugins.participatorybudget.business.vote;

import java.util.List;
import java.util.Map;

import fr.paris.lutece.plugins.participatorybudget.util.Constants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides instances management methods (create, find, ...) for Vote objects
 */
public final class VoteHome
{
    // Static variable pointed at the DAO instance
    private static IVoteDAO _dao = (IVoteDAO) SpringContextService.getBean( "participatorybudget.voteDAO" );
    private static Plugin _plugin = PluginService.getPlugin( Constants.PLUGIN_NAME );

    /**
     * Private constructor - this class need not be instantiated
     */
    private VoteHome( )
    {
    }

    /**
     * Create an instance of the vote class
     * 
     * @param vote
     *            The instance of the Vote which contains the informations to store
     * @return The instance of vote which has been created with its primary key.
     */
    public static Vote create( Vote vote )
    {
        _dao.insert( vote, _plugin );

        return vote;
    }

    /**
     * Remove the vote whose identifier is specified in parameter
     * 
     * @param nVoteId
     *            The vote Id
     * @param plugin
     *            the Plugin
     */
    public static void remove( String strUserId, int nProjetId )
    {
        _dao.delete( strUserId, nProjetId, _plugin );
    }

    /**
     * Load the data of all the vote objects and returns them in form of a list
     * 
     * @return the list which contains the data of all the vote objects
     */
    public static List<Vote> getVotesList( )
    {
        return _dao.selectVotesList( _plugin );
    }

    /**
     * 
     * @param nUserId
     * @param nProjetId
     */
    public static void removeAll( String strUserId )
    {
        _dao.deleteAll( strUserId, _plugin );
    }

    /**
     * 
     * @param nUserId
     * @return
     */
    public static List<Vote> getVoteUser( String strUserId )
    {
        return _dao.selectVotesUser( strUserId, _plugin );
    }

    /**
     * 
     * @param nUserId
     * @param nLocalisation
     * @return
     */
    public static int getVoteUserArrondissement( String strUserId, int nLocalisation )
    {
        return _dao.countNbVotesUserArrondissement( strUserId, nLocalisation, _plugin );
    }

    /**
     * 
     * @param nUserId
     * @param nLocalisation
     * @return
     */
    public static int getVoteUserNotLocalisation( String strUserId, int nLocalisation )
    {
        return _dao.countNbVotesUser( strUserId, nLocalisation, _plugin );
    }

    /**
     * Return number of votes by campaign
     * 
     * @return A map with the campaign code as key and number of votes as value
     */
    public static Map<String, Integer> countNbVotesByCampaignCode( )
    {
        return _dao.countNbVotesByCampaignCode( _plugin );
    }

    /**
     * Return number of votes by campaign then by date
     * 
     * @return A map with the campaign code as key and a map as value containing the date as key and the number of votes as value
     */
    public static Map<String, Map<String, Integer>> countNbVotesByDateAllCampaigns( )
    {
        return _dao.countNbVotesByDateAllCampaigns( _plugin );
    }

    /**
     * Return number of votes by theme for a campaign
     * 
     * @param campaignId
     *            The id of the campaign
     * @return A map with the theme as key and and the number of votes as value
     */
    public static Map<String, Integer> countNbVotesByTheme( int campaignId )
    {
        return _dao.countNbVotesByTheme( campaignId, _plugin );
    }

    /**
     * Return number of votes by project for a campaign
     * 
     * @param campaignId
     *            The id of the campaign
     * @return A map with the project id as key and and the number of votes as value
     */
    public static Map<Integer, Integer> countNbVotesByProjectId( int campaignId )
    {
        return _dao.countNbVotesByProjectId( campaignId, _plugin );
    }

    /**
     * Return number of votes by location for a campaign
     * 
     * @param campaignId
     *            The id of the campaign
     * @return A map with the location as key and and the number of votes as value
     */
    public static Map<String, Integer> countNbVotesByLocation( int campaignId )
    {
        return _dao.countNbVotesByLocation( campaignId, _plugin );
    }

    /**
     * 
     * @param strUserId
     * @param nIdProject
     * @return
     */
    public static Vote getVote( String strUserId, int nIdProject )
    {
        return _dao.selectVote( strUserId, nIdProject, _plugin );
    }

    /**
     * 
     * @return
     */
    public static List<String> getAllUserId( )
    {
        return _dao.selectUser( _plugin );
    }

    /**
     * 
     * @param nUserId
     * @param nStatus
     * @return
     */
    public static void validateVote( String userId, int status )
    {
        _dao.validateVote( userId, status, _plugin );
    }

    /**
     * 
     * @param nUserId
     * @return
     */
    public static List<Vote> getVoteUser( String userId, int status )
    {
        return _dao.selectVotes( userId, status, _plugin );
    }
}
