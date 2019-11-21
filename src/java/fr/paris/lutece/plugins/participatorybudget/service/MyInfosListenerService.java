package fr.paris.lutece.plugins.participatorybudget.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.service.security.LuteceUser;
/**
 * 
 * MyInfosListenerService
 *
 */
public class MyInfosListenerService {

	
	private static List<IMyInfosListener> _lisListeners =  new ArrayList<IMyInfosListener>( );
	private static boolean _bHasListeners;
	private static Map<String, List<IMyInfosListener>> _mapListeners = new HashMap<String, List<IMyInfosListener>>( );  
	public static final String Document_PROPERTY_RESOURCE_TYPE = "document";
	  /**
	     * Register a comment listener.
	     * @param strExtendableResourceType The extendable resource type associated
	     *            with the listener. Use
	     *            {@link #CONSTANT_EVERY_EXTENDABLE_RESOURCE_TYPE} to associated
	     *            the listener with every resource type.
	     * @param listener The listener to register
	     */
	    public static synchronized void registerListener( IMyInfosListener listener )
	    {
	    	_lisListeners.add(listener);
	    	_bHasListeners = true;
	    }
	    /**
	     * Register a comment listener.
	     * @param strExtendableResourceType The extendable resource type associated
	     *            with the listener. Use
	     *            {@link #CONSTANT_EVERY_EXTENDABLE_RESOURCE_TYPE} to associated
	     *            the listener with every resource type.
	     * @param listener The listener to register
	     */
	    public static synchronized void registerListener( String strExtendableResourceType, IMyInfosListener listener )
	    {
	        List<IMyInfosListener> listListeners = _mapListeners.get( strExtendableResourceType );
	        if ( listListeners == null )
	        {
	            listListeners = new ArrayList<IMyInfosListener>( );
	            _mapListeners.put( strExtendableResourceType, listListeners );
	        }
	        listListeners.add( listener );
	        _bHasListeners = true;
	    }

  

	    /**
	     * Check if there is listeners to notify
	     * @return True if there is at last one listener, false otherwise
	     */
	    public static boolean hasListener( )
	    {
	        return _bHasListeners;
	    }
	    
	    
	    
	    /**
	     * Notify to listeners new follow. Only listeners associated
	     * with the extendable resource type of the comment are notified.
	     * @param strExtendableResourceType The extendable resource type
	     * @param strIdExtendableResource The extendable resource id of the comment
	     * @param request the HTTP request
	     */
	    public static void updateNickName( String strLuteceUserName,String strNickName)
	    {
	        
	       
	            for ( IMyInfosListener listener : _lisListeners )
	            {
	                listener.updateNickName(strLuteceUserName, strNickName);
	            }
	        
	       
	    }
	    
	    /**
	     * Notify to listeners new follow. Only listeners associated
	     * with the extendable resource type of the comment are notified.
	     * @param strExtendableResourceType The extendable resource type
	     * @param strIdExtendableResource The extendable resource id of the comment
	     * @param request the HTTP request
	     */
	    public static void createNickName( String strLuteceUserName,String strNickName)
	    {
	        
	       
	            for ( IMyInfosListener listener : _lisListeners )
	            {
	                listener.createNickName(strLuteceUserName, strNickName);
	            }
	        
	       
	    }
	    
	    /**
	     * Can change arrondissement by user. 
	     * @return result 
	     */
	    public static int canChangeArrond( LuteceUser user )
	    {
	       int bRes = 0;
	       
	            for ( IMyInfosListener listener : _mapListeners.get( Document_PROPERTY_RESOURCE_TYPE ) )
	            {
	            		bRes = listener.canChangeArrond( user );
	            		//bres=0 -->default value
	            		if(bRes!=0)
	            		{
	            		  return bRes;
	            		}
	            }
	       return bRes ;
	    }
	    
	    /**
	     * Do delete votes after changing arrondissement
	     * @param request  the HttpServletRequest
	     * @return result 
	     */
	    public static String deleteVotes( HttpServletRequest request )
	    {
	    	String strRes = "";
	            for ( IMyInfosListener listener : _mapListeners.get( Document_PROPERTY_RESOURCE_TYPE ) )
	            {
	            	strRes = listener.deleteVotes( request );
	            	if(!StringUtils.isEmpty(strRes))
            		{
	            	    
	            	    return strRes;
            		}
	            }
		   return strRes;
	    }
	    
	  
}
