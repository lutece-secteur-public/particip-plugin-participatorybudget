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
package fr.paris.lutece.plugins.participatorybudget.service.campaign;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagnePhase;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagnePhaseHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneTheme;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneThemeHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class CampaignChronoService implements ICampaignChronoService {
	
	private static final String BEAN_CAMPAIGNCHRONO_SERVICE = "participatorybudget.campaignChronoService";
	
	private static ICampaignChronoService _singleton;
	
	public static ICampaignChronoService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_CAMPAIGNCHRONO_SERVICE );
        }
        return _singleton;
    }
	
	
	// ***********************************************************************************
	// * GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERAT *
	// * GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERAT *
	// ***********************************************************************************
	
    /**
     * {@inheritDoc}
     */
    @Override
	public String generate( )
    {
    	// Creates new campagne ---------------------------------------------------------------------------
    	
    	Campagne lastCampagne = CampagneHome.getLastCampagne();

    	char newCampagneCode = lastCampagne.getCode().charAt(0);
    	newCampagneCode += 1;
    	
    	Campagne newCampagne = new Campagne();
    	newCampagne.setCode               ( newCampagneCode + "" );
    	newCampagne.setTitle              ( "(clone) " + lastCampagne.getTitle() );
    	newCampagne.setDescription        ( "(clone) " + lastCampagne.getDescription() );
    	newCampagne.setActive             ( false );
    	newCampagne.setCodeModerationType ( lastCampagne.getCodeModerationType() );
    	newCampagne.setModerationDuration ( lastCampagne.getModerationDuration() );
    	
        CampagneHome.create( newCampagne );
    	
        // Creates phases ---------------------------------------------------------------------------------
        
        Collection<CampagnePhase> lastPhases = CampagnePhaseHome.getCampagnePhasesListByCampagne( lastCampagne.getCode() );
        
        for (CampagnePhase lastPhase : lastPhases) {
        	
			CampagnePhase phase = new CampagnePhase();

			Calendar newStart = Calendar.getInstance(); newStart.setTime( lastPhase.getStart() ); newStart.add( Calendar.YEAR, 1 );
			Calendar newEnd   = Calendar.getInstance();   newEnd.setTime(   lastPhase.getEnd() );   newEnd.add( Calendar.YEAR, 1 );
			
			phase.setCodePhaseType ( lastPhase.getCodePhaseType() );
			phase.setCodeCampagne  ( "" + newCampagneCode );
			phase.setStart         ( new Timestamp(newStart.getTimeInMillis()) );
			phase.setEnd           ( new Timestamp(  newEnd.getTimeInMillis()) );
			
			CampagnePhaseHome.create( phase );
		}
    	
    	// Creates themes ---------------------------------------------------------------------------------
    	
        Collection<CampagneTheme> lastThemes = CampagneThemeHome.getCampagneThemesListByCampagne( lastCampagne.getCode() );
        
        for (CampagneTheme lastTheme : lastThemes) {
        	
        	CampagneTheme theme = new CampagneTheme();

			theme.setCode         ( lastTheme.getCode() );
			theme.setCodeCampagne ( "" + newCampagneCode );
			theme.setTitle        ( lastTheme.getTitle() );
			theme.setDescription  ( lastTheme.getDescription() );
			theme.setActive       ( true );
			
			CampagneThemeHome.create( theme );
		}
        
    	// Creates depositary ----------------------------------------------------------------------------- 
    	
//        Collection<CampagneDepositaire> lastDepositaires = CampagneDepositaireHome.getCampagneDepositaireListByCampagne( lastCampagne.getCode() );
//        
//        for (CampagneDepositaire lastDepositaire : lastDepositaires) {
//        	
//        	CampagneDepositaire depositaire = new CampagneDepositaire();
//
//        	depositaire.setCodeDepositaireType ( lastDepositaire.getCodeDepositaireType() );
//        	depositaire.setCodeCampagne        ( "" + newCampagneCode );
//			
//			CampagneDepositaireHome.create( depositaire );
//		}
        
        // Reseting cache
        
        CampaignService.getInstance().reset();
        
        return newCampagne.getCode();
    }
    
}