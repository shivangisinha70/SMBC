package smbc.selenium.steps;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resource/features",  // Path to feature files
        glue = "smbc.selenium.steps",                   // Package containing StepDefinitions
        plugin = {"pretty", "html:target/cucumber-reports.html"} // Reporting plugins
)
public class TestRunner {
}
