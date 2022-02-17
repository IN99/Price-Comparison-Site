package eu.idreesn.javaCode;

import java.util.ArrayList;

/**
 * An abstract web scraper class, extends Thread class.
 */
public abstract class WebScraper extends Thread {
    private Comparison comparisonProduct = new Comparison();
    private Retailer retailer;
    private HibernateDao hibernateDAO;
    private ArrayList<String> pageList = new ArrayList<>();
    private boolean productsAdded;

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public HibernateDao getHibernateDAO() {
        return hibernateDAO;
    }

    public void setHibernateDAO(HibernateDao hibernateDAO) {
        this.hibernateDAO = hibernateDAO;
    }

    public boolean isProductsAdded() {
        return productsAdded;
    }

    public void setProductsAdded(boolean productsAdded) {
        this.productsAdded = productsAdded;
    }
    public boolean areProductsAdded() {
        return productsAdded;
    }

    public ArrayList<String> getPageList() {
        return pageList;
    }

    public void setPageList(ArrayList<String> pageList) {
        this.pageList = pageList;
    }

    public Comparison getComparisonProduct() {
        return comparisonProduct;
    }

    public void setComparisonProduct(Comparison comparisonProduct) {
        this.comparisonProduct = comparisonProduct;
    }
}
