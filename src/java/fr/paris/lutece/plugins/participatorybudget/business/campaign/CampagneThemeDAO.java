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
 * This class provides Data Access methods for CampagneTheme objects
 */

public final class CampagneThemeDAO implements ICampagneThemeDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_campagne_theme ) FROM participatorybudget_campaign_theme";
    private static final String SQL_QUERY_SELECT = "SELECT id_campagne_theme, code_campagne, code_theme, title, description, active, image_file FROM participatorybudget_campaign_theme WHERE id_campagne_theme = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatorybudget_campaign_theme ( id_campagne_theme, code_campagne, code_theme, title, description, active, image_file ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatorybudget_campaign_theme WHERE id_campagne_theme = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatorybudget_campaign_theme SET id_campagne_theme = ?, code_campagne = ?, code_theme = ?, title = ?, description = ?, active = ?, image_file = ? WHERE id_campagne_theme = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_campagne_theme, code_campagne, code_theme, title, description, active, image_file FROM participatorybudget_campaign_theme";
    private static final String SQL_QUERY_SELECTALL_BY_CAMPAGNE = SQL_QUERY_SELECTALL + " WHERE code_campagne = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_campagne_theme FROM participatorybudget_campaign_theme";
    private static final String SQL_QUERY_SELECT_BY_TITLETHEME = "SELECT id_campagne_theme, code_campagne, code_theme, title, description, active, image_file FROM participatorybudget_campaign_theme WHERE code_theme = ?";

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
    public void insert( CampagneTheme campagneTheme, Plugin plugin )
    {
        int nCpt = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        campagneTheme.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( nCpt++, campagneTheme.getId( ) );
        daoUtil.setString( nCpt++, campagneTheme.getCodeCampagne( ) );
        daoUtil.setString( nCpt++, campagneTheme.getCode( ) );
        daoUtil.setString( nCpt++, campagneTheme.getTitle( ) );
        daoUtil.setString( nCpt++, campagneTheme.getDescription( ) );
        daoUtil.setBoolean( nCpt++, campagneTheme.getActive( ) );
        File image = campagneTheme.getImage( );
        if ( image != null )
        {
            daoUtil.setInt( nCpt++, image.getIdFile( ) );
        }
        else
        {
            daoUtil.setIntNull( nCpt++ );
        }

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CampagneTheme load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );

        CampagneTheme campagneTheme = null;

        if ( daoUtil.next( ) )
        {
            campagneTheme = getRow( daoUtil );
        }

        daoUtil.free( );
        return campagneTheme;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CampagneTheme loadByCodeTheme( String codeTheme, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_TITLETHEME, plugin );
        daoUtil.setString( 1, codeTheme );
        daoUtil.executeQuery( );

        CampagneTheme campagneTheme = null;

        if ( daoUtil.next( ) )
        {
            campagneTheme = getRow( daoUtil );
        }

        daoUtil.free( );
        return campagneTheme;
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
    public void store( CampagneTheme campagneTheme, Plugin plugin )
    {
        int nCpt = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( nCpt++, campagneTheme.getId( ) );
        daoUtil.setString( nCpt++, campagneTheme.getCodeCampagne( ) );
        daoUtil.setString( nCpt++, campagneTheme.getCode( ) );
        daoUtil.setString( nCpt++, campagneTheme.getTitle( ) );
        daoUtil.setString( nCpt++, campagneTheme.getDescription( ) );
        daoUtil.setBoolean( nCpt++, campagneTheme.getActive( ) );
        File image = campagneTheme.getImage( );
        if ( image != null )
        {
            daoUtil.setInt( nCpt++, image.getIdFile( ) );
        }
        else
        {
            daoUtil.setIntNull( nCpt++ );
        }
        daoUtil.setInt( nCpt++, campagneTheme.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampagneTheme> selectCampagneThemesList( Plugin plugin )
    {
        Collection<CampagneTheme> campagneThemeList = new ArrayList<CampagneTheme>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampagneTheme campagneTheme = getRow( daoUtil );

            campagneThemeList.add( campagneTheme );
        }

        daoUtil.free( );
        return campagneThemeList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdCampagneThemesList( Plugin plugin )
    {
        Collection<Integer> campagneThemeList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            campagneThemeList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return campagneThemeList;
    }

    private CampagneTheme getRow( DAOUtil daoUtil )
    {
        int nCpt = 1;
        CampagneTheme campagneTheme = new CampagneTheme( );

        campagneTheme.setId( daoUtil.getInt( nCpt++ ) );
        campagneTheme.setCodeCampagne( daoUtil.getString( nCpt++ ) );
        campagneTheme.setCode( daoUtil.getString( nCpt++ ) );
        campagneTheme.setTitle( daoUtil.getString( nCpt++ ) );
        campagneTheme.setDescription( daoUtil.getString( nCpt++ ) );
        campagneTheme.setActive( daoUtil.getBoolean( nCpt++ ) );
        campagneTheme.setImage( FileHome.findByPrimaryKey( daoUtil.getInt( nCpt++ ) ) );
        return campagneTheme;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampagneTheme> selectCampagneThemesListByCampagne( String campagneCode, Plugin plugin )
    {
        Collection<CampagneTheme> campagneThemeList = new ArrayList<CampagneTheme>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_CAMPAGNE, plugin );
        daoUtil.setString( 1, campagneCode );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampagneTheme campagneTheme = getRow( daoUtil );

            campagneThemeList.add( campagneTheme );
        }

        daoUtil.free( );
        return campagneThemeList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, List<CampagneTheme>> selectCampagneThemesMapByCampagne( Plugin plugin )
    {
        Map<String, List<CampagneTheme>> campagneThemeMap = new HashMap<String, List<CampagneTheme>>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampagneTheme campagneTheme = getRow( daoUtil );

            List<CampagneTheme> campagneThemeList = campagneThemeMap.get( campagneTheme.getCodeCampagne( ) );
            if ( campagneThemeList == null )
            {
                campagneThemeList = new ArrayList<CampagneTheme>( );
                campagneThemeMap.put( campagneTheme.getCodeCampagne( ), campagneThemeList );
            }
            campagneThemeList.add( campagneTheme );
        }

        daoUtil.free( );
        return campagneThemeMap;
    }
}
