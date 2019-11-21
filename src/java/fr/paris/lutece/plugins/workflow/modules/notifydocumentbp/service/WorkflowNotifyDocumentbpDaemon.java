package fr.paris.lutece.plugins.workflow.modules.notifydocumentbp.service;

import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class WorkflowNotifyDocumentbpDaemon extends Daemon
{

    public static final String PROPERTY_WORKFLOW_ID = "workflow-notifydocumentbp.workflow.id";

    @Override
    public void run( )
    {
        String strIdWorkflow = AppPropertiesService.getProperty( PROPERTY_WORKFLOW_ID );
        if ( strIdWorkflow != null && !strIdWorkflow.isEmpty( ) )
        {
            int nIdWorkflow = Integer.parseInt( strIdWorkflow );
            NotifyDocumentbpService.getInstance( ).processAction( nIdWorkflow );
        }
    }

}
