package com.googlecode.rubex.data;

/**
 * Marks setters that belong to structure fields.
 * 
 * @author Mikhail Vladimirov
 */
public @interface StructureField
{
    /**
     * Return name of the structure field this setter belongs to.
     */
    public String name ();
    
    /**
     * Tells whether this field is optional.
     * 
     * @return <code>true</code> if this field is optional, <code>false</code> 
     * otherwise
     */
    public boolean optional () default false;
}
