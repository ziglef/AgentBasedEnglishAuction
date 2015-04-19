package auctions;

import products.Product;

import java.util.ArrayList;

/**
 * Class that represents an auction without reputation but using price history
 *
 * @author Rui Grandão  - ei11010@fe.up.pt
 * @author André Silva - ei12133@fe.up.pt
 *
 */
public class HistoryAuction extends Auction {
    ////////////////////
    // Private Fields //
    ////////////////////
    private ArrayList<History> priceHistory;

    /////////////////
    // Constructor //
    /////////////////
    public HistoryAuction(String name, ArrayList<Product> products, ArrayList<String> participants) {
        super(name, products, participants);
        priceHistory = new ArrayList<>(products.size());
    }

    ////////////////////
    // Custom Methods //
    ////////////////////
    private void initGui(){
        // TODO: add code for GUI initialization for a given auction
    }

    /////////////////////////
    // Getters and Setters //
    /////////////////////////
    public ArrayList<History> getPriceHistory() { return priceHistory; }
    public History getPriceHistory(Integer i) { return priceHistory.get(i); }
}
