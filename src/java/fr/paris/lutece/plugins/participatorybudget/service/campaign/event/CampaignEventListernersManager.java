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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 *
 */
public class CampaignEventListernersManager
{
    private static final String BEAN_EVENT_LISTENER_MANAGER = "participatorybudget.campaignEventListernersManager";

    private static List<CampaignEventListener> _listEventListeners = new ArrayList<>( );

    // ***********************************************************************************
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // ***********************************************************************************

    private static CampaignEventListernersManager _singleton;

    public static CampaignEventListernersManager getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_EVENT_LISTENER_MANAGER );
            _singleton.setListeners( SpringContextService.getBeansOfType( CampaignEventListener.class ) );
        }
        return _singleton;
    }

    // ***********************************************************************************
    // * EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EV *
    // * EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EV *
    // ***********************************************************************************

    /**
     * Add the a listener
     * 
     * @param listener
     *            The Listener to add
     */
    public void addListener( CampaignEventListener listener )
    {
        _listEventListeners.add( listener );

        AppLogService.info( "New Campaign Event Listener registered : " + listener.getClass( ).getName( ) );
    }

    /**
     * Sets the listeners list
     * 
     * @param listEventListeners
     *            The Listeners list
     */
    public void setListeners( List<CampaignEventListener> listEventListeners )
    {
        _listEventListeners = listEventListeners;

        for ( CampaignEventListener listener : _listEventListeners )
        {
            AppLogService.info( "New Campaign Event Listener registered : " + listener.getClass( ).getName( ) );
        }
    }

    /**
     * Notify an event to all listeners
     *
     * @param event
     *            A campaign Event
     * @return Results of listeners, or empty List if no result.
     */
    public List<String> notifyListeners( CampaignEvent event )
    {
        List<String> results = new ArrayList<>( );

        for ( CampaignEventListener listener : _listEventListeners )
        {
            String result = listener.processCampaignEvent( event );
            if ( StringUtils.isNotBlank( result ) )
            {
                results.add( result );
            }
        }

        return results;
    }
}
