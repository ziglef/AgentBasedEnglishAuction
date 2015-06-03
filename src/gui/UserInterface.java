package gui;

import jadex.Jadex;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface {

    private static DefaultTableModel biddersTableModel;
    private static DefaultTableModel auctionTableModel;

    private static Jadex jadex;

    public static void main(String [] args) {

        jadex = new Jadex();

        JFrame window = new JFrame("TNEL - English Auction");
        JPanel mainPanel = new JPanel(new GridLayout(1, 4));

        biddersTableModel = new DefaultTableModel(){

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
                if (jadex.addNewBidderToApplication(biddersTableModel)) {
                   System.out.println("Created new bidder");
                } else {
                    System.out.println("Failed to create new bidder");
                }
            }
        });

        auctionTableModel = new DefaultTableModel(){

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
                if (jadex.addNewAuctionToApplication(auctionTableModel)) {
                    System.out.println("Created new auction");
                } else {
                    System.out.println("Failed to create new auction");
                }
            }
        });

        mainPanel.add(createBidderButton);
        mainPanel.add(createAuctionButton);
        mainPanel.add(new JScrollPane(biddersTable));
        mainPanel.add(new JScrollPane(auctionTable));

        window.add(mainPanel);
        window.setSize(1200, 500);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.validate();
        window.repaint();
    }
}
