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
	
    public BizStartExportThread() {
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
	public void run() {

		try 
		{
			process( );
		} 
		catch (Exception ex) 
		{
			AppLogService.error( "Error processing BizStartExportThread thread : " + ex.getMessage(), ex);
		}

	}

    // *********************************************************************************************
    // * PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS P *
    // * PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS PROCESS P *
    // *********************************************************************************************

	private void process()
    {
		
    	// Get all requested exports
    	List<BizStatFile> files = BizStatFileHome.findByStatusWithoutBinaryContent( BizStatFile.STATUS_REQUESTED );
    	
    	// Treating each of one
    	for (BizStatFile file : files) {
    		
			// Updating file status in database
    		file.setStatus( BizStatFile.STATUS_UNDER_TREATMENT );
    		BizStatFileHome.update( file );

    		// Exporting
    		ByteArrayOutputStream os = new ByteArrayOutputStream();
            
            // Makes Excel opening file in "BOM UTF-8" encoding to preserve french accents
    		os.write(239);
       		os.write(187);
       		os.write(191);
            
    		OutputStreamWriter osw = new OutputStreamWriter( os );

			try {
	       		CSVPrinter csvPrinter = new CSVPrinter( osw,  CSVFormat.DEFAULT.withDelimiter( ';' ));

				Method method = BizStatService.class.getMethod( file.getFileName(), null );
				
				// Invoke the export method of BizStatService
				List<String[]> rows = (List<String[]>) method.invoke( BizStatService.getInstance(), null );
				
				for (String[] rowStrings : rows) {
					csvPrinter.printRecord( rowStrings );
				}
				
				csvPrinter.flush();
	    		csvPrinter.close();
	    		
				// Updating file status in database
				file.setStatus( BizStatFile.STATUS_AVAILABLE );
				file.setValue ( os.toByteArray() );
				BizStatFileHome.update( file );

			} catch (Exception e) {
				
				StringWriter writer = new StringWriter();
				PrintWriter printWriter= new PrintWriter(writer);
				e.printStackTrace(printWriter);

				// Updating file status in database
				file.setStatus( BizStatFile.STATUT_ERROR );
				file.setError ( writer.toString().substring(0, Math.min( writer.toString().length() - 1, 3900) ) ); // Db column : VARCHAR(4000)
				BizStatFileHome.update( file );
				
			} 

		}
    }

}