package gui;

import products.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AuctionInterfaceBDI {

    private DefaultTableModel biddersTableModel;
    private Integer productID;
    private ArrayList<Product> products;

    public AuctionInterfaceBDI() {

        productID = 0;
        products = new ArrayList<>();

        JFrame window = new JFrame("TNEL - English Auction");

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

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
        biddersTableModel.addColumn("Starting Price");
        biddersTableModel.addColumn("Description");

        JButton createProductButton = new JButton("Create Product");

        final JLabel productNameL = new JLabel("Product name:");
        final JComboBox<String> productNames = new JComboBox<>();
        final JLabel productStartingPriceL = new JLabel("Product starting price:");
        final JTextField productStartingPrice = new JTextField();
        final JLabel productDescriptionL = new JLabel("Product description:");
        final JTextField productDescription = new JTextField();

        for( String pname : Product.PRODUCT_NAMES ){
            productNames.addItem( pname );
        }

        createProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Product p = new Product(productID, productNames.getSelectedItem().toString(), Float.valueOf(productStartingPrice.getText()), productDescription.getText());
                products.add(p);
                productID++;

                biddersTableModel.addRow(new Object[]{p.getID(), p.getName(), p.getStartingPrice(), p.getDesc()});
            }
        });

        mainPanel.add(leftPanel);
        mainPanel.add(new JScrollPane(biddersTable));

        leftPanel.add(productNameL);
        leftPanel.add(productNames);
        leftPanel.add(productStartingPriceL);
        leftPanel.add(productStartingPrice);
        leftPanel.add(productDescriptionL);
        leftPanel.add(productDescription);
        leftPanel.add(createProductButton);

        window.add(mainPanel);
        window.setSize(640, 480);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.validate();
        window.repaint();
    }
}
