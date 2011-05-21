package com.googlecode.rubex.data;

public interface ListDataObject extends DataObject
{
    public int getSize ();
    public DataObject getElementAt (int index);
    public DataObject [] getAllElements ();
}
