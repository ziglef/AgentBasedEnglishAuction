package products;

/**
 * Class that represents a product without any price history
 *
 * @author Rui Grand�o  - ei11010@fe.up.pt
 * @author Andr� Silva - ei12133@fe.up.pt
 *
 */
public class Product {
    public static String[] PRODUCT_NAMES = { "MONITOR", "TECLADO", "RATO", "TAPETE", "TELEMOVEL", "PORTATIL", "HEADSET", "CADEIRA", "MICROFONE"};
    ////////////////////
    // Private Fields //
    ////////////////////
    private Integer ID;
    private String name;
    private String desc;
    private String currentBidder;

    private double startingPrice;
    private double currentPrice;

    //////////////////
    // Constructors //
    //////////////////
    public Product (Integer ID, String name, double startingPrice){
        this.ID = ID;
        this.name = name;
        this.startingPrice = startingPrice;
        this.currentPrice = startingPrice;
        this.currentBidder = "";
    }

    public Product (Integer ID, String name, double startingPrice, String desc){
        this(ID, name, startingPrice);
        this.desc = desc;
    }

    public Product (Integer ID, String name){
        this(ID, name, 0.0);
        this.currentPrice = 0;
        this.desc = "Wishlist Product";
    }

    /////////////////////////
    // Getters and Setters //
    /////////////////////////
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public double getStartingPrice() { return startingPrice; }
    public void setStartingPrice(double startingPrice) { this.startingPrice = startingPrice; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public Integer getID() { return ID; }

    public String getCurrentBidder() { return currentBidder; }
    public void setCurrentBidder(String currentBidder) { this.currentBidder = currentBidder; }
}

