/* **************************************************************************
 * $Novell: /ldap/src/jldap/com/novell/ldap/LDAPAttributeSchema.java,v 1.8 2000/09/14 22:43:26 judy Exp $
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

import com.novell.ldap.client.SchemaParser;
import com.novell.ldap.client.AttributeQualifier;
import java.util.Enumeration;
import java.io.IOException;

/*
 * 4.2 public class LDAPAttributeSchema
 *                extends LDAPSchemaElement
 */

/**
 *  Represents the definition of an attribute in the schema.
 *
 *  <p>The LDAPAttributeSchema class is used to query for an attribute's syntax,
 *  to add an attribute definition to a directory's schema, and to delete an
 *  attribute definition from the schema. See RFC 2252 for a description of
 *  attribute representation in LDAP.</p>
 *
 */
public class LDAPAttributeSchema extends LDAPSchemaElement {

   	private String syntaxString;
	private boolean single = false;
	private String superior;

   /*
    * 4.2.1 Constructors
    */

   /**
    * Constructs an attribute definition for adding to or deleting from a
    * directory's schema.
    *
    * @param name  Name of the attribute.
    *<br><br>
    * @param oid   Object identifer of the attribute, in
    *              dotted numerical format.
    *<br><br>
    * @param description   Optional description of the attribute.
    *<br><br>
    * @param syntaxString  Object identifer of the syntax of the
    *              attribute, in dotted numerical format.
    *<br><br>
    * @param single    True if the attribute is to be single-valued.
    *<br><br>
    * @param superior  Optional name of the attribute type which this
    *              attribute type derives from; null if there is no
    *              superior attribute type.
    *<br><br>
    * @param aliases   Optional list of additional names by which the
    *              attribute may be known; null if there are no
    *              aliases.
    */
   public LDAPAttributeSchema(String name, String oid, String description,
                              String syntaxString, boolean single,
                              String superior, String[] aliases) {

		super.name = new String(name);
		super.oid = new String(oid);
		super.description = new String(description);
		this.syntaxString = new String (syntaxString);
		this.single = single;
		this.superior = new String(superior);
                if( aliases != null){
                  super.aliases = new String[aliases.length];
		  for( int i = 0; i < super.aliases.length; i++ ){
	  		super.aliases[i] = aliases[i];
		  }
                }

   }

   /**
    * Constructs an attribute definition from the raw string value returned
    * on a directory query for "attributetypes".
    *
    *  @param raw      The raw string value returned on a directory
    *                  query for "attributetypes".
    */
   public LDAPAttributeSchema(String raw) {
                try{
		  SchemaParser parser = new SchemaParser( raw );

                  if( parser.getName() != null)
		   super.name = new String(parser.getName());
                  super.aliases = parser.getAliases();
                  if( parser.getID() != null)
                    super.oid = new String(parser.getID());
                  if( parser.getDescription() != null)
                   super.description = new String(parser.getDescription());
                  if( parser.getSyntax() != null)
                   syntaxString = new String(parser.getSyntax());
                  if( parser.getSuperior() != null)
                   syntaxString = new String(parser.getSuperior());
                  single = parser.getSingle();
                  super.obsolete = parser.getObsolete();
                  Enumeration qualifiers = parser.getQualifiers();
                  AttributeQualifier attrQualifier;
                  while(qualifiers.hasMoreElements()){
                   attrQualifier = (AttributeQualifier) qualifiers.nextElement();
                   setQualifier(attrQualifier.getName(), attrQualifier.getValues());
                  }
              }
              catch( IOException e){
              }
   }

   /*
    * 4.2.2 getSyntaxString
    */

   /**
    * Returns the object identifer of the syntax of the attribute, in
    * dotted numerical format.
    *
    * @return The object identifer of the attribute's syntax.
    */
   public String getSyntaxString() {
		return syntaxString;
   }

   /*
    * 4.2.4 getSuperior
    */

   /**
    * Returns the name of the attribute type which this attribute derives
    * from, or null if there is no superior attribute.
    *
    * @return The attribute's superior attribute, or null if there is none.
    */
   public String getSuperior() {
      return superior;
   }

   /*
    * 4.2.3 isSingleValued
    */

   /**
    * Returns true if the attribute is single-valued.
    *
    * @return True if the attribute is single-valued; false if the attribute
    *         is multi-valued.
    */
   public boolean isSingleValued() {
      return single;
   }

   /**
    * Returns a string in a format suitable for directly adding to a
    * directory, as a value of the particular schema element attribute.
    *
    * @return A string representation of the attribute's definition.
    */
   public String getValue() {

      StringBuffer valueBuffer = new StringBuffer("( ");
      String token;
      String[] strArray;

      if( (token = getID()) != null){
        valueBuffer.append(token);
      }
      strArray = getAliases();
      if( (token = getName()) != null){
        valueBuffer.append(" NAME ");
        if(strArray != null){
          valueBuffer.append("( ");
        }
        valueBuffer.append("'" + token + "'");
        if(strArray != null){
          for( int i = 0; i < strArray.length; i++ ){
            valueBuffer.append(" '" + strArray[i] + "'");
          }
          valueBuffer.append(" )");
        }
      }
      if( (token = getDescription()) != null){
        valueBuffer.append(" DESC ");
        valueBuffer.append("'" + token + "'");
      }
      if( isObsolete()){
        valueBuffer.append(" OBSOLETE");
      }
      if( (token = getSuperior()) != null){
        valueBuffer.append(" SUP ");
        valueBuffer.append("'" + token + "'");
      }
      if( (token = getSyntaxString()) != null){
        valueBuffer.append(" SYNTAX ");
        valueBuffer.append(token);
      }
      if( isSingleValued()){
        valueBuffer.append(" SINGLE-VALUE");
      }
      Enumeration en = getQualifierNames();
      while( en.hasMoreElements()){
        token = (String) en.nextElement();
        if( (token != null)){
          valueBuffer.append(" " + token );
          strArray = getQualifier(token);
          if(strArray != null){
            for( int i = 0; i < strArray.length; i++ ){
              valueBuffer.append(" '" + strArray[i] + "'");
            }
          }
        }
      }
      valueBuffer.append(" )");
      return valueBuffer.toString();
   }

}
