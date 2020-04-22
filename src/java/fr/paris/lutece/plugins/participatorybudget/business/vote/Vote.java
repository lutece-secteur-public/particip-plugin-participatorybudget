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
package fr.paris.lutece.plugins.participatorybudget.business.vote;

import java.sql.Timestamp;

/**
 * This is the business class for the object Votes
 */
public class Vote
{
    // Variables declarations
    private int id;

    private String _strUserId;
    private Timestamp _dateVote;
    private int _nArrondissement;
    private int _nAge;
    private String _strBirthDate;
    private int _nProjetId;
    private String _strIpAddress;
    private String _strTitle;
    private String _strLocation;
    private String _strTheme;
    private int _nStatus;
    private int _nStatusExportStats;

    /**
     * Status of Vote
     */
    public enum Status
    {

        STATUS_VALIDATED( 1, "participatorybudget.message.labelStatusValidated" ),
        STATUS_NOT_VALIDATED( 0, "participatorybudget.message.labelStatusNotValidated" );

        private final int nValue;
        private final String strLibelle;

        Status( int nValeur, String strMessage )
        {
            this.nValue = nValeur;
            this.strLibelle = strMessage;
        }

        public int getValeur( )
        {
            return this.nValue;
        }

        public String getLibelle( )
        {
            return this.strLibelle;
        }
    }

    public int getId( )
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    /**
     * Returns the DateVote
     * 
     * @return The DateVote
     */
    public Timestamp getDateVote( )
    {
        return _dateVote;
    }

    /**
     * Sets the DateVote
     * 
     * @param dateVote
     *            The DateVote
     */
    public void setDateVote( Timestamp dateVote )
    {
        _dateVote = dateVote;
    }

    /**
     * Returns the Arrondissement
     * 
     * @return The Arrondissement
     */
    public int getArrondissement( )
    {
        return _nArrondissement;
    }

    /**
     * Sets the Arrondissement
     * 
     * @param nArrondissement
     *            The Arrondissement
     */
    public void setArrondissement( int nArrondissement )
    {
        _nArrondissement = nArrondissement;
    }

    /**
     * Returns the Age
     * 
     * @return The Age
     */
    public int getAge( )
    {
        return _nAge;
    }

    /**
     * Sets the Age
     * 
     * @param nAge
     *            The Age
     */
    public void setAge( int nAge )
    {
        _nAge = nAge;
    }

    /**
     * Returns the IdUser
     * 
     * @return The IdUser
     */
    public String getUserId( )
    {
        return _strUserId;
    }

    /**
     * Sets the IdUser
     * 
     * @param nIdUser
     *            The IdUser
     */
    public void setUserId( String strIdUser )
    {
        _strUserId = strIdUser;
    }

    /**
     * Returns the IdProjet
     * 
     * @return The IdProjet
     */
    public int getProjetId( )
    {
        return _nProjetId;
    }

    /**
     * Sets the IdProjet
     * 
     * @param nIdProjet
     *            The IdProjet
     */
    public void setProjetId( int nIdProjet )
    {
        _nProjetId = nIdProjet;
    }

    /**
     * 
     * @return the birhday date
     */
    public String getBirthDate( )
    {
        return _strBirthDate;
    }

    /**
     * 
     * @param strBirthDate
     *            the birhday date
     */
    public void setBirthDate( String strBirthDate )
    {
        this._strBirthDate = strBirthDate;
    }

    /**
     * 
     * @return the user if adress
     */
    public String getIpAddress( )
    {
        return _strIpAddress;
    }

    /**
     * 
     * @param strIpAdress
     *            the user if adress
     */
    public void setIpAddress( String strIpAdress )
    {
        this._strIpAddress = strIpAdress;
    }

    /**
     * 
     * @return the project title
     */
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * 
     * @param strTitle
     *            the project title
     */
    public void setTitle( String strTitle )
    {
        this._strTitle = strTitle;
    }

    /**
     * Returns the project location
     * 
     * @return The project location
     */
    public String getLocation( )
    {
        return _strLocation;
    }

    /**
     * Sets the nLocation
     * 
     * @param nLocation
     *            The project location
     */
    public void setLocation( String nLocation )
    {
        _strLocation = nLocation;
    }

    /**
     * 
     * @param strTheme
     *            the uproject theme
     */
    public void setTheme( String strTheme )
    {
        this._strTheme = strTheme;
    }

    /**
     * 
     * @return the project theme
     */
    public String getTheme( )
    {
        return _strTheme;
    }

    /**
     * Returns the project Status
     * 
     * @return The project Status
     */
    public int geStatus( )
    {
        return _nStatus;
    }

    /**
     * Sets the Status
     * 
     * @param nLocation
     *            The project Status
     */
    public void setStatus( int nStatus )
    {
        _nStatus = nStatus;
    }

    /**
     * GET StatusExportStats
     * 
     * @return
     */
    public int getStatusExportStats( )
    {
        return _nStatusExportStats;
    }

    /**
     * SET StatusExportStats
     * 
     * @param nStatusExportStats
     */
    public void setStatusExportStats( int nStatusExportStats )
    {
        this._nStatusExportStats = nStatusExportStats;
    }

}
