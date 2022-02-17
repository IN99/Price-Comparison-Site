package eu.idreesn.javaCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  Product class for product object and maps to the table in DB.
 */
@Entity
@Table (name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="img_url")
    private String img_url;
    // Creates one to many relation between online_store table and junction table (online_store_product)
//    @OneToMany(mappedBy = "product")
//    private Set<Comparison> comparison = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
