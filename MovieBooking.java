package MovieBooking;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import java.util.List;
import java.util.Set;

public class MovieBooking {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private final String EMAIL = "your.email@gmail.com"; // Replace with your actual email

    public MovieBooking() {
        // No need to set system property if geckodriver is in PATH
        // Otherwise, set the correct path: System.setProperty("webdriver.gecko.driver", "path/to/geckodriver.exe");
        this.driver = new FirefoxDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.actions = new Actions(driver);
        this.driver.manage().window().maximize();
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    public void selectCity() {
        try {
            System.out.println("Opening BookMyShow website...");
            driver.get("https://in.bookmyshow.com/");
            Thread.sleep(3000);

            // Try to find and click Pune city
            try {
                WebElement citySelection = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(),'Pune')]")));
                citySelection.click();
                System.out.println("Selected Pune city");
                Thread.sleep(2000);
            } catch (TimeoutException e) {
                // Alternative selector if the first one doesn't work
                WebElement cityBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'region-selector')]//span[contains(text(),'Pune')]")));
                cityBtn.click();
                System.out.println("Selected Pune city (alternative method)");
                Thread.sleep(2000);
            }

        } catch (Exception e) {
            System.out.println("Error selecting city: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void signIn() {
        try {
            System.out.println("Attempting to sign in...");
            
            // Look for Sign in button with multiple selectors
            WebElement signInButton = null;
            try {
                signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(text(),'Sign in')] | //a[contains(text(),'Sign in')] | //button[contains(text(),'Sign In')]")));
            } catch (TimeoutException e) {
                signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[contains(text(),'Sign') and contains(text(),'in')]")));
            }
            
            signInButton.click();
            System.out.println("Clicked Sign In button");
            Thread.sleep(3000);

            // Look for Google sign-in option
            WebElement googleSignIn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Continue with Google')] | //button[contains(text(),'Google')] | //*[contains(@class,'google')]")));
            googleSignIn.click();
            System.out.println("Clicked Continue with Google");
            Thread.sleep(3000);

            // Handle Google sign-in popup
            switchToGooglePopup();

            // Enter email - Fixed the XPath
            WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@type='devrajjoshi0906@gmail.com'] | //input[@id='identifierId']")));
            emailInput.clear();
            emailInput.sendKeys(EMAIL);
            System.out.println("Entered email");
            Thread.sleep(1000);

            // Click Next
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(text(),'Next')] | //div[contains(text(),'Next')] | //button[contains(text(),'Next')]")));
            nextButton.click();
            System.out.println("Clicked Next");
            Thread.sleep(3000);

            // Note: You'll need to handle password input manually or programmatically
            System.out.println("Please complete the Google sign-in process manually...");
            Thread.sleep(10000); // Wait for manual completion

            // Switch back to main window
            String mainWindow = driver.getWindowHandles().iterator().next();
            driver.switchTo().window(mainWindow);
            Thread.sleep(2000);

        } catch (Exception e) {
            System.out.println("Error during sign in: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void selectLatestHindiMovie() {
        try {
            System.out.println("Selecting latest Hindi movie...");
            
            // Click on Movies tab
            WebElement moviesTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Movies')] | //span[contains(text(),'Movies')] | //*[contains(@href,'movies')]")));
            moviesTab.click();
            System.out.println("Clicked Movies tab");
            Thread.sleep(3000);

            // Wait for page to load
            waitForPageLoad();

            // Try to select Hindi language filter
            try {
                WebElement hindiFilter = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[text()='Hindi'] | //span[text()='Hindi'] | //*[contains(text(),'Hindi')]")));
                hindiFilter.click();
                System.out.println("Applied Hindi filter");
                Thread.sleep(3000);
            } catch (TimeoutException e) {
                System.out.println("Hindi filter not found, proceeding without filter");
            }

            // Select the first available movie
            WebElement latestMovie = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'movie-card')] | //a[contains(@class,'movie')] | //div[contains(@class,'card')]")));
            
            scrollIntoView(latestMovie);
            latestMovie.click();
            System.out.println("Selected movie");
            Thread.sleep(3000);

            // Click Book tickets button
            WebElement bookTickets = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Book tickets')] | //a[contains(text(),'Book')] | //*[contains(text(),'Book') and contains(text(),'ticket')]")));
            bookTickets.click();
            System.out.println("Clicked Book tickets");
            Thread.sleep(3000);

        } catch (Exception e) {
            System.out.println("Error selecting movie: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void selectTheaterAndShow() {
        try {
            System.out.println("Selecting theater and show...");
            
            // Wait for venue search or theater list to load
            Thread.sleep(3000);
            
            // Try to search for specific theater
            try {
                WebElement searchTheater = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[contains(@placeholder,'Search')] | //input[@id='search-venue'] | //input[contains(@class,'search')]")));
                searchTheater.clear();
                searchTheater.sendKeys("City Pride");
                Thread.sleep(2000);
                System.out.println("Searched for City Pride theater");

                // Select from search results
                WebElement theater = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(),'City Pride')] | //div[contains(text(),'City Pride')]")));
                theater.click();
                System.out.println("Selected City Pride theater");
            } catch (TimeoutException e) {
                // If search doesn't work, try to select any available theater
                System.out.println("Search not available, selecting first available theater...");
                WebElement firstTheater = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'venue')] | //a[contains(@class,'cinema')] | //div[contains(@class,'theater')]")));
                firstTheater.click();
                System.out.println("Selected first available theater");
            }
            
            Thread.sleep(3000);

            // Select show timing - look for evening/night shows
            List<WebElement> showTimings = driver.findElements(
                By.xpath("//div[contains(@class,'showtime')] | //button[contains(@class,'time')] | //span[contains(@class,'time')]"));
            
            boolean showSelected = false;
            for (WebElement showTime : showTimings) {
                String time = showTime.getText().trim();
                if (time.length() > 0 && (time.contains("PM") || time.contains("pm"))) {
                    try {
                        showTime.click();
                        System.out.println("Selected show timing: " + time);
                        showSelected = true;
                        break;
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
            
            if (!showSelected && !showTimings.isEmpty()) {
                // Select first available show if no PM show found
                showTimings.get(0).click();
                System.out.println("Selected first available show");
            }
            
            Thread.sleep(3000);

            // Accept Terms & Conditions if prompted
            try {
                WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Accept')] | //button[contains(text(),'Proceed')] | //button[contains(text(),'Continue')]")));
                acceptButton.click();
                System.out.println("Accepted terms and conditions");
                Thread.sleep(2000);
            } catch (TimeoutException e) {
                System.out.println("No terms and conditions dialog found");
            }

        } catch (Exception e) {
            System.out.println("Error selecting theater and show: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void selectSeats() {
        try {
            System.out.println("Selecting seats...");
            
            // Select number of seats
            try {
                WebElement seatCount = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[@id='pop_2'] | //button[contains(text(),'2')] | //*[contains(@data-count,'2')]")));
                seatCount.click();
                System.out.println("Selected 2 seats");
                Thread.sleep(2000);
            } catch (TimeoutException e) {
                System.out.println("Seat count selector not found, proceeding...");
            }

            // Click on Select Seats button
            try {
                WebElement selectSeatsButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Select Seats')] | //a[contains(text(),'Select')] | //button[contains(text(),'Choose')]")));
                selectSeatsButton.click();
                System.out.println("Clicked Select Seats");
                Thread.sleep(5000);
            } catch (TimeoutException e) {
                System.out.println("Select Seats button not found, seat selection might be automatic");
            }

            // Wait for seat layout to load
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(@class,'seat')] | //*[contains(@class,'layout')]")));
                System.out.println("Seat layout loaded");
            } catch (TimeoutException e) {
                System.out.println("Seat layout not detected");
            }

            // Select 2 available seats
            List<WebElement> availableSeats = driver.findElements(
                By.xpath("//a[contains(@class,'available')] | //div[contains(@class,'available')] | //*[contains(@class,'seat') and not(contains(@class,'occupied'))]"));
            
            System.out.println("Found " + availableSeats.size() + " available seats");
            
            if (availableSeats.size() >= 2) {
                // Select first two available seats
                for (int i = 0; i < 2 && i < availableSeats.size(); i++) {
                    try {
                        scrollIntoView(availableSeats.get(i));
                        availableSeats.get(i).click();
                        System.out.println("Selected seat " + (i + 1));
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println("Could not select seat " + (i + 1) + ": " + e.getMessage());
                    }
                }

                Thread.sleep(2000);

                // Click on Pay/Proceed button
                try {
                    WebElement payButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(text(),'Pay')] | //button[contains(text(),'Proceed')] | //button[contains(text(),'Continue')]")));
                    payButton.click();
                    System.out.println("Clicked Pay button");
                    Thread.sleep(3000);
                } catch (TimeoutException e) {
                    System.out.println("Pay button not found - seats might need manual confirmation");
                }
            } else {
                System.out.println("Not enough seats available. Found: " + availableSeats.size());
            }

        } catch (Exception e) {
            System.out.println("Error selecting seats: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void switchToGooglePopup() {
        try {
            String mainWindow = driver.getWindowHandle();
            Thread.sleep(2000);
            
            Set<String> allWindows = driver.getWindowHandles();
            for (String window : allWindows) {
                if (!window.equals(mainWindow)) {
                    driver.switchTo().window(window);
                    System.out.println("Switched to Google popup window");
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("Error switching to Google popup: " + e.getMessage());
        }
    }

    private void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Error scrolling to element: " + e.getMessage());
        }
    }

    private void waitForPageLoad() {
        try {
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            System.out.println("Page load wait failed: " + e.getMessage());
        }
    }

    public void cleanup() {
        try {
            if (driver != null) {
                System.out.println("Closing browser...");
                driver.quit();
            }
        } catch (Exception e) {
            System.out.println("Error during cleanup: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        MovieBooking booking = new MovieBooking();
        try {
            System.out.println("Starting movie booking automation...");
            
            booking.selectCity();
            Thread.sleep(2000);
            
            booking.signIn();
            Thread.sleep(3000);
            
            booking.selectLatestHindiMovie();
            Thread.sleep(3000);
            
            booking.selectTheaterAndShow();
            Thread.sleep(3000);
            
            booking.selectSeats();
            
            System.out.println("Booking process completed. Please complete payment manually if required.");
            Thread.sleep(10000); // Wait to see results
            
        } catch (Exception e) {
            System.out.println("Error in main execution: " + e.getMessage());
            e.printStackTrace();
        } finally {
            booking.cleanup();
        }
    }
}