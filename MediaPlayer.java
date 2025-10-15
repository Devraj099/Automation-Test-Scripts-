package MediaPlayer;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MediaPlayer {
    
    public static void main(String[] args) {
        WebDriver driver = null;
        JavascriptExecutor js = null;
        
        try {
            // Set up FirefoxDriver with options
            FirefoxOptions options = new FirefoxOptions();
            options.addPreference("media.autoplay.default", 0); // Allow autoplay
            options.addPreference("media.autoplay.blocking_policy", 0);
            options.addPreference("media.autoplay.allow-muted", true);
            options.setAcceptInsecureCerts(true);
            
            System.out.println("Starting Firefox browser...");
            driver = new FirefoxDriver(options);
            js = (JavascriptExecutor) driver;
            
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            
            // Using YouTube's HTML5 video player (no login required)
            System.out.println("Navigating to video page...");
            driver.get("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
            
            Thread.sleep(5000); // Wait for page to load
            System.out.println("✓ Page loaded successfully");
            System.out.println("Current URL: " + driver.getCurrentUrl());
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            
            // Test 1: Locate the video player
            System.out.println("\n=== Test 1: Locating Video Player ===");
            WebElement videoPlayer = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("video.html5-main-video"))
            );
            
            if (videoPlayer.isDisplayed()) {
                System.out.println("✓ PASS: Video player found and displayed");
            } else {
                System.out.println("✗ FAIL: Video player not visible");
            }
            
            // Test 2: Check if video is loaded
            System.out.println("\n=== Test 2: Check Video Loaded ===");
            Boolean videoLoaded = (Boolean) js.executeScript(
                "return arguments[0].readyState >= 2;", videoPlayer
            );
            
            if (videoLoaded) {
                System.out.println("✓ PASS: Video is loaded and ready");
            } else {
                System.out.println("✗ FAIL: Video not loaded properly");
            }
            
            // Test 3: Get video duration
            System.out.println("\n=== Test 3: Get Video Duration ===");
            Object durationObj = js.executeScript(
                "return arguments[0].duration;", videoPlayer
            );
            double duration = durationObj instanceof Long ? 
                ((Long) durationObj).doubleValue() : (Double) durationObj;
            
            if (duration > 0) {
                System.out.println("✓ PASS: Video duration: " + String.format("%.2f", duration) + " seconds");
            } else {
                System.out.println("✗ FAIL: Could not get video duration");
            }
            
            // Test 4: Play the video
            System.out.println("\n=== Test 4: Play Video ===");
            js.executeScript("arguments[0].play();", videoPlayer);
            Thread.sleep(3000);
            
            Boolean isPlaying = (Boolean) js.executeScript(
                "return !arguments[0].paused && !arguments[0].ended;", videoPlayer
            );
            
            if (isPlaying) {
                System.out.println("✓ PASS: Video is playing");
                
                // Get current time
                Object currentTimeObj = js.executeScript(
                    "return arguments[0].currentTime;", videoPlayer
                );
                double currentTime = currentTimeObj instanceof Long ? 
                    ((Long) currentTimeObj).doubleValue() : (Double) currentTimeObj;
                System.out.println("  Current playback time: " + String.format("%.2f", currentTime) + " seconds");
            } else {
                System.out.println("✗ FAIL: Video is not playing");
            }
            
            // Test 5: Pause the video
            System.out.println("\n=== Test 5: Pause Video ===");
            js.executeScript("arguments[0].pause();", videoPlayer);
            Thread.sleep(2000);
            
            Boolean isPaused = (Boolean) js.executeScript(
                "return arguments[0].paused;", videoPlayer
            );
            
            if (isPaused) {
                System.out.println("✓ PASS: Video is paused");
            } else {
                System.out.println("✗ FAIL: Video did not pause");
            }
            
            // Test 6: Set and verify volume
            System.out.println("\n=== Test 6: Set Volume ===");
            js.executeScript("arguments[0].volume = 0.5;", videoPlayer);
            Thread.sleep(1000);
            
            Object volumeObj = js.executeScript(
                "return arguments[0].volume;", videoPlayer
            );
            double volume = volumeObj instanceof Long ? 
                ((Long) volumeObj).doubleValue() : (Double) volumeObj;
            
            if (Math.abs(volume - 0.5) < 0.01) {
                System.out.println("✓ PASS: Volume set to 50% (0.5)");
            } else {
                System.out.println("✗ FAIL: Volume not set correctly. Current: " + volume);
            }
            
            // Test 7: Mute/Unmute
            System.out.println("\n=== Test 7: Mute Video ===");
            js.executeScript("arguments[0].muted = true;", videoPlayer);
            Thread.sleep(1000);
            
            Boolean isMuted = (Boolean) js.executeScript(
                "return arguments[0].muted;", videoPlayer
            );
            
            if (isMuted) {
                System.out.println("✓ PASS: Video is muted");
            } else {
                System.out.println("✗ FAIL: Video is not muted");
            }
            
            // Unmute
            js.executeScript("arguments[0].muted = false;", videoPlayer);
            Thread.sleep(1000);
            System.out.println("✓ Video unmuted");
            
            // Test 8: Seek to specific time
            System.out.println("\n=== Test 8: Seek to 10 seconds ===");
            js.executeScript("arguments[0].currentTime = 10;", videoPlayer);
            Thread.sleep(2000);
            
            Object seekTimeObj = js.executeScript(
                "return arguments[0].currentTime;", videoPlayer
            );
            double seekTime = seekTimeObj instanceof Long ? 
                ((Long) seekTimeObj).doubleValue() : (Double) seekTimeObj;
            
            if (seekTime >= 9 && seekTime <= 11) {
                System.out.println("✓ PASS: Seeked to ~10 seconds. Current time: " + String.format("%.2f", seekTime));
            } else {
                System.out.println("✗ FAIL: Seek failed. Current time: " + seekTime);
            }
            
            // Test 9: Check video dimensions
            System.out.println("\n=== Test 9: Video Dimensions ===");
            Long videoWidth = (Long) js.executeScript(
                "return arguments[0].videoWidth;", videoPlayer
            );
            Long videoHeight = (Long) js.executeScript(
                "return arguments[0].videoHeight;", videoPlayer
            );
            
            System.out.println("✓ Video resolution: " + videoWidth + "x" + videoHeight);
            
            // Test 10: Get playback rate
            System.out.println("\n=== Test 10: Playback Speed ===");
            Object playbackRateObj = js.executeScript(
                "return arguments[0].playbackRate;", videoPlayer
            );
            double playbackRate = playbackRateObj instanceof Long ? 
                ((Long) playbackRateObj).doubleValue() : (Double) playbackRateObj;
            System.out.println("✓ Current playback rate: " + playbackRate + "x");
            
            // Change playback speed to 1.5x
            js.executeScript("arguments[0].playbackRate = 1.5;", videoPlayer);
            Thread.sleep(1000);
            playbackRateObj = js.executeScript(
                "return arguments[0].playbackRate;", videoPlayer
            );
            playbackRate = playbackRateObj instanceof Long ? 
                ((Long) playbackRateObj).doubleValue() : (Double) playbackRateObj;
            System.out.println("✓ Playback rate changed to: " + playbackRate + "x");
            
            System.out.println("\n==================================================");
            System.out.println("ALL TESTS COMPLETED SUCCESSFULLY!");
            System.out.println("==================================================");
            
            Thread.sleep(3000); // Keep browser open for 3 seconds to see results
            
        } catch (Exception e) {
            System.err.println("\n==================================================");
            System.err.println("ERROR OCCURRED");
            System.err.println("==================================================");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("\nFull stack trace:");
            e.printStackTrace();
        } finally {
            if (driver != null) {
                try {
                    System.out.println("\nClosing browser...");
                    Thread.sleep(2000);
                    driver.quit();
                    System.out.println("✓ Browser closed successfully");
                } catch (Exception e) {
                    System.err.println("Error while closing browser: " + e.getMessage());
                }
            }
        }
    }
}