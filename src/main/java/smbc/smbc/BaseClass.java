package smbc.smbc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class BaseClass {
    protected WebDriver driver;

    // Method to initialize WebDriver
    public void initializeDriver() {
        System.setProperty("webdriver.edge.driver", "C://msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
    }

    // Method to navigate to a specific URL
    public void navigateTo(String url) {
        driver.get(url);
    }

    // Method to close the WebDriver
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
