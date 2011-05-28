package com.googlecode.rubex.observable.event;

import java.util.EventListener;

/**
 * Listens for observable collection events.
 * 
 * @param <T> type of observable collection elements
 * 
 * @author Mikhail Vladimirov
 */
public interface ObservableCollectionListener <T>
    extends EventListener
{
    /**
     * Called when element was added to the collection.
     * 
     * @param event event details
     */
    public void onElementAdded (ObservableCollectionEvent <T> event);
    
    /**
     * Called when element was changed in the collection.
     * 
     * @param event event details
     */
    public void onElementChanged (ObservableCollectionEvent <T> event);
    
    /**
     * Called when element was removed from the collection.
     * 
     * @param event event details
     */
    public void onElementRemoved (ObservableCollectionEvent <T> event);
}
