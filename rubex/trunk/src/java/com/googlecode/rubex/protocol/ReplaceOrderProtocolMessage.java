package com.googlecode.rubex.protocol;

import com.googlecode.rubex.exchange.OrderTimeInForce;
import com.googlecode.rubex.exchange.OrderType;

/**
 * Replace order request.
 * 
 * @author Mikhail Vladimirov
 */
public interface ReplaceOrderProtocolMessage extends ProtocolMessage
{
    /**
     * Name of the original order ID field.
     */
    public final static String ORIGINAL_ORDER_ID = "originalOrderID";
    
    /**
     * Name of the order ID field.
     */
    public final static String ORDER_ID = "orderID";
    
    /**
     * Name of the quantity field.
     */
    public final static String QUANTITY = "quantity";
    
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
     * Return ID of the order to be replaced.
     */
    public long getOriginalOrderID ();
    
    /**
     * Return unique ID of the new order.
     */
    public long getOrderID ();
    
    /**
     * Return quantity of the new order.
     */
    public long getQuantity ();
    
    /**
     * Return type of the new order.
     */
    public OrderType getOrderType ();
    
    /**
     * Return time in force for the new order.
     */
    public OrderTimeInForce getTimeInForce ();
    
    /**
     * Return limit price for the new order or 0 if there is no limit price.
     */
    public long getLimitPrice ();
    
    /**
     * Return stop price for the new order or 0 if there is no stop price.
     */
    public long getStopPrice ();
    
    /**
     * Return visible quantity for the new order or 0 if there is no visible 
     * quantity.
     */
    public long getVisibleQuantity ();
}
