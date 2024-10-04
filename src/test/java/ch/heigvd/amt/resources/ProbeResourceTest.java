package ch.heigvd.amt.resources;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
class ProbeResourceTest {
    @Test
    void testIndex() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .body(containsString("Welcome to Uptime"));
    }


    @Test
    void testRegister() {
        given().param("url", "https://example.com")
                .when().post("/probes")
                .then()
                .statusCode(200)
                .body(containsString("Probes"));
    }
}