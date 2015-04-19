package auctions;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents the price history for a given product
 *
 * @author Rui Grandão  - ei11010@fe.up.pt
 * @author André Silva - ei12133@fe.up.pt
 *
 */
public class History {
    ////////////////////
    // Private Fields //
    ////////////////////
    private List<Float> priceHistory;

    /////////////////
    // Constructor //
    /////////////////
    public History(){
        priceHistory = new ArrayList<>();
    }

    ////////////////////
    // Custom Methods //
    ////////////////////
    public Float getAvgBidrate(){
        Float avgBidrate = 0f;

        for(int i=0; i<priceHistory.size()-1; i++){
            avgBidrate += priceHistory.get(i+1) - priceHistory.get(i);
        }

        return avgBidrate/(priceHistory.size()-1);
    }

    /////////////////////////
    // Getters and Setters //
    /////////////////////////
    public List<Float> getPriceHistory() { return priceHistory; }
    public Float getPriceHistory(Integer i){ return priceHistory.get(i); }

    public void addPriceHistory(Float value){ priceHistory.add(value); }

}
