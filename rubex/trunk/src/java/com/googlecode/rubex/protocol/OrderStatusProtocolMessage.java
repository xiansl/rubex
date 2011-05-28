package com.googlecode.rubex.protocol;

import com.googlecode.rubex.exchange.OrderSide;
import com.googlecode.rubex.exchange.OrderTimeInForce;
import com.googlecode.rubex.exchange.OrderType;
import com.googlecode.rubex.party.PartyOrderState;

public interface OrderStatusProtocolMessage
    extends ProtocolMessage
{
    /**
     * Name of the order ID field.
     */
    public final static String ORDER_ID = "orderID";
    
    /**
     * Name of the order state field
     */
    public final static String ORDER_STATE = "orderState";
    
    /**
     * Name of the account field.
     */
    public final static String ACCOUNT = "account";
    
    /**
     * Name of the symbol field.
     */
    public final static String SYMBOL = "symbol";
    
    /**
     * Name of the side field.
     */
    public final static String SIDE = "side";
    
    /**
     * Name of the quantity field.
     */
    public final static String QUANTITY = "quantity";
    
    /**
     * Name of the filled quantity field.
     */
    public final static String FILLED_QUANTITY = "filledQuantity";
    
    /**
     * Name of the filled value field.
     */
    public final static String FILLED_VALUE = "filledValue";
    
    /**
     * Name of the order type field.
     */
    public final static String ORDER_TYPE = "orderType";
    
    /**
     * Name of the time in force field.
     */
    public final static String TIME_IN_FORCE = "timeInForce";
    
    /**
     * Name of the limit price field.
     */
    public final static String lIMIT_PRICE = "limitPrice";
    
    /**
     * Name of the stop price field.
     */
    public final static String STOP_PRICE = "stopPrice";
    
    /**
     * Name of the visible quantity field.
     */
    public final static String VISIBLE_QUANTITY = "visibleQuantity";
    
    /**
     * Return unique order ID.
     */
    public long getOrderID ();

    /**
     * Return order state.
     */
    public PartyOrderState getOrderState ();
    
    /**
     * Return account ID this order belongs to or 0 for default account.
     */
    public long getAccount ();
    
    /**
     * Return security symbol for this order.
     */
    public String getSymbol ();
    
    /**
     * Return side of this order.
     */
    public OrderSide getSide ();
    
    /**
     * Return quantity of this order.
     */
    public long getQuantity ();
    
    /**
     * Return filled quantity of this order.
     */
    public long getFilledQuantity ();
    
    /**
     * Return filled value of this order.
     */
    public long getFilledValue ();
    
    /**
     * Return type of this order.
     */
    public OrderType getOrderType ();
    
    /**
     * Return time in force for this order or <code>null</code> if there is no 
     * time in force.
     */
    public OrderTimeInForce getTimeInForce ();
    
    /**
     * Return limit price for this order or 0 if there is no limit price.
     */
    public long getLimitPrice ();
    
    /**
     * Return stop price for this order or 0 if there is no stop price.
     */
    public long getStopPrice ();
    
    /**
     * Return visible quantity for this order or 0 if there is no visible 
     * quantity.
     */
    public long getVisibleQuantity ();
}
