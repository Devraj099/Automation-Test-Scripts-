package Instalogin;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

public class InstaScript {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String USERNAME = "devrajjoshi99";
    private final String PASSWORD = "Devraj$9";

    public InstaScript() {
        System.setProperty("webdriver.firefox.driver", "C:\\selenium webdrivers\\geckodriver.exe");
        this.driver = new FirefoxDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Increased wait time
        this.driver.manage().window().maximize();
    }

    public void login() {
        try {
            driver.get("https://www.instagram.com/accounts/login/");
            Thread.sleep(3000); // Increased wait time

            // Updated selectors for username and password
            WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[name='username']")));
            usernameField.sendKeys(USERNAME);

            WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[name='password']")));
            passwordField.sendKeys(PASSWORD);

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[type='submit']")));
            loginButton.click();

            Thread.sleep(5000); // Increased wait time

            // Handle "Save Login Info" popup
            try {
                WebElement notNowButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[text()='Not Now']")));
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
            driver.get("https://www.instagram.com/explore/tags/" + hashtag);
            Thread.sleep(5000); // Increased wait time

            // Updated selector for recent posts
            WebElement recentPost = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("article img[decoding='auto']")));
            recentPost.click();

            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(2000);
                    
                    // Updated like button selector
                    WebElement likeButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("svg[aria-label='Like']")));
                    likeButton.click();

                    // Updated next button selector
                    WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("svg[aria-label='Next']")));
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
            driver.get("https://www.instagram.com/" + username);
            Thread.sleep(3000);

            // Updated follow button selector
            WebElement followButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button._acan._acap._acas")));
            followButton.click();

            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Error following user: " + e.getMessage());
        }
    }

    public void postStory(String imagePath) {
        try {
            // Click on Create/New Post button (updated selector)
            WebElement newPostButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("svg[aria-label='New post']")));
            newPostButton.click();

            Thread.sleep(2000);

            // Updated file input selector
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='file']")));
            fileInput.sendKeys(imagePath);

            Thread.sleep(2000);

            // Updated next button selector
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button:contains('Next')")));
            nextButton.click();

            Thread.sleep(2000);

            // Updated caption field selector
            WebElement captionField = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("textarea[aria-label='Write a caption...']")));
            captionField.sendKeys("Test automation post #automation");

            // Updated share button selector
            WebElement shareButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button:contains('Share')")));
            shareButton.click();

            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("Error posting story: " + e.getMessage());
        }
    }

    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static void main(String[] args) {
        InstaScript bot = new InstaScript();
        try {
            bot.login();
            Thread.sleep(5000); // Added delay after login
            
            bot.likeRecentPosts("automation");
            Thread.sleep(2000);
            
            bot.followUser("seleniumhq");
            Thread.sleep(2000);
            
            bot.postStory("path_to_your_image.jpg");
            
        } catch (Exception e) {
            System.out.println("Error in main execution: " + e.getMessage());
        } finally {
            bot.cleanup();
        }
    }
}
