package com.googlecode.rubex.data;

/**
 * Visits data objects.
 * 
 * @param <T> type of visit result
 * 
 * @author Mikhail Vladimirov
 */
public interface DataObjectVisitor <T>
{
    /**
     * Visit null data object.
     * 
     * @param null data object to be visited
     * @return visit result
     */
    public T visitNullDataObject (NullDataObject nullDataObject);
    
    /**
     * Visit boolean data object.
     * 
     * @param boolean data object to be visited
     * @return visit result
     */
    public T visitBooleanDataObject (BooleanDataObject booleanDataObject);
    
    /**
     * Visit integer data object.
     * 
     * @param integer data object to be visited
     * @return visit result
     */
    public T visitIntegerDataObject (IntegerDataObject integerDataObject);
    
    /**
     * Visit real data object.
     * 
     * @param real data object to be visited
     * @return visit result
     */
    public T visitRealDataObject (RealDataObject realDataObject);
    
    /**
     * Visit string data object.
     * 
     * @param string data object to be visited
     * @return visit result
     */
    public T visitStringDataObject (StringDataObject stringDataObject);
    
    /**
     * Visit binary data object.
     * 
     * @param binary data object to be visited
     * @return visit result
     */
    public T visitBinaryDataObject (BinaryDataObject binaryDataObject);
    
    /**
     * Visit list data object.
     * 
     * @param list data object to be visited
     * @return visit result
     */
    public T visitListDataObject (ListDataObject listDataObject);
    
    /**
     * Visit structure data object.
     * 
     * @param structure data object to be visited
     * @return visit result
     */
    public T visitStructureDataObject (StructureDataObject structureDataObject);
}
