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
package fr.paris.lutece.plugins.budgetparticipatif.deamon;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.budgetparticipatif.business.MyVote;
import fr.paris.lutece.plugins.budgetparticipatif.business.NotifyUser;
import fr.paris.lutece.plugins.budgetparticipatif.business.VoteHome;
import fr.paris.lutece.plugins.budgetparticipatif.service.MyVoteService;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpace;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpaceHome;
import fr.paris.lutece.plugins.openamidentityclient.service.OpenamIdentityException;
import fr.paris.lutece.plugins.openamidentityclient.service.OpenamIdentityService;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * Daemon to send notification to users
 */
public class NotifyUserDaemon extends Daemon
{
    private static final String DATASTORE_KEY_BUDGETPARTICIPATIF_DAEMON_LAST_RUN = "budgetparticipatif.notifyUserDaemon.timeDaemonLastRun";
    private static final String DATASTORE_KEY_BUDGETPARTICIPATIF_DAEMON_FIRST_RUN = "budgetparticipatif.notifyUserDaemon.timeDaemonFirstRun";
    private static final String DATASTORE_KEY_BUDGETPARTICIPATIF_DAEMON_NBR_RUN = "budgetparticipatif.notifyUserDaemon.timeDaemonNbrRun";
    private static final String DATASTORE_KEY_BUDGETPARTICIPATIF_NB_NOTIFICATION_IN_POOL = "budgetparticipatif.notifyUserDaemon.nbNotificationInPool";
    private static final String DATASTORE_KEY_BUDGETPARTICIPATIF_NOTIFICATION_TIME_TO_WAIT_BEFORE_SEND_NOTIFICATION_IN_POOL="budgetparticipatif.notifyUserDaemon.timeToWaitBeforeSendPoolNotification";
    
    private static final String MARK_PROD_URL = "prod_url";
    private static final String MARK_NBR_VOTE_TOTAL = "nbr_vote_total";
    private static final String MARK_NBR_VOTE_PARIS = "nbr_vote_paris";
    private static final String MARK_NBR_VOTE_ARRDT = "nbr_vote_arrdt";
    private static final String MARK_NBR_DOC_SPACE = "nbr_vote_space";
    private static final String MARK_NBR_DAY = "nbr_jour";
    
    private static final String PROPERTY_SUBSCRIPTION_NOTIFICATION_SUBJECT = "budgetparticipatif.notifyUserDaemon.notificationSubject1";
    private static final String PROPERTY_SUBSCRIPTION_NOTIFICATION_SUBJECT_TWO = "budgetparticipatif.notifyUserDaemon.notificationSubject2";
    private static final String PROPERTY_SUBSCRIPTION_NOTIFICATION_SENDER_NAME = "budgetparticipatif.notifyUserDaemon.notification.sender.name";
    
    private static final String TEMPLATE_EMAIL_BUDJETPARTICIPATIF = "skin/plugins/budgetparticipatif/email_notify_user.html";
        
    
    private MyVoteService _myVoteService = SpringContextService
			.getBean(MyVoteService.BEAN_NAME);

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(  )
    {
      
    	
    	 String strTimeLastRun = DatastoreService.getDataValue( DATASTORE_KEY_BUDGETPARTICIPATIF_DAEMON_LAST_RUN, "0" );
    	 String strTimeFirstRun = DatastoreService.getDataValue( DATASTORE_KEY_BUDGETPARTICIPATIF_DAEMON_FIRST_RUN, "0" );
    	 String strNbrRun = DatastoreService.getDataValue(DATASTORE_KEY_BUDGETPARTICIPATIF_DAEMON_NBR_RUN, "0");
    	 
    	 DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	 long cuurrentTime=  System.currentTimeMillis(  ) ;
    	 
         long lTimeLastRun;
         long lTimeFirstRun;
         long lNbrRun;

         try {
        	 
			lTimeLastRun = df.parse(strTimeLastRun).getTime();
			lTimeFirstRun = df.parse(strTimeFirstRun).getTime();
			lNbrRun = Integer.parseInt(strNbrRun);
         
         } catch (ParseException e) {
        	 
        	lTimeLastRun = 0L;
		    lTimeFirstRun = 0l;
		    lNbrRun = -1;
		}
          
      
    	if((lTimeFirstRun <= cuurrentTime && lNbrRun == 0) ){
    		
	    	 String strSubject = AppPropertiesService.getProperty( PROPERTY_SUBSCRIPTION_NOTIFICATION_SUBJECT );
	         String strSenderName = AppPropertiesService.getProperty( PROPERTY_SUBSCRIPTION_NOTIFICATION_SENDER_NAME );
	         String strSenderEmail = MailService.getNoReplyEmail(  );
	         selectMail( strSubject, strSenderName, strSenderEmail, "5 jours");
	         DatastoreService.removeData( DATASTORE_KEY_BUDGETPARTICIPATIF_DAEMON_NBR_RUN );
	         DatastoreService.setDataValue( DATASTORE_KEY_BUDGETPARTICIPATIF_DAEMON_NBR_RUN,"1" );
    
    	}else if( (lTimeLastRun <= cuurrentTime && lNbrRun == 1)){
    		
    		 String strSubject = AppPropertiesService.getProperty( PROPERTY_SUBSCRIPTION_NOTIFICATION_SUBJECT_TWO );
	         String strSenderName = AppPropertiesService.getProperty( PROPERTY_SUBSCRIPTION_NOTIFICATION_SENDER_NAME );
	         String strSenderEmail = MailService.getNoReplyEmail(  );
	         selectMail( strSubject, strSenderName, strSenderEmail, "24 heures");
	         DatastoreService.removeData( DATASTORE_KEY_BUDGETPARTICIPATIF_DAEMON_NBR_RUN );
	         DatastoreService.setDataValue( DATASTORE_KEY_BUDGETPARTICIPATIF_DAEMON_NBR_RUN,"2" );
    	}
    }

    
    
    
    private void selectMail(String strSubject, String strSenderName, String strSenderEmail, String numberDay){
    	
    	
    	String strNbrNotifyUserInthePool = DatastoreService.getDataValue(DATASTORE_KEY_BUDGETPARTICIPATIF_NB_NOTIFICATION_IN_POOL, "10");
    	  
    	int nNbrNotifyInPool=Integer.parseInt(strNbrNotifyUserInthePool);
    	
    	List<NotifyUser> listUserToNotify=new ArrayList<NotifyUser>();
    	NotifyUser notifyUSer;
    	List<String> listUserId= VoteHome.getAllUserId( );
    	Collection<DocumentSpace>  listSpace= DocumentSpaceHome.findAll( );
    	int nbreDocumentSpace=0;
    	
    	for(String idUser: listUserId){
    		
    		MyVote myVote= _myVoteService.getUserVote(idUser);
    		for(DocumentSpace sp: listSpace){
    			
    			if(sp.getName( ).trim( ).equals(myVote.getArrondissementUser())){
    				
    				nbreDocumentSpace= _myVoteService.getNumberDocumentSpace(sp.getId());
    				
    			}
    		}
    		
    		int nbrVoteArrdt = myVote.getTotVotesArrondissement();
    		
    		if((myVote.getTotVotesToutParis() < 10 ) || (nbreDocumentSpace >= 10 && nbrVoteArrdt < 10 )|| (nbreDocumentSpace < 10 && nbrVoteArrdt < nbreDocumentSpace ) ){
    			
    			notifyUSer=new NotifyUser();
    			notifyUSer.setIdUser(idUser);
    			notifyUSer.setMyVote(myVote);
    			notifyUSer.setNbreDocumentSpace(nbreDocumentSpace);
    			listUserToNotify.add(notifyUSer);
    			
    		}
    		
    		if(listUserToNotify.size()==nNbrNotifyInPool)
    		{
    			sendNotificationInThePool(listUserToNotify, strSubject, strSenderName, strSenderEmail, numberDay);
    			
    		}
    	}
    	
    	sendNotificationInThePool(listUserToNotify, strSubject, strSenderName, strSenderEmail, numberDay);
    }
    /**
 

    /**
     * Notify a user
     * @param strUserName The name of the user to notify
     * @param strSubject The subject of the email to send
     * @param strSenderName The name of the sender of the email
     * @param strSenderEmail The email address of the sender of the email
     */
    private void notifyUser( String idUser, String strSubject, String strSenderName, String strSenderEmail, MyVote myVote, int nbreDocumentSpace, String numberDay)
    {
    	
         String strUserEmail= null;
         int numberDocument= (nbreDocumentSpace < 10) ? nbreDocumentSpace : 10 ;
         
		try {
			
			strUserEmail = OpenamIdentityService.getService().getAccount(idUser).getLogin(	);
		
		} catch (OpenamIdentityException e) {
		
			strUserEmail= "";
		
		}
    	
		if(strUserEmail != null && !StringUtils.isEmpty(strUserEmail)){
			
	        Map<String, Object> model = new HashMap<String, Object>(  );
	        model.put( MARK_PROD_URL, AppPathService.getProdUrl(  ) );
	        model.put( MARK_NBR_VOTE_TOTAL, myVote.getNbTotVotes( ) );
	        model.put( MARK_NBR_VOTE_PARIS, myVote.getTotVotesToutParis( ) );
	        model.put( MARK_NBR_VOTE_ARRDT, myVote.getTotVotesArrondissement( ) );
	        model.put( MARK_NBR_DOC_SPACE, numberDocument );
	        model.put( MARK_NBR_DAY, numberDay );
	
	        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EMAIL_BUDJETPARTICIPATIF, Locale.getDefault(  ), model );
	
	        MailService.sendMailHtml( strUserEmail, strSenderName, strSenderEmail, strSubject, template.getHtml(  ) );
		}
    }
    
    private synchronized void  sendNotificationInThePool(List<NotifyUser> listUserToNotify,String strSubject, String strSenderName, String strSenderEmail, String numberDay)
    {
    	String strNbrTimeToWaiteBeforeSendNotification = DatastoreService.getDataValue(DATASTORE_KEY_BUDGETPARTICIPATIF_NOTIFICATION_TIME_TO_WAIT_BEFORE_SEND_NOTIFICATION_IN_POOL, "500");
    	long lNbrTimeToWaiteBeforeSendNotification=Long.parseLong(strNbrTimeToWaiteBeforeSendNotification);
    	
    	for(NotifyUser user:listUserToNotify)
		{
    		try {
    			Thread.sleep(lNbrTimeToWaiteBeforeSendNotification);
    		} catch (InterruptedException e) {
    			
    			AppLogService.error(e);
    		}
			notifyUser(user.getIdUser(), strSubject, strSenderName, strSenderEmail, user.getMyVote(), user.getNbreDocumentSpace(), numberDay);
		}
		
		listUserToNotify.clear();
    	
    }
    
    
   
}
