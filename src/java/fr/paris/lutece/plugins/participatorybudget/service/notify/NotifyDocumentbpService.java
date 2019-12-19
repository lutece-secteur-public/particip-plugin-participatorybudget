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
package fr.paris.lutece.plugins.participatorybudget.service.notify;

import java.util.List;
import java.util.Locale;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.action.ActionFilter;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceWorkflow;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceWorkflowFilter;
import fr.paris.lutece.plugins.workflowcore.business.workflow.Workflow;
import fr.paris.lutece.plugins.workflowcore.service.action.ActionService;
import fr.paris.lutece.plugins.workflowcore.service.action.IActionService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceWorkflowService;
import fr.paris.lutece.plugins.workflowcore.service.resource.ResourceWorkflowService;
import fr.paris.lutece.plugins.workflowcore.service.workflow.IWorkflowService;
import fr.paris.lutece.plugins.workflowcore.service.workflow.WorkflowService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class NotifyDocumentbpService implements INotifyDocumentbpService
{
    public static final String CONSTANT_RESOURCE_TYPE = "DOCUMENT_BP";
    public static final String BEAN_DOCUMENT_BP_SERVICE = "workflow-notifydocumentbp.documentbpService";

    public static final String PROPERTY_WORKFLOW_ID                   = "workflow-notifydocumentbp.workflow.id";
    public static final String PROPERTY_WORKFLOW_ACTION_INITIER_NOTIF = "workflow-notifydocumentbp.workflow.actionNameInitierNotif";
    public static final String PROPERTY_WORKFLOW_ACTION_NOTIFIER      = "workflow-notifydocumentbp.workflow.actionNameNotifier";

    private static INotifyDocumentbpService _singleton;

    /**
     * 
     * @return
     */
    public static INotifyDocumentbpService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_DOCUMENT_BP_SERVICE );
        }

        return _singleton;
    }

    @Override
    public void processAction( int nIdWorkflow )
    {
    	AppLogService.error("Processing actions for workflow #" + nIdWorkflow + " (INotifyDocumentbpService) :");
    	
        IWorkflowService workflowService = SpringContextService.getBean( WorkflowService.BEAN_SERVICE );
        String strIdWorkflowActionNameNotifier = AppPropertiesService.getProperty( NotifyDocumentbpService.PROPERTY_WORKFLOW_ACTION_NOTIFIER, "" );
        
        IActionService actionService = SpringContextService.getBean( ActionService.BEAN_SERVICE );
        ActionFilter filter = new ActionFilter( );
        filter.setIdWorkflow( nIdWorkflow );

        List<Action> listActions = actionService.getListActionByFilter( filter );

        for ( Action action : listActions )
        {
        	if ( strIdWorkflowActionNameNotifier.equals(action.getName()) )
        	{
            	AppLogService.error("	- Launching action '" + strIdWorkflowActionNameNotifier + "'...");

            	IResourceWorkflowService resourceWorkflowService = SpringContextService.getBean( ResourceWorkflowService.BEAN_SERVICE );
	            ResourceWorkflowFilter resourceWorkflowFilter = new ResourceWorkflowFilter( );
	            resourceWorkflowFilter.setIdWorkflow( nIdWorkflow );
	            resourceWorkflowFilter.setResourceType( CONSTANT_RESOURCE_TYPE );
	
	            List<ResourceWorkflow> listResource = resourceWorkflowService.getListResourceWorkflowByFilter( resourceWorkflowFilter );

            	AppLogService.error("	  ... for " + listResource.size() + " resources.");

	            for ( ResourceWorkflow resource : listResource )
	            {
	                workflowService.doProcessAction( resource.getIdResource( ), resource.getResourceType( ), action.getId( ), -1, null, Locale.getDefault( ), true, null );
	            }
        	}
        }
    }

    @Override
    public void processAction( Workflow workflow, Document document )
    {
    	AppLogService.error("Processing actions for workflow #" + workflow.getId() + " and document #" + document.getId() + " (INotifyDocumentbpService) :");

    	if ( workflow != null && workflow.isEnabled( ) )
        {
            IWorkflowService workflowService = SpringContextService.getBean( WorkflowService.BEAN_SERVICE );

            // workflowService.getState(document.getId(), CONSTANT_RESOURCE_TYPE, workflow.getId(), -1);

            IActionService actionService = SpringContextService.getBean( ActionService.BEAN_SERVICE );
            ActionFilter filter = new ActionFilter( );
            filter.setIdWorkflow( workflow.getId( ) );

            List<Action> listActions = actionService.getListActionByFilter( filter );

            for ( Action action : listActions )
            {
            	if ( PROPERTY_WORKFLOW_ACTION_NOTIFIER.equals(action.getName()) )
            	{
                	AppLogService.error("	- Launching action '" + PROPERTY_WORKFLOW_ACTION_NOTIFIER + "'...");

                	IResourceWorkflowService resourceWorkflowService = SpringContextService.getBean( ResourceWorkflowService.BEAN_SERVICE );
	                ResourceWorkflowFilter resourceWorkflowFilter = new ResourceWorkflowFilter( );
	                resourceWorkflowFilter.setIdWorkflow( workflow.getId( ) );
	
	                List<ResourceWorkflow> listResource = resourceWorkflowService.getListResourceWorkflowByFilter( resourceWorkflowFilter );
	
	            	AppLogService.error("	  ... for " + listResource.size() + " resources.");

	            	for ( ResourceWorkflow resource : listResource )
	                {
	                    workflowService.doProcessAction( resource.getIdResource( ), resource.getResourceType( ), action.getId( ), resource.getExternalParentId( ),
	                            null, Locale.getDefault( ), true, null );
	                }
                }
            }

            // WorkflowService.getInstance( ).getState( document.getId(), CONSTANT_RESOURCE_TYPE, workflow.getId(), -1 );
        }
    }

}
