package fr.paris.lutece.plugins.participatorybudget.service;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.security.UserNotSignedException;

interface IDocumentBodyService {

    public String getPage( HttpServletRequest request, String strDocumentId, String strPortletId, int nMode ) throws UserNotSignedException, SiteMessageException;
}
