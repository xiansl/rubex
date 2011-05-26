package com.googlecode.rubex.protocol;

/**
 * Abstract base class for implementations of {@link NewOrderProtocolMessage} 
 * interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractNewOrderProtocolMessage 
    implements NewOrderProtocolMessage
{
    /**
     * {@inheritDoc}
     */
    @Override
    public ProtocolMessageType getType ()
    {
        return ProtocolMessageType.NEW_ORDER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (ProtocolMessageVisitor <T> visitor)
    {
        return visitor.visitNewOrder (this);
    }
}
