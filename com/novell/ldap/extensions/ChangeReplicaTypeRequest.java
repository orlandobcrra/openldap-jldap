/* **************************************************************************
 * $Id: ChangeReplicaTypeRequest.java,v 1.12 2000/10/04 22:39:32 judy Exp $
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
package com.novell.ldap.extensions; 

import com.novell.ldap.*;
import com.novell.ldap.asn1.*;
import java.io.*;
 
/**
 *
 *  Changes the type of the replica that resides 
 *  on the specified directory server.  
 *
 *  <p>To change a replica's type, you must create an instance of this class and 
 *  then call the extendedOperation method with this object as the required 
 *  LDAPExtendedOperation parameter.</p>
 *
 *  <p>The changeReplicaTypeRequest extension uses the following OID:<br>
 *  &nbsp;&nbsp;&nbsp;2.16.840.1.113719.1.27.100.15</p>
 *
 *  <p>The requestValue has the following format:<br>
 *
 *  requestValue ::=<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp; flags &nbsp;&nbsp;&nbsp;&nbsp;       INTEGER<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp; replicaType&nbsp;&nbsp;&nbsp;&nbsp;  INTEGER<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp; serverName&nbsp;&nbsp;&nbsp;&nbsp;   LDAPDN<br>
 *  &nbsp;&nbsp;&nbsp;&nbsp; dn&nbsp;&nbsp;&nbsp;&nbsp;           LDAPDN</p>
 */
public class ChangeReplicaTypeRequest extends LDAPExtendedOperation {
   
/**
 *
 * Constructs a new extended operation object for changing a replica's type.   
 *
 * @param dn          The distinguished name of the replica's
 *                    partition root.
 *<br><br>
 * @param serverDN    The server on which the replica resides.
 * <br><br>
 *      
 * @param replicaType    The new replica type. The replica types are defined 
 *                       in the NamingContextConstants class.
 *<br><br>
 * @param flags   Specifies whether all servers in the replica ring must be up  
 *                before proceeding. When set to zero, the status of the servers is 
 *                not checked. When set to LDAP_ENSURE_SERVERS_UP, all servers must be 
 *                up for the operation to proceed.
 *
 * @exception LDAPException A general exception which includes an error message 
 *                          and an LDAP error code.
 *
 * @see NamingContextConstants#LDAP_RT_MASTER
 * @see NamingContextConstants#LDAP_RT_SECONDARY
 * @see NamingContextConstants#LDAP_RT_READONLY
 * @see NamingContextConstants#LDAP_RT_SUBREF
 * @see NamingContextConstants#LDAP_RT_SPARSE_WRITE
 * @see NamingContextConstants#LDAP_RT_SPARSE_READ
 */   
 public ChangeReplicaTypeRequest(String dn, String serverDN, int replicaType, int flags) 
                throws LDAPException {
        
        super(NamingContextConstants.CHANGE_REPLICA_TYPE_REQ, null);
        
        try {
            
            if ( (dn == null) || (serverDN == null) )
                throw new LDAPException("Invalid parameter",
                                    LDAPException.PARAM_ERROR);
                                    
         ByteArrayOutputStream encodedData = new ByteArrayOutputStream();
         LBEREncoder encoder  = new LBEREncoder();
                                                 
          ASN1Integer asn1_flags = new ASN1Integer(flags);
          ASN1Integer asn1_replicaType = new ASN1Integer(replicaType);
          ASN1OctetString asn1_serverDN = new ASN1OctetString(serverDN);
          ASN1OctetString asn1_dn = new ASN1OctetString(dn);
            
            asn1_flags.encode(encoder, encodedData);
            asn1_replicaType.encode(encoder, encodedData);
            asn1_serverDN.encode(encoder, encodedData);
            asn1_dn.encode(encoder, encodedData);
            
            setValue(encodedData.toByteArray());
            
        }
      catch(IOException ioe) {
         throw new LDAPException("Encoding Error",
                                 LDAPException.ENCODING_ERROR);
      }
   }

}
