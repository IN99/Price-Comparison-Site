package eu.idreesn.javaCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  Comparison class for comparison object and maps to the table in DB.
 */
@Entity
@Table (name="comparison")
public class Comparison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comp_id")
    private int id;

    @Column(name = "price")
    private double price;

    @Column(name = "url")
    private String url;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id",nullable = false)

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
//    private List<Product> product = new ArrayList<Product>();
    private Product product;

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne
    @JoinColumn(name="retailer_id", nullable=false)
    private Retailer retailer;

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double  price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}