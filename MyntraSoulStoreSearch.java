package Myntra;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
import java.util.List;

public class MyntraSoulStoreSearch {
    public static void main(String[] args) {
        WebDriver driver = null;
        
        System.setProperty("webdriver.firefox.driver", "C:\\selenium webdrivers\\geckodriver.exe");
        WebDriver driver1 = new FirefoxDriver();
        try {
            
            
            
            driver1.manage().window().maximize();
            
            WebDriverWait wait = new WebDriverWait(driver1, Duration.ofSeconds(20));
            JavascriptExecutor js = (JavascriptExecutor) driver1;
            
            System.out.println("Navigating to Myntra...");
            driver1.get("https://www.myntra.com");
            
            // Wait for page to fully load
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            Thread.sleep(3000);
            
            // Handle potential popups/overlays with more robust approach
            try {
                List<WebElement> popups = driver1.findElements(By.xpath(
                    "//button[contains(@class, 'myntraweb-sprite') or " +
                    "contains(text(), 'Close') or " +
                    "contains(text(), '×') or " +  
                    "@aria-label='Close' or " +
                    "contains(@class, 'close')]"
                ));
                
                for (WebElement popup : popups) {
                    try {
                        if (popup.isDisplayed() && popup.isEnabled()) {
                            popup.click();
                            Thread.sleep(1000);
                            break;
                        }
                    } catch (Exception ignored) {}
                }
            } catch (Exception e) {
                System.out.println("No popup found or couldn't close popup");
            }
            
            // Find search box with updated selectors
            WebElement searchBox = null;
            String[] searchSelectors = {
                "input[placeholder*='Search']",
                "input[placeholder*='search']",
                ".desktop-searchBar input",
                "input[data-group='search']",
                "#desktop-header-cnt input",
                ".desktop-query",
                "input.desktop-searchBar"
            };
            
            for (String selector : searchSelectors) {
                try {
                    searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
                    System.out.println("Found search box with selector: " + selector);
                    break;
                } catch (Exception e) {
                    continue;
                }
            }
            
            if (searchBox == null) {
                // Try XPath as fallback
                try {
                    searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[contains(@placeholder, 'Search') or contains(@placeholder, 'search')]")));
                } catch (Exception e) {
                    throw new RuntimeException("Could not find search box with any selector");
                }
            }
            
            System.out.println("Searching for Soul Store T-shirt...");
            
            // Click on search box first to ensure it's focused
            searchBox.click();
            Thread.sleep(500);
            
            // Clear any existing text and search
            searchBox.clear();
            Thread.sleep(500);
            searchBox.sendKeys("Soul Store T-shirt");
            Thread.sleep(1000);
            
            // Try multiple ways to submit search
            boolean searchSubmitted = false;
            
            // Method 1: Press Enter
            try {
                searchBox.sendKeys(Keys.ENTER);
                searchSubmitted = true;
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Enter key didn't work, trying search button...");
            }
            
            // Method 2: Click search button if Enter didn't work
            if (!searchSubmitted) {
                try {
                    WebElement searchButton = driver1.findElement(By.cssSelector(
                        ".desktop-submit, button[type='submit'], .desktop-iconSearch, " +
                        "button[aria-label='search'], .search-button"));
                    searchButton.click();
                    searchSubmitted = true;
                } catch (Exception e) {
                    // Method 3: JavaScript click
                    try {
                        js.executeScript("arguments[0].form.submit();", searchBox);
                        searchSubmitted = true;
                    } catch (Exception ex) {
                        System.out.println("All search submission methods failed");
                    }
                }
            }
            
            if (!searchSubmitted) {
                throw new RuntimeException("Could not submit search");
            }
            
            // Wait for search results page to load
            System.out.println("Waiting for search results...");
            Thread.sleep(4000);
            
            // Check if we're on search results page
            String currentUrl = driver1.getCurrentUrl();
            System.out.println("Current URL: " + currentUrl);
            
            if (!currentUrl.contains("search") && !currentUrl.contains("soul") && !currentUrl.contains("t-shirt")) {
                System.out.println("Search might not have worked. Trying direct URL...");
                driver1.get("https://www.myntra.com/soul-store");
                Thread.sleep(3000);
            }
            
            // Look for products with multiple selectors
            String[] resultSelectors = {
                "ul.results-base li",
                ".product-base",
                ".results-base .product-productMetaInfo",
                "[data-testid='product-base']",
                ".product-tuple-container",
                "li.product-tuple",
                ".search-searchProductsContainer li"
            };
            
            List<WebElement> products = null;
            for (String selector : resultSelectors) {
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selector)));
                    products = driver1.findElements(By.cssSelector(selector));
                    if (!products.isEmpty()) {
                        System.out.println("Found products with selector: " + selector);
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            if (products != null && !products.isEmpty()) {
                System.out.println("Found " + products.size() + " products");
                
                // Look for Soul Store products
                boolean foundSoulStore = false;
                for (int i = 0; i < Math.min(products.size(), 20); i++) {
                    try {
                        WebElement product = products.get(i);
                        String productText = product.getText().toLowerCase();
                        
                        if (productText.contains("soul store") || productText.contains("soulstore")) {
                            System.out.println("Found Soul Store product #" + (i+1) + ": " + 
                                productText.substring(0, Math.min(150, productText.length())));
                            
                            // Scroll to element and click
                            js.executeScript("arguments[0].scrollIntoView(true);", product);
                            Thread.sleep(1000);
                            
                            try {
                                product.click();
                            } catch (Exception e) {
                                // Try JavaScript click if regular click fails
                                js.executeScript("arguments[0].click();", product);
                            }
                            
                            foundSoulStore = true;
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error processing product " + (i+1) + ": " + e.getMessage());
                        continue;
                    }
                }
                
                if (!foundSoulStore) {
                    System.out.println("No Soul Store products found. Available products:");
                    for (int i = 0; i < Math.min(5, products.size()); i++) {
                        try {
                            String productText = products.get(i).getText();
                            System.out.println((i+1) + ". " + productText.substring(0, Math.min(100, productText.length())));
                        } catch (Exception e) {
                            System.out.println((i+1) + ". Could not read product text");
                        }
                    }
                    
                    // Click first product as fallback
                    try {
                        System.out.println("Clicking first available product...");
                        WebElement firstProduct = products.get(0);
                        js.executeScript("arguments[0].scrollIntoView(true);", firstProduct);
                        Thread.sleep(1000);
                        firstProduct.click();
                    } catch (Exception e) {
                        js.executeScript("arguments[0].click();", products.get(0));
                    }
                }
                
                // Wait to observe the result
                Thread.sleep(5000);
                System.out.println("Final URL: " + driver1.getCurrentUrl());
                
            } else {
                System.out.println("No products found in search results.");
                System.out.println("Page title: " + driver1.getTitle());
                
                // Check if there's a "no results" message
                try {
                    WebElement noResults = driver1.findElement(By.xpath("//*[contains(text(), 'Sorry') or contains(text(), 'no results') or contains(text(), 'not found')]"));
                    System.out.println("No results message: " + noResults.getText());
                } catch (Exception e) {
                    System.out.println("No specific 'no results' message found");
                }
            }
            
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver1 != null) {
                System.out.println("Closing browser...");
                try {
                    Thread.sleep(2000); // Brief pause to see results
                    driver1.quit();
                } catch (Exception e) {
                    System.err.println("Error closing driver: " + e.getMessage());
                }
            }
        }
    }
}