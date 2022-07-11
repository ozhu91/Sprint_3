package CourierApi;

import CourierApi.Data.CourierLoginPostRequest;
import CourierApi.Data.CourierLoginPostResponse;
import CourierApi.Data.CourierRegistrationPostResponse;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@RunWith(Parameterized.class)
public class CourierApiLoginTest extends CourierApiPrototype {
    private final String login;
    private final String password;
    private final String firstName;

    public CourierApiLoginTest( String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][]getDataCourier() {
        return new Object[][] {
                {"zhuma", "1234", "zhumzhum"}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Test get ID courier. Positive authorization")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка получения id курьера")
    public void TestGetCourier() {
        courierRegistrationPost( new CourierRegistrationPostResponse(login, password, firstName));
        CourierLoginPostResponse AuthorizationBody = new CourierLoginPostResponse(
                login, password);
        Response loginResponse = courierLoginPost(AuthorizationBody);
        loginResponse
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("id", notNullValue());
        courierDelete(courierLoginPost(new CourierLoginPostResponse(login, password)).body()
                .as(CourierLoginPostRequest.class)
                .getId());
    }

    @Test
    @DisplayName("Test get ID courier. Positive without login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка получения id курьера без login")
    public void TestGetCourierWithoutLogin() {
        courierRegistrationPost( new CourierRegistrationPostResponse(login, password, firstName));
        Response loginResponse = courierLoginPost(
                "{\"password\": " + password + "}"
        );
        loginResponse
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
        courierDelete(courierLoginPost(new CourierLoginPostResponse(login, password)).body()
                .as(CourierLoginPostRequest.class)
                .getId());
    }

    @Test
    @DisplayName("Test get ID courier. Positive without password")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка получения id курьера без password")
    public void TestGetCourierWithoutPassword() {
        courierRegistrationPost( new CourierRegistrationPostResponse(login, password, firstName));
        String newPassword = password + "wr";
        Response loginResponse = courierLoginPost(
                "{\"login\": \"" + login + "\",\"password\": \"" + newPassword + "\"}"
        );
        loginResponse
                .then()
                .statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
        courierDelete(courierLoginPost(new CourierLoginPostResponse(login, password)).body()
                .as(CourierLoginPostRequest.class)
                .getId());
    }

    @Test
    @DisplayName("Test get ID courier. Positive non-existent courier")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка получения id несущевтсующего курьера")
    public void TestGetNonExistentCourier() {
        courierRegistrationPost( new CourierRegistrationPostResponse(login, password, firstName));
        courierDelete(courierLoginPost(new CourierLoginPostResponse(login, password)).body()
                .as(CourierLoginPostRequest.class)
                .getId());
        CourierLoginPostResponse AuthorizationBody = new CourierLoginPostResponse(
                login, password);
        Response loginResponse = courierLoginPost(AuthorizationBody);
        loginResponse
                .then()
                .statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Test delete courier")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка удаления курьера")
    public void TestDeleteCourier() {
        courierRegistrationPost( new CourierRegistrationPostResponse(login, password, firstName));
        Response deleteResponse = courierDelete(courierLoginPost(new CourierLoginPostResponse(login, password)).body()
                                    .as(CourierLoginPostRequest.class)
                                    .getId());
        deleteResponse.then().statusCode(200);
    }

}
