package com.googlecode.rubex.protocol;

/**
 * Abstract base class for implementations of 
 * {@link ReplaceOrderProtocolMessage} interface.
 * 
 * @author Mikhail Vladimirov
 */
public abstract class AbstractReplaceOrderProtocolMessage 
    implements ReplaceOrderProtocolMessage
{
    /**
     * {@inheritDoc}
     */
    @Override
    public ProtocolMessageType getType ()
    {
        return ProtocolMessageType.REPLACE_ORDER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept (ProtocolMessageVisitor <T> visitor)
    {
        return visitor.visitReplaceOrder (this);
    }
}
