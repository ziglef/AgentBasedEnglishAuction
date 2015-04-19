package auctions;

import products.Product;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Class that represents an auction without reputation or a price history
 *
 * @author Rui Grandão  - ei11010@fe.up.pt
 * @author André Silva - ei12133@fe.up.pt
 *
 */
public class Auction {
    ////////////////////
    // Private Fields //
    ////////////////////
    private String name;

    private ArrayList<Product> products;
    private int currentProduct;

    private ArrayList<String> participants;
    private JPanel gui;

    /////////////////
    // Constructor //
    /////////////////
    public Auction( String name, ArrayList<Product> products, ArrayList<String> participants ){
        this.name = name;
        this.products = products;
        this.currentProduct = 0;
        this.participants = participants;

        this.gui = new JPanel(true);
        this.initGui();
    }

    ////////////////////
    // Custom Methods //
    ////////////////////
    private void initGui(){
        // TODO: add code for GUI initialization for a given auction
    }

    /////////////////////////
    // Getters and Setters //
    /////////////////////////
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public JPanel getGui() { return gui; }

    public ArrayList<Product> getProducts() { return products; }
    public Product getProduct(Integer i){ return products.get(i); }

    public ArrayList<String> getParticipants() { return participants; }
    public String getParticipant(Integer i){ return participants.get(i); }
}
