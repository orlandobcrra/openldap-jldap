package com.novell.ldap;

/*  Represents the definition of a specific matching rule use in the
 *  directory schema.
 *  
 * <p>The LDAPMatchingRuleUseSchema class is used to discover or modify which
 * attributes are suitable for use with an extensible matching rule. It contains 
 * the name and identifier of a matching rule, and a list of attributes which 
 * it applies to.</p>
 */


public class LDAPMatchingRuleUseSchema
                extends LDAPSchemaElement
{

    /**
     * Constructs a matching rule use definition for adding to or deleting
     * from the schema.
     *
     * @param name        The name of the matching rule.
     *</br></br>
     * @param oid         The unique object identifier of the matching rule
     *                    in dotted numerical format.
     *</br></br>
     * @param description An optional description of the matching rule use.
     *</br></br>
     * @param obsolete    True if the matching rule use is obsolete.
     *</br></br>
     * @param attributes  A list of attributes that this matching rule
     *                    applies to. These values may be either the
     *                    names or numeric oids of the attributes.
     *</br></br>
     * @param aliases     Optional list of additional names by which the
     *                    matching rule use may be known; null if there
     *                    are no aliases.
     */
    public LDAPMatchingRuleUseSchema(String name,
                                     String oid,
                                     String description,
                                     boolean obsolete,
                                     String[] attributes,
                                     String[] aliases)
    {
        throw new RuntimeException("Class LDAPMatchingRuleUseSchema not implemented");
    }



    /**
     * Constructs a matching rule use definition from the raw string value
     * returned on a schema query for matchingRuleUse.
     *
     * @param raw        The raw string value returned on a schema
     *                   query for matchingRuleUse.
     */
    public LDAPMatchingRuleUseSchema(String raw)
    {
        throw new RuntimeException("Class LDAPMatchingRuleUseSchema not implemented");
    }

    /**
     * Returns an array of all the attributes which this matching rule
     * applies to.
     *
     * @return An array of all the attributes which this matching rule applies to.
     */
    public String[] getAttributes()
    {
        throw new RuntimeException("Method LDAPMatchingRuleUseSchema.getAttributes not implemented");
    }
}
