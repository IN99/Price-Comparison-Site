package eu.idreesn.javaCode;

import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class ScraperManager {
    ArrayList<WebScraper> scraperList = new ArrayList<WebScraper>();

    public void initializeScraperManager() {
        for (WebScraper scraper : scraperList) {
            scraper.start();
        }
    }

    public ArrayList<WebScraper> getScraperList() {
        return scraperList;
    }

    public void setScraperList(ArrayList<WebScraper> scraperList) {
        this.scraperList = scraperList;
    }


}



