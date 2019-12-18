 /*
 /*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.plugins.participatorybudget.web.rs;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import fr.paris.lutece.plugins.participatorybudget.service.NoSuchPhaseException;
import fr.paris.lutece.plugins.participatorybudget.service.authentication.RequestAuthenticationService;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampagnesService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;

import net.sf.json.JSONObject;

@Path(RestConstants.BASE_PATH + "campaign")
public class CampaignRest {

    private static final String LOG_UNAUTHENTICATED_REQUEST = "Calling Campaign rest API with unauthenticated request";
	
    private String formatJson(String status, boolean result) {
		String message;
    	JSONObject json = new JSONObject();
    	json.put("status", status);
    	json.put("result", result);
    	message = json.toString();
    	
    	return message;
	}

    private String formatJson(String status, String result) {
		String message;
        JSONObject json = new JSONObject();
        json.put("status", status);
        json.put("result", result);
        message = json.toString();

        return message;
	}

    private String formatJson(String status, List<String> result) {
        String message;
        JSONObject json = new JSONObject();
        json.put("status", status);
        json.put("result", result);
        message = json.toString();
        return message;
	}
	
	/**
     * get isBeforeBeginning of a phase of a campaign
     * 
     * @return the response of the request isBeforeBeginning for a phase of a campaign
	 * @throws ServletException 
     */
    @GET
    @Path("{campaign}/{phase}/before-beginning")
    @Produces(MediaType.APPLICATION_JSON)
    public String isBeforeBeginning( @Context
    	    HttpServletRequest request, @PathParam("campaign") String campaign, @PathParam("phase") String phase) throws ServletException {
    	if ( !isRequestAuthenticated( request ) )
	    {
	        AppLogService.error( LOG_UNAUTHENTICATED_REQUEST );
	        throw new ServletException( LOG_UNAUTHENTICATED_REQUEST );
	    }
    	try {
            return formatJson( "OK", CampagnesService.getInstance().isBeforeBeginning( campaign, phase ) );
    	} catch (NoSuchPhaseException e) {
			AppLogService.error(e);
            return formatJson( "KO", false );
    	}
    }
    
    /**
     * get isAfterBeginning of a phase of a campaign
     * 
     * @return the response of the request isAfterBeginning for a phase of a campaign
     * @throws ServletException 
     */
    @GET
    @Path("{campaign}/{phase}/after-beginning")
    @Produces(MediaType.APPLICATION_JSON)
    public String isAfterBeginning(@Context
    	    HttpServletRequest request, @PathParam("campaign") String campaign, @PathParam("phase") String phase) throws ServletException {
    	if ( !isRequestAuthenticated( request ) )
	    {
	        AppLogService.error( LOG_UNAUTHENTICATED_REQUEST );
	        throw new ServletException( LOG_UNAUTHENTICATED_REQUEST );
	    }
    	try {
            return formatJson( "OK", CampagnesService.getInstance().isAfterBeginning( campaign, phase ) );
    	} catch (NoSuchPhaseException e) {
			AppLogService.error(e);
	        return formatJson( "KO", false );
    	}
    }
    
    /**
     * get isDuring of a phase of a campaign
     * 
     * @return the response of the request isDuring for a phase of a campaign
     * @throws ServletException 
     */
    @GET
    @Path("{campaign}/{phase}/during")
    @Produces(MediaType.APPLICATION_JSON)
    public String isDuring(@Context
    	    HttpServletRequest request, @PathParam("campaign") String campaign, @PathParam("phase") String phase) throws ServletException
    {
    	if ( !isRequestAuthenticated( request ) )
    	    {
    	        AppLogService.error( LOG_UNAUTHENTICATED_REQUEST );
    	        throw new ServletException( LOG_UNAUTHENTICATED_REQUEST );
    	    }
    	try {
            return formatJson( "OK", CampagnesService.getInstance().isDuring( campaign, phase ) );
    	} catch (NoSuchPhaseException e) {
			AppLogService.error(e);
	        return formatJson( "KO", false );
    	}
    }

    /**
     * get isBeforeEnd of a phase of a campaign
     * 
     * @return the response of the request isBeforeEnd for a phase of a campaign
     * @throws ServletException 
     */
    @GET
    @Path("{campaign}/{phase}/before-end")
    @Produces(MediaType.APPLICATION_JSON)
    public String isBeforeEnd(@Context
    	    HttpServletRequest request, @PathParam("campaign") String campaign, @PathParam("phase") String phase) throws ServletException {
    	if ( !isRequestAuthenticated( request ) )
	    {
	        AppLogService.error( LOG_UNAUTHENTICATED_REQUEST );
	        throw new ServletException( LOG_UNAUTHENTICATED_REQUEST );
	    }
    	try {
            return formatJson( "OK", CampagnesService.getInstance().isBeforeEnd( campaign, phase ) );
        } catch (NoSuchPhaseException e) {
			AppLogService.error(e);
	        return formatJson( "KO", false );
        }
    }

    /**
     * get isAfterEnd of a phase of a campaign
     * 
     * @return the response of the request isAfterEnd for a phase of a campaign
     * @throws ServletException 
     */
    @GET
    @Path("{campaign}/{phase}/after-end")
    @Produces(MediaType.APPLICATION_JSON)
    public String isAfterEnd(@Context HttpServletRequest request, @PathParam("campaign") String campaign, @PathParam("phase") String phase) throws ServletException
    {
        if ( !isRequestAuthenticated( request ) )
	    {
	        AppLogService.error( LOG_UNAUTHENTICATED_REQUEST );
	        throw new ServletException( LOG_UNAUTHENTICATED_REQUEST );
	    }
        try {
            return formatJson( "OK", CampagnesService.getInstance().isAfterEnd( campaign, phase ) );
        } catch (NoSuchPhaseException e) {
			AppLogService.error(e);
	        return formatJson( "KO", false );
        }
    }

    /**
     * get areas of a campaign
     *
     * @return the response of the request getAreas for a campaign
     * @throws ServletException
     */
    @GET
    @Path("{campaign}/areas")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCampaignAreas(@Context HttpServletRequest request, @PathParam("campaign") String campaign) throws ServletException
    {
        if ( !isRequestAuthenticated( request ) )
	    {
	        AppLogService.error( LOG_UNAUTHENTICATED_REQUEST );
	        throw new ServletException( LOG_UNAUTHENTICATED_REQUEST );
	    }
        try {
            return formatJson( "OK", CampagnesService.getInstance().getAreas( campaign ) );
        } catch (NoSuchPhaseException e) {
			AppLogService.error(e);
	        return formatJson( "KO", false );
        }
    }

    /**
     * get areas of last campaign
     *
     * @return the response of the request getAreas for the last campaign
     * @throws ServletException
     */
    @GET
    @Path("areas")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLastCampaignAreas(@Context HttpServletRequest request) throws ServletException
    {
    	if ( !isRequestAuthenticated( request ) )
	    {
	        AppLogService.error( LOG_UNAUTHENTICATED_REQUEST );
	        throw new ServletException( LOG_UNAUTHENTICATED_REQUEST );
	    }
    	try {
            return formatJson( "OK", CampagnesService.getInstance().getAreas( ) );
    	} catch (NoSuchPhaseException e) {
			AppLogService.error(e);
	        return formatJson( "KO", false );
    	}
    }
    
    /**
     * get area whole of a campaign
     *
     * @return the response of the request getAreas for a campaign
     * @throws ServletException
     */
    @GET
    @Path("{campaign}/whole")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCampaignWhole(@Context HttpServletRequest request, @PathParam("campaign") String campaign) throws ServletException
    {
        if ( !isRequestAuthenticated( request ) )
	    {
	        AppLogService.error( LOG_UNAUTHENTICATED_REQUEST );
	        throw new ServletException( LOG_UNAUTHENTICATED_REQUEST );
	    }
        try {
            return formatJson( "OK", CampagnesService.getInstance().getWholeArea( campaign ) );
        } catch (NoSuchPhaseException e) {
			AppLogService.error(e);
	        return formatJson( "KO", false );
        }
    }

    /**
     * get areas of last campaign
     *
     * @return the response of the request getAreas for the last campaign
     * @throws ServletException
     */
    @GET
    @Path("whole")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLastCampaignWhole(@Context HttpServletRequest request) throws ServletException
    {
        if ( !isRequestAuthenticated( request ) )
	    {
	        AppLogService.error( LOG_UNAUTHENTICATED_REQUEST );
	        throw new ServletException( LOG_UNAUTHENTICATED_REQUEST );
	    }
        try {
            String result = CampagnesService.getInstance().getWholeArea( );
            if (result.equals("")) {
                return formatJson( "KO", result );
            }
            return formatJson( "OK", result );
        } catch (NoSuchPhaseException e) {
		    AppLogService.error(e);
	        return formatJson( "KO", false );
        }
    }

    /**
     * Checks if the request is authenticated or not
     *
     * @param request
     *            the HTTP request
     * @return {@code true} if the request is authenticated, {@code false} otherwise
     */
    private boolean isRequestAuthenticated( HttpServletRequest request )
    {
        return RequestAuthenticationService.getRequestAuthenticator( ).isRequestAuthenticated( request );
    }
}