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
package fr.paris.lutece.plugins.participatorybudget.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class UserAccessVoteDAO implements IUserAccessVoteDAO
{
	// Constants
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatorybudget_user_access_vote ( id_user, has_acces_vote ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatorybudget_user_access_vote SET has_acces_vote = ? WHERE id_user = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatorybudget_user_access_vote WHERE id_user = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_user, has_acces_vote FROM participatorybudget_user_access_vote";
    private static final String SQl_QUERY_SELECT = SQL_QUERY_SELECTALL + " where id_user= ?";
    
	@Override
	public void insert( UserAccessVote userVote, Plugin plugin ) 
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
		{
			daoUtil.setString( 1, userVote.getId( ) );
		    daoUtil.setBoolean( 2, userVote.isHasAccessVote() );
		      	        
		    daoUtil.executeUpdate(  );
		}
		
	}

	@Override
	public void update( UserAccessVote userVote, Plugin plugin  ) 
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
		{
		    daoUtil.setBoolean( 1, userVote.isHasAccessVote() );
		    daoUtil.setString( 2, userVote.getId( ) );
		      	        
		    daoUtil.executeUpdate(  );
		}
	}

	@Override
	public void delete( String strUserId, Plugin plugin  ) 
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
		{
	        daoUtil.setString( 1, strUserId );
	        
	        daoUtil.executeUpdate(  );
		}
	}

	@Override
	public UserAccessVote select( String strUserId, Plugin plugin  ) 
	{
		UserAccessVote userAccessVote= null;
		
		try ( DAOUtil daoUtil = new DAOUtil( SQl_QUERY_SELECT, plugin ) )
		{
			daoUtil.setString( 1, strUserId );
			daoUtil.executeQuery(  );

	        if ( daoUtil.next(  ) )
	        {
	        	userAccessVote = new UserAccessVote();
	        	userAccessVote.setId( daoUtil.getString( 1 ) );
	        	userAccessVote.setHasAccessVote( daoUtil.getBoolean( 2 ) );
	           
	        }
		}

		return userAccessVote;
	}

}
