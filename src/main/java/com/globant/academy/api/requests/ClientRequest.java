package com.globant.academy.api.requests;

import com.globant.academy.api.models.Client;
import com.globant.academy.api.utils.Constants;
import com.globant.academy.api.utils.JsonFileReader;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import com.github.javafaker.Faker;

import java.lang.reflect.Method;
import java.util.*;

public class ClientRequest extends BaseRequest {
    private final Faker faker = new Faker();
    private String endpoint;

    public Response getClients() {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_ENDPOINT);
        return requestGet(endpoint, createBaseHeaders());
    }

    public Response updateClient(Client client, String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_ENDPOINT, clientId);
        return requestPut(endpoint, createBaseHeaders(), client);
    }

    /**
     Create client

     @param client model
     @return rest-assured response
     */
    public Response createClient(Client client) {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_ENDPOINT);
        return requestPost(endpoint, createBaseHeaders(), client);
    }

    public List<Response> createRandomNClients(int n, int starterId) {
        List<Client> clientList = new LinkedList<>();
        int starterIdCounter = starterId;
        for (int i = 0; i < n; i++) {
            clientList.add(this.createRandomClient(String.valueOf(starterIdCounter++)));
        }
        endpoint = String.format(Constants.URL, Constants.CLIENTS_ENDPOINT);
        List<Response> responses = new ArrayList<>();
        for (Client client : clientList) {
            Response response = requestPost(endpoint, createBaseHeaders(), client);
            responses.add(response);
        }
        return responses;
    }

    public Client createRandomClient(String id) {
        Client randomClient = new Client();
        randomClient.setId(id);
        randomClient.setName(faker.name().firstName());
        randomClient.setLastName(faker.name().lastName());
        randomClient.setEmail(faker.internet().emailAddress());
        randomClient.setPhone(faker.phoneNumber().cellPhone());
        randomClient.setCity(faker.address().city());
        randomClient.setCountry(faker.address().country());

        return randomClient;
    }

    public Client createClientByPath(String path) {
        JsonFileReader jsonFile = new JsonFileReader();
        return (Client) jsonFile.getObjectByJson(path, Client.class);
    }

    /**
     Delete client by id

     @param clientId string
     @return rest-assured response
     */
    public Response deleteClient(String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_ENDPOINT, clientId);
        return requestDelete(endpoint, createBaseHeaders());
    }

    public List<Client> getClientsEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Client.class);
    }

    public Client updateParameter(Client client, String parameter, String newParameterValue) {
        try {
            String method = String.format("set%s%s", parameter.substring(0, 1).toUpperCase(), parameter.substring(1));
            Method setter = client.getClass().getMethod(method, newParameterValue.getClass());
            setter.invoke(client, newParameterValue);
        } catch (Exception ignored) {}
        return client;
    }

    public boolean validateSchema(Response response, String schemaPath) {
        try {
            response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            return true;
        }
        catch(AssertionError e) {
            return false;
        }
    }

}
