package com.googlecode.rubex.party.event;

import java.util.EventObject;

import com.googlecode.rubex.party.PartyOrder;

/**
 * Contains details about party event.
 * 
 * @author Mikhail Vladimirov
 */
public class PartyEvent extends EventObject
{
    private final PartyOrder oldOrder;
    private final PartyOrder newOrder;
    
    /**
     * Create new party event with given event source, old order and new order.
     * 
     * @param source event source
     * @param oldOrder old party order
     * @param newOrder new party order
     */
    public PartyEvent (Object source, PartyOrder oldOrder, PartyOrder newOrder)
    {
        super (source);
        
        this.oldOrder = oldOrder;
        this.newOrder = newOrder;
    }

    /**
     * Return old party order.
     */
    public PartyOrder getOldOrder ()
    {
        return oldOrder;
    }

    /**
     * Return new party order.
     */
    public PartyOrder getNewOrder ()
    {
        return newOrder;
    }
}
