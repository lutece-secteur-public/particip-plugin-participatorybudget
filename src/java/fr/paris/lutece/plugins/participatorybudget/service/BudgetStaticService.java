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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campaign;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class BudgetStaticService extends AbstractCacheableService
{

    private static BudgetStaticService _singleton;

    private static final String BEAN_BUDGET_STATIC_SERVICE = "participatorybudget.budgetStaticService";

    private static final String SERVICE_NAME = "Budget Static Cache";

    private static final String MARK_GLOBAL_STATIC = "global_static";
    private static final String MARK_CAMPAIGN_STATIC = "campaign_static";

    private static final String MARK_CAMPAIGN_LIST = "campaign_list";
    private static final String MARK_CAMPAIGN = "campaign";

    private static final String MARK_THEME_LABEL_LIST = "theme_label_list";
    private static final String MARK_THEME_FRONT_RGB_LIST = "theme_front_rgb_list";

    public static final String CACHE_KEY = "[budgetStatic]";

    // *********************************************************************************************
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // *********************************************************************************************

    public static BudgetStaticService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_BUDGET_STATIC_SERVICE );
        }
        return _singleton;
    }

    public BudgetStaticService( )
    {
        initCache( );
    }

    public String getName( )
    {
        return SERVICE_NAME;
    }

    // *********************************************************************************************
    // * PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLI *
    // * PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLIC PUBLI *
    // *********************************************************************************************

    /**
     * Fill model with static content for a specific campaign.
     */
    public void fillCampaignStaticContent( Map<String, Object> model, String strCampaignCode )
    {
        // Add list of campaigns
        model.put( MARK_CAMPAIGN_LIST, CampaignService.getInstance( ).getCampaigns( ) );

        // Add static data of the specified campaign
        @SuppressWarnings( "unchecked" )
        Map<String, Object> cached = (Map<String, Object>) getFromCache( CACHE_KEY );
        if ( cached == null )
        {
            cached = putAllStaticContentInCache( );
        }
        model.put( MARK_CAMPAIGN_STATIC, cached.get( strCampaignCode ) );
    }

    /**
     * Fill model with static content for all campaigns
     */
    public void fillAllCampaignsStaticContent( Map<String, Object> model )
    {
        // Add list of campaigns
        model.put( MARK_CAMPAIGN_LIST, CampaignService.getInstance( ).getCampaigns( ) );

        // Add static data of all campaigns
        @SuppressWarnings( "unchecked" )
        Map<String, Object> cached = (Map<String, Object>) getFromCache( CACHE_KEY );
        if ( cached == null )
        {
            cached = putAllStaticContentInCache( );
        }
        model.put( MARK_GLOBAL_STATIC, cached );

    }

    // *********************************************************************************************
    // * PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE P *
    // * PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE PRIVATE P *
    // *********************************************************************************************

    /**
     * Returns a map with data of all campaigns :
     * 
     * - content.Key = campaign code - content.Value = a map with data of the campaign : - campaignContent.key = data name (campaign, themes, areas, submitter
     * types, submitter types values) - campaignContent.value = data values
     */
    private Map<String, Object> putAllStaticContentInCache( )
    {
        Map<String, Object> content = new HashMap<String, Object>( );

        Collection<Campaign> campaigns = CampaignService.getInstance( ).getCampaigns( );
        for ( Campaign campaign : campaigns )
        {
            Map<String, Object> campaignContent = new HashMap<String, Object>( );

            // Campaign
            campaignContent.put( MARK_CAMPAIGN, campaign );

            // Add themes of the campaign
            campaignContent.put( MARK_THEME_LABEL_LIST, CampaignService.getInstance( ).getThemes( campaign.getCode( ) ) );
            campaignContent.put( MARK_THEME_FRONT_RGB_LIST, CampaignService.getInstance( ).getThemesFrontRgb( campaign.getCode( ) ) );

            content.put( campaign.getCode( ), campaignContent );
        }

        putInCache( CACHE_KEY, content );

        return content;
    }

}
