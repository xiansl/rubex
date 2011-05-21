package com.googlecode.rubex.data;

public interface StructureDataObject extends DataObject
{
    public String [] getFieldNames ();
    public DataObject getField (String name);
}
