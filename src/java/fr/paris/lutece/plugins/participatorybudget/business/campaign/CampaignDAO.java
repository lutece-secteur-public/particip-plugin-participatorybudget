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

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Campaign objects
 */

public final class CampaignDAO implements ICampaignDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_campaign ) FROM participatorybudget_campaign";
    private static final String SQL_QUERY_SELECT = "SELECT id_campaign, code_campaign, title, description, active, code_moderation_type, moderation_duration FROM participatorybudget_campaign WHERE id_campaign = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatorybudget_campaign ( id_campaign, code_campaign, title, description, active, code_moderation_type, moderation_duration ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatorybudget_campaign WHERE id_campaign = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatorybudget_campaign SET id_campaign = ?, code_campaign = ?, title = ?, description = ?, active = ?, code_moderation_type = ?, moderation_duration = ? WHERE id_campaign = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_campaign, code_campaign, title, description, active, code_moderation_type, moderation_duration FROM participatorybudget_campaign";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_campaign FROM participatorybudget_campaign";
    private static final String SQL_QUERY_SELECT_LAST_CAMPAIGN = "SELECT id_campaign, code_campaign, title, description, active, code_moderation_type, moderation_duration "
            + "FROM participatorybudget_campaign WHERE code_campaign = ( SELECT MAX( code_campaign ) FROM participatorybudget_campaign );";

    // Queries for image list
    private static final String SQL_QUERY_NEW_PK_IMAGE = "SELECT max( id_campaign_image ) FROM participatorybudget_campaign_image";
    private static final String SQL_QUERY_INSERT_IMAGE = "INSERT INTO participatorybudget_campaign_image (id_campaign_image, code_campaign, id_file ) values ( ?,  ? , ? )";
    private static final String SQL_QUERY_SELECT_IMAGES = "SELECT id_campaign_image, code_campaign, id_file from participatorybudget_campaign_image WHERE code_campaign = ?";
    private static final String SQL_QUERY_DELETE_IMAGES = "DELETE FROM participatorybudget_campaign_image WHERE code_campaign = ?";
    private static final String SQL_QUERY_SELECTALL_IMAGES = "SELECT id_campaign_image, code_campaign, id_file type FROM participatorybudget_campaign_image";

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
     * Generates a new primary key for CampaignImages
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    private int newPrimaryKeyImages( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_IMAGE, plugin );
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
    public void insert( Campaign campaign, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        campaign.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, campaign.getId( ) );
        daoUtil.setString( 2, campaign.getCode( ) );
        daoUtil.setString( 3, campaign.getTitle( ) );
        daoUtil.setString( 4, campaign.getDescription( ) );
        daoUtil.setBoolean( 5, campaign.getActive( ) );
        daoUtil.setString( 6, campaign.getCodeModerationType( ) );
        daoUtil.setInt( 7, campaign.getModerationDuration( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    public void insertImages( Campaign campaign, Plugin plugin )
    {
        for ( File image : campaign.getImgs( ) )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_IMAGE, plugin );

            daoUtil.setInt( 1, newPrimaryKeyImages( plugin ) );
            daoUtil.setString( 2, campaign.getCode( ) );
            daoUtil.setInt( 3, image.getIdFile( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Campaign load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );

        Campaign campaign = null;

        if ( daoUtil.next( ) )
        {
            campaign = getRow( daoUtil );
        }

        daoUtil.free( );
        loadImagesIds( campaign, plugin );
        return campaign;
    }

    private void loadImagesIds( Campaign campaign, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_IMAGES, plugin );
        daoUtil.setString( 1, campaign.getCode( ) );
        daoUtil.executeQuery( );
        List<File> listImgs = new ArrayList<File>( );
        while ( daoUtil.next( ) )
        {
            listImgs.add( FileHome.findByPrimaryKey( daoUtil.getInt( 3 ) ) );
        }
        daoUtil.free( );
        campaign.setImgs( listImgs );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        deleteImages( nKey, plugin );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    private void deleteImages( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_IMAGES, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Campaign campaign, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, campaign.getId( ) );
        daoUtil.setString( 2, campaign.getCode( ) );
        daoUtil.setString( 3, campaign.getTitle( ) );
        daoUtil.setString( 4, campaign.getDescription( ) );
        daoUtil.setBoolean( 5, campaign.getActive( ) );
        daoUtil.setString( 6, campaign.getCodeModerationType( ) );
        daoUtil.setInt( 7, campaign.getModerationDuration( ) );
        daoUtil.setInt( 8, campaign.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );

        deleteImages( campaign.getId( ), plugin );
        insertImages( campaign, plugin );
    }

    /**
     * Returns the campain the code of which is the SQL 'max' (Ex : if 6 campains 'B0' - 'C' - 'D' - 'G0' - 'GA' - 'G', returns campain 'GA').
     * 
     * {@inheritDoc }
     */
    @Override
    public Campaign selectLastCampaign( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_CAMPAIGN, plugin );
        daoUtil.executeQuery( );

        Campaign campaign = null;

        if ( daoUtil.next( ) )
        {
            campaign = getRow( daoUtil );
        }

        daoUtil.free( );
        loadImagesIds( campaign, plugin );
        return campaign;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Campaign> selectCampaignsList( Plugin plugin )
    {
        HashMap<String, Campaign> campaignMap = new HashMap<String, Campaign>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            Campaign campaign = getRow( daoUtil );
            campaignMap.put( campaign.getCode( ), campaign );
            campaign.setImgs( new ArrayList<File>( ) );
        }

        daoUtil.free( );

        daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_IMAGES, plugin );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            String campaignCode = daoUtil.getString( 2 );
            int fileId = daoUtil.getInt( 3 );
            Campaign campaign = campaignMap.get( campaignCode );
            if ( campaign != null )
            {
                campaign.getImgs( ).add( FileHome.findByPrimaryKey( fileId ) );
            }
            else
            {
                AppLogService.info( "Ideation, ideation_campaign_images orphaned file: " + campaignCode + "," + fileId );
            }
        }
        ArrayList<Campaign> result = new ArrayList<Campaign>( campaignMap.values( ) );
        daoUtil.free( );
        return result;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdCampaignsList( Plugin plugin )
    {
        Collection<Integer> campaignList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            campaignList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return campaignList;
    }

    private Campaign getRow( DAOUtil daoUtil )
    {
        int nCpt = 1;
        Campaign campaign = new Campaign( );
        campaign.setId( daoUtil.getInt( nCpt++ ) );
        campaign.setCode( daoUtil.getString( nCpt++ ) );
        campaign.setTitle( daoUtil.getString( nCpt++ ) );
        campaign.setDescription( daoUtil.getString( nCpt++ ) );
        campaign.setActive( daoUtil.getBoolean( nCpt++ ) );
        campaign.setCodeModerationType( daoUtil.getString( nCpt++ ) );
        campaign.setModerationDuration( daoUtil.getInt( nCpt++ ) );
        return campaign;
    }

}
