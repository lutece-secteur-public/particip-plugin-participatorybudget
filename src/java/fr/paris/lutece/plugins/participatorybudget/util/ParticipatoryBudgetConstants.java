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

import org.apache.commons.lang.NotImplementedException;

/**
 * This class provides constants
 *
 */
public final class ParticipatoryBudgetConstants
{
    public static final String PLUGIN_NAME = "participatorybudget";
    public static final String PROJECT_TITLE = "title";
    public static final String PROJECT_LOCATION = "location";
    public static final String PROJECT_THEME = "theme";

    public static final String LOCATION_WHOLE_CITY = "whole_city";

    public static final String NOT_POPULAR_AREA_VALUE = "no";

    // Properties
    public static final String PROPERTY_AUTHORIZED_CITIES = "participatorybudget.authorized.cities";

    // Other constants
    public static final String AUTHORIZED_CITIES_SEPARATOR = ",";

    // CampaignPhase
    public static final String PRE_IDEATION = "PRE_IDEATION";
    public static final String IDEATION = "IDEATION";
    public static final String POST_IDEATION = "POST_IDEATION";
    public static final String CO_CONSTRUCTION = "CO_CONSTRUCTION";
    public static final String PRE_SUBMIT = "PRE_SUBMIT";
    public static final String SUBMIT = "SUBMIT";
    public static final String PRE_VOTE = "PRE_VOTE";
    public static final String VOTE = "VOTE";
    public static final String POST_VOTE = "POST_VOTE";
    public static final String PRE_RESULT = "PRE_RESULT";
    public static final String RESULT = "RESULT";

    public static final String BEGINNING_DATETIME = "BEGINNING_DATETIME";
    public static final String END_DATETIME = "END_DATETIME";

    // Document constants

    // TODO : Put this space id in property file
    public static final int DOCUMENT_PROJECT_PARENT_SPACE_ID = 2;

    public static final String DOCUMENT_TYPE_PROJECT = "pb_project";

    public static final String DOCUMENT_ATTRIBUTE_ADDRESS = "address";
    public static final String DOCUMENT_ATTRIBUTE_ADDRESS_GEOLOC = "address_geoloc";
    public static final String DOCUMENT_ATTRIBUTE_CAMPAIGN = "campaign";
    public static final String DOCUMENT_ATTRIBUTE_CONTENT = "content";
    public static final String DOCUMENT_ATTRIBUTE_DEPARTMENT = "department";
    public static final String DOCUMENT_ATTRIBUTE_DESCRIPTION = "description";
    public static final String DOCUMENT_ATTRIBUTE_LOCATION = "location";
    public static final String DOCUMENT_ATTRIBUTE_IMAGE = "image";

    public static final String DOCUMENT_ATTRIBUTE_PHASE_1_START_DATE = "phase1_start_date";
    public static final String DOCUMENT_ATTRIBUTE_PHASE_2_START_DATE = "phase2_start_date";
    public static final String DOCUMENT_ATTRIBUTE_PHASE_3_START_DATE = "phase3_start_date";
    public static final String DOCUMENT_ATTRIBUTE_PHASE_4_END_DATE = "phase4_end_date";
    public static final String DOCUMENT_ATTRIBUTE_PHASE_4_START_DATE = "phase4_start_date";

    public static final String DOCUMENT_ATTRIBUTE_PROPOSAL_ID = "proposal_id";
    public static final String DOCUMENT_ATTRIBUTE_PROPOSAL_NICKNAMES = "proposal_nicknames";
    public static final String DOCUMENT_ATTRIBUTE_PROPOSAL_SUBTITLE = "proposal_subtitle";
    public static final String DOCUMENT_ATTRIBUTE_PROPOSAL_TITLE = "proposal_title";
    public static final String DOCUMENT_ATTRIBUTE_PROPOSAL_URL = "proposal_url";

    public static final String DOCUMENT_ATTRIBUTE_STATUS = "status";

    public static final String DOCUMENT_ATTRIBUTE_STEP_1_START_DATE = "step1_start_date";
    public static final String DOCUMENT_ATTRIBUTE_STEP_2_START_DATE = "step2_start_date";
    public static final String DOCUMENT_ATTRIBUTE_STEP_3_START_DATE = "step3_start_date";
    public static final String DOCUMENT_ATTRIBUTE_STEP_4_START_DATE = "step4_start_date";
    public static final String DOCUMENT_ATTRIBUTE_STEP_5_START_DATE = "step5_start_date";

    public static final String DOCUMENT_ATTRIBUTE_THEME = "theme";
    public static final String DOCUMENT_ATTRIBUTE_TOTAL_VOTES = "total_votes";
    public static final String DOCUMENT_ATTRIBUTE_VALUE = "value";
    public static final String DOCUMENT_ATTRIBUTE_POPULAR_AREA = "popular_area";

    /**
     * Private constructor
     */
    private ParticipatoryBudgetConstants( )
    {
        throw new NotImplementedException( "This class should not be instantiated !" );
    }

}
