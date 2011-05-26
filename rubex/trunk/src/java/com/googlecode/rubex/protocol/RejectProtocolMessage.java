package com.googlecode.rubex.protocol;

import com.googlecode.rubex.data.DataObject;

/**
 * Notification about rejected message.
 * 
 * @author Mikhail Vladimirov
 */
public interface RejectProtocolMessage extends ProtocolMessage
{
    /**
     * Name of the reject reason field.
     */
    public final static String REJECT_REASON = "rejectReason";
    
    /**
     * Name of the rejected message field.
     */
    public final static String REJECTED_MESSAGE = "rejectedMessage";
    
    /**
     * Return human-readable explanation why message was rejected.
     */
    public String getRejectReason ();

    /**
     * Return rejected message as data object.
     */
    public DataObject getRejectedMessage ();
}
