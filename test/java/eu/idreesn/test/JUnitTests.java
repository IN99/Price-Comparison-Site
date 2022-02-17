package eu.idreesn.test;


import static org.junit.jupiter.api.Assertions.*;

import eu.idreesn.javaCode.ArgosScraper;
import eu.idreesn.javaCode.CurrysScraper;
import org.junit.jupiter.api.*;

/**
 * Unit test for simple App.
 */
public class JUnitTests {
    /**
     * Testing methods
     */
    @org.junit.jupiter.api.Test
    void formatNameAssertEquals() {
        CurrysScraper currysScraper = new CurrysScraper();
        String name = "Beats Solo 3 Wireless Bluetooth Headphones - Black";
        String result = currysScraper.formatName(name);

        assertEquals("Beats Solo 3 Black", result);
    }

    @org.junit.jupiter.api.Test
    void formatNameAssertNotEquals() {
        CurrysScraper currysScraper = new CurrysScraper();
        String name = "Beats Solo 3 Wireless Bluetooth Headphones - Black";
        String result = currysScraper.formatName(name);

        assertNotEquals("Beats Solo 3 Wireless - Black", result);
    }

    @org.junit.jupiter.api.Test
    void priceToDoubleAssertEqualsTest() {
        ArgosScraper argosScraper = new ArgosScraper();
        String price = "£81.99";
        double result = argosScraper.priceToDouble(price);

        assertEquals(81.99, result);
    }

    @org.junit.jupiter.api.Test
    void priceToDoubleAssertNotEqualsTest() {
        ArgosScraper argosScraper = new ArgosScraper();
        String price = "£81.99";
        double result = argosScraper.priceToDouble(price);

        assertNotEquals("£81.99", result);
    }
}
