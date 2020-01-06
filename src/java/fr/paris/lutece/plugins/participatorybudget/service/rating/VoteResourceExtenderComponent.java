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
package fr.paris.lutece.plugins.participatorybudget.service.rating;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfig;
import fr.paris.lutece.plugins.extend.util.ExtendErrorException;
import fr.paris.lutece.plugins.extend.web.component.AbstractResourceExtenderComponent;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

/**
 *  VoteResourceExtenderComponent : Fake ExtenderComponent not used by the Extender
 */
public class VoteResourceExtenderComponent extends AbstractResourceExtenderComponent
{

    @Override
    public void buildXmlAddOn(String strIdExtendableResource, String strExtendableResourceType, String strParameters, StringBuffer strXml)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getPageAddOn(String strIdExtendableResource, String strExtendableResourceType, String strParameters, HttpServletRequest request)
    {
        throw new UnsupportedOperationException("Not supported.");    }

    @Override
    public String getConfigHtml(ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getInfoHtml(ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public IExtenderConfig getConfig(int nIdExtender)
    {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void doSaveConfig(HttpServletRequest request, IExtenderConfig config) throws ExtendErrorException
    {
        throw new UnsupportedOperationException("Not supported.");
    }
    
}
