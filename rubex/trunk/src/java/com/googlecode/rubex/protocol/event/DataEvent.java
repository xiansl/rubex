package com.googlecode.rubex.protocol.event;

import java.util.EventObject;

import com.googlecode.rubex.data.DataObject;

/**
 * Contains details about data-related event.
 * 
 * @author Mikhail Vladimirov
 */
public class DataEvent extends EventObject
{
    private final DataObject data;
    
    /**
     * Create new data event with given event source and data object.
     * 
     * @param source event source
     * @param data data object
     */
    public DataEvent (Object source, DataObject data)
    {
        super (source);
        
        if (data == null)
            throw new IllegalArgumentException ("Data is null");
        
        this.data = data;
    }
    
    /**
     * Return data object of this event.
     */
    public DataObject getData ()
    {
        return data;
    }
}
