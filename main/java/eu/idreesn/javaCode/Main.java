package eu.idreesn.javaCode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main class for example threaded web scraper
 */
public class Main {
    public static void main(String[] args) {

        // Configures Spring annotation
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Injects dependencies into scraper manager
        ScraperManager scraperManager = (ScraperManager) context.getBean("scraperManager");

        // Initializes all scrapers
        scraperManager.initializeScraperManager();

    }
}
