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
package fr.paris.lutece.plugins.participatorybudget.service.vote;

import fr.paris.lutece.plugins.participatorybudget.business.vote.UserAccessVote;
import fr.paris.lutece.plugins.participatorybudget.business.vote.UserAccessVoteHome;
import fr.paris.lutece.plugins.participatorybudget.business.vote.VotePerLocation;
import fr.paris.lutece.plugins.participatorybudget.business.vote.VotePerLocationHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class VoteParArrandissementService implements IVoteParArrandissementService

{
    private static final String BEAN_VOTE_PAR_ARRAND_SERVICE = "participatorybudget.voteParArrandService";
    private static IVoteParArrandissementService _singleton;

    public static IVoteParArrandissementService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_VOTE_PAR_ARRAND_SERVICE );

        }

        return _singleton;
    }

    @Override
    public VotePerLocation selectVotePerLocation( String strArrd )
    {
        return VotePerLocationHome.selectVotePerLocation( strArrd );
    }

    @Override
    public boolean isUserAccessVote( String strIdUser )
    {
        if ( UserAccessVoteHome.selectUserAccessVote( strIdUser ) != null )
        {

            return UserAccessVoteHome.selectUserAccessVote( strIdUser ).isHasAccessVote( );
        }

        return false;

    }

    @Override
    public void updateUserAccessVote( UserAccessVote userAccessVote )
    {
        UserAccessVoteHome.updateUserAccessVote( userAccessVote );
    }

    @Override
    public void insertUserAccessVote( UserAccessVote userAccessVote )
    {
        UserAccessVoteHome.insertUserAccessVote( userAccessVote );
    }

    @Override
    public void setAccessVote( boolean bool, String userId )
    {

        UserAccessVote user = new UserAccessVote( );
        user.setHasAccessVote( bool );
        user.setId( userId );

        insertUserAccessVote( user );
    }

}
