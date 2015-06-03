package services;

import auctions.Auction;
import products.Product;

import java.util.ArrayList;

/**
 * Created on 03/06/15.
 *
 * @author André Silva
 * @author Rui Grandão
 */
public interface ICommunicationFromAuctionService {
    public void receiveInvitation(String auction, ArrayList<Product> products);
    public void sendAuctionInformation(String bidder, String auction, Auction auctionObject);
}
