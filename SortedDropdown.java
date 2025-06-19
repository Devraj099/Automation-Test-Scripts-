package SortedDropdown;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SortedDropdown {
    
    public static void main(String[] args) {
        WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            driver.get("https://www.w3schools.com/tags/tryit.asp?filename=tryhtml_select");
            driver.switchTo().frame("iframeResult");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("cars")));
            
            System.out.println("=== Testing Basic Dropdown Sort Verification ===\n");
            verifyDropdownSortBasic(driver, By.name("cars"));
            
            System.out.println("\n=== Testing Complex Sorting Scenarios ===");
            testComplexSortingScenarios();
            
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
    
    // Original basic method (unchanged)
    public static boolean verifyDropdownSortBasic(WebDriver driver, By dropdownLocator) {
        try {
            Select dropdown = new Select(driver.findElement(dropdownLocator));
            
            List<String> originalList = dropdown.getOptions().stream()
                .map(WebElement::getText)
                .filter(text -> !text.trim().isEmpty())
                .collect(Collectors.toList());
            
            List<String> sortedList = new ArrayList<>(originalList);
            Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
            
            boolean isSorted = originalList.equals(sortedList);
            
            System.out.println("BASIC METHOD:");
            System.out.println("Original order: " + originalList);
            System.out.println("Sorted order:   " + sortedList);
            System.out.println("Is already sorted: " + isSorted);
            
            return isSorted;
            
        } catch (Exception e) {
            System.err.println("Error in verifyDropdownSortBasic: " + e.getMessage());
            return false;
        }
    }
    
    // Complex sorting scenarios
    public static void testComplexSortingScenarios() {
        System.out.println("\n1. PRIORITY-BASED SORTING (Countries)");
        testPriorityBasedSorting();
        
        System.out.println("\n2. NATURAL/ALPHANUMERIC SORTING");
        testNaturalSorting();
        
        System.out.println("\n3. VERSION NUMBER SORTING");
        testVersionSorting();
        
        System.out.println("\n4. DATE-BASED SORTING");
        testDateBasedSorting();
        
        System.out.println("\n5. HIERARCHICAL SORTING (Departments/Roles)");
        testHierarchicalSorting();
        
        System.out.println("\n6. MULTI-CRITERIA SORTING");
        testMultiCriteriaSorting();
        
        System.out.println("\n7. WEIGHTED FREQUENCY SORTING");
        testWeightedFrequencySorting();
        
        System.out.println("\n8. GEOGRAPHIC SORTING");
        testGeographicSorting();
    }
    
    // 1. Priority-based sorting for countries/regions
    public static void testPriorityBasedSorting() {
        List<String> countries = Arrays.asList(
            "France", "United States", "Germany", "Canada", 
            "United Kingdom", "Australia", "Japan", "Brazil", "India"
        );
        
        List<String> highPriority = Arrays.asList("United States", "Canada", "United Kingdom");
        List<String> mediumPriority = Arrays.asList("Germany", "France", "Japan");
        
        List<String> sorted = countries.stream()
            .sorted((a, b) -> {
                int aPriority = getPriority(a, highPriority, mediumPriority);
                int bPriority = getPriority(b, highPriority, mediumPriority);
                
                if (aPriority != bPriority) {
                    return Integer.compare(aPriority, bPriority);
                }
                return a.compareToIgnoreCase(b);
            })
            .collect(Collectors.toList());
        
        System.out.println("Original: " + countries);
        System.out.println("Priority sorted: " + sorted);
        System.out.println("Is priority sorted: " + isPrioritySorted(countries, highPriority, mediumPriority));
    }
    
    private static int getPriority(String item, List<String> high, List<String> medium) {
        if (high.contains(item)) return 1;
        if (medium.contains(item)) return 2;
        return 3;
    }
    
    private static boolean isPrioritySorted(List<String> list, List<String> high, List<String> medium) {
        for (int i = 1; i < list.size(); i++) {
            int currentPriority = getPriority(list.get(i), high, medium);
            int previousPriority = getPriority(list.get(i-1), high, medium);
            
            if (currentPriority < previousPriority) return false;
            if (currentPriority == previousPriority && 
                list.get(i).compareToIgnoreCase(list.get(i-1)) < 0) return false;
        }
        return true;
    }
    
    // 2. Natural/Alphanumeric sorting (handles numbers within strings)
    public static void testNaturalSorting() {
        List<String> items = Arrays.asList(
            "Item1", "Item10", "Item2", "Item20", "Item3", 
            "File1.txt", "File10.txt", "File2.txt", "Version1.2", "Version1.10"
        );
        
        List<String> naturalSorted = items.stream()
            .sorted(SortedDropdown::naturalCompare)
            .collect(Collectors.toList());
        
        System.out.println("Original: " + items);
        System.out.println("Natural sorted: " + naturalSorted);
        System.out.println("Is naturally sorted: " + isNaturallySorted(items));
    }
    
    private static int naturalCompare(String a, String b) {
        String[] aParts = a.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        String[] bParts = b.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        
        int maxLength = Math.max(aParts.length, bParts.length);
        
        for (int i = 0; i < maxLength; i++) {
            String aPart = i < aParts.length ? aParts[i] : "";
            String bPart = i < bParts.length ? bParts[i] : "";
            
            if (aPart.matches("\\d+") && bPart.matches("\\d+")) {
                int aNum = Integer.parseInt(aPart);
                int bNum = Integer.parseInt(bPart);
                int result = Integer.compare(aNum, bNum);
                if (result != 0) return result;
            } else {
                int result = aPart.compareToIgnoreCase(bPart);
                if (result != 0) return result;
            }
        }
        return 0;
    }
    
    private static boolean isNaturallySorted(List<String> list) {
        for (int i = 1; i < list.size(); i++) {
            if (naturalCompare(list.get(i-1), list.get(i)) > 0) return false;
        }
        return true;
    }
    
    // 3. Version number sorting
    public static void testVersionSorting() {
        List<String> versions = Arrays.asList(
            "1.0.0", "1.2.0", "1.10.0", "2.0.0", "1.2.3", 
            "1.2.10", "10.0.0", "2.1.0", "1.0.10"
        );
        
        List<String> versionSorted = versions.stream()
            .sorted(SortedDropdown::compareVersions)
            .collect(Collectors.toList());
        
        System.out.println("Original: " + versions);
        System.out.println("Version sorted: " + versionSorted);
        System.out.println("Is version sorted: " + isVersionSorted(versions));
    }
    
    private static int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        
        int maxLength = Math.max(parts1.length, parts2.length);
        
        for (int i = 0; i < maxLength; i++) {
            int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            
            int result = Integer.compare(num1, num2);
            if (result != 0) return result;
        }
        return 0;
    }
    
    private static boolean isVersionSorted(List<String> list) {
        for (int i = 1; i < list.size(); i++) {
            if (compareVersions(list.get(i-1), list.get(i)) > 0) return false;
        }
        return true;
    }
    
    // 4. Date-based sorting (assuming date strings in format)
    public static void testDateBasedSorting() {
        List<String> dates = Arrays.asList(
            "2024-01-15", "2023-12-20", "2024-01-01", 
            "2024-02-10", "2023-11-30", "2024-01-20"
        );
        
        List<String> dateSorted = dates.stream()
            .sorted(String::compareTo) // ISO format sorts naturally
            .collect(Collectors.toList());
        
        System.out.println("Original: " + dates);
        System.out.println("Date sorted: " + dateSorted);
        System.out.println("Is date sorted: " + isDateSorted(dates));
    }
    
    private static boolean isDateSorted(List<String> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i-1).compareTo(list.get(i)) > 0) return false;
        }
        return true;
    }
    
    // 5. Hierarchical sorting (departments with roles)
    public static void testHierarchicalSorting() {
        List<String> roles = Arrays.asList(
            "Engineering - Senior Developer", "Marketing - Manager", 
            "Engineering - Junior Developer", "HR - Specialist",
            "Marketing - Coordinator", "Engineering - Manager",
            "HR - Manager", "Finance - Analyst"
        );
        
        Map<String, Integer> departmentOrder = new HashMap<>();
        departmentOrder.put("Engineering", 1);
        departmentOrder.put("Marketing", 2);
        departmentOrder.put("Finance", 3);
        departmentOrder.put("HR", 4);
        
        Map<String, Integer> roleOrder = new HashMap<>();
        roleOrder.put("Manager", 1);
        roleOrder.put("Senior Developer", 2);
        roleOrder.put("Developer", 3);
        roleOrder.put("Junior Developer", 4);
        roleOrder.put("Analyst", 5);
        roleOrder.put("Coordinator", 6);
        roleOrder.put("Specialist", 7);
        
        List<String> hierarchicalSorted = roles.stream()
            .sorted((a, b) -> {
                String[] aParts = a.split(" - ");
                String[] bParts = b.split(" - ");
                
                String aDept = aParts[0];
                String bDept = bParts[0];
                String aRole = aParts[1];
                String bRole = bParts[1];
                
                int deptComparison = Integer.compare(
                    departmentOrder.getOrDefault(aDept, 999),
                    departmentOrder.getOrDefault(bDept, 999)
                );
                
                if (deptComparison != 0) return deptComparison;
                
                return Integer.compare(
                    roleOrder.getOrDefault(aRole, 999),
                    roleOrder.getOrDefault(bRole, 999)
                );
            })
            .collect(Collectors.toList());
        
        System.out.println("Original: " + roles);
        System.out.println("Hierarchical sorted: " + hierarchicalSorted);
    }
    
    // 6. Multi-criteria sorting (name, then status, then priority)
    public static void testMultiCriteriaSorting() {
        List<String> items = Arrays.asList(
            "Project A [Active] (High)", "Project B [Inactive] (Low)",
            "Project A [Inactive] (Medium)", "Project C [Active] (High)",
            "Project B [Active] (High)", "Project A [Active] (Low)"
        );
        
        List<String> multiSorted = items.stream()
            .sorted((a, b) -> {
                String[] aParts = parseMultiCriteria(a);
                String[] bParts = parseMultiCriteria(b);
                
                // Compare by name first
                int nameComparison = aParts[0].compareTo(bParts[0]);
                if (nameComparison != 0) return nameComparison;
                
                // Then by status (Active before Inactive)
                int statusComparison = aParts[1].compareTo(bParts[1]);
                if (statusComparison != 0) return statusComparison;
                
                // Finally by priority (High, Medium, Low)
                Map<String, Integer> priorityOrder = new HashMap<>();
                priorityOrder.put("High", 1);
                priorityOrder.put("Medium", 2);
                priorityOrder.put("Low", 3);
                
                return Integer.compare(
                    priorityOrder.get(aParts[2]),
                    priorityOrder.get(bParts[2])
                );
            })
            .collect(Collectors.toList());
        
        System.out.println("Original: " + items);
        System.out.println("Multi-criteria sorted: " + multiSorted);
    }
    
    private static String[] parseMultiCriteria(String item) {
        String name = item.split(" \\[")[0];
        String status = item.replaceAll(".*\\[(.*)\\].*", "$1");
        String priority = item.replaceAll(".*\\((.*)\\)", "$1");
        return new String[]{name, status, priority};
    }
    
    // 7. Weighted frequency sorting (popular items first)
    public static void testWeightedFrequencySorting() {
        List<String> items = Arrays.asList(
            "Apple", "Banana", "Apple", "Cherry", "Banana", 
            "Apple", "Date", "Banana", "Cherry"
        );
        
        Map<String, Long> frequency = items.stream()
            .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        
        List<String> frequencySorted = items.stream()
            .distinct()
            .sorted((a, b) -> {
                int freqComparison = Long.compare(frequency.get(b), frequency.get(a)); // Descending
                if (freqComparison != 0) return freqComparison;
                return a.compareToIgnoreCase(b); // Alphabetical for ties
            })
            .collect(Collectors.toList());
        
        System.out.println("Original (with duplicates): " + items);
        System.out.println("Frequency map: " + frequency);
        System.out.println("Frequency sorted: " + frequencySorted);
    }
    
    // 8. Geographic sorting (by continent, then country)
    public static void testGeographicSorting() {
        List<String> locations = Arrays.asList(
            "USA (North America)", "Germany (Europe)", "Japan (Asia)",
            "Canada (North America)", "Brazil (South America)", "France (Europe)",
            "India (Asia)", "Australia (Oceania)", "Mexico (North America)"
        );
        
        Map<String, Integer> continentOrder = new HashMap<>();
        continentOrder.put("North America", 1);
        continentOrder.put("Europe", 2);
        continentOrder.put("Asia", 3);
        continentOrder.put("South America", 4);
        continentOrder.put("Oceania", 5);
        continentOrder.put("Africa", 6);
        
        List<String> geoSorted = locations.stream()
            .sorted((a, b) -> {
                String aContinentPart = a.replaceAll(".*\\((.*)\\)", "$1");
                String bContinentPart = b.replaceAll(".*\\((.*)\\)", "$1");
                String aCountry = a.split(" \\(")[0];
                String bCountry = b.split(" \\(")[0];
                
                int continentComparison = Integer.compare(
                    continentOrder.getOrDefault(aContinentPart, 999),
                    continentOrder.getOrDefault(bContinentPart, 999)
                );
                
                if (continentComparison != 0) return continentComparison;
                return aCountry.compareToIgnoreCase(bCountry);
            })
            .collect(Collectors.toList());
        
        System.out.println("Original: " + locations);
        System.out.println("Geographic sorted: " + geoSorted);
    }
    
    // Selenium-based complex sorting verification
    public static boolean verifyComplexDropdownSort(WebDriver driver, By dropdownLocator, String sortType) {
        try {
            Select dropdown = new Select(driver.findElement(dropdownLocator));
            List<String> options = dropdown.getOptions().stream()
                .map(WebElement::getText)
                .filter(text -> !text.trim().isEmpty())
                .collect(Collectors.toList());
            
            switch (sortType.toLowerCase()) {
                case "natural":
                    return isNaturallySorted(options);
                case "version":
                    return isVersionSorted(options);
                case "date":
                    return isDateSorted(options);
                case "priority":
                    List<String> high = Arrays.asList("United States", "Canada", "United Kingdom");
                    List<String> medium = Arrays.asList("Germany", "France", "Japan");
                    return isPrioritySorted(options, high, medium);
                default:
                    return isBasicSorted(options);
            }
        } catch (Exception e) {
            System.err.println("Error in verifyComplexDropdownSort: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean isBasicSorted(List<String> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i-1).compareToIgnoreCase(list.get(i)) > 0) return false;
        }
        return true;
    }
}