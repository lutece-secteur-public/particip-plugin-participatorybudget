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
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for CampagnePhase objects
 */

public final class CampagnePhaseDAO implements ICampagnePhaseDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_campagne_phase ) FROM participatorybudget_campaign_phase";
    private static final String SQL_QUERY_SELECT = "SELECT id_campagne_phase, code_phase_type, code_campagne, start, end FROM participatorybudget_campaign_phase WHERE id_campagne_phase = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatorybudget_campaign_phase ( id_campagne_phase, code_phase_type, code_campagne, start, end ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatorybudget_campaign_phase WHERE id_campagne_phase = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatorybudget_campaign_phase SET id_campagne_phase = ?, code_phase_type = ?, code_campagne = ?, start = ?, end = ? WHERE id_campagne_phase = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_campagne_phase, code_phase_type, code_campagne, start, end FROM participatorybudget_campaign_phase";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_campagne_phase FROM participatorybudget_campaign_phase";
    private static final String SQL_QUERY_SELECTALL_BY_CAMPAGNE = SQL_QUERY_SELECTALL + " WHERE code_campagne = ?";
    private static final String SQL_QUERY_SELECTALL_ORDERED = "SELECT participatorybudget_campaign_phase.id_campagne_phase, participatorybudget_campaign_phase.code_phase_type, participatorybudget_campaign_phase.code_campagne, participatorybudget_campaign_phase.start, participatorybudget_campaign_phase.END FROM participatorybudget_campaign_phase JOIN participatorybudget_campaign_phase_type ON participatorybudget_campaign_phase_type.code_phase_type = participatorybudget_campaign_phase.code_phase_type ORDER BY participatorybudget_campaign_phase.code_campagne ASC, participatorybudget_campaign_phase_type.order_num ASC";

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
    public void insert( CampagnePhase campagnePhase, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        campagnePhase.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, campagnePhase.getId( ) );
        daoUtil.setString( 2, campagnePhase.getCodePhaseType( ) );
        daoUtil.setString( 3, campagnePhase.getCodeCampagne( ) );
        daoUtil.setTimestamp( 4, campagnePhase.getStart( ) );
        daoUtil.setTimestamp( 5, campagnePhase.getEnd( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CampagnePhase load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );

        CampagnePhase campagnePhase = null;

        if ( daoUtil.next( ) )
        {
            campagnePhase = new CampagnePhase( );
            campagnePhase.setId( daoUtil.getInt( 1 ) );
            campagnePhase.setCodePhaseType( daoUtil.getString( 2 ) );
            campagnePhase.setCodeCampagne( daoUtil.getString( 3 ) );
            campagnePhase.setStart( daoUtil.getTimestamp( 4 ) );
            campagnePhase.setEnd( daoUtil.getTimestamp( 5 ) );
        }

        daoUtil.free( );
        return campagnePhase;
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
    public void store( CampagnePhase campagnePhase, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, campagnePhase.getId( ) );
        daoUtil.setString( 2, campagnePhase.getCodePhaseType( ) );
        daoUtil.setString( 3, campagnePhase.getCodeCampagne( ) );
        daoUtil.setTimestamp( 4, campagnePhase.getStart( ) );
        daoUtil.setTimestamp( 5, campagnePhase.getEnd( ) );
        daoUtil.setInt( 6, campagnePhase.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampagnePhase> selectCampagnePhasesList( Plugin plugin )
    {
        Collection<CampagnePhase> campagnePhaseList = new ArrayList<CampagnePhase>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampagnePhase campagnePhase = new CampagnePhase( );

            campagnePhase.setId( daoUtil.getInt( 1 ) );
            campagnePhase.setCodePhaseType( daoUtil.getString( 2 ) );
            campagnePhase.setCodeCampagne( daoUtil.getString( 3 ) );
            campagnePhase.setStart( daoUtil.getTimestamp( 4 ) );
            campagnePhase.setEnd( daoUtil.getTimestamp( 5 ) );

            campagnePhaseList.add( campagnePhase );
        }

        daoUtil.free( );
        return campagnePhaseList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<CampagnePhase> selectCampagnePhasesOrderedList( Plugin plugin )
    {
        List<CampagnePhase> campagnePhaseList = new ArrayList<CampagnePhase>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ORDERED, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                CampagnePhase campagnePhase = new CampagnePhase( );

                campagnePhase.setId( daoUtil.getInt( 1 ) );
                campagnePhase.setCodePhaseType( daoUtil.getString( 2 ) );
                campagnePhase.setCodeCampagne( daoUtil.getString( 3 ) );
                campagnePhase.setStart( daoUtil.getTimestamp( 4 ) );
                campagnePhase.setEnd( daoUtil.getTimestamp( 5 ) );

                campagnePhaseList.add( campagnePhase );
            }
        }

        return campagnePhaseList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdCampagnePhasesList( Plugin plugin )
    {
        Collection<Integer> campagnePhaseList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            campagnePhaseList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return campagnePhaseList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampagnePhase> selectCampagnePhasesListByCampagne( String campagneCode, Plugin plugin )
    {
        Collection<CampagnePhase> campagnePhaseList = new ArrayList<CampagnePhase>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_CAMPAGNE, plugin );
        daoUtil.setString( 1, campagneCode );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CampagnePhase campagnePhase = new CampagnePhase( );

            campagnePhase.setId( daoUtil.getInt( 1 ) );
            campagnePhase.setCodePhaseType( daoUtil.getString( 2 ) );
            campagnePhase.setCodeCampagne( daoUtil.getString( 3 ) );
            campagnePhase.setStart( daoUtil.getTimestamp( 4 ) );
            campagnePhase.setEnd( daoUtil.getTimestamp( 5 ) );

            campagnePhaseList.add( campagnePhase );
        }

        daoUtil.free( );
        return campagnePhaseList;
    }
}
