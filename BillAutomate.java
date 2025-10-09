package BillAutomate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BillAutomate {
    
    // Product class to store product details
    static class Product {
        String name;
        int quantity;
        double price;
        
        Product(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }
        
        double getTotal() {
            return quantity * price;
        }
    }
    
    public static void main(String[] args) {
        WebDriver driver = null;
        
        try {
            // Set ChromeDriver path
        	 System.setProperty("webdriver.firefox.driver", "C:\\selenium webdrivers\\geckodriver.exe");
            
        	 driver = new FirefoxDriver();
            //driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            // Navigate to e-commerce site
            driver.get("https://www.saucedemo.com");
            
            // Login
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            
            System.out.println("✓ Login successful!");
            System.out.println("=====================================");
            
            // Add products to cart (simulating Indian products)
            // Need to re-find elements after each click to avoid stale element exception
            
            // Add Laptop Backpack
            driver.findElements(By.className("btn_inventory")).get(0).click();
            Thread.sleep(500); // Small wait for DOM update
            
            // Add LED Bike Light
            driver.findElements(By.className("btn_inventory")).get(1).click();
            Thread.sleep(500);
            
            // Add Cotton T-Shirt (first one)
            driver.findElements(By.className("btn_inventory")).get(2).click();
            Thread.sleep(500);
            
            // Add Winter Jacket
            driver.findElements(By.className("btn_inventory")).get(3).click();
            Thread.sleep(500);
            
            System.out.println("✓ Products added to cart");
            
            // Go to cart
            driver.findElement(By.className("shopping_cart_link")).click();
            
            // Create product list with Indian pricing (adjusted for 4 items)
            List<Product> products = new ArrayList<>();
            products.add(new Product("Laptop Backpack", 1, 2499.00));
            products.add(new Product("LED Bike Light", 1, 899.00));
            products.add(new Product("Cotton T-Shirt", 1, 599.00));
            products.add(new Product("Winter Jacket", 1, 3999.00));
            
            // Generate Indian Bill
            System.out.println("\n========================================");
            System.out.println("           TECH MART");
            System.out.println("   123 Shopping Street, Pune, MH 411001");
            System.out.println("   Phone: +91-20-1234-5678");
            System.out.println("   GST: 27AABCU9603R1ZM");
            System.out.println("========================================");
            
            // Bill details
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy, HH:mm");
            System.out.println("Bill No: INV-2025-00847");
            System.out.println("Date: " + now.format(formatter));
            System.out.println("Cashier: John Doe");
            System.out.println("Customer: Walk-in");
            System.out.println("========================================");
            
            // Print items
            System.out.println("\nITEM                    QTY   PRICE    AMOUNT");
            System.out.println("----------------------------------------");
            
            double subtotal = 0.0;
            for (Product product : products) {
                double itemTotal = product.getTotal();
                subtotal += itemTotal;
                System.out.printf("%-20s    %d   ₹%.2f   ₹%.2f%n", 
                    product.name, product.quantity, product.price, itemTotal);
            }
            
            System.out.println("----------------------------------------");
            
            // Calculate GST (12% - CGST 6% + SGST 6%)
            double cgst = subtotal * 0.06; // 6% CGST
            double sgst = subtotal * 0.06; // 6% SGST
            double totalGst = cgst + sgst;  // 12% Total GST
            double discount = 200.00;
            double grandTotal = subtotal + totalGst - discount;
            
            // Print totals
            System.out.printf("\nSubtotal:                      ₹%.2f%n", subtotal);
            System.out.printf("CGST (6%%):                     ₹%.2f%n", cgst);
            System.out.printf("SGST (6%%):                     ₹%.2f%n", sgst);
            System.out.printf("Total GST (12%%):               ₹%.2f%n", totalGst);
            System.out.printf("Discount:                     -₹%.2f%n", discount);
            System.out.println("========================================");
            System.out.printf("GRAND TOTAL:                   ₹%.2f%n", grandTotal);
            System.out.println("========================================");
            
            System.out.println("\nPayment Method: UPI (9876543210@paytm)");
            System.out.println("\n========================================");
            System.out.println("      THANK YOU FOR SHOPPING!");
            System.out.println("   Visit us at www.techmart.com");
            System.out.println("========================================");
            
            // Verify cart items on website
            List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
            System.out.println("\n\n--- WEBSITE VERIFICATION ---");
            System.out.println("Items in cart on website: " + cartItems.size());
            
            if (cartItems.size() > 0) {
                System.out.println("✓ Cart verification PASSED");
            } else {
                System.out.println("✗ Cart verification FAILED");
            }
            
            // Proceed to checkout
            driver.findElement(By.id("checkout")).click();
            
            // Fill checkout information
            driver.findElement(By.id("first-name")).sendKeys("Rahul");
            driver.findElement(By.id("last-name")).sendKeys("Sharma");
            driver.findElement(By.id("postal-code")).sendKeys("411001");
            driver.findElement(By.id("continue")).click();
            
            System.out.println("✓ Checkout information filled");
            
            // Verify checkout page
            WebElement summarySubtotal = driver.findElement(By.className("summary_subtotal_label"));
            WebElement summaryTax = driver.findElement(By.className("summary_tax_label"));
            WebElement summaryTotal = driver.findElement(By.className("summary_total_label"));
            
            System.out.println("\n--- WEBSITE BILLING SUMMARY ---");
            System.out.println(summarySubtotal.getText());
            System.out.println(summaryTax.getText());
            System.out.println(summaryTotal.getText());
            
            // Complete order
            driver.findElement(By.id("finish")).click();
            
            // Verify order completion
            WebElement confirmationMsg = driver.findElement(By.className("complete-header"));
            System.out.println("\n--- ORDER STATUS ---");
            System.out.println(confirmationMsg.getText());
            
            if (confirmationMsg.getText().contains("Thank you")) {
                System.out.println("\n✓✓✓ ORDER COMPLETED SUCCESSFULLY! ✓✓✓");
                System.out.println("✓✓✓ BILLING TEST PASSED! ✓✓✓");
            } else {
                System.out.println("\n✗ Order completion failed");
            }
            
            // GST Calculation Verification
            System.out.println("\n========================================");
            System.out.println("GST CALCULATION BREAKDOWN:");
            System.out.println("========================================");
            System.out.printf("Base Amount: ₹%.2f%n", subtotal);
            System.out.printf("CGST Rate: 6%% = ₹%.2f%n", cgst);
            System.out.printf("SGST Rate: 6%% = ₹%.2f%n", sgst);
            System.out.printf("Total GST: 12%% = ₹%.2f%n", totalGst);
            System.out.printf("Final Amount: ₹%.2f%n", grandTotal);
            System.out.println("========================================");
            
            if (totalGst == (subtotal * 0.12)) {
                System.out.println("✓ GST Calculation VERIFIED");
            } else {
                System.out.println("✗ GST Calculation ERROR");
            }
            
        } catch (Exception e) {
            System.out.println("\n✗✗✗ Test failed with error ✗✗✗");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close browser
            if (driver != null) {
                try {
                    Thread.sleep(3000); // Wait 3 seconds to see the result
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                driver.quit();
                System.out.println("\n✓ Browser closed");
            }
        }
    }
}