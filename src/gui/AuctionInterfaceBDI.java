package gui;

import auctions.Auction;
import finalpriceprediction.Database;
import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Agent
@Description("This agent represents an auction interface")
@Service
@ProvidedServices(@ProvidedService(type= ICommunicationFromBidderService.class))
public class AuctionInterfaceBDI implements ICommunicationFromBidderService {

    @Agent
    private BDIAgent auctionAgent;

    @Belief
    private Auction auction;

    private DefaultTableModel productTableModel;
    private Integer productID;

    private int actualProduct;

    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    private Runnable task;

    private Database db;

    @AgentBody
    public void body() {
        auctionAgent.waitForDelay(100).get();

        db = Database.getInstance();
        productID = 0;
        actualProduct = 0;
        task = new Runnable() {
            public void run() {
                System.out.println("Acabou o leilao para o produto " + auction.getProduct(actualProduct).getName() +
                        " com um preco final de " + auction.getProduct(actualProduct).getCurrentPrice() +
                        " e foi ganho pelo agente " + auction.getProduct(actualProduct).getCurrentBidder());

                sendYouWonMessage(auction.getProduct(actualProduct).getCurrentBidder(), auction.getProduct(actualProduct).getName(), auction.getProduct(actualProduct).getCurrentPrice() );
                auction.addProductSold(auction.getProduct(actualProduct));

                actualProduct++;
                if( actualProduct < auction.getProducts().size() )
                    startAuction();
                else
                    System.out.println("Todos os productos do leilao " + auction.getName() + " foram leiloados.");
            }
        };

        auction = new Auction(auctionAgent.getAgentName());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        productTableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        JTable biddersTable = new JTable(productTableModel);

        productTableModel.addColumn("ID");
        productTableModel.addColumn("Name");
        productTableModel.addColumn("Starting Price");
        productTableModel.addColumn("Description");

        JButton createProductButton = new JButton("Create Product");
        JButton sendInvitationsButton = new JButton("Send Invitations");
        JButton startAuctionButton = new JButton("Start Auction");

        final JLabel productNameL = new JLabel("Product name:");
        final JComboBox<String> productNames = new JComboBox<>();
        final JLabel productPriceL = new JLabel("Product starting price:");
        final JLabel productDescriptionL = new JLabel("Product description:");
        final JTextField productDescription = new JTextField();

        double min = 0.00, value = 0.00, max = Double.MAX_VALUE, stepSize = 0.01;

        SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, stepSize);
        final JSpinner productPrice = new JSpinner(model);

        for (String pname : Product.PRODUCT_NAMES) {
            productNames.addItem(pname);
        }

        createProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Product p = new Product(productID, productNames.getSelectedItem().toString(), (double)productPrice.getValue(), productDescription.getText());
                auction.addProduct(p);
                productID++;

                productTableModel.addRow(new Object[]{p.getID(), p.getName(), p.getStartingPrice(), p.getDesc()});
            }
        });

        sendInvitationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendInvitations();
            }
        });

        startAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startAuction();
            }
        });

        mainPanel.setPreferredSize( new Dimension(600, 475) );
        leftPanel.setPreferredSize( new Dimension(575, 450) );
        JScrollPane biddersTableScrollable = new JScrollPane( biddersTable );
        biddersTableScrollable.setPreferredSize( new Dimension(600, 475) );

        mainPanel.add( leftPanel );
        mainPanel.add( biddersTableScrollable );

        leftPanel.add(productNameL);
        leftPanel.add(productNames);
        leftPanel.add(productPriceL);
        leftPanel.add(productPrice);
        leftPanel.add(productDescriptionL);
        leftPanel.add(productDescription);

        JPanel buttons = new JPanel( new GridLayout( 2, 1 ));

        JPanel buttonGroup1 = new JPanel( new GridLayout( 1, 2 ));
        buttonGroup1.add(createProductButton);
        buttonGroup1.add(sendInvitationsButton);

        buttons.add(buttonGroup1);
        buttons.add(startAuctionButton);

        leftPanel.add(buttons);

        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), auctionAgent.getAgentName())));
        UserInterface.getInstance().addAuctionInterface( mainPanel );
    }

    private void startAuction() {
        // Adicionar a base de dados os preços iniciais dos items
        for( Product p : auction.getProducts() ){
            db.addAvgStartingPrice( p.getName(), p.getStartingPrice() );
        }

        // Avisar os agentes que existe um novo item a ser licitado
        sendNewItemBeingAuctioned();

        // Definir timeout
        worker.schedule(task, 3, TimeUnit.SECONDS);
        // Quando nao houver mais bids durante o tempo do timeout o producto actual acaba e leiloa-se o proximo
        // Quando receber uma bid, se for valida actualiza pre�o actual e transmite aos agentes
    }

    /* ICommunicationFromAuctionService */
    private void sendNewItemBeingAuctioned() {
        SServiceProvider.getServices(auctionAgent.getServiceProvider(), ICommunicationFromAuctionService.class, RequiredServiceInfo.SCOPE_PLATFORM)
                .addResultListener(new IntermediateDefaultResultListener<ICommunicationFromAuctionService>()
                {
                    public void intermediateResultAvailable(ICommunicationFromAuctionService ts) {
                        System.out.println("Sending to all agents a notification stating that " + auction.getProduct(actualProduct).getName() + " is now being auctioned!");
                        ts.receiveNewItemBeingAuctioned(auctionAgent.getAgentName(), auction.getProduct(actualProduct));
                    }
                });
    }

    private void sendNewPriceNotification(final Product product) {
        SServiceProvider.getServices(auctionAgent.getServiceProvider(), ICommunicationFromAuctionService.class, RequiredServiceInfo.SCOPE_PLATFORM)
                .addResultListener(new IntermediateDefaultResultListener<ICommunicationFromAuctionService>()
                {
                    public void intermediateResultAvailable(ICommunicationFromAuctionService ts) {
                        System.out.println("Sending to all agents a notification stating that " + auction.getProduct(actualProduct).getName() + " has had its price changed!");
                        ts.receiveNewPrice(auctionAgent.getAgentName(), product);
                    }
                });
    }

    private void sendInvitations() {
        SServiceProvider.getServices(auctionAgent.getServiceProvider(), ICommunicationFromAuctionService.class, RequiredServiceInfo.SCOPE_PLATFORM)
                .addResultListener(new IntermediateDefaultResultListener<ICommunicationFromAuctionService>()
                {
                    public void intermediateResultAvailable(ICommunicationFromAuctionService ts) {
                        ts.receiveInvitation(auctionAgent.getAgentName(), auction.getProducts());
                    }
                });
    }

    private void sendAuctionInfo(final String bidder) {
        SServiceProvider.getServices(auctionAgent.getServiceProvider(), ICommunicationFromAuctionService.class, RequiredServiceInfo.SCOPE_PLATFORM)
                .addResultListener(new IntermediateDefaultResultListener<ICommunicationFromAuctionService>()
                {
                    public void intermediateResultAvailable(ICommunicationFromAuctionService ts) {
                        ts.receiveAuctionInformation(bidder, auctionAgent.getAgentName(), auction.getProducts());
                    }
                });
    }

    private void sendYouWonMessage(final String currentBidder, final String productName, final double currentPrice) {
        SServiceProvider.getServices(auctionAgent.getServiceProvider(), ICommunicationFromAuctionService.class, RequiredServiceInfo.SCOPE_PLATFORM)
                .addResultListener(new IntermediateDefaultResultListener<ICommunicationFromAuctionService>()
                {
                    public void intermediateResultAvailable(ICommunicationFromAuctionService ts) {
                        ts.receiveWinNotification(auctionAgent.getAgentName(), currentBidder, productName, currentPrice);
                        db.addAvgFinalPrice(productName, currentPrice);
                        db.addAvgNoBidders(productName, (double)auction.getParticipants().size());
                    }
                });
    }

    /* ICommunicationFromBidderService */

    @Override
    public void acceptAuction(String bidder, String auctionName) {
        if (auctionName.equals(auctionAgent.getAgentName())) {
            System.out.println("Invitation accepted by " + bidder);
            auction.getParticipants().add(bidder);
        }
    }

    @Override
    public void askForAuction(String bidder) {
        sendAuctionInfo(bidder);
    }

    @Override
    public void receiveBidOnProduct(String bidder, Product product, Double value) {
        System.out.println("Received a bid from agent: " + bidder);
        System.out.println("He placed a bid on product " + product.getName() + " for " + value + ".");
        System.out.println("Current value for " + product.getName() + " is " + product.getCurrentPrice() + ".");
        System.out.println("Current bidder for " + product.getName() + " is " + product.getCurrentBidder() + ".");

        if( value > auction.getProduct(product).getCurrentPrice() ) {
            auction.getProduct(product).setCurrentPrice(value);
            auction.getProduct(product).setCurrentBidder(bidder);

            sendNewPriceNotification(auction.getProduct(product));
        }
    }
}
