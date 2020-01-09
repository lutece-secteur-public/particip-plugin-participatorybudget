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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import fr.paris.lutece.plugins.participatorybudget.service.MyInfosService;
import fr.paris.lutece.plugins.participatorybudget.web.MyInfosXPage;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;

@Path( RestConstants.BASE_PATH + "myinfos" )
public class MyInfosRest extends AbstractServiceRest
{

    private static final String LOG_UNAUTHENTICATED_REQUEST = "Calling MyInfos rest API with unauthenticated request";

    // *********************************************************************************************
    // * MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS M *
    // * MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS MYINFOS M *
    // *********************************************************************************************

    /**
     * Returns true if current user infos are valid
     * 
     * @return {@code true} if current user infos are valid, {@code false} otherwise
     * @throws ServletException
     */
    @GET
    @Path( "{userid}/are-myinfos-valid" )
    @Produces( MediaType.APPLICATION_JSON )
    public String areMyInfosValid( @Context HttpServletRequest request, @PathParam( "userid" ) String userId ) throws ServletException
    {
        if ( !isRequestAuthenticated( request ) )
        {
            AppLogService.error( LOG_UNAUTHENTICATED_REQUEST );
            throw new ServletException( LOG_UNAUTHENTICATED_REQUEST );
        }
        try
        {
            LuteceUser user = SecurityService.getInstance( ).getUser( userId );
            if ( user == null )
            {
                return formatJson( "KO", "No such user : " + userId + " !" );
            }
            return formatJson( "OK", MyInfosService.loadUserInfos( user ).getIsValid( ) );
        }
        catch( Exception e )
        {
            AppLogService.error( e );
            return formatJson( "KO", false );
        }
    }

    /**
     * Returns URL of page where user can fill its personal infos
     * 
     * @return url of the web page
     * @throws ServletException
     */
    @GET
    @Path( "url-myinfos-fill-action" )
    @Produces( MediaType.APPLICATION_JSON )
    public String getUrlMyInfosFillAction( @Context HttpServletRequest request ) throws ServletException
    {
        if ( !isRequestAuthenticated( request ) )
        {
            AppLogService.error( LOG_UNAUTHENTICATED_REQUEST );
            throw new ServletException( LOG_UNAUTHENTICATED_REQUEST );
        }
        try
        {
            return formatJson( "OK", MyInfosXPage.getUrlMyInfos( ) );
        }
        catch( Exception e )
        {
            AppLogService.error( e );
            return formatJson( "KO", false );
        }
    }

}
