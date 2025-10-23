package AllFunctions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public class AllFunctions {
    
    static WebDriver driver;
    static WebDriverWait wait;
    
    public static void main(String[] args) {
        
        try {
            // Initialize WebDriver - Using WebDriver Manager approach (recommended)
            // Option 1: Firefox (uncomment if using Firefox)
            // System.setProperty("webdriver.firefox.driver", "path/to/geckodriver.exe");
            // driver = new FirefoxDriver();
            
            // Option 2: Chrome (recommended - comment out if using Firefox)
            // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver.exe");
            // driver = new ChromeDriver();
            
            // Option 3: Use WebDriverManager (best practice - add dependency in pom.xml)
            // WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            
           // driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // 1. TEXTBOX Operations
            handleTextBox();
            Thread.sleep(1000);
            
            // 2. DROPDOWN Operations
            handleDropdown();
            Thread.sleep(1000);
            
            // 3. CHECKBOX Operations
            handleCheckBox();
            Thread.sleep(1000);
            
            // 4. RADIO BUTTON Operations
            handleRadioButton();
            Thread.sleep(1000);
            
            // 5. BUTTON Operations
            handleButton();
            Thread.sleep(1000);
            
            // 6. ALERT Operations
            handleAlerts();
            Thread.sleep(1000);
            
            // 7. IFRAME Operations
            handleIFrames();
            Thread.sleep(1000);
            
            // 8. WINDOW Operations
            handleWindows();
            Thread.sleep(1000);
            
            // 9. FILE UPLOAD Operations
            handleFileUpload();
            Thread.sleep(1000);
            
            System.out.println("\n===== ALL OPERATIONS COMPLETED SUCCESSFULLY! =====");
            
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close browser after 3 seconds
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (driver != null) {
                driver.quit();
                System.out.println("Browser closed successfully");
            }
        }
    }
    
    // 1. TEXTBOX - Input, Clear, Get Text
    public static void handleTextBox() {
        System.out.println("\n=== TEXTBOX Operations ===");
        
        try {
            driver.get("https://www.selenium.dev/selenium/web/web-form.html");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("my-text")));
            
            // Find textbox by name
            WebElement textInput = driver.findElement(By.name("my-text"));
            
            // Enter text
            textInput.clear();
            textInput.sendKeys("Hello Selenium");
            System.out.println("✓ Text entered: Hello Selenium");
            
            // Get entered value
            String value = textInput.getAttribute("value");
            System.out.println("✓ Retrieved value: " + value);
            
            // Clear textbox
            textInput.clear();
            System.out.println("✓ Textbox cleared");
            Thread.sleep(500);
            
            // Enter new text
            textInput.sendKeys("New Text 2025");
            System.out.println("✓ New text entered: New Text 2025");
            
            // Password field
            WebElement password = driver.findElement(By.name("my-password"));
            password.clear();
            password.sendKeys("SecretPassword123");
            System.out.println("✓ Password entered");
            
            // Textarea
            WebElement textarea = driver.findElement(By.name("my-textarea"));
            textarea.clear();
            textarea.sendKeys("This is a multi-line\ntext area content");
            System.out.println("✓ Textarea filled");
            
        } catch (Exception e) {
            System.err.println("✗ TextBox error: " + e.getMessage());
        }
    }
    
    // 2. DROPDOWN - Select by Index, Value, Visible Text
    public static void handleDropdown() {
        System.out.println("\n=== DROPDOWN Operations ===");
        
        try {
            driver.get("https://www.selenium.dev/selenium/web/web-form.html");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("my-select")));
            
            // Find dropdown element
            WebElement dropdownElement = driver.findElement(By.name("my-select"));
            Select dropdown = new Select(dropdownElement);
            
            // Get all options
            List<WebElement> allOptions = dropdown.getOptions();
            System.out.println("✓ Total options: " + allOptions.size());
            
            // Print all options
            System.out.println("  Available options:");
            for (WebElement option : allOptions) {
                System.out.println("  - " + option.getText());
            }
            
            // Select by visible text
            dropdown.selectByVisibleText("Two");
            Thread.sleep(300);
            System.out.println("✓ Selected by visible text: Two");
            
            // Select by value
            dropdown.selectByValue("1");
            Thread.sleep(300);
            System.out.println("✓ Selected by value: 1 (One)");
            
            // Select by index
            dropdown.selectByIndex(3);
            Thread.sleep(300);
            System.out.println("✓ Selected by index: 3 (Three)");
            
            // Get selected option
            WebElement selectedOption = dropdown.getFirstSelectedOption();
            System.out.println("✓ Currently selected: " + selectedOption.getText());
            
            // Check if dropdown is multi-select
            if (dropdown.isMultiple()) {
                System.out.println("✓ This is a multi-select dropdown");
            } else {
                System.out.println("✓ This is a single-select dropdown");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Dropdown error: " + e.getMessage());
        }
    }
    
    // 3. CHECKBOX - Select, Deselect, Check Status
    public static void handleCheckBox() {
        System.out.println("\n=== CHECKBOX Operations ===");
        
        try {
            driver.get("https://www.selenium.dev/selenium/web/web-form.html");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("my-check-1")));
            
            // Find checkboxes
            WebElement checkbox1 = driver.findElement(By.id("my-check-1"));
            WebElement checkbox2 = driver.findElement(By.id("my-check-2"));
            
            // Check if checkboxes are displayed
            System.out.println("✓ Checkbox 1 displayed: " + checkbox1.isDisplayed());
            System.out.println("✓ Checkbox 2 displayed: " + checkbox2.isDisplayed());
            
            // Check if checkboxes are enabled
            System.out.println("✓ Checkbox 1 enabled: " + checkbox1.isEnabled());
            
            // Check current state and select
            if (!checkbox1.isSelected()) {
                checkbox1.click();
                Thread.sleep(300);
                System.out.println("✓ Checkbox 1 selected");
            }
            
            // Verify selection
            System.out.println("✓ Checkbox 1 is selected: " + checkbox1.isSelected());
            
            // Select second checkbox
            if (!checkbox2.isSelected()) {
                checkbox2.click();
                Thread.sleep(300);
                System.out.println("✓ Checkbox 2 selected");
            }
            
            // Uncheck first checkbox
            if (checkbox1.isSelected()) {
                checkbox1.click();
                Thread.sleep(300);
                System.out.println("✓ Checkbox 1 unchecked");
            }
            
            System.out.println("✓ Final state - Checkbox 1: " + checkbox1.isSelected() + 
                             ", Checkbox 2: " + checkbox2.isSelected());
                             
        } catch (Exception e) {
            System.err.println("✗ Checkbox error: " + e.getMessage());
        }
    }
    
    // 4. RADIO BUTTON - Select, Verify Selection
    public static void handleRadioButton() {
        System.out.println("\n=== RADIO BUTTON Operations ===");
        
        try {
            driver.get("https://www.selenium.dev/selenium/web/web-form.html");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("my-radio-1")));
            
            // Find radio buttons
            WebElement radio1 = driver.findElement(By.id("my-radio-1"));
            WebElement radio2 = driver.findElement(By.id("my-radio-2"));
            
            // Check if radio buttons are displayed
            System.out.println("✓ Radio 1 displayed: " + radio1.isDisplayed());
            System.out.println("✓ Radio 2 displayed: " + radio2.isDisplayed());
            
            // Check if enabled
            System.out.println("✓ Radio 1 enabled: " + radio1.isEnabled());
            
            // Select first radio button
            if (!radio1.isSelected()) {
                radio1.click();
                Thread.sleep(300);
                System.out.println("✓ Radio button 1 selected");
            }
            
            // Verify selection
            System.out.println("✓ Radio 1 is selected: " + radio1.isSelected());
            System.out.println("✓ Radio 2 is selected: " + radio2.isSelected());
            
            // Select second radio button (first will be automatically deselected)
            radio2.click();
            Thread.sleep(300);
            System.out.println("✓ Radio button 2 selected");
            
            // Verify selection changed
            System.out.println("✓ Radio 1 is selected: " + radio1.isSelected());
            System.out.println("✓ Radio 2 is selected: " + radio2.isSelected());
            
        } catch (Exception e) {
            System.err.println("✗ Radio button error: " + e.getMessage());
        }
    }
    
    // 5. BUTTON - Click
    public static void handleButton() {
        System.out.println("\n=== BUTTON Operations ===");
        
        try {
            driver.get("https://www.selenium.dev/selenium/web/web-form.html");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit']")));
            
            // Find button
            WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            
            // Get button text
            String buttonText = submitButton.getText();
            System.out.println("✓ Button text: " + buttonText);
            
            // Check if button is enabled
            System.out.println("✓ Button enabled: " + submitButton.isEnabled());
            
            // Check if button is displayed
            System.out.println("✓ Button displayed: " + submitButton.isDisplayed());
            
            // Click button
            submitButton.click();
            System.out.println("✓ Button clicked - Form submitted");
            
            // Wait for page to load
            wait.until(ExpectedConditions.titleContains("Submitted"));
            System.out.println("✓ Redirected to: " + driver.getTitle());
            
        } catch (Exception e) {
            System.err.println("✗ Button error: " + e.getMessage());
        }
    }
    
    // 6. ALERT Operations
    public static void handleAlerts() {
        System.out.println("\n=== ALERT Operations ===");
        
        try {
            // Navigate to alerts page
            driver.get("https://www.selenium.dev/selenium/web/alerts.html");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("alert")));
            
            // Simple Alert
            driver.findElement(By.id("alert")).click();
            wait.until(ExpectedConditions.alertIsPresent());
            org.openqa.selenium.Alert simpleAlert = driver.switchTo().alert();
            String alertText = simpleAlert.getText();
            System.out.println("✓ Simple Alert text: " + alertText);
            simpleAlert.accept();
            System.out.println("✓ Simple alert accepted");
            
            Thread.sleep(1000);
            
            // Confirm Alert (OK/Cancel)
            driver.findElement(By.id("confirm")).click();
            wait.until(ExpectedConditions.alertIsPresent());
            org.openqa.selenium.Alert confirmAlert = driver.switchTo().alert();
            System.out.println("✓ Confirm Alert text: " + confirmAlert.getText());
            confirmAlert.accept(); // Click OK
            System.out.println("✓ Confirm alert accepted (OK)");
            
            Thread.sleep(1000);
            
            // Confirm Alert - Dismiss
            driver.findElement(By.id("confirm")).click();
            wait.until(ExpectedConditions.alertIsPresent());
            confirmAlert = driver.switchTo().alert();
            confirmAlert.dismiss(); // Click Cancel
            System.out.println("✓ Confirm alert dismissed (Cancel)");
            
            Thread.sleep(1000);
            
            // Prompt Alert
            driver.findElement(By.id("prompt")).click();
            wait.until(ExpectedConditions.alertIsPresent());
            org.openqa.selenium.Alert promptAlert = driver.switchTo().alert();
            System.out.println("✓ Prompt Alert text: " + promptAlert.getText());
            promptAlert.sendKeys("Selenium Test Input");
            promptAlert.accept();
            System.out.println("✓ Prompt alert with text accepted");
            
        } catch (Exception e) {
            System.err.println("✗ Alert handling error: " + e.getMessage());
        }
    }
    
    // 7. IFRAME Operations
    public static void handleIFrames() {
        System.out.println("\n=== IFRAME Operations ===");
        
        try {
            driver.get("https://www.selenium.dev/selenium/web/iframes.html");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("iframe")));
            
            // Get number of iframes
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            System.out.println("✓ Total iframes on page: " + iframes.size());
            
            if (iframes.size() > 0) {
                // Switch to iframe by index
                driver.switchTo().frame(0);
                System.out.println("✓ Switched to iframe by index (0)");
                
                // Find element inside iframe
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                WebElement iframeContent = driver.findElement(By.tagName("body"));
                String content = iframeContent.getText();
                System.out.println("✓ Iframe content: " + (content.isEmpty() ? "[Empty or visual content]" : content));
                
                // Switch back to main content
                driver.switchTo().defaultContent();
                System.out.println("✓ Switched back to main content");
                
                // Switch to iframe by WebElement
                WebElement iframeElement = driver.findElement(By.tagName("iframe"));
                driver.switchTo().frame(iframeElement);
                System.out.println("✓ Switched to iframe by WebElement");
                
                // Switch back
                driver.switchTo().defaultContent();
                System.out.println("✓ Switched back to main content");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Iframe handling error: " + e.getMessage());
        }
    }
    
    // 8. WINDOW/TAB Operations
    public static void handleWindows() {
        System.out.println("\n=== WINDOW/TAB Operations ===");
        
        try {
            driver.get("https://www.selenium.dev/selenium/web/window_switching_tests/page_with_link.html");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Open new window")));
            
            // Get main window handle
            String mainWindow = driver.getWindowHandle();
            System.out.println("✓ Main window handle: " + mainWindow);
            System.out.println("✓ Main window title: " + driver.getTitle());
            
            // Click link that opens new window
            driver.findElement(By.linkText("Open new window")).click();
            System.out.println("✓ Clicked link to open new window");
            
            // Wait for new window
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            
            // Get all window handles
            Set<String> allWindows = driver.getWindowHandles();
            System.out.println("✓ Total windows/tabs: " + allWindows.size());
            
            // Switch to new window
            for (String window : allWindows) {
                if (!window.equals(mainWindow)) {
                    driver.switchTo().window(window);
                    System.out.println("✓ Switched to new window");
                    Thread.sleep(500);
                    System.out.println("✓ New window title: " + driver.getTitle());
                    
                    // Close new window
                    driver.close();
                    System.out.println("✓ New window closed");
                }
            }
            
            // Switch back to main window
            driver.switchTo().window(mainWindow);
            System.out.println("✓ Switched back to main window");
            System.out.println("✓ Current title: " + driver.getTitle());
            
        } catch (Exception e) {
            System.err.println("✗ Window handling error: " + e.getMessage());
            // Make sure we're back on main window
            try {
                Set<String> handles = driver.getWindowHandles();
                if (!handles.isEmpty()) {
                    driver.switchTo().window(handles.iterator().next());
                }
            } catch (Exception ex) {
                // Ignore
            }
        }
    }
    
    // 9. FILE UPLOAD Operations
    public static void handleFileUpload() {
        System.out.println("\n=== FILE UPLOAD Operations ===");
        
        try {
            driver.get("https://www.selenium.dev/selenium/web/upload.html");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("upload")));
            
            // Find file upload element
            WebElement uploadElement = driver.findElement(By.id("upload"));
            
            System.out.println("✓ File upload element found");
            System.out.println("✓ Element type: " + uploadElement.getAttribute("type"));
            System.out.println("✓ Element is displayed: " + uploadElement.isDisplayed());
            System.out.println("✓ Element is enabled: " + uploadElement.isEnabled());
            
            // Note: For actual file upload, provide real file path
            // Example: uploadElement.sendKeys("C:\\Users\\YourName\\Documents\\test.txt");
            
            System.out.println("\n  ℹ To upload a file, use: uploadElement.sendKeys(\"full/path/to/file\")");
            System.out.println("  Note: Provide absolute file path");
            System.out.println("  Windows example: C:\\Users\\test\\document.pdf");
            System.out.println("  Linux/Mac example: /home/user/documents/test.pdf");
            
        } catch (Exception e) {
            System.err.println("✗ File upload error: " + e.getMessage());
        }
    }
}