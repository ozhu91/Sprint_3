package CourierApi;

import CourierApi.Data.CourierLoginPostRequest;
import CourierApi.Data.CourierLoginPostResponse;
import CourierApi.Data.CourierRegistrationPostResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApiPrototype {
    @Step("Send POST request to /api/v1/courier with Serialized Data")
    public Response courierRegistrationPost(CourierRegistrationPostResponse body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Send POST request to /api/v1/courier with JSON")
    public Response courierRegistrationPost(String json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Send POST request to /api/v1/courier/login with Serialized Data")
    public Response courierLoginPost(CourierLoginPostResponse body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post("/api/v1/courier/login");
    }
    @Step("Send POST request to /api/v1/courier/login with JSON")
    public Response courierLoginPost(String json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
    }


    @Step("Send POST request to /api/v1/courier/login")
    public Response courierDelete(Integer id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + id);
    }

}
