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
        super(new GridLayout(1, 4));

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


        JButton createBidderButton = new JButton("Create Bidder");
        createBidderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (UserInterface.getInstance().getJadex().addNewBidderToApplication(biddersTableModel)) {
                    System.out.println("Created new bidder");
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
        JTable auctionTable = new JTable(auctionTableModel);

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

        JPanel bidderButtonPanel = new JPanel();
        bidderButtonPanel.add( createBidderButton );
        this.add(bidderButtonPanel);

        JPanel auctionButtonPanel = new JPanel();
        bidderButtonPanel.add( createAuctionButton );
        this.add(auctionButtonPanel);

        this.add(new JScrollPane(biddersTable));
        this.add(new JScrollPane(auctionTable));
    }
}
