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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for CampaignImage objects
 */

public final class CampaignImageDAO implements ICampaignImageDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_campaign_image ) FROM participatorybudget_campaign_image";
    private static final String SQL_QUERY_SELECT = "SELECT id_campaign_image, code_campaign, id_file FROM participatorybudget_campaign_image WHERE id_campaign_image = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatorybudget_campaign_image ( id_campaign_image, code_campaign, id_file ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatorybudget_campaign_image WHERE id_campaign_image = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatorybudget_campaign_image SET id_campaign_image = ?, code_campaign = ?, id_file = ? WHERE id_campaign_image = ?";
    private static final String SQL_QUERY_CHANGEALL_CAMPAIGN_CODE = "UPDATE participatorybudget_campaign_image SET code_campaign = ? WHERE code_campaign = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_campaign_image, code_campaign, id_file FROM participatorybudget_campaign_image";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_campaign_image FROM participatorybudget_campaign_image";

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
    public void insert( CampaignImage campaignImage, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        campaignImage.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, campaignImage.getId( ) );
        daoUtil.setString( 2, campaignImage.getCodeCampaign( ) );
        daoUtil.setInt( 3, campaignImage.getFile( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CampaignImage load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );

        CampaignImage campaignImage = null;

        if ( daoUtil.next( ) )
        {
            campaignImage = new CampaignImage( );
            campaignImage.setId( daoUtil.getInt( 1 ) );
            campaignImage.setCodeCampaign( daoUtil.getString( 2 ) );
            campaignImage.setFile( daoUtil.getInt( 3 ) );
        }

        daoUtil.free( );
        return campaignImage;
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
    public void store( CampaignImage campaignImage, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, campaignImage.getId( ) );
        daoUtil.setString( 2, campaignImage.getCodeCampaign( ) );
        daoUtil.setInt( 3, campaignImage.getFile( ) );
        daoUtil.setInt( 4, campaignImage.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampaignImage> selectCampaignImagesList( Plugin plugin )
    {
        Collection<CampaignImage> campaignImageList = new ArrayList<CampaignImage>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampaignImage campaignImage = new CampaignImage( );

            campaignImage.setId( daoUtil.getInt( 1 ) );
            campaignImage.setCodeCampaign( daoUtil.getString( 2 ) );
            campaignImage.setFile( daoUtil.getInt( 3 ) );

            campaignImageList.add( campaignImage );
        }

        daoUtil.free( );
        return campaignImageList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdCampaignImagesList( Plugin plugin )
    {
        Collection<Integer> campaignImageList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            campaignImageList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return campaignImageList;
    }
}
