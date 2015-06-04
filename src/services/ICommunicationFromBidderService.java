package services;

import products.Product;

/**
 * Created on 03/06/15.
 *
 * @author André Silva
 * @author Rui Grandão
 */
public interface ICommunicationFromBidderService {
    void acceptAuction(String bidder, String auctionName);
    void askForAuction(String bidder);
    void receiveBidOnProduct(String bidder, Product product, Double value);
}
