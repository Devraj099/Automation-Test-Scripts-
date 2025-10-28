package HTTPCode;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class HTTPCode {
    
    public static void main(String[] args) {
        // Set FirefoxDriver path
        System.setProperty("webdriver.firefox.driver", "C:\\selenium webdrivers\\geckodriver.exe");
        
        // Firefox options
        FirefoxOptions options = new FirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);
        
        try {
            // Maximize window
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            // Navigate to the website
            String websiteUrl = "https://www.amazon.in";
            System.out.println("Checking HTTP Status Codes for: " + websiteUrl);
            System.out.println();
            driver.get(websiteUrl);
            
            Thread.sleep(2000);
            
            // Get all links
            List<WebElement> links = driver.findElements(By.tagName("a"));
            System.out.println("Total Links Found: " + links.size());
            System.out.println();
            
            // Check status for first 10 links (for demo)
            int count = 0;
            for (WebElement link : links) {
                if (count >= 10) break; // Check only first 10 links
                
                try {
                    String url = link.getAttribute("href");
                    
                    if (url != null && !url.isEmpty() && 
                        (url.startsWith("http://") || url.startsWith("https://"))) {
                        
                        int statusCode = getHTTPStatusCode(url);
                        
                        System.out.println("URL: " + url);
                        System.out.println("Status Code: " + statusCode);
                        
                        // Basic classification
                        if (statusCode == 200) {
                            System.out.println("Result: OK");
                        } else if (statusCode == 404) {
                            System.out.println("Result: NOT FOUND");
                        } else if (statusCode >= 400) {
                            System.out.println("Result: ERROR");
                        } else if (statusCode >= 200 && statusCode < 400) {
                            System.out.println("Result: SUCCESS");
                        }
                        
                        System.out.println("---");
                        count++;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            System.out.println("\nChecked " + count + " links successfully.");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("Browser closed.");
            }
        }
    }
    
    // Get HTTP status code
    public static int getHTTPStatusCode(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.connect();
            
            int statusCode = connection.getResponseCode();
            connection.disconnect();
            return statusCode;
            
        } catch (Exception e) {
            return 0;
        }
    }
}