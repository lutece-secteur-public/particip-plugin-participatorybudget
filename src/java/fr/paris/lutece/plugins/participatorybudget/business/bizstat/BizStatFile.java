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
package fr.paris.lutece.plugins.participatorybudget.business.bizstat;

import java.io.Serializable;
import java.sql.Timestamp;

import fr.paris.lutece.portal.business.user.AdminUser;

/**
 *
 * class PhysicalFile
 *
 */
public class BizStatFile implements Serializable
{
	
	public static String STATUS_REQUESTED       = "requested";
	public static String STATUS_UNDER_TREATMENT = "(being processed...)";
	public static String STATUS_AVAILABLE       = "available";
	public static String STATUS_PURGED          = "purged";
	public static String STATUT_ERROR           = "error";
	
    private int       _nIdBizStatFile;
    private String    _strStatus;
    private String    _strFileName;
    private int       _nIdAdminUser;
	private String    _strAdminUserAccessCode;
    private String    _strAdminUserEmail;
    private String    _strReason;
    private String    _strDescription;
    private String    _strError;
    private Timestamp _tsCreationDate;
    private byte [ ]  _byValue;
    private int       _nContentSize;
    
	public int getIdBizStatFile() {
		return _nIdBizStatFile;
	}
	
	public void setIdBizStatFile(int _nIdBizStatFile) {
		this._nIdBizStatFile = _nIdBizStatFile;
	}
	
	public String getStatus() {
		return _strStatus;
	}
	
	public void setStatus(String _strStatus) {
		this._strStatus = _strStatus;
	}
	
	public String getFileName() {
		return _strFileName;
	}
	
	public void setFileName(String _strFileName) {
		this._strFileName = _strFileName;
	}
	
    public int getIdAdminUser() {
		return _nIdAdminUser;
	}

	public void setIdAdminUser(int _nIdAdminUser) {
		this._nIdAdminUser = _nIdAdminUser;
	}

	public String getAdminUserAccessCode() {
		return _strAdminUserAccessCode;
	}

	public void setAdminUserAccessCode(String _strAdminUserAccessCode) {
		this._strAdminUserAccessCode = _strAdminUserAccessCode;
	}

	public String getAdminUserEmail() {
		return _strAdminUserEmail;
	}

	public void setAdminUserEmail(String _strAdminUserEmail) {
		this._strAdminUserEmail = _strAdminUserEmail;
	}

	public String getReason() {
		return _strReason;
	}

	public void setReason(String _strReason) {
		this._strReason = _strReason;
	}

	public String getDescription() {
		return _strDescription;
	}
	
	public void setDescription(String _strDescription) {
		this._strDescription = _strDescription;
	}
	
	public String getError() {
		return _strError;
	}
	
	public void setError(String _strError) {
		this._strError = _strError;
	}
	
	public Timestamp getCreationDate() {
		return _tsCreationDate;
	}
	
	public void setCreationDate(Timestamp _tsCreationDate) {
		this._tsCreationDate = _tsCreationDate;
	}
	
	public byte[] getValue() {
		return _byValue;
	}
	
	public void setValue(byte[] _byValue) {
		this._byValue = _byValue;
	}

	public int getContentSize() {
		return _nContentSize;
	}

	public void setContentSize(int _nContentSize) {
		this._nContentSize = _nContentSize;
	}

}