/*
 * Copyright (c) 2002-2015, Mairie de Paris
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
 * This class provides instances management methods (create, find, ...) for CampagneTheme objects
 */

public final class CampagneThemeHome
{
    // Static variable pointed at the DAO instance

    private static ICampagneThemeDAO _dao = SpringContextService.getBean( "participatorybudget.campagneThemeDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatorybudget" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private CampagneThemeHome(  )
    {
    }

    /**
     * Create an instance of the campagneTheme class
     * @param campagneTheme The instance of the CampagneTheme which contains the informations to store
     * @return The  instance of campagneTheme which has been created with its primary key.
     */
    public static CampagneTheme create( CampagneTheme campagneTheme )
    {
        _dao.insert( campagneTheme, _plugin );

        return campagneTheme;
    }

    /**
     * Update of the campagneTheme which is specified in parameter
     * @param campagneTheme The instance of the CampagneTheme which contains the data to store
     * @return The instance of the  campagneTheme which has been updated
     */
    public static CampagneTheme update( CampagneTheme campagneTheme )
    {
        _dao.store( campagneTheme, _plugin );

        return campagneTheme;
    }

    /**
     * Remove the campagneTheme whose identifier is specified in parameter
     * @param nKey The campagneTheme Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a campagneTheme whose identifier is specified in parameter
     * @param nKey The campagneTheme primary key
     * @return an instance of CampagneTheme
     */
    public static CampagneTheme findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }
    
    /**
     * Returns an instance of a campagneTheme whose identifier is specified in parameter
     * @param codeTheme The codeTheme
     * @return an instance of CampagneTheme
     */
    public static CampagneTheme findByCodeTheme( String codeTheme )
    {
        return _dao.loadByCodeTheme(codeTheme, _plugin);
    }

    /**
     * Load the data of all the campagneTheme objects and returns them in form of a collection
     * @return the collection which contains the data of all the campagneTheme objects
     */
    public static Collection<CampagneTheme> getCampagneThemesList( )
    {
        return _dao.selectCampagneThemesList( _plugin );
    }
    
    /**
     * Load the id of all the campagneTheme objects and returns them in form of a collection
     * @return the collection which contains the id of all the campagneTheme objects
     */
    public static Collection<Integer> getIdCampagneThemesList( )
    {
        return _dao.selectIdCampagneThemesList( _plugin );
    }

    /**
     * Load the data of all the campagneTheme objects for a campagne and returns them in form of a collection
     * @return the collection which contains the data of all the campagneTheme objects
     */
    public static Collection<CampagneTheme> getCampagneThemesListByCampagne( String codeCampagne )
    {
        return _dao.selectCampagneThemesListByCampagne( codeCampagne, _plugin );
    }

    /**
     * Load the data of all the campagneTheme objects mapped from campagne code and returns them in form of a map
     * @return the collection which contains the data of all the campagneTheme objects
     */
    public static Map<String, List<CampagneTheme>> getCampagneThemesMapByCampagne( )
    {
        return _dao.selectCampagneThemesMapByCampagne( _plugin );
    }
}

