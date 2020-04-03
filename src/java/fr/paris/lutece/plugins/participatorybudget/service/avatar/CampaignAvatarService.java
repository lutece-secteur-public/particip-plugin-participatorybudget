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
package fr.paris.lutece.plugins.participatorybudget.service.avatar;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import fr.paris.lutece.plugins.avatarserver.business.Avatar;
import fr.paris.lutece.plugins.avatarserver.business.AvatarHome;
import fr.paris.lutece.plugins.avatarserver.service.AvatarService;
import fr.paris.lutece.plugins.avatarserver.service.HashService;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignUploadHandler;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class CampaignAvatarService
{

    private static final String PARAMETER_AVATAR_IMAGE = "avatar_image";

    public static void createAvatar( HttpServletRequest request, String strLuteceUserName )
    {

        CampaignUploadHandler handler = SpringContextService.getBean( CampaignUploadHandler.BEAN_NAME );
        List<FileItem> listAvatar = handler.getListUploadedFiles( PARAMETER_AVATAR_IMAGE, request.getSession( ) );
        // save Avatar
        if ( listAvatar != null && listAvatar.size( ) > 0 && listAvatar.get( 0 ).getSize( ) > 0 )
        {

            Avatar avatar = new Avatar( );
            avatar.setValue( listAvatar.get( 0 ).get( ) );
            avatar.setMimeType( listAvatar.get( 0 ).getContentType( ) );
            avatar.setEmail( strLuteceUserName );

            AvatarService.create( avatar );
        }
        handler.removeSessionFiles( request.getSession( ).getId( ) );
    }

    public static void updateAvatar( HttpServletRequest request, String strLuteceUserName )
    {

        CampaignUploadHandler handler = SpringContextService.getBean( CampaignUploadHandler.BEAN_NAME );
        List<FileItem> listAvatar = handler.getListUploadedFiles( PARAMETER_AVATAR_IMAGE, request.getSession( ) );
        // save Avatar
        if ( listAvatar != null && listAvatar.size( ) > 0 && listAvatar.get( 0 ).getSize( ) > 0 )
        {

            String strHash = HashService.getHash( strLuteceUserName );
            Avatar avatar = AvatarHome.findByHash( strHash );
            if ( avatar == null )
            {
                avatar = new Avatar( );
                avatar.setEmail( strLuteceUserName );
                avatar.setValue( listAvatar.get( 0 ).get( ) );
                avatar.setMimeType( listAvatar.get( 0 ).getContentType( ) );

                AvatarService.create( avatar );
            }
            else
            {
                avatar.setValue( listAvatar.get( 0 ).get( ) );
                avatar.setMimeType( listAvatar.get( 0 ).getContentType( ) );
                AvatarService.update( avatar );
            }
        }
        handler.removeSessionFiles( request.getSession( ).getId( ) );

    }

}
