/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.participatorybudget.business;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import fr.paris.lutece.plugins.participatorybudget.service.MyInfosService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;


/**
 * MesInfosForm
 */
public class MyInfosForm implements Serializable, Cloneable
{
    private static final long serialVersionUID = 3729142258589861636L;
    private String _strNickname;

    @NotEmpty( message = "budgetparticipatif.validation.civility.notEmpty" )
    private String _strCivility;
	//@NotEmpty( message = "budgetparticipatif.validation.firstname.notEmpty" )
    private String _strFirstname;
    //@NotEmpty( message = "budgetparticipatif.validation.lastname.notEmpty" )
    private String _strLastname;
    private String _strPostalCode;
    @NotEmpty( message = "budgetparticipatif.validation.address.notEmpty" )
    private String _strAddress;
	private Double _dLongitude;
	private Double _dLatitude;
	@NotEmpty( message = "budgetparticipatif.validation.birthdate.notEmpty" )
    @Pattern( regexp = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)" , message = "budgetparticipatif.validation.birthdate.pattern")
    private String _strBirthdate;
	//@NotEmpty( message = "budgetparticipatif.validation.arrondissement.notEmpty" )
    //@Pattern( regexp = "(7500[1-9])|(7501[0-9])|(75020)", message = "budgetparticipatif.validation.arrondissement.pattern" )
    private String _strArrondissement;
    @NotEmpty( message = "budgetparticipatif.validation.iliveinparis.notEmpty" )
    private String _strIliveinparis;
    private boolean _bIsValid;
    //@Min( value = 15 , message = "budgetparticipatif.validation.age.min" )
    private int _nAge;
    private String _strSendaccountvalidation;
    private boolean _bAccountVerified;
    @NotEmpty( message = "budgetparticipatif.validation.geojson.notEmpty" )
    private String _strGeojson;
    
    
    
    /**
     * Returns the Firstname
     * @return The Firstname
     */
    public String getFirstname(  )
    {
        return _strFirstname;
    }

    /**
     * Sets the Firstname
     * @param strFirstname The Firstname
     */
    public void setFirstname( String strFirstname )
    {
        _strFirstname = strFirstname;
    }

    /**
     * Returns the Lastname
     * @return The Lastname
     */
    public String getLastname(  )
    {
        return _strLastname;
    }

    /**
     * Sets the Lastname
     * @param strLastname The Lastname
     */
    public void setLastname( String strLastname )
    {
        _strLastname = strLastname;
    }

    /**
     * Returns the Address
     * @return The Address
     */
    public String getAddress(  )
    {
        return _strAddress;
    }

    /**
     * Sets the Address
     * @param strAddress The Address
     */
    public void setAddress( String strAddress )
    {
        _strAddress = strAddress;
    }

    /**
     * Returns the Birthdate
     * @return The Birthdate
     */
    public String getBirthdate(  )
    {
        return _strBirthdate;
    }

    /**
     * Sets the Birthdate
     * @param strBirthdate The Birthdate
     */
    public void setBirthdate( String strBirthdate ) 
    {
        _strBirthdate = strBirthdate;
        
        // Validate Birth Date format before calculating the age
        Validator validator = BeanValidationUtil.getValidator();
        Set<ConstraintViolation<MyInfosForm>> constraintViolations = validator.validateProperty(this , "_strBirthdate" );
        if( constraintViolations.isEmpty() )
        {
            try
            {
                _nAge = MyInfosService.getAge( strBirthdate );
            }
            catch (ParseException ex)
            {
                _nAge = 0;
                AppLogService.error( "Error setting age from birthdate" + ex.getMessage(), ex );
            }
        }
        else
        {
            _nAge = 18;  // A valid age to not create a violation since the date format is not valid
        }
    }
    
    /**
     * Returns the Age
     * @return The Age
     */
    public int getAge(  )
    {
        return _nAge;
    }

    /**
     * Returns the Arrondissement
     * @return The Arrondissement
     */
    public String getArrondissement(  )
    {
        return _strArrondissement;
    }

    /**
     * Sets the Arrondissement
     * @param strArrondissement The Arrondissement
     */
    public void setArrondissement( String strArrondissement )
    {
        _strArrondissement = strArrondissement;
    }

    /**
     * Returns the Iliveinparis
     * @return The Iliveinparis
     */
    public String getIliveinparis(  )
    {
        return _strIliveinparis;
    }

    /**
     * Sets the Iliveinparis
     * @param strIliveinparis The Iliveinparis
     */
    public void setIliveinparis( String strIliveinparis )
    {
        _strIliveinparis = strIliveinparis;
    }

    /**
     * Check if the user is valid or not
     * @return True if the user is valid
     */
    public boolean getIsValid(  )
    {
        return _bIsValid;
    }

    /**
     * Set the user to valid or not valid
     * @param bIsValid True if the user is valid, false otherwise
     */
    public void setIsValid( boolean bIsValid )
    {
        _bIsValid = bIsValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MyInfosForm clone(  )
    {
        try
        {
            return (MyInfosForm) super.clone(  );
        }
        catch ( CloneNotSupportedException e )
        {
            return null;
        }
    }
	
	/**
	 * Send Account Validation
	 * @param strSendAccountValidation strSendAccountValidation
	 */
	public void setSendaccountvalidation(String strSendAccountValidation)
	{
		
		_strSendaccountvalidation=strSendAccountValidation;
		
	}
	
	/**
     * Returns the SendAccountValidation
     * @return The SendAccountValidation
     */
    public String getSendaccountvalidation(  )
    {
        return _strSendaccountvalidation;
    }

    /**
     * 
     * @return true if the account is verified
     */
	public boolean isAccountVerified() {
		return _bAccountVerified;
	}

	/**
	 * 
	 * @param _bAccountVerified true if the account is verified
	 */
	public void setAccountVerified(boolean _bAccountVerified) {
		this._bAccountVerified = _bAccountVerified;
	}
	
    public String getNickname() {
		return _strNickname;
	}

	public void setNickname(String _strNickname) {
		this._strNickname = _strNickname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCivility() {
		return _strCivility;
	}

	public void setCivility(String strCivility) {
		this._strCivility = strCivility;
	}
	
	/**
	 * 
	 * @return the postal code
	 */
	public String getPostalCode() {
		return _strPostalCode;
	}
	
	/**
	 * 
	 * @param _strPostalCode  the postal code
	 */
	public void setPostalCode(String _strPostalCode) {
		this._strPostalCode = _strPostalCode;
	}
	
	/**
	 * 
	 * @return the longitude
	 */
	public Double getLongitude() {
		return _dLongitude;
	}

	/**
	 * 
	 * @param _dLongitude the longitude
	 */
	public void setLongitude(Double _dLongitude) {
		this._dLongitude = _dLongitude;
	}
	/**
	 * 
	 * @return the latitude
	 */
    public Double getLatitude() {
		return _dLatitude;
	}
 /**
  * 
  * @param _dLatitude the latitude
  */
	public void setLatitude(Double _dLatitude) {
		this._dLatitude = _dLatitude;
	}

public String getGeojson() {
	return _strGeojson;
}

public void setGeojson(String _strGeojson) {
	this._strGeojson = _strGeojson;
}

	
}
