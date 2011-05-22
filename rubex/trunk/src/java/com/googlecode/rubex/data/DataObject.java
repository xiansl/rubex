package com.googlecode.rubex.data;

/**
 * Data entity used to exchange information.
 * 
 * @author Mikhail Vladimirov
 */
public interface DataObject
{
    /**
     * Return type of the data entity.
     */
    public DataObjectType getType ();
    
    /**
     * Accept visitor by calling appropriate <code>visitXXX</code> method on it.
     * 
     * @param <T> type of visit result
     * @param visitor visitor to accept
     * @return visit result
     */
    public <T> T accept (DataObjectVisitor <T> visitor);
}
