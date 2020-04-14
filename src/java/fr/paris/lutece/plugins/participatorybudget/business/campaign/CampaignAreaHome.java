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
import java.util.Map;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides instances management methods (create, find, ...) for CampaignArea objects
 */

public final class CampaignAreaHome
{
    // Static variable pointed at the DAO instance

    private static ICampaignAreaDAO _dao = SpringContextService.getBean( "participatorybudget.campaignAreaDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatorybudget" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private CampaignAreaHome( )
    {
    }

    /**
     * Create an instance of the campaignArea class
     * 
     * @param campaignArea
     *            The instance of the CampaignArea which contains the informations to store
     * @return The instance of campaignArea which has been created with its primary key.
     */
    public static CampaignArea create( CampaignArea campaignArea )
    {
        _dao.insert( campaignArea, _plugin );

        return campaignArea;
    }

    /**
     * Update of the campaignAre which is specified in parameter
     * 
     * @param campaignArea
     *            The instance of the CampaignArea which contains the data to store
     * @return The instance of the campaignArea which has been updated
     */
    public static CampaignArea update( CampaignArea campaignArea )
    {
        _dao.store( campaignArea, _plugin );

        return campaignArea;
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
     * Remove the campaignArea whose identifier is specified in parameter
     * 
     * @param nKey
     *            The campaignArea Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a campaignArea whose identifier is specified in parameter
     * 
     * @param nKey
     *            The campaignArea primary key
     * @return an instance of CampaignArea
     */
    public static CampaignArea findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the campaignArea objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the campaignArea objects
     */
    public static Collection<CampaignArea> getCampaignAreasList( )
    {
        return _dao.selectCampaignAreasList( _plugin );
    }

    /**
     * Load the id of all the campaignArea objects and returns them in form of a collection
     * 
     * @return the collection which contains the id of all the campaignArea objects
     */
    public static Collection<Integer> getIdCampaignAreasList( )
    {
        return _dao.selectIdCampaignAreasList( _plugin );
    }

    /**
     * Load the data of all the campaignArea objects for a campaign and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the campaignArea objects
     */
    public static Collection<CampaignArea> getCampaignAreasListByCampaign( String codeCampaign )
    {
        return _dao.selectCampaignAreasListByCampaign( codeCampaign, _plugin );
    }

    /**
     * Load the data of all the campaignArea objects mapped from campaign code and returns them in form of a map
     * 
     * @return the collection which contains the data of all the campaignArea objects
     */
    public static Map<String, List<CampaignArea>> getCampaignAreasMapByCampaign( )
    {
        return _dao.selectCampaignAreasMapByCampaign( _plugin );
    }
}
