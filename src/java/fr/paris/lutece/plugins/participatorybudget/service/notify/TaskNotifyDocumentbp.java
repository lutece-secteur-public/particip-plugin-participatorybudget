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
package fr.paris.lutece.plugins.participatorybudget.service.notify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.participatorybudget.business.notify.TaskNotifyDocumentbpConfig;
import fr.paris.lutece.plugins.participatorybudget.service.ProjectService;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * 
 * TaskNotifyIdeation Task
 *
 */
public class TaskNotifyDocumentbp extends SimpleTask
{
    // BEANS CONFIG
    public static final String CONFIG_SERVICE_BEAN_NAME = "workflow-notifydocumentbp.taskNotifyDocumentbpConfigService";

    // PARAMETERS
    private static final String PARAM_BP_EMAIL = "participatorybudget.email";

    private static final String PROPERTY_LUTECE_PLF_NAME = "lutece.plf.name";

    // MARKS
    private static final String MARK_PSEUDO_ABONNEE = "pseudo_abonnee";
    private static final String MARK_TITRE_PROJET = "titre_projet";
    private static final String MARK_NUMERO_LUTECE_DOCUMENT = "numero_lutece_document";
    private static final String MARK_URL_FICHE = "url_fiche";
    private static final String MARK_PHASE_ACTUELLE_PROJET = "phase_actuelle_projet";
    private static final String MARK_MESSAGE_NOTIFICATION = "message_notification";

    public static final String CONSTANT_RESOURCE_TYPE = "DOCUMENT_BP";

    private static final String DSKEY_MAIL_RECIPIENT = "workflow-notifydocumentbp.site_property.admin_mail.recipient";
    private static final String DSKEY_MAIL_SEND_ACTIVATED = "workflow-notifydocumentbp.site_property.admin_mail.activated";
    private static final String DSKEY_MAIL_SENDER = "workflow-notifydocumentbp.site_property.admin_mail.sender";

    // ERRORS
    private static final String ERROR_GET_EMAIL = "Ideation, failed to get email";

    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    @Named( CONFIG_SERVICE_BEAN_NAME )
    private ITaskConfigService _taskNotifyIdeationConfigService;
    @Inject
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;

    private StringBuffer _strLog = new StringBuffer( );

    @Override
    public String getTitle( Locale locale )
    {
        return "Document Bp Notify";
    }

    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        _strLog = new StringBuffer( "	    - Processing task 'TaskNotifyDocumentbp' for resourceHistory #" + nIdResourceHistory + "\n" );

        TaskNotifyDocumentbpConfig config = _taskNotifyIdeationConfigService.findByPrimaryKey( this.getId( ) );

        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );

        if ( ( resourceHistory != null ) && CONSTANT_RESOURCE_TYPE.equals( resourceHistory.getResourceType( ) ) )
        {
            Document document = DocumentHome.findByPrimaryKey( resourceHistory.getIdResource( ) );
            _strLog.append( "	    - Document number is #" + document.getId( ) + "\n" );

            // Get realization message
            String realizationNotify = "";
            DocumentAttribute realizationNotifyAttr = document.getAttribute( "realization_notify" );
            if ( realizationNotifyAttr == null )
            {
                _strLog.append( "ERROR : Unable to find attribute 'realization_notify' for document #" + document.getId( ) + " !" + "\n" );
            }
            else
            {
                realizationNotify = realizationNotifyAttr.getTextValue( ).trim( );
            }

            // Get list of user guid to notify
            Set<String> userGuids = new HashSet<String>( );

            Set<String> userGuidDocumentFollowers = new HashSet<String>( );

            if ( config.isAbonnes( ) )
            {
                userGuidDocumentFollowers.addAll( getActiveSubscribersGuid( ProjectService.getInstance( ).getUniqueUserGuidsProjectFollowers( document ) ) );
            }

            userGuids.addAll( userGuidDocumentFollowers );

            // Process notify
            sendMailToUserGuidSet( document.getId( ), document.getTitle( ), realizationNotify, userGuids, config, locale );

            _strLog.append( "	      Nb notifiable users (possible duplicates) :" + "\n" );
            _strLog.append( "	        --> Followers    of project       = " + userGuidDocumentFollowers.size( ) + "\n" );
            _strLog.append( "	      Nb notified users (without duplicates) = " + userGuids.size( ) + "\n" );
        }
        else
        {
            _strLog.append( "ERROR : unable to find resourceHistory '" + nIdResourceHistory + "' !" + "\n" );
        }

        AppLogService.info( _strLog.toString( ) );
        sendAdminMail( _strLog.toString( ) );
    }

    // *********************************************************************************************
    // * SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER S *
    // * SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER SUBSCRIBER S *
    // *********************************************************************************************

    /**
     * Returns a Set wchich contains only user the notify of whom is activated.
     */
    private Set<String> getActiveSubscribersGuid( Set<String> userGuids )
    {

        Set<String> activeSubscriberGuids = new HashSet<String>( );

        SubscriptionFilter filterSubUpdate = new SubscriptionFilter( );
        filterSubUpdate.setSubscriptionProvider( "participatoryideation.subscriptionProviderName" );
        filterSubUpdate.setSubscriptionKey( "updateOnRealization" );
        List<Subscription> listSubUpdate = SubscriptionService.getInstance( ).findByFilter( filterSubUpdate );

        for ( Subscription subscription : listSubUpdate )
        {
            if ( userGuids.contains( subscription.getUserId( ) ) )
            {
                activeSubscriberGuids.add( subscription.getUserId( ) );
            }
        }

        return activeSubscriberGuids;
    }

    // ***********************************************************************************
    // * SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND *
    // * SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND SEND *
    // ***********************************************************************************

    private void sendMailToUserGuidSet( int documentId, String documentTitle, String realizationNotify, Set<String> userGuidSet,
            TaskNotifyDocumentbpConfig config, Locale locale )
    {
        for ( String userGuid : userGuidSet )
        {
            sendMailToUserGuid( documentId, documentTitle, realizationNotify, userGuid, config, locale );
        }
    }

    private void sendMailToUserGuid( int documentId, String documentTitle, String realizationNotify, String userGuid, TaskNotifyDocumentbpConfig config,
            Locale locale )
    {

        String strSenderName = config.getSenderName( );
        String strRecipientBcc = config.getRecipientsBcc( );
        String strRecipientCc = config.getRecipientsCc( );
        String strSubject = config.getSubject( );

        String strMessage = StringUtils.EMPTY;

        String strEmail = StringUtils.EMPTY;
        String strSenderEmail = config.getSenderEmail( );

        String strNickNameUser = UserPreferencesService.instance( ).getNickname( userGuid );

        _strLog.append( "	    - Generating mail for user '" + userGuid + "'..." );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PSEUDO_ABONNEE, strNickNameUser );
        model.put( MARK_TITRE_PROJET, documentTitle );
        model.put( MARK_NUMERO_LUTECE_DOCUMENT, documentId );
        model.put( MARK_URL_FICHE, AppPathService.getProdUrl( "https://budgetparticipatif.paris.fr/bp" ) + "jsp/site/Portal.jsp?document_id=" + documentId
                + "&portlet_id=158" );
        model.put( MARK_PHASE_ACTUELLE_PROJET, "" );
        model.put( MARK_MESSAGE_NOTIFICATION, realizationNotify );

        strMessage = AppTemplateService.getTemplateFromStringFtl( config.getMessage( ), locale, model ).getHtml( );

        _strLog.append( " done." + "\n" );

        try
        {
            strEmail = UserPreferencesService.instance( ).get( userGuid, PARAM_BP_EMAIL, StringUtils.EMPTY );
        }
        catch( Exception e )
        {
            throw new RuntimeException( ERROR_GET_EMAIL, e );
        }

        MailService.sendMailHtml( strEmail, strRecipientCc, strRecipientBcc, strSenderName, strSenderEmail, strSubject, strMessage );
    }

    // ***********************************************************************************
    // * RESULT_MAIL RESULT_MAIL RESULT_MAIL RESULT_MAIL RESULT_MAIL RESULT_MAIL RESULT_ *
    // * RESULT_MAIL RESULT_MAIL RESULT_MAIL RESULT_MAIL RESULT_MAIL RESULT_MAIL RESULT_ *
    // ***********************************************************************************

    private void sendAdminMail( String str )
    {
        String strMailActivated = AppPropertiesService.getProperty( DSKEY_MAIL_SEND_ACTIVATED, "0" );

        if ( "1".equals( strMailActivated ) )
        {
            String lutecePlfName = AppPropertiesService.getProperty( PROPERTY_LUTECE_PLF_NAME, "unidentified Lutece platform" );
            String strRecipient = AppPropertiesService.getProperty( DSKEY_MAIL_RECIPIENT, "ddct-mpc-site-bp@paris.fr" );
            String strSenderEmail = AppPropertiesService.getProperty( DSKEY_MAIL_SENDER, "no-reply@paris.fr" );

            String strTitle = "BP (" + lutecePlfName + ") - Mailing about realization (from UpdateSuiviSyntProjetsFromEudonet)";

            MailService.sendMailText( strRecipient, "No Reply", strSenderEmail, strTitle, str );
        }
    }
}
