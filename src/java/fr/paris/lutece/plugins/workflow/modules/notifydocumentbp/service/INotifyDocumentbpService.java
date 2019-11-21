package fr.paris.lutece.plugins.workflow.modules.notifydocumentbp.service;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.workflowcore.business.workflow.Workflow;

public interface INotifyDocumentbpService
{
    /**
     * 
     * @param workflow
     */
    public void processAction( int nIdWorkflow );

    /**
     * 
     * @param workflow
     * @param document
     */
    public void processAction( Workflow workflow, Document document );

}
