package Flipkart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.util.ArrayList;

public class Flipkart {
    public static void main(String[] args) throws InterruptedException {
        // Set path to chromedriver
    	System.setProperty("webdriver.firefox.driver", "C:\\selenium webdrivers\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        driver.get("https://www.flipkart.com/");
        try {
            // 1. Open Flipkart
            
            driver.manage().window().maximize();
            Thread.sleep(2000);

            // 2. Close login popup if present
            try {
                WebElement closeBtn = driver.findElement(By.cssSelector("button.QqFHMw vslbG+ In9uk2 JTo6b7"));
                closeBtn.click();
            } catch (Exception e) {
                // Popup not present
            }

            // 3. Search for S25 Ultra
            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys("S25 Ultra");
            searchBox.submit();
            Thread.sleep(3000);

            // 4. Click on the first product
            WebElement firstProduct = driver.findElement(By.xpath("//*[@id=\"container\"]/div/div[3]/div[1]/div[2]/div[2]/div/div/div/a"));
            firstProduct.click();
            Thread.sleep(3000);

            // 5. Switch to new tab
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));

            // 6. Click Add to Cart
            WebElement addToCartBtn = driver.findElement(By.xpath("//button[contains(text(),'Add to cart')]"));
            addToCartBtn.click();
            Thread.sleep(3000);

            System.out.println("Product added to cart!");

        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
