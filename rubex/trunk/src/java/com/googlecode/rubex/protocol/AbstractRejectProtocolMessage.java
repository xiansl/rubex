package com.googlecode.rubex.protocol;

/**
 * Abstract base class for implementations of {@link RejectProtocolMessage} 
 * interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractRejectProtocolMessage 
    implements RejectProtocolMessage
{
    /**
     * {@inheritDoc}
     */
    @Override
    public ProtocolMessageType getType ()
    {
        return ProtocolMessageType.REJECT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (ProtocolMessageVisitor <T> visitor)
    {
        return visitor.visitReject (this);
    }
}
