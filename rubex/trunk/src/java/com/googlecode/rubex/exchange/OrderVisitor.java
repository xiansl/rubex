package com.googlecode.rubex.exchange;

/**
 * Visits order.
 * 
 * @param <T> type of visit result
 * 
 * @author Mikhail Vladimirov
 */
public interface OrderVisitor <T>
{
    /**
     * Visit market order.
     * 
     * @param marketOrder market order to visit
     * @return visit result
     */
    public T visitMarketOrder (MarketOrder marketOrder);
    
    /**
     * Visit limit order.
     * 
     * @param limitOrder limit order to visit
     * @return visit result
     */
    public T visitLimitOrder (LimitOrder limitOrder);
    
    /**
     * Visit stop order.
     * 
     * @param stopOrder stop order to visit
     * @return visit result
     */
    public T visitStopOrder (StopOrder stopOrder);
    
    /**
     * Visit stop limit order.
     * 
     * @param stopLimitOrder stop limit order to visit
     * @return visit result
     */
    public T visitStopLimitOrder (StopLimitOrder stopLimitOrder);
    
    /**
     * Visit iceberg order.
     * 
     * @param icebergOrder iceberg order to visit
     * @return visit result
     */
    public T visitIcebergOrder (IcebergOrder icebergOrder);
}
