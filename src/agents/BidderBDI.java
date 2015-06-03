package agents;

import gui.UserInterface;
import jadex.bdiv3.BDIAgent;
import jadex.bridge.service.annotation.Service;
import jadex.micro.annotation.*;
import products.Product;
import services.ICommunicationFromAuctionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 19/04/15.
 *
 * @author André Silva
 * @author Rui Grandão
 */

@Agent
@Description("This agent represents a bidder")
@Service
@ProvidedServices(@ProvidedService(type=ICommunicationFromAuctionService.class))
public class BidderBDI implements ICommunicationFromAuctionService {

    @Agent
    protected BDIAgent bidder;

    protected List<String> auctions;

    protected List<WishListProduct> wishlist;

    private DefaultTableModel productsTableModel;

    class WishListProduct {

        private String name;
        private double desirePrice;

        public WishListProduct(String name) {
            this.name = name;
            desirePrice = 0.0;
        }

        public WishListProduct(String name, double price) {
            this.name = name;
            desirePrice = price;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public double getDesirePrice() { return desirePrice; }
        public void setDesirePrice(double desirePrice) { this.desirePrice = desirePrice; }
    }

    @AgentBody
    public void body() {
        bidder.waitForDelay(100).get();

        auctions = new ArrayList<>();
        wishlist = new ArrayList<>();

        /* PRODUCT TAB - START */
        JPanel productEditPanel = new JPanel(new GridLayout(5, 1));

        /* PRODUCT EDIT - START */
        JLabel productNameL = new JLabel("Product name:");

        final JComboBox<String> productNames = new JComboBox<>();
        for (String pname : Product.PRODUCT_NAMES) {
            productNames.addItem(pname);
        }

        JLabel productPriceL = new JLabel("Product name:");
        double min = 0.00, value = 0.00, max = Double.MAX_VALUE, stepSize = 0.01;

        SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, stepSize);
        final JSpinner productPrice = new JSpinner(model);

        JButton addProductWishlistButton = new JButton("Add Product Wishlist");
        addProductWishlistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productName = productNames.getSelectedItem().toString();
                double desiredPrice = (double)productPrice.getValue();

                if (!wishlistContains(productName, desiredPrice)) {
                    productsTableModel.addRow(new Object[]{productName, desiredPrice});
                    wishlist.add(new WishListProduct(productName, desiredPrice));
                }
            }
        });

        productEditPanel.add(productNameL);
        productEditPanel.add(productNames);
        productEditPanel.add(productPriceL);
        productEditPanel.add(productPrice);
        productEditPanel.add(addProductWishlistButton);

        /* PRODUCT EDIT - END */

        /* PRODUCT TABLE - START */
        productsTableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        JTable productsTable = new JTable(productsTableModel);

        productsTableModel.addColumn("Product");
        productsTableModel.addColumn("Desired Price");
        /* PRODUCT TABLE - END */

        JPanel productPanel = new JPanel(new GridLayout(1, 2));
        productPanel.add(productEditPanel);
        productPanel.add(new JScrollPane(productsTable));
        /* PRODUCT TAB - END */

        /* AUCTION TAB - START */
        JPanel auctionsPanel = new JPanel(new GridLayout(1, 2));
        /* AUCTION TAB - END */

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Products", productPanel);
        tabbedPane.add("Auctions", auctionsPanel);

        JPanel finalPanel = new JPanel();
        finalPanel.setPreferredSize( new Dimension(600, 475) );
        tabbedPane.setPreferredSize( new Dimension(575, 450) );
        finalPanel.add(tabbedPane);
        finalPanel.setBorder( BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), bidder.getAgentName() ) );

        UserInterface.getInstance().addAgentInterface( finalPanel );
    }

    private boolean wishlistContains(String name, double price) {

        for(WishListProduct product : wishlist) {
            if (name.equals(product.getName()) && product.getDesirePrice() == price)
                return true;
        }

        return false;
    }

    @Override
    public void receiveInvitation(String auction, ArrayList<Product> products) {

        if (!auctions.contains(auction)) {
            System.out.println("Bidder received invitation from auction: " + auction);

            boolean auctionWasAdded = false;

            for (Product product : products) {
                for (WishListProduct wishProduct : wishlist) {
                    if (wishProduct.getName().equals(product.getName())) {

                        System.out.println("Product: " + product.getName());
                        System.out.println("Adding auction: " + auction);
                        auctions.add(auction);
                        auctionWasAdded = true;
                        break;
                    }
                }
                if (auctionWasAdded)
                    break;
            }
        }
    }
}
