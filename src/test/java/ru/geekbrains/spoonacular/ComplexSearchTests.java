package ru.geekbrains.spoonacular;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class ComplexSearchTests {

    static Properties properties;
    static String host;
    static String API_KEY;
    static String endpoint;

    public ComplexSearchTests() throws IOException {
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream("src/test/resources/application.properties"));
        host = properties.getProperty("host");
        API_KEY = properties.getProperty("api-key");
        endpoint = properties.getProperty("endpoint.name");
        RestAssured.baseURI = host;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


    @Test
    @DisplayName("Response 200")
    void searchRecipes200ResponseTest() {
        given().param("apiKey", API_KEY)
                .when().get(endpoint)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Response time less than 500ms")
    void searchRecipesResponseTimeLess500Test() {
        given().param("apiKey", API_KEY)
                .when().get(endpoint)
                .then().time(lessThan(500L));
    }

    @Test
    @DisplayName("Response body contains 'Cauliflower' string")
    void searchRecipesContainsStringTest() {
        given().param("apiKey", API_KEY)
                .when().get(endpoint)
                .then().body(containsString("Cauliflower"));
    }

    @Test
    @DisplayName("Response array size is 10")
    void searchRecipesResultsArraySizeTest() {
        given().param("apiKey", API_KEY)
                .when().get(endpoint)
                .then().assertThat().body("results.size()", is(10));
    }


    JsonPath expectedJSON = new JsonPath(FileUtils.readFileToString(
            new File("src/test/resources/ComplexSearchExpected.json"), StandardCharsets.UTF_8));

    @Test
    @DisplayName("Response JSON equal to expected from file")
    void searchRecipesTotalResponseTest() {
        given().param("apiKey", API_KEY)
                .when().get(endpoint)
                .then().body("", equalTo(expectedJSON.getMap("")));
    }

}
