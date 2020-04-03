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
 * This class provides Data Access methods for CampaignPhase objects
 */

public final class CampaignPhaseTypeDAO implements ICampaignPhaseTypeDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_phase_type ) FROM participatorybudget_campaign_phase_type";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_phase_type, code_phase_type, label, order_num FROM participatorybudget_campaign_phase_type";
    private static final String SQL_QUERY_SELECTALL_ORDERED = SQL_QUERY_SELECTALL + " ORDER BY order_num ASC";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        int nKey = 1;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin ) )
        {
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nKey = daoUtil.getInt( 1 ) + 1;
            }
        }

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampaignPhaseType> selectAll( Plugin plugin )
    {
        Collection<CampaignPhaseType> list = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                CampaignPhaseType campaignPhaseType = new CampaignPhaseType( );

                campaignPhaseType.setId( daoUtil.getInt( 1 ) );
                campaignPhaseType.setCode( daoUtil.getString( 2 ) );
                campaignPhaseType.setLabel( daoUtil.getString( 3 ) );
                campaignPhaseType.setOrder( daoUtil.getInt( 4 ) );

                list.add( campaignPhaseType );
            }
        }

        return list;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<CampaignPhaseType> selectAllOrdered( Plugin plugin )
    {
        List<CampaignPhaseType> list = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ORDERED, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                CampaignPhaseType campaignPhaseType = new CampaignPhaseType( );

                campaignPhaseType.setId( daoUtil.getInt( 1 ) );
                campaignPhaseType.setCode( daoUtil.getString( 2 ) );
                campaignPhaseType.setLabel( daoUtil.getString( 3 ) );
                campaignPhaseType.setOrder( daoUtil.getInt( 4 ) );

                list.add( campaignPhaseType );
            }
        }

        return list;
    }

}
