package com.globant.academy.stepDefinitions;

import com.globant.academy.api.models.Resource;
import com.globant.academy.api.requests.ResourceRequest;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class ResourceSteps {
    private static final Logger logger = LogManager.getLogger(ResourceSteps.class);
    private final ResourceRequest resourceRequest = new ResourceRequest();
    private List<Resource> activeResourceList = new ArrayList<>();
    private List<Response> responses;
    private Response response;
    private List<Resource> resourceList;

    @Given("there are {int} or more active resources")
    public void thereAreNOrMoreActiveResources(int resourcesQuantity) {
        response = resourceRequest.getResources();
        Assert.assertEquals(200, response.getStatusCode());

        resourceList = resourceRequest.getResourcesEntity(response);
        if (resourceList.size() != resourcesQuantity) {
            int quantityDiff = resourcesQuantity - resourceList.size();
            responses = resourceRequest.createRandomNResources(quantityDiff, resourceList.size() + 1);

            for (Response response : responses) {
                Assert.assertEquals(201, response.statusCode());
            }
        }
    }

    @When("I search for all active resources")
    public void iSearchForAllActiveResources() {
        response = resourceRequest.getResources();
        logger.info(response.jsonPath().prettify());
        Assert.assertEquals(200, response.getStatusCode());

        resourceList = resourceRequest.getResourcesEntity(response);
        for (Resource resource : resourceList) {
            if (resource.getActive().equals("true")) {
                activeResourceList.add(resource);
                Assert.assertEquals(200, response.statusCode());
            }
        }
    }

    @And("I update them as inactive")
    public void iUpdateThemAsInactive() {
        responses = new ArrayList<>();
        for (Resource resource : resourceList) {
            resource.setActive("false");
            response = resourceRequest.updateResource(resource, resource.getId());
            responses.add(response);
        }
    }

    @Then("the active resources response should have a status code of {int}")
    public void theResourcesResponseShouldHaveAStatusCodeOf(int statusCode) {
        for (Response response : responses) {
            logger.info(response.jsonPath().prettify());
            Assert.assertEquals(statusCode, response.getStatusCode());
        }
    }
}
