package Whatsapp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.ArrayList;



public class Whasapp {
    public static void main(String[] args) {
        // Set path to chromedriver
    	System.setProperty("webdriver.firefox.driver", "C:\\selenium webdrivers\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            // 1. Open WhatsApp Web
            driver.get("https://web.whatsapp.com/");
            driver.manage().window().maximize();


            // Wait for QR code scan (wait for search box to appear)
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@contenteditable='true'][@data-tab='3']")));

            // Search for contact/group
            String contactName = "Contact Name"; // Change this
            WebElement searchBox = driver.findElement(
                By.xpath("//div[@contenteditable='true'][@data-tab='3']"));
            searchBox.click();
            searchBox.sendKeys(contactName);

            // Wait for and click on the contact/group
            By contactXpath = By.xpath("//span[@title='" + contactName + "']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(contactXpath));
            WebElement contact = driver.findElement(contactXpath);

            // --- THIS IS THE IMPORTANT PART ---
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", contact);
            contact.click();

            // Type and send the message
            String message = "Hello from Selenium!";
            WebElement messageBox = driver.findElement(
                By.xpath("//div[@contenteditable='true'][@data-tab='10']"));
            messageBox.sendKeys(message);

            WebElement sendButton = driver.findElement(
                By.xpath("//span[@data-icon='send']"));
            sendButton.click();

            System.out.println("Message sent!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // driver.quit();
        }
    }
}
