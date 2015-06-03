package gui;

import jadex.bdiv3.BDIAgent;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;
import products.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

@Agent
@Description("This agent represents an auction interface")
public class AuctionInterfaceBDI {

    @Agent
    protected BDIAgent auction;

    private DefaultTableModel biddersTableModel;
    private Integer productID;
    private ArrayList<Product> products;


    @AgentBody
    public void body() {
        auction.waitForDelay(100).get();

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
        final JLabel productPriceL = new JLabel("Product starting price:");
        final JLabel productDescriptionL = new JLabel("Product description:");
        final JTextField productDescription = new JTextField();

        double min = 0.00, value = 0.00, max = Double.MAX_VALUE, stepSize = 0.01;

        SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, stepSize);
        final JSpinner productPrice = new JSpinner(model);

        for (String pname : Product.PRODUCT_NAMES) {
            productNames.addItem( pname );
        }

        createProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Product p = new Product(productID, productNames.getSelectedItem().toString(), (double)productPrice.getValue(), productDescription.getText());
                products.add(p);
                productID++;

                biddersTableModel.addRow(new Object[]{p.getID(), p.getName(), p.getStartingPrice(), p.getDesc()});
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

        window.add(mainPanel);
        window.setSize(640, 480);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.validate();
        window.repaint();
    }
}
