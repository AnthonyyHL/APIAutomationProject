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
    private String clientId;

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
        logger.info(String.format("Phone number successfully updated to: %s", newPhoneNumber));
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

    @When("I create a new client sending a POST request")
    public void iCreateANewClientSendingAPOSTRequest() {
        response = clientRequest.getClients();
        clientList = clientRequest.getClientsEntity(response);
        String starterId = String.valueOf(clientList.size() + 1);
        Assert.assertEquals(200, response.getStatusCode());

        client = clientRequest.createRandomClient(starterId);
        response = clientRequest.createClient(client);

        logger.info(response.jsonPath().prettify());

        Assert.assertEquals(201, response.getStatusCode());
    }

    @And("I search for the new client in the system")
    public void iSearchForTheNewClientInTheSystem() {
        response = clientRequest.getClients();
        clientList = clientRequest.getClientsEntity(response);
        Assert.assertEquals(200, response.getStatusCode());

        for (Client client : clientList) {
            if (client.getId().equals(this.client.getId())) {
                Assert.assertEquals(client, this.client);
                logger.info(String.format("Client %s was successfully found", client.getName()));
                this.client = client;
            }
        }
    }

    @And("I update the {string} parameter of the new client with the following value: {string}")
    public void iUpdateARandomParameterOfTheNewClient(String parameter, String newValue) {
        clientId = client.getId();
        Client updatedClient = clientRequest.updateParameter(client, parameter, newValue);

        response = clientRequest.updateClient(updatedClient, clientId);
        Assert.assertEquals(200, response.getStatusCode());
        logger.info(String.format("Parameter \"%s\" with value \"%s\" in the new client", parameter, newValue));
        logger.info(response.jsonPath().prettify());
    }

    @And("I remove the new client from the system")
    public void iRemoveTheNewClientFromTheSystem() {
        response = clientRequest.deleteClient(clientId);
    }
}