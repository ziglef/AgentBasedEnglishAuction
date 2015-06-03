package gui;

import auctions.Auction;
import jadex.bridge.service.annotation.Service;
import products.Product;
import services.ICommunicationFromAuctionService;

import jadex.bdiv3.BDIAgent;
import jadex.bdiv3.annotation.Belief;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.*;
import services.ICommunicationFromBidderService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Agent
@Description("This agent represents an auction interface")
@Service
@ProvidedServices(@ProvidedService(type= ICommunicationFromBidderService.class))
public class AuctionInterfaceBDI implements ICommunicationFromBidderService {

    @Agent
    protected BDIAgent auctionAgent;

    @Belief
    protected Auction auction;

    private DefaultTableModel productTableModel;
    private Integer productID;


    @AgentBody
    public void body() {
        auctionAgent.waitForDelay(100).get();

        productID = 0;

        auction = new Auction(auctionAgent.getAgentName());

        JFrame window = new JFrame("English Auction");

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
                auction.getProducts().add(p);
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

        mainPanel.add(leftPanel);
        mainPanel.add(new JScrollPane(biddersTable));

        leftPanel.add(productNameL);
        leftPanel.add(productNames);
        leftPanel.add(productPriceL);
        leftPanel.add(productPrice);
        leftPanel.add(productDescriptionL);
        leftPanel.add(productDescription);
        leftPanel.add(createProductButton);
        leftPanel.add(sendInvitationsButton);

        window.add(mainPanel);
        window.setSize(640, 480);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.validate();
        window.repaint();
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

    /* ICommunicationFromBidderService */

    @Override
    public void acceptAuction(String bidder, String auctionName) {
        if (auctionName.equals(auctionAgent.getAgentName())) {
            System.out.println("Invitation accepted by " + bidder);
            auction.getParticipants().add(bidder);
        }
    }
}
