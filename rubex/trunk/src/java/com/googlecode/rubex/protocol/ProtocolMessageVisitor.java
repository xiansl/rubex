package com.googlecode.rubex.protocol;

/**
 * Visits business-level protocol message.
 * 
 * @param <T> visit result type
 * 
 * @author Mikhail Vladimirov
 */
public interface ProtocolMessageVisitor <T>
{
    /**
     * Visit reject request.
     * 
     * @param reject reject to be visited
     * @return visit result
     */
    public T visitReject (RejectProtocolMessage reject);
    
    /**
     * Visit new order request.
     * 
     * @param newOrder new order request to be visited
     * @return visit result
     */
    public T visitNewOrder (NewOrderProtocolMessage newOrder);
    
    /**
     * Visit replace order request.
     * 
     * @param replaceOrder replace order request to be visited
     * @return visit result
     */
    public T visitReplaceOrder (ReplaceOrderProtocolMessage replaceOrder);
    
    /**
     * Visit cancel order request.
     * 
     * @param cancelOrder cancel order request to be visited
     * @return visit result
     */
    public T visitCancelOrder (CancelOrderProtocolMessage cancelOrder);
    
    /**
     * Visit order status message.
     * 
     * @param orderStatus order status message to be visited
     * @return visit result
     */
    public T visitOrderStatus (OrderStatusProtocolMessage orderStatus);
}
