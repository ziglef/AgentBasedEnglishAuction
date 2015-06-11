package finalpriceprediction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static Database instance;

    private Map<String, ArrayList<Double>> avgFinalPrice;
    private Map<String, ArrayList<Double>> avgNoBidders;
    private Map<String, ArrayList<Double>> avgNoConcAuctions;
    private Map<String, ArrayList<Double>> avgStartingPrice;

    // Gets the current instance of the database or creates it if none exist //
    public static synchronized Database getInstance(){
        if( instance == null )
            instance = new Database();

        return instance;
    }

    // Constructor called when no database exists //
    private Database() {
        avgFinalPrice = new HashMap<>();
        avgNoBidders = new HashMap<>();
        avgNoConcAuctions = new HashMap<>();
        avgStartingPrice = new HashMap<>();
    }

    public ArrayList<Double> getAvgFinalPrice(String product) { return avgFinalPrice.get(product); }
    public ArrayList<Double> getAvgNoBidders(String product) { return avgNoBidders.get(product); }
    public ArrayList<Double> getAvgNoConcAuctions(String product) { return avgNoConcAuctions.get(product); }
    public ArrayList<Double> getAvgStartingPrice(String product) { return avgStartingPrice.get(product); }

    public void addAvgFinalPrice(String product, Double value) {
        ArrayList<Double> newFinalPrice = avgFinalPrice.get(product);
        if( newFinalPrice == null )
            newFinalPrice = new ArrayList<>();
        newFinalPrice.add(value);
        avgFinalPrice.put(product, newFinalPrice);
    }
    public void addAvgNoBidders(String product, Double value) {
        ArrayList<Double> newAvgNoBidders = avgNoBidders.get(product);
        if( newAvgNoBidders == null )
            newAvgNoBidders = new ArrayList<>();
        newAvgNoBidders.add(value);
        avgNoBidders.put(product, newAvgNoBidders);
    }
    public void addAvgNoConcAuctions(String product, Double value) {
        ArrayList<Double> newAvgNoConcAuctions = avgNoConcAuctions.get(product);
        if( newAvgNoConcAuctions == null )
            newAvgNoConcAuctions = new ArrayList<>();
        newAvgNoConcAuctions.add(value);
        avgNoConcAuctions.put(product, newAvgNoConcAuctions);
    }
    public void addAvgStartingPrice(String product, Double value) {
        ArrayList<Double> newAvgStartingPrice = avgNoBidders.get(product);
        if( newAvgStartingPrice == null )
            newAvgStartingPrice = new ArrayList<>();
        newAvgStartingPrice.add(value);
        avgNoBidders.put(product, newAvgStartingPrice);
    }
}
