import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class PatientIntegrationTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnPatientsWithValidToken() {
        String loginpayload = """
                   {
                       "email": "testuser@test.com",
                       "password": "password123"
                   }
                """;

        String token = given()
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
                .jsonPath()
                .getString("token");
        System.out.println("Generated token : " + token);

        // get token then get all patients
        given()
                .headers("Authorization", "Bearer " + token)
                .when()
                .get("/api/patients")
                .then()
                .statusCode(200)
                .body("patients", notNullValue());
    }
}
