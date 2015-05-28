package gui;

import jadex.Jadex;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface {

    private static DefaultTableModel biddersTableModel;

    private static Jadex jadex;

    public static void main(String [] args) {

        jadex = new Jadex();

        JFrame window = new JFrame("TNEL - English Auction");
        JPanel mainPanel = new JPanel(true);

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

        mainPanel.add(createBidderButton);
        mainPanel.add(new JScrollPane(biddersTable));

        window.add(mainPanel);
        window.setSize(640, 480);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.validate();
        window.repaint();
    }
}
