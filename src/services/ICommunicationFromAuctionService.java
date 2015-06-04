package services;

import products.Product;

import java.util.ArrayList;

/**
 * Created on 03/06/15.
 *
 * @author André Silva
 * @author Rui Grandão
 */
public interface ICommunicationFromAuctionService {
    void receiveInvitation(String auction, ArrayList<Product> products);
    void receiveAuctionInformation(String bidder, String auction, ArrayList<Product> products);
    void receiveNewItemBeingAuctioned(String auction, Product product);
    void receiveNewPrice(String auction, Product product);
    void receiveWinNotification(String auction, String bidder, String product, double value);
}
