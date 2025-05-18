import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {

    @BeforeAll
    static void setup() {
        // this is the url what will be called for all tests we write
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnOKWithValidToken() {
        // these credentials exist in the db
        String loginpayload = """
                   {
                       "email": "testuser@test.com",
                       "password": "password123"
                   }
                """;

        Response response = given()
                // arrange
                .contentType("application/json")
                .body(loginpayload)
                .when()
                // act
                .post("/auth/login")
                .then()
                // assert
                .statusCode(200)
                .body("token", notNullValue()) // -> assert token is not null
                .extract()
                .response();

        System.out.println("Generated token : " + response.jsonPath().getString("token"));
    }

    @Test
    public void shouldReturnUnauthorizedWithInvalidToken() {
        String loginpayload = """
                   {
                       "email": "invaliduser@test.com",
                       "password": "rong-password"
                   }
                """;

        given()
                // arrange
                .contentType("application/json")
                .body(loginpayload)
                .when()
                // act
                .post("/auth/login")
                .then()
                // assert
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }


}
