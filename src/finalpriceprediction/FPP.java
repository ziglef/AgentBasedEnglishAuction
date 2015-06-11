package finalpriceprediction;

/**
 * Created on 04/06/15.
 *
 * @author André Silva
 * @author Rui Grandão
 *
 * alpha = MIN(AvgNumberOfBidders/10, 1) * 0.15
 * beta = MIN(AvgNumberOfConcurrentAuctions/10, 1) * (-0.15)
 * gamma = (asp - sp) * 0.6
 * fp = avp + alpha * avp + beta * avp + gamma
 *
 */

public class FPP {

    Integer alphaBound = 10;
    double alphaWeight = 0.15;
    private double calculateAlpha(double averageNumberOfBidders) {

        System.out.println(Math.min(averageNumberOfBidders/alphaBound, 1) * alphaWeight);
        return Math.min(averageNumberOfBidders/alphaBound, 1) * alphaWeight;
    }

    Integer betaBound = 10;
    double betaWeight = 0.15;
    private double calculateBeta(double averageNumberOfConcurrentAuctions) {

        System.out.println(Math.min(averageNumberOfConcurrentAuctions/betaBound, 1) * betaWeight);
        return Math.min(averageNumberOfConcurrentAuctions/betaBound, 1) * -betaWeight;
    }

    double startPriceWeight = 0.6;
    private double calculateGama(double averageStartingPrice, double startingPrice) {

        System.out.println((startingPrice - averageStartingPrice) * startPriceWeight);
        return (startingPrice - averageStartingPrice) * startPriceWeight;
    }

    public double calculateFinalPrice(double averageFinalPrice, double averageNumberOfBidders, double averageNumberOfConcurrentAuctions, double averageStartingPrice, double startingPrice) {
        return Math.max(averageFinalPrice, startingPrice) + averageFinalPrice * calculateAlpha(averageNumberOfBidders) + averageFinalPrice * calculateBeta(averageNumberOfConcurrentAuctions) + calculateGama(averageStartingPrice, startingPrice);
    }

    public FPP(){

    }
}
