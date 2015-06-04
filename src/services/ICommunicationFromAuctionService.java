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
}
