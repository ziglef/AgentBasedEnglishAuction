package agents;


import gui.UserInterface;
import jadex.bdiv3.BDIAgent;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.search.SServiceProvider;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.*;
import products.Product;
import services.ICommunicationFromAuctionService;
import services.ICommunicationFromBidderService;

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
    protected BDIAgent bidderAgent;

    protected List<String> auctions;

    protected List<WishListProduct> wishlist;

    private DefaultTableModel productsTableModel;
    private DefaultTableModel auctionsTableModel;
    private DefaultTableModel productsWonTableModel;

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
        bidderAgent.waitForDelay(100).get();

        auctions = new ArrayList<>();
        wishlist = new ArrayList<>();

        /* PRODUCT TAB - START */
        JPanel productEditPanel = new JPanel(new GridLayout(6, 1));

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

        final JButton askAuctionsButton = new JButton("Ask For Auctions");
        askAuctionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getAuctionList();
            }
        });

        productEditPanel.add(productNameL);
        productEditPanel.add(productNames);
        productEditPanel.add(productPriceL);
        productEditPanel.add(productPrice);
        productEditPanel.add(addProductWishlistButton);
        productEditPanel.add(askAuctionsButton);

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
        auctionsTableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        auctionsTableModel.addColumn("Name");
        /* AUCTION TAB - END */

                /* AUCTION TAB - START */
        productsWonTableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        productsWonTableModel.addColumn("Auction");
        productsWonTableModel.addColumn("Name");
        productsWonTableModel.addColumn("Price");
        /* AUCTION TAB - END */

        JTable auctionsTable = new JTable(auctionsTableModel);
        JScrollPane auctionsScrollPanel = new JScrollPane(auctionsTable);

        JTable productsWonTable = new JTable(productsWonTableModel);
        JScrollPane productsScrollPanel = new JScrollPane(productsWonTable);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Products", productPanel);
        tabbedPane.add("Auctions", auctionsScrollPanel);
        tabbedPane.add("Products Bought", productsScrollPanel);

        JPanel finalPanel = new JPanel();
        finalPanel.setPreferredSize( new Dimension(600, 475) );
        tabbedPane.setPreferredSize( new Dimension(575, 450) );
        finalPanel.add(tabbedPane);
        finalPanel.setBorder( BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), bidderAgent.getAgentName() ) );

        UserInterface.getInstance().addAgentInterface( finalPanel );
    }

    private boolean wishlistContains(String name, double price) {

        for(WishListProduct product : wishlist) {
            if (name.equals(product.getName()) && product.getDesirePrice() == price)
                return true;
        }

        return false;
    }

    private boolean wishlistContains(String name) {

        for(WishListProduct product : wishlist) {
            if (name.equals(product.getName()))
                return true;
        }

        return false;
    }

    /* ICommunicationFromAuctionService */

    private double BOUND = 1.25;

    private boolean checkIfItsWorthItToBuyProduct(double price, double desiredPrice) {

        if (price <= desiredPrice * BOUND)
            return true;

        return false;
    }

    private void checkIfAuctionHasWishlistProductsAndAddIfYes(String auction, ArrayList<Product> products) {
        boolean auctionWasAdded = false;

        for (Product product : products) {
            for (WishListProduct wishProduct : wishlist) {
                if (wishProduct.getName().equals(product.getName())) {

                    if (checkIfItsWorthItToBuyProduct(product.getStartingPrice(), wishProduct.getDesirePrice())) {
                        System.out.println("Product: " + product.getName() + " Price: " + product.getStartingPrice());
                        System.out.println("Adding auction: " + auction);
                        auctions.add(auction);
                        auctionWasAdded = true;

                        enterAuction(auction);
                        break;
                    }
                }
            }
            if (auctionWasAdded)
                break;
        }
    }

    @Override
    public void receiveInvitation(String auction, ArrayList<Product> products) {

        if (!auctions.contains(auction)) {
            System.out.println("Bidder " + bidderAgent.getAgentName() + " received invitation from auction: " + auction);

            checkIfAuctionHasWishlistProductsAndAddIfYes(auction, products );
        }
    }

    @Override
    public void receiveAuctionInformation(String bidder, String auction, ArrayList<Product> products) {
        if (bidderAgent.getAgentName().equals(bidder) && !auctions.contains(auction)) {
            System.out.println("Auction " + auction + " is available");

            checkIfAuctionHasWishlistProductsAndAddIfYes(auction, products );
        }
    }

    @Override
    public void receiveNewItemBeingAuctioned(String auction, Product product) {
        if( wishlistContains(product.getName()) )
            sendBidOnProduct(bidderAgent.getAgentName(), product, product.getCurrentPrice()+1);
    }

    @Override
    public void receiveNewPrice(String auction, Product product) {
        WishListProduct currentProduct = null;
/*
        System.out.println("Wishlist:");
        for (WishListProduct wishProduct : wishlist) {
            System.out.println("Product: " + wishProduct.getName());
        }
        System.out.println("Product being auctioned: " + product.getName());
*/
        if( wishlistContains(product.getName()) ){
            for (WishListProduct wlProduct : wishlist) {
                if (wlProduct.getName().equals(product.getName())) {
                    currentProduct = wlProduct;
                    break;
                }
            }
            if (!product.getCurrentBidder().equals(bidderAgent.getAgentName()) && checkIfItsWorthItToBuyProduct(product.getCurrentPrice(), currentProduct.getDesirePrice()))
                sendBidOnProduct(bidderAgent.getAgentName(), product, product.getCurrentPrice()+1);
        }
    }

    @Override
    public void receiveWinNotification(String auction, String bidder, String product, double value) {
        if( bidder.equals(bidderAgent.getAgentName()) ){
            productsWonTableModel.addRow(new Object[]{auction, product, String.valueOf(value)});
        }
    }

    private void sendBidOnProduct(String agentName, final Product product, final double bid) {
        SServiceProvider.getServices(bidderAgent.getServiceProvider(), ICommunicationFromBidderService.class, RequiredServiceInfo.SCOPE_PLATFORM)
                .addResultListener(new IntermediateDefaultResultListener<ICommunicationFromBidderService>()
                {
                    public void intermediateResultAvailable(ICommunicationFromBidderService ts) {
                        ts.receiveBidOnProduct(bidderAgent.getAgentName(), product, bid);
                    }
                });
    }

    /* ICommunicationFromBidderService */

    private void enterAuction(final String auction) {
        SServiceProvider.getServices(bidderAgent.getServiceProvider(), ICommunicationFromBidderService.class, RequiredServiceInfo.SCOPE_PLATFORM)
                .addResultListener(new IntermediateDefaultResultListener<ICommunicationFromBidderService>()
                {
                    public void intermediateResultAvailable(ICommunicationFromBidderService ts) {
                        ts.acceptAuction(bidderAgent.getAgentName(), auction);
                    }
                });

        auctionsTableModel.addRow(new Object[]{auction});
    }

    private void getAuctionList() {
        SServiceProvider.getServices(bidderAgent.getServiceProvider(), ICommunicationFromBidderService.class, RequiredServiceInfo.SCOPE_PLATFORM)
                .addResultListener(new IntermediateDefaultResultListener<ICommunicationFromBidderService>()
                {
                    public void intermediateResultAvailable(ICommunicationFromBidderService ts) {
                        ts.askForAuction(bidderAgent.getAgentName());
                    }
                });
    }
}
