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
package fr.paris.lutece.plugins.participatorybudget.service;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import fr.paris.lutece.plugins.leaflet.business.GeolocItem;
import fr.paris.lutece.plugins.participatorybudget.business.Civility;
import fr.paris.lutece.plugins.participatorybudget.business.MyInfosForm;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.plugins.participatorybudget.util.ParticipatoryBudgetConstants;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.prefs.IPortalUserPreferencesService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

/**
 * MyInfos Service
 */
public final class MyInfosService
{

    private static final java.util.regex.Pattern patternPostalCode = java.util.regex.Pattern.compile( ", (75[0-1][0-2][0-9]) PARIS" );
    public static final String UNDEFINED_POSTALCODE_KEY = "undefined";
    public static final String LUTECE_USER_KEY_VERIFIED = "user.validated";
    private static final String PREF_KEY_EMAIL = "participatorybudget.email";
    private static final String PREF_KEY_FIRSTNAME = "participatorybudget.firstname";
    private static final String PREF_KEY_CREATIONDATE = "participatorybudget.creationdate";
    private static final String PREF_KEY_CIVILITY = "participatorybudget.civility";
    private static final String PREF_KEY_LASTNAME = "participatorybudget.lastname";
    private static final String PREF_KEY_ADDRESS = "participatorybudget.address";
    private static final String PREF_KEY_GEO_JSON = "participatorybudget.geojson";
    private static final String PREF_KEY_BIRTHDATE = "participatorybudget.birthdate";
    private static final String PREF_KEY_ARRONDISSEMENT_VOTE = "participatorybudget.arrondissement";
    private static final String PREF_KEY_POSTAL_CODE = "participatorybudget.postalCode";
    private static final String PREF_KEY_ILIVEINPARIS = "participatorybudget.iliveinparis";
    // private static final String PREF_KEY_STAYCONNECTED = "participatorybudget.stayconnected";
    private static final String PREF_KEY_SEND_ACCOUNT_VALIDATION = "participatorybudget.sendaccountvalidation";
    private static final String PREF_KEY_ACCOUNT_VERIFIED = "participatorybudget.accountVerified";
    private static final String PROPERTY_ARRONDISSEMENT = "participatorybudget.arrondissement.";
    private static final String PROPERTY_SELECT_ARRONDISSEMENT = "participatorybudget.mes_infos.arrondissement";
    private static final String PROPERTY_SELECT_POSTAL_CODE = "participatorybudget.mes_infos.postalCode";
    private static final String PROPERTY_SELECT_POSTAL_CODE_16_KEY = "participatorybudget.mes_infos.postalCode16Key";
    private static final String PROPERTY_SELECT_POSTAL_CODE_16_LABEL = "participatorybudget.mes_infos.postalCode16Label";
    private static final String PREF_VALUE_ON = "on";
    private static final String PREF_VALUE_OFF = "off";
    private static final String DEFAULT_CHECKED = "default";
    private static final String ADDRESS_SEPARATOR = " ";

    private static final String KEY_SEND_ACCOUNT_VALIDATION_BACK_URL = "participatorybudget.site_property.send_account_validation_back_url";
    private static final String KEY_ENABLE_VALIDATION_ACCOUNT = "participatorybudget.site_property.enable_validation_account";

    private static final String TYPE_VALIDATE_ACCOUNT = "validate_account";
    private static volatile ReferenceList _listArrondissements;
    private static volatile ReferenceList _listPostalCode;
    private static List<String> _listAuthorizedCities = Arrays
            .asList( AppPropertiesService.getProperty( ParticipatoryBudgetConstants.PROPERTY_AUTHORIZED_CITIES ).split( ParticipatoryBudgetConstants.AUTHORIZED_CITIES_SEPARATOR ) );

    /** Private constructor */
    private MyInfosService( )
    {
    }

    /**
     * Load informations about the user from ParisConnect and local preferences
     * 
     * @param user
     *            The User
     * @return User's infos
     */
    public static MyInfosForm loadUserInfos( LuteceUser user )
    {
        MyInfosForm myInfos = new MyInfosForm( );

        IPortalUserPreferencesService instance = UserPreferencesService.instance( );

        // Initialized to true, but can be changed in populate methods
        myInfos.setIsValid( true );

        // Populate the MyInfosForm object
        populateMyInfosData( myInfos, instance, user );

        return myInfos;
    }

    /**
     * Populate the MyInfosForm with the user data
     * 
     * @param myInfos
     *            The instance of MyInfosForm
     * @param instance
     *            The IPortalUserPreferencesService instance
     * @param user
     *            The user
     */
    private static void populateMyInfosData( MyInfosForm myInfos, IPortalUserPreferencesService instance, LuteceUser user )
    {
        populateNickname( myInfos, instance, user );
        populateCivility( myInfos, instance, user );
        populateFirstName( myInfos, instance, user );
        populateLastName( myInfos, instance, user );
        populateBirthDate( myInfos, instance, user );
        populateAddress( myInfos, instance, user );
        populatePostalCode( myInfos, instance, user );
        populateArrondissement( myInfos, instance, user );
        populateGeoJson( myInfos, instance, user );
        populateILiveInParisFlag( myInfos, instance, user );
        populateSendAccountValidationFlag( myInfos, instance, user );
        populateAccountVerifiedFlag( myInfos, instance, user );

    }

    /**
     * load UserNickName
     * 
     * @param strLuteceUserName
     *            strLuteceUserName
     * @return UserNickName
     */
    public static String loadUserNickname( String strLuteceUserName )
    {

        return UserPreferencesService.instance( ).getNickname( strLuteceUserName );

    }

    /**
     * load UserNickName
     * 
     * @param strLuteceUserName
     *            strLuteceUserName
     * @return UserNickName
     */
    public static boolean isNicknameAlreadyExist( String strNickName )
    {

        return !StringUtils.isEmpty( strNickName ) && UserPreferencesService.instance( ).existsNickname( strNickName.trim( ) );

    }

    /**
     * return true if the user is verified
     * 
     * @param user
     *            The User
     * @return true if the user is verified
     */
    public static boolean isAccountVerified( LuteceUser user )
    {

        String bEnableValidationAccount = DatastoreService.getDataValue( KEY_ENABLE_VALIDATION_ACCOUNT, "false" );

        if ( new Boolean( bEnableValidationAccount ) )
        {

            // test if the user have verified his account
            boolean bpVerified = UserPreferencesService.instance( ).getBoolean( user.getName( ), PREF_KEY_ACCOUNT_VERIFIED, false );
            boolean openamVerified = user.getUserInfo( LUTECE_USER_KEY_VERIFIED ) != null ? new Boolean( user.getUserInfo( LUTECE_USER_KEY_VERIFIED ) ) : false;

            return openamVerified || bpVerified;
        }

        return true;
    }

    /**
     * Save Users info into ParisConnect and local preferences
     * 
     * @param user
     *            The User
     * @param myInfos
     *            The infos
     */
    public static void saveUserInfos( String strLuteceUserName, MyInfosForm myInfos )
    {
        IPortalUserPreferencesService instance = UserPreferencesService.instance( );

        // instance.clear( user.getName( ) );
        String strCreationDate = instance.get( strLuteceUserName, PREF_KEY_CREATIONDATE, StringUtils.EMPTY );

        if ( strCreationDate == null || strCreationDate.isEmpty( ) )
        {
            Date dCreationDate = new Date( Calendar.getInstance( ).getTime( ).getTime( ) );
            instance.put( strLuteceUserName, PREF_KEY_CREATIONDATE, dCreationDate.toString( ) );
        }

        instance.put( strLuteceUserName, PREF_KEY_FIRSTNAME, myInfos.getFirstname( ) );
        instance.put( strLuteceUserName, PREF_KEY_LASTNAME, myInfos.getLastname( ) );
        instance.put( strLuteceUserName, PREF_KEY_ADDRESS, myInfos.getAddress( ) );
        instance.put( strLuteceUserName, PREF_KEY_BIRTHDATE, myInfos.getBirthdate( ) );
        instance.put( strLuteceUserName, PREF_KEY_ILIVEINPARIS, myInfos.getIliveinparis( ) );
        instance.put( strLuteceUserName, PREF_KEY_CIVILITY, myInfos.getCivility( ) );
        instance.put( strLuteceUserName, PREF_KEY_POSTAL_CODE, myInfos.getPostalCode( ) );
        instance.put( strLuteceUserName, PREF_KEY_ARRONDISSEMENT_VOTE, myInfos.getArrondissement( ) );

        instance.put( strLuteceUserName, PREF_KEY_SEND_ACCOUNT_VALIDATION, myInfos.getSendaccountvalidation( ) );

        instance.put( strLuteceUserName, PREF_KEY_GEO_JSON, myInfos.getGeojson( ) );

        // subscribe alert
        myInfos.setIsValid( isUserValid( myInfos ) );

        if ( !StringUtils.isEmpty( myInfos.getNickname( ) ) && !StringUtils.isEmpty( instance.getNickname( strLuteceUserName ) )
                && !myInfos.getNickname( ).equals( instance.getNickname( strLuteceUserName ) ) )
        {
            instance.setNickname( strLuteceUserName, myInfos.getNickname( ) );
            MyInfosListenerService.updateNickName( strLuteceUserName, myInfos.getNickname( ) );

        }
        else
            if ( StringUtils.isEmpty( instance.getNickname( strLuteceUserName ) ) )
            {

                MyInfosListenerService.createNickName( strLuteceUserName, myInfos.getNickname( ) );
            }
        instance.setNickname( strLuteceUserName, myInfos.getNickname( ) );

    }

    /**
     * Save Users email into localPreferences
     * 
     * @param user
     *            The User
     * @param strEmail
     *            The email
     */
    public static void saveUserEmail( String strLuteceUserName, String strEmail )
    {
        IPortalUserPreferencesService instance = UserPreferencesService.instance( );
        instance.put( strLuteceUserName, PREF_KEY_EMAIL, strEmail );
    }

    /**
     * Gives the user email
     * 
     * @param user
     *            the user
     * @return the user email
     */
    public static String getUserEmail( String strLuteceUserName )
    {
        return UserPreferencesService.instance( ).get( strLuteceUserName, PREF_KEY_EMAIL, StringUtils.EMPTY );
    }

    /**
     * check if a validation mail must be send to the user
     * 
     * @param user
     *            the user
     * @param myInfos
     *            the user information
     * @return
     */
    public static boolean mustSendAccountValidationMail( String strLuteceUserName, MyInfosForm myInfos )
    {

        String bEnableValidationAccount = DatastoreService.getDataValue( KEY_ENABLE_VALIDATION_ACCOUNT, "false" );

        return new Boolean( bEnableValidationAccount ) && StringUtils.isNotEmpty( myInfos.getSendaccountvalidation( ) );
    }

    /**
     * Send account validation mail if the user never have validation mail
     * 
     * @param user
     *            the user
     */

    /**
     * Provides the list of arrondissements
     * 
     * @return The list
     */
    // TODO Area concept should be generalized and configurable in back-office
    public static ReferenceList getArrondissements( )
    {
        if ( _listArrondissements == null )
        {
            synchronized( MyInfosService.class )
            {
                if ( _listArrondissements == null )
                {
                    _listArrondissements = new ReferenceList( );
                    _listArrondissements.addItem( UNDEFINED_POSTALCODE_KEY, I18nService.getLocalizedString( PROPERTY_SELECT_ARRONDISSEMENT, Locale.FRENCH ) );

                    _listArrondissements.addItem( "area_1", "Area 1" );
                    _listArrondissements.addItem( "area_2", "Area 2" );
                    _listArrondissements.addItem( "area_3", "Area 3" );
                    _listArrondissements.addItem( "whole_city", "Whole city" );

                    // for (int i = 0; i < 20; i++) {
                    // String strCode = "" + (75001 + i);
                    // _listArrondissements.addItem(strCode, I18nService
                    // .getLocalizedString(PROPERTY_ARRONDISSEMENT
                    // + strCode, Locale.FRENCH));
                    // }
                }
            }
        }

        return _listArrondissements;
    }

    /**
     * Provides the list of arrondissements
     * 
     * @return The list
     */
    public static ReferenceList getPostalCodes( )
    {
        if ( _listPostalCode == null )
        {
            synchronized( MyInfosService.class )
            {
                if ( _listPostalCode == null )
                {
                    _listPostalCode = new ReferenceList( );
                    _listPostalCode.addItem( UNDEFINED_POSTALCODE_KEY, I18nService.getLocalizedString( PROPERTY_SELECT_POSTAL_CODE, Locale.FRENCH ) );

                    for ( int i = 0; i < 20; i++ )
                    {
                        String strCode = "" + ( 75001 + i );
                        _listPostalCode.addItem( strCode, strCode );
                        if ( i == 15 )
                        {
                            _listPostalCode.addItem( I18nService.getLocalizedString( PROPERTY_SELECT_POSTAL_CODE_16_KEY, Locale.FRENCH ),
                                    I18nService.getLocalizedString( PROPERTY_SELECT_POSTAL_CODE_16_LABEL, Locale.FRENCH ) );
                        }
                    }
                }
            }
        }

        return _listPostalCode;
    }

    /**
     * Calculate age
     * 
     * @param strBirthDate
     *            The birth date as a String
     * @return The age
     * @throws ParseException
     *             if parsing failed
     */
    public static int getAge( String strBirthDate ) throws ParseException
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern( "dd/MM/yyyy" );
        DateTime dateBirthdate = formatter.parseDateTime( strBirthDate );
        Years years = Years.yearsBetween( new LocalDate( dateBirthdate ), new LocalDate( ) );

        return years.getYears( );
    }

    public static boolean setAdressValid( MyInfosForm myInfos )
    {

        GeolocItem geolocItem = null;
        try
        {
            geolocItem = GeolocItem.fromJSON( myInfos.getGeojson( ) );

            Matcher m = patternPostalCode.matcher( geolocItem.getAddress( ) );
            m.find( );

            String strPostalCode = m.group( 1 );

            if ( !StringUtils.isEmpty( strPostalCode ) )
            {
                myInfos.setLatitude( geolocItem.getLat( ) );
                myInfos.setLongitude( geolocItem.getLon( ) );
                myInfos.setAddress( geolocItem.getAddress( ) );
                myInfos.setPostalCode( strPostalCode );
                return true;
            }

        }
        catch( IOException e )
        {

            AppLogService.error( "MyInfosApp: malformed data from client: address = " + myInfos.getGeojson( ) + "; exeception " + e );
        }

        return false;

    }

    /**
     * Check if a user is valid or not
     * 
     * @param myInfos
     *            True if the user is valid, valse otherwise
     * @return
     */
    private static boolean isUserValid( MyInfosForm myInfos )
    {
        return StringUtils.isNotBlank( myInfos.getCivility( ) )
                // && StringUtils.isNotBlank(myInfos.getNickname())
                && StringUtils.isNotBlank( myInfos.getAddress( ) ) && StringUtils.isNotBlank( myInfos.getPostalCode( ) )
                && StringUtils.isNotBlank( myInfos.getArrondissement( ) ) && !UNDEFINED_POSTALCODE_KEY.equals( myInfos.getArrondissement( ) )
                && StringUtils.isNotBlank( myInfos.getBirthdate( ) )
                && ( StringUtils.isNotBlank( myInfos.getIliveinparis( ) ) && myInfos.getIliveinparis( ).equals( PREF_VALUE_ON ) );
    }

    /**
     * Populates the nickname into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populateNickname( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        String strNickname = userPreferenceService.getNickname( user );

        if ( !CampaignService.getInstance( ).isDuring( ParticipatoryBudgetConstants.VOTE ) && StringUtils.isBlank( strNickname ) )
        {
            myInfos.setIsValid( false );
        }

        myInfos.setNickname( strNickname );
    }

    /**
     * Populates the civility into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populateCivility( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        String strCivility = userPreferenceService.get( user.getName( ), PREF_KEY_CIVILITY, null );

        if ( strCivility == null )
        {
            myInfos.setIsValid( false );

            strCivility = Civility.fromNumericCode( user.getUserInfo( LuteceUser.GENDER ) ).getLabelCode( );
        }

        myInfos.setCivility( strCivility );
    }

    /**
     * Populates the first name into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populateFirstName( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        String strFirstName = userPreferenceService.get( user.getName( ), PREF_KEY_FIRSTNAME, null );

        myInfos.setFirstname( strFirstName != null ? strFirstName : user.getUserInfo( LuteceUser.NAME_GIVEN ) );
    }

    /**
     * Populates the last name into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populateLastName( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        String strLastName = userPreferenceService.get( user.getName( ), PREF_KEY_LASTNAME, null );

        if ( strLastName == null )
        {
            strLastName = user.getUserInfo( LuteceUser.NAME_FAMILY );

            if ( StringUtils.isEmpty( strLastName ) )
            {
                strLastName = user.getUserInfo( LuteceUser.NAME_CIVILITY );
            }
        }

        myInfos.setLastname( strLastName );
    }

    /**
     * Populates the birth date into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populateBirthDate( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        String strBirthDate = userPreferenceService.get( user.getName( ), PREF_KEY_BIRTHDATE, null );

        if ( strBirthDate == null )
        {
            myInfos.setIsValid( false );

            strBirthDate = user.getUserInfo( LuteceUser.BDATE );
        }

        myInfos.setBirthdate( strBirthDate );
    }

    /**
     * Populates the address into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populateAddress( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        String strAddress = userPreferenceService.get( user.getName( ), PREF_KEY_ADDRESS, null );

        if ( strAddress == null )
        {
            // TODO [JPO, 2019-10-12] Desactivated code to make OpenPB working without SuggestPOI
            // myInfos.setIsValid( false );

            String strCity = user.getUserInfo( LuteceUser.HOME_INFO_POSTAL_CITY );

            if ( _listAuthorizedCities.contains( strCity.trim( ).toLowerCase( ) ) )
            {
                strAddress = user.getUserInfo( LuteceUser.HOME_INFO_POSTAL_STREET ) + ADDRESS_SEPARATOR
                        + user.getUserInfo( LuteceUser.HOME_INFO_POSTAL_POSTALCODE ) + ADDRESS_SEPARATOR + strCity;
            }
        }

        myInfos.setAddress( strAddress );
    }

    /**
     * Populates the postal code into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populatePostalCode( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        String strPostalCode = userPreferenceService.get( user.getName( ), PREF_KEY_POSTAL_CODE, StringUtils.EMPTY );

        if ( StringUtils.isBlank( strPostalCode ) )
        {
            // TODO [JPO, 2019-10-12] Desactivated code to make OpenPB working without SuggestPOI
            // myInfos.setIsValid( false );
        }

        myInfos.setPostalCode( strPostalCode );
    }

    /**
     * Populates the arrondissement into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populateArrondissement( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        String strArrondissement = userPreferenceService.get( user.getName( ), PREF_KEY_ARRONDISSEMENT_VOTE, StringUtils.EMPTY );

        if ( CampaignService.getInstance( ).isAfterBeginning( ParticipatoryBudgetConstants.SUBMIT ) && CampaignService.getInstance( ).isBeforeEnd( ParticipatoryBudgetConstants.VOTE )
                && ( StringUtils.isBlank( strArrondissement ) || UNDEFINED_POSTALCODE_KEY.equals( strArrondissement ) ) )
        {
            myInfos.setIsValid( false );
        }

        myInfos.setArrondissement( strArrondissement );
    }

    /**
     * Populates the geo json into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populateGeoJson( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        String strGeoJson = userPreferenceService.get( user.getName( ), PREF_KEY_GEO_JSON, StringUtils.EMPTY );

        if ( StringUtils.isBlank( strGeoJson ) )
        {
            // TODO [JPO, 2019-10-12] Desactivated code to make OpenPB working without SuggestPOI
            // myInfos.setIsValid( false );
        }

        myInfos.setGeojson( strGeoJson );
    }

    /**
     * Populates the flag ILiveInParis into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populateILiveInParisFlag( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        if ( userPreferenceService.existsKey( user.getName( ), PREF_KEY_ILIVEINPARIS ) )
        {
            String strILiveInParis = userPreferenceService.get( user.getName( ), PREF_KEY_ILIVEINPARIS, PREF_VALUE_OFF );

            if ( PREF_VALUE_OFF.equals( strILiveInParis ) )
            {
                myInfos.setIsValid( false );
            }

            myInfos.setIliveinparis( strILiveInParis );
        }
        else
        {
            myInfos.setIsValid( false );

            myInfos.setIliveinparis( DEFAULT_CHECKED );
        }
    }

    /**
     * Populates the send account validation flag into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populateSendAccountValidationFlag( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        if ( userPreferenceService.existsKey( user.getName( ), PREF_KEY_SEND_ACCOUNT_VALIDATION ) )
        {
            myInfos.setSendaccountvalidation( userPreferenceService.get( user.getName( ), PREF_KEY_SEND_ACCOUNT_VALIDATION, PREF_VALUE_OFF ) );
        }
        else
        {
            myInfos.setSendaccountvalidation( DEFAULT_CHECKED );
        }
    }

    /**
     * Populates the account verified validation flag into the specified information form.
     * 
     * @param myInfos
     *            the information form
     * @param userPreferenceService
     *            the service containing the user preferences
     * @param user
     *            the connected user
     */
    private static void populateAccountVerifiedFlag( MyInfosForm myInfos, IPortalUserPreferencesService userPreferenceService, LuteceUser user )
    {
        myInfos.setAccountVerified( userPreferenceService.getBoolean( user.getName( ), PREF_KEY_ACCOUNT_VERIFIED, false ) );
    }

}
