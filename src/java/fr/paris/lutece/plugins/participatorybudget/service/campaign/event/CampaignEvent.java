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
package fr.paris.lutece.plugins.participatorybudget.service.campaign.event;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;

/**
 * DocumentEvent are sent by CampaignService to notify campaigns changes to listeners.
 */
public class CampaignEvent
{
    public static final String CAMPAIGN_CREATED = "CAMPAIGN_CREATED";
    public static final String CAMPAIGN_DELETED = "CAMPAIGN_DELETED";
    public static final String CAMPAIGN_CLONED = "CAMPAIGN_CLONED";

    public static final String CAMPAIGN_CODE_MODIFICATION_AUTHORISATION = "CAMPAIGN_CODE_MODIFICATION_AUTHORISATION"; // Want to modify campaign code
    public static final String CAMPAIGN_CODE_MODIFIED = "CAMPAIGN_CODE_MODIFIED";

    // Variables declarations
    private Campagne _mainCampaign;
    private Campagne _linkedCampaign;
    private String _strType;

    /**
     * Creates a new instance of CampaignEvent
     * 
     * @param mainCampaign
     *            The main campaign (the one the event is about)
     * @param linkedCampaign
     *            A linked campaign (for example, a cloned one)
     * @param nType
     *            The type of event
     */
    public CampaignEvent( Campagne mainCampaign, Campagne linkedCampaign, String strType )
    {
        _mainCampaign = mainCampaign;
        _linkedCampaign = linkedCampaign;
        _strType = strType;
    }

    /**
     * Returns the EventType
     *
     * @return The EventType
     */
    public String getEventType( )
    {
        return _strType;
    }

    /**
     * Returns the main campaign (the one the event is about)
     *
     * @return The main campaign
     */
    public Campagne getMainCampaign( )
    {
        return _mainCampaign;
    }

    /**
     * Returns the linked campaign (for example, a cloned one)
     *
     * @return The linked campaign
     */
    public Campagne getLinkedCampaign( )
    {
        return _linkedCampaign;
    }
}
