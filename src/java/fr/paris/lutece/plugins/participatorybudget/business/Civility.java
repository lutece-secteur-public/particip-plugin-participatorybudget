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
package fr.paris.lutece.plugins.participatorybudget.business;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 *
 * this enum represents a civility
 */
public enum Civility
{
    DEFAULT( "0", "NPR" ),
    MRS( "2", "M" ),
    MS( "1", "MME" );

    private static Map<String, Civility> _mapNumericCivility = new HashMap<>( );
    private static Map<String, Civility> _mapLabelCivility = new HashMap<>( );

    static
    {
        for ( Civility enumCivility : EnumSet.allOf( Civility.class ) )
        {
            _mapNumericCivility.put( enumCivility._strNumericCode, enumCivility );
            _mapLabelCivility.put( enumCivility._strLabelCode, enumCivility );
        }
    }

    private String _strNumericCode;
    private String _strLabelCode;

    /**
     * Constructor
     * 
     * @param strNumericCode
     *            the numeric code
     * @param strLabelCode
     *            the label code
     */
    Civility( String strNumericCode, String strLabelCode )
    {
        _strNumericCode = strNumericCode;
        _strLabelCode = strLabelCode;
    }

    /**
     * Gives the numeric code
     * 
     * @return the code
     */
    public String getNumericCode( )
    {
        return _strNumericCode;
    }

    /**
     * Gives the label code
     * 
     * @return the code
     */
    public String getLabelCode( )
    {
        return _strLabelCode;
    }

    /**
     * Retrieves the Civility from the specified numeric code.
     * 
     * @param strNumericCode
     *            the code
     * @return the Civility
     */
    public static Civility fromNumericCode( String strNumericCode )
    {
        return fromCode( strNumericCode, _mapNumericCivility );
    }

    /**
     * Retrieves the Civility from the specified numeric code.
     * 
     * @param strLabelCode
     *            the code
     * @return the Civility
     */
    public static Civility fromLabelCode( String strLabelCode )
    {
        return fromCode( strLabelCode, _mapLabelCivility );
    }

    /**
     * Retrieves the Civility from the specified code in the specified map.
     * 
     * @param strCode
     *            the code
     * @param map
     *            the map
     * @return the Civility
     */
    private static Civility fromCode( String strCode, Map<String, Civility> map )
    {
        Civility civility = DEFAULT;

        if ( !StringUtils.isEmpty( strCode ) )
        {
            civility = map.get( strCode );

            if ( civility == null )
            {
                civility = DEFAULT;
            }
        }

        return civility;
    }
}
