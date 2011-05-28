package com.googlecode.rubex.protocol;

/**
 * Abstract base class for implementations of {@link OrderStatusProtocolMessage} 
 * interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractOrderStatusProtocolMessage 
    implements OrderStatusProtocolMessage
{
    /**
     * {@inheritDoc}
     */
    @Override
    public ProtocolMessageType getType ()
    {
        return ProtocolMessageType.ORDER_STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (ProtocolMessageVisitor <T> visitor)
    {
        return visitor.visitOrderStatus (this);
    }
}
