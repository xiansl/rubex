package com.googlecode.rubex.data;

public interface DataObjectVisitor <T>
{
    public T visitNullDataObject (NullDataObject nullDataObject);
    public T visitBooleanDataObject (BooleanDataObject booleanDataObject);
    public T visitIntegerDataObject (IntegerDataObject integerDataObject);
    public T visitRealDataObject (RealDataObject realDataObject);
    public T visitStringDataObject (StringDataObject stringDataObject);
    public T visitBinaryDataObject (BinaryDataObject binaryDataObject);
    public T visitListDataObject (ListDataObject listDataObject);
    public T visitStructureDataObject (StructureDataObject structureDataObject);
}
