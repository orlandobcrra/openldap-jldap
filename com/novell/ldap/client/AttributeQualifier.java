/* **************************************************************************
 * $Id: LDAPSchemaElement.java,v 1.2 2000/03/14 18:17:29 smerrill Exp $
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

package com.novell.ldap.client;

import java.util.Vector;
public class AttributeQualifier{
   String name;
   Vector values;

   public AttributeQualifier( String name ){
     this.name = name;
     values = new Vector();
   }
   public void addValue( String value ){
     values.addElement( value );
   }
   public String getName(){
     return name;
   }
   public String[] getValues(){
     String[] strValues = null;
     if( values.size() > 0 ){
      strValues = new String[values.size()];
      for(int i = 0; i < values.size(); i++ ){
        strValues[i] = (String) values.get(i);
       }
    }
    return strValues;
   }
 }
