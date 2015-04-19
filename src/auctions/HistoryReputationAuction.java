package auctions;

import products.Product;

import java.util.ArrayList;

/**
 * Class that represents an auction with price history and reputation
 *
 * @author Rui Grandão  - ei11010@fe.up.pt
 * @author André Silva - ei12133@fe.up.pt
 *
 */
public class HistoryReputationAuction extends HistoryAuction{
    ////////////////////
    // Private Fields //
    ////////////////////
    private Integer reputation;

    /////////////////
    // Constructor //
    /////////////////
    public HistoryReputationAuction(String name, ArrayList<Product> products, ArrayList<String> participants, Integer reputation) {
        super(name, products, participants);
        this.reputation = reputation;
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
    public Integer getReputation() { return reputation; }
}
