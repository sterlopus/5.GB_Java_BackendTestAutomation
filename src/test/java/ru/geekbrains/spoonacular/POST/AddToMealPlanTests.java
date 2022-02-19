package ru.geekbrains.spoonacular.POST;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;
import static org.hamcrest.Matchers.*;

public class AddToMealPlanTests {

    static Properties properties;
    static String API_KEY;
    static String hash;
    static String endpoint;
    static String username;
    static Integer id;
    File requestBody;

    @BeforeAll
    static void beforeAll() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream("src/test/resources/POST/application.properties"));
        API_KEY = properties.getProperty("apiKey");
        hash = properties.getProperty("hash");
        endpoint = properties.getProperty("endpoint.name");
        username = properties.getProperty("username");
        RestAssured.baseURI = properties.getProperty("host");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


    @AfterEach
    void tearDown() {
        if (id != null) {
            given().pathParam("username", username)
                    .when().queryParam("hash", hash).queryParam("apiKey", API_KEY).delete(endpoint + "/" + id)
                    .then().statusCode(200).body("status", is("success"))
            ;
            id = null;

        }
    }



    @Test
    @DisplayName("wrong auth w/o hash - 401")
    void authNoHashAddToMealPlanTest() {

        given().pathParam("username", username)
                .when().queryParam("apiKey", API_KEY).post(endpoint)
                .then().statusCode(401).body("status", is("failure"))
        ;
    }


    @Test
    @DisplayName("wrong auth w/o apiKey - 401")
    void authNoApikeyAddToMealPlanTest() {
        given().pathParam("username", username)
                .when().queryParam("hash", hash).post(endpoint)
                .then().statusCode(401).body("status", is("failure"))
        ;
    }


    @Test
    @DisplayName("post INGREDIENT to plan - OK")
    void postIngredientToMealPlan() {
        requestBody = new File("src/test/resources/POST/ingredientRequestBody.json");

        id = given().pathParam("username", username).body(requestBody)
                .when().queryParam("hash", hash).queryParam("apiKey", API_KEY).post(endpoint)
                .then().statusCode(200).body("status", is("success"))
                .extract().response().path("id")
        ;
    }

    @Test
    @DisplayName("post RECIPE to plan - OK")
    void postRecipeToMealPlan() {
        requestBody = new File("src/test/resources/POST/ingredientRequestBody.json");

        id = given().pathParam("username", username).body(requestBody)
                .when().queryParam("hash", hash).queryParam("apiKey", API_KEY).post(endpoint)
                .then().statusCode(200).body("status", is("success"))
                .extract().response().path("id")
        ;
    }

}
