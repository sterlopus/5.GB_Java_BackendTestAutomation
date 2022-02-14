import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class SpoonacularTest {

    private static final String API_KEY = "82c9229354f849e78efe010d94150807";

    public SpoonacularTest() throws IOException {
    }

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://api.spoonacular.com";
    }

    @Test
    void SearchRecipesTest() {

        given()
                .param("apiKey", API_KEY)
                .param("query", "pasta")
                .param("number", 3)
                .log()
//                .all()  here we can see "quota usage"
                .params()
                .expect()
                .statusCode(200)
                .time(lessThan(1000L))
                .body("offset", is(0))
                .body("number", is(3))
                .log()
                .body()
                .when()
                .get("recipes/complexSearch")
                .body();
    }

    @Test
    void SearchRecipes200ResponseTest(){
        given().param("apiKey", API_KEY)
                .when().get("recipes/complexSearch")
                .then().statusCode(200);
    }

    @Test
    void SearchRecipesResponseTimeLess500Test() {
        given().param("apiKey", API_KEY)
                .when().get("recipes/complexSearch")
                .then().time(lessThan(500L));
    }

    @Test
    void SearchRecipesContainsStringTest() {
        given().param("apiKey", API_KEY)
                .when().get("recipes/complexSearch")
                .then().body(containsString("Cauliflower"));
    }

    @Test
    void SearchRecipesResultsArraySizeTest(){
        given().param("apiKey", API_KEY)
                .when().get("recipes/complexSearch")
                .then().assertThat().body("results.size()", is(10));
    }


    JsonPath expectedJSON = new JsonPath(FileUtils.readFileToString(
            new File("src/test/resources/ComplexSearchExpected.json"), StandardCharsets.UTF_8));

    @Test
    void SearchRecipesTotalResponseTest(){
        given().param("apiKey", API_KEY)
                .when().get("recipes/complexSearch")
                .then().body("", equalTo(expectedJSON.getMap("")));
    }

}
