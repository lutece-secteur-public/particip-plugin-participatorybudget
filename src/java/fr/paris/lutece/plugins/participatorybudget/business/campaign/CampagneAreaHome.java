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

import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.Collection;

/**
 * This class provides instances management methods (create, find, ...) for CampagneArea objects
 */

public final class CampagneAreaHome
{
    // Static variable pointed at the DAO instance

    private static ICampagneAreaDAO _dao = SpringContextService.getBean( "participatorybudget.campagneAreaDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatorybudget" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private CampagneAreaHome( )
    {
    }

    /**
     * Create an instance of the campagneArea class
     * 
     * @param campagneArea
     *            The instance of the CampagneArea which contains the informations to store
     * @return The instance of campagneArea which has been created with its primary key.
     */
    public static CampagneArea create( CampagneArea campagneArea )
    {
        _dao.insert( campagneArea, _plugin );

        return campagneArea;
    }

    /**
     * Update of the campagneAre which is specified in parameter
     * 
     * @param campagneArea
     *            The instance of the CampagneArea which contains the data to store
     * @return The instance of the campagneArea which has been updated
     */
    public static CampagneArea update( CampagneArea campagneArea )
    {
        _dao.store( campagneArea, _plugin );

        return campagneArea;
    }

    /**
     * Remove the campagneArea whose identifier is specified in parameter
     * 
     * @param nKey
     *            The campagneArea Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a campagneArea whose identifier is specified in parameter
     * 
     * @param nKey
     *            The campagneArea primary key
     * @return an instance of CampagneArea
     */
    public static CampagneArea findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the campagneArea objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the campagneArea objects
     */
    public static Collection<CampagneArea> getCampagneAreasList( )
    {
        return _dao.selectCampagneAreasList( _plugin );
    }

    /**
     * Load the id of all the campagneArea objects and returns them in form of a collection
     * 
     * @return the collection which contains the id of all the campagneArea objects
     */
    public static Collection<Integer> getIdCampagneAreasList( )
    {
        return _dao.selectIdCampagneAreasList( _plugin );
    }

    /**
     * Load the data of all the campagneArea objects for a campagne and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the campagneArea objects
     */
    public static Collection<CampagneArea> getCampagneAreasListByCampagne( String codeCampagne )
    {
        return _dao.selectCampagneAreasListByCampagne( codeCampagne, _plugin );
    }

    /**
     * Load the data of all the campagneArea objects mapped from campagne code and returns them in form of a map
     * 
     * @return the collection which contains the data of all the campagneArea objects
     */
    public static Map<String, List<CampagneArea>> getCampagneAreasMapByCampagne( )
    {
        return _dao.selectCampagneAreasMapByCampagne( _plugin );
    }
}
