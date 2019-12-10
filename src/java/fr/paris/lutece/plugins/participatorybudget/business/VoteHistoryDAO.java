/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for Vote objects
 */
public final class VoteHistoryDAO implements IVoteHistoryDAO
{
    // Constants
	private static final String SQL_QUERY_NEW_PK = "SELECT max( id ) FROM participatorybudget_votes_history";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatorybudget_votes_history ( id_user, id_projet, date_vote, arrondissement, age,birth_date,ip_address, title, localisation, thematique, status, id, status_export_stats) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatorybudget_votes_history WHERE id_user = ? AND id_projet = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_user, id_projet, date_vote, arrondissement, age,birth_date,ip_address, title, localisation, thematique, status, id, status_export_stats FROM participatorybudget_votes_history";
    private static final String SQL_QUERY_DELETE_ALL= "DELETE FROM participatorybudget_votes_history WHERE id_user = ?";
    private static final String SQL_QUERY_UPDATE_TAG= "UPDATE participatorybudget_votes_history SET status_export_stats= ?, date_vote= ? where id = ?";
    private static final String SQl_QUERY_SELECT = SQL_QUERY_SELECTALL + " where id_user= ?";
    private static final String SQl_QUERY_SELECT_By_PORJECT = "SELECT id_user, id_projet, date_vote, arrondissement, age,birth_date,ip_address, title, localisation, thematique, status, status_export_stats FROM participatorybudget_votes_history where id_user = ?  and id_projet= ?";
    private static final String SQl_QUERY_COUNT_VOTE_ARR= "SELECT COUNT(*) FROM participatorybudget_votes_history where id_user= ? and localisation = ?";
    private static final String SQl_QUERY_COUNT_VOTE= "SELECT COUNT(*) FROM participatorybudget_votes_history where id_user= ? and localisation <> ?";
    private static final String SQl_BY_EXPORTED_STATUS= " WHERE status_export_stats = ?";
    
    
    
    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK , plugin  );
        daoUtil.executeQuery( );

        int nKey = 1;

        if( daoUtil.next( ) )
        {
                nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free();

        return nKey;
    }

    
    /**
     * Insert a new record in the table.
     * @param vote instance of the Vote object to insert
     * @param plugin The plugin
     */
    @Override
    public void insert( Vote vote, Plugin plugin )
    {
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

	    vote.setId( newPrimaryKey( plugin ) );
     
        daoUtil.setString( 1, vote.getUserId(  ) );
        daoUtil.setInt( 2, vote.getProjetId(  ) );
        daoUtil.setTimestamp( 3, vote.getDateVote(  ) );
        daoUtil.setInt( 4, vote.getArrondissement(  ) );
        daoUtil.setInt( 5, vote.getAge(  ) );
        daoUtil.setString(6, vote.getBirthDate());
        daoUtil.setString(7, vote.getIpAddress());
        daoUtil.setString(8, vote.getTitle());
        daoUtil.setString(9, vote.getLocalisation());
        daoUtil.setString(10, vote.getThematique());
        daoUtil.setInt(11, vote.geStatus());
        daoUtil.setInt(12, vote.getId( ));
        daoUtil.setInt(13, vote.getStatusExportStats());
        
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     * @param nUserId The User Id
     * @param nProjetId The User Id
     * @param plugin The plugin
     */
    public void delete( String strUserId, int nProjetId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setString( 1, strUserId );
        daoUtil.setInt( 2, nProjetId );
        
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void updateTagStats( Vote vote, int nTag,  Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_TAG, plugin );
        
        daoUtil.setInt( 1, nTag );
        daoUtil.setTimestamp( 2, vote.getDateVote() );
        daoUtil.setInt( 3, vote.getId() );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

	/**
    * Load the data of all the votes and returns them as a List
    * @param plugin The plugin
    * @return The List which contains the data of all the votes
    */
    @Override
    public List<Vote> selectVotesList( Plugin plugin )
    {
        List<Vote> voteList = new ArrayList<Vote>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Vote vote = new Vote(  );

            vote.setUserId( daoUtil.getString( 1 ) );
            vote.setProjetId( daoUtil.getInt( 2 ) );
            vote.setDateVote( daoUtil.getTimestamp( 3 ) );
            vote.setArrondissement( daoUtil.getInt( 4 ) );
            vote.setAge( daoUtil.getInt( 5 ) );
            vote.setBirthDate( daoUtil.getString( 6 ) );
            vote.setIpAddress(daoUtil.getString( 7 ));
            vote.setTitle(daoUtil.getString( 8 ));
            vote.setLocalisation(daoUtil.getString( 9 ));
            vote.setThematique(daoUtil.getString( 10 ));
            vote.setStatus(daoUtil.getInt( 11 ));
            vote.setId(daoUtil.getInt( 12 ));
            vote.setStatusExportStats(daoUtil.getInt( 13 ));
            
            voteList.add( vote );
        }

        daoUtil.free(  );

        return voteList;
    }
    /**
     * Load the data of all the votes and returns them as a List
     * @param plugin The plugin
     * @return The List which contains the data of all the votes
     */
    @Override
    public List<Vote> selectVotesListByExportStatus( Plugin plugin, int status )
    {
    	List<Vote> voteList = new ArrayList<Vote>(  );
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL + SQl_BY_EXPORTED_STATUS, plugin );
    	daoUtil.setInt( 1, status );
    	daoUtil.executeQuery(  );
    	
    	while ( daoUtil.next(  ) )
    	{
    		Vote vote = new Vote(  );
    		
    		vote.setUserId( daoUtil.getString( 1 ) );
    		vote.setProjetId( daoUtil.getInt( 2 ) );
    		vote.setDateVote( daoUtil.getTimestamp( 3 ) );
    		vote.setArrondissement( daoUtil.getInt( 4 ) );
    		vote.setAge( daoUtil.getInt( 5 ) );
    		vote.setBirthDate( daoUtil.getString( 6 ) );
    		vote.setIpAddress(daoUtil.getString( 7 ));
    		vote.setTitle(daoUtil.getString( 8 ));
    		vote.setLocalisation(daoUtil.getString( 9 ));
    		vote.setThematique(daoUtil.getString( 10 ));
    		vote.setStatus(daoUtil.getInt( 11 ));
    		vote.setId(daoUtil.getInt( 12 ));
    		vote.setStatusExportStats(daoUtil.getInt( 13 ));
    		
    		voteList.add( vote );
    	}
    	
    	daoUtil.free(  );
    	
    	return voteList;
    }
    /**
     * 
     */
	@Override
	public void deleteAll(String strUserId , Plugin plugin) {
		
		 DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL, plugin );
	     daoUtil.setString( 1, strUserId );
	     daoUtil.executeUpdate(  );
	     daoUtil.free(  );
		
	}
	
	/**
	    * Load the data of all the votes and returns them as a List
	    * @param plugin The plugin
	    * @return The List which contains the data of all the votes
	   */
	    @Override
	    public List<Vote> selectVotesUser(String strUserId, Plugin plugin )
	    {
	        List<Vote> voteList = new ArrayList<Vote>(  );
	        DAOUtil daoUtil = new DAOUtil( SQl_QUERY_SELECT, plugin );
	        daoUtil.setString( 1, strUserId );
	        daoUtil.executeQuery(  );

	        while ( daoUtil.next(  ) )
	        {
	            Vote vote = new Vote(  );

	            vote.setUserId( daoUtil.getString( 1 ) );
	            vote.setProjetId( daoUtil.getInt( 2 ) );
	            vote.setDateVote( daoUtil.getTimestamp( 3 ) );
	            vote.setArrondissement( daoUtil.getInt( 4 ) );
	            vote.setAge( daoUtil.getInt( 5 ) );
	            vote.setBirthDate( daoUtil.getString( 6 ) );
	            vote.setIpAddress(daoUtil.getString( 7 ));
	            vote.setTitle(daoUtil.getString( 8 ));
	            vote.setLocalisation(daoUtil.getString( 9 ));
	            vote.setThematique(daoUtil.getString( 10 ));
	            vote.setStatus(daoUtil.getInt( 11 ));
	            vote.setStatusExportStats(daoUtil.getInt( 12 ));
	            
	            voteList.add( vote );
	        }

	        daoUtil.free(  );

	        return voteList;
	    }
	  /**
	   *   
	   * @param nUserId
	   * @param strLocalisation
	   * @param plugin
	   * @return
	   */
	  @Override  
	  public int countNbVotesUserArrondissement(String strUserId, int nLocalisation, Plugin plugin ){
		    int nbrVotes =0 ;
	        DAOUtil daoUtil = new DAOUtil( SQl_QUERY_COUNT_VOTE_ARR, plugin );
	        daoUtil.setString( 1, strUserId );
	        daoUtil.setInt( 2, nLocalisation );
	        daoUtil.executeQuery(  );

	        if ( daoUtil.next(  ) )
	        {
	        	nbrVotes = daoUtil.getInt( 1 );
	        }
	        
	        daoUtil.free(  );

	        return nbrVotes;
	  }
	  /**
	   * 
	   * @param nUserId
	   * @param strLocalisation
	   * @param plugin
	   * @return
	   */
	  @Override
	  public int countNbVotesUser(String strUserId, int nLocalisation, Plugin plugin ){
		    int nbrVotes =0 ;
	        DAOUtil daoUtil = new DAOUtil( SQl_QUERY_COUNT_VOTE, plugin );
	        daoUtil.setString( 1, strUserId );
	        daoUtil.setInt( 2, nLocalisation );
	        daoUtil.executeQuery(  );

	        if ( daoUtil.next(  ) )
	        {
	        	nbrVotes = daoUtil.getInt( 1 );
	        }
	        
	        daoUtil.free(  );

	        return nbrVotes;
	  }
	  
	 
	  @Override
	  public Vote selectVoteUser(String strUserId, int nIdproject,Plugin plugin ){
		  
		   Vote vote= null;
		   DAOUtil daoUtil = new DAOUtil( SQl_QUERY_SELECT_By_PORJECT, plugin );
	       daoUtil.setString( 1, strUserId );
	       daoUtil.setInt( 2, nIdproject );
	       daoUtil.executeQuery(  );

	        if ( daoUtil.next(  ) )
	        {
	  		    vote= new Vote();
	            vote.setUserId( daoUtil.getString( 1 ) );
	            vote.setProjetId( daoUtil.getInt( 2 ) );
	            vote.setDateVote( daoUtil.getTimestamp( 3 ) );
	            vote.setArrondissement( daoUtil.getInt( 4 ) );
	            vote.setAge( daoUtil.getInt( 5 ) );
	            vote.setBirthDate( daoUtil.getString( 6 ) );
	            vote.setIpAddress(daoUtil.getString( 7 ));
	            vote.setTitle(daoUtil.getString( 8 ));
	            vote.setLocalisation(daoUtil.getString( 9 ));
	            vote.setThematique(daoUtil.getString( 10 ));
	            vote.setStatus(daoUtil.getInt( 11 ));
	            vote.setStatusExportStats(daoUtil.getInt( 12 ));
	           
	        }

	        daoUtil.free(  );
	        return vote;
	  }
}
