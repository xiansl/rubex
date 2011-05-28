package junk;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import com.googlecode.rubex.orderbook.OrderBookEntrySide;
import com.googlecode.rubex.orderbook.event.OrderBookListener;
import com.googlecode.rubex.orderbook.event.OrderBookQuoteEvent;
import com.googlecode.rubex.orderbook.event.OrderBookTradeEvent;

public class OrderBookWindow implements OrderBookListener
{
    private final JFrame frame;
    private final JTable table;
    private final OrderBookTableModel tableModel;
    
    public OrderBookWindow ()
    {
        frame = new JFrame ("Order Book");
        frame.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
        frame.getContentPane ().setLayout (new BorderLayout ());
        
        tableModel = new OrderBookTableModel ();
        table = new JTable (tableModel);
        
        frame.getContentPane ().add (new JScrollPane (table), BorderLayout.CENTER);
        
        frame.pack ();
        frame.setVisible (true);
    }
    
    @Override
    public void onTrade (OrderBookTradeEvent event)
    {
        // Do nothing
    }

    @Override
    public void onQuote (final OrderBookQuoteEvent event)
    {
        SwingUtilities.invokeLater (new Runnable()
        {
            @Override
            public void run ()
            {
                tableModel.updateQuote (event.getSide (), event.getPrice (), event.getQuantityDelta ());
            }
        });
    }

    private static class OrderBookTableModel extends AbstractTableModel
    {
        private final List<Long> bids = new ArrayList <Long> ();
        private final List<Long> prices = new ArrayList <Long> ();
        private final List<Long> asks = new ArrayList <Long> ();
        
        @Override
        public int getRowCount ()
        {
            return prices.size ();
        }
        @Override
        public int getColumnCount ()
        {
            return 3;
        }
        @Override
        public Object getValueAt (int rowIndex, int columnIndex)
        {
            switch (columnIndex)
            {
            case 0:
                return bids.get (rowIndex) == null ? "" : bids.get (rowIndex);
            case 1:
                return prices.get (rowIndex);
            case 2:
                return asks.get (rowIndex) == null ? "" : asks.get (rowIndex);
            default:
                throw new IllegalArgumentException ("Invalid column index: " + columnIndex); 
            }
        }
        
        @Override
        public String getColumnName (int column)
        {
            switch (column)
            {
            case 0:
                return "Bid";
            case 1:
                return "Price";
            case 2:
                return "Ask";
            default:
                throw new IllegalArgumentException ("Invalid column index: " + column); 
            }
        }
        public void updateQuote (OrderBookEntrySide side, long price, long quantityDelta)
        {
            Long priceObject = Long.valueOf (price);
            
            int index = Collections.binarySearch (prices, priceObject);
            
            if (index >= 0)
            {
                switch (side)
                {
                case BID:
                    Long bid = bids.get (index);
                    bid = bid == null ? Long.valueOf (quantityDelta) : Long.valueOf (bid.longValue () + quantityDelta);
                    bids.set (index, bid.longValue () == 0 ? null : bid);
                    break;
                case ASK:
                    Long ask = asks.get (index);
                    ask = ask == null ? Long.valueOf (quantityDelta) : Long.valueOf (ask.longValue () + quantityDelta);
                    asks.set (index, ask.longValue () == 0 ? null : ask);
                    break;
                default:
                    throw new Error ("Unknown order book entry side");
                }
                
                fireTableRowsUpdated (index, index);
            }
            else
            {
                index = -index - 1;
                
                switch (side)
                {
                case BID:
                    bids.add (index, quantityDelta == 0 ? null : Long.valueOf (quantityDelta));
                    asks.add (index, null);
                    break;
                case ASK:
                    bids.add (index, null);
                    asks.add (index, quantityDelta == 0 ? null : Long.valueOf (quantityDelta));
                    break;
                default:
                    throw new Error ("Unknown order book entry side");
                }
                
                prices.add (index, priceObject);
                fireTableRowsInserted (index, index);
            }
            
            if (bids.get (index) == null && asks.get (index) == null)
            {
                bids.remove (index);
                prices.remove (index);
                asks.remove (index);
                
                fireTableRowsDeleted (index, index);
            }
        }
    }
}
