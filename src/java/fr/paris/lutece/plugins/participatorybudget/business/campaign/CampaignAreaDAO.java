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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for CampaignArea objects
 */

public final class CampaignAreaDAO implements ICampaignAreaDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_campaign_area ) FROM participatorybudget_campaign_area";
    private static final String SQL_QUERY_SELECT = "SELECT id_campaign_area, code_campaign, title, active, type, number_votes FROM participatorybudget_campaign_area WHERE id_campaign_area = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatorybudget_campaign_area ( id_campaign_area, code_campaign, title, active, type, number_votes ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatorybudget_campaign_area WHERE id_campaign_area = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatorybudget_campaign_area SET id_campaign_area = ?, code_campaign = ?, title = ?, active = ?, type = ?, number_votes = ? WHERE id_campaign_area = ?";
    private static final String SQL_QUERY_CHANGEALL_CAMPAIGN_CODE = "UPDATE participatorybudget_campaign_area SET code_campaign = ? WHERE code_campaign = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_campaign_area, code_campaign, title, active, type, number_votes FROM participatorybudget_campaign_area";
    private static final String SQL_QUERY_SELECTALL_BY_CAMPAIGN = SQL_QUERY_SELECTALL + " WHERE code_campaign = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_campaign_area FROM participatorybudget_campaign_area";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );

        int nKey = 1;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free( );

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( CampaignArea campaignArea, Plugin plugin )
    {
        int nCpt = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        campaignArea.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( nCpt++, campaignArea.getId( ) );
        daoUtil.setString( nCpt++, campaignArea.getCodeCampaign( ) );
        daoUtil.setString( nCpt++, campaignArea.getTitle( ) );
        daoUtil.setBoolean( nCpt++, campaignArea.getActive( ) );
        daoUtil.setString( nCpt++, campaignArea.getType( ) );
        daoUtil.setInt( nCpt++, campaignArea.getNumberVotes( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CampaignArea load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );

        CampaignArea campaignArea = null;

        if ( daoUtil.next( ) )
        {
            campaignArea = getRow( daoUtil );
        }

        daoUtil.free( );
        return campaignArea;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void changeCampainCode( String oldCampaignCode, String newCampaignCode, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHANGEALL_CAMPAIGN_CODE, plugin ) )
        {
            daoUtil.setString( 1, newCampaignCode );
            daoUtil.setString( 2, oldCampaignCode );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( CampaignArea campaignArea, Plugin plugin )
    {
        int nCpt = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( nCpt++, campaignArea.getId( ) );
        daoUtil.setString( nCpt++, campaignArea.getCodeCampaign( ) );
        daoUtil.setString( nCpt++, campaignArea.getTitle( ) );
        daoUtil.setBoolean( nCpt++, campaignArea.getActive( ) );
        daoUtil.setString( nCpt++, campaignArea.getType( ) );
        daoUtil.setInt( nCpt++, campaignArea.getNumberVotes( ) );

        daoUtil.setInt( nCpt++, campaignArea.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampaignArea> selectCampaignAreasList( Plugin plugin )
    {
        Collection<CampaignArea> campaignAreaList = new ArrayList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampaignArea campaignArea = getRow( daoUtil );

            campaignAreaList.add( campaignArea );
        }

        daoUtil.free( );
        return campaignAreaList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdCampaignAreasList( Plugin plugin )
    {
        Collection<Integer> campaignAreaList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            campaignAreaList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return campaignAreaList;
    }

    private CampaignArea getRow( DAOUtil daoUtil )
    {
        int nCpt = 1;
        CampaignArea campaignArea = new CampaignArea( );

        campaignArea.setId( daoUtil.getInt( nCpt++ ) );
        campaignArea.setCodeCampaign( daoUtil.getString( nCpt++ ) );
        campaignArea.setTitle( daoUtil.getString( nCpt++ ) );
        campaignArea.setActive( daoUtil.getBoolean( nCpt++ ) );
        campaignArea.setType( daoUtil.getString( nCpt++ ) );
        campaignArea.setNumberVotes( daoUtil.getInt( nCpt++ ) );

        return campaignArea;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampaignArea> selectCampaignAreasListByCampaign( String campaignCode, Plugin plugin )
    {
        Collection<CampaignArea> campaignAreaList = new ArrayList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_CAMPAIGN, plugin );
        daoUtil.setString( 1, campaignCode );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampaignArea campaignArea = getRow( daoUtil );

            campaignAreaList.add( campaignArea );
        }

        daoUtil.free( );
        return campaignAreaList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, List<CampaignArea>> selectCampaignAreasMapByCampaign( Plugin plugin )
    {
        Map<String, List<CampaignArea>> campaignAreaMap = new HashMap<String, List<CampaignArea>>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampaignArea campaignArea = getRow( daoUtil );

            List<CampaignArea> campaignAreaList = campaignAreaMap.get( campaignArea.getCodeCampaign( ) );
            if ( campaignAreaList == null )
            {
                campaignAreaList = new ArrayList<CampaignArea>( );
                campaignAreaMap.put( campaignArea.getCodeCampaign( ), campaignAreaList );
            }
            campaignAreaList.add( campaignArea );
        }

        daoUtil.free( );
        return campaignAreaMap;
    }
}
