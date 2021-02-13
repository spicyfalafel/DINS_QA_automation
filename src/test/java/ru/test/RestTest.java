package ru.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class RestTest {
    private static RequestSpecification spec;

    public static String ENDPOINT = "http://localhost:8080/";
    @BeforeClass
    public static void initSpec() {
        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(ENDPOINT)
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    @Test
    public void createUserShouldReturn201() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("firstName", "AAA");
        requestParams.put("lastName", "BBB");
        requestParams.put("phone", "5321456789");
        requestParams.put("email", "I_Am_KEK@kekmail.com");

        given()
                .spec(spec)
                .when()
                .body(requestParams.toJSONString())
                .post("users")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void searchUserByName() {
        given()
                .spec(spec)
                .param("name", "John")
                .when()
                .get("users/search")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("size()", is(2));
    }

    @Test
    public void getContactFromCertainUser() {
        given()
                .spec(spec)
                .when()
                .get("users/1/contacts/2")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void findContactById() {
        given()
                .spec(spec)
                .param("userId", "2")
                .when()
                .get("users/1/contacts/search")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
