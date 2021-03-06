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
package fr.paris.lutece.plugins.participatorybudget.web.rs;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * AbstractServiceRestTest
 */
public class AbstractServiceRestTest extends LuteceTestCase
{
    private static final String STATUS = "status";
    private static final String RESULT = "result";
    private static final String COMPLEMENT = "complement";

    private static final String JSON_RETURN_1 = "{\"status\":\"status\",\"result\":\"result\",\"complement\":\"\"}";
    private static final String JSON_RETURN_2 = "{\"status\":\"status\",\"result\":\"result\",\"complement\":\"complement\"}";

    /**
     * Test of Authentication verification
     */
    @Test
    public void testIsRequestAuthenticated( )
    {
        MockAbstractServiceRest service = new MockAbstractServiceRest( );

        assertFalse( service.isRequestAuthenticated( new MockHttpServletRequest( ) ) );
    }

    /**
     * Test of Json String providers
     */
    @Test
    public void testFormatJson( )
    {
        MockAbstractServiceRest service = new MockAbstractServiceRest( );

        assertEquals( JSON_RETURN_1, service.formatJson( STATUS, RESULT ) );
        assertEquals( JSON_RETURN_2, service.formatJson( STATUS, RESULT, COMPLEMENT ) );

    }

}
