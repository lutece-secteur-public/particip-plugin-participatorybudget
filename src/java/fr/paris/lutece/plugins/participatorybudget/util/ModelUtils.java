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
package fr.paris.lutece.plugins.participatorybudget.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * Class providing utility methods for for Model
 *
 */
public final class ModelUtils
{
    // Markers
    private static final String MARKER_AUTHORIZED_ADDRESS = "authorized_address";

    private static List<String> _listAuthorizedCities = Arrays.asList( AppPropertiesService
            .getProperty( ParticipatoryBudgetConstants.PROPERTY_AUTHORIZED_CITIES ).split( ParticipatoryBudgetConstants.AUTHORIZED_CITIES_SEPARATOR ) );

    /**
     * Default constructor
     */
    private ModelUtils( )
    {
    }

    /**
     * Stores into the specified model the flag to know if the user address is authorized or not
     * 
     * @param model
     *            the model the fill
     * @param strAddress
     *            the user address
     * @param user
     *            the user
     */
    public static void storeUnauthorizedAddress( Map<String, Object> model, String strAddress, LuteceUser user )
    {
        boolean bIsAuthorized = true;

        if ( StringUtils.isEmpty( strAddress ) )
        {
            String strCity = user.getUserInfo( LuteceUser.HOME_INFO_POSTAL_CITY );

            if ( !StringUtils.isEmpty( strCity ) && !_listAuthorizedCities.contains( strCity.trim( ).toLowerCase( ) ) )
            {
                bIsAuthorized = false;
            }
        }

        model.put( MARKER_AUTHORIZED_ADDRESS, bIsAuthorized );
    }

}
