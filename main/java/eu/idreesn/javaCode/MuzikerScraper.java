package eu.idreesn.javaCode;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import info.debatty.java.stringsimilarity.*;
//import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.List;

/**
 * Selenium Chrome Driver to access data from Muziker website
 * that load data dynamically with JavaScript
 */
public class MuzikerScraper extends WebScraper {
    /**
     * Scrapes data from website and
       gets each product out of the list of all scraped products.
     */
    public void scrapeAll() {
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\ChromiumDriver\\chromedriver.exe");
        //Create instance of web driver - this must be on the path.
        WebDriver driver = new ChromeDriver(options);

        if(getHibernateDAO().findRetailer("Muziker").size() == 0){
            getHibernateDAO().saveRetailer(getRetailer());
        }

        for (int x = 0; x < getPageList().size(); x++) {
            System.out.println(getPageList().get(x));
            driver.get("https://www.muziker.co.uk/wireless-headphones?brands%5B%5D=akg&brands%5B%5D=apple&brands%5B%5D=audio-technica&brands%5B%5D=beats&brands%5B%5D=bose&brands%5B%5D=earfun&brands%5B%5D=marshall&brands%5B%5D=samsung&brands%5B%5D=sennheiser&brands%5B%5D=skullcandy" + getPageList().get(x));

            //Navigate Chrome to page.
            //Wait for page to load
            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //Output details for individual products
            List<WebElement> phoneList = driver.findElements(By.cssSelector(".col-6.col-sm-4.col-md-4.col-xl-3.col-xxl-3.d-flex"));
            List<WebElement> nameList = driver.findElements(By.cssSelector("div.row.flex-wrap.wrap-grid-toggler .product-tile-header"));
            List<WebElement> descList = driver.findElements(By.cssSelector("div.row.flex-wrap.wrap-grid-toggler div.tile-header p.p-sm.product-tile-sub-header"));
            List<WebElement> priceList = driver.findElements(By.cssSelector(".tile-price strong"));
            List<WebElement> prodImageUrl = driver.findElements(By.cssSelector("div.tile-img > picture > img"));
            List<WebElement> productUrlList = driver.findElements(By.cssSelector("div.row.flex-wrap.wrap-grid-toggler a.link-overlay"));
            for (int i = 0; i < phoneList.size(); i++) {
//                System.out.println(productUrlList.get(i));
                String fullPrice = "0";
                String prodName = nameList.get(i).getText();
                String prodDesc = null;
                String prodImgUrl = prodImageUrl.get(i).getAttribute("src");
                String prodUrl = productUrlList.get(i).getAttribute("href");
                String imageUrl = null;

                if(prodName.contains("MRXJ2ZM/A")){
                    continue;
                }

                prodName = formatName(prodName);
                // System.out.println(prodName);

                try {
                    fullPrice = priceList.get(i).getText().replaceAll("[£|p|/unit|']", "");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Could not get product price");
                    fullPrice = "0";
                }
                double price = 0;
                if (fullPrice.length() > 0) {
                    price = priceToDouble(fullPrice);
                    //System.out.println(price);
                }
                else{
                    fullPrice = "N/A";
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

                                String productStoredInDatabase = productList.get(z).getName().replaceAll("\\s+","");

                                if (productStoredInDatabase.equalsIgnoreCase(scrapedProduct)) {
                                    Comparison com = new Comparison();
                                    com.setProduct(productList.get(z));
                                    com.setUrl(prodUrl);
                                    com.setPrice(price);
                                    com.setRetailer(getRetailer());
                                    getHibernateDAO().saveOrUpdateComparison(com);
                                    System.out.println(productList.get(z).getId() + "comparison added");
                                }
                                ;
//                            }
//                        }
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
        // While loop to make this bit run endlessly with sleep() every 10 sec
        while (true) {
            try {
                System.out.println("Muziker scraping");
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
        String removeExcessWord[] = {"-",".","Wireless","Triple","Mystic","Bluetooth", "Earphones", "Headphones", "Earbuds", "On Ear","Over Ear", "In Ear", "True", "Noise","Cancelling","More","ANC","Headphone","Collection"};
        String result = prodName;

        if(result.toLowerCase().contains("Apple Airpods MV".toLowerCase())){
            result = result.replace("MV7N2ZM/A 2019", "").trim().replaceAll(" +", " ") + " White 2019";
        }

        if(result.contains("SM-R180")){
            result = result.replace("SM-R180", "").trim().replaceAll(" +", " ");
        }
        for (int y = 0; y < removeExcessWord.length; ++y) {
            //System.out.println(removeExcessWord[y] +" " + result);
            if (result.contains(removeExcessWord[y])) {
                result = result.replace(removeExcessWord[y], "").trim().replaceAll(" +", " ");
            }
        }
        return result;
    }


}
