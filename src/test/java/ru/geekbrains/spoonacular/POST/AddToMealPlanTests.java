package ru.geekbrains.spoonacular.POST;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.geekbrains.spoonacular.dto.AddToMealPlanResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AddToMealPlanTests {

    static Properties properties;
    static String API_KEY;
    static String hash;
    static String host;
    static String endpoint;
    static String username;
    static Integer id;
    static RequestSpecification requestSpecification;
    static ResponseSpecification responseSpecification;
    File requestBody;

    @BeforeAll
    static void beforeAll() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream("src/test/java/ru/geekbrains/spoonacular/POST/application.properties"));
        API_KEY = properties.getProperty("apiKey");
        hash = properties.getProperty("hash");
        host = properties.getProperty("host");
        endpoint = properties.getProperty("endpoint.name");
        username = properties.getProperty("username");

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(host)
                .addHeader("Content-Type", "text/plain")
                .addPathParam("username", username)
                .addQueryParam("apiKey", API_KEY)
                .addQueryParam("hash", hash)
//                .log(LogDetail.ALL)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectContentType("application/json")
                .expectHeader("Connection", "keep-alive")
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectResponseTime(lessThan(1000L))
                .expectStatusCode(200)
                .build();

        RestAssured.baseURI = host;
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


    // Negative auth tests
    @Test
    @DisplayName("wrong auth w/o hash - 401")
    void authNoHashAddToMealPlanTest() {
        given().pathParam("username", username)
                .when().queryParam("apiKey", API_KEY).post(endpoint)
                .then().statusCode(401).body("status", is("failure"));
    }

    @Test
    @DisplayName("wrong auth w/o apiKey - 401")
    void authNoApikeyAddToMealPlanTest() {
        given().pathParam("username", username)
                .when().queryParam("hash", hash).post(endpoint)
                .then().statusCode(401).body("status", is("failure"));
    }


    // Positive POST tests
    @ParameterizedTest
    @ValueSource ( strings = {
            "src/test/resources/spoonacular/recipeRequestBody.json",
            "src/test/resources/spoonacular/ingredientRequestBody.json",
            "src/test/resources/spoonacular/ingredientRequestBodyWrongSlot.json",
            "src/test/resources/spoonacular/menuitemRequestBody.json",
            "src/test/resources/spoonacular/customFoodRequestBody.json"
    })
    void addFoodTypeToMealPlanTest(String str) {
        requestBody = new File(str);
        AddToMealPlanResponse response =
                given().spec(requestSpecification).body(requestBody)
                        .when().post(endpoint)
                        .then().spec(responseSpecification)
                            .extract().body().as(AddToMealPlanResponse.class);

        assertThat(response.getStatus(), equalTo("success"));
        id = response.getId();
    }


}
