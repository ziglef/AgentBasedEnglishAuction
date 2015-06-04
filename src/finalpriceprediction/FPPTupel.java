package finalpriceprediction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 19/04/15.
 *
 * @author André Silva
 * @author Rui Grandão
 */

public class FPPTupel {

    /* ArrayList Description - Position */
    final Integer NUMBER_OF_BIDDERS = 0;
    final Integer NUMBER_OF_CONCURRENT_AUCTIONS = 1;
    final Integer OPENING_BID_VALUE = 2;
    final Integer BID_RATE = 3;
    final Integer REPUTATION_RELIABILITY_AUCTION_SITE = 4;

    /* Belief Set Of Current Round Of Relevant Auction (PHI) */
    protected ArrayList<Integer> currentRoundArray = new ArrayList<>(4);

    /* Belief Set Of Past Round Of Relevant Auction (OMEGA) */
    protected ArrayList<Integer> pastRoundArray = new ArrayList<>(4);

    /* Belief Set Obtained By The Average Past Closed Auctions In The Same Auction Site (ETA) */
    //protected ArrayList<Integer> AvgPastClosedAuctionsArray = new ArrayList<Integer>(4);

    /* Plan Set Affection */
    protected Map<Integer, Float> planArray = new HashMap<>(5);


    /* Constructors */
    public FPPTupel(Integer numberOfBidders, Integer numberOfConcurrentAuctions, Integer openingBidValue, Float auctionSiteReputation) {

        currentRoundArray.add(NUMBER_OF_BIDDERS, numberOfBidders);
        currentRoundArray.add(NUMBER_OF_CONCURRENT_AUCTIONS, numberOfConcurrentAuctions);
        currentRoundArray.add(OPENING_BID_VALUE, openingBidValue);
        currentRoundArray.add(BID_RATE, 0);

        pastRoundArray.add(NUMBER_OF_BIDDERS, numberOfBidders);
        pastRoundArray.add(NUMBER_OF_CONCURRENT_AUCTIONS, numberOfConcurrentAuctions);
        pastRoundArray.add(OPENING_BID_VALUE, openingBidValue);
        pastRoundArray.add(BID_RATE, 0);

        computePlans(auctionSiteReputation, 1);
    }

    public FPPTupel(Integer numberOfBidders, Integer numberOfConcurrentAuctions, Integer openingBidValue, Float auctionSiteReputation, ArrayList<Integer> AvgPastClosedAuctionsArray) {

        currentRoundArray.add(NUMBER_OF_BIDDERS, numberOfBidders);
        currentRoundArray.add(NUMBER_OF_CONCURRENT_AUCTIONS, numberOfConcurrentAuctions);
        currentRoundArray.add(OPENING_BID_VALUE, openingBidValue);
        currentRoundArray.add(BID_RATE, AvgPastClosedAuctionsArray.get(BID_RATE));

        pastRoundArray.add(NUMBER_OF_BIDDERS, AvgPastClosedAuctionsArray.get(NUMBER_OF_BIDDERS));
        pastRoundArray.add(NUMBER_OF_CONCURRENT_AUCTIONS, AvgPastClosedAuctionsArray.get(NUMBER_OF_CONCURRENT_AUCTIONS));
        pastRoundArray.add(OPENING_BID_VALUE, AvgPastClosedAuctionsArray.get(OPENING_BID_VALUE));
        pastRoundArray.add(BID_RATE, AvgPastClosedAuctionsArray.get(BID_RATE));

        computePlans(auctionSiteReputation, 1);
    }


    /* Methods */
    private void computePlans(Float auctionSiteReputation, Integer roundNumber) {

        planArray.put(REPUTATION_RELIABILITY_AUCTION_SITE, auctionSiteReputation);
        updatePlans(roundNumber);
    }

    private void updatePlans(Integer roundNumber) {

        planArray.put(NUMBER_OF_BIDDERS, calculateNumberOfBidders());
        planArray.put(NUMBER_OF_CONCURRENT_AUCTIONS, calculateNumberOfConcurrentAuctions());
        planArray.put(OPENING_BID_VALUE, calculateOpeningBidValue(roundNumber));
        planArray.put(BID_RATE, calculateBidRate(roundNumber));
    }

    private Float calculateNumberOfBidders() {

        Integer currentNOBidders = currentRoundArray.get(NUMBER_OF_BIDDERS);
        Integer pastNOBidders = pastRoundArray.get(NUMBER_OF_BIDDERS);

        return (float)(currentNOBidders - pastNOBidders) / pastNOBidders;
    }

    private Float calculateNumberOfConcurrentAuctions() {

        Integer currentNOConcurrentAuctions = currentRoundArray.get(NUMBER_OF_CONCURRENT_AUCTIONS);
        Integer pastNOConcurrentAuctions = pastRoundArray.get(NUMBER_OF_CONCURRENT_AUCTIONS);

        return (float)(currentNOConcurrentAuctions - pastNOConcurrentAuctions) / pastNOConcurrentAuctions;
    }

    private Float calculateOpeningBidValue(Integer roundNumber) {

        if (roundNumber == 1) {

            Integer currentOpenBidValue = currentRoundArray.get(OPENING_BID_VALUE);
            Integer pastOpenBidValue = pastRoundArray.get(OPENING_BID_VALUE);

            return (float)(currentOpenBidValue - pastOpenBidValue) / pastOpenBidValue;
        }

        return 0.0f;
    }

    private Float calculateBidRate(Integer roundNumber) {

        if (roundNumber > 1) {

            Integer currentBidRate = currentRoundArray.get(OPENING_BID_VALUE);
            Integer pastBidRate = pastRoundArray.get(OPENING_BID_VALUE);

            return (float)(currentBidRate - pastBidRate) / pastBidRate;
        }

        return 0.0f;
    }

    public Float getFinalPricePredictionForRound(Integer roundNumber, Float averageFinalPrice) {
        updatePlans(roundNumber);

        return averageFinalPrice + planArray.get(NUMBER_OF_BIDDERS) * averageFinalPrice
                                    - planArray.get(NUMBER_OF_CONCURRENT_AUCTIONS) * averageFinalPrice
                                        + planArray.get(OPENING_BID_VALUE) * averageFinalPrice
                                            + planArray.get(BID_RATE) * averageFinalPrice
                                                + planArray.get(REPUTATION_RELIABILITY_AUCTION_SITE) * averageFinalPrice;
    }
}