package com.googlecode.rubex.data;

/**
 * Abstract base class for implementations of {@link DataObject} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractDataObject implements DataObject
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return DataObjectUtils.formatAsString (this);
    }
}
