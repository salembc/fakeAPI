package FakeApi;

import io.restassured.response.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

import Utilities.RequestBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.json.JSONObject;

import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiTest {

    private static Properties properties;
    private static String url;

    @BeforeAll
    public static void setUp() throws IOException {
        properties = loadProperties();
        url = properties.getProperty("apiFake.url");

    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = ApiTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return null;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }

    @BeforeEach
    public void beforeEach(TestInfo testinfo, TestReporter testReporter) {
        testReporter.publishEntry("Execution Date");
    }

    @AfterEach
    public void afterEach(TestInfo testinfo) {
        System.out.println("-------------------------------------------------------------------------------");
    }

    @Test
    @DisplayName("apiFake - petición POST")
    public void CP001() {
        try {

            // Create request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("name", "salami");
            requestBody.put("job", "goat");

            String body = requestBody.toString();

            // Config headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");

            // Build n' send request
            RequestBuilder requestBuilder = new RequestBuilder(url + "/api/users");
            Response response = requestBuilder.sendPostRequest(body, headers);
            response.prettyPrint();

            // Assertions
            assertEquals(201, response.getStatusCode());

            String responseBody = response.getBody().asString();
            Map<String, Object> responseMap = from(responseBody).getMap("");

            assertThat(responseMap, hasKey("id"));
            assertThat(responseMap.get("id"), instanceOf(String.class));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("apiFake - petición GET")
    public void CP002() {

        try {

            Map<String, String> headers = new HashMap<>();

            RequestBuilder requestBuilder = new RequestBuilder(url + "/api/users/1");
            Response response = requestBuilder.sendGetRequest(headers);
            response.prettyPrint();

            assertEquals(200, response.getStatusCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}