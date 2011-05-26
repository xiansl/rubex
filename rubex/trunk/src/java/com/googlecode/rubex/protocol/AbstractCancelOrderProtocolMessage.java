package com.googlecode.rubex.protocol;

/**
 * Abstract base class for implementations of {@link CancelOrderProtocolMessage} 
 * interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractCancelOrderProtocolMessage 
    implements CancelOrderProtocolMessage
{
    /**
     * {@inheritDoc}
     */
    @Override
    public ProtocolMessageType getType ()
    {
        return ProtocolMessageType.CANCEL_ORDER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (ProtocolMessageVisitor <T> visitor)
    {
        return visitor.visitCancelOrder (this);
    }
}
