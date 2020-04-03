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
package fr.paris.lutece.plugins.participatorybudget.service;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampaignService;
import fr.paris.lutece.plugins.participatorybudget.service.vote.MyVoteService;
import fr.paris.lutece.plugins.participatorybudget.util.BudgetUtils;
import fr.paris.lutece.portal.business.resourceenhancer.IResourceDisplayManager;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * Manager for add on display TODO : move this class into a document specific class !
 */
public class DocumentPageService implements IResourceDisplayManager
{
    private static final String MARK_ARRONDISSEMENT_VOTE_USER = "arrondissementVote";
    private static final String MARK_ID_DOCUMENT = "id_document";
    private static final String MARK_LOCALISATION = "localisation_text";
    private static final String MARK_THEMATIQUE = "thematique_text";
    private static final String MARK_TITLE = "title_text";
    private static final String MARK_STATUS = "status_document";
    private static final String MARK_CAMPAIGN = "campaign_text_document";

    @Override
    public void getXmlAddOn( StringBuffer strXml, String strResourceType, int nResourceId )
    {
        return;
    }

    @Override
    public void buildPageAddOn( Map<String, Object> model, String strResourceType, int nIdResource, String strPortletId, HttpServletRequest request )
    {

        MyVoteService _myVoteService = SpringContextService.getBean( MyVoteService.BEAN_NAME );
        String arrondissement = null;
        boolean isValidated = false;

        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        if ( user != null )
        {
            arrondissement = BudgetUtils.getArrondissementDisplay( user );
            isValidated = _myVoteService.isUserVoteValidated( user.getName( ) );

        }

        if ( arrondissement != null )
        {

            model.put( MARK_ARRONDISSEMENT_VOTE_USER, arrondissement );

        }
        else
        {

            model.put( MARK_ARRONDISSEMENT_VOTE_USER, "notConnected" );

        }
        Document doc = DocumentHome.findByPrimaryKey( nIdResource );
        // recup localisation
        DocumentAttribute attr = doc.getAttribute( "localisation" );
        String strLocalisationDoc = StringUtils.EMPTY;
        if ( attr != null && StringUtils.isNotEmpty( attr.getTextValue( ) ) )
        {
            strLocalisationDoc = attr.getTextValue( );
        }
        // recup thematique
        attr = doc.getAttribute( "thematique" );
        String strThematiqueDoc = StringUtils.EMPTY;
        if ( attr != null && StringUtils.isNotEmpty( attr.getTextValue( ) ) )
        {
            strThematiqueDoc = attr.getTextValue( );
        }
        // recup title
        attr = doc.getAttribute( "title_idea" );
        String strTitleDoc = StringUtils.EMPTY;
        if ( attr != null && StringUtils.isNotEmpty( attr.getTextValue( ) ) )
        {
            strTitleDoc = attr.getTextValue( );
        }
        // recup status
        attr = doc.getAttribute( "statut_project" );
        String strStatusDoc = StringUtils.EMPTY;
        if ( attr != null && StringUtils.isNotEmpty( attr.getTextValue( ) ) )
        {
            strStatusDoc = attr.getTextValue( );
        }
        // recup campaign
        attr = doc.getAttribute( "campaign" );
        String strCampaignDoc = StringUtils.EMPTY;
        if ( attr != null && StringUtils.isNotEmpty( attr.getTextValue( ) ) )
        {
            strCampaignDoc = attr.getTextValue( );
        }

        model.put( MARK_ID_DOCUMENT, nIdResource );
        model.put( MARK_LOCALISATION, strLocalisationDoc );
        model.put( MARK_THEMATIQUE, strThematiqueDoc );
        model.put( MARK_TITLE, strTitleDoc );
        model.put( MARK_STATUS, strStatusDoc );
        model.put( MARK_CAMPAIGN, strCampaignDoc );
        model.put( BudgetUtils.MARK_CAMPAIGN_SERVICE, CampaignService.getInstance( ) );
        model.put( BudgetUtils.MARK_VOTE_VALIDATED, isValidated );
    }
}
