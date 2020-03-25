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

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * This is the business class for the object CampagnePhaseType
 */
public class CampagnePhaseType implements Serializable
{
	private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;

    @NotEmpty( message = "#i18n{participatorybudget.validation.campagnephasetype.Code.notEmpty}" )
    @Size( max = 50, message = "#i18n{participatorybudget.validation.campagnephasetype.Code.size}" )
    private String _strCode;

    @NotEmpty( message = "#i18n{participatorybudget.validation.campagnephasetype.Label.notEmpty}" )
    @Size( max = 255, message = "#i18n{participatorybudget.validation.campagnephasetype.Label.size}" )
    private String _strLabel;

    @NotEmpty( message = "#i18n{participatorybudget.validation.campagnephasetype.Order.notEmpty}" )
    private int _nOrder;
    
    // *********************************************************************************************
    // * GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET G *
    // * GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET GET-SET G *
    // *********************************************************************************************

    public int getId() {
		return _nId;
	}

	public void setId(int _nId) {
		this._nId = _nId;
	}

	public String getCode() {
		return _strCode;
	}

	public void setCode(String _strCode) {
		this._strCode = _strCode;
	}

	public String getLabel() {
		return _strLabel;
	}

	public void setLabel(String _strLabel) {
		this._strLabel = _strLabel;
	}

	public int getOrder() {
		return _nOrder;
	}

	public void setOrder(int _strOrder) {
		this._nOrder = _strOrder;
	}

}
