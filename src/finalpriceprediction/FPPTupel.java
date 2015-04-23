package finalpriceprediction;

import java.util.ArrayList;

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
    protected ArrayList<Integer> CurrentRoundArray = new ArrayList<Integer>(4);

    /* Belief Set Of Past Round Of Relevant Auction (OMEGA) */
    protected ArrayList<Integer> PastRoundArray = new ArrayList<Integer>(4);

    /* Belief Set Obtained By The Average Past Closed Auctions In The Same Auction Site (ETA) */
    //protected ArrayList<Integer> AvgPastClosedAuctionsArray = new ArrayList<Integer>(4);

    /* Plan Set Affection */
    protected ArrayList<Integer> PlanArray = new ArrayList<Integer>(5);


    /* Constructors */
    public FPPTupel(Integer numberOfBidders, Integer numberOfConcurrentAuctions, Integer openingBidValue, Integer bidRate, Integer auctionSiteReputation) {

        CurrentRoundArray.add(NUMBER_OF_BIDDERS, numberOfBidders);
        CurrentRoundArray.add(NUMBER_OF_CONCURRENT_AUCTIONS, numberOfConcurrentAuctions);
        CurrentRoundArray.add(OPENING_BID_VALUE, openingBidValue);
        CurrentRoundArray.add(BID_RATE, bidRate);

        PastRoundArray.add(NUMBER_OF_BIDDERS, numberOfBidders);
        PastRoundArray.add(NUMBER_OF_CONCURRENT_AUCTIONS, numberOfConcurrentAuctions);
        PastRoundArray.add(OPENING_BID_VALUE, openingBidValue);
        PastRoundArray.add(BID_RATE, bidRate);

        computePlans(auctionSiteReputation, 1);
    }

    public FPPTupel(Integer numberOfBidders, Integer numberOfConcurrentAuctions, Integer openingBidValue, Integer bidRate, Integer auctionSiteReputation, ArrayList<Integer> AvgPastClosedAuctionsArray) {

        CurrentRoundArray.add(NUMBER_OF_BIDDERS, numberOfBidders);
        CurrentRoundArray.add(NUMBER_OF_CONCURRENT_AUCTIONS, numberOfConcurrentAuctions);
        CurrentRoundArray.add(OPENING_BID_VALUE, openingBidValue);
        CurrentRoundArray.add(BID_RATE, bidRate);

        PastRoundArray.add(NUMBER_OF_BIDDERS, AvgPastClosedAuctionsArray.get(NUMBER_OF_BIDDERS));
        PastRoundArray.add(NUMBER_OF_CONCURRENT_AUCTIONS, AvgPastClosedAuctionsArray.get(NUMBER_OF_CONCURRENT_AUCTIONS));
        PastRoundArray.add(OPENING_BID_VALUE, AvgPastClosedAuctionsArray.get(OPENING_BID_VALUE));
        PastRoundArray.add(BID_RATE, AvgPastClosedAuctionsArray.get(BID_RATE));

        computePlans(auctionSiteReputation, 1);
    }


    /* Methods */
    private void computePlans(Integer auctionSiteReputation, Integer roundNumber) {

        updatePlans(roundNumber);
        PlanArray.add(REPUTATION_RELIABILITY_AUCTION_SITE, auctionSiteReputation);
    }

    private void updatePlans(Integer roundNumber) {

        PlanArray.add(NUMBER_OF_BIDDERS, calculateNumberOfBidders());
        PlanArray.add(NUMBER_OF_CONCURRENT_AUCTIONS, calculateNumberOfConcurrentAuctions());
        PlanArray.add(OPENING_BID_VALUE, calculateOpeningBidValue(roundNumber));
        PlanArray.add(BID_RATE, calculateBidRate(roundNumber));
    }

    private Integer calculateNumberOfBidders() {

        Integer CurrentNOBidders = CurrentRoundArray.get(NUMBER_OF_BIDDERS);
        Integer PastNOBidders = PastRoundArray.get(NUMBER_OF_BIDDERS);

        return (CurrentNOBidders - PastNOBidders) / PastNOBidders;
    }

    private Integer calculateNumberOfConcurrentAuctions() {

        Integer CurrentNOConcurrentAuctions = CurrentRoundArray.get(NUMBER_OF_CONCURRENT_AUCTIONS);
        Integer PastNOConcurrentAuctions = PastRoundArray.get(NUMBER_OF_CONCURRENT_AUCTIONS);

        return (CurrentNOConcurrentAuctions - PastNOConcurrentAuctions) / PastNOConcurrentAuctions;
    }

    private Integer calculateOpeningBidValue(Integer roundNumber) {

        if (roundNumber == 1) {

            Integer CurrentOpenBidValue = CurrentRoundArray.get(OPENING_BID_VALUE);
            Integer PastOpenBidValue = PastRoundArray.get(OPENING_BID_VALUE);

            return (CurrentOpenBidValue - PastOpenBidValue) / PastOpenBidValue;
        }

        return 0;
    }

    private Integer calculateBidRate(Integer roundNumber) {

        if (roundNumber > 1) {

            Integer CurrentBidRate = CurrentRoundArray.get(OPENING_BID_VALUE);
            Integer PastBidRate = PastRoundArray.get(OPENING_BID_VALUE);

            return (CurrentBidRate - PastBidRate) / PastBidRate;
        }

        return 0;
    }
}