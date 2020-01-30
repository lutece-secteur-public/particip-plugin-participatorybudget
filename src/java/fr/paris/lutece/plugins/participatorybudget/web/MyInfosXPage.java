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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.avatar.service.AvatarService;
import fr.paris.lutece.plugins.participatorybudget.business.MyAccount;
import fr.paris.lutece.plugins.participatorybudget.business.MyInfosForm;
import fr.paris.lutece.plugins.participatorybudget.service.MyInfosListenerService;
import fr.paris.lutece.plugins.participatorybudget.service.MyInfosService;
import fr.paris.lutece.plugins.participatorybudget.service.avatar.CampagneAvatarService;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampagneUploadHandler;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.plugins.participatorybudget.util.Constants;
import fr.paris.lutece.plugins.participatorybudget.util.ModelUtils;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.portal.ThemesService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.json.AbstractJsonResponse;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage ParisConnectUser xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = MyInfosXPage.PAGE_MY_INFOS, pageTitleI18nKey = "participatorybudget.xpage.myinfos.pageTitle", pagePathI18nKey = "participatorybudget.xpage.myInfos.pagePathLabel" )
public class MyInfosXPage extends MVCApplication
{

    /**
     * Name of this application
     */
    public static final String TOKEN_DO_SEND_AVIS = "doSendAvis";
    public static final String TOKEN_DO_SUBSCRIBE_ALERT = "doSubscribeAlert";
    public static final String TOKEN_DO_SAVE_MY_INFOS = "doSaveMyInfos";
    public static final String TOKEN_DO_CREATE_MY_INFOS = "doCreateMyInfos";
    public static final String PAGE_MY_INFOS = "mesInfos";
    private static final long serialVersionUID = -4316691400124512414L;
    // Templates
    private static final String TEMPLATE_MES_INFOS = "/skin/plugins/participatorybudget/mes_infos.html";
    private static final String TEMPLATE_CREATE_MY_INFOS = "/skin/plugins/participatorybudget/create_my_infos.html";
    private static final String TEMPLATE_MES_INFOS_POPUP = "/skin/plugins/participatorybudget/mes_infos_popup.html";
    private static final String TEMPLATE_CHECKED_MY_INFOS_AFTER_LOGIN = "/skin/plugins/participatorybudget/checked_my_infos_after_login.html";

    // Views
    private static final String VIEW_MY_INFOS = "mesinfos";
    private static final String VIEW_MY_VOTES = "myVotes";
    private static final String VIEW_CREATE_MY_INFOS = "createMyInfos";

    // Actions
    private static final String ACTION_SAVE = "save";
    private static final String ACTION_DO_CREATE_INFOS = "doCreateInfos";

    // Infos
    private static final String INFO_SAVED = "participatorybudget.info.saved";
    private static final String INFO_VALIDATION_MAIL_SEND = "participatorybudget.info.validationMailSend";
    private static final String ERROR_DURING_VALIDATION_MAIL_SEND = "participatorybudget.error.duringValidationMailSend";

    // Markers

    private static final String MARK_LOGIN = "login";
    private static final String MARK_PASSWORD = "password";
    private static final String MARK_CONFIRMATION_PASSWORD = "confirmation_password";
    private static final String MARK_NICKNAME = "nickname";
    private static final String MARK_FIRSTNAME = "firstname";
    private static final String MARK_CIVILITY = "civility";
    private static final String MARK_LASTNAME = "lastname";
    private static final String MARK_ADDRESS = "address";
    private static final String MARK_POSTAL_CODE = "postal_code";
    private static final String MARK_BIRTHDATE = "birthdate";
    private static final String MARK_ARRONDISSEMENT = "arrondissement";
    private static final String MARK_ILIVEINPARIS = "iliveinparis";
    private static final String MARK_AREA_LIST = "area_list";
    private static final String MARK_POSTAL_CODE_LIST = "postal_code_list";
    private static final String MARK_COMPLETE_INFO = "completeInfos";
    private static final String MARK_GEO_JSON = "geoJson";
    private static final String MARK_TYPE_DOCUMENT = "document";
    private static final String MARK_EXTENDABLERESOURCETYPE = "extendableResourceType";
    private static final String MARK_PAGE = "page";
    private static final String MARK_VOTEVALID = "voteValid";
    private static final String MARK_FORM = "form";
    private static final String MARK_TRUE = "true";
    private static final String MARK_URL_MONCOMPTE = "url_moncompte";

    private static final String MARK_THEME = "theme";
    private static final String MARK_SEND_ACCOUNT_VALIDATION = "send_account_validation";
    private static final String MARK_ACCOUNT_VERIFIED = "account_verified";
    private static final String MARK_USER_VALID = "user_valid";
    private static final String MARK_INIT_FORM = "init_form";
    private static final String MARK_IS_FOR_POPUP = "is_for_popup";
    private static final String MARK_AVATAR_URL = "avatar_url";
    private static final String MARK_HANDLER = "handler";

    private static final String MARK_CAMPAGNE_SERVICE = "campagneService";

    // Jsp redirections
    private static final String JSP_PORTAL = "jsp/site/Portal.jsp";

    // Messages
    private static final String MESSAGE_INFO_USER_MUST_COMPLETE_PROFILE = "participatorybudget.labelUserMustCompleteProfile";
    private static final String MESSAGE_ERROR_CONFIRMATION_PASSWORD = "participatorybudget.messageErrorConfirmationPassword";
    private static final String MESSAGE_ERROR_NICKNAME_ALREADY_EXIST = "participatorybudget.messageErrorNicknameAlreadyExist";
    private static final String MESSAGE_ERROR_ADRESS_GEOLOC_FORMAT = "participatorybudget.messageErrorAdressGeolocFormat";
    private static final String MESSAGE_ERROR_NICKNAME_MISSING = "participatorybudget.validation.nickname.notEmpty";
    private static final String PROPERTY_CONFIRM_MODIFY_ARRAND = "participatorybudget.property_confirm_modify_arrand";
    private static final String PROPERTY_CONFIRM_MODIFY_ARRAND_WITH_VOTES_DELETING = "participatorybudget.property_confirm_modify_arrand_with_votes_deleting";
    private static final String PROPERTY_ERROR_MODIFY_ARRAND = "participatorybudget.property_error_changement_arrond";
    private static final String PROPERTY_ERROR_END_PHASE = "participatorybudget.property_error_end_phase";
    private static final String CAMPAGNE_BP_APP_CODE = AppPropertiesService.getProperty( "participatorybudget.identity.application.code" );
    // Parameters
    private static final String PARAMETER_COMPLETE_INFOS = "completeInfos";
    private static final String PARAMETER_REFERER = "referer";

    // Json ERROR CODE
    // Json ERROR CODE
    private static final String JSON_ERROR_CODE_USER_NOT_SIGNED = "USER_NOT_SIGNED";
    private static final String JSON_ERROR_DURING_SEND_AVIS = "JSON_ERROR_DURING_SEND_AVIS";
    private static final String JSON_ERROR_CODE_ACCOUNT_NOT_VERIFIED = "ACCOUNT_NOT_VERIFIED";

    // Properties
    private static final String PROPERTY_URL_MONCOMPTE = "participatorybudget.url.moncompte";

    // Key
    private static final String IDS_KEY_USER_GENDER = AppPropertiesService.getProperty( "participatorybudget.identity.user.gender" );
    private static final String IDS_KEY_USER_FIRST_NAME = AppPropertiesService.getProperty( "participatorybudget.identity.user.first_name" );
    private static final String IDS_KEY_USER_LAST_NAME = AppPropertiesService.getProperty( "participatorybudget.identity.user.last_name" );
    private static final String IDS_KEY_USER_BIRTHDAY = AppPropertiesService.getProperty( "participatorybudget.identity.user.birthday" );
    private static final String IDS_KEY_USER_ADDRESS = AppPropertiesService.getProperty( "participatorybudget.identity.user.address" );
    private static final String IDS_KEY_USER_ADDRESS_POSTAL_CODE = AppPropertiesService.getProperty( "participatorybudget.identity.user.postal_code" );
    private static final String IDS_KEY_USER_ADDRESS_CITY = AppPropertiesService.getProperty( "participatorybudget.identity.user.city" );
    private static final String IDS_KEY_USER_ADDRESS_DETAIL = AppPropertiesService.getProperty( "participatorybudget.identity.user.address_detail" );

    private static final String MARK_IDS_ATTRIBUTES = "ids_attributes";
    private static final String MARK_LABEL_IDS_GENDER = "Civilit&eacute;";
    private static final String MARK_LABEL_IDS_FIRST_NAME = "Pr&eacute;nom";
    private static final String MARK_LABEL_IDS_LAST_NAME = "Nom";
    private static final String MARK_LABEL_IDS_BIRTHDAY = "Date de naissance";
    private static final String MARK_LABEL_IDS_ADDRESS = "Adresse du domicile";

    private static final String COMMA = ", ";

    private static final Map<String, String> MAP_IDS_ATTRIBUTES = new HashMap<String, String>( )
    {
        {
            put( MARK_LABEL_IDS_GENDER, IDS_KEY_USER_GENDER );
            put( MARK_LABEL_IDS_FIRST_NAME, IDS_KEY_USER_FIRST_NAME );
            put( MARK_LABEL_IDS_LAST_NAME, IDS_KEY_USER_LAST_NAME );
            put( MARK_LABEL_IDS_BIRTHDAY, IDS_KEY_USER_BIRTHDAY );
        }
    };

    private static final Map<String, String> MAP_CIVILITY = new HashMap<String, String>( )
    {
        {
            put( "0", "participatorybudget.mes_infos.civilityNPR" );
            put( "1", "participatorybudget.mes_infos.civilityMme" );
            put( "2", "participatorybudget.mes_infos.civilityM" );
        }
    };

    // regex for address split
    private static final String REGEX_ADDRESS = AppPropertiesService.getProperty( "participatorybudget.address.suggestpoi.regex" );
    private Pattern _patternAddress;

    // Local session variables
    private MyInfosForm _form;
    private MyAccount _accountForm;

    // private BudgetRatingService _budgetRating = SpringContextService.getBean(
    // BudgetRatingService.BEAN_NAME );

    /**
     * Constructor
     */
    public MyInfosXPage( )
    {
        super( );
        _patternAddress = Pattern.compile( REGEX_ADDRESS );
    }

    /**
     * Displays user's personal infos
     * 
     * @param request
     *            The HTTP request
     * @return The page
     * @throws UserNotSignedException
     *             if the user is not signed
     */
    @View( value = VIEW_CREATE_MY_INFOS )
    public XPage getCreateMyInfos( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );

        MyInfosForm myInfos = _form;

        if ( _accountForm != null )
        {
            model.put( MARK_LOGIN, _accountForm.getLogin( ) );
            model.put( MARK_PASSWORD, _accountForm.getPassword( ) );
            model.put( MARK_CONFIRMATION_PASSWORD, _accountForm.getConfirmationPassword( ) );

        }

        if ( myInfos != null )
        {

            model.put( MARK_CIVILITY, myInfos.getCivility( ) );
            model.put( MARK_NICKNAME, myInfos.getNickname( ) );
            model.put( MARK_FIRSTNAME, myInfos.getFirstname( ) );
            model.put( MARK_LASTNAME, myInfos.getLastname( ) );
            model.put( MARK_ARRONDISSEMENT, myInfos.getArrondissement( ) );
            model.put( MARK_ADDRESS, myInfos.getAddress( ) );
            model.put( MARK_BIRTHDATE, myInfos.getBirthdate( ) );
            model.put( MARK_POSTAL_CODE, myInfos.getPostalCode( ) );
            model.put( MARK_ILIVEINPARIS, myInfos.getIliveinparis( ) );
            model.put( MARK_SEND_ACCOUNT_VALIDATION, myInfos.getSendaccountvalidation( ) );
            model.put( MARK_GEO_JSON, myInfos.getGeojson( ) );
            model.put( MARK_INIT_FORM, false );

        }
        else
        {

            model.put( MARK_INIT_FORM, true );
        }
        model.put( MARK_HANDLER, SpringContextService.getBean( CampagneUploadHandler.BEAN_NAME ) );

        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, MyInfosXPage.TOKEN_DO_CREATE_MY_INFOS ) );
        model.put( MARK_AREA_LIST, MyInfosService.getArrondissements( ) );
        model.put( MARK_POSTAL_CODE_LIST, MyInfosService.getPostalCodes( ) );

        // Check if the submission/vote phases are open or not
        model.put( MARK_CAMPAGNE_SERVICE, CampaignService.getInstance( ) );

        return getXPage( TEMPLATE_CREATE_MY_INFOS, request.getLocale( ), model );

    }

    /**
     * Handles the removal form of a parisconnectuser bm * @param request The Http request
     * 
     * @return the jsp URL to display the form to manage parisconnectusers
     * @throws UserNotSignedException
     *             if user not signed
     */
    @Action( ACTION_DO_CREATE_INFOS )
    public XPage doCreateMyInfos( HttpServletRequest request ) throws UserNotSignedException
    {

        LuteceUser user = SecurityService.getInstance( ).getRemoteUser( request );

        boolean bError = false;

        boolean bCapchaVerified = true;
        MyInfosForm form = new MyInfosForm( );
        MyAccount formMyAccount = new MyAccount( );

        populate( formMyAccount, request );
        populate( form, request );

        // !SecurityTokenService.getInstance().validate(request,
        // MyInfosXPage.TOKEN_DO_SAVE_MY_INFOS)

        // Check constraints

        bError = !bCapchaVerified || !validateBean( formMyAccount, getLocale( request ) ) || !validateBean( form, getLocale( request ) );

        if ( !CampaignService.getInstance( ).isDuring( Constants.VOTE ) && StringUtils.isBlank( form.getNickname( ) ) )
        {
            bError = true;
            addError( MESSAGE_ERROR_NICKNAME_MISSING, getLocale( request ) );
        }

        if ( !bError && !MyInfosService.setAdressValid( form ) )
        {
            bError = true;
            addError( MESSAGE_ERROR_ADRESS_GEOLOC_FORMAT, getLocale( request ) );
        }

        if ( !bError && MyInfosService.isNicknameAlreadyExist( form.getNickname( ) ) )
        {
            bError = true;
            addError( MESSAGE_ERROR_NICKNAME_ALREADY_EXIST, getLocale( request ) );
        }

        if ( !bError && !formMyAccount.getPassword( ).equals( formMyAccount.getConfirmationPassword( ) ) )
        {
            bError = true;
            addError( MESSAGE_ERROR_CONFIRMATION_PASSWORD, getLocale( request ) );
        }

        MyInfosService.saveUserInfos( user.getName( ), form );
        MyInfosService.saveUserEmail( user.getName( ), user.getEmail( ) );
        // Save avatar
        CampagneAvatarService.createAvatar( request, user.getName( ) );

        // Add message

        //
        // MyInfosService.sendAccountValidationMail(request, getLocale(request), createAccountResult.getUid());
        //
        //
        //
        // addInfo(INFO_VALIDATION_MAIL_SEND,
        // getLocale(request));
        // reint form
        _form = null;
        _accountForm = null;
        return redirectView( request, VIEW_MY_INFOS );
    }

    /**
     * Displays user's personal infos
     * 
     * @param request
     *            The HTTP request
     * @return The page
     * @throws UserNotSignedException
     *             if the user is not signed
     */
    @View( value = VIEW_MY_INFOS, defaultView = true )
    public XPage getMyInfos( HttpServletRequest request ) throws UserNotSignedException
    {
        return getMyInfos( request, false );
    }

    /**
     * Get the content of the my info page
     * 
     * @param request
     *            the request
     * @param bIsForPopup
     *            True if the content should be formated to be displayed in a popup, false otherwise
     * @return The required XPage
     * @throws UserNotSignedException
     *             If the user has not signed in
     */
    private XPage getMyInfos( HttpServletRequest request, boolean bIsForPopup ) throws UserNotSignedException
    {
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

            boolean bInitForm = false;
            // ParisConnectUser user = getTestUser( );
            if ( user != null )
            {

                MyAccount myAccountForm;
                if ( _accountForm != null )
                {
                    myAccountForm = _accountForm;
                    _accountForm = null;
                }
                else
                {
                    myAccountForm = new MyAccount( );
                }

                populate( myAccountForm, user );

                MyInfosForm myInfos;

                if ( _form != null )
                {
                    myInfos = _form;
                    _form = null;
                }
                else
                {
                    bInitForm = true;
                    myInfos = MyInfosService.loadUserInfos( user );
                }

                boolean bCompleteInfos = Boolean.parseBoolean( request.getParameter( PARAMETER_COMPLETE_INFOS ) );
                // test if the user has validated his account
                // if the template is call for popup this test is already make
                if ( !bIsForPopup )
                {
                    // MyInfosService.isAccountVerified(user);
                }

                if ( bCompleteInfos )
                {
                    addInfo( MESSAGE_INFO_USER_MUST_COMPLETE_PROFILE, request.getLocale( ) );
                }

                Map<String, Object> model = getModel( );

                if ( myAccountForm != null )
                {
                    model.put( MARK_LOGIN, myAccountForm.getLogin( ) );
                    model.put( MARK_PASSWORD, myAccountForm.getPassword( ) );
                    model.put( MARK_CONFIRMATION_PASSWORD, myAccountForm.getConfirmationPassword( ) );

                }
                model.put( MARK_HANDLER, SpringContextService.getBean( CampagneUploadHandler.BEAN_NAME ) );
                model.put( MARK_NICKNAME, myInfos.getNickname( ) );
                model.put( MARK_AVATAR_URL, AvatarService.getAvatarUrl( user.getName( ) ) );
                model.put( MARK_FIRSTNAME, myInfos.getFirstname( ) );
                model.put( MARK_LASTNAME, myInfos.getLastname( ) );
                model.put( MARK_ADDRESS, myInfos.getAddress( ) );
                model.put( MARK_BIRTHDATE, myInfos.getBirthdate( ) );
                model.put( MARK_ILIVEINPARIS, myInfos.getIliveinparis( ) );
                model.put( MARK_ARRONDISSEMENT, myInfos.getArrondissement( ) );
                model.put( MARK_SEND_ACCOUNT_VALIDATION, myInfos.getSendaccountvalidation( ) );
                model.put( MARK_INIT_FORM, bInitForm );

                model.put( MARK_ACCOUNT_VERIFIED, MyInfosService.isAccountVerified( user ) );
                model.put( MARK_AREA_LIST, MyInfosService.getArrondissements( ) );
                model.put( MARK_POSTAL_CODE_LIST, MyInfosService.getPostalCodes( ) );

                model.put( MARK_USER_VALID, myInfos.getIsValid( ) );

                model.put( MARK_COMPLETE_INFO, bCompleteInfos );

                model.put( MARK_IS_FOR_POPUP, bIsForPopup );

                model.put( MARK_CIVILITY, myInfos.getCivility( ) );
                model.put( MARK_GEO_JSON, myInfos.getGeojson( ) );
                model.put( MARK_POSTAL_CODE, myInfos.getPostalCode( ) );

                model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, MyInfosXPage.TOKEN_DO_SAVE_MY_INFOS ) );

                model.put( MARK_URL_MONCOMPTE, AppPropertiesService.getProperty( PROPERTY_URL_MONCOMPTE ) );

                // Check if the submission/vote phases are open or not
                model.put( MARK_CAMPAGNE_SERVICE, CampaignService.getInstance( ) );

                ModelUtils.storeUnauthorizedAddress( model, myInfos.getAddress( ), user );

                return getXPage( bIsForPopup ? TEMPLATE_MES_INFOS_POPUP : TEMPLATE_MES_INFOS, request.getLocale( ), model );
            }
        }

        throw new UserNotSignedException( );
    }

    /**
     * Handles the removal form of a parisconnectuser
     * 
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage parisconnectusers
     * @throws UserNotSignedException
     *             if user not signed
     * @throws SiteMessageException
     */
    @Action( ACTION_SAVE )
    public XPage doSave( HttpServletRequest request ) throws UserNotSignedException, SiteMessageException
    {
        if ( SecurityService.isAuthenticationEnable( ) )
        {

            boolean bError = false;
            LuteceUser user = SecurityService.getInstance( ).getRemoteUser( request );

            // ParisConnectUser user = getTestUser( );
            if ( user != null )
            {
                String voteValid = request.getParameter( MARK_VOTEVALID );
                MyInfosForm formSession = (MyInfosForm) request.getSession( ).getAttribute( MARK_FORM );
                if ( StringUtils.isNotEmpty( voteValid ) && voteValid.equals( MARK_TRUE ) && formSession != null )
                {
                    updateMyinfos( user, formSession, request );
                    request.getSession( ).removeAttribute( MARK_FORM );
                    MyInfosListenerService.deleteVotes( request );

                    return redirectView( request, VIEW_MY_INFOS );
                }

                boolean bCapchaVerified = true;
                MyInfosForm form = new MyInfosForm( );
                MyAccount myAccountForm = new MyAccount( );

                populate( form, request );
                populate( myAccountForm, request );

                // !SecurityTokenService.getInstance().validate(request,
                // MyInfosXPage.TOKEN_DO_SAVE_MY_INFOS)

                // Check constraints
                bError = !bCapchaVerified || !validateBean( form, getLocale( request ) );

                if ( !CampaignService.getInstance( ).isDuring( Constants.VOTE ) && StringUtils.isBlank( form.getNickname( ) ) )
                {
                    bError = true;
                    addError( MESSAGE_ERROR_NICKNAME_MISSING, getLocale( request ) );
                }

                // TODO [JPO, 2019-10-12] Desactivated code to make OpenPB working without SuggestPOI
                // if (!bError && !MyInfosService.setAdressValid(form) ){
                // bError = true;
                // addError(MESSAGE_ERROR_ADRESS_GEOLOC_FORMAT, getLocale(request));
                // }

                if ( !bError && ( form.getNickname( ) != null
                        && ( MyInfosService.loadUserNickname( user.getName( ) ) != null
                                && !form.getNickname( ).equals( MyInfosService.loadUserNickname( user.getName( ) ) )
                                || MyInfosService.loadUserNickname( user.getName( ) ) == null )
                        && MyInfosService.isNicknameAlreadyExist( form.getNickname( ) ) ) )
                {
                    bError = true;
                    addError( MESSAGE_ERROR_NICKNAME_ALREADY_EXIST, getLocale( request ) );
                }

                if ( !bError )
                {
                    // update avatar
                    CampagneAvatarService.updateAvatar( request, user.getName( ) );
                    MyInfosService.saveUserEmail( user.getName( ), user.getEmail( ) );

                    if ( !StringUtils.isEmpty( myAccountForm.getLogin( ) ) )
                    {
                        bError = ( !StringUtils.isEmpty( myAccountForm.getPassword( ) ) && !validateBean( myAccountForm, getLocale( request ) ) );

                        if ( !bError && !myAccountForm.getPassword( ).equals( myAccountForm.getConfirmationPassword( ) ) )
                        {
                            // bError = true;
                            // addError(MESSAGE_ERROR_CONFIRMATION_PASSWORD, getLocale(request));

                        }
                        else
                        {

                            // reint account form
                            myAccountForm = null;
                        }
                    }
                }

                if ( bError )
                {
                    _form = form;
                    _accountForm = myAccountForm;
                    String strReferer = request.getHeader( PARAMETER_REFERER );

                    if ( StringUtils.contains( strReferer, "view=" + VIEW_MY_VOTES ) )
                    {
                        redirectView( request, VIEW_MY_VOTES );
                    }

                    Map<String, String> mapParameters = new HashMap<String, String>( );

                    return redirect( request, VIEW_MY_INFOS, mapParameters );
                }
                String strUserOldArrond = MyInfosService.loadUserInfos( user ).getArrondissement( );

                if ( !strUserOldArrond.equals( request.getParameter( MARK_ARRONDISSEMENT ) ) )
                {
                    int nRes = MyInfosListenerService.canChangeArrond( user );
                    if ( nRes == 2 )
                    {
                        SiteMessageService.setMessage( request, PROPERTY_ERROR_END_PHASE, SiteMessage.TYPE_ERROR );
                    }
                    else
                        if ( nRes == 1 )
                        {
                            Map<String, Object> requestParameters = new HashMap<String, Object>( );
                            requestParameters.put( MARK_PAGE, PAGE_MY_INFOS );
                            requestParameters.put( MVCUtils.PARAMETER_ACTION, ACTION_SAVE );
                            requestParameters.put( MARK_EXTENDABLERESOURCETYPE, MARK_TYPE_DOCUMENT );
                            request.getSession( ).setAttribute( MARK_FORM, form );
                            requestParameters.put( MARK_VOTEVALID, MARK_TRUE );
                            SiteMessageService.setMessage( request, PROPERTY_CONFIRM_MODIFY_ARRAND_WITH_VOTES_DELETING, SiteMessage.TYPE_CONFIRMATION,
                                    JSP_PORTAL, requestParameters );

                        }
                        else
                            if ( nRes == 0 )
                            {
                                Map<String, Object> requestParameters = new HashMap<String, Object>( );
                                requestParameters.put( MARK_PAGE, PAGE_MY_INFOS );
                                requestParameters.put( MVCUtils.PARAMETER_ACTION, ACTION_SAVE );
                                requestParameters.put( MARK_EXTENDABLERESOURCETYPE, MARK_TYPE_DOCUMENT );
                                request.getSession( ).setAttribute( MARK_FORM, form );
                                requestParameters.put( MARK_VOTEVALID, MARK_TRUE );
                                SiteMessageService.setMessage( request, PROPERTY_CONFIRM_MODIFY_ARRAND, SiteMessage.TYPE_CONFIRMATION, JSP_PORTAL,
                                        requestParameters );

                            }
                            else
                                if ( nRes == -1 )
                                {
                                    SiteMessageService.setMessage( request, PROPERTY_ERROR_MODIFY_ARRAND, SiteMessage.TYPE_ERROR );
                                }

                }

                updateMyinfos( user, form, request );

                String strReferer = request.getHeader( PARAMETER_REFERER );

                if ( StringUtils.contains( strReferer, "view=" + VIEW_MY_VOTES ) )
                {
                    redirectView( request, VIEW_MY_VOTES );
                }

                return redirectView( request, VIEW_MY_INFOS );

            }
        }

        throw new UserNotSignedException( );
    }

    private void updateMyinfos( LuteceUser user, MyInfosForm form, HttpServletRequest request )
    {
        MyInfosService.saveUserInfos( user.getName( ), form );
        MyInfosService.saveUserEmail( user.getName( ), user.getEmail( ) );

        // update user info into luteceuser
        user.setUserInfo( LuteceUser.NAME_FAMILY, form.getFirstname( ) );
        user.setUserInfo( LuteceUser.NAME_GIVEN, form.getLastname( ) );
        user.setUserInfo( LuteceUser.HOME_INFO_POSTAL_STREET, form.getAddress( ) );
        user.setUserInfo( LuteceUser.BDATE, form.getBirthdate( ) );

        addInfo( INFO_SAVED, getLocale( request ) );

    }

    /**
     * Check if the current user has a valid account
     * 
     * @param request
     *            The request
     * @return A JSON string containing true or false in the field result corresponding to the validity of the user account
     * @throws SiteMessageException
     */
    public String isUserValid( HttpServletRequest request ) throws SiteMessageException
    {
        AbstractJsonResponse jsonResponse = null;

        boolean bIsValid;
        boolean bIsAccountVerified;
        LuteceUser user = null;

        if ( SecurityService.isAuthenticationEnable( ) )
        {
            user = SecurityService.getInstance( ).getRegisteredUser( request );
            if ( user != null )
            {
                {
                    modifyUserEmail( user );

                    bIsValid = MyInfosService.loadUserInfos( user ).getIsValid( );
                    bIsAccountVerified = MyInfosService.isAccountVerified( user );

                    if ( !bIsAccountVerified )
                    {

                        jsonResponse = new ErrorJsonResponse( JSON_ERROR_CODE_ACCOUNT_NOT_VERIFIED );
                    }
                    else
                        if ( !bIsValid )
                        {
                            jsonResponse = new JsonResponse( bIsValid && bIsAccountVerified );
                        }
                        else
                        {
                            jsonResponse = new JsonResponse( bIsAccountVerified );
                        }
                }
            }
        }

        if ( user == null )
        {
            jsonResponse = new ErrorJsonResponse( JSON_ERROR_CODE_USER_NOT_SIGNED );
        }

        return JsonUtil.buildJsonResponse( jsonResponse );
    }

    /**
     * Check if the current user has a valid account
     * 
     * @param request
     *            The request
     * @return A JSON string containing true or false in the field result corresponding to the validity of the user account
     */
    public String doSendValidationMail( HttpServletRequest request )
    {
        AbstractJsonResponse jsonResponse = null;

        return JsonUtil.buildJsonResponse( jsonResponse );
    }

    /**
     * Get the URL to manage infos of the current user
     * 
     * @param strIdResource
     *            The id of the resource
     * @param strResourceType
     *            The resource type
     * @param dVoteValue
     *            The value of the vote
     * @return The URL to manage infos of the current user
     */
    public static String getUrlCompleteMyInfos( String strIdResource, String strResourceType, double dVoteValue )
    {
        UrlItem urlItem = new UrlItem( AppPathService.getPortalUrl( ) );
        urlItem.addParameter( MVCUtils.PARAMETER_PAGE, PAGE_MY_INFOS );
        urlItem.addParameter( MVCUtils.PARAMETER_VIEW, VIEW_MY_INFOS );
        urlItem.addParameter( PARAMETER_COMPLETE_INFOS, Boolean.TRUE.toString( ) );

        return urlItem.getUrl( );
    }

    /**
     * Get the URL to manage infos of the current user
     * 
     * @param strIdResource
     *            The id of the resource
     * @param strResourceType
     *            The resource type
     * @param dVoteValue
     *            The value of the vote
     * @return The URL to manage infos of the current user
     */
    public static String getUrlMyInfos( )
    {
        UrlItem urlItem = new UrlItem( AppPathService.getPortalUrl( ) );
        urlItem.addParameter( MVCUtils.PARAMETER_PAGE, PAGE_MY_INFOS );
        urlItem.addParameter( MVCUtils.PARAMETER_VIEW, VIEW_MY_INFOS );
        urlItem.addParameter( PARAMETER_COMPLETE_INFOS, Boolean.TRUE.toString( ) );
        return urlItem.getUrl( );
    }

    /**
     * Get the HTML content of the popup of the infos of the user
     * 
     * @param request
     *            The request
     * @return The HTML content
     */
    public String getMyInfosPanelForAjax( HttpServletRequest request )
    {
        try
        {
            if ( _form == null )
            {
                addInfo( MESSAGE_INFO_USER_MUST_COMPLETE_PROFILE, request.getLocale( ) );
            }

            return getMyInfos( request, true ).getContent( );
        }
        catch( UserNotSignedException e )
        {
            return StringUtils.EMPTY;
        }
    }

    /**
     * Checked if the user have already set the info
     * 
     * @param request
     *            The request
     * @return The HTML content
     */
    public String getCheckedMyInfosAfterLogin( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );
        model.put( Markers.BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_THEME, ThemesService.getGlobalThemeObject( ) );
        model.put( Markers.PAGE_TITLE, getDefaultPageTitle( request.getLocale( ) ) );
        return getXPage( TEMPLATE_CHECKED_MY_INFOS_AFTER_LOGIN, request.getLocale( ), model ).getContent( );
    }

    /**
     * Do save infos entered by the user. This method should only be called when using the AJAX mode
     * 
     * @param request
     *            The request
     */
    public void doSaveForAjax( HttpServletRequest request )
    {
        boolean bCapchaVerified = true;
        boolean bError = false;
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = (LuteceUser) SecurityService.getInstance( ).getRegisteredUser( request );

            if ( user != null )
            {
                MyInfosForm form = new MyInfosForm( );
                populate( form, request );

                // Check constraints
                bError = !bCapchaVerified || !validateBean( form, getLocale( request ) );

                if ( !CampaignService.getInstance( ).isDuring( Constants.VOTE ) && StringUtils.isBlank( form.getNickname( ) ) )
                {
                    bError = true;
                    addError( MESSAGE_ERROR_NICKNAME_MISSING, getLocale( request ) );
                }

                // if (!bError && !MyInfosService.setAdressValid(form) ){
                // bError = true;
                // addError(MESSAGE_ERROR_ADRESS_GEOLOC_FORMAT, getLocale(request));
                // }

                if ( !bError && ( form.getNickname( ) != null
                        && ( MyInfosService.loadUserNickname( user.getName( ) ) != null
                                && !form.getNickname( ).equals( MyInfosService.loadUserNickname( user.getName( ) ) )
                                || MyInfosService.loadUserNickname( user.getName( ) ) == null )
                        && MyInfosService.isNicknameAlreadyExist( form.getNickname( ) ) ) )
                {
                    bError = true;
                    addError( MESSAGE_ERROR_NICKNAME_ALREADY_EXIST, getLocale( request ) );
                }

                if ( !bError )
                {
                    // update avatar
                    CampagneAvatarService.updateAvatar( request, user.getName( ) );
                }

                // || !SecurityTokenService.getInstance().validate(request,
                // MyInfosXPage.TOKEN_DO_SAVE_MY_INFOS)
                // Check constraints
                if ( bError )
                {
                    _form = form;

                    return;
                }

                MyInfosService.saveUserInfos( user.getName( ), form );
                MyInfosService.saveUserEmail( user.getName( ), user.getEmail( ) );

                // update user info into luteceuser
                user.setUserInfo( LuteceUser.NAME_FAMILY, form.getFirstname( ) );
                user.setUserInfo( LuteceUser.NAME_GIVEN, form.getLastname( ) );
                user.setUserInfo( LuteceUser.HOME_INFO_POSTAL_STREET, form.getAddress( ) );
                user.setUserInfo( LuteceUser.BDATE, form.getBirthdate( ) );
                user.setUserInfo( LuteceUser.HOME_INFO_POSTAL_POSTALCODE, form.getPostalCode( ) );

            }
        }
    }

    /**
     * Modify the user email if it is different from the stored one
     * 
     * @param user
     *            the user
     */
    private void modifyUserEmail( LuteceUser user )
    {
        String strUserMailFromLocalMyInfos = MyInfosService.getUserEmail( user.getName( ) );
        String strUserMailFromConnectedUserInfos = user.getEmail( );

        if ( strUserMailFromConnectedUserInfos != null )
        {
            if ( strUserMailFromLocalMyInfos == null || !strUserMailFromLocalMyInfos.equals( strUserMailFromConnectedUserInfos ) )
            {
                MyInfosService.saveUserEmail( user.getName( ), strUserMailFromConnectedUserInfos );
            }
        }
    }

    /**
     * Populates the specified {@code MyAccount} object with the data of the specified user
     * 
     * @param myAccountForm
     *            the {@code MyAccount} object
     * @param user
     *            the user
     */
    private void populate( MyAccount myAccountForm, LuteceUser user )
    {
        if ( StringUtils.isEmpty( myAccountForm.getLogin( ) ) )
        {
            myAccountForm.setLogin( user.getEmail( ) );
        }
    }

}
