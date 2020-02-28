import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;

public class TestRestMessage {
    private static final Logger LOGGER = Logger.getLogger(TestRestMessage.class.getName());

    public TestRestMessage() {
    }

    @BeforeEach
    public void setBaseUri() {
        RestAssured.baseURI = "https://maps.googleapis.com";
    }

    @Test
    public void testStatusCode() {
        LOGGER.info("base uri is: " + RestAssured.baseURI);

        Response res =
                given().param("query", "restaurants in mumbai")
                        .param("key", "Xyz")
                        .when()
                        .get("maps/api/place/textsearch/json");
        assertThat(res.statusCode()).isEqualTo(200);
    }

    @Test
    public void testStatusCodeRestAssured() {
        given().param("query", "restaurants in mumbai")
                .param("key", "AIzaSyB4XsJfLmE4yLMdxBFPAi9f4WR6JlBHD2I")
                .when()
                .get("/maps/api/place/textsearch/json")
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    public void test01() {
        Response res = given().param("query", "Churchgate Station")
                .param("key", "AIzaSyB4XsJfLmE4yLMdxBFPAi9f4WR6JlBHD2I")
                .when()
                .get("/maps/api/place/textsearch/json")
                .then()
                .contentType(ContentType.JSON)
                .extract().response();

        LOGGER.info(res.asString());

    }
}
