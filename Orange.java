package Orange;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
import java.util.List;

public class Orange {
    private static WebDriver driver;
    private static WebDriverWait wait;
    
    public static void main(String[] args) {
        // Set up FirefoxDriver
    	System.setProperty("webdriver.firefox.driver", "C:\\selenium webdrivers\\geckodriver.exe");
    	driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            // Navigate to OrangeHRM
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
            driver.manage().window().maximize();
            
            // Login
            login();
            
            // Test different modules
            testPIMModule();
            testLeaveModule();
            testRecruitmentModule();
            testDirectoryModule();
            
            // Logout
            logout();
            
            System.out.println("All tests completed successfully!");
            
        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
    
    private static void login() {
        try {
            System.out.println("Starting login process...");
            
            WebElement username = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
            WebElement password = driver.findElement(By.name("password"));
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
            
            username.clear();
            username.sendKeys("Admin");
            password.clear();
            password.sendKeys("admin123");
            loginButton.click();
            
            // Wait for dashboard to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h6[text()='Dashboard']")));
            System.out.println("Login successful!");
            
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            throw e;
        }
    }
    
    private static void testPIMModule() {
        try {
            System.out.println("Testing PIM module...");
            
            // Navigate to PIM
            WebElement pimMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='PIM']")));
            pimMenu.click();
            
            // Add Employee
            WebElement addEmployee = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add Employee']")));
            addEmployee.click();
            
            // Fill employee details
            WebElement firstName = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("firstName")));
            WebElement lastName = driver.findElement(By.name("lastName"));
            
            firstName.clear();
            firstName.sendKeys("John");
            lastName.clear();
            lastName.sendKeys("Doe");
            
            WebElement saveButton = driver.findElement(By.xpath("//button[@type='submit']"));
            saveButton.click();
            
            // Wait for success message or page load
            Thread.sleep(3000);
            System.out.println("Employee added successfully!");
            
            // Navigate to Employee List
            WebElement employeeList = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Employee List']")));
            employeeList.click();
            
            // Search for employee
            WebElement searchName = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//input[@placeholder='Type for hints...'])[1]")));
            searchName.clear();
            searchName.sendKeys("John");
            
            WebElement searchButton = driver.findElement(By.xpath("//button[@type='submit']"));
            searchButton.click();
            
            Thread.sleep(2000);
            System.out.println("Employee search completed!");
            
        } catch (Exception e) {
            System.out.println("PIM module test failed: " + e.getMessage());
        }
    }
    
    private static void testLeaveModule() {
        try {
            System.out.println("Testing Leave module...");
            
            // Navigate to Leave
            WebElement leaveMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Leave']")));
            leaveMenu.click();
            
            // Apply Leave
            WebElement applyLeave = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Apply']")));
            applyLeave.click();
            
            // Select Leave Type dropdown
            WebElement leaveTypeDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='oxd-select-text oxd-select-text--active']")));
            leaveTypeDropdown.click();
            
            // Select vacation option
            WebElement vacationOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), 'CAN - Vacation')]")));
            vacationOption.click();
            
            // Enter from date
            WebElement fromDate = driver.findElement(By.xpath("(//input[@placeholder='yyyy-mm-dd'])[1]"));
            fromDate.clear();
            fromDate.sendKeys("2024-12-25");
            
            // Enter to date
            WebElement toDate = driver.findElement(By.xpath("(//input[@placeholder='yyyy-mm-dd'])[2]"));
            toDate.clear();
            toDate.sendKeys("2024-12-26");
            
            // Add comment
            WebElement comment = driver.findElement(By.xpath("//textarea"));
            comment.clear();
            comment.sendKeys("Vacation Leave");
            
            // Submit
            WebElement applyButton = driver.findElement(By.xpath("//button[@type='submit']"));
            applyButton.click();
            
            Thread.sleep(3000);
            System.out.println("Leave applied successfully!");
            
        } catch (Exception e) {
            System.out.println("Leave module test failed: " + e.getMessage());
        }
    }
    
    private static void testRecruitmentModule() {
        try {
            System.out.println("Testing Recruitment module...");
            
            // Navigate to Recruitment
            WebElement recruitmentMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Recruitment']")));
            recruitmentMenu.click();
            
            // Add Candidate
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Add')]")));
            addButton.click();
            
            // Fill candidate details
            WebElement firstName = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("firstName")));
            WebElement lastName = driver.findElement(By.name("lastName"));
            WebElement email = driver.findElement(By.xpath("(//input[@placeholder='Type here'])[1]"));
            
            firstName.clear();
            firstName.sendKeys("Jane");
            lastName.clear();
            lastName.sendKeys("Smith");
            email.clear();
            email.sendKeys("jane.smith@email.com");
            
            // Select vacancy
            WebElement vacancyDropdown = driver.findElement(By.xpath("(//div[@class='oxd-select-text oxd-select-text--active'])[1]"));
            vacancyDropdown.click();
            
            // Select first available vacancy
            List<WebElement> vacancyOptions = driver.findElements(By.xpath("//div[@role='option']"));
            if (!vacancyOptions.isEmpty()) {
                vacancyOptions.get(0).click();
            }
            
            WebElement saveButton = driver.findElement(By.xpath("//button[@type='submit']"));
            saveButton.click();
            
            Thread.sleep(3000);
            System.out.println("Candidate added successfully!");
            
        } catch (Exception e) {
            System.out.println("Recruitment module test failed: " + e.getMessage());
        }
    }
    
    private static void testDirectoryModule() {
        try {
            System.out.println("Testing Directory module...");
            
            // Navigate to Directory
            WebElement directoryMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Directory']")));
            directoryMenu.click();
            
            // Search Directory
            WebElement directorySearch = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='Type for hints...']")));
            directorySearch.clear();
            directorySearch.sendKeys("John");
            
            WebElement searchButton = driver.findElement(By.xpath("//button[@type='submit']"));
            searchButton.click();
            
            Thread.sleep(2000);
            System.out.println("Directory search completed!");
            
        } catch (Exception e) {
            System.out.println("Directory module test failed: " + e.getMessage());
        }
    }
    
    private static void logout() {
        try {
            System.out.println("Logging out...");
            
            WebElement userDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.className("oxd-userdropdown-tab")));
            userDropdown.click();
            
            WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Logout']")));
            logoutLink.click();
            
            // Wait for login page to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
            System.out.println("Logged out successfully!");
            
        } catch (Exception e) {
            System.out.println("Logout failed: " + e.getMessage());
        }
    }
}
