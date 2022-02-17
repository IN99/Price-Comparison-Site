package eu.idreesn.javaCode;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * Contains annotations and deals with the dependencies between classes
 */
@Configuration
public class AppConfig {
    SessionFactory sessionFactory;


    /**
     * Sets up the session factory and it creates instances of sessions when a connection to a database is requested.
     *
     * @return session factory object
     */
    @Bean
    public SessionFactory sessionFactory() {
        if (sessionFactory == null) {//Build sessionFatory once only
            try {
                //Create a builder for the standard service registry
                StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

                //Load configuration from hibernate configuration file.
                //Here we are using a configuration file that specifies Java annotations.
                standardServiceRegistryBuilder.configure("resources/hibernate.cfg.xml");

                //Create the registry that will be used to build the session factory
                StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
                try {
                    //Create the session factory - this is the goal of the init method.
                    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
                } catch (Exception e) {
                        /* The registry would be destroyed by the SessionFactory,
                            but we had trouble building the SessionFactory, so destroy it manually */
                    System.err.println("Session Factory build failed.");
                    e.printStackTrace();
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                //Ouput result
                System.out.println("Session factory built.");
            } catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                System.err.println("SessionFactory creation failed." + ex);
                ex.printStackTrace();
            }
        }
        return sessionFactory;
    }

    /**
     * Constructs a bean for the hibernate DAO.
     *
     * @return Hibernate DAO
     */
    @Bean
    public HibernateDao productsDao() {
        HibernateDao productsDao = new HibernateDao();
        // Injects sessionFactiory dependency
        productsDao.setSessionFactory(sessionFactory());
        return productsDao;
    }

    /**
     * Scraper manager object that injects scraper beans into the scraper manager
     * @return scraper manager object
     */
    @Bean
    public ScraperManager scraperManager() {
        ScraperManager scraperManager = new ScraperManager();
        // All scraper objects are added to array list
        ArrayList<WebScraper> scraperList = new ArrayList<>();
        //   scraperList.add(mobilesScraper());
        scraperList.add(rsScraper());
        scraperList.add(muzikerScraper());
        scraperList.add(argosScraper());
        scraperList.add(currysScraper());
//        scraperList.add(bootsAppliancesScraper());
        // Sets up scraper manager
        scraperManager.setScraperList(scraperList);

        return scraperManager;
    }

    /**
     * Scraper object that injects scraper beans and hibernate into the Currys Scraper;
     *
     * @return scraper object
     */
    @Bean
    public CurrysScraper currysScraper() {
        CurrysScraper currysScraper = new CurrysScraper();
        currysScraper.setRetailer(currysRetailer());
        ArrayList<String> pageList = new ArrayList<>();
        pageList.add("1_50/relevance-desc/xx-criteria.html");
        pageList.add("2_50/relevance-desc/xx-criteria.html");
        pageList.add("3_50/relevance-desc/xx-criteria.html");
        pageList.add("4_50/relevance-desc/xx-criteria.html");
        pageList.add("5_50/relevance-desc/xx-criteria.html");

        currysScraper.setPageList(pageList);
        // Initializes SessionFactory
        currysScraper.setHibernateDAO(productsDao());

        currysScraper.setProductsAdded(false);

        // Returns scraper object
        return currysScraper;
    }

    /**
     * Scraper object that injects scraper beans and hibernate into the Argos Scraper;
     *
     * @return scraper object
     */
    @Bean
    public ArgosScraper argosScraper() {
        ArgosScraper argosScraper = new ArgosScraper();
        argosScraper.setRetailer(argosRetailer());
        ArrayList<String> pageList = new ArrayList<>();
        pageList.add("");
        pageList.add("opt/page:2/");
        pageList.add("opt/page:3/");

        argosScraper.setPageList(pageList);
        // Initializes SessionFactory
        argosScraper.setHibernateDAO(productsDao());

        argosScraper.setProductsAdded(false);

        // Returns scraper object
        return argosScraper;
    }

    //**Did not have time to fully implement this Scraper**

//    public AoScraper aoScraper() {
//        AoScraper aoScraper = new AoScraper();
//        aoScraper.setRetailer(aoRetailer());
//        ArrayList<String> pageList = new ArrayList<>();
//        pageList.add("?pagesize=60");
//        pageList.add("?page=60&pagesize=60");
//        pageList.add("?page=120&pagesize=60");
////        pageList.add("headphones/1/107-164-178/?page=120&pagesize=60");
////        pageList.add("?page=240&pagesize=60&search=Wireless%20Headphones");
//
//        aoScraper.setPageList(pageList);
//        // Initializes SessionFactory
//        aoScraper.setHibernateDAO(productsDao());
//
//        // Returns scraper object
//        return aoScraper;
//    }
//
//    @Bean
//    public Retailer aoRetailer() {
//        // Creates online store object
//        Retailer aoRetailer = new Retailer();
//
//        // Sets up the object with a set of data
////        aoRetailer.setId(4);
//        aoRetailer.setName("AO");
//        aoRetailer.setLogoUrl("n/a");
//
//        // Returns online store object
//        return aoRetailer;
//    }

    /**
     * Scraper object that injects scraper beans and hibernate into the Richer Sounds Scraper;
     *
     * @return scraper object
     */
    @Bean
    public RsScraper rsScraper() {
        RsScraper rsScraper = new RsScraper();
        rsScraper.setRetailer(rsRetailer());
        ArrayList<String> pageList = new ArrayList<>();
        pageList.add("?p=1");
        pageList.add("?p=2");
        pageList.add("?p=3");
        pageList.add("?p=4");
        pageList.add("?p=5");
        pageList.add("?p=6");
        pageList.add("?p=7");


        rsScraper.setPageList(pageList);
        // Initializes SessionFactory
        rsScraper.setHibernateDAO(productsDao());

        rsScraper.setProductsAdded(false);

        // Returns scraper object
        return rsScraper;
    }

    /**
     * Scraper object that injects scraper beans and hibernate into the Muziker Scraper;
     *
     * @return scraper object
     */
    @Bean
    public MuzikerScraper muzikerScraper() {
        MuzikerScraper muzikerScraper = new MuzikerScraper();
        muzikerScraper.setRetailer(muzikerRetailer());
        ArrayList<String> pageList = new ArrayList<>();
        pageList.add("&per=60&page=1&sort_by=sort_score%20desc");
        pageList.add("&per=60&page=2&sort_by=sort_score%20desc");
        pageList.add("&per=60&page=3&sort_by=sort_score%20desc");
        pageList.add("per=60&page=4&sort_by=sort_score%20desc");

        muzikerScraper.setPageList(pageList);
        // Initializes SessionFactory
        muzikerScraper.setHibernateDAO(productsDao());

        muzikerScraper.setProductsAdded(false);

        // Returns scraper object
        return muzikerScraper;
    }

    /**
     * rsRetailer method creates a retailer bean for Richer Sounds;
     *
     * @return retailer object
     */
    @Bean
    public Retailer rsRetailer() {
        // Creates online store object
        Retailer rsRetailer = new Retailer();

        // Sets up the object with a set of data
        rsRetailer.setId(1);
        rsRetailer.setName("Richer Sounds");
        rsRetailer.setLogoUrl("https://www.richersounds.com/media/logo/stores/1/logo_1.png");

        // Returns online store object
        return rsRetailer;
    }

    /**
     * muzikerRetailer method creates a retailer bean for Muziker;
     *
     * @return retailer object
     */
    @Bean
    public Retailer muzikerRetailer() {
        // Creates online store object
        Retailer muzikerRetailer = new Retailer();

        // Sets up the object with a set of data
        muzikerRetailer.setId(2);
        muzikerRetailer.setName("Muziker");
        muzikerRetailer.setLogoUrl("https://muzikercdn.com/assets/muziker-logo-37bb35d4a20bff1c12c43a5067c6864aef0b57d30052a93d824295bfb135ea4d.png");

        // Returns online store object
        return muzikerRetailer;
    }

    /**
     * argosRetailer method creates a retailer bean for Argos;
     *
     * @return retailer object
     */
    @Bean
    public Retailer argosRetailer() {
        // Creates online store object
        Retailer argosRetailer = new Retailer();

        // Sets up the object with a set of data
        argosRetailer.setId(3);
        argosRetailer.setName("Argos");
        argosRetailer.setLogoUrl("https://media.4rgos.it/i/Argos/logo_argos2x?w=120&qlt=75&fmt=png");

        // Returns online store object
        return argosRetailer;
    }

    /**
     * currysRetailer method creates a retailer bean for Currys;
     *
     * @return retailer object
     */
    @Bean
    public Retailer currysRetailer() {
        // Creates online store object
        Retailer currysRetailer = new Retailer();

        // Sets up the object with a set of data
        currysRetailer.setId(4);
        currysRetailer.setName("Currys PC World");
        currysRetailer.setLogoUrl("https://currys-ssl.cdn.dixons.com/grafx/images/logos/logo.png");

        // Returns online store object
        return currysRetailer;
    }

}
