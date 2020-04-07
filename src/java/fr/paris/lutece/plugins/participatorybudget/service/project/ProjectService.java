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
package fr.paris.lutece.plugins.participatorybudget.service.project;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentType;
import fr.paris.lutece.plugins.document.business.DocumentTypeHome;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpace;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpaceHome;
import fr.paris.lutece.plugins.document.business.workflow.DocumentAction;
import fr.paris.lutece.plugins.document.business.workflow.DocumentActionHome;
import fr.paris.lutece.plugins.document.business.workflow.DocumentState;
import fr.paris.lutece.plugins.document.modules.solr.indexer.SolrDocIndexer;
import fr.paris.lutece.plugins.document.service.DocumentException;
import fr.paris.lutece.plugins.document.service.DocumentService;
import fr.paris.lutece.plugins.document.service.publishing.PublishingService;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.follow.service.extender.FollowResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.participatorybudget.util.ParticipatoryBudgetConstants;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class ProjectService implements IProjectService
{

    private static final String DEFAULT_ADMIN = "admin";

    // ***********************************************************************************
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // ***********************************************************************************

    private static final String BEAN_PROJECT_SERVICE = "participatorybudget.projectService";

    private static ProjectService _singleton;

    @Inject
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;

    public static IProjectService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_PROJECT_SERVICE );
        }
        return _singleton;
    }

    // ***********************************************************************************
    // * CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CR *
    // * CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CR *
    // ***********************************************************************************

    @Override
    public int createproject( String title, String summary, Timestamp validityBegin, int portletId, Map<String, String> docFields ) throws DocumentException
    {
        // Get the document space where to store the document. Create it if necessary.
        int idSpace = getOrCreateIdSpace( docFields.get( ParticipatoryBudgetConstants.DOCUMENT_ATTRIBUTE_CAMPAIGN ) );

        // Construct then store the document.
        Document doc = new Document( );
        doc.setCodeDocumentType( ParticipatoryBudgetConstants.DOCUMENT_TYPE_PROJECT );
        doc.setSpaceId( idSpace );
        doc.setStateId( 1 );
        doc.setPageTemplateDocumentId( 1 );
        doc.setSkipPortlet( true );
        doc.setSkipCategories( true );

        DocumentType documentType = DocumentTypeHome.findByPrimaryKey( ParticipatoryBudgetConstants.DOCUMENT_TYPE_PROJECT );
        List<DocumentAttribute> attributes = documentType.getAttributes( );
        doc.setAttributes( attributes );

        doc.setTitle( title );
        doc.setSummary( summary );
        doc.setDateValidityBegin( validityBegin );

        // Searching for attributes values in the Map, else put empty string.
        for ( DocumentAttribute attribute : attributes )
        {
            String code = attribute.getCode( );
            String value = ( docFields.containsKey( code ) ? docFields.get( code ) : StringUtils.EMPTY );
            doc.getAttribute( attribute.getCode( ) ).setTextValue( value == null ? "" : value );
        }

        doc.setPublishedStatus( 0 );

        AdminUser defaultAdmin = AdminUserHome.findUserByLogin( DEFAULT_ADMIN );
        DocumentService.getInstance( ).createDocument( doc, defaultAdmin );

        // Approve and validate document.
        DocumentService.getInstance( ).changeDocumentState( doc, defaultAdmin, DocumentState.STATE_IN_CHANGE );

        DocumentAction actionApprobation = DocumentActionHome.findByPrimaryKey( DocumentAction.ACTION_SUBMIT_CHANGE );
        DocumentService.getInstance( ).changeDocumentState( doc, defaultAdmin, actionApprobation.getFinishDocumentState( ).getId( ) );

        DocumentAction actionValidation = DocumentActionHome.findByPrimaryKey( DocumentAction.ACTION_VALIDATE_CHANGE );
        DocumentService.getInstance( ).validateDocument( doc, defaultAdmin, actionValidation.getFinishDocumentState( ).getId( ) );

        // Publish and indexing document if portletId > 0
        if ( portletId > 0 )
        {
            PublishingService.getInstance( ).assign( doc.getId( ), portletId );
            PublishingService.getInstance( ).publish( doc.getId( ), portletId );
            PublishingService.getInstance( ).changeDocumentOrder( doc.getId( ), portletId, 1 );

            // Add document to sorl incremental index actions.
            SolrDocIndexer indexer = SpringContextService.getBean( SolrDocIndexer.BEAN_NAME );
            Portlet portlet = PortletHome.findByPrimaryKey( portletId );
            if ( portlet == null )
            {
                AppLogService.error( "Unable to find portlet #" + portletId + " ! Documents not indexed." );
            }
            else
            {
                try
                {
                    indexer.indexListDocuments( portlet, Arrays.asList( doc.getId( ) ) );
                }
                catch( Exception e )
                {
                    AppLogService.error( "Error when indexing document #" + doc.getId( ) + " with SOLR", e );
                }
            }
        }

        return doc.getId( );
    }

    /**
     * Returns the id of the searched document space. Creates it if not existing.
     */
    private int getOrCreateIdSpace( String nameSpaceChild )
    {
        // Search for child space.
        List<DocumentSpace> docSpace = DocumentSpaceHome.findChilds( ParticipatoryBudgetConstants.DOCUMENT_PROJECT_PARENT_SPACE_ID );
        for ( DocumentSpace item : docSpace )
        {
            if ( nameSpaceChild.equals( item.getName( ) ) )
            {
                return item.getId( );
            }
        }

        // Child not found, so create it.
        DocumentSpace childSpace = new DocumentSpace( );
        childSpace.setIdParent( ParticipatoryBudgetConstants.DOCUMENT_PROJECT_PARENT_SPACE_ID );
        childSpace.setName( nameSpaceChild );
        childSpace.setDescription( nameSpaceChild );
        childSpace.setViewType( "detail" );
        childSpace.setIdIcon( 2 );
        childSpace.resetAllowedDocumentTypesList( );
        childSpace.setDocumentCreationAllowed( true );
        childSpace.setWorkgroup( "all" );
        childSpace.addAllowedDocumentType( ParticipatoryBudgetConstants.DOCUMENT_TYPE_PROJECT );
        DocumentSpaceHome.create( childSpace );

        return childSpace.getId( );
    }

    // *********************************************************************************************
    // * FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLO *
    // * FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLO *
    // *********************************************************************************************

    @Override
    public Set<String> getUniqueUserGuidsProjectFollowers( Document project )
    {
        Set<String> userGuids = new HashSet<String>( );

        ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter( );

        filter.setExtenderType( FollowResourceExtender.RESOURCE_EXTENDER );
        filter.setExtendableResourceType( "document" );
        filter.setIdExtendableResource( String.valueOf( project.getId( ) ) );

        List<ResourceExtenderHistory> listHistories = _resourceExtenderHistoryService.findByFilter( filter );

        for ( ResourceExtenderHistory followerHistory : listHistories )
        {
            userGuids.add( followerHistory.getUserGuid( ) );
        }

        return userGuids;
    }

}
