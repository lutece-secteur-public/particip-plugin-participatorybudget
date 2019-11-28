/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

/**
 * This class provides constants
 *
 */
public final class Constants
{
    // Properties
    public static final String PROPERTY_AUTHORIZED_CITIES = "participatorybudget.authorized.cities";
    
    // Other constants
    public static final String AUTHORIZED_CITIES_SEPARATOR = ",";
    
    // CampagnePhase
	
	public static final String    PRE_IDEATION =    "PRE_IDEATION";
    public static final String        IDEATION =        "IDEATION";
    public static final String   POST_IDEATION =   "POST_IDEATION";
    public static final String CO_CONSTRUCTION = "CO_CONSTRUCTION";
    public static final String      PRE_SUBMIT =      "PRE_SUBMIT";
    public static final String          SUBMIT =          "SUBMIT";
    public static final String        PRE_VOTE =        "PRE_VOTE";
    public static final String            VOTE =            "VOTE";
    public static final String       POST_VOTE =       "POST_VOTE";
    public static final String      PRE_RESULT =      "PRE_RESULT";
    public static final String          RESULT =          "RESULT";

    public static final String BEGINNING_DATETIME = "BEGINNING_DATETIME";
    public static final String       END_DATETIME =       "END_DATETIME";

    /**
     * Private constructor
     */
    private Constants(  )
    {
    }
    
}
