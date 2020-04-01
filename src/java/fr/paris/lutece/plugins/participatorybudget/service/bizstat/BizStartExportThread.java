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
package fr.paris.lutece.plugins.participatorybudget.service.bizstat;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import fr.paris.lutece.plugins.participatorybudget.business.bizstat.BizStatFile;
import fr.paris.lutece.plugins.participatorybudget.business.bizstat.BizStatFileHome;
import fr.paris.lutece.portal.service.util.AppLogService;

public class BizStartExportThread extends Thread
{

    public BizStartExportThread( )
    {
        super( "BizStart export thread" );
    }

    // *********************************************************************************************
    // * RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN R *
    // * RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN RUN R *
    // *********************************************************************************************

    /**
     * {@inheritDoc }
     */
    @Override
    public void run( )
    {

        try
        {
            process( );
        }
        catch( Exception ex )
        {
            AppLogService.error( "Error processing BizStartExportThread thread : " + ex.getMessage( ), ex );
        }

    }

    // *********************************************************************************************
    // * PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS P *
    // * PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS P *
    // *********************************************************************************************

    private void process( )
    {

        // Get all requested exports
        List<BizStatFile> files = BizStatFileHome.findByStatusWithoutBinaryContent( BizStatFile.STATUS_REQUESTED );

        // Treating each of one
        for ( BizStatFile file : files )
        {

            // Updating file status in database
            file.setStatus( BizStatFile.STATUS_UNDER_TREATMENT );
            BizStatFileHome.update( file );

            // Exporting
            ByteArrayOutputStream os = new ByteArrayOutputStream( );

            // Makes Excel opening file in "BOM UTF-8" encoding to preserve french accents
            os.write( 239 );
            os.write( 187 );
            os.write( 191 );

            OutputStreamWriter osw = new OutputStreamWriter( os );

            try
            {
                CSVPrinter csvPrinter = new CSVPrinter( osw, CSVFormat.DEFAULT.withDelimiter( ';' ) );

                Method method = BizStatService.class.getMethod( file.getFileName( ), null );

                // Invoke the export method of VoteStatService
                List<String [ ]> rows = (List<String [ ]>) method.invoke( BizStatService.getInstance( ), null );

                for ( String [ ] rowStrings : rows )
                {
                    csvPrinter.printRecord( rowStrings );
                }

                csvPrinter.flush( );
                csvPrinter.close( );

                // Updating file status in database
                file.setStatus( BizStatFile.STATUS_AVAILABLE );
                file.setValue( os.toByteArray( ) );
                BizStatFileHome.update( file );

            }
            catch( Exception e )
            {

                StringWriter writer = new StringWriter( );
                PrintWriter printWriter = new PrintWriter( writer );
                e.printStackTrace( printWriter );

                // Updating file status in database
                file.setStatus( BizStatFile.STATUT_ERROR );
                file.setError( writer.toString( ).substring( 0, Math.min( writer.toString( ).length( ) - 1, 3900 ) ) ); // Db column : VARCHAR(4000)
                BizStatFileHome.update( file );

            }

        }
    }

}
