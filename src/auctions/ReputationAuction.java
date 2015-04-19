package auctions;

import products.Product;
import java.util.ArrayList;

/**
 * Class that represents an auction without price history but with a reputation
 *
 * @author Rui Grandão  - ei11010@fe.up.pt
 * @author André Silva - ei12133@fe.up.pt
 *
 */
public class ReputationAuction extends Auction {
    ////////////////////
    // Private Fields //
    ////////////////////
    private Integer reputation;

    /////////////////
    // Constructor //
    /////////////////
    public ReputationAuction(String name, ArrayList<Product> products, ArrayList<String> participants, Integer reputation) {
        super(name, products, participants);

        this.reputation = reputation;
    }

    /////////////////////////
    // Getters and Setters //
    /////////////////////////
    public Integer getReputation() { return reputation; }
}
