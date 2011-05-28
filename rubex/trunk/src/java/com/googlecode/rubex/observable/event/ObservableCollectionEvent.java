package com.googlecode.rubex.observable.event;

import java.util.EventObject;

/**
 * Contains details about observable collection event.
 * 
 * @param <T> type of observable collection elements
 * 
 * @author Mikhail Vladimirov
 */
public class ObservableCollectionEvent <T>
    extends EventObject
{
    private final T oldElement;
    private final T newElement;
    
    public ObservableCollectionEvent (
        Object source, T oldElement, T newElement)
    {
        super (source);
        
        this.oldElement = oldElement;
        this.newElement = newElement;
    }

    protected T getOldElement ()
    {
        return oldElement;
    }

    protected T getNewElement ()
    {
        return newElement;
    }
}
