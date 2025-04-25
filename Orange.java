package Orange;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

public class Orange {
    public static void main(String[] args) throws InterruptedException {
        // Set up ChromeDriver
    	System.setProperty("webdriver.firefox.driver", "C:\\selenium webdrivers\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        
        try {
            // Navigate to OrangeHRM
            driver.get("https://opensource-demo.orangehrmlive.com/");
            //driver.manage().window().maximize();
            Thread.sleep(2000);  // Wait for page to load
            
            // Login
            WebElement username = driver.findElement(By.name("username"));
            WebElement password = driver.findElement(By.name("password"));
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
            
            username.sendKeys("Admin");
            password.sendKeys("admin123");
            loginButton.click();
            
            Thread.sleep(2000);  // Wait for dashboard to load
            
            // Verify login success
            WebElement dashboardHeader = driver.findElement(By.xpath("//h6[text()='Dashboard']"));
            if(dashboardHeader.isDisplayed()) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed!");
            }
            
            // Navigate to PIM
            WebElement pimMenu = driver.findElement(By.xpath("//span[text()='PIM']"));
            pimMenu.click();
            Thread.sleep(1000);
            
            // Add Employee
            WebElement addEmployee = driver.findElement(By.xpath("//a[text()='Add Employee']"));
            addEmployee.click();
            Thread.sleep(1000);
            
            // Fill employee details
            WebElement firstName = driver.findElement(By.name("firstName"));
            WebElement lastName = driver.findElement(By.name("lastName"));
            firstName.sendKeys("John");
            lastName.sendKeys("Doe");
            driver.findElement(By.xpath("//button[@type='submit']")).click();
            Thread.sleep(2000);
            System.out.println("Employee added successfully!");

            // Navigate to Employee List
            WebElement employeeList = driver.findElement(By.xpath("//a[text()='Employee List']"));
            employeeList.click();
            Thread.sleep(1000);
            
            // Search for an employee
            WebElement searchName = driver.findElement(By.xpath("//input[@placeholder='Type for hints...']"));
            searchName.sendKeys("John");
            WebElement searchButton = driver.findElement(By.xpath("//button[@type='submit']"));
            searchButton.click();
            Thread.sleep(2000);
            System.out.println("Employee search completed!");

            // Navigate to Leave
            WebElement leaveMenu = driver.findElement(By.xpath("//span[text()='Leave']"));
            leaveMenu.click();
            Thread.sleep(1000);
            
            // Apply Leave
            WebElement applyLeave = driver.findElement(By.xpath("//a[text()='Apply']"));
            applyLeave.click();
            Thread.sleep(1000);
            
            // Select Leave Type
            WebElement leaveType = driver.findElement(By.xpath("//div[contains(@class, 'oxd-select-text--after')]"));
            leaveType.click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//span[contains(text(), 'Vacation')]")).click();
            Thread.sleep(1000);
            
            // Enter leave date
            WebElement fromDate = driver.findElement(By.xpath("//input[@placeholder='yyyy-mm-dd']"));
            fromDate.sendKeys("2024-12-25");
            
            // Add comment
            driver.findElement(By.xpath("//textarea")).sendKeys("Vacation Leave");
            driver.findElement(By.xpath("//button[@type='submit']")).click();
            Thread.sleep(2000);
            System.out.println("Leave applied successfully!");

            // Navigate to Recruitment
            WebElement recruitmentMenu = driver.findElement(By.xpath("//span[text()='Recruitment']"));
            recruitmentMenu.click();
            Thread.sleep(1000);
            
            // Add Candidate
            driver.findElement(By.xpath("//button[text()=' Add ']")).click();
            Thread.sleep(1000);
            
            // Fill candidate details
            driver.findElement(By.name("firstName")).sendKeys("Jane");
            driver.findElement(By.name("lastName")).sendKeys("Smith");
            driver.findElement(By.xpath("//input[@placeholder='Type here']")).sendKeys("jane.smith@email.com");
            driver.findElement(By.xpath("//button[@type='submit']")).click();
            Thread.sleep(2000);
            System.out.println("Candidate added successfully!");

            // Navigate to Directory
            WebElement directoryMenu = driver.findElement(By.xpath("//span[text()='Directory']"));
            directoryMenu.click();
            Thread.sleep(1000);
            
            // Search Directory
            WebElement directorySearch = driver.findElement(By.xpath("//input[@placeholder='Type for hints...']"));
            directorySearch.sendKeys("John");
            driver.findElement(By.xpath("//button[@type='submit']")).click();
            Thread.sleep(2000);
            System.out.println("Directory search completed!");

            // Logout
            driver.findElement(By.className("oxd-userdropdown-tab")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//a[text()='Logout']")).click();
            System.out.println("Logged out successfully!");
            
            System.out.println("Test completed successfully!");
            
        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
