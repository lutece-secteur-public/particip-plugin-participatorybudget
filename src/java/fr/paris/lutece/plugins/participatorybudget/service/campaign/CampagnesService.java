package fr.paris.lutece.plugins.participatorybudget.service.campaign;

import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagnePhase;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagnePhaseHome;
import fr.paris.lutece.plugins.participatorybudget.service.NoSuchPhaseException;
import fr.paris.lutece.plugins.participatorybudget.util.Constants;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;


public class CampagnesService implements ICampagneService {

    // Attributes
    
	private Map<String, Timestamp> _cache = null;
		
	// ***********************************************************************************
	// * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
	// * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
	// ***********************************************************************************
	
	private static final String BEAN_CAMPAGNE_SERVICE = "participatorybudget.campagneService";
	
	private static ICampagneService _singleton;
	
	public static ICampagneService getInstance () {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_CAMPAGNE_SERVICE );
        }
        return _singleton;
    }
	
	// ***********************************************************************************
	// * CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE C *
	// * CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE CACHE C *
	// ***********************************************************************************

    private Map<String, Timestamp> getCache ( ) 
    {
    	if (_cache == null) 
    		reset();
    	
    	return _cache;
	}

    private String getKey ( String campain, String phase, String datetimeType ) 
    {
    	return campain + "-" + phase + "-" + datetimeType;
	}
    
	public void reset () 
	{
		AppLogService.debug("CampagnePhase cache reset");

		Map<String, Timestamp> cache = new HashMap<String, Timestamp> ();
		
		Collection<CampagnePhase> phases = CampagnePhaseHome.getCampagnePhasesList();
		for (CampagnePhase phase : phases) 
		{
			String beginningKey = getKey( phase.getCodeCampagne(), phase.getCodePhaseType(), Constants.BEGINNING_DATETIME );
			String       endKey = getKey( phase.getCodeCampagne(), phase.getCodePhaseType(), Constants.END_DATETIME );
			
			cache.put( beginningKey, phase.getStart() );
			cache.put(       endKey, phase.getEnd  () );
			
			AppLogService.debug("  -> Added '" + phase.getCodeCampagne() + "-" + phase.getCodePhaseType() + "' = '" + phase.getStart() + "/" + phase.getEnd() + "'.");
		}
		
		_cache = cache;
	}
	
    // ***********************************************************************************
	// * CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN *
	// * CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN CAMPAIN *
	// ***********************************************************************************

	public Campagne getLastCampagne () 
	{
		return CampagneHome.getLastCampagne();
	}
    
	// ***********************************************************************************
	// * PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE P *
	// * PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE PHASE P *
	// ***********************************************************************************

	private Timestamp getTimestamp( String campain, String phase, String timestampType )
	{
    	String key = getKey( campain, phase, timestampType );
    	Timestamp timeStamp = getCache().get( key );
    	if (timeStamp == null ) {
    		String errorMsg = "Null datetime for campagne '" + campain + "' and phase '" + phase + "' and timestampType '" + timestampType + "'. ";
    		AppLogService.error( errorMsg );
    		throw new NoSuchPhaseException( errorMsg );
    	}
    	return timeStamp;
	}
	
    public boolean isBeforeBeginning ( String campain, String phase )
    {
    	Timestamp timeStamp = getTimestamp( campain, phase, Constants.BEGINNING_DATETIME );
    	Date date = new Date(  );
    	boolean result = date.before(timeStamp);
    	return result;
    }
    
    public boolean isBeforeEnd       ( String campain, String phase )
    {
    	Timestamp timeStamp = getTimestamp( campain, phase, Constants.END_DATETIME );
    	Date date = new Date(  );
    	boolean result = date.before(timeStamp);
    	return result;
    }
     
    public boolean isDuring          ( String campain, String phase ) 
    {
    	Timestamp beginningTimeStamp = getTimestamp( campain, phase, Constants.BEGINNING_DATETIME );
    	Timestamp       endTimeStamp = getTimestamp( campain, phase, Constants.END_DATETIME       );
    	
    	Date date = new Date(  );
    	
    	boolean result = date.after(beginningTimeStamp) && date.before(endTimeStamp);
    	return result;
    }
     
    public boolean isAfterBeginning  ( String campain, String phase )      
    {
    	Timestamp timeStamp = getTimestamp( campain, phase, Constants.BEGINNING_DATETIME );
    	Date date = new Date(  );
    	boolean result = date.after(timeStamp);
    	return result;
    }

    public boolean isAfterEnd        ( String campain, String phase ) 
    {
    	Timestamp timeStamp = getTimestamp( campain, phase, Constants.END_DATETIME );
    	Date date = new Date(  );
    	boolean result = date.after(timeStamp);
    	return result;
    }

    public Timestamp start ( String campain, String phase )
    {
    	Timestamp timeStamp = getTimestamp( campain, phase, Constants.BEGINNING_DATETIME );
    	return timeStamp;
    }
    
    public Timestamp end ( String campain, String phase )
    {
    	Timestamp timeStamp = getTimestamp( campain, phase, Constants.END_DATETIME );
    	return timeStamp;
    }
    
	public String startStr ( String campain, String phase, String format, boolean withAccents ) 
	{
		Timestamp ts = start( campain, phase );
		Date date = new Date ();
		date.setTime(ts.getTime());
		String formattedDate = new SimpleDateFormat( format ).format( date );
		return toSoLovelyString (formattedDate,withAccents); 
	}

	public String endStr ( String campain, String phase, String format, boolean withAccents ) 
	{
		Timestamp ts = end( campain, phase );
		Date date = new Date ();
		date.setTime(ts.getTime());
		String formattedDate = new SimpleDateFormat( format ).format( date );
		return toSoLovelyString (formattedDate, withAccents); 
	}

	// Same methods than preceding, for last campagne
    
    public boolean isBeforeBeginning ( String phase ) { return isBeforeBeginning ( getLastCampagne().getCode(), phase); }
    public boolean isBeforeEnd       ( String phase ) { return isBeforeEnd       ( getLastCampagne().getCode(), phase); }
    public boolean isDuring          ( String phase ) { return isDuring          ( getLastCampagne().getCode(), phase); }
    public boolean isAfterBeginning  ( String phase ) { return isAfterBeginning  ( getLastCampagne().getCode(), phase); }
    public boolean isAfterEnd        ( String phase ) { return isAfterEnd        ( getLastCampagne().getCode(), phase); }

    public Timestamp start    ( String phase )                                     { return start    ( getLastCampagne().getCode(), phase); }
    public Timestamp end      ( String phase )                                     { return end      ( getLastCampagne().getCode(), phase); }
	public String    startStr ( String phase, String format, boolean withAccents ) { return startStr ( getLastCampagne().getCode(), phase, format, withAccents ); }
	public String      endStr ( String phase, String format, boolean withAccents ) { return   endStr ( getLastCampagne().getCode(), phase, format, withAccents ); }

    // ***********************************************************************************
	// * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS U *
	// * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS U *
	// ***********************************************************************************

	private String toSoLovelyString ( String msg, boolean withAccents ) {
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
			soLovelyStr = "1er " + soLovelyStr.substring(3);
		}
		
		// Deleting "0" if first char
		if ( soLovelyStr.charAt(0) == '0' )
		{
			soLovelyStr = soLovelyStr.substring(1);
		}
		
		return soLovelyStr;
	}
	
}
