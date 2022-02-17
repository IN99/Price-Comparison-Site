package eu.idreesn.javaCode;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import info.debatty.java.stringsimilarity.*;
//import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.List;

/**
 * Selenium Chrome Driver to access data from Argos website
 * that load data dynamically with JavaScript
 */
public class ArgosScraper extends WebScraper {

    /**
     * Scrapes data from website and
     gets each product out of the list of all scraped products.
     */
    public void scrapeAll() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\ChromiumDriver\\chromedriver.exe");
        //Create instance of web driver - this must be on the path.
        WebDriver driver = new ChromeDriver(options);

        if (getHibernateDAO().findRetailer("Argos").size() == 0) {
            getHibernateDAO().saveRetailer(getRetailer());
        }

        for (int x = 0; x < getPageList().size(); x++) {
            System.out.println(getPageList().get(x));
            driver.get("https://www.argos.co.uk/browse/technology/headphones-and-earphones/wireless-headphones/c:815766/brands:apple,beats-by-dre,bose,google,huawei,jabra,jbl,jlab,lg,samsung,sennheiser,skullcandy,sony/" + getPageList().get(x));

            //Navigate Chrome to page.
            //Wait for page to load
            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            for(int i=0;i<330;i++) {
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,80)", "");
            }
            //Output details for individual products
            List<WebElement> phoneList = driver.findElements(By.cssSelector(".ProductCardstyles__ContentBlock-sc-1fgptbz-5"));
            List<WebElement> nameList = driver.findElements(By.cssSelector(".ProductCardstyles__Title-sc-1fgptbz-12 "));
            List<WebElement> priceList = driver.findElements(By.cssSelector(".ProductCardstyles__PriceText-sc-1fgptbz-14"));
            List<WebElement> productUrlList = driver.findElements(By.cssSelector("div.ProductCardstyles__TextContainer-sc-1fgptbz-6.faVtmd > a"));
            List<WebElement> prodImageUrl = driver.findElements(By.cssSelector("div.ProductCardstyles__ImageWrapper-sc-1fgptbz-3.fSXevH > div > picture > img"));

            for (int i = 0; i < phoneList.size(); i++) {
                String fullPrice = "0";
                String prodName = nameList.get(i).getText();
                String prodDesc = null;
                String prodImgUrl = prodImageUrl.get(i).getAttribute("src");
                String prodUrl = null;

                prodName = formatName(prodName);


                if (productUrlList.get(i).getAttribute("href").contains("//cat.hlserve.com/")) {
                    continue;
                } else {
                    prodUrl = productUrlList.get(i).getAttribute("href");
                }

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
                    prodDesc = price + " " + prodName;
                } catch (IndexOutOfBoundsException e) {
                    prodDesc = "N/A";
                }


                List<Product> productResults = getHibernateDAO().findProduct(prodName.toLowerCase().replaceAll("\\s+", ""));
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
                        String scrapedProduct = prodName.toLowerCase().replaceAll("\\s+", "");

                        String productStoredInDatabase = productList.get(z).getName().replaceAll("\\s+", "");

                        NormalizedLevenshtein normalizedLevenshtein = new NormalizedLevenshtein();

                        if (productStoredInDatabase.equalsIgnoreCase(scrapedProduct)) {
                            Comparison com = new Comparison();
                            com.setProduct(productList.get(z));
                            com.setUrl(productUrlList.get(i).getAttribute("href"));
                            com.setPrice(price);
                            com.setRetailer(getRetailer());
                            getHibernateDAO().saveOrUpdateComparison(com);
                            System.out.println(productList.get(z).getId() + "comparison added");
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
        // While loop to make this bit run endlessly with sleep() every 10 sec
        while (true) {
            try {
                System.out.println("Argos scraping");
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
        String removeExcessWord[] = {"-","Wireless", "Bluetooth", "Earphones", "Headphones", "Earbuds", "On", "In","Over","Ear", "True", "Noise","Cancelling","ANC"};
        String result = prodName;

        if (result.contains("AirPods with Charging Case (2nd Generation)")) {
            result = result.replace("with Charging Case (2nd Generation)", "").trim().replaceAll(" +", " ") + " White 2019";
        }

        if (result.toLowerCase().contains("Apple Airpods Pro".toLowerCase())) {
            result = result.replace("with Wireless Charging Case", "").trim().replaceAll(" +", " ") + " White";
        }
        if(result.contains("Elite 75T") && result.contains("Titanium")){
            result = result + " Black";
        }
        if(result.contains("Elite 75T") && result.contains("Charging")){
            result = result.replace("with", "").trim().replaceAll(" +", " ") + " Black";
        }
        if(result.toLowerCase().contains("by Dre".toLowerCase())){
            result = result.replace("by Dre", "").replace("By Dre","").trim().replaceAll(" +", " ");
        }
        for (int y = 0; y < removeExcessWord.length; ++y) {
            //System.out.println(removeExcessWord[y] +" " + result);
            if (result.toLowerCase().contains(removeExcessWord[y].toLowerCase())) {
                result = result.replace(removeExcessWord[y], "").trim().replaceAll(" +", " ");
            }
        }
        return result;
    }

}
