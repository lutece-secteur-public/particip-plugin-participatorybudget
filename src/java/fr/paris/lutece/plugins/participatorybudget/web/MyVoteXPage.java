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
package fr.paris.lutece.plugins.participatorybudget.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.follow.service.FollowService;
import fr.paris.lutece.plugins.extend.modules.follow.service.IFollowService;
import fr.paris.lutece.plugins.extend.modules.follow.service.extender.FollowResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.service.IRatingService;
import fr.paris.lutece.plugins.extend.modules.rating.service.RatingAddOnService;
import fr.paris.lutece.plugins.extend.modules.rating.service.RatingService;
import fr.paris.lutece.plugins.extend.modules.rating.service.security.IRatingSecurityService;
import fr.paris.lutece.plugins.extend.modules.rating.service.security.RatingSecurityService;
import fr.paris.lutece.plugins.extend.modules.rating.service.validator.RatingValidationManagementService;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.plugins.participatorybudget.business.MyInfosForm;
import fr.paris.lutece.plugins.participatorybudget.business.MyVote;
import fr.paris.lutece.plugins.participatorybudget.business.Vote;
import fr.paris.lutece.plugins.participatorybudget.business.VoteHome;
import fr.paris.lutece.plugins.participatorybudget.business.VotePerLocation;
import fr.paris.lutece.plugins.participatorybudget.service.IVoteParArrandissementService;
import fr.paris.lutece.plugins.participatorybudget.service.MyInfosService;
import fr.paris.lutece.plugins.participatorybudget.service.MyVoteService;
import fr.paris.lutece.plugins.participatorybudget.service.VoteParArrandissementService;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.plugins.participatorybudget.service.rating.BudgetRatingService;
import fr.paris.lutece.plugins.participatorybudget.util.BudgetUtils;
import fr.paris.lutece.plugins.participatorybudget.util.Constants;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.captcha.CaptchaSecurityService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.json.AbstractJsonResponse;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage ParisConnectUser xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = MyVoteXPage.VIEW_MY_VOTES, pageTitleI18nKey = "participatorybudget.xpage.mesVotes.pageTitle", pagePathI18nKey = "participatorybudget.xpage.mesVotes.pagePathLabel" )
public class MyVoteXPage extends MVCApplication
{

    /**
     * Name of this application
     */
    public static final String TOKEN_DO_SEND_AVIS = "doSendAvis";
    public static final String TOKEN_DO_SUBSCRIBE_ALERT = "doSubscribeAlert";
    public static final String TOKEN_DO_SAVE_MY_INFOS = "doSaveMyInfos";
    public static final String TOKEN_DO_CREATE_MY_INFOS = "doCreateMyInfos";
    public static final String PAGE_MY_INFOS = "mesVotes";
    private static final long serialVersionUID = -4316691400124512414L;
    private static final String JCAPTCHA_PLUGIN = "jcaptcha";
    // Templates
    private static final String TEMPLATE_MY_VOTES = "/skin/plugins/participatorybudget/mes_votes.html";
    private static final String TEMPLATE_BUTTON_CANCEL_VOTE = "/skin/plugins/participatorybudget/button_cancel_vote_html.html";
    private static final String TEMPLATE_NAVBAR_PROJECT = "/skin/plugins/participatorybudget/navbar_project.html";
    private static final String TEMPLATE_BUTTON_DO_VOTE = "/skin/plugins/participatorybudget/button_do_vote_html.html";
    private static final String TEMPLATE_BUTTON_VALID_VOTE = "/skin/plugins/participatorybudget/button_valid_vote.html";

    public static final String SUBSCRIPTION_UPDATE_ON_REALIZATION = "updateOnRealization";

    // Views
    public static final String VIEW_MY_VOTES = "myVotes";

    // Actions
    private static final String ACTION_VALIDATE_VOTES = "validateVote";

    // Markers

    private static final String MARK_USER_VOTES = "userVotes";
    private static final String MARK_USER_TYPE_VOTE = "type_vote";
    private static final String MARK_USER_VOTES_PARIS = "WHOLE CITY";
    private static final String MARK_USER_VOTES_OTHER = "Paris ";
    private static final String MARK_USER_ARRONDISSEMENT = "user_arrondissement";
    private static final String MARK_ARRONDISSEMENT = "arrondissement";
    private static final String MARK_NBR_PROJET_ARRDT = "nbrProjetArrond";
    private static final String MARK_NBR_PROJET_PARIS = "nbrProjetParis";
    private static final String MARK_DATE_VALIDATION = "validate_vote_date";
    private static final String MARK_USER_PSEUDO = "user_pseudo";
    private static final String MARK_LIST_VALIDATE_PROJECTS = "vote_projects_list";
    private static final String PROPERTY_MY_VOTES_PAGE_TITLE = "participatorybudget.xpage.myVotes.pageTitle";
    private static final String PROPERTY_MY_VOTES_PAGE_PATH_LABEL = "participatorybudget.xpage.myVotes.pagePathLabel";

    // Messages

    private static final String MESSAGE_INFO_USER_VOTE_VALIDE = "participatorybudget.messageSuccesSaveVote";
    private static final String MESSAGE_INFO_VOTE = "participatorybudget.message.info.vote";
    private static final String MESSAGE_PAGE_NOT_ACCESSIBLE = "participatorybudget.page_not_accessible";

    // Parameters
    private static final String PARAMETER_ARRONDISSEMENT = "arrondissement";
    private static final String PARAM_NOTIFY_VALIDE = "notify_valide";
    private static final String PARAM_VALIDATE = "validate";
    private static final String PARAM_FOLLOW = "vote_follow";
    private static final String PARAMETER_COMPLETE_INFOS = "completeInfos";

    // Json ERROR CODE
    // Json ERROR CODE
    private static final String JSON_ERROR_CODE_USER_NOT_SIGNED = "USER_NOT_SIGNED";
    private static final String JSON_ERROR_CODE_USER_CAN_NOT_VOTE = "USER_CAN_NOT_VOTE";
    private static final String JSON_ERROR_CODE_USER_ALREADY_VOTED = "USER_ALREADY_VOTED";
    private static final String JSON_ERROR_CHECKED_ARRONDISSEMENT = "JSON_ERROR_CHECKED_ARRONDISSEMENT";
    private static final String JSON_ERROR_ALREADY_VOTED_ARRONDISSEMENT = "JSON_ERROR_ALREADY_VOTED_ARRONDISSEMENT";
    private static final String JSON_ERROR_ALREADY_VOTED_TOUT_PARIS = "JSON_ERROR_ALREADY_VOTED_TOUT_PARIS";
    private static final String JSON_ERROR_VOTE_USER_ARROND = "JSON_ERROR_VOTE_USER_ARROND";
    private static final String JSON_ERROR_CODE_USER__VOTED_MAX = "ERROR_CODE_USER_VOTED_MAX";

    private static final String CONSTANT_HTTP = "http";
    private static final String DATE_EMAIL_VALIDATION_PATTERN = "dd-MM-yyyy 'à' HH:mm:ss";
    private static final String VOTE_TITLE_SEPARATOR = "; ";

    // Key
    private static final String KEY_ENABLE_CAPTCHA_USER_INFORMATIONS = "participatorybudget.site_property.enable_captcha_user_informations";
    private static final String KEY_ERROR_CODE_USER_ALREADY_VOTED = "participatorybudget.site_property.error_code_user_already_voted";
    private static final String KEY_ERROR_CODE_USER_CAN_NOT_VOTE = "participatorybudget.site_property.error_code_user_can_not_vote";
    private static final String KEY_ERROR_CODE_USER__VOTED_MAX = "participatorybudget.site_property.error_code_user__voted_max";
    private static final String KEY_ERROR_VOTE_USER_ARROND = "participatorybudget.site_property.error_vote_user_arrond";
    private static final String KEY_ERROR_ALREADY_VOTED_TOUT_PARIS = "participatorybudget.site_property.error_already_voted_tout_paris";
    private static final String KEY_ERROR_ALREADY_VOTED_ARRONDISSEMENT = "participatorybudget.site_property.error_already_voted_arrondissement";
    private static final String KEY_ERROR_CHECKED_ARRONDISSEMENT = "participatorybudget.site_property.error_checked_arrondissement";
    private static final String KEY_ERROR_CODE_USER_NOT_SIGNED = "participatorybudget.site_property.error_code_user_not_signed";
    private static final String KEY_VALIDATION_VOTE_EMAIL_SENDER = "participatorybudget.site_property.vote_confirmation.sender";
    private static final String KEY_VALIDATION_VOTE_EMAIL_CC = "participatorybudget.site_property.vote_confirmation.cc";
    private static final String KEY_VALIDATION_VOTE_EMAIL_SUBJECT = "participatorybudget.site_property.vote_confirmation.subject";
    private static final String KEY_VALIDATION_VOTE_EMAIL_TEMPLATE = "participatorybudget.site_property.vote_confirmation.template.htmlblock";

    // Constant
    private static final String LOCALISATION_LABEL = "localisation";
    private static final String LOCALISATION_VALUE = "whole_city";
    private static final String SEPARATOR = "||";

    private MyVoteService _myVoteService = SpringContextService.getBean( MyVoteService.BEAN_NAME );
    private IRatingService _ratingService = SpringContextService.getBean( RatingService.BEAN_SERVICE );
    private IRatingSecurityService _ratingSecurityService = SpringContextService.getBean( RatingSecurityService.BEAN_SERVICE );

    private IVoteParArrandissementService _nbrVoteService = VoteParArrandissementService.getInstance( );

    // Messages
    private static final String MESSAGE_INFO_USER_MUST_COMPLETE_PROFILE = "participatorybudget.labelUserMustCompleteProfile";

    private CaptchaSecurityService _captchaService = new CaptchaSecurityService( );

    @Inject
    private IFollowService _followService = SpringContextService.getBean( FollowService.BEAN_SERVICE );
    @Inject
    private IResourceExtenderHistoryService _resourceExtenderService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );

    /**
     * Get the page to view the ratings of the current user
     * 
     * @param request
     *            The request
     * @return The HTML content to display
     * @throws UserNotSignedException
     *             If the user has not signed in
     * @throws SiteMessageException
     */
    @View( VIEW_MY_VOTES )
    public XPage getMyVotes( HttpServletRequest request ) throws UserNotSignedException, SiteMessageException
    {

        if ( !CampaignService.getInstance( ).isDuring( "VOTE" ) )
        {
            SiteMessageService.setMessage( request, MESSAGE_PAGE_NOT_ACCESSIBLE, SiteMessage.TYPE_STOP );
        }

        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        if ( !SecurityService.isAuthenticationEnable( ) || user == null )
        {
            throw new UserNotSignedException( );
        }
        String notify_valide = request.getParameter( PARAM_NOTIFY_VALIDE );

        MyInfosForm myInfo = MyInfosService.loadUserInfos( user );

        boolean isValidated = _myVoteService.isUserVoteValidated( user.getName( ) );
        int maxDcmtArrondissement = 0;
        if ( _nbrVoteService.selectVotePerLocation( myInfo.getArrondissement( ) ) != null )
        {
            maxDcmtArrondissement = _nbrVoteService.selectVotePerLocation( myInfo.getArrondissement( ) ).getNbVotes( );
        }
        int maxDcmtToutParis = _nbrVoteService.selectVotePerLocation( "whole_city" ).getNbVotes( );

        Map<String, Object> model = getModel( );

        if ( notify_valide != null && notify_valide.equals( "true" ) )
        {
            addInfo( MESSAGE_INFO_USER_VOTE_VALIDE, request.getLocale( ) );
        }
        model.put( MARK_USER_ARRONDISSEMENT, BudgetUtils.getArrondissementDisplay( user ) );
        model.put( MARK_NBR_PROJET_ARRDT, maxDcmtArrondissement );
        model.put( MARK_NBR_PROJET_PARIS, maxDcmtToutParis );

        model.put( MARK_USER_VOTES, _myVoteService.getUserVote( user ) );

        model.put( BudgetUtils.MARK_VOTE_VALIDATED, isValidated );

        model.put( BudgetUtils.MARK_CAMPAGNE_SERVICE, CampaignService.getInstance( ) );

        XPage page = getXPage( TEMPLATE_MY_VOTES, getLocale( request ), model );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_MY_VOTES_PAGE_TITLE, Locale.FRENCH ) );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_MY_VOTES_PAGE_PATH_LABEL, Locale.FRENCH ) );

        return page;
    }

    public String saveUserArr( HttpServletRequest request )
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        if ( user != null )
        {
            String arr = request.getParameter( MARK_ARRONDISSEMENT );

            BudgetUtils.setArrondissement( user.getName( ), arr );
        }
        return StringUtils.EMPTY;
    }

    /**
     * Load user current votes
     * 
     * @param request
     *            The request
     * @return The JSON result
     * @throws SiteMessageException
     */
    public String loadVotes( HttpServletRequest request )
    {
        if ( !CampaignService.getInstance( ).isDuring( "VOTE" ) )
        {
            return JsonUtil.buildJsonResponse( new JsonResponse( "closed" ) );
        }
        Map<String, Object> model = new HashMap<String, Object>( );

        List<MyVote> listMyVoteFull = new ArrayList<MyVote>( );
        List<MyVote> listMyVoteParis = new ArrayList<MyVote>( );
        List<MyVote> listMyVoteOther = new ArrayList<MyVote>( );

        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        if ( user != null )
        {
            listMyVoteFull = _myVoteService.getUserVote( user );
            model.put( MARK_USER_ARRONDISSEMENT, BudgetUtils.getArrondissementDisplay( user ) );
            model.put( BudgetUtils.MARK_VOTE_VALIDATED, _myVoteService.isUserVoteValidated( user.getName( ) ) );
        }

        for ( MyVote vote : listMyVoteFull )
        {
            if ( LOCALISATION_VALUE.equals( vote.getDocument( ).getAttribute( LOCALISATION_LABEL ).getTextValue( ) ) )
            {
                listMyVoteParis.add( vote );
            }
            else
            {
                listMyVoteOther.add( vote );
            }
        }

        model.put( MARK_USER_VOTES, listMyVoteParis );
        model.put( MARK_USER_TYPE_VOTE, MARK_USER_VOTES_PARIS );

        String templateVotesParis = AppTemplateService.getTemplate( TEMPLATE_NAVBAR_PROJECT, request.getLocale( ), model ).getHtml( );

        model.put( MARK_USER_VOTES, listMyVoteOther );
        if ( user != null )
        {
            model.put( MARK_USER_TYPE_VOTE, BudgetUtils.getArrondissementDisplay( user ) );
        }
        else
        {
            model.put( MARK_USER_TYPE_VOTE, MARK_USER_VOTES_OTHER );
        }

        String templateVotesOther = AppTemplateService.getTemplate( TEMPLATE_NAVBAR_PROJECT, request.getLocale( ), model ).getHtml( );

        model.put( MARK_NBR_PROJET_ARRDT, listMyVoteOther.size( ) );
        model.put( MARK_NBR_PROJET_PARIS, listMyVoteParis.size( ) );

        String templateValidBtn = AppTemplateService.getTemplate( TEMPLATE_BUTTON_VALID_VOTE, request.getLocale( ), model ).getHtml( );

        boolean arrChoosed = true;
        if ( user != null && BudgetUtils.getArrondissement( user.getName( ) ).isEmpty( ) )
        {
            arrChoosed = false;
        }

        return JsonUtil.buildJsonResponse(
                new JsonResponse( templateVotesParis + SEPARATOR + templateVotesOther + SEPARATOR + templateValidBtn + SEPARATOR + arrChoosed ) );
    }

    /**
     * Do vote
     * 
     * @param request
     *            The request
     * @return The JSON result
     * @throws SiteMessageException
     */
    public String doVote( HttpServletRequest request ) throws SiteMessageException
    {
        String strIdExtendableResource = request.getParameter( RatingConstants.PARAMETER_ID_EXTENDABLE_RESOURCE );
        String strExtendableResourceType = request.getParameter( RatingConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE );
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        String strUserId = user.getName( );
        MyInfosForm myInfos = MyInfosService.loadUserInfos( user );
        int maxDcmtArrondissement = 0;
        int maxDcmtToutParis = 0;
        VotePerLocation voteLoc = _nbrVoteService.selectVotePerLocation( myInfos.getArrondissement( ) );
        if ( voteLoc != null )
        {
            maxDcmtArrondissement = voteLoc.getNbVotes( );
        }
        voteLoc = _nbrVoteService.selectVotePerLocation( "whole_city" );
        if ( voteLoc != null )
        {
            maxDcmtToutParis = voteLoc.getNbVotes( );
        }
        String localisationProjet = request.getParameter( Constants.PROJECT_LOCALISATION );

        try
        {

            // This is duplicated in RatingValidationService.java because here
            // we want to return json
            // instead of redirecting to an url or throwing a
            // SiteMessageException
            if ( !CampaignService.getInstance( ).isDuring( "VOTE" ) || _myVoteService.isUserVoteValidated( strUserId ) )
            {
                SiteMessageService.setMessage( request, RatingConstants.MESSAGE_CANNOT_VOTE, SiteMessage.TYPE_STOP );
            }

            if ( StringUtils.isBlank( strIdExtendableResource ) || StringUtils.isBlank( strExtendableResourceType ) )
            {
                SiteMessageService.setMessage( request, RatingConstants.MESSAGE_ERROR_GENERIC_MESSAGE, SiteMessage.TYPE_STOP );
            }
            if ( VoteHome.getVote( strUserId, Integer.parseInt( strIdExtendableResource ) ) != null )
            {
                return doCancelVote( request );
            }
            // Check if the user can vote or not
            try
            {
                if ( !_ratingSecurityService.canVote( request, strIdExtendableResource, strExtendableResourceType ) )
                {

                    if ( _ratingSecurityService.hasAlreadyVoted( request, strIdExtendableResource, strExtendableResourceType ) )
                    {

                        return JsonUtil.buildJsonResponse( new ErrorJsonResponse( JSON_ERROR_CODE_USER_ALREADY_VOTED,
                                DatastoreService.getDataValue( KEY_ERROR_CODE_USER_ALREADY_VOTED, "" ) ) );

                    }
                    return JsonUtil.buildJsonResponse(
                            new ErrorJsonResponse( JSON_ERROR_CODE_USER_CAN_NOT_VOTE, DatastoreService.getDataValue( KEY_ERROR_CODE_USER_CAN_NOT_VOTE, "" ) ) );
                }
            }
            catch( UserNotSignedException e )
            {
                SiteMessageService.setMessage( request, RatingConstants.MESSAGE_CANNOT_VOTE, SiteMessage.TYPE_STOP );
            }
        }
        catch( SiteMessageException e )
        {
            return JsonUtil.buildJsonResponse( new ErrorJsonResponse( AppPathService.getSiteMessageUrl( request ) ) );
        }

        //
        // This is duplicated in RatingValidationService.java because here we
        // want to return json
        // instead of redirecting to an url or throwing a SiteMessageException
        int nbrVoteUserArrond = VoteHome.getVoteUserNotLocalisation( strUserId, 75000 );
        int nbrVoteUserParis = VoteHome.getVoteUserArrondissement( strUserId, 75000 );

        if ( nbrVoteUserParis >= maxDcmtToutParis && nbrVoteUserArrond >= maxDcmtArrondissement )
        {

            return JsonUtil.buildJsonResponse(
                    new ErrorJsonResponse( JSON_ERROR_CODE_USER__VOTED_MAX, DatastoreService.getDataValue( KEY_ERROR_CODE_USER__VOTED_MAX, "" ) ) );
        }
        if ( nbrVoteUserParis >= maxDcmtToutParis && maxDcmtToutParis > 0
                && request.getParameter( Constants.PROJECT_LOCALISATION ).equals( Constants.LOCALISATION_PARIS ) )
        {

            return JsonUtil.buildJsonResponse(
                    new ErrorJsonResponse( JSON_ERROR_ALREADY_VOTED_TOUT_PARIS, DatastoreService.getDataValue( KEY_ERROR_ALREADY_VOTED_TOUT_PARIS, "" ) ) );
        }
        if ( nbrVoteUserArrond >= maxDcmtArrondissement && maxDcmtArrondissement > 0
                && !request.getParameter( Constants.PROJECT_LOCALISATION ).equals( Constants.LOCALISATION_PARIS ) )
        {

            return JsonUtil.buildJsonResponse( new ErrorJsonResponse( JSON_ERROR_ALREADY_VOTED_ARRONDISSEMENT,
                    DatastoreService.getDataValue( KEY_ERROR_ALREADY_VOTED_ARRONDISSEMENT, "" ) ) );

        }
        if ( localisationProjet != null && !localisationProjet.equals( BudgetUtils.getArrondissementDisplay( user ) )
                && !localisationProjet.equals( Constants.LOCALISATION_PARIS ) )
        {

            return JsonUtil
                    .buildJsonResponse( new ErrorJsonResponse( JSON_ERROR_VOTE_USER_ARROND, DatastoreService.getDataValue( KEY_ERROR_VOTE_USER_ARROND, "" ) ) );

        }

        // Do RatingValidationManagementService after the previous checks to get
        // nice JSON error messages
        // instead of url redirects. If a user manually uses
        // extend/modules/rating/doVote.jsp instead of
        // participatorybudget/doVote.jsp, he will get redirects but that's not a
        // problem for a manually crafted url
        String strErrorUrl = RatingValidationManagementService.validateRating( request, SecurityService.getInstance( ).getRegisteredUser( request ),
                strIdExtendableResource, strExtendableResourceType, BudgetRatingService.VOTE_VALUE );

        if ( StringUtils.isNotEmpty( strErrorUrl ) )
        {
            if ( !strErrorUrl.startsWith( CONSTANT_HTTP ) )
            {
                strErrorUrl = AppPathService.getBaseUrl( request ) + strErrorUrl;
            }

            return JsonUtil.buildJsonResponse( new ErrorJsonResponse( strErrorUrl ) );
        }
        // Users can only vote 1 in budget participatif implementation
        _ratingService.doVote( strIdExtendableResource, strExtendableResourceType, BudgetRatingService.VOTE_VALUE, request );

        // TODO : send notifications to mailing list
        // sendNotification( request, strIdExtendableResource,
        // strExtendableResourceType, nVoteValue );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( RatingConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        model.put( RatingConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_BUTTON_CANCEL_VOTE, request.getLocale( ), model );

        return JsonUtil.buildJsonResponse( new JsonResponse( template.getHtml( ) ) );
    }

    /**
     * Do cancel a vote
     * 
     * @param request
     *            The request
     * @return The JSON result
     */
    public String doCancelVote( HttpServletRequest request )
    {
        String strIdExtendableResource = request.getParameter( RatingConstants.PARAMETER_ID_EXTENDABLE_RESOURCE );
        String strExtendableResourceType = request.getParameter( RatingConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE );
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        String strUserId = user.getName( );
        if ( !CampaignService.getInstance( ).isDuring( "VOTE" ) || _myVoteService.isUserVoteValidated( strUserId ) )
        {

            try
            {
                SiteMessageService.setMessage( request, RatingConstants.MESSAGE_CANNOT_VOTE, SiteMessage.TYPE_STOP );
            }
            catch( SiteMessageException e )
            {
                return JsonUtil.buildJsonResponse( new ErrorJsonResponse( AppPathService.getSiteMessageUrl( request ) ) );
            }
        }

        try
        {
            if ( StringUtils.isBlank( strIdExtendableResource ) || StringUtils.isBlank( strExtendableResourceType ) )
            {
                SiteMessageService.setMessage( request, RatingConstants.MESSAGE_ERROR_GENERIC_MESSAGE, SiteMessage.TYPE_STOP );
            }

            // Check if the user can vote or not
            if ( !_ratingSecurityService.canDeleteVote( request, strIdExtendableResource, strExtendableResourceType )
                    && VoteHome.getVote( strUserId, Integer.parseInt( strIdExtendableResource ) ) == null )
            {
                SiteMessageService.setMessage( request, RatingConstants.MESSAGE_CANNOT_VOTE, SiteMessage.TYPE_STOP );
            }
        }
        catch( SiteMessageException e )
        {
            return JsonUtil.buildJsonResponse( new ErrorJsonResponse( AppPathService.getSiteMessageUrl( request ) ) );
        }

        _ratingService.doCancelVote( user, strIdExtendableResource, strExtendableResourceType );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( RatingConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        model.put( RatingConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_BUTTON_DO_VOTE, request.getLocale( ), model );

        return JsonUtil.buildJsonResponse( new JsonResponse( template.getHtml( ) ) );
    }

    /**
     * 
     * @param request
     * @return
     * @throws UserNotSignedException
     */
    public String doCancelVoteUser( HttpServletRequest request ) throws UserNotSignedException
    {
        return _myVoteService.cancelVote( request );
    }

    /**
     * Retrieve JSON for vote recap before confirmation
     * 
     * @param request
     * @return
     * @throws UserNotSignedException
     */
    public String valideVote( HttpServletRequest request ) throws UserNotSignedException
    {

        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        int nbrVoteArrdt = VoteHome.getVoteUserNotLocalisation( user.getName( ), 75000 );
        int nbrVoteParis = VoteHome.getVoteUserArrondissement( user.getName( ), 75000 );
        int nbrVoteTotal = nbrVoteArrdt + nbrVoteParis;

        MyVote myVote = new MyVote( );
        myVote.setNbTotVotes( nbrVoteTotal );
        myVote.setNbTotVotesArrondissement( nbrVoteArrdt );
        myVote.setNbTotVotesToutParis( nbrVoteParis );
        myVote.setVoteVlidated( false );

        AbstractJsonResponse jsonResponse = new JsonResponse( myVote );

        return JsonUtil.buildJsonResponse( jsonResponse );

    }

    /**
     * Validate votes for the user
     * 
     * @param request
     * @return
     * @throws UserNotSignedException
     * @throws SiteMessageException
     */
    @Action( value = ACTION_VALIDATE_VOTES )
    public XPage doValideVote( HttpServletRequest request ) throws UserNotSignedException, SiteMessageException
    {

        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        boolean bValidateVote = BooleanUtils.toBoolean( request.getParameter( PARAM_VALIDATE ) );
        boolean bVotesInFollow = BooleanUtils.toBoolean( request.getParameter( PARAM_FOLLOW ) );

        if ( bValidateVote )
        {

            _myVoteService.validateVotes( user.getName( ), Vote.Status.STATUS_VALIDATED.getValeur( ) );
            addInfo( MESSAGE_INFO_USER_VOTE_VALIDE, request.getLocale( ) );

            if ( bVotesInFollow )
            {
                // Creating extender entry
                ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter( );
                filter.setExtendableResourceType( RatingAddOnService.PROPERTY_RESOURCE_TYPE );
                filter.setUserGuid( user.getName( ) );
                filter.setExtenderType( FollowResourceExtender.RESOURCE_EXTENDER );
                List<ResourceExtenderHistory> listResourceExtender = _resourceExtenderService.findByFilter( filter );
                for ( Vote vote : VoteHome.getVoteUser( user.getName( ) ) )
                {
                    String strProjetId = String.valueOf( vote.getProjetId( ) );
                    boolean bAlreadyFollow = false;
                    for ( ResourceExtenderHistory resourceExtenderHistory : listResourceExtender )
                    {
                        if ( StringUtils.equals( resourceExtenderHistory.getIdExtendableResource( ), strProjetId ) )
                        {
                            bAlreadyFollow = true;
                            break;
                        }
                    }
                    if ( !bAlreadyFollow )
                    {
                        _followService.doFollow( String.valueOf( vote.getProjetId( ) ), RatingAddOnService.PROPERTY_RESOURCE_TYPE, 1, request );
                    }
                }

                // Activating notification
                String strLuteceUserName = user.getName( );
                if ( !hasUserSubscribedToResource( strLuteceUserName, SUBSCRIPTION_UPDATE_ON_REALIZATION ) )
                {
                    Subscription subscription = new Subscription( );
                    subscription.setIdSubscribedResource( "*" );
                    subscription.setUserId( strLuteceUserName );
                    subscription.setSubscriptionKey( SUBSCRIPTION_UPDATE_ON_REALIZATION );
                    subscription.setSubscriptionProvider( "participatoryideation.subscriptionProviderName" );
                    SubscriptionService.getInstance( ).createSubscription( subscription );
                }
            }

            // Send email
            sendConfirmationVoteEmail( user );
        }

        return getMyVotes( request );

    }

    private boolean hasUserSubscribedToResource( String strLuteceUserName, String strSubscriptionKey )
    {
        SubscriptionFilter filter = new SubscriptionFilter( strLuteceUserName, "participatoryideation.subscriptionProviderName", strSubscriptionKey, "*" );
        List<Subscription> listSubscription = SubscriptionService.getInstance( ).findByFilter( filter );
        if ( ( listSubscription != null ) && ( listSubscription.size( ) > 0 ) )
        {
            return true;
        }
        return false;
    }

    /**
     * Send email to the user to confirm the validation of the vote
     * 
     * @param user
     *            The user from who to send confirmation email
     */
    private void sendConfirmationVoteEmail( LuteceUser user )
    {
        if ( user != null )
        {
            // Retrieve the list of the validated vote of the user
            List<Vote> listVote = VoteHome.getVoteUser( user.getName( ), Vote.Status.STATUS_VALIDATED.getValeur( ) );

            if ( listVote != null && !listVote.isEmpty( ) )
            {
                // // Create the list of the projects title which will be display in the email
                // StringBuilder strListProjectTitleBuilder = new StringBuilder( StringUtils.EMPTY );
                // Iterator<Vote> voteIterator = listVote.iterator( );
                // while ( voteIterator.hasNext( ) )
                // {
                // strListProjectTitleBuilder.append( voteIterator.next( ).getTitle( ) );
                // if( voteIterator.hasNext( ) )
                // {
                // strListProjectTitleBuilder.append( VOTE_TITLE_SEPARATOR );
                // }
                // }

                // Retrieve the confirmation vote email information
                String strEmailSender = DatastoreService.getDataValue( KEY_VALIDATION_VOTE_EMAIL_SENDER, StringUtils.EMPTY );
                String strEmailCc = DatastoreService.getDataValue( KEY_VALIDATION_VOTE_EMAIL_CC, StringUtils.EMPTY );
                String strEmailSubject = DatastoreService.getDataValue( KEY_VALIDATION_VOTE_EMAIL_SUBJECT, StringUtils.EMPTY );
                String strEmailTemplate = DatastoreService.getDataValue( KEY_VALIDATION_VOTE_EMAIL_TEMPLATE, StringUtils.EMPTY );

                // Compute the current date and retrieve the MyInfosForm of the user to get its nickname
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat( DATE_EMAIL_VALIDATION_PATTERN );
                String strFormattedDate = simpleDateFormat.format( new Date( ) );
                MyInfosForm userInfoForm = MyInfosService.loadUserInfos( user );

                // Create the map for the freemarker marker for email
                Map<String, Object> model = new HashMap<>( );
                model.put( MARK_DATE_VALIDATION, strFormattedDate );
                model.put( MARK_USER_PSEUDO, ( userInfoForm != null ) ? userInfoForm.getNickname( ) : null );
                model.put( MARK_LIST_VALIDATE_PROJECTS, listVote );

                // Replace the Freemarker marks with the models data
                HtmlTemplate htmlTemplateMail = AppTemplateService.getTemplateFromStringFtl( strEmailTemplate, Locale.FRENCH, model );

                // Send the mail to the user
                MailService.sendMailHtml( user.getEmail( ), strEmailCc, null, strEmailSender, strEmailSender, strEmailSubject, htmlTemplateMail.getHtml( ) );
            }
        }
    }

    /**
     * DoSend Avis using the AJAX mode
     * 
     * @param request
     *            The request
     */
    /*
     * public String doSendAvis(HttpServletRequest request) { String strMail = request.getParameter(PARAMETER_MAIL); String strMessage =
     * request.getParameter(PARAMETER_MESSAGE); String strBackUrl = DatastoreService.getDataValue( KEY_PARIS_CONNECT_SEND_AVIS_BACK_URL,
     * "https://budgetparticipatif.paris.fr/bp/"); AbstractJsonResponse jsonResponse = null;
     * 
     * // if(SecurityTokenService.getInstance().validate(request, // TOKEN_DO_SEND_AVIS) ) // // { // // AppLogService.error(
     * "doSubscribeAlert: Token not validated" ); // // jsonResponse=new // BudgetErrorJsonResponse(JSON_ERROR_DURING_SEND_AVIS,SecurityTokenService.
     * getInstance().getToken(request, // TOKEN_DO_SEND_AVIS));
     * 
     * // }else // //
     * 
     * if (StringUtils.isNotEmpty(strMessage) && ParisConnectService.getInstance().sendAvisMessage(strMail, strMessage, strBackUrl)) { jsonResponse = new
     * JsonResponse(new CampagneResponse(Boolean.TRUE, SecurityTokenService.getInstance().getToken(request, TOKEN_DO_SEND_AVIS)));
     * 
     * } else { jsonResponse = new CampagneErrorJsonResponse( JSON_ERROR_DURING_SEND_AVIS, SecurityTokenService .getInstance() .getToken(request,
     * TOKEN_DO_SEND_AVIS)); }
     * 
     * return JsonUtil.buildJsonResponse(jsonResponse); }
     */

    /**
     * 
     * @return true if the captcha is enable
     */
    private boolean isEnableCaptcha( )
    {
        return PluginService.isPluginEnable( JCAPTCHA_PLUGIN ) && new Boolean( DatastoreService.getDataValue( KEY_ENABLE_CAPTCHA_USER_INFORMATIONS, "false" ) );
    }

    /**
     * 
     * @param request
     * @param user
     * @return
     */
    private static String isArrondissementValide( HttpServletRequest request, LuteceUser user )
    {

        AbstractJsonResponse jsonResponse = null;
        String arrondissement = request.getParameter( PARAMETER_ARRONDISSEMENT );
        String codePostal = BudgetUtils.getArrondissementDisplay( user ).trim( );

        if ( !arrondissement.equals( codePostal ) && !arrondissement.equals( Constants.LOCALISATION_PARIS ) )
        {

            jsonResponse = new ErrorJsonResponse( JSON_ERROR_CHECKED_ARRONDISSEMENT, DatastoreService.getDataValue( KEY_ERROR_CHECKED_ARRONDISSEMENT, "" ) );
            return JsonUtil.buildJsonResponse( jsonResponse );

        }
        else
        {

            return null;
        }
    }

    /**
     * Get the URL to view My Vote of a user
     * 
     * @param request
     *            The request
     * @return The URL
     */
    public static String getUrlViewUserVote( HttpServletRequest request )
    {
        UrlItem urlItem = new UrlItem( AppPathService.getBaseUrl( request ) + AppPathService.getPortalUrl( ) );
        urlItem.addParameter( "page", "mesVotes" );
        urlItem.addParameter( "view", VIEW_MY_VOTES );

        return urlItem.getUrl( );
    }

}
