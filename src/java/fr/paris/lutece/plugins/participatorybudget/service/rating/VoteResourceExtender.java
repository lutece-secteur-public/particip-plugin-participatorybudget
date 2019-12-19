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
package fr.paris.lutece.plugins.participatorybudget.service.rating;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.modules.rating.service.security.IRatingSecurityService;
import fr.paris.lutece.plugins.extend.service.extender.AbstractResourceExtender;
import fr.paris.lutece.plugins.participatorybudget.service.MyVoteService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * VoteResourceExtender
 */
public class VoteResourceExtender extends AbstractResourceExtender
{
    private static final String DSKEY_USER_CAN_VOTE = "participatorybudget.site_property.extend.template.userCanVote.textblock";
    private static final String DSKEY_USER_CAN_VOTE_DEFAULT = "participatorybudget.extend.template.userCanVote not defined in datastore";
    private static final String DSKEY_USER_CANNOT_VOTE = "participatorybudget.site_property.extend.template.userCannotVote.textblock";
    private static final String DSKEY_VOTE_CLOSED = "participatorybudget.site_property.extend.template.voteClosed.textblock";
    private static final String DSKEY_USER_CANNOT_VOTE_DEFAULT = "participatorybudget.extend.template.userCannotVote not defined in datastore";
    private static final String BOOKMARK_ID = "@id@";
    @Inject
	private MyVoteService _myVoteService;
    @Inject
    private IRatingSecurityService _ratingSecurityService;

    /**
     * {@inheritDoc }
     */
    @Override
    public String getContent(String strIdExtendableResource, String strExtendableResourceType, String strParameters, HttpServletRequest request)
    {
        boolean bCanVote;
        
        bCanVote = !_ratingSecurityService.hasAlreadyVoted(request, strIdExtendableResource, strExtendableResourceType);
        
        String strTemplate;
        if( bCanVote )
        {
            strTemplate = DatastoreService.getDataValue( DSKEY_USER_CAN_VOTE , DSKEY_USER_CAN_VOTE_DEFAULT );
        } 
        else
        {
            strTemplate = DatastoreService.getDataValue( DSKEY_USER_CANNOT_VOTE , DSKEY_USER_CANNOT_VOTE_DEFAULT );
        }
        return strTemplate.replaceAll( BOOKMARK_ID , strIdExtendableResource );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void doCreateResourceAddOn(ResourceExtenderDTO resourceExtender)
    {
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void doDeleteResourceAddOn(ResourceExtenderDTO resourceExtender)
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInvoked( String strExtenderType )
    {
        if ( StringUtils.isNotBlank( strExtenderType ) )
        {
            return getKey(  ).equals( strExtenderType );
        }

        return false;
    }

}
