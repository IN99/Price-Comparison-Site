package eu.idreesn.javaCode;

import org.hibernate.Session;
import org.hibernate.SessionFactory;


import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 *  HibernateDao contains methods used to query and manipulate the  database.
 */
public class HibernateDao {
    SessionFactory sessionFactory;

    public void saveProduct(Product product) {
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        session.save(product);

        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("Product added to database with ID: " + product.getId());

    }

    /**
     * The saveRetailer method saves a new retailer to the database.
     *
     * @param retailer
     */
    public void saveRetailer(Retailer retailer) {
        Session session = sessionFactory.getCurrentSession();
        //Start transaction
        session.beginTransaction();

        //Add product to database - will not be stored until we commit the transaction
        session.save(retailer);

        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("Retailer Added: " + retailer.getId() + " " + retailer.getName());
    }

    /**
     * The saveOrUpdateComparison adds a new comparison to the table.
     *
     * @param  comparison
     */
    public void saveOrUpdateComparison(Comparison comparison) {
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        //Add to database - will not be stored until we commit the transaction
        session.saveOrUpdate(comparison);

        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("Comparison added to database with ID: " + comparison.getId());
    }

    /**
     * The findProduct method queries the database and finds a product with same name.
     * @param name
     * @return list of products with same name, null if none found .
     */
    public List<Product> findProduct(String name) {
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();
        //Start transaction
        session.beginTransaction();
        String hql = "from Product where LOWER(REPLACE(name, ' ',''))= :name";
        Query query = session.createQuery(hql);
        query.setParameter("name", name);
        List<Product> productList = query.getResultList();
        session.close();
        return productList;
    }


    /**
     * The findComparison method queries the database and finds a comparison with same url.
     * @param url
     * @return list of comparison with same url, null if none found .
     */
    public List<Comparison> findComparison(String url) {
        //Get a new Session instance from the session factory and start transaction
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        //Get class
        List<Comparison> comparisonList = session.createQuery("from Comparison where url='" + url + "'").getResultList();
        session.close();
        return comparisonList;
    }

    /**
     * The getProductList method queries the database and will a return list of all products in the product table.
     * @return list of all products in the database.
     */
    public List<Product> getProductList() {
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        // get list of available categories
        List<Product> productList = (ArrayList<Product>) session.createQuery("from Product").getResultList();

        //Close the session and release database connection
        session.close();
        return productList;
    }

    /**
     * The findRetailer method queries the database and finds a retailer with same name.
     * @param name
     * @return list of retailer with same name, null if none found .
     */
    public List<Retailer> findRetailer(String name) {
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        List<Retailer> retailerList = (ArrayList<Retailer>) session.createQuery("from Retailer where retailer_name = '" + name + "'").getResultList();

        session.close();
        return retailerList;
    }


    public void deleteComparison(Comparison comparison) {
        //Get a new Session instance from the session factory and start transaction
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.delete(comparison);

        //Commit transaction to save it to database
        session.getTransaction().commit();
        session.close();
    }


    public void shutDown() {
        sessionFactory.close();
    }


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
