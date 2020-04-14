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
 * This class provides instances management methods (create, find, ...) for CampaignTheme objects
 */

public final class CampaignThemeHome
{
    // Static variable pointed at the DAO instance

    private static ICampaignThemeDAO _dao = SpringContextService.getBean( "participatorybudget.campaignThemeDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatorybudget" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private CampaignThemeHome( )
    {
    }

    /**
     * Create an instance of the campaignTheme class
     * 
     * @param campaignTheme
     *            The instance of the CampaignTheme which contains the informations to store
     * @return The instance of campaignTheme which has been created with its primary key.
     */
    public static CampaignTheme create( CampaignTheme campaignTheme )
    {
        _dao.insert( campaignTheme, _plugin );

        return campaignTheme;
    }

    /**
     * Update of the campaignTheme which is specified in parameter
     * 
     * @param campaignTheme
     *            The instance of the CampaignTheme which contains the data to store
     * @return The instance of the campaignTheme which has been updated
     */
    public static CampaignTheme update( CampaignTheme campaignTheme )
    {
        _dao.store( campaignTheme, _plugin );

        return campaignTheme;
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
     * Remove the campaignTheme whose identifier is specified in parameter
     * 
     * @param nKey
     *            The campaignTheme Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a campaignTheme whose identifier is specified in parameter
     * 
     * @param nKey
     *            The campaignTheme primary key
     * @return an instance of CampaignTheme
     */
    public static CampaignTheme findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Returns an instance of a campaignTheme whose identifier is specified in parameter
     * 
     * @param codeTheme
     *            The codeTheme
     * @return an instance of CampaignTheme
     */
    public static CampaignTheme findByCodeTheme( String codeTheme )
    {
        return _dao.loadByCodeTheme( codeTheme, _plugin );
    }

    /**
     * Load the data of all the campaignTheme objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the campaignTheme objects
     */
    public static Collection<CampaignTheme> getCampaignThemesList( )
    {
        return _dao.selectCampaignThemesList( _plugin );
    }

    /**
     * Load the id of all the campaignTheme objects and returns them in form of a collection
     * 
     * @return the collection which contains the id of all the campaignTheme objects
     */
    public static Collection<Integer> getIdCampaignThemesList( )
    {
        return _dao.selectIdCampaignThemesList( _plugin );
    }

    /**
     * Load the data of all the campaignTheme objects for a campaign and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the campaignTheme objects
     */
    public static Collection<CampaignTheme> getCampaignThemesListByCampaign( String codeCampaign )
    {
        return _dao.selectCampaignThemesListByCampaign( codeCampaign, _plugin );
    }

    /**
     * Load the data of all the campaignTheme objects mapped from campaign code and returns them in form of a map
     * 
     * @return the collection which contains the data of all the campaignTheme objects
     */
    public static Map<String, List<CampaignTheme>> getCampaignThemesMapByCampaign( )
    {
        return _dao.selectCampaignThemesMapByCampaign( _plugin );
    }
}
