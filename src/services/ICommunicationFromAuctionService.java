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
    public void sendInvitations(String auction, ArrayList<Product> products);
}
