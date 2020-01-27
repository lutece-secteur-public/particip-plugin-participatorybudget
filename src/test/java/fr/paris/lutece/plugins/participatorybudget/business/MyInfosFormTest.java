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

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Test;

/**
 * MyInfosFormTest
 */
public class MyInfosFormTest extends LuteceTestCase
{

    /**
     * Test of setBirthdate method, of class MyInfosForm.
     */
    @Test
    public void testSetBirthdate( )
    {
        MyInfosForm instance = getValidBean( );

        System.out.println( "setBirthdate - Date format" );
        instance.setBirthdate( "01/01/1960" );
        validate( instance, 0 ); // Valid format
        instance.setBirthdate( "1/1/1960" );
        validate( instance, 0 ); // Valid format
        instance.setBirthdate( "10/21/2010" );
        validate( instance, 1 ); // Wrong format : month > 12
        instance.setBirthdate( "10-10-2010" );
        validate( instance, 1 ); // Wrong format : bad separator
    }

    private void validate( MyInfosForm instance, int nViolationCount )
    {
        Set<ConstraintViolation<MyInfosForm>> listErrors = BeanValidationUtil.validate( instance );
        for ( ConstraintViolation<MyInfosForm> cv : listErrors )
        {
            System.out.println( "- constraint violation : " + I18nService.getLocalizedString( cv.getMessage( ), Locale.FRENCH ) );
        }
        assertEquals( listErrors.size( ), nViolationCount );
    }

    private MyInfosForm getValidBean( )
    {
        MyInfosForm bean = new MyInfosForm( );
        bean.setAddress( "Address" );
        bean.setFirstname( "John" );
        bean.setFirstname( "John" );
        bean.setLastname( "Doe" );
        bean.setCivility( "MME" );
        bean.setArrondissement( "75001" );
        bean.setBirthdate( "10/10/1960" );
        bean.setIliveinparis( "on" );
        return bean;

    }

}
