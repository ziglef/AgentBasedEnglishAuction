package finalpriceprediction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static Database instance;

    private Map<String, ArrayList<Double>> finalPrice;
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
        finalPrice = new HashMap<>();
        avgNoBidders = new HashMap<>();
        avgNoConcAuctions = new HashMap<>();
        avgStartingPrice = new HashMap<>();
    }

    public ArrayList<Double> getFinalPrice(String product) { return finalPrice.get(product); }
    public ArrayList<Double> getAvgNoBidders(String product) { return avgNoBidders.get(product); }
    public ArrayList<Double> getAvgNoConcAuctions(String product) { return avgNoConcAuctions.get(product); }
    public ArrayList<Double> getAvgStartingPrice(String product) { return avgStartingPrice.get(product); }

    public void addFinalPrice(String product, Double value) {
        ArrayList<Double> newFinalPrice = finalPrice.get(product);
        newFinalPrice.add(value);
        finalPrice.put(product, newFinalPrice);
    }
    public void addAvgNoBidders(String product, Double value) {
        ArrayList<Double> newAvgNoBidders = avgNoBidders.get(product);
        newAvgNoBidders.add(value);
        avgNoBidders.put(product, newAvgNoBidders);
    }
    public void addAvgNoConcAuctions(String product, Double value) {
        ArrayList<Double> newAvgNoConcAuctions = avgNoConcAuctions.get(product);
        newAvgNoConcAuctions.add(value);
        avgNoConcAuctions.put(product, newAvgNoConcAuctions);
    }
    public void addAvgStartingPrice(String product, Double value) {
        ArrayList<Double> newAvgStartingPrice = avgNoBidders.get(product);
        newAvgStartingPrice.add(value);
        avgNoBidders.put(product, newAvgStartingPrice);
    }
}
