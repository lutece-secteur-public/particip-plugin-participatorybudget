/*
 * Copyright (c) 2002-2020, Mairie de Paris
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
package fr.paris.lutece.plugins.participatorybudget.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.paris.lutece.plugins.participatorybudget.business.MyInfosForm;
import fr.paris.lutece.plugins.participatorybudget.service.MyInfosService;
import fr.paris.lutece.portal.service.prefs.IPortalUserPreferencesService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.security.LuteceUser;

public class BudgetUtils
{

    private static final String PREF_KEY_ARRONDISSEMENT_VOTE = "participatorybudget.arrondissement";

    public static final String ARRONDISSEMENT_USER = "arrondUser";

    public static final String MARK_VOTE_VALIDATED = "voteValidated";
    public static final String MARK_CAMPAGNE_SERVICE = "campagneService";

    /**
     * Get arrondissement for display
     * 
     * @param user
     * @return string for arrondissement display
     */
    public static String getArrondissementDisplay( LuteceUser user )
    {
        MyInfosForm myInfo = MyInfosService.loadUserInfos( user );
        return myInfo.getArrondissement( );
    }

    /**
     * Get arrondissement from user preference
     * 
     * @param strLuteceUserName
     * @return arrondissement from user preference
     */
    public static String getArrondissement( String strLuteceUserName )
    {
        IPortalUserPreferencesService instance = UserPreferencesService.instance( );
        return instance.get( strLuteceUserName, PREF_KEY_ARRONDISSEMENT_VOTE, "" );
    }

    /**
     * Set arrondissement
     * 
     * @param strLuteceUserName
     * @param arrondissement
     * @return
     */
    public static void setArrondissement( String strLuteceUserName, String arrondissement )
    {
        IPortalUserPreferencesService instance = UserPreferencesService.instance( );
        instance.put( strLuteceUserName, PREF_KEY_ARRONDISSEMENT_VOTE, arrondissement );
    }
}
