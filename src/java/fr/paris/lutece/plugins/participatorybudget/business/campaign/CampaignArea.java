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
package fr.paris.lutece.plugins.participatorybudget.business.campaign;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;

import java.io.Serializable;

/**
 * This is the business class for the object CampaignArea
 */
public class CampaignArea implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;

    @NotEmpty( message = "#i18n{participatorybudget.validation.campaignarea.CodeCampaign.notEmpty}" )
    @Size( max = 50, message = "#i18n{participatorybudget.validation.campaignarea.CodeCampaign.size}" )
    private String _strCodeCampaign;

    @NotEmpty( message = "#i18n{participatorybudget.validation.campaignarea.Title.notEmpty}" )
    @Size( max = 50, message = "#i18n{participatorybudget.validation.campaignarea.Title.size}" )
    private String _strTitle;

    @Pattern( regexp = "(whole|localized)", message = "#i18n{participatorybudget.validation.campaignarea.Type.pattern}" )
    @NotEmpty( message = "#i18n{participatorybudget.validation.campaignarea.Type.notEmpty}" )
    @Size( max = 50, message = "#i18n{participatorybudget.validation.campaignarea.Type.size}" )
    private String _strType;

    @Min( value = 0, message = "#i18n{participatorybudget.validation.campaignarea.NumberVotes.min}" )
    private int _nNumberVotes;

    private boolean _bActive;

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getNumberVotes( )
    {
        return _nNumberVotes;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setNumberVotes( int numberVotes )
    {
        _nNumberVotes = numberVotes;
    }

    /**
     * Returns the CodeCampaign
     * 
     * @return The CodeCampaign
     */
    public String getCodeCampaign( )
    {
        return _strCodeCampaign;
    }

    /**
     * Sets the CodeCampaign
     * 
     * @param strCodeCampaign
     *            The CodeCampaign
     */
    public void setCodeCampaign( String strCodeCampaign )
    {
        _strCodeCampaign = strCodeCampaign;
    }

    /**
     * Returns the Title
     * 
     * @return The Title
     */
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * Sets the Title
     * 
     * @param strTitle
     *            The Title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    public boolean getActive( )
    {
        return _bActive;
    }

    /**
     * Sets the Active
     * 
     * @param bActive
     *            The Active
     */
    public void setActive( boolean bActive )
    {
        _bActive = bActive;
    }

    public String getType( )
    {
        return _strType;
    }

    /**
     * Sets the Type
     * 
     * @param strType
     *            The Type
     */
    public void setType( String strType )
    {
        _strType = strType;
    }
}
