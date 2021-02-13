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
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
/*
    I confess I couldn't create the jar properly using maven so I
    copied tests here...
 */
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
        JSONObject user = new JSONObject();
        user.put("firstName", "AAA");
        user.put("lastName", "BBB");
        user.put("phone", "5321456789");
        user.put("email", "I_Am_KEK@kekmail.com");

        given()
                .spec(spec)
                .when()
                .body(user.toJSONString())
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
    public void getContactsUserWithBadId() {
        given()
                .spec(spec)
                .when()
                .get("users/-1/contacts/")
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }



    @Test
    public void createUserFirstNameBetween2And15() {
        JSONObject user = new JSONObject();
        user.put("firstName", "A");
        user.put("lastName", "B");

        given()
                .spec(spec)
                .body(user.toJSONString())
                .post("users")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("$", hasKey("firstName"))
                .body("size()", is(1));
    }
}
