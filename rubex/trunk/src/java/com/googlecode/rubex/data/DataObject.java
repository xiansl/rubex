package com.googlecode.rubex.data;

public interface DataObject
{
    public DataObjectType getType ();
    
    public <T> T accept (DataObjectVisitor <T> visitor);
}
