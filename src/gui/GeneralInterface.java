package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeneralInterface extends JPanel{

    private DefaultTableModel biddersTableModel;
    private DefaultTableModel auctionTableModel;
    private static GeneralInterface instance;

    public static synchronized GeneralInterface getInstance(){
        if( instance == null )
            instance = new GeneralInterface();

        return instance;
    }

    private GeneralInterface() {
        super(new GridLayout(1, 3));

        biddersTableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        JTable biddersTable = new JTable(biddersTableModel);

        biddersTableModel.addColumn("ID");
        biddersTableModel.addColumn("Name");
        biddersTableModel.addColumn("Type");

        final CheckboxGroup agentTypes = new CheckboxGroup();
        Checkbox agentSimple = new Checkbox("Simple", true, agentTypes);
        Checkbox agentRandom = new Checkbox("Random", false, agentTypes);
        Checkbox agentFPP = new Checkbox("Pondered", false, agentTypes);

        JButton createBidderButton = new JButton("Create Bidder");
        createBidderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bidderType = agentTypes.getSelectedCheckbox().getLabel();
                if (UserInterface.getInstance().getJadex().addNewBidderToApplication(biddersTableModel, bidderType)) {
                    System.out.println("Created new bidder of type " + bidderType);
                } else {
                    System.out.println("Failed to create new bidder");
                }
            }
        });

        auctionTableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        JTable auctionsTable = new JTable(auctionTableModel);

        auctionTableModel.addColumn("ID");
        auctionTableModel.addColumn("Auction Name");

        JButton createAuctionButton = new JButton("Create Auction");
        createAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (UserInterface.getInstance().getJadex().addNewAuctionToApplication(auctionTableModel)) {
                    System.out.println("Created new auction");
                } else {
                    System.out.println("Failed to create new auction");
                }
            }
        });

        JPanel leftPanel = new JPanel( new GridLayout(2, 1));

        JPanel bidderButtonPanel = new JPanel();
        bidderButtonPanel.add( createBidderButton );
        JPanel auctionButtonPanel = new JPanel();
        auctionButtonPanel.add( createAuctionButton );

        JLabel auctionImage = new JLabel(new ImageIcon("assets/auction.jpg"));
        auctionImage.setPreferredSize( new Dimension( 700, 706 ));

        JPanel buttonsPanel = new JPanel( new GridLayout(1, 2) );
        buttonsPanel.add(bidderButtonPanel);
        buttonsPanel.add(auctionButtonPanel);

        JPanel optionsPanel = new JPanel( new GridLayout(3, 1) );
        optionsPanel.add(agentSimple);
        optionsPanel.add(agentRandom);
        optionsPanel.add(agentFPP);
        optionsPanel.setPreferredSize( new Dimension(300, 100) );

        JPanel agentsPanel = new JPanel( new GridLayout(2, 1) );

        agentsPanel.add( optionsPanel );
        agentsPanel.add( buttonsPanel );

        leftPanel.add( auctionImage );
        leftPanel.add( agentsPanel );

        leftPanel.setPreferredSize( new Dimension(620, 1000) );

        JScrollPane biddersScrollPane = new JScrollPane(biddersTable);
        JScrollPane auctionsScrollTable = new JScrollPane(auctionsTable);

        biddersScrollPane.setPreferredSize( new Dimension( 500, 900 ));
        auctionsScrollTable.setPreferredSize( new Dimension( 500, 900 ));

        JPanel centerLeftPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ));
        JLabel dummy = new JLabel();
        dummy.setPreferredSize( new Dimension(100, 115) );
        centerLeftPanel.add( dummy );
        centerLeftPanel.add(leftPanel);

        this.add( centerLeftPanel );
        this.add( biddersScrollPane );
        this.add( auctionsScrollTable );
    }
}
