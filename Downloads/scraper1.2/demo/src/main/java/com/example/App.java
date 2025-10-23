package com.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws InterruptedException, IOException {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.zomato.com/te/madurai");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int desiredCount = 15;
        List<WebElement> restaurantLinks;

        // Scroll to load enough restaurants
        while (true) {
            restaurantLinks = driver.findElements(By.xpath("//a[@class='sc-hqGPoI kCiEKB']"));
            if (restaurantLinks.size() >= desiredCount) break;
            js.executeScript("window.scrollBy(0,1000)");
            Thread.sleep(1000);
        }

        System.out.println("Total restaurants loaded: " + restaurantLinks.size());

        // Save restaurant URLs to avoid stale references
        List<String> restaurantUrls = new ArrayList<>();
        for (WebElement link : restaurantLinks) {
            restaurantUrls.add(link.getAttribute("href"));
        }

        CSVWriter writer = new CSVWriter(new FileWriter("madurai_reviews.csv"));
        String[] header = {"No", "City", "Restaurant", "Review"};
        writer.writeNext(header);

        int count = 1;

        for (String restaurantUrl : restaurantUrls) {
            driver.get(restaurantUrl);
            Thread.sleep(2000);

            // Get restaurant name from <h1>
            String restaurantName;
            try {
                restaurantName = driver.findElement(By.xpath("//h1")).getText();
            } catch (NoSuchElementException e) {
                System.out.println("No <h1> found for: " + restaurantUrl);
                continue;
            }

            // Click the actual Reviews tab
            try {
                WebElement reviewButton = driver.findElement(By.xpath("//a[contains(text(),'Reviews')]"));
                reviewButton.click();
                Thread.sleep(2000);
                js.executeScript("window.scrollBy(0,500)"); // ensure reviews load
                Thread.sleep(1000);
            } catch (NoSuchElementException e) {
                System.out.println("No Reviews tab for: " + restaurantName);
                continue;
            }

            // Fetch reviews
            List<WebElement> reviews = driver.findElements(By.xpath(
                "//p[" +
                "starts-with(@class,'sc-1hez2tp-0 sc-') " +
                "and not(@color) " +
                "and not(contains(text(),'Select restaurants based on user generated reviews, ratings and photos')) " +
                "and not(contains(text(),'Using GPS')) " +
                "and not(contains(text(),'View Gallery')) " +
                "and not(contains(text(),'Detect current location')) " +
                "and not(contains(text(),'Reviews are better in app')) " +
                "and not(contains(.,'New to Zomato? Create account')) " +
                "and normalize-space() " +
                "and string-length(normalize-space(.)) - string-length(translate(normalize-space(.), ' ', '')) >= 2" +
                "]"
            ));

            for (WebElement rev : reviews) {
                String reviewText = rev.getText();
                if (!reviewText.trim().isEmpty() && reviewText.split(" ").length >= 3) {
                    String[] line = {String.valueOf(count), "Madurai", restaurantName, reviewText};
                    writer.writeNext(line);
                    count++;
                }
            }
        }

        writer.close();
        driver.quit();
        System.out.println("Scraping completed. Total reviews saved: " + (count - 1));
    }
}
