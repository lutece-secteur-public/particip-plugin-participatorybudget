/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
import java.util.List;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;
import fr.paris.lutece.util.ReferenceList;

public interface ICampaignService {

    // ***********************************************************************************
	// * CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN *
	// * CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN *
	// ***********************************************************************************

	// Returns the campain the code of which is the SQL 'max'.
	// Ex : if 6 campagne with 'B0' - 'C' - 'D' - 'G0' - 'GA' - 'G', returns campagne 'GA'.
	public Campagne getLastCampagne ( ); 
    
	// ***********************************************************************************
	// * PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE P *
	// * PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE P *
	// ***********************************************************************************
	
    public boolean   isBeforeBeginning( String campagne, String phase ); // ......current < PHASE_BEGINNING
    public boolean   isBeforeEnd      ( String campagne, String phase ); // ..................................current < PHASE_END
    public boolean   isDuring         ( String campagne, String phase ); //                 PHASE_BEGINNING < current < PHASE_END
    public boolean   isAfterBeginning ( String campagne, String phase ); //                 PHASE_BEGINNING < current............................
    public boolean   isAfterEnd       ( String campagne, String phase ); //                                             PHASE_END < current......
	public Timestamp start            ( String campagne, String phase ); 
	public Timestamp end              ( String campagne, String phase ); 
	public String    startStr         ( String campagne, String phase, String format, boolean withAccents ); 
	public String    endStr           ( String campagne, String phase, String format, boolean withAccents ); 

	// Same as precedent, for last campagne
    public boolean   isBeforeBeginning( String phase ); 
    public boolean   isBeforeEnd      ( String phase );
    public boolean   isDuring         ( String phase );
    public boolean   isAfterBeginning ( String phase );
    public boolean   isAfterEnd       ( String phase ); 
    public Timestamp start            ( String phase ); 
	public Timestamp end              ( String phase ); 
	public String    startStr         ( String phase, String format, boolean withAccents ); 
	public String    endStr           ( String phase, String format, boolean withAccents ); 

    // ***********************************************************************************
	// * AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS A *
	// * AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS AREAS A *
	// ***********************************************************************************

	public List<String> getAllAreas	      ( String codeCampaign ); 
	public List<String> getLocalizedAreas ( String codeCampaign );
	public boolean  hasWholeArea	      ( String codeCampaign );
	public boolean  hasWholeArea          ( String codeCampaign, int idCampaign );
	public String   getWholeArea          ( String codeCampaign );

	// Same as precedent, for last campagne
	public List<String> getAllAreas	     ( );
	public List<String> getLocalizedAreas( );
	public boolean  hasWholeArea         ( );
	public boolean  hasWholeArea         ( int idCampaign );
	public String   getWholeArea         ( );

    // ***********************************************************************************
	// * THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES TH *
	// * THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES TH *
	// ***********************************************************************************

	public ReferenceList getThemes ( String codeCampaign ); 

	// Same as precedent, for last campagne
	public ReferenceList getThemes ( ); 

    // ***********************************************************************************
	// * CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE C *
	// * CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE C *
	// ***********************************************************************************

    // Resets the internal cache of phases
	public void reset( ); 
    
}
