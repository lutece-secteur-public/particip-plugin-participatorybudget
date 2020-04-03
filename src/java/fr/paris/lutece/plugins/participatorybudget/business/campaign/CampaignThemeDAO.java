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
 * This class provides Data Access methods for CampaignTheme objects
 */

public final class CampaignThemeDAO implements ICampaignThemeDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_campaign_theme ) FROM participatorybudget_campaign_theme";
    private static final String SQL_QUERY_SELECT = "SELECT id_campaign_theme, code_campaign, code_theme, title, description, active, image_file FROM participatorybudget_campaign_theme WHERE id_campaign_theme = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatorybudget_campaign_theme ( id_campaign_theme, code_campaign, code_theme, title, description, active, image_file ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatorybudget_campaign_theme WHERE id_campaign_theme = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatorybudget_campaign_theme SET id_campaign_theme = ?, code_campaign = ?, code_theme = ?, title = ?, description = ?, active = ?, image_file = ? WHERE id_campaign_theme = ?";
    private static final String SQL_QUERY_CHANGEALL_CAMPAIGN_CODE = "UPDATE participatorybudget_campaign_theme SET code_campaign = ? WHERE code_campaign = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_campaign_theme, code_campaign, code_theme, title, description, active, image_file FROM participatorybudget_campaign_theme";
    private static final String SQL_QUERY_SELECTALL_BY_CAMPAIGN = SQL_QUERY_SELECTALL + " WHERE code_campaign = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_campaign_theme FROM participatorybudget_campaign_theme";
    private static final String SQL_QUERY_SELECT_BY_TITLETHEME = "SELECT id_campaign_theme, code_campaign, code_theme, title, description, active, image_file FROM participatorybudget_campaign_theme WHERE code_theme = ?";

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
    public void insert( CampaignTheme campaignTheme, Plugin plugin )
    {
        int nCpt = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        campaignTheme.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( nCpt++, campaignTheme.getId( ) );
        daoUtil.setString( nCpt++, campaignTheme.getCodeCampaign( ) );
        daoUtil.setString( nCpt++, campaignTheme.getCode( ) );
        daoUtil.setString( nCpt++, campaignTheme.getTitle( ) );
        daoUtil.setString( nCpt++, campaignTheme.getDescription( ) );
        daoUtil.setBoolean( nCpt++, campaignTheme.getActive( ) );
        File image = campaignTheme.getImage( );
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
    public CampaignTheme load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );

        CampaignTheme campaignTheme = null;

        if ( daoUtil.next( ) )
        {
            campaignTheme = getRow( daoUtil );
        }

        daoUtil.free( );
        return campaignTheme;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CampaignTheme loadByCodeTheme( String codeTheme, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_TITLETHEME, plugin );
        daoUtil.setString( 1, codeTheme );
        daoUtil.executeQuery( );

        CampaignTheme campaignTheme = null;

        if ( daoUtil.next( ) )
        {
            campaignTheme = getRow( daoUtil );
        }

        daoUtil.free( );
        return campaignTheme;
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
    public void store( CampaignTheme campaignTheme, Plugin plugin )
    {
        int nCpt = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( nCpt++, campaignTheme.getId( ) );
        daoUtil.setString( nCpt++, campaignTheme.getCodeCampaign( ) );
        daoUtil.setString( nCpt++, campaignTheme.getCode( ) );
        daoUtil.setString( nCpt++, campaignTheme.getTitle( ) );
        daoUtil.setString( nCpt++, campaignTheme.getDescription( ) );
        daoUtil.setBoolean( nCpt++, campaignTheme.getActive( ) );
        File image = campaignTheme.getImage( );
        if ( image != null )
        {
            daoUtil.setInt( nCpt++, image.getIdFile( ) );
        }
        else
        {
            daoUtil.setIntNull( nCpt++ );
        }
        daoUtil.setInt( nCpt++, campaignTheme.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampaignTheme> selectCampaignThemesList( Plugin plugin )
    {
        Collection<CampaignTheme> campaignThemeList = new ArrayList<CampaignTheme>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampaignTheme campaignTheme = getRow( daoUtil );

            campaignThemeList.add( campaignTheme );
        }

        daoUtil.free( );
        return campaignThemeList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdCampaignThemesList( Plugin plugin )
    {
        Collection<Integer> campaignThemeList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            campaignThemeList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return campaignThemeList;
    }

    private CampaignTheme getRow( DAOUtil daoUtil )
    {
        int nCpt = 1;
        CampaignTheme campaignTheme = new CampaignTheme( );

        campaignTheme.setId( daoUtil.getInt( nCpt++ ) );
        campaignTheme.setCodeCampaign( daoUtil.getString( nCpt++ ) );
        campaignTheme.setCode( daoUtil.getString( nCpt++ ) );
        campaignTheme.setTitle( daoUtil.getString( nCpt++ ) );
        campaignTheme.setDescription( daoUtil.getString( nCpt++ ) );
        campaignTheme.setActive( daoUtil.getBoolean( nCpt++ ) );
        campaignTheme.setImage( FileHome.findByPrimaryKey( daoUtil.getInt( nCpt++ ) ) );
        return campaignTheme;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampaignTheme> selectCampaignThemesListByCampaign( String campaignCode, Plugin plugin )
    {
        Collection<CampaignTheme> campaignThemeList = new ArrayList<CampaignTheme>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_CAMPAIGN, plugin );
        daoUtil.setString( 1, campaignCode );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampaignTheme campaignTheme = getRow( daoUtil );

            campaignThemeList.add( campaignTheme );
        }

        daoUtil.free( );
        return campaignThemeList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, List<CampaignTheme>> selectCampaignThemesMapByCampaign( Plugin plugin )
    {
        Map<String, List<CampaignTheme>> campaignThemeMap = new HashMap<String, List<CampaignTheme>>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampaignTheme campaignTheme = getRow( daoUtil );

            List<CampaignTheme> campaignThemeList = campaignThemeMap.get( campaignTheme.getCodeCampaign( ) );
            if ( campaignThemeList == null )
            {
                campaignThemeList = new ArrayList<CampaignTheme>( );
                campaignThemeMap.put( campaignTheme.getCodeCampaign( ), campaignThemeList );
            }
            campaignThemeList.add( campaignTheme );
        }

        daoUtil.free( );
        return campaignThemeMap;
    }
}
