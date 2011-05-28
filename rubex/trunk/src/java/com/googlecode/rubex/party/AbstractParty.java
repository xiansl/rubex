package com.googlecode.rubex.party;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.rubex.party.event.PartyEvent;
import com.googlecode.rubex.party.event.PartyListener;

/**
 * Abstract base class for implementations of {@link Party} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractParty implements Party
{
    private final List <PartyListener> listeners =
        new ArrayList <PartyListener> ();

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addPartyListener (PartyListener listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        this.listeners.add (listener);
        
        PartyOrder [] orders = getAllOrders ();
        for (PartyOrder order: orders)
            listener.onOrder (new PartyEvent (this, null, order));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void removePartyListener (PartyListener listener)
    {
        if (listener == null)
            throw new IllegalArgumentException ("Listener is null");
        
        this.listeners.remove (listener);
    }
    
    /**
     * Get all party listeners.
     * 
     * @return an array of {@link PartyListener} objects
     */
    public synchronized PartyListener [] getAllPartyListeners ()
    {
        return listeners.toArray (new PartyListener [listeners.size ()]);
    }
    
    /**
     * Notify all party listeners about created order.
     * 
     * @param order order that was created
     */
    protected synchronized void fireOnOrderCreated (PartyOrder order)
    {
        PartyEvent event = null;
        
        for (PartyListener listener: listeners)
        {
            if (event == null)
                event = new PartyEvent (this, null, order);
            
            listener.onOrderCreated (event);
        }
    }
    
    /**
     * Notify all party listeners about changed order.
     * 
     * @param oldOrder order before change
     * @param oldOrder order after change
     */
    protected synchronized void fireOnOrderChanged (
        PartyOrder oldOrder, PartyOrder newOrder)
    {
        PartyEvent event = null;
        
        for (PartyListener listener: listeners)
        {
            if (event == null)
                event = new PartyEvent (this, oldOrder, newOrder);
            
            listener.onOrderChanged (event);
        }
    }
}
