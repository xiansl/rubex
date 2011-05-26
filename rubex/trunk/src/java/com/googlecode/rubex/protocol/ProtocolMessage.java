package com.googlecode.rubex.protocol;

/**
 * Business-level protocol message.
 * 
 * @author Mikhail Vladimirov
 */
public interface ProtocolMessage
{
    /**
     * Return type of the message.
     */
    public ProtocolMessageType getType ();
    
    /**
     * Accept given visitor.
     * 
     * @param <T> type of visit result
     * @param visitor visitor to be accepted
     * @return visit result
     */
    public <T> T accept (ProtocolMessageVisitor <T> visitor);
}
