package products;

/**
 * Class that represents a product without any price history
 *
 * @author Rui Grandão  - ei11010@fe.up.pt
 * @author André Silva - ei12133@fe.up.pt
 *
 */
public class Product {
    ////////////////////
    // Private Fields //
    ////////////////////
    private String name;
    private String desc;

    private Float startingPrice;

    //////////////////
    // Constructors //
    //////////////////
    public Product (String name, Float startingPrice){
        this.name = name;
        this.startingPrice = startingPrice;
    }

    public Product (String name, Float startingPrice, String desc){
        this(name, startingPrice);
        this.desc = desc;
    }

    /////////////////////////
    // Getters and Setters //
    /////////////////////////
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public Float getStartingPrice() { return startingPrice; }
    public void setStartingPrice(Float startingPrice) { this.startingPrice = startingPrice; }

}

