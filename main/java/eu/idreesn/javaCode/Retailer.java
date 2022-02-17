package eu.idreesn.javaCode;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  Retailer class for retailer object and maps to the table in DB.
 */
@Entity
@Table (name="retailer")
public class Retailer {

    @Id
    @Column(name = "retailer_id")
    private int id;

    @Column(name = "retailer_name")
    private String name;

    @Column(name="retailer_logo")
    private String logoUrl;

//    @OneToMany(mappedBy = "retailer")
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
