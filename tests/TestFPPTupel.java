package tests;

import finalpriceprediction.FPPTupel;

import java.util.ArrayList;

/**
 * Created on 19/04/15.
 *
 * @author André Silva
 * @author Rui Grandão
 */
public class TestFPPTupel {

    public static void main(String[] args) {
        ArrayList<Integer> pastHistory = new ArrayList<>();
        pastHistory.add(0,12);
        pastHistory.add(1, 12);
        pastHistory.add(2, 1500);
        pastHistory.add(3, 50);

        FPPTupel tupel = new FPPTupel(2, 10, 1000, 50.0f, pastHistory);


        System.out.println("Final Price: " + tupel.getFinalPricePredictionForRound(2, 1500.0f));
    }
}
