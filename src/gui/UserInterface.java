package gui;

import jadex.Jadex;

import javax.swing.*;
import java.awt.*;

public class UserInterface extends JFrame {
    private static UserInterface instance;

    private Jadex jadex;

    private JTabbedPane tabs;
    private GeneralInterface generalInterface;
    private JPanel agentsPanel;
    private JScrollPane agentsScrollablePanel;
    private JPanel auctionsPanel;
    private JScrollPane auctionsScrollablePanel;

    // Gets the current instance of the user interface or creates it if none exist //
    public static synchronized UserInterface getInstance(){
        if( instance == null )
            instance = new UserInterface();

        return instance;
    }

    // Constructor called when no user interface exists //
    private UserInterface() {
        // Create the JFrame
        super("TNEL - English Auction");
        // Create the Jadex platform
        jadex = new Jadex();

        // Create the tabbed panel
        this.tabs = new JTabbedPane();

        // Create the general interface for the application and at it to its respective tab
        this.generalInterface = GeneralInterface.getInstance();
        this.tabs.addTab("General", generalInterface);

        // Create the agents panel and add it to the tabs
        agentsPanel = new JPanel( new GridLayout( 0, 3 ) );
        agentsScrollablePanel = new JScrollPane( agentsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        this.tabs.addTab("Agents", agentsScrollablePanel);

        // Create the auctions panel and add it to the tabs
        auctionsPanel = new JPanel( new GridLayout(0, 3) );
        auctionsScrollablePanel = new JScrollPane( auctionsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        this.tabs.addTab("Auctions", auctionsScrollablePanel);

        this.add(tabs);
        this.setSize(1920, 1080);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.validate();
        this.repaint();
    }

    public Jadex getJadex() {
        return jadex;
    }

    public void addAgentInterface( JPanel panel ){
        this.agentsPanel.add( panel );
    }

    public void addAuctionInterface( JPanel panel ){
        this.auctionsPanel.add( panel );
    }
}
