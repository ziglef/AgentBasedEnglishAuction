package agents;

import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
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

    protected List<Product> wishlist;

    private DefaultTableModel productsTableModel;
    private Integer productID;

    @AgentBody
    public void body() {
        bidder.waitForDelay(100).get();

        auctions = new ArrayList<>();
        wishlist = new ArrayList<>();
        productID = 0;

        JFrame window = new JFrame(bidder.getAgentName());

        /* PRODUCT TAB - START */
        JPanel productEditPanel = new JPanel(new GridLayout(3, 1));

        /* PRODUCT EDIT - START */
        final JLabel productNameL = new JLabel("Product name:");

        final JComboBox<String> productNames = new JComboBox<>();
        for (String pname : Product.PRODUCT_NAMES) {
            productNames.addItem(pname);
        }

        JButton addProductWishlistButton = new JButton("Add Product Wishlist");
        addProductWishlistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Product p = new Product(productID, productNames.getSelectedItem().toString());
                wishlist.add(p);
                productID++;

                productsTableModel.addRow(new Object[]{p.getID(), p.getName()});
            }
        });

        productEditPanel.add(productNameL);
        productEditPanel.add(productNames);
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

        productsTableModel.addColumn("ID");
        productsTableModel.addColumn("Name");
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

        window.setContentPane(tabbedPane);
        window.setSize(640, 480);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.validate();
        window.repaint();
    }

    @Override
    public void sendInvitations(String auction, ArrayList<Product> products) {

        if (!auctions.contains(auction)) {
            System.out.println("Bidder received invitation from auction: " + auction);

            boolean auctionWasAdded = false;

            for (Product product : products) {
                for (Product wishProduct : wishlist) {
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
