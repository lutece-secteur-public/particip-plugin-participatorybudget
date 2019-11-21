package fr.paris.lutece.plugins.participatorybudget.service.campaign;

import java.sql.Timestamp;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;

public interface ICampagneService {

	// Returns the campain the code of which is the SQL 'max'.
	// Ex : if 6 campagne with 'B0' - 'C' - 'D' - 'G0' - 'GA' - 'G', returns campagne 'GA'.
	public Campagne getLastCampagne ( ); 
    
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
    
    // Resets the internal cache of phases
	public void reset( ); 
    
}
