/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.budgetparticipatif.business.bizstat;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Field objects
 */
public final class BizStatFileDAO implements IBizStatFileDAO
{
    // Constants
    private static final String SQL_QUERY_ALL            = "SELECT id_bizstat_file, status, file_name, id_admin_user, admin_user_access_code, admin_user_email, reason, description, error, creation_date            , OCTET_LENGTH(file_value) FROM budgetparticipatif_bizstat_file";
    private static final String SQL_QUERY_FIND_BY_ID     = "SELECT id_bizstat_file, status, file_name, id_admin_user, admin_user_access_code, admin_user_email, reason, description, error, creation_date, file_value, OCTET_LENGTH(file_value) FROM budgetparticipatif_bizstat_file WHERE id_bizstat_file = ?";
    private static final String SQL_QUERY_FIND_BY_STATUS = "SELECT id_bizstat_file, status, file_name, id_admin_user, admin_user_access_code, admin_user_email, reason, description, error, creation_date,             OCTET_LENGTH(file_value) FROM budgetparticipatif_bizstat_file WHERE status          = ?";

    private static final String SQL_INSERT = "INSERT INTO budgetparticipatif_bizstat_file (status, file_name, id_admin_user, admin_user_access_code, admin_user_email, reason, description, error, file_value)" + " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_UPDATE = "UPDATE budgetparticipatif_bizstat_file SET status = ?, file_name = ?, id_admin_user = ?, admin_user_access_code = ?, admin_user_email = ?, reason = ?, description = ?, error = ?, file_value = ?  where id_bizstat_file = ? ";
    private static final String SQL_DELETE = "DELETE FROM budgetparticipatif_bizstat_file WHERE id_bizstat_file = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( BizStatFile bizStatFile, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_INSERT, plugin );
        
        daoUtil.setString ( 1, bizStatFile.getStatus()              );
        daoUtil.setString ( 2, bizStatFile.getFileName()            );
        daoUtil.setInt    ( 3, bizStatFile.getIdAdminUser()         );
        daoUtil.setString ( 4, bizStatFile.getAdminUserAccessCode() );
        daoUtil.setString ( 5, bizStatFile.getAdminUserEmail()      );
        daoUtil.setString ( 6, bizStatFile.getReason()              );
        daoUtil.setString ( 7, bizStatFile.getDescription()         );
        daoUtil.setString ( 8, bizStatFile.getError()               );
        daoUtil.setBytes  ( 9, bizStatFile.getValue()               );
        // Creation date is set by the DB

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( BizStatFile bizStatFile , Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_UPDATE, plugin );
        
        daoUtil.setString (  1, bizStatFile.getStatus()              );
        daoUtil.setString (  2, bizStatFile.getFileName()            );
        daoUtil.setInt    (  3, bizStatFile.getIdAdminUser()         );
        daoUtil.setString (  4, bizStatFile.getAdminUserAccessCode() );
        daoUtil.setString (  5, bizStatFile.getAdminUserEmail()      );
        daoUtil.setString (  6, bizStatFile.getReason()              );
        daoUtil.setString (  7, bizStatFile.getDescription()         );
        daoUtil.setString (  8, bizStatFile.getError()               );
        daoUtil.setBytes  (  9, bizStatFile.getValue()               );
        daoUtil.setInt    ( 10, bizStatFile.getIdBizStatFile()       );
        // Creation date is not modifed

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BizStatFile loadWithBytes( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );

        BizStatFile file = null;

        if ( daoUtil.next( ) )
        { 
        	file = new BizStatFile( );
        	file.setIdBizStatFile       ( daoUtil.getInt       (  1 ) );
        	file.setStatus              ( daoUtil.getString    (  2 ) );
        	file.setFileName            ( daoUtil.getString    (  3 ) );
        	file.setIdAdminUser         ( daoUtil.getInt       (  4 ) );
        	file.setAdminUserAccessCode ( daoUtil.getString    (  5 ) );
        	file.setAdminUserEmail      ( daoUtil.getString    (  6 ) );
        	file.setReason              ( daoUtil.getString    (  7 ) );
        	file.setDescription         ( daoUtil.getString    (  8 ) );
        	file.setError               ( daoUtil.getString    (  9 ) );
        	file.setCreationDate        ( daoUtil.getTimestamp ( 10 ) );
        	file.setValue               ( daoUtil.getBytes     ( 11 ) );
        	file.setContentSize         ( daoUtil.getInt       ( 12 ) );
        }

        daoUtil.free( );

        return file;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_DELETE );
        daoUtil.setInt( 1, nId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    @Override
    public List<BizStatFile> selectAllWithoutBytes( Plugin plugin )
    {
        List<BizStatFile> files = new ArrayList<BizStatFile>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
        	BizStatFile file = new BizStatFile(  );

        	file.setIdBizStatFile       ( daoUtil.getInt       (  1 ) );
        	file.setStatus              ( daoUtil.getString    (  2 ) );
        	file.setFileName            ( daoUtil.getString    (  3 ) );
        	file.setIdAdminUser         ( daoUtil.getInt       (  4 ) );
        	file.setAdminUserAccessCode ( daoUtil.getString    (  5 ) );
        	file.setAdminUserEmail      ( daoUtil.getString    (  6 ) );
        	file.setReason              ( daoUtil.getString    (  7 ) );
        	file.setDescription         ( daoUtil.getString    (  8 ) );
        	file.setError               ( daoUtil.getString    (  9 ) );
        	file.setCreationDate        ( daoUtil.getTimestamp ( 10 ) );
        	file.setContentSize         ( daoUtil.getInt       ( 11 ) );

        	files.add( file );
        }

        daoUtil.free(  );

        return files;
    }

    @Override
    public List<BizStatFile> selectByStatusWithoutBytes( String status, Plugin plugin )
    {
        List<BizStatFile> files = new ArrayList<BizStatFile> ();
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_STATUS, plugin );
        daoUtil.setString( 1, status );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
        	BizStatFile file = new BizStatFile(  );

        	file.setIdBizStatFile       ( daoUtil.getInt       (  1 ) );
        	file.setStatus              ( daoUtil.getString    (  2 ) );
        	file.setFileName            ( daoUtil.getString    (  3 ) );
        	file.setIdAdminUser         ( daoUtil.getInt       (  4 ) );
        	file.setAdminUserAccessCode ( daoUtil.getString    (  5 ) );
        	file.setAdminUserEmail      ( daoUtil.getString    (  6 ) );
        	file.setReason              ( daoUtil.getString    (  7 ) );
        	file.setDescription         ( daoUtil.getString    (  8 ) );
        	file.setError               ( daoUtil.getString    (  9 ) );
        	file.setCreationDate        ( daoUtil.getTimestamp ( 10 ) );
        	file.setContentSize         ( daoUtil.getInt       ( 11 ) );

        	files.add( file );
        }

        daoUtil.free(  );

        return files;
    }

}
