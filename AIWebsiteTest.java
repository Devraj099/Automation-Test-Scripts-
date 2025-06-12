package AI;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
import java.util.List;

public class AIWebsiteTest {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    public void setUp() {
        // Set up Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        // Initialize driver
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        System.out.println("Browser setup completed successfully");
    }
    
    public void testChatGPTInterface() {
        try {
            System.out.println("Starting ChatGPT interface test...");
            
            // Navigate to ChatGPT
            driver.get("https://chat.openai.com");
            Thread.sleep(3000);
            
            // Check if page loaded properly
            String pageTitle = driver.getTitle();
            System.out.println("Page title: " + pageTitle);
            
            if (pageTitle.toLowerCase().contains("chatgpt") || pageTitle.toLowerCase().contains("openai")) {
                System.out.println("✓ ChatGPT page loaded successfully");
            } else {
                System.out.println("✗ Unexpected page title");
            }
            
            // Look for chat input area
            WebElement chatInput = null;
            try {
                // Try different possible selectors for chat input
                chatInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("textarea[placeholder*='Message'], textarea[data-id='root'], #prompt-textarea")
                ));
                System.out.println("✓ Chat input field found");
            } catch (Exception e) {
                System.out.println("✗ Chat input field not found, trying alternative selectors...");
                
                // Try alternative selectors
                List<WebElement> textareas = driver.findElements(By.tagName("textarea"));
                if (!textareas.isEmpty()) {
                    chatInput = textareas.get(0);
                    System.out.println("✓ Found textarea element");
                }
            }
            
            if (chatInput != null) {
                // Test sending a message
                String testMessage = "Hello, can you help me test this AI interface?";
                chatInput.clear();
                chatInput.sendKeys(testMessage);
                System.out.println("✓ Message typed successfully: " + testMessage);
                
                // Look for send button
                try {
                    WebElement sendButton = driver.findElement(
                        By.cssSelector("button[data-testid='send-button'], button[aria-label*='Send'], button:has(svg)")
                    );
                    sendButton.click();
                    System.out.println("✓ Send button clicked");
                } catch (Exception e) {
                    // Try pressing Enter if send button not found
                    chatInput.sendKeys(org.openqa.selenium.Keys.ENTER);
                    System.out.println("✓ Enter key pressed to send message");
                }
                
                // Wait for response
                Thread.sleep(5000);
                System.out.println("✓ Waiting for AI response...");
                
            } else {
                System.out.println("✗ Could not locate chat input field");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error during ChatGPT test: " + e.getMessage());
        }
    }
    
    public void testPerplexityAI() {
        try {
            System.out.println("\nStarting Perplexity AI test...");
            
            driver.get("https://www.perplexity.ai");
            Thread.sleep(3000);
            
            String pageTitle = driver.getTitle();
            System.out.println("Page title: " + pageTitle);
            
            if (pageTitle.toLowerCase().contains("perplexity")) {
                System.out.println("✓ Perplexity AI page loaded successfully");
            }
            
            // Look for search/query input
            try {
                WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("textarea[placeholder*='Ask'], input[placeholder*='Ask'], textarea")
                ));
                
                String testQuery = "What is artificial intelligence?";
                searchInput.clear();
                searchInput.sendKeys(testQuery);
                System.out.println("✓ Query typed: " + testQuery);
                
                // Submit query
                searchInput.sendKeys(org.openqa.selenium.Keys.ENTER);
                System.out.println("✓ Query submitted");
                
                Thread.sleep(5000);
                System.out.println("✓ Waiting for AI-powered search results...");
                
            } catch (Exception e) {
                System.out.println("✗ Could not interact with Perplexity search: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error during Perplexity test: " + e.getMessage());
        }
    }
    
    public void testClaudeAI() {
        try {
            System.out.println("\nStarting Claude AI test...");
            
            driver.get("https://claude.ai");
            Thread.sleep(3000);
            
            String pageTitle = driver.getTitle();
            System.out.println("Page title: " + pageTitle);
            
            if (pageTitle.toLowerCase().contains("claude")) {
                System.out.println("✓ Claude AI page loaded successfully");
            }
            
            // Check for key elements that indicate AI interface
            try {
                // Look for chat interface elements
                List<WebElement> chatElements = driver.findElements(
                    By.cssSelector("textarea, input[type='text'], [role='textbox']")
                );
                
                if (!chatElements.isEmpty()) {
                    System.out.println("✓ Chat interface elements detected");
                } else {
                    System.out.println("! No obvious chat elements found - may require login");
                }
                
                // Check for AI-related text content
                String pageSource = driver.getPageSource().toLowerCase();
                if (pageSource.contains("artificial intelligence") || 
                    pageSource.contains("ai assistant") || 
                    pageSource.contains("conversation")) {
                    System.out.println("✓ AI-related content detected on page");
                }
                
            } catch (Exception e) {
                System.out.println("✗ Error analyzing Claude interface: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error during Claude test: " + e.getMessage());
        }
    }
    
    public void testAIResponsiveness() {
        System.out.println("\nTesting AI website responsiveness...");
        
        try {
            // Test page load time
            long startTime = System.currentTimeMillis();
            driver.get("https://chat.openai.com");
            long loadTime = System.currentTimeMillis() - startTime;
            
            System.out.println("Page load time: " + loadTime + "ms");
            
            if (loadTime < 5000) {
                System.out.println("✓ Good page load performance");
            } else {
                System.out.println("! Slow page load detected");
            }
            
            // Test JavaScript execution
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Long jsReady = (Long) js.executeScript("return document.readyState == 'complete' ? 1 : 0");
            
            if (jsReady == 1) {
                System.out.println("✓ JavaScript loaded successfully");
            } else {
                System.out.println("✗ JavaScript not fully loaded");
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error during responsiveness test: " + e.getMessage());
        }
    }
    
    public void generateTestReport() {
        String separator = "==================================================";
        System.out.println("\n" + separator);
        System.out.println("AI WEBSITE TESTING REPORT");
        System.out.println(separator);
        System.out.println("Test completed at: " + java.time.LocalDateTime.now());
        System.out.println("Browser: " + driver.getClass().getSimpleName());
        System.out.println(separator);
    }
    
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed successfully");
        }
    }
    
    public static void main(String[] args) {
        AIWebsiteTest test = new AIWebsiteTest();
        
        try {
            test.setUp();
            test.testChatGPTInterface();
            test.testPerplexityAI();
            test.testClaudeAI();
            test.testAIResponsiveness();
            test.generateTestReport();
            
        } catch (Exception e) {
            System.out.println("Test execution failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            test.tearDown();
        }
    }
}