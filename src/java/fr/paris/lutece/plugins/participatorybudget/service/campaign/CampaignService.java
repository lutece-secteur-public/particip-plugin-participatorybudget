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
package fr.paris.lutece.plugins.participatorybudget.service.campaign;

import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campaign;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignArea;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignAreaHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignPhase;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignPhaseHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignTheme;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampaignThemeHome;
import fr.paris.lutece.plugins.participatorybudget.service.NoSuchPhaseException;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.event.CampaignEvent;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.event.CampaignEventListernersManager;
import fr.paris.lutece.plugins.participatorybudget.util.Constants;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;

public class CampaignService implements ICampaignService
{

    private static final String BEAN_CAMPAIGN_SERVICE = "participatorybudget.campaignService";

    // Attributes
    private Map<String, Timestamp> _cache = null;

    // ***********************************************************************************
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // ***********************************************************************************

    private static ICampaignService _singleton;

    public static ICampaignService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_CAMPAIGN_SERVICE );
        }
        return _singleton;
    }

    // ***********************************************************************************
    // * CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE C *
    // * CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE C *
    // ***********************************************************************************

    private Map<String, Timestamp> getCache( )
    {
        if ( _cache == null )
            reset( );

        return _cache;
    }

    private String getKey( String campain, String phase, String datetimeType )
    {
        return campain + "-" + phase + "-" + datetimeType;
    }

    public void reset( )
    {
        AppLogService.debug( "CampaignPhase cache reset" );

        Map<String, Timestamp> cache = new HashMap<String, Timestamp>( );

        Collection<CampaignPhase> phases = CampaignPhaseHome.getCampaignPhasesList( );
        for ( CampaignPhase phase : phases )
        {
            String beginningKey = getKey( phase.getCodeCampaign( ), phase.getCodePhaseType( ), Constants.BEGINNING_DATETIME );
            String endKey = getKey( phase.getCodeCampaign( ), phase.getCodePhaseType( ), Constants.END_DATETIME );

            cache.put( beginningKey, phase.getStart( ) );
            cache.put( endKey, phase.getEnd( ) );

            AppLogService.debug(
                    "  -> Added '" + phase.getCodeCampaign( ) + "-" + phase.getCodePhaseType( ) + "' = '" + phase.getStart( ) + "/" + phase.getEnd( ) + "'." );
        }

        _cache = cache;
    }

    // ***********************************************************************************
    // * CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN *
    // * CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN *
    // ***********************************************************************************

    public Campaign getLastCampaign( )
    {
        return CampaignHome.getLastCampaign( );
    }

    // ***********************************************************************************
    // * PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE P *
    // * PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE P *
    // ***********************************************************************************

    private Timestamp getTimestamp( String campain, String phase, String timestampType )
    {
        String key = getKey( campain, phase, timestampType );
        Timestamp timeStamp = getCache( ).get( key );
        if ( timeStamp == null )
        {
            String errorMsg = "Null datetime for campaign '" + campain + "' and phase '" + phase + "' and timestampType '" + timestampType + "'. ";
            AppLogService.error( errorMsg );
            throw new NoSuchPhaseException( errorMsg );
        }
        return timeStamp;
    }

    public boolean isBeforeBeginning( String campain, String phase )
    {
        Timestamp timeStamp = getTimestamp( campain, phase, Constants.BEGINNING_DATETIME );
        Date date = new Date( );
        boolean result = date.before( timeStamp );
        return result;
    }

    public boolean isBeforeEnd( String campain, String phase )
    {
        Timestamp timeStamp = getTimestamp( campain, phase, Constants.END_DATETIME );
        Date date = new Date( );
        boolean result = date.before( timeStamp );
        return result;
    }

    public boolean isDuring( String campain, String phase )
    {
        Timestamp beginningTimeStamp = getTimestamp( campain, phase, Constants.BEGINNING_DATETIME );
        Timestamp endTimeStamp = getTimestamp( campain, phase, Constants.END_DATETIME );

        Date date = new Date( );

        boolean result = date.after( beginningTimeStamp ) && date.before( endTimeStamp );
        return result;
    }

    public boolean isAfterBeginning( String campain, String phase )
    {
        Timestamp timeStamp = getTimestamp( campain, phase, Constants.BEGINNING_DATETIME );
        Date date = new Date( );
        boolean result = date.after( timeStamp );
        return result;
    }

    public boolean isAfterEnd( String campain, String phase )
    {
        Timestamp timeStamp = getTimestamp( campain, phase, Constants.END_DATETIME );
        Date date = new Date( );
        boolean result = date.after( timeStamp );
        return result;
    }

    public Timestamp start( String campain, String phase )
    {
        Timestamp timeStamp = getTimestamp( campain, phase, Constants.BEGINNING_DATETIME );
        return timeStamp;
    }

    public Timestamp end( String campain, String phase )
    {
        Timestamp timeStamp = getTimestamp( campain, phase, Constants.END_DATETIME );
        return timeStamp;
    }

    public String startStr( String campain, String phase, String format, boolean withAccents )
    {
        Timestamp ts = start( campain, phase );
        Date date = new Date( );
        date.setTime( ts.getTime( ) );
        String formattedDate = new SimpleDateFormat( format ).format( date );
        return toSoLovelyString( formattedDate, withAccents );
    }

    public String endStr( String campain, String phase, String format, boolean withAccents )
    {
        Timestamp ts = end( campain, phase );
        Date date = new Date( );
        date.setTime( ts.getTime( ) );
        String formattedDate = new SimpleDateFormat( format ).format( date );
        return toSoLovelyString( formattedDate, withAccents );
    }

    // Same methods than preceding, for last campaign

    public boolean isBeforeBeginning( String phase )
    {
        return isBeforeBeginning( getLastCampaign( ).getCode( ), phase );
    }

    public boolean isBeforeEnd( String phase )
    {
        return isBeforeEnd( getLastCampaign( ).getCode( ), phase );
    }

    public boolean isDuring( String phase )
    {
        return isDuring( getLastCampaign( ).getCode( ), phase );
    }

    public boolean isAfterBeginning( String phase )
    {
        return isAfterBeginning( getLastCampaign( ).getCode( ), phase );
    }

    public boolean isAfterEnd( String phase )
    {
        return isAfterEnd( getLastCampaign( ).getCode( ), phase );
    }

    public Timestamp start( String phase )
    {
        return start( getLastCampaign( ).getCode( ), phase );
    }

    public Timestamp end( String phase )
    {
        return end( getLastCampaign( ).getCode( ), phase );
    }

    public String startStr( String phase, String format, boolean withAccents )
    {
        return startStr( getLastCampaign( ).getCode( ), phase, format, withAccents );
    }

    public String endStr( String phase, String format, boolean withAccents )
    {
        return endStr( getLastCampaign( ).getCode( ), phase, format, withAccents );
    }

    // ***********************************************************************************
    // * AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS A *
    // * AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS A *
    // ***********************************************************************************

    public List<String> getAllAreas( String codeCampaign )
    {
        Collection<CampaignArea> result = CampaignAreaHome.getCampaignAreasListByCampaign( codeCampaign );
        List<String> areas = new ArrayList<>( );
        for ( CampaignArea c : result )
        {
            if ( c.getActive( ) )
            {
                areas.add( c.getTitle( ) );
            }
        }
        return areas;
    }

    public List<String> getLocalizedAreas( String codeCampaign )
    {
        Collection<CampaignArea> result = CampaignAreaHome.getCampaignAreasListByCampaign( codeCampaign );
        List<String> areas = new ArrayList<>( );
        for ( CampaignArea c : result )
        {
            if ( c.getActive( ) && c.getType( ).equals( "localized" ) )
            {
                areas.add( c.getTitle( ) );
            }
        }
        return areas;
    }

    public boolean hasWholeArea( String codeCampaign )
    {
        Collection<CampaignArea> areas = CampaignAreaHome.getCampaignAreasListByCampaign( codeCampaign );
        for ( CampaignArea a : areas )
        {
            if ( a.getType( ).equals( "whole" ) )
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasWholeArea( String codeCampaign, int idCampaign )
    {
        Collection<CampaignArea> areas = CampaignAreaHome.getCampaignAreasListByCampaign( codeCampaign );
        for ( CampaignArea a : areas )
        {
            if ( a.getType( ).equals( "whole" ) && a.getId( ) != idCampaign )
            {
                return true;
            }
        }
        return false;
    }

    public String getWholeArea( String codeCampaign )
    {
        Collection<CampaignArea> areas = CampaignAreaHome.getCampaignAreasListByCampaign( codeCampaign );
        for ( CampaignArea a : areas )
        {
            if ( a.getType( ).equals( "whole" ) && a.getActive( ) )
            {
                return a.getTitle( );
            }
        }
        return "";
    }

    // Same methods than preceding, for last campaign

    public List<String> getAllAreas( )
    {
        return getAllAreas( getLastCampaign( ).getCode( ) );
    }

    public List<String> getLocalizedAreas( )
    {
        return getLocalizedAreas( getLastCampaign( ).getCode( ) );
    }

    public boolean hasWholeArea( )
    {
        return hasWholeArea( getLastCampaign( ).getCode( ) );
    }

    public boolean hasWholeArea( int id )
    {
        return hasWholeArea( getLastCampaign( ).getCode( ), id );
    }

    public String getWholeArea( )
    {
        return getWholeArea( getLastCampaign( ).getCode( ) );
    }

    // ***********************************************************************************
    // * THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES TH *
    // * THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES TH *
    // ***********************************************************************************

    @Override
    public ReferenceList getThemes( String codeCampaign )
    {
        ReferenceList items = new ReferenceList( );

        Collection<CampaignTheme> list = CampaignThemeHome.getCampaignThemesListByCampaign( codeCampaign );
        for ( CampaignTheme theme : list )
        {
            items.addItem( theme.getCode( ), theme.getTitle( ) );
        }

        return items;
    }

    @Override
    public ReferenceList getThemes( )
    {
        return getThemes( getLastCampaign( ).getCode( ) );
    }

    // *********************************************************************************************
    // * CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE *
    // * CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE CLONE *
    // *********************************************************************************************

    /**
     * {@inheritDoc}
     */
    @Override
    public int clone( int campaignId )
    {
        // Create new campaign ---------------------------------------------------------------------------

        Campaign campaignToClone = CampaignHome.findByPrimaryKey( campaignId );

        // Generate new code, max 50 chars
        String newCampaignCode = StringUtils.abbreviate( "(clone) " + campaignToClone.getCode( ), 50 );

        Campaign newCampaign = new Campaign( );
        newCampaign.setCode( newCampaignCode + "" );
        newCampaign.setTitle( "(clone) " + campaignToClone.getTitle( ) );
        newCampaign.setDescription( "(clone) " + campaignToClone.getDescription( ) );
        newCampaign.setActive( false );
        newCampaign.setCodeModerationType( campaignToClone.getCodeModerationType( ) );
        newCampaign.setModerationDuration( campaignToClone.getModerationDuration( ) );

        CampaignHome.create( newCampaign );

        // Clone phases ---------------------------------------------------------------------------------

        Collection<CampaignPhase> lastPhases = CampaignPhaseHome.getCampaignPhasesListByCampaign( campaignToClone.getCode( ) );

        for ( CampaignPhase lastPhase : lastPhases )
        {

            CampaignPhase phase = new CampaignPhase( );

            Calendar newStart = Calendar.getInstance( );
            newStart.setTime( lastPhase.getStart( ) );
            newStart.add( Calendar.YEAR, 1 );
            Calendar newEnd = Calendar.getInstance( );
            newEnd.setTime( lastPhase.getEnd( ) );
            newEnd.add( Calendar.YEAR, 1 );

            phase.setCodePhaseType( lastPhase.getCodePhaseType( ) );
            phase.setCodeCampaign( "" + newCampaignCode );
            phase.setStart( new Timestamp( newStart.getTimeInMillis( ) ) );
            phase.setEnd( new Timestamp( newEnd.getTimeInMillis( ) ) );

            CampaignPhaseHome.create( phase );
        }

        // Clone themes ---------------------------------------------------------------------------------

        Collection<CampaignTheme> lastThemes = CampaignThemeHome.getCampaignThemesListByCampaign( campaignToClone.getCode( ) );

        for ( CampaignTheme lastTheme : lastThemes )
        {

            CampaignTheme theme = new CampaignTheme( );

            theme.setCode( lastTheme.getCode( ) );
            theme.setCodeCampaign( "" + newCampaignCode );
            theme.setTitle( lastTheme.getTitle( ) );
            theme.setDescription( lastTheme.getDescription( ) );
            theme.setActive( true );

            CampaignThemeHome.create( theme );
        }

        // Clone areas -----------------------------------------------------------------------------

        Collection<CampaignArea> lastAreas = CampaignAreaHome.getCampaignAreasListByCampaign( campaignToClone.getCode( ) );

        for ( CampaignArea lastArea : lastAreas )
        {

            CampaignArea area = new CampaignArea( );

            area.setActive( lastArea.getActive( ) );
            area.setCodeCampaign( "" + newCampaignCode );
            area.setNumberVotes( lastArea.getNumberVotes( ) );
            area.setTitle( lastArea.getTitle( ) );
            area.setType( lastArea.getType( ) );

            CampaignAreaHome.create( area );
        }

        CampaignEventListernersManager.getInstance( ).notifyListeners( new CampaignEvent( newCampaign, campaignToClone, CampaignEvent.CAMPAIGN_CLONED ) );

        // Reseting cache

        CampaignService.getInstance( ).reset( );

        return newCampaign.getId( );
    }

    // ***********************************************************************************
    // * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS U *
    // * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS U *
    // ***********************************************************************************

    private String toSoLovelyString( String msg, boolean withAccents )
    {
        String soLovelyStr = null;

        // Deleting accents
        if ( !withAccents )
        {
            soLovelyStr = Normalizer.normalize( msg, Normalizer.Form.NFD ).replaceAll( "\\p{InCombiningDiacriticalMarks}+", "" );
        }
        else
        {
            soLovelyStr = msg;
        }

        // Replacing "01" by "1er"
        if ( soLovelyStr.startsWith( "01" ) )
        {
            soLovelyStr = "1er " + soLovelyStr.substring( 3 );
        }

        // Deleting "0" if first char
        if ( soLovelyStr.charAt( 0 ) == '0' )
        {
            soLovelyStr = soLovelyStr.substring( 1 );
        }

        return soLovelyStr;
    }

}
