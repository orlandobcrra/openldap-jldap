/* **************************************************************************
 * $Novell: /ldap/src/jldap/com/novell/ldap/LDAPExtendedResponse.java,v 1.16 2000/09/29 15:17:14 judy Exp $
 *
 * Copyright (C) 1999, 2000 Novell, Inc. All Rights Reserved.
 * 
 * THIS WORK IS SUBJECT TO U.S. AND INTERNATIONAL COPYRIGHT LAWS AND
 * TREATIES. USE, MODIFICATION, AND REDISTRIBUTION OF THIS WORK IS SUBJECT
 * TO VERSION 2.0.1 OF THE OPENLDAP PUBLIC LICENSE, A COPY OF WHICH IS
 * AVAILABLE AT HTTP://WWW.OPENLDAP.ORG/LICENSE.HTML OR IN THE FILE "LICENSE"
 * IN THE TOP-LEVEL DIRECTORY OF THE DISTRIBUTION. ANY USE OR EXPLOITATION
 * OF THIS WORK OTHER THAN AS AUTHORIZED IN VERSION 2.0.1 OF THE OPENLDAP
 * PUBLIC LICENSE, OR OTHER PRIOR WRITTEN CONSENT FROM NOVELL, COULD SUBJECT
 * THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY. 
 ***************************************************************************/

package com.novell.ldap;

import java.io.IOException;
import com.novell.ldap.LDAPResponse;
import com.novell.ldap.protocol.*;

/**
 *
 *  Encapsulates the response returned by an LDAP server on an
 *  asynchronous extended operation request.  It extends LDAPResponse.
 *  
 *  The response can contain the OID of the extension, an octet string
 *  with the operation's data, both, or neither.
 *
 */
public class LDAPExtendedResponse extends LDAPResponse {

    /**
     * Creates an LDAPExtendedResponse object which encapsulates
     * a server response to an asynchronous extended operation request.
     *
     * @param message  The LDAPMessage to convert to an 
     *                 LDAPExtendedResponse object.
     */
    public LDAPExtendedResponse(com.novell.ldap.protocol.LDAPMessage message)
    {
        super(message);
    }

    /**
     * Returns the message identifier of the response.
     *
     * @return OID of the response.
     */
    public String getID()
    {
        return((ExtendedResponse)message.getProtocolOp()).getResponseName().getString();
    }

    /**
     * Returns the value part of the response in raw bytes.
     *
     * @return The value of the response.
     */
    public byte[] getValue()
    {
        return((ExtendedResponse)message.getProtocolOp()).getResponse().getContent();
    }

}

