package eu.idreesn.javaCode;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

/**
 * Selenium Chrome Driver to access data from Richer Sounds website
 * that load data dynamically with JavaScript
 */
public class RsScraper extends WebScraper {
//    Product product= new Product();


    /**
     * Scrapes data from website and
     gets each product out of the list of all scraped products.
     */
    public void scrapeAll() {
//        //We need an options class to run headless - not needed if we want default options
//        List<Product> productList = getHibernateDAO().getProductList();

        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\ChromiumDriver\\chromedriver.exe");
        //Create instance of web driver - this must be on the path.
        WebDriver driver = new ChromeDriver(options);

        if (getHibernateDAO().findRetailer("Richer Sounds").size() == 0) {
            getHibernateDAO().saveRetailer(getRetailer());
        }

        for (int x = 0; x < getPageList().size(); x++) {
            System.out.println(getPageList().get(x));
            driver.get("https://www.richersounds.com/headphones/wireless-headphones.html?brand=5556%2C5112%2C2637%2C145%2C142%2C24%2C107%2C22%2C161%2C172/" + getPageList().get(x));

            //Navigate Chrome to page.
            //Wait for page to load
            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            List<WebElement> phoneList = driver.findElements(By.cssSelector("div.products.wrapper.grid.products-grid.clearfix > ol > li:nth-child(n)"));
            List<WebElement> descList = driver.findElements(By.cssSelector("div.products.wrapper.grid.products-grid.clearfix span.sku-product"));
            List<WebElement> nameList = driver.findElements(By.cssSelector("div.products.wrapper.grid.products-grid.clearfix > ol > li:nth-child(n) > div > div > div.product-info-first > h3 > a"));
            List<WebElement> priceList = driver.findElements(By.cssSelector("div.products.wrapper.grid.products-grid.clearfix span[data-price-type='finalPrice'] span.price"));
            List<WebElement> productUrlList = driver.findElements(By.cssSelector("div.products.wrapper.grid.products-grid.clearfix a.product.photo.product-item-photo"));
            List<WebElement> prodImageUrl = driver.findElements(By.cssSelector("span.product-image-wrapper > img"));


            for (int i = 0; i < phoneList.size(); i++) {
                String fullPrice = "0";
                String prodName = nameList.get(i).getText();
                String prodDesc = null;
                String prodImgUrl = prodImageUrl.get(i).getAttribute("srcset");
                String prodUrl = productUrlList.get(i).getAttribute("href");

                prodName=formatName(prodName);


                try {
                    fullPrice = priceList.get(i).getText().replaceAll("[£|p|/unit|']", "");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Could not get product price");
                    fullPrice = "0";
                }
                double price = 0;
                if (fullPrice.length() > 0) {
                    price = priceToDouble(fullPrice);
                }


                try {
                    prodDesc = descList.get(i).getText();
                } catch (IndexOutOfBoundsException e) {
                    prodDesc = "N/A";
                }

                List<Product> productResults = getHibernateDAO().findProduct(prodName.toLowerCase().replaceAll("\\s+",""));
                List<Product> productList = getHibernateDAO().getProductList();

                List<Comparison> comparisonList = getHibernateDAO().findComparison(prodUrl);


                if (productResults.size() == 0) {
                    Product product = new Product();
                    product.setName(prodName);
                    product.setDescription(prodDesc);
                    product.setImg_url(prodImgUrl);
                    Comparison comparison = new Comparison();
                    comparison.setProduct(product);
                    comparison.setUrl(prodUrl);
                    comparison.setPrice(price);
                    comparison.setRetailer(getRetailer());
                    getHibernateDAO().saveProduct(product);
                    getHibernateDAO().saveOrUpdateComparison(comparison);
                } else if (comparisonList.size() == 0) {
                    for (int z = 0; z < productList.size(); z++) {
                        String scrapedProduct = prodName.replaceAll("\\s+","");
                        // System.out.println("AO " + prodName);

                        String productStoredInDatabase = productList.get(z).getName().replaceAll("\\s+","");
                        //System.out.println("AO in DB " + productStoredInDatabase);

                        NormalizedLevenshtein normalizedLevenshtein = new NormalizedLevenshtein();
                        // System.out.println(normalizedLevenshtein.similarity(scrapedProduct, productStoredInDatabase));

                        if (productStoredInDatabase.equalsIgnoreCase(scrapedProduct)) {
                            Comparison com = new Comparison();
                            com.setProduct(productList.get(z));
                            com.setUrl(productUrlList.get(i).getAttribute("href"));
                            com.setPrice(price);
                            com.setRetailer(getRetailer());
                            getHibernateDAO().saveOrUpdateComparison(com);
                        }
                        ;
                    }
                } else {
                    System.out.println(prodName + " " + prodUrl + " Product already added to database");
                }
            }
        }
        setProductsAdded(true);
        driver.quit();
    }


    /**
     * Starts the thread.
     */
    public void run() {
        // While loop to make this bit run endlessly with sleep() every 3 sec
        while (true) {
            try {
                System.out.println("RS scraping");
                scrapeAll();
                sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * Removes unnecessary symbols from price and returns a cleaned price coverted into double
     */
    public double priceToDouble(String priceString) {
        double price = 0;
        String cleanedPriceString = priceString.replaceAll("[£|p|/unit|']", "");
        price = Double.parseDouble(cleanedPriceString);
        return price;
    }

    /**
     * Removes the extra words from product name to make sure only brand, model and colour remains
     */
    public String formatName(String prodName) {
        String removeExcessWord[] = {"(", ")"};
        String result = prodName;
        if(result.toLowerCase().contains("w/ Wireless Charging Case".toLowerCase())){
            result = result.replace("w/ Wireless Charging Case", "").trim().replaceAll(" +", " ");
        }
        for (int y = 0; y < removeExcessWord.length; ++y) {
            if (result.contains(removeExcessWord[y])) {
                result = result.replace(removeExcessWord[y], "").trim().replaceAll(" +", " ");
            }
        }
        return result;
    }

}
