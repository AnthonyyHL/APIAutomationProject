package com.globant.academy.api.requests;

import com.globant.academy.api.utils.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest {
    protected Response requestGet(String endPoint, Map<String, ?> headers) {
        return RestAssured.given()
                          .contentType(Constants.VALUE_CONTENT_TYPE)
                          .headers(headers)
                          .when()
                          .get(endPoint);
    }

    protected Response requestPost(String endPoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                          .contentType(Constants.VALUE_CONTENT_TYPE)
                          .headers(headers)
                          .body(body)
                          .when()
                          .post(endPoint);
    }

    protected Response requestPut(String endPoint, Map<String, ?> headers, Object body) {
        return RestAssured.given()
                          .contentType(Constants.VALUE_CONTENT_TYPE)
                          .headers(headers)
                          .body(body)
                          .when()
                          .post(endPoint);
    }

    protected Map<String, String> createBaseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.VALUE_CONTENT_TYPE);
        return headers;
    }
}
