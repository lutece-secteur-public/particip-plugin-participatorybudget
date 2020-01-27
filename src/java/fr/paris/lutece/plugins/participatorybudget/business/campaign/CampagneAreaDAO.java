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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class provides Data Access methods for CampagneArea objects
 */

public final class CampagneAreaDAO implements ICampagneAreaDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_campagne_area ) FROM participatorybudget_campaign_area";
    private static final String SQL_QUERY_SELECT = "SELECT id_campagne_area, code_campagne, title, active, type, number_votes FROM participatorybudget_campaign_area WHERE id_campagne_area = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatorybudget_campaign_area ( id_campagne_area, code_campagne, title, active, type, number_votes ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatorybudget_campaign_area WHERE id_campagne_area = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatorybudget_campaign_area SET id_campagne_area = ?, code_campagne = ?, title = ?, active = ?, type = ?, number_votes = ? WHERE id_campagne_area = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_campagne_area, code_campagne, title, active, type, number_votes FROM participatorybudget_campaign_area";
    private static final String SQL_QUERY_SELECTALL_BY_CAMPAGNE = SQL_QUERY_SELECTALL + " WHERE code_campagne = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_campagne_area FROM participatorybudget_campaign_area";

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
    public void insert( CampagneArea campagneArea, Plugin plugin )
    {
        int nCpt = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        campagneArea.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( nCpt++, campagneArea.getId( ) );
        daoUtil.setString( nCpt++, campagneArea.getCodeCampagne( ) );
        daoUtil.setString( nCpt++, campagneArea.getTitle( ) );
        daoUtil.setBoolean( nCpt++, campagneArea.getActive( ) );
        daoUtil.setString( nCpt++, campagneArea.getType( ) );
        daoUtil.setInt( nCpt++, campagneArea.getNumberVotes( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CampagneArea load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );

        CampagneArea campagneArea = null;

        if ( daoUtil.next( ) )
        {
            campagneArea = getRow( daoUtil );
        }

        daoUtil.free( );
        return campagneArea;
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
    public void store( CampagneArea campagneArea, Plugin plugin )
    {
        int nCpt = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( nCpt++, campagneArea.getId( ) );
        daoUtil.setString( nCpt++, campagneArea.getCodeCampagne( ) );
        daoUtil.setString( nCpt++, campagneArea.getTitle( ) );
        daoUtil.setBoolean( nCpt++, campagneArea.getActive( ) );
        daoUtil.setString( nCpt++, campagneArea.getType( ) );
        daoUtil.setInt( nCpt++, campagneArea.getNumberVotes( ) );

        daoUtil.setInt( nCpt++, campagneArea.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampagneArea> selectCampagneAreasList( Plugin plugin )
    {
        Collection<CampagneArea> campagneAreaList = new ArrayList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampagneArea campagneArea = getRow( daoUtil );

            campagneAreaList.add( campagneArea );
        }

        daoUtil.free( );
        return campagneAreaList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdCampagneAreasList( Plugin plugin )
    {
        Collection<Integer> campagneAreaList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            campagneAreaList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return campagneAreaList;
    }

    private CampagneArea getRow( DAOUtil daoUtil )
    {
        int nCpt = 1;
        CampagneArea campagneArea = new CampagneArea( );

        campagneArea.setId( daoUtil.getInt( nCpt++ ) );
        campagneArea.setCodeCampagne( daoUtil.getString( nCpt++ ) );
        campagneArea.setTitle( daoUtil.getString( nCpt++ ) );
        campagneArea.setActive( daoUtil.getBoolean( nCpt++ ) );
        campagneArea.setType( daoUtil.getString( nCpt++ ) );
        campagneArea.setNumberVotes( daoUtil.getInt( nCpt++ ) );

        return campagneArea;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampagneArea> selectCampagneAreasListByCampagne( String campagneCode, Plugin plugin )
    {
        Collection<CampagneArea> campagneAreaList = new ArrayList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_CAMPAGNE, plugin );
        daoUtil.setString( 1, campagneCode );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampagneArea campagneArea = getRow( daoUtil );

            campagneAreaList.add( campagneArea );
        }

        daoUtil.free( );
        return campagneAreaList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, List<CampagneArea>> selectCampagneAreasMapByCampagne( Plugin plugin )
    {
        Map<String, List<CampagneArea>> campagneAreaMap = new HashMap<String, List<CampagneArea>>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampagneArea campagneArea = getRow( daoUtil );

            List<CampagneArea> campagneAreaList = campagneAreaMap.get( campagneArea.getCodeCampagne( ) );
            if ( campagneAreaList == null )
            {
                campagneAreaList = new ArrayList<CampagneArea>( );
                campagneAreaMap.put( campagneArea.getCodeCampagne( ), campagneAreaList );
            }
            campagneAreaList.add( campagneArea );
        }

        daoUtil.free( );
        return campagneAreaMap;
    }
}
