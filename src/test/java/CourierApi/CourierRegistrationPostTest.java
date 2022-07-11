package CourierApi;

import CourierApi.Data.CourierLoginPostRequest;
import CourierApi.Data.CourierLoginPostResponse;
import CourierApi.Data.CourierRegistrationPostResponse;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CourierRegistrationPostTest extends CourierApiPrototype {
    private final String login;
    private final String password;
    private final String firstName;

    public CourierRegistrationPostTest( String login, String password, String firstName) {
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
    @DisplayName("Test create new courier. Positive create")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка создания нового курьера ")
    public void TestCreateNewCourier() {
        Response registrationResponse = courierRegistrationPost(
                new CourierRegistrationPostResponse(login, password, firstName));
        registrationResponse.then().statusCode(201);
        courierDelete(courierLoginPost(new CourierLoginPostResponse(login, password)).body()
                .as(CourierLoginPostRequest.class)
                .getId());
    }

    @Test
    @DisplayName("Test create recurrent courier. Positive recurrent")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка создания уже зарегистрированного курьера ")
    public void TestCreateRecurrentCourier() {
        courierRegistrationPost( new CourierRegistrationPostResponse(login, password, firstName));
        Response registrationResponse = courierRegistrationPost(
                new CourierRegistrationPostResponse(login, password, firstName));
        registrationResponse
                .then()
                .statusCode(409)
                .and()
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        courierDelete(courierLoginPost(new CourierLoginPostResponse(login, password)).body()
                .as(CourierLoginPostRequest.class)
                .getId());
    }

    @Test
    @DisplayName("Test create courier. Positive without login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка создания курьера без указания login")
    public void TestCreateCourierWithoutLogin() {
        Response registrationResponse = courierRegistrationPost(
                "{\"password\": " + password + ", \"firstName\": \"" + firstName + "\" }"
        );
        registrationResponse
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Test create courier. Positive without password")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка создания курьера без указания password")
    public void TestCreateCourierWithoutPassword() {
        Response registrationResponse = courierRegistrationPost(
                "{\"login\": \"" + login + "\", \"firstName\": \"" + firstName + "\" }"
        );
        registrationResponse
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
