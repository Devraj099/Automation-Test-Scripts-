package Instalogin;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InstaScript {
    private WebDriver driver;  // Instance variable
    private WebDriverWait wait;
    private final String USERNAME = "devrajjoshi99";
    private final String PASSWORD = "Devraj$9";

    public InstaScript() {
        // Fix: Remove WebDriver declaration and assign directly to instance variable
        System.setProperty("webdriver.firefox.driver", "C:\\selenium webdrivers\\geckodriver.exe");
        this.driver = new FirefoxDriver();  // Assign to instance variable using 'this'
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.driver.manage().window().maximize();
    }

  


public void login() {
    try {
        // Navigate to Instagram
        driver.get("https://www.instagram.com/");
        Thread.sleep(2000); // Wait for page to load

        // Find and fill username
        WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.name("username")));
        usernameField.sendKeys(USERNAME);

        // Find and fill password
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys(PASSWORD);

        // Click login button
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        // Wait for login to complete
        Thread.sleep(3000);

        // Handle "Save Login Info" popup if it appears
        try {
            WebElement notNowButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Not Now')]")));
            notNowButton.click();
        } catch (Exception e) {
            System.out.println("Save Login Info popup did not appear");
        }

    } catch (Exception e) {
        System.out.println("Error during login: " + e.getMessage());
    }
}

public void likeRecentPosts(String hashtag) {
    try {
        // Navigate to hashtag page
        driver.get("https://www.instagram.com/explore/tags/" + hashtag);
        Thread.sleep(3000);

        // Click on most recent post
        WebElement recentPost = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@class='_aagw']")));
        recentPost.click();

        // Like several posts
        for (int i = 0; i < 5; i++) {
            try {
                // Wait for like button and click it
                WebElement likeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@class='_aamw']//button")));
                likeButton.click();

                // Click next button
                WebElement nextButton = driver.findElement(
                    By.xpath("//button[@class=' _abl-']//div//span[contains(@class, 'next')]"));
                nextButton.click();

                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Error liking post: " + e.getMessage());
                break;
            }
        }

    } catch (Exception e) {
        System.out.println("Error in liking posts: " + e.getMessage());
    }
}

public void followUser(String username) {
    try {
        // Navigate to user's profile
        driver.get("https://www.instagram.com/" + username);
        Thread.sleep(2000);

        // Click follow button
        WebElement followButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Follow')]")));
        followButton.click();

        Thread.sleep(2000);
    } catch (Exception e) {
        System.out.println("Error following user: " + e.getMessage());
    }
}

public void postStory(String imagePath) {
    try {
        // Click on "New Post" button
        WebElement newPostButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@role='menuitem']")));
        newPostButton.click();

        // Upload image
        WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//input[@type='file']")));
        fileInput.sendKeys(imagePath);

        // Wait for upload and click Next
        WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Next')]")));
        nextButton.click();

        // Add caption and share
        WebElement captionField = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//textarea[@aria-label='Write a caption...']")));
        captionField.sendKeys("Test automation post #automation");

        WebElement shareButton = driver.findElement(By.xpath("//button[contains(text(), 'Share')]"));
        shareButton.click();

        Thread.sleep(3000);
    } catch (Exception e) {
        System.out.println("Error posting story: " + e.getMessage());
    }
}

public void cleanup() {
    // Close the browser
    if (driver != null) {
        driver.quit();
    }
}

public static void main(String[] args) {
    InstaScript bot = new InstaScript();
    try {
        // Execute test sequence
        bot.login();
        Thread.sleep(2000);
        
        // Like posts with hashtag
        bot.likeRecentPosts("automation");
        
        // Follow a user
        bot.followUser("seleniumhq");
        
        // Post a story
        bot.postStory("path_to_your_image.jpg");
        
    } catch (Exception e) {
        System.out.println("Error in main execution: " + e.getMessage());
    } finally {
        bot.cleanup();
    }
}
}