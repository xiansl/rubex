package com.googlecode.rubex.exchange;

public interface OrderVisitor <T>
{
    public T visitMarketOrder (MarketOrder marketOrder);
    public T visitLimitOrder (LimitOrder marketOrder);
    public T visitStopOrder (StopOrder marketOrder);
    public T visitStopLimitOrder (StopLimitOrder marketOrder);
    public T visitIcebergOrder (IcebergOrder marketOrder);
}
