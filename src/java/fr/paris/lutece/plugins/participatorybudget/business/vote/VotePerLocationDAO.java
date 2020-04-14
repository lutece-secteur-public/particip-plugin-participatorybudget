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
package fr.paris.lutece.plugins.participatorybudget.business.vote;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Vote objects
 */
public final class VotePerLocationDAO implements IVotePerLocationDAO
{

    // Constants
    private static final String SQL_QUERY_UPDATE = "UPDATE participatorybudget_votes_per_location SET nb_votes = ?  where id = ? ";
    private static final String SQL_QUERY_SELECTALL = "SELECT id, location_ardt, nb_votes FROM  participatorybudget_votes_per_location";
    private static final String SQL_QUERY_BY_ARR = " WHERE location_ardt = ? ";
    private static final String SQL_QUERY_SELECT_BY_ARR = SQL_QUERY_SELECTALL + SQL_QUERY_BY_ARR;

    @Override
    public void update( VotePerLocation vote, Plugin plugin )
    {

        int nCpt = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( nCpt++, vote.getNbVotes( ) );
        daoUtil.setInt( nCpt, vote.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );

    }

    @Override
    public VotePerLocation select( String strArrd, Plugin plugin )
    {
        VotePerLocation vote = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ARR, plugin );
        daoUtil.setString( 1, strArrd );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            vote = new VotePerLocation( );
            vote.setId( daoUtil.getInt( 1 ) );
            vote.setLocationArd( daoUtil.getString( 2 ) );
            vote.setNbVotes( daoUtil.getInt( 3 ) );
        }

        daoUtil.free( );
        return vote;
    }

    @Override
    public List<VotePerLocation> getListVotes( Plugin plugin )
    {
        List<VotePerLocation> voteList = new ArrayList<VotePerLocation>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            VotePerLocation vote = new VotePerLocation( );

            vote.setId( daoUtil.getInt( 1 ) );
            vote.setLocationArd( daoUtil.getString( 2 ) );
            vote.setNbVotes( daoUtil.getInt( 3 ) );

            voteList.add( vote );
        }

        daoUtil.free( );

        return voteList;

    }

}
