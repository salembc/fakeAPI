package Utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class RequestBuilder {

    private String baseUrl;

    public RequestBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response sendPostRequest(String body, Map<String, String> headers) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        request.headers(headers);
        if (body != null && !body.isEmpty()) {
            request.body(body);
        }
        return request.post();
    }

    public Response sendGetRequest(Map<String, String> headers) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        request.headers(headers);
        return request.get();
    }
}