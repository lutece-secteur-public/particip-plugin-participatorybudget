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
