package products;

/**
 * Class that represents a product without any price history
 *
 * @author Rui Grand�o  - ei11010@fe.up.pt
 * @author Andr� Silva - ei12133@fe.up.pt
 *
 */
public class Product {
    public static String[] PRODUCT_NAMES = { "MEIAS", "MONITOR", "TECLADO", "RATO", "TAPETE" };
    ////////////////////
    // Private Fields //
    ////////////////////
    private Integer ID;
    private String name;
    private String desc;

    private double startingPrice;

    //////////////////
    // Constructors //
    //////////////////
    public Product (Integer ID, String name, double startingPrice){
        this.ID = ID;
        this.name = name;
        this.startingPrice = startingPrice;
    }

    public Product (Integer ID, String name, double startingPrice, String desc){
        this(ID, name, startingPrice);
        this.desc = desc;
    }

    /////////////////////////
    // Getters and Setters //
    /////////////////////////
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public double getStartingPrice() { return startingPrice; }
    public void setStartingPrice(Float startingPrice) { this.startingPrice = startingPrice; }

    public Integer getID() { return ID; }
}

