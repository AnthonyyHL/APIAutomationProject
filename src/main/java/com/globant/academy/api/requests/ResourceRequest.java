package com.globant.academy.api.requests;

import com.globant.academy.api.models.Resource;
import com.globant.academy.api.utils.Constants;
import com.github.javafaker.Faker;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ResourceRequest extends BaseRequest {
    private final Faker faker = new Faker();
    private String endpoint;

    public Response getResources() {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_ENDPOINT);
        return requestGet(endpoint, createBaseHeaders());
    }

    public Response updateResource(Resource resource, String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_ENDPOINT, resourceId);
        return requestPut(endpoint, createBaseHeaders(), resource);
    }

    public List<Response> createRandomNResources(int n, int starterId) {
        List<Resource> resourceList = new LinkedList<>();
        int starterIdCounter = starterId;
        for (int i = 0; i < n; i++) {
            resourceList.add(this.createRandomResource(String.valueOf(starterIdCounter++)));
        }
        endpoint = String.format(Constants.URL, Constants.RESOURCES_ENDPOINT);
        List<Response> responses = new ArrayList<>();
        for (Resource resource : resourceList) {
            Response response = requestPost(endpoint, createBaseHeaders(), resource);
            responses.add(response);
        }
        return responses;
    }

    public Resource createRandomResource(String id) {
        Resource randomResource = new Resource();
        randomResource.setId(id);
        randomResource.setName(faker.commerce().productName());
        randomResource.setTrademark(faker.company().name());
        randomResource.setStock(String.valueOf(faker.number().numberBetween(0, 1000)));
        randomResource.setPrice(String.valueOf(faker.number().randomDouble(2, 1, 10000)));
        randomResource.setDescription(faker.lorem().sentence(30));
        randomResource.setTags(faker.commerce().promotionCode());
        randomResource.setActive(String.valueOf(faker.random().nextBoolean()));

        return randomResource;
    }

    public List<Resource> getResourcesEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Resource.class);
    }

}
