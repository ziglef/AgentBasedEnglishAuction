package gui;

import auctions.Auction;
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
import java.util.TimerTask;

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
    private boolean canWeMoveForwardPls;
    private java.util.Timer t;
    TimerTask ttask;


    @AgentBody
    public void body() {
        auctionAgent.waitForDelay(100).get();

        productID = 0;
        actualProduct = -1;
        canWeMoveForwardPls = true;
        t = new java.util.Timer(auctionAgent.getAgentName());
        ttask = new TimerTask() {
            @Override
            public void run() {
                canWeMoveForwardPls = true;
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
        // Loop until nao haver mais produtos
        while( actualProduct < auction.getProducts().size() ) {
            if( canWeMoveForwardPls ) {
                // resetar o canWeMoveForwardPls
                canWeMoveForwardPls = false;

                // Pegar num item e definir como item actual
                actualProduct++;
                // Avisar os agentes que existe um novo item a ser licitado
                warnNewItemBeingAuctioned(auction.getProduct(actualProduct));
                // Definir timeout
                // Quando nao houver mais bids durante o tempo do timeout o producto actual acaba e leiloa-se o proximo
                t.schedule(ttask, 3000);
                // Quando receber uma bid, se for valida actualiza pre�o actual e transmite aos agentes
                // TODO: fazer um listener para mensagens de bid em que o pre�o do item actual aumenta e o timer reinicia
                // reiniciar o timer implica fazer t.cancel e t = new java.Util.Timer(auctionAgent.getAgentName())
            }
        }
    }

    // TODO: send message to agents
    private void warnNewItemBeingAuctioned(Product product) {

    }

    /* ICommunicationFromAuctionService */

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
}
