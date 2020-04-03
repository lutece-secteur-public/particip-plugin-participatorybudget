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
package fr.paris.lutece.plugins.participatorybudget.business.campaign;

import java.util.Collection;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides instances management methods (create, find, ...) for CampaignPhase objects
 */

public final class CampaignPhaseHome
{
    // Static variable pointed at the DAO instance

    private static ICampaignPhaseDAO _dao = SpringContextService.getBean( "participatorybudget.campaignPhaseDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatorybudget" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private CampaignPhaseHome( )
    {
    }

    /**
     * Create an instance of the campaignPhase class
     * 
     * @param campaignPhase
     *            The instance of the CampaignPhase which contains the informations to store
     * @return The instance of campaignPhase which has been created with its primary key.
     */
    public static CampaignPhase create( CampaignPhase campaignPhase )
    {
        _dao.insert( campaignPhase, _plugin );

        return campaignPhase;
    }

    /**
     * Update of the campaignPhase which is specified in parameter
     * 
     * @param campaignPhase
     *            The instance of the CampaignPhase which contains the data to store
     * @return The instance of the campaignPhase which has been updated
     */
    public static CampaignPhase update( CampaignPhase campaignPhase )
    {
        _dao.store( campaignPhase, _plugin );

        return campaignPhase;
    }

    /**
     * Change a campaign code
     * 
     * @param oldCampaignCode
     *            The campaign code to change
     * @param newCampaignCode
     *            The new campaign code
     */
    public static void changeCampainCode( String oldCampaignCode, String newCampaignCode )
    {
        _dao.changeCampainCode( oldCampaignCode, newCampaignCode, _plugin );
    }

    /**
     * Remove the campaignPhase whose identifier is specified in parameter
     * 
     * @param nKey
     *            The campaignPhase Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a campaignPhase whose identifier is specified in parameter
     * 
     * @param nKey
     *            The campaignPhase primary key
     * @return an instance of CampaignPhase
     */
    public static CampaignPhase findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the campaignPhase objects and returns them in form of an ordered list
     * 
     * @return the collection which contains the data of all the campaignPhase objects
     */
    public static List<CampaignPhase> getCampaignPhasesOrderedList( )
    {
        return _dao.selectCampaignPhasesOrderedList( _plugin );
    }

    /**
     * Load the data of all the campaignPhase objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the campaignPhase objects
     */
    public static Collection<CampaignPhase> getCampaignPhasesList( )
    {
        return _dao.selectCampaignPhasesList( _plugin );
    }

    /**
     * Load the id of all the campaignPhase objects and returns them in form of a collection
     * 
     * @return the collection which contains the id of all the campaignPhase objects
     */
    public static Collection<Integer> getIdCampaignPhasesList( )
    {
        return _dao.selectIdCampaignPhasesList( _plugin );
    }

    /**
     * Load the data of all the campaignPhase objects for a campaign and returns them in form of a collection
     * 
     * @param campaignCode
     *            the campaign code
     * @return the collection which contains the data of all the campaignPhase objects
     */
    public static Collection<CampaignPhase> getCampaignPhasesListByCampaign( String campaignCode )
    {
        return _dao.selectCampaignPhasesListByCampaign( campaignCode, _plugin );
    }
}
