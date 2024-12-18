package smbc.smbc;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotUtility {

    public static void captureScreenshot(WebDriver driver, String filePath) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            // Ensure the target directory exists
            Path targetPath = Paths.get(filePath).getParent();
            if (!Files.exists(targetPath)) {
                Files.createDirectories(targetPath);
            }

            Files.copy(screenshot.toPath(), Paths.get(filePath));
            System.out.println("Screenshot saved: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + filePath);
            e.printStackTrace();
        }
    }
}
