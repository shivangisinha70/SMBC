package smbc.selenium.steps;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.NoSuchElementException;
import smbc.smbc.BaseClass;
import smbc.smbc.ScreenshotUtility;

import io.cucumber.java.en.*;
import static org.junit.Assert.*;

public class StepDefinitions extends BaseClass {

    @Given("I navigate to the Investopedia website")
    public void i_navigate_to_the_investopedia_website() {
        initializeDriver(); // Initialize WebDriver
        System.out.println("Driver initialized: " + (driver != null)); // Debug log
        driver.get("https://www.investopedia.com/markets/quote?tvwidgetsymbol=aapl");

        // Handle cookie consent
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'onetrust-close-btn-handler')]")));
            closeButton.click();
            System.out.println("Cookie consent closed successfully.");
        } catch (Exception e) {
            System.out.println("Cookie consent prompt not found or already handled.");
        }
    }

    @When("I search for {string}")
    public void i_search_for(String stock) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='Search Company or Symbol...']"))); // Update the XPath as needed
            searchBox.sendKeys(stock);
            searchBox.submit();
        } catch (TimeoutException e) {
            System.out.println("Element not found. Current page source:");
            System.out.println(driver.getPageSource());
            throw e;
        }
    }

    @Then("I capture the stock title and price")
    public void i_capture_the_stock_title_and_price() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Increase wait time to 20 seconds
        try {
            WebElement iframe = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[contains(@src, 'tradingview-widget.com')]")));
            driver.switchTo().frame(iframe);
            
            // Locate and interact with elements inside the iframe
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='tv-symbol-header__first-line tv-symbol-info-widget__first-line'][contains(text(),'Apple Inc')]"))); 
            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='tv-symbol-price-quote__value js-symbol-last']/span")));
            
            // Log element text for debugging
            System.out.println("Title element found: " + titleElement.getText());
            System.out.println("Price element found: " + priceElement.getText());

            String title = titleElement.getText();
            //String priceText = priceElement.getText().replace("$", "");
            double price = Double.parseDouble(priceElement.getText());

            System.out.println("Stock Title: " + title);
            System.out.println("Stock Price: " + price);
        } catch (TimeoutException | StaleElementReferenceException e) {
            System.out.println("Element not found or became stale. Current page source:");
            System.out.println(driver.getPageSource());
            throw e;
        } finally {
            // Switching back to main content
            driver.switchTo().defaultContent();
        }
    }

    @Then("I validate if the price is below {int} or above {int}")
    public void i_validate_if_the_price_is_below_or_above(Integer lowerLimit, Integer upperLimit) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50)); // Increased wait time to 50 seconds 
        try {
            // Switch to the correct iframe with retry logic for StaleElementReferenceException
            WebElement iframe = null;
            for (int retry = 0; retry < 3; retry++) {
                try {
                    iframe = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[contains(@src, 'tradingview-widget.com')]")));
                    driver.switchTo().frame(iframe);
                    break;
                } catch (StaleElementReferenceException e) {
                    System.out.println("Stale element reference during frame switch, retrying...");
                }
            }
            
            if (iframe == null) {
                throw new NoSuchElementException("Unable to switch to iframe after retries.");
            }
            
            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='tv-symbol-price-quote__value js-symbol-last']/span")));
            double price = Double.parseDouble(priceElement.getText().replace("$", ""));

            if (price < lowerLimit) {
                System.out.println("Test Passed: Stock price is below " + lowerLimit + " USD.");
            } else if (price > upperLimit) {
                System.out.println("Test Passed: Stock price is above " + upperLimit + " USD.");
            } else {
                System.out.println("Test Failed: Stock price is between " + lowerLimit + " and " + upperLimit + " USD.");
                fail("Stock price did not meet the criteria.");
            }
        } catch (TimeoutException | StaleElementReferenceException e) {
            System.out.println("Element not found or became stale. Current page source:"); 
            System.out.println(driver.getPageSource()); // Print page source for debugging
            throw e;
        } finally {
            // Switch back to the main content
            driver.switchTo().defaultContent();
        }
    }

    @Then("I take a screenshot for test evidence")
    public void i_take_a_screenshot_for_test_evidence() {
        ScreenshotUtility.captureScreenshot(driver, "test-evidence/apple_stock_screenshot.png");
        tearDown(); // Close WebDriver
    }
}
