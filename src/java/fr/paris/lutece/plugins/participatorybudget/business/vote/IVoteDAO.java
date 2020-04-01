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

import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * IVoteDAO Interface
 */
public interface IVoteDAO
{
    /**
     * Insert a new record in the table.
     * 
     * @param vote
     *            instance of the Vote object to inssert
     * @param plugin
     *            the Plugin
     */
    void insert( Vote vote, Plugin plugin );

    /**
     * Delete a record from the table
     * 
     * @param nUserId
     *            The User Id
     * @param nProjetId
     *            The User Id
     * @param plugin
     *            The plugin
     */
    public void delete( String strUserId, int nProjetId, Plugin plugin );

    /**
     * Load the data of all the vote objects and returns them as a List
     * 
     * @param plugin
     *            the Plugin
     * @return The List which contains the data of all the vote objects
     */
    public List<Vote> selectVotesList( Plugin plugin );

    /**
     * 
     * @param nUserId
     * @param plugin
     */
    public void deleteAll( String strUserId, Plugin plugin );

    /**
     * 
     * @param nUserId
     * @param plugin
     */
    public void validateVote( String strUserId, int statusVote, Plugin plugin );

    /**
     * 
     * @param nUserId
     * @param plugin
     */

    public List<Vote> selectVotes( String strUserId, int statusVote, Plugin plugin );

    /**
     * 
     * @param nUserId
     * @param plugin
     * @return
     */
    public List<Vote> selectVotesUser( String strUserId, Plugin plugin );

    /**
     * 
     * @param nUserId
     * @param strLocalisation
     * @param plugin
     * @return
     */
    public int countNbVotesUserArrondissement( String strUserId, int nLocalisation, Plugin plugin );

    /**
     * Return number of votes by campaign
     * 
     * @return A map with the campaign code as key and number of votes as value
     */
    public Map<String, Integer> countNbVotesByCampaignCode( Plugin plugin );

    /**
     * Return number of votes by campaign then by date
     * 
     * @return A map with the campaign code as key and a map as value containing the date as key and the number of votes as value
     */
    public Map<String, Map<String, Integer>> countNbVoteByDateAllCampaigns( Plugin plugin );

    /**
     * 
     * @param nUserId
     * @param strLocalisation
     * @param plugin
     * @return
     */
    public int countNbVotesUser( String strUserId, int nLocalisation, Plugin plugin );

    /**
     * 
     * @param nUserId
     * @param nIdproject
     * @param plugin
     * @return
     */
    public Vote selectVote( String strUserId, int nIdproject, Plugin plugin );

    /**
     * 
     * @param plugin
     * @return
     */
    public List<String> selectUser( Plugin plugin );

}
