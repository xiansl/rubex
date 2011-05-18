package com.googlecode.rubex.utils;

public class LongUtils
{
    private LongUtils ()
    {
        throw new Error ("Do not instantiate me");
    }
    
    public static long safeAdd (long x, long y)
    {
        if (x > 0 && y > 0 && (x > Long.MAX_VALUE - y))
            throw new RuntimeException ("Long number overflow");
        
        if (x < 0 && y < 0 && (x < Long.MIN_VALUE - y))
            throw new RuntimeException ("Long number overflow");
        
        return x + y;
    }
    
    public static long safeMultiply (long x, long y)
    {
        if (x > 0 && y > 0 && (x > Long.MAX_VALUE / y))
            throw new RuntimeException ("Long number overflow");
        
        if (x < 0 && y < 0 && (x < Long.MAX_VALUE / y))
            throw new RuntimeException ("Long number overflow");
        
        if (x < 0 && y > 0 && (x < Long.MIN_VALUE / y))
            throw new RuntimeException ("Long number overflow");
        
        if (x > 0 && y < 0 && (x > Long.MIN_VALUE / y))
            throw new RuntimeException ("Long number overflow");
        
        return x * y;
    }
}
