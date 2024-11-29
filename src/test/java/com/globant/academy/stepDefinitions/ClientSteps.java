package com.globant.academy.stepDefinitions;

import com.globant.academy.api.models.Client;
import com.globant.academy.api.requests.ClientRequest;
import com.globant.academy.api.utils.Constants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Random;

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);
    private final ClientRequest clientRequest = new ClientRequest();
    private final Random random = new Random();
    private List<Response> responses;
    private Response response;
    private List<Client> clientList;
    private Client client;
    private String storedPhoneNumber;

    @Given("there are {int} or more registered clients")
    public void thereAreNOrMoreRegisteredClients(int clientsQuantity) {
        response = clientRequest.getClients();
        logger.info(response.jsonPath().prettify());
        Assert.assertEquals(200, response.getStatusCode());

        clientList = clientRequest.getClientsEntity(response);
        if (clientList.size() != clientsQuantity) {
            int quantityDiff = clientsQuantity - clientList.size();
            responses = clientRequest.createRandomNClients(quantityDiff, clientList.size() + 1);

            for (Response response : responses) {
                Assert.assertEquals(201, response.statusCode());
            }
        }

        int randomId = random.nextInt(9) + 1;
        Client client = clientRequest.createClientByPath(Constants.DEFAULT_CLIENT_PATH);
        response = clientRequest.updateClient(client, String.valueOf(randomId));
    }

    @When("I search for the first client named {string} in the system")
    public void iSearchForTheFirstClientNamedInTheSystem(String clientName) {
        response = clientRequest.getClients();
        logger.info(response.jsonPath().prettify());
        Assert.assertEquals(200, response.getStatusCode());

        clientList = clientRequest.getClientsEntity(response);
        for (Client client : clientList) {
            if (client.getName().equals(clientName)) {
                this.client = client;
            }
        }
    }

    @And("I store her current phone number")
    public void iStoreHerCurrentPhoneNumber() {
        storedPhoneNumber = client.getPhone();
    }


    @And("I update her phone number to {string}")
    public void iUpdateHerPhoneNumberTo(String newPhoneNumber) {
        client.setPhone(newPhoneNumber);
        response = clientRequest.updateClient(client, client.getId());
        Assert.assertEquals(200, response.getStatusCode());
    }

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.getStatusCode());
    }

    @And("the stored number should be different from {string}")
    public void theStoredNumberShouldBeDifferentFrom(String newPhoneNumber) {
        Assert.assertNotEquals(storedPhoneNumber, newPhoneNumber);
        logger.info(String.format("Client name: %s", client.getName()));
        logger.info(String.format("Old phone number: %s", storedPhoneNumber));
        logger.info(String.format("New phone number: %s", newPhoneNumber));
    }

    @And("there is no registered clients")
    public void thereIsNoRegisteredClients() {
        response = clientRequest.getClients();
        Assert.assertEquals(200, response.getStatusCode());
        List<Client> clients = clientRequest.getClientsEntity(response);

        for (Client client : clients) {
            response = clientRequest.deleteClient(client.getId());
            Assert.assertEquals(200, response.getStatusCode());
        }
    }
}
