package gui;

import javax.swing.*;

public class UserInterface {

    public static void main(String [] args) {

        JFrame window = new JFrame("TNEL - English Auction");
        JPanel mainPanel = new JPanel(true);
        JLabel testLabel = new JLabel("Hello World");

        mainPanel.add(testLabel);
        window.add(mainPanel);

        window.setSize(640, 480);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.validate();
        window.repaint();
    }
}
