package com.googlecode.rubex.party;

import com.googlecode.rubex.exchange.OrderSide;
import com.googlecode.rubex.exchange.OrderTimeInForce;
import com.googlecode.rubex.exchange.OrderType;

public interface PartyOrder
{
    public long getOrderID ();
    public PartyOrderState getOrderState ();
    public long getAccount ();
    public String getSymbol ();
    public OrderSide getSide ();
    public long getQuantity ();
    public long getFilledQuantity ();
    public long getFilledValue ();
    public OrderType getOrderType ();
    public OrderTimeInForce getTimeInForce ();
    public long getLimitPrice ();
    public long getStopPrice ();
    public long getVisibleQuantity ();
}
