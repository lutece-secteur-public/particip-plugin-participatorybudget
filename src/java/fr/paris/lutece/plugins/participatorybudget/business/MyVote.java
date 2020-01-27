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
package fr.paris.lutece.plugins.participatorybudget.business;

import fr.paris.lutece.plugins.document.business.Document;

import java.util.Date;

/**
 * Class that represents the rating of a document from a user
 */
public class MyVote
{
    private Document _document;
    private Date _dateVote;
    private int _nNbTotVotes;
    private int _nNbTotVotesToutParis;
    private int _nNbTotVotesArrondissement;
    private String _strArrondissementUser;
    private boolean _bVoteValidated;

    /**
     * Creates a default vote
     */
    public MyVote( )
    {
        // Do nothing
    }

    /**
     * Creates a new vote
     * 
     * @param document
     *            The rated document
     * @param dateVote
     *            The date of the rating
     * @param nNbTotVotes
     *            The total number of votes for this document
     */
    public MyVote( Document document, Date dateVote, int nNbTotVotes )
    {
        this._document = document;
        this._dateVote = dateVote;
        this._nNbTotVotes = nNbTotVotes;
    }

    /**
     * Creates a new vote
     * 
     * @param document
     *            The rated document
     * @param dateVote
     *            The date of the rating
     * @param nNbTotVotes
     *            The total number of votes for this document
     */
    public MyVote( Document document, Date dateVote, int nNbTotVotes, int nNbVotesToutParis, int nNbVoteArrondissement )
    {
        this._document = document;
        this._dateVote = dateVote;
        this._nNbTotVotes = nNbTotVotes;
        this._nNbTotVotesArrondissement = nNbVoteArrondissement;
        this._nNbTotVotesToutParis = nNbVotesToutParis;
    }

    /**
     * Get the rated document
     * 
     * @return The rated document
     */
    public Document getDocument( )
    {
        return _document;
    }

    /**
     * Set the rated document
     * 
     * @param document
     *            The rated document
     */
    public void setDocument( Document document )
    {
        this._document = document;
    }

    /**
     * Get the rating date
     * 
     * @return The rating date
     */
    public Date getDateVote( )
    {
        return _dateVote;
    }

    /**
     * Set the rating date
     * 
     * @param dateVote
     *            The rating date
     */
    public void setDateVote( Date dateVote )
    {
        this._dateVote = dateVote;
    }

    /**
     * Get the total number of votes for the document
     * 
     * @return The total number of votes for the document
     */
    public int getNbTotVotes( )
    {
        return _nNbTotVotes;
    }

    /**
     * Set the total number of votes for the document
     * 
     * @param nNbTotVotes
     *            The total number of votes for the document
     */
    public void setNbTotVotes( int nNbTotVotes )
    {
        this._nNbTotVotes = nNbTotVotes;
    }

    /**
     * Get the total number of votes in TOUT paris for the document
     * 
     * @return The total number of votes in tout paris for the document
     */
    public int getTotVotesToutParis( )
    {
        return _nNbTotVotesToutParis;
    }

    /**
     * Set the total number of votes in tout paris for the document
     * 
     * @param nNbTotVotes
     *            The total number of votes in tout paris for the document
     */
    public void setNbTotVotesToutParis( int nNbTotVotesToutParis )
    {
        this._nNbTotVotesToutParis = nNbTotVotesToutParis;
    }

    /**
     * Get the total number of votes in TOUT paris for the document
     * 
     * @return The total number of votes in tout paris for the document
     */
    public int getTotVotesArrondissement( )
    {
        return _nNbTotVotesArrondissement;
    }

    /**
     * Set the total number of votes in Arrondissement for the document
     * 
     * @param nNbTotVotes
     *            The total number of votes in Arrondissementfor the document
     */
    public void setNbTotVotesArrondissement( int nNbTotVotesArrondissement )
    {
        this._nNbTotVotesArrondissement = nNbTotVotesArrondissement;
    }

    /**
     * 
     * @return
     */
    public String getArrondissementUser( )
    {
        return this._strArrondissementUser;
    }

    /**
     * 
     * @param strArrondissementUser
     */
    public void setArrondissementUser( String strArrondissementUser )
    {
        this._strArrondissementUser = strArrondissementUser;
    }

    /**
     * 
     */
    public boolean isVoteVlidated( )
    {
        return this._bVoteValidated;
    }

    /**
     * 
     */
    public void setVoteVlidated( boolean bool )
    {
        _bVoteValidated = bool;
    }

}
