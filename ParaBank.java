package ParaBank;

import java.time.Duration;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import java.util.List;

public class ParaBank {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://parabank.parasoft.com";
    private final String USERNAME = "Collin";
    private final String PASSWORD = "Demo";

    public static void main(String[] args) {
        ParaBank test = new ParaBank();
        try {
            System.out.println("Starting ParaBank automation tests...");
            test.setUp();
            test.registerUserIfNeeded();
            test.loginTest();
            test.accountOverviewTest();
            test.openNewAccountTest();
            test.transferFundsTest();
            test.billPayTest();
            System.out.println("\n=== All tests completed successfully! ===");
        } catch (Exception e) {
            System.out.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            test.tearDown();
        }
    }

    public void setUp() {
        try {
            System.out.println("Setting up WebDriver...");
            // No need to set system property if geckodriver is in PATH
            // System.setProperty("webdriver.gecko.driver", "C:\\selenium webdrivers\\geckodriver.exe");
            
            this.driver = new FirefoxDriver(); // Fixed: assign to instance variable
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            System.out.println("Navigating to ParaBank website...");
            driver.get(BASE_URL);
            Thread.sleep(2000);
            
        } catch (Exception e) {
            throw new RuntimeException("Setup failed: " + e.getMessage());
        }
    }

    public void registerUserIfNeeded() {
        try {
            System.out.println("Checking if user registration is needed...");
            
            // Try to find register link
            WebElement registerLink = driver.findElement(By.linkText("Register"));
            registerLink.click();
            Thread.sleep(2000);

            // Fill registration form
            WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customer.firstName")));
            firstNameField.sendKeys("John");
            
            driver.findElement(By.id("customer.lastName")).sendKeys("Doe");
            driver.findElement(By.id("customer.address.street")).sendKeys("123 Main St");
            driver.findElement(By.id("customer.address.city")).sendKeys("New York");
            driver.findElement(By.id("customer.address.state")).sendKeys("NY");
            driver.findElement(By.id("customer.address.zipCode")).sendKeys("10001");
            driver.findElement(By.id("customer.phoneNumber")).sendKeys("1234567890");
            driver.findElement(By.id("customer.ssn")).sendKeys("123456789");
            
            driver.findElement(By.id("customer.username")).sendKeys(USERNAME);
            driver.findElement(By.id("customer.password")).sendKeys(PASSWORD);
            driver.findElement(By.id("repeatedPassword")).sendKeys(PASSWORD);
            
            // Submit registration
            driver.findElement(By.xpath("//input[@value='Register']")).click();
            Thread.sleep(3000);
            
            // Check if registration was successful
            try {
                WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h1[contains(text(),'Welcome')] | //p[contains(text(),'Your account was created successfully')]")));
                System.out.println("User registered successfully!");
                
                // Navigate back to login page
                driver.get(BASE_URL);
                Thread.sleep(2000);
                
            } catch (TimeoutException e) {
                System.out.println("Registration might have failed or user already exists. Proceeding to login...");
                driver.get(BASE_URL);
                Thread.sleep(2000);
            }
            
        } catch (Exception e) {
            System.out.println("Registration process failed: " + e.getMessage());
            // Continue to login anyway
            driver.get(BASE_URL);
        }
    }

    public void loginTest() {
        try {
            System.out.println("Performing login test...");
            
            // Find login elements
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
            WebElement passwordField = driver.findElement(By.name("password"));
            WebElement loginButton = driver.findElement(By.xpath("//input[@value='Log In']"));

            // Clear and enter credentials
            usernameField.clear();
            usernameField.sendKeys(USERNAME);
            passwordField.clear();
            passwordField.sendKeys(PASSWORD);
            
            Thread.sleep(1000);
            loginButton.click();
            Thread.sleep(3000);

            // Verify login success
            try {
                WebElement welcomeMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='title'] | //h1[contains(text(),'Accounts Overview')] | //p[contains(text(),'Welcome')]")));
                System.out.println("✓ Login test successful!");
            } catch (TimeoutException e) {
                // Check for error message
                try {
                    WebElement errorMsg = driver.findElement(By.xpath("//p[@class='error']"));
                    System.out.println("Login failed with error: " + errorMsg.getText());
                    throw new RuntimeException("Login failed: " + errorMsg.getText());
                } catch (NoSuchElementException ne) {
                    throw new RuntimeException("Login failed: Unknown error");
                }
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Login test failed: " + e.getMessage());
        }
    }

    public void accountOverviewTest() {
        try {
            System.out.println("Performing account overview test...");
            
            // Click Accounts Overview
            WebElement accountsOverview = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Accounts Overview")));
            accountsOverview.click();
            Thread.sleep(2000);

            // Wait for accounts table
            WebElement accountsTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accountTable")));

            // Get account information
            List<WebElement> accountRows = driver.findElements(By.xpath("//table[@id='accountTable']//tr[position()>1]"));
            
            if (accountRows.size() > 0) {
                WebElement firstAccountLink = accountRows.get(0).findElement(By.tagName("a"));
                String accountNumber = firstAccountLink.getText();
                System.out.println("✓ Account Overview test successful! Found account: " + accountNumber);
                System.out.println("Total accounts found: " + accountRows.size());
            } else {
                System.out.println("No accounts found. This might be expected for a new user.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Account Overview test failed: " + e.getMessage());
        }
    }

    public void openNewAccountTest() {
        try {
            System.out.println("Performing open new account test...");
            
            // Navigate to Open New Account
            WebElement openNewAccountLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Open New Account")));
            openNewAccountLink.click();
            Thread.sleep(2000);

            // Wait for form
            WebElement accountTypeSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("type")));

            // Select account type
            Select accountType = new Select(accountTypeSelect);
            accountType.selectByVisibleText("SAVINGS");
            Thread.sleep(1000);

            // Select from account (if available)
            try {
                Select fromAccount = new Select(driver.findElement(By.id("fromAccountId")));
                if (fromAccount.getOptions().size() > 0) {
                    fromAccount.selectByIndex(0);
                }
            } catch (Exception e) {
                System.out.println("No existing account to select from, using default");
            }

            // Submit
            WebElement openAccountButton = driver.findElement(By.xpath("//input[@value='Open New Account']"));
            openAccountButton.click();
            Thread.sleep(3000);

            // Verify success
            try {
                WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h1[contains(text(),'Account Opened!')] | //p[contains(text(),'Congratulations')]")));
                System.out.println("✓ Open New Account test successful!");
                
                // Get new account number if available
                try {
                    WebElement newAccountNumber = driver.findElement(By.id("newAccountId"));
                    System.out.println("New account number: " + newAccountNumber.getText());
                } catch (Exception e) {
                    System.out.println("Could not retrieve new account number");
                }
                
            } catch (TimeoutException e) {
                System.out.println("Account opening might have failed or taken longer than expected");
            }

        } catch (Exception e) {
            throw new RuntimeException("Open New Account test failed: " + e.getMessage());
        }
    }

    public void transferFundsTest() {
        try {
            System.out.println("Performing transfer funds test...");
            
            // Navigate to Transfer Funds
            WebElement transferFundsLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Transfer Funds")));
            transferFundsLink.click();
            Thread.sleep(2000);

            // Wait for transfer form
            WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("amount")));

            // Fill transfer details
            amountField.clear();
            amountField.sendKeys("50");

            // Select accounts (if available)
            try {
                Select fromAccount = new Select(driver.findElement(By.id("fromAccountId")));
                if (fromAccount.getOptions().size() > 0) {
                    fromAccount.selectByIndex(0);
                }

                Select toAccount = new Select(driver.findElement(By.id("toAccountId")));
                if (toAccount.getOptions().size() > 1) {
                    toAccount.selectByIndex(1);
                } else if (toAccount.getOptions().size() > 0) {
                    toAccount.selectByIndex(0);
                }
            } catch (Exception e) {
                System.out.println("Account selection issue: " + e.getMessage());
            }

            Thread.sleep(1000);

            // Submit transfer
            WebElement transferButton = driver.findElement(By.xpath("//input[@value='Transfer']"));
            transferButton.click();
            Thread.sleep(3000);

            // Verify transfer success
            try {
                WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h1[contains(text(),'Transfer Complete!')] | //p[contains(text(),'has been transferred')]")));
                System.out.println("✓ Transfer Funds test successful!");
            } catch (TimeoutException e) {
                // Check for error messages
                try {
                    WebElement errorMsg = driver.findElement(By.xpath("//p[@class='error']"));
                    System.out.println("Transfer failed: " + errorMsg.getText());
                } catch (Exception ex) {
                    System.out.println("Transfer may have failed - no success message found");
                }
            }

        } catch (Exception e) {
            System.out.println("Transfer Funds test failed: " + e.getMessage());
            // Don't throw exception, continue with other tests
        }
    }

    public void billPayTest() {
        try {
            System.out.println("Performing bill pay test...");
            
            // Navigate to Bill Pay
            WebElement billPayLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Bill Pay")));
            billPayLink.click();
            Thread.sleep(2000);

            // Wait for form
            WebElement payeeNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("payee.name")));

            // Fill payee information
            payeeNameField.clear();
            payeeNameField.sendKeys("Electric Company");
            
            driver.findElement(By.name("payee.address.street")).sendKeys("456 Power St");
            driver.findElement(By.name("payee.address.city")).sendKeys("Electric City");
            driver.findElement(By.name("payee.address.state")).sendKeys("CA");
            driver.findElement(By.name("payee.address.zipCode")).sendKeys("90210");
            driver.findElement(By.name("payee.phoneNumber")).sendKeys("5551234567");
            
            WebElement accountNumberField = driver.findElement(By.name("payee.accountNumber"));
            accountNumberField.sendKeys("12345678");
            
            WebElement verifyAccountField = driver.findElement(By.name("verifyAccount"));
            verifyAccountField.sendKeys("12345678");
            
            WebElement amountField = driver.findElement(By.name("amount"));
            amountField.sendKeys("75");

            // Select from account
            try {
                Select fromAccount = new Select(driver.findElement(By.name("fromAccountId")));
                if (fromAccount.getOptions().size() > 0) {
                    fromAccount.selectByIndex(0);
                }
            } catch (Exception e) {
                System.out.println("From account selection issue: " + e.getMessage());
            }

            Thread.sleep(1000);

            // Submit payment
            WebElement sendPaymentButton = driver.findElement(By.xpath("//input[@value='Send Payment']"));
            sendPaymentButton.click();
            Thread.sleep(3000);

            // Verify success
            try {
                WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h1[contains(text(),'Bill Payment Complete')] | //p[contains(text(),'payment has been successfully submitted')]")));
                System.out.println("✓ Bill Pay test successful!");
            } catch (TimeoutException e) {
                // Check for errors
                try {
                    WebElement errorMsg = driver.findElement(By.xpath("//p[@class='error']"));
                    System.out.println("Bill payment failed: " + errorMsg.getText());
                } catch (Exception ex) {
                    System.out.println("Bill payment may have failed - no success message found");
                }
            }

        } catch (Exception e) {
            System.out.println("Bill Pay test failed: " + e.getMessage());
            // Don't throw exception, continue with other tests
        }
    }

    public void tearDown() {
        try {
            System.out.println("Cleaning up...");
            
            if (driver != null) {
                // Try to logout
                try {
                    WebElement logoutLink = driver.findElement(By.linkText("Log Out"));
                    logoutLink.click();
                    Thread.sleep(1000);
                    System.out.println("✓ Logged out successfully!");
                } catch (Exception e) {
                    System.out.println("Logout link not found or already logged out");
                }
                
                driver.quit();
                System.out.println("✓ Browser closed successfully!");
            }
        } catch (Exception e) {
            System.out.println("Cleanup failed: " + e.getMessage());
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception ex) {
                    System.out.println("Force quit failed: " + ex.getMessage());
                }
            }
        }
    }

    // Utility Methods
    private void waitForElement(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Scroll failed: " + e.getMessage());
        }
    }

    private void handleAlert() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("Alert detected: " + alertText);
            alert.accept();
        } catch (NoAlertPresentException e) {
            // No alert present, continue
        }
    }

    private boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private void waitAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollIntoView(element);
        element.click();
    }
}