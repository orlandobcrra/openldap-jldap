/* **************************************************************************
 * $Novell: /ldap/src/jldap/com/novell/ldap/LDAPRebindAuth.java,v 1.5 2000/09/11 22:47:49 judy Exp $
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

/*
 *4.24 public class LDAPRebindAuth
 */
 
/**
 *  Represents information used to authenticate the client in cases where
 *  the client follows referrals automatically.
 */
public class LDAPRebindAuth {

   private String _dn;
   private String _password;

   /*
    * 4.24.1 Constructors
    */

   /**
    * Constructs information that is used by the client for authentication
    * when following referrals automatically.
    *
    *  @param dn           The distinguished name to use when authenticating to
    *                      a server.
    *<br><br>
    *  @param password     The password to use when authenticating to a server.
    */
   public LDAPRebindAuth ( String dn, String password ) {
      _dn = dn;
      _password = password;
   }

   /*
    * 4.24.2 getDN
    */

   /**
    * Returns the distinguished name to be used for reauthentication on
    * automatic referral following.
    *
    * @return The distinguished name from the object.
    */
   public String getDN() {
      return _dn;
   }

   /*
    * 4.24.3 getPassword
    */

   /**
    * Returns the password to be used for reauthentication on automatic
    * referral following.
    *
    * @return The password from the object.
    */
   public String getPassword() {
      return _password;
   }

}
