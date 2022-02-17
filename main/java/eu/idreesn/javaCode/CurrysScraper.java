package eu.idreesn.javaCode;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;


/**
 * Selenium Chrome Driver to access data from Currys website
 * that load data dynamically with JavaScript
 */
public class CurrysScraper extends WebScraper {

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

        if (getHibernateDAO().findRetailer("Currys PC World").size() == 0) {
            getHibernateDAO().saveRetailer(getRetailer());
        }

        for (int x = 0; x < getPageList().size(); x++) {
            System.out.println(getPageList().get(x));

            driver.get("https://www.currys.co.uk/gbuk/wireless-and-bluetooth-headphones/headphones/headphones/291_3919_31664_670_1939_49_99975_75037_1367_7141_97335_743_32969_43_267_19502_256_ba00010486-bv00308562/" + getPageList().get(x));

            //Navigate Chrome to page.
            //Wait for page to load
            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            //Output details for individual products
            List<WebElement> phoneList = driver.findElements(By.cssSelector(".productWrapper"));
            List<WebElement> brandList = driver.findElements(By.cssSelector("div.productWrapper > header > a > span:nth-child(1)"));
            List<WebElement> nameList = driver.findElements(By.cssSelector("div.productWrapper > header > a > span:nth-child(2)"));
            List<WebElement> priceList = driver.findElements(By.cssSelector("span.ProductPriceBlock__Price-kTVxGg"));
            List<WebElement> productUrlList = driver.findElements(By.xpath("//header[@class='productTitle']/a"));
            List<WebElement> productDesc = driver.findElements(By.cssSelector(".productDescription"));

            for (int i = 0; i < phoneList.size(); i++) {
                String fullPrice = "0";
                String prodName =brandList.get(i).getText() + " " + nameList.get(i).getText();
                String prodDesc = null;
                String prodImgUrl = null;
                String prodUrl = productUrlList.get(i).getAttribute("href");

                if (phoneList.get(i).findElements(By.cssSelector("div.product-images > picture > source")).size() > 0) {
                    prodImgUrl = phoneList.get(i).findElement(By.cssSelector("div.product-images > picture > source")).getAttribute("srcset");
                } else {
                    prodImgUrl = phoneList.get(i).findElement(By.cssSelector("div.product-images > img")).getAttribute("src");
                }

                prodName = formatName(prodName);

                try {
                    fullPrice = priceList.get(i).getText().replaceAll("[£|p|/unit|']", "");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Could not get product price");
                    fullPrice = "0";
                }

                try {
                    prodDesc = productDesc.get(i).getText();
                } catch (IndexOutOfBoundsException e) {
                    prodDesc = "N/A";
                }

                double price = 0;
                if (fullPrice.length() > 0) {
                    price = priceToDouble(fullPrice);
                    //System.out.println(price);
                }


//                System.out.println(productResults);
//                NormalizedLevenshtein normalizedLevenshtein = new NormalizedLevenshtein();
                List<Product> productResults = getHibernateDAO().findProduct(prodName.toLowerCase().replaceAll("\\s+", ""));
                List<Product> productList = getHibernateDAO().getProductList();
//                Lisst<Comparison> compResults = getHibernateDAO().findComparison(prodUrl);

                List<Comparison> comparisonList = getHibernateDAO().findComparison(prodUrl);

                if (productResults.size() == 0) {//Product does not exist
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
                } else if (comparisonList.size() == 0) {//Product exists, no comparison
                    for (int z = 0; z < productList.size(); z++) {
                        String scrapedProduct = prodName.replaceAll("\\s+", "");

                        String productInDb = productList.get(z).getName().replaceAll("\\s+", "");

                        if (productInDb.equalsIgnoreCase(scrapedProduct)) {
                            Comparison com = new Comparison();
                            com.setProduct(productList.get(z));
                            com.setUrl(prodUrl);
                            com.setPrice(price);
                            com.setRetailer(getRetailer());
                            getHibernateDAO().saveOrUpdateComparison(com);
                        }

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
                System.out.println("Currys scraping");
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
        String removeExcessWord[] = {"-","Wireless", "Triple", "Mystic", "Titanium", "Headphones", "Bluetooth", "Earphones", "Earbuds", "True", "Noise","Cancelling","Sports", "BTNC"};
        String result = prodName;
        System.out.println(result);
        if(result.contains("AirPods with Charging Case")){
            result = result.replace("with Charging Case (2nd generation)", "").trim().replaceAll(" +", " ") + " 2019";
        }
        if(result.contains("Elite 75T") && result.contains("Charging")){
            result = result.replace("Enabled", "").trim().replaceAll(" +", " ");
        }
        if(result.contains("JLAB AUDIO") && result.contains("White & Grey")){
            result = result.replace("& Grey", "").trim().replaceAll(" +", " ");
        }
        if(result.contains("JLAB AUDIO")){
            result = result.replace("AUDIO", "").trim().replaceAll(" +", " ");
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
