/* **************************************************************************
 * $Id: ExtendedResponseFactory.java,v 1.2 2000/07/27 22:04:09 javed Exp $
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
package com.novell.ldap.ext; 

import com.novell.ldap.*;
import java.io.IOException; 
/**
 *
 *  This factory class takes an LDAPExtendedResponse and returns 
 *  an object (that implements the base class ParsedExtendedResponse)
 *  based on the OID.  You can then call methods defined in the child
 *  class to parse the contents of the response.  The methods available
 *  depend on the child class. All child classes inherit from the 
 *  ParsedExtendedResponse.
 *
 */
public class ExtendedResponseFactory {
    
    static public ParsedExtendedResponse parseExtendedResponse(LDAPExtendedResponse inResponse) 
            throws LDAPException {
                
        // Get the oid stored in the Extended response
        String inOID = inResponse.getID();
        
        try {
            if (inOID.equals(NamingContextConstants.NAMING_CONTEXT_COUNT_RES)) {
                return new NamingContextEntryCountResponse(inResponse);
            }
            if (inOID.equals(NamingContextConstants.GET_IDENTITY_NAME_RES) ) {
                return new GetContextIdentityNameResponse(inResponse);
            }
            else
                throw new LDAPException("Unsupported OID in LDAPResponse", 
                    LDAPException.DECODING_ERROR);
        }
        
        catch(IOException ioe) {
			throw new LDAPException("Error Decoding respnseValue", 
                    LDAPException.DECODING_ERROR);
		}
    }
}
