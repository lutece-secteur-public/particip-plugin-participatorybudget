/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
package fr.paris.lutece.plugins.participatorybudget.deamon;

import java.lang.Thread.State;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import fr.paris.lutece.plugins.participatorybudget.business.bizstat.BizStatFile;
import fr.paris.lutece.plugins.participatorybudget.business.bizstat.BizStatFileHome;
import fr.paris.lutece.plugins.participatorybudget.service.bizstat.BizStartExportThread;
import fr.paris.lutece.portal.service.daemon.Daemon;


/**
 * Daemon to send notification to users
 */
public class BizStatExportDaemon extends Daemon
{

	private BizStartExportThread _threadExport = null;
	
    @Override
    public void run( )
    {
    	purge();
    	export();

    }

    // *********************************************************************************************
    // * PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE *
    // * PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE PURGE *
    // *********************************************************************************************

    private void purge() {
    	
    	// Get all exports
    	List<BizStatFile> files = BizStatFileHome.findAllWithoutBinaryContent();

    	// Calculating beginning of the current day
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( System.currentTimeMillis() );
		calendar.set( Calendar.HOUR_OF_DAY , 0 );
		calendar.set( Calendar.MINUTE      , 0 );
		calendar.set( Calendar.SECOND      , 0 );
		calendar.set( Calendar.MILLISECOND , 0 );
		Timestamp today = new Timestamp( calendar.getTimeInMillis() );

    	// Purging old ones
    	for (BizStatFile file : files) {
			if ( file.getCreationDate().before( today ) )
			{
				file.setValue  ( null );
				file.setStatus ( BizStatFile.STATUS_PURGED );
				BizStatFileHome.update( file );
			}
		}
    	
    }
    
    // *********************************************************************************************
    // * EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPOR *
    // * EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPORT EXPOR *
    // *********************************************************************************************
    
    private void export() {
	
    	if ( _threadExport == null )
		{
        	// Creates a thread if does not exist
			_threadExport = new BizStartExportThread();
		}
    	else if ( _threadExport.getState() == State.TERMINATED )
     	{
	    	// Creates a new thread
			_threadExport = new BizStartExportThread();

			// Files stilling in a 'being processed' status are statued as 'error'
        	List<BizStatFile> files = BizStatFileHome.findByStatusWithoutBinaryContent( BizStatFile.STATUS_UNDER_TREATMENT );
	    	for (BizStatFile file : files) {
				file.setStatus ( BizStatFile.STATUT_ERROR );
				file.setError  ( "Still 'being processed' while the export daemon was terminated." );
				BizStatFileHome.update( file );
			}
     	}
    	
    	if ( _threadExport.getState() == State.NEW ) 
    	{
        	// Launches the thread if it does not run
			try 
			{
				_threadExport.start();
			} 
			catch (Exception e) 
			{
				_threadExport.interrupt();
				_threadExport = null;
			}
		}
    }
    
}
