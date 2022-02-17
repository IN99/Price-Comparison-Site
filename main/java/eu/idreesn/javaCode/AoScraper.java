//******Did not have time to check for errors and implement this*******

//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//
//import java.util.List;
//
///**
// * Demonstrates how you can use Selenium Chrome Driver to access data from websites
// * that load data dynamically with JavaScript
// */
//public class AoScraper extends WebScraper {
////    Product product= new Product();
//
//
//    /**
//     * Demonstrates use of ChromeDriver with Selenium
//     */
//    public void scrapeAll() throws Exception {
////        //We need an options class to run headless - not needed if we want default options
////        List<Product> productList = getHibernateDAO().getProductList();
//
//        ChromeOptions options = new ChromeOptions();
//        options.setHeadless(true);
//
//        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\ChromiumDriver\\chromedriver.exe");
//        //Create instance of web driver - this must be on the path.
//        WebDriver driver = new ChromeDriver(options);
//        JavascriptExecutor jse = (JavascriptExecutor) driver;
//        if (getHibernateDAO().findRetailer("a").size() == 0) {
//            getHibernateDAO().saveRetailer(getRetailer());
//        }
//
//        for (int x = 0; x < getPageList().size(); x++) {
//            driver.manage().window().maximize();
//            System.out.println(getPageList().get(x));
//            driver.get("https://ao.com/l/headphones-4329/1-20/107-164-178/" + getPageList().get(x));
//            if (driver.findElements(By.cssSelector("button[data-testid='cookieBanner-accept-btn']")).size() != 0) {
//                driver.findElement(By.cssSelector("button[data-testid='cookieBanner-accept-btn']")).click();
//            }
//
//            //Navigate Chrome to page.
//            //Wait for page to load
//            try {
//                Thread.sleep(5000);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-200)", "");
//
//            for(int i=0;i<630;i++) {
//                System.out.println(i);
//                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,100)", "");
//            }
//
//            List<WebElement> phoneList = driver.findElements(By.cssSelector(".product-card__inner-flex-container.o-flex-container"));
//            List<WebElement> descList = driver.findElements(By.cssSelector("span.ml-1"));
//            List<WebElement> nameList = driver.findElements(By.cssSelector("h2[itemprop='name']"));
//            List<WebElement> priceList = driver.findElements(By.cssSelector("div.text-title-lg"));
//            List<WebElement> productUrlList = driver.findElements(By.cssSelector("div.h-full > a.u-d--block"));
//            for (WebElement phones : productUrlList) {
//                System.out.println(phones.getAttribute("href"));
//
//            }
//
//            System.out.println(phoneList.size());
////            for (int i = 0; i < phoneList.size(); i++) {
//////                System.out.println(productUrlList.get(i));
////                String fullPrice = "0";
////                String prodName = nameList.get(i).getText();
////                String prodDesc = null;
////                String prodUrl = productUrlList.get(i).getAttribute("href");
////                String imageUrl = null;
////                String removeExcessWord[] = {"Wireless","Defiant","Bluetooth", "Earphones", "Headphones", "Earbuds", "On-Ear", "In-Ear", "True", "Water Resistant", "-"};
////                String result = prodName;
////                for (int y = 0; y < removeExcessWord.length; ++y) {
////                    //System.out.println(removeExcessWord[y] +" " + result);
////                    if (result.contains(removeExcessWord[y])) {
////                        result = result.replace(removeExcessWord[y], "").trim().replaceAll(" +", " ");
////                        prodName = result;
////                    }
////                }
////
////
////
////                // System.out.println(prodName);
////
////                try {
////                    fullPrice = priceList.get(i).getText().replaceAll("[£|p|/unit|']", "");
////                } catch (IndexOutOfBoundsException e) {
////                    System.out.println("Could not get product price");
////                    fullPrice = "0";
////                }
////                double price = 0;
////                if (fullPrice.length() > 0) {
////                    price = priceToDouble(fullPrice);
////                    //System.out.println(price);
////                }
////
////
////                try {
////                    prodDesc = price + " " + prodName;
////                } catch (IndexOutOfBoundsException e) {
////                    prodDesc = "N/A";
////                }
////
////
////                List<Product> productResults = getHibernateDAO().findProduct(prodName.toLowerCase().replaceAll("\\s+",""));
////                List<Product> productList = getHibernateDAO().getProductList();
//////                Lisst<Comparison> compResults = getHibernateDAO().findComparison(prodUrl);
////
////                List<Comparison> comparisonList = getHibernateDAO().findComparison(prodUrl);
////
////
////                if (productResults.size() == 0) {
////                    Product product = new Product();
////                    product.setName(prodName);
////                    product.setDescription(prodDesc);
////                    Comparison comparison = new Comparison();
////                    comparison.setProduct(product);
////                    comparison.setUrl(prodUrl);
////                    comparison.setPrice(price);
////                    comparison.setRetailer(getRetailer());
////                    getHibernateDAO().saveProduct(product);
////                    getHibernateDAO().saveOrUpdateComparison(comparison);
////                } else if (comparisonList.size() == 0) {
////                    for (int z = 0; z < productList.size(); z++) {
////                        String scrapedProduct = prodName.toLowerCase().replaceAll("\\s+","");
////                        //System.out.println("argos " + prodName);
////
////                        String productStoredInDatabase = productList.get(z).getName().replaceAll("\\s+","");
////                        //System.out.println("Argos in DB " + productStoredInDatabase);
////
////                        NormalizedLevenshtein normalizedLevenshtein = new NormalizedLevenshtein();
////                        // System.out.println(normalizedLevenshtein.similarity(scrapedProduct, productStoredInDatabase));
////
////                        if (productStoredInDatabase.equalsIgnoreCase(scrapedProduct)) {
////                            Comparison com = new Comparison();
////                            com.setProduct(productList.get(z));
////                            com.setUrl(prodUrl);
////                            com.setPrice(price);
////                            com.setRetailer(getRetailer());
////                            getHibernateDAO().saveOrUpdateComparison(com);
////                            System.out.println(productList.get(z).getId() + "comparison added");
////                        }
////                        ;
////                    }
////                } else {
////                    System.out.println(prodName + " " + prodUrl + " Product already added to database");
////                }
////            }
//        }
//        setProductsAdded(true);
//        driver.quit();
//    }
//
//
//    public void run() {
//        // While loop to make this bit run endlessly with sleep() every 10 sec
//        while (true) {
//            try {
//                System.out.println("AO scraping");
//                scrapeAll();
//                sleep(10000);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//
//    }
//
//    public double priceToDouble(String priceString) {
//        double price = 0;
//        String cleanedPriceString = priceString.replaceAll("[£|p|/unit|']", "");
//        price = Double.parseDouble(cleanedPriceString);
//        return price;
//    }
//
//
//}
