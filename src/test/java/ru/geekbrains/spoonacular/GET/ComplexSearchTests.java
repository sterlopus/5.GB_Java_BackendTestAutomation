package ru.geekbrains.spoonacular.GET;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;


public class ComplexSearchTests {

    static Properties properties;
    static String host;
    static String API_KEY;
    static String endpoint;

    static RequestSpecification requestSpecification;
    static ResponseSpecification responseSpecification;

    public ComplexSearchTests() throws IOException {
    }

    @BeforeAll
    static void beforeAll() throws IOException {

        properties = new Properties();
        properties.load(new FileInputStream("src/test/java/ru/geekbrains/spoonacular/GET/application.properties"));
        host = properties.getProperty("host");
        endpoint = properties.getProperty("endpoint.name");
        API_KEY = properties.getProperty("apiKey");

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(host)
                .addQueryParam("apiKey", API_KEY)
                .log(LogDetail.ALL)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(1000L))
                .expectStatusCode(200)
                .expectContentType("application/json")
                .build();

        RestAssured.baseURI = host;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


    // Negative auth tests
    @Test
    @DisplayName("Negative authorization (no apikey")
    void complexSearchNotAuthorizedTest() {
        when().get(endpoint)
                .then().statusCode(401);
    }

    @Test
    @DisplayName("Negative authorization (wrong apikey")
    void complexSearchWrongApiKeyTest() {
        given().param("apiKey", "WRONG_API_KEY")
                .when().get(endpoint)
                .then().statusCode(401);
    }


    // Positive tests
    @Test
    @DisplayName("response fields all are good ")
    void searchRecipes200ResponseTest() {
        given().spec(requestSpecification)
                .when().get(endpoint)
                .then().spec(responseSpecification);
    }


    @Test
    @DisplayName("Response array size is 10")
    void searchRecipesResultsArraySizeTest() {
        given().spec(requestSpecification)
                .when().get(endpoint)
                .then().spec(responseSpecification)
                    .body("results.size()", is(10));
    }


    JsonPath expectedJSON = new JsonPath(FileUtils.readFileToString(
            new File("src/test/java/ru/geekbrains/spoonacular/GET/ComplexSearchExpected.json"), StandardCharsets.UTF_8));
    @Test
    @DisplayName("Response JSON equal to expected from file")
    void searchRecipesTotalResponseTest() {
        given().spec(requestSpecification)
                .when().get(endpoint)
                .then().spec(responseSpecification)
                    .body("", equalTo(expectedJSON.getMap("")));
    }

}
