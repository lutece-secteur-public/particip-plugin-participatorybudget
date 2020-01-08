/*
 * Copyright (c) 2002-2020, Mairie de Paris
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

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides Data Access methods for Campagne objects
 */

public final class CampagneDAO implements ICampagneDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_campagne ) FROM participatorybudget_campaign";
    private static final String SQL_QUERY_SELECT = "SELECT id_campagne, code_campagne, title, description, active, code_moderation_type, moderation_duration FROM participatorybudget_campaign WHERE id_campagne = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatorybudget_campaign ( id_campagne, code_campagne, title, description, active, code_moderation_type, moderation_duration ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatorybudget_campaign WHERE id_campagne = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatorybudget_campaign SET id_campagne = ?, code_campagne = ?, title = ?, description = ?, active = ?, code_moderation_type = ?, moderation_duration = ? WHERE id_campagne = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_campagne, code_campagne, title, description, active, code_moderation_type, moderation_duration FROM participatorybudget_campaign";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_campagne FROM participatorybudget_campaign";
    private static final String SQL_QUERY_SELECT_LAST_CAMPAGNE = "SELECT id_campagne, code_campagne, title, description, active, code_moderation_type, moderation_duration "
            + "FROM participatorybudget_campaign WHERE code_campagne = ( SELECT MAX( code_campagne ) FROM participatorybudget_campaign );";

    // Queries for image list
    private static final String SQL_QUERY_NEW_PK_IMAGE = "SELECT max( id_campagne_image ) FROM participatorybudget_campaign_image";
    private static final String SQL_QUERY_INSERT_IMAGE = "INSERT INTO participatorybudget_campaign_image (id_campagne_image, code_campagne, id_file ) values ( ?,  ? , ? )";
    private static final String SQL_QUERY_SELECT_IMAGES = "SELECT id_campagne_image, code_campagne, id_file from participatorybudget_campaign_image WHERE code_campagne = ?";
    private static final String SQL_QUERY_DELETE_IMAGES = "DELETE FROM participatorybudget_campaign_image WHERE code_campagne = ?";
    private static final String SQL_QUERY_SELECTALL_IMAGES = "SELECT id_campagne_image, code_campagne, id_file type FROM participatorybudget_campaign_image";

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
     * Generates a new primary key for CampagneImages
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
    public void insert( Campagne campagne, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        campagne.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, campagne.getId( ) );
        daoUtil.setString( 2, campagne.getCode( ) );
        daoUtil.setString( 3, campagne.getTitle( ) );
        daoUtil.setString( 4, campagne.getDescription( ) );
        daoUtil.setBoolean( 5, campagne.getActive( ) );
        daoUtil.setString( 6, campagne.getCodeModerationType( ) );
        daoUtil.setInt( 7, campagne.getModerationDuration( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    public void insertImages( Campagne campagne, Plugin plugin )
    {
        for ( File image : campagne.getImgs( ) )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_IMAGE, plugin );

            daoUtil.setInt( 1, newPrimaryKeyImages( plugin ) );
            daoUtil.setString( 2, campagne.getCode( ) );
            daoUtil.setInt( 3, image.getIdFile( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Campagne load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );

        Campagne campagne = null;

        if ( daoUtil.next( ) )
        {
            campagne = getRow( daoUtil );
        }

        daoUtil.free( );
        loadImagesIds( campagne, plugin );
        return campagne;
    }

    private void loadImagesIds( Campagne campagne, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_IMAGES, plugin );
        daoUtil.setString( 1, campagne.getCode( ) );
        daoUtil.executeQuery( );
        List<File> listImgs = new ArrayList<File>( );
        while ( daoUtil.next( ) )
        {
            listImgs.add( FileHome.findByPrimaryKey( daoUtil.getInt( 3 ) ) );
        }
        daoUtil.free( );
        campagne.setImgs( listImgs );
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
    public void store( Campagne campagne, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, campagne.getId( ) );
        daoUtil.setString( 2, campagne.getCode( ) );
        daoUtil.setString( 3, campagne.getTitle( ) );
        daoUtil.setString( 4, campagne.getDescription( ) );
        daoUtil.setBoolean( 5, campagne.getActive( ) );
        daoUtil.setString( 6, campagne.getCodeModerationType( ) );
        daoUtil.setInt( 7, campagne.getModerationDuration( ) );
        daoUtil.setInt( 8, campagne.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );

        deleteImages( campagne.getId( ), plugin );
        insertImages( campagne, plugin );
    }

    /**
     * Returns the campain the code of which is the SQL 'max' (Ex : if 6 campains 'B0' - 'C' - 'D' - 'G0' - 'GA' - 'G', returns campain 'GA').
     * 
     * {@inheritDoc }
     */
    @Override
    public Campagne selectLastCampagne( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_CAMPAGNE, plugin );
        daoUtil.executeQuery( );

        Campagne campagne = null;

        if ( daoUtil.next( ) )
        {
            campagne = getRow( daoUtil );
        }

        daoUtil.free( );
        loadImagesIds( campagne, plugin );
        return campagne;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Campagne> selectCampagnesList( Plugin plugin )
    {
        HashMap<String, Campagne> campagneMap = new HashMap<String, Campagne>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            Campagne campagne = getRow( daoUtil );
            campagneMap.put( campagne.getCode( ), campagne );
            campagne.setImgs( new ArrayList<File>( ) );
        }

        daoUtil.free( );

        daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_IMAGES, plugin );
        daoUtil.executeQuery( );
        while ( daoUtil.next( ) )
        {
            String campagneCode = daoUtil.getString( 2 );
            int fileId = daoUtil.getInt( 3 );
            Campagne campagne = campagneMap.get( campagneCode );
            if ( campagne != null )
            {
                campagne.getImgs( ).add( FileHome.findByPrimaryKey( fileId ) );
            }
            else
            {
                AppLogService.info( "Ideation, ideation_campagne_images orphaned file: " + campagneCode + "," + fileId );
            }
        }
        ArrayList<Campagne> result = new ArrayList<Campagne>( campagneMap.values( ) );
        daoUtil.free( );
        return result;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdCampagnesList( Plugin plugin )
    {
        Collection<Integer> campagneList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            campagneList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return campagneList;
    }

    private Campagne getRow( DAOUtil daoUtil )
    {
        int nCpt = 1;
        Campagne campagne = new Campagne( );
        campagne.setId( daoUtil.getInt( nCpt++ ) );
        campagne.setCode( daoUtil.getString( nCpt++ ) );
        campagne.setTitle( daoUtil.getString( nCpt++ ) );
        campagne.setDescription( daoUtil.getString( nCpt++ ) );
        campagne.setActive( daoUtil.getBoolean( nCpt++ ) );
        campagne.setCodeModerationType( daoUtil.getString( nCpt++ ) );
        campagne.setModerationDuration( daoUtil.getInt( nCpt++ ) );
        return campagne;
    }

}
