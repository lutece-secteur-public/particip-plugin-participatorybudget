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
package fr.paris.lutece.plugins.participatorybudget.service.rating;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.extend.modules.rating.service.validator.IRatingValidationService;
import fr.paris.lutece.plugins.participatorybudget.business.MyInfosForm;
import fr.paris.lutece.plugins.participatorybudget.business.VoteHome;
import fr.paris.lutece.plugins.participatorybudget.business.VotePerLocation;
import fr.paris.lutece.plugins.participatorybudget.service.IVoteParArrandissementService;
import fr.paris.lutece.plugins.participatorybudget.service.MyInfosService;
import fr.paris.lutece.plugins.participatorybudget.service.MyVoteService;
import fr.paris.lutece.plugins.participatorybudget.service.VoteParArrandissementService;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.plugins.participatorybudget.util.BudgetUtils;
import fr.paris.lutece.plugins.participatorybudget.util.Constants;
import fr.paris.lutece.plugins.participatorybudget.web.MyInfosXPage;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * Validator that checks that the user is valid before allowing him to rate projects.
 */
public class RatingValidationService implements IRatingValidationService
{

    /**
     * {@inheritDoc}
     */
    @Override
    public String validateRating( HttpServletRequest request, LuteceUser user, String strIdResource, String strResourceTypeKey, double dVoteValue )
    {
        if ( !BudgetRatingService.isBudgetResource( strIdResource, strResourceTypeKey ) )
        {
            return null;
        }
        MyInfosForm myInfo = MyInfosService.loadUserInfos( user );

        if ( !myInfo.getIsValid( ) )// || !MyInfosService.isAccountVerified(user))
        {
            return MyInfosXPage.getUrlCompleteMyInfos( strIdResource, strResourceTypeKey, dVoteValue );
        }

        if ( dVoteValue != 1 )
        {
            return getUrlVote( strIdResource, strResourceTypeKey );
        }

        IVoteParArrandissementService _nbrVoteService = VoteParArrandissementService.getInstance( );
        MyVoteService _myVoteService = SpringContextService.getBean( MyVoteService.BEAN_NAME );

        String strUserId = user.getName( );
        String strDefaultErrorUrl = AppPathService.getBaseUrl( request );
        if ( StringUtils.isEmpty( strDefaultErrorUrl ) )
        {
            // Just make sure it's not empty because empty means no errors !
            strDefaultErrorUrl = "/";
        }

        // This is duplicated in MyVoteXPage.java because there it wants to return json
        // instead of redirecting to an url or throwing a SiteMessageException
        if ( !CampaignService.getInstance( ).isDuring( "VOTE" ) || _myVoteService.isUserVoteValidated( strUserId ) )
        {
            return strDefaultErrorUrl;
        }

        // This is duplicated in MyVoteXPage.java because there it want to return json
        // instead of redirecting to an url or throwing a SiteMessageException
        int nbrVoteUserArrond = VoteHome.getVoteUserNotLocalisation( strUserId, 75000 );
        int nbrVoteUserParis = VoteHome.getVoteUserArrondissement( strUserId, 75000 );
        int maxDcmtArrondissement = 0;
        int maxDcmtToutParis = 0;
        VotePerLocation voteLoc = _nbrVoteService.selectVotePerLocation( myInfo.getArrondissement( ) );
        if ( voteLoc != null )
        {
            maxDcmtArrondissement = voteLoc.getNbVotes( );
        }
        voteLoc = _nbrVoteService.selectVotePerLocation( "whole_city" );
        if ( voteLoc != null )
        {
            maxDcmtToutParis = voteLoc.getNbVotes( );
        }
        String localisationProjet = request.getParameter( Constants.PROJECT_LOCALISATION );

        if ( nbrVoteUserParis >= maxDcmtToutParis && nbrVoteUserArrond >= maxDcmtArrondissement )
        {

            return strDefaultErrorUrl;
        }
        if ( nbrVoteUserParis >= maxDcmtToutParis && maxDcmtToutParis > 0
                && request.getParameter( Constants.PROJECT_LOCALISATION ).equals( Constants.LOCALISATION_PARIS ) )
        {

            return strDefaultErrorUrl;
        }
        if ( nbrVoteUserArrond >= maxDcmtArrondissement && maxDcmtArrondissement > 0
                && !request.getParameter( Constants.PROJECT_LOCALISATION ).equals( Constants.LOCALISATION_PARIS ) )
        {

            return strDefaultErrorUrl;

        }
        if ( localisationProjet != null && !localisationProjet.equals( BudgetUtils.getArrondissementDisplay( user ) )
                && !localisationProjet.equals( Constants.LOCALISATION_PARIS ) )
        {

            return strDefaultErrorUrl;

        }

        return null;
    }

    /**
     * Get the URL to vote for a resource. User can only vote for the value 1 when using this method.
     * 
     * @param strIdResource
     *            The id of the resource
     * @param strResourceTypeKey
     *            The resource type key
     * @return The required URL
     */
    private String getUrlVote( String strIdResource, String strResourceTypeKey )
    {
        UrlItem urlItem = new UrlItem( "jsp/site/plugins/extend/modules/rating/DoVote.jsp" );
        urlItem.addParameter( "idExtendableResource", strIdResource );
        urlItem.addParameter( "extendableResourceType", strResourceTypeKey );
        urlItem.addParameter( "voteValue", 1 );

        return urlItem.getUrl( );
    }
}
