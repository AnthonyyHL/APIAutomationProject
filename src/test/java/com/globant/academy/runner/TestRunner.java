package com.globant.academy.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.globant.academy.stepDefinitions",
        plugin = {"pretty:target/cucumber/cucumber.txt",
                "html:target/cucumber/report",
                "json:target/cucumber.json"}
)

public class TestRunner {
}
