package OrderApi;

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
import static org.hamcrest.Matchers.notNullValue;

public class OrderApiGetPostsListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Test get order list")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка получения списка заказов")
    public void TestGetOrderList() {
        orderListGet().then().statusCode(200).and().assertThat().body("orders", notNullValue());
    }

    @Test
    @DisplayName("Test get order list with params")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка получения списка заказов с указанием параметров")
    public void TestGetOrderListWithParams() {
        orderListGet("?limit=3").then().statusCode(200).and().assertThat().body("orders", notNullValue());
    }

    @Step("Get order list")
    public Response orderListGet() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
    }

    @Step("Get order list with parameters")
    public Response orderListGet(String params) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders" + params);
    }

}


