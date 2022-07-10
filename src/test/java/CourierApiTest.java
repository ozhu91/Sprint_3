
import CourierApiData.CourierLoginPostRequest;
import CourierApiData.CourierLoginPostResponse;
import CourierApiData.CourierRegistrationPostResponse;
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
public class CourierApiTest {
    private final String login;
    private final String password;
    private final String firstName;

    public CourierApiTest( String login, String password, String firstName) {
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
        deleteCourier(courierLoginPost(new CourierLoginPostResponse(login, password)));
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
        deleteCourier(courierLoginPost(new CourierLoginPostResponse(login, password)));
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
        deleteCourier(courierLoginPost(new CourierLoginPostResponse(login, password)));
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
        deleteCourier(courierLoginPost(new CourierLoginPostResponse(login, password)));
    }

    @Test
    @DisplayName("Test get ID courier. Positive without password")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка получения id курьера без password")
    public void TestGetCourierWithoutPassword() {
        courierRegistrationPost( new CourierRegistrationPostResponse(login, password, firstName));
        Response loginResponse = courierLoginPost(
                "{\"login\": \"" + login + "\" }"
        );
        loginResponse
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
        deleteCourier(courierLoginPost(new CourierLoginPostResponse(login, password)));
    }

    @Test
    @DisplayName("Test get ID courier. Positive non-existent courier")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка получения id несущевтсующего курьера")
    public void TestGetNonExistentCourier() {
        courierRegistrationPost( new CourierRegistrationPostResponse(login, password, firstName));
        deleteCourier(courierLoginPost(new CourierLoginPostResponse(login, password)));
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
        Response deleteResponse = deleteCourier(
                courierLoginPost(new CourierLoginPostResponse(login, password)));
        deleteResponse.then().statusCode(200);
    }

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

    @Step("Delete courier")
    public Response deleteCourier(Response loginResponse) {
        return  courierDelete(loginResponse
                .body()
                .as(CourierLoginPostRequest.class)
                .getId());
    }
}
