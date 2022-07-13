package order.api;

import api.model.OrderCreatePostRequest;
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
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderApiCreateTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[]color;

    public OrderApiCreateTest(
            String firstName, String lastName, String address, int metroStation, String phone,
            int rentTime, String deliveryDate, String comment, String[] color
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name="color: {7}")
    public static Object[][]getDataCOrderMain() {
        return new Object[][] {
                {
                        "Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5,
                        "2020-06-06", "Saske, come back to Konoha", new String[]{"BLACK"}
                },
                {
                        "Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5,
                        "2020-06-06", "Saske, come back to Konoha", new String[]{"GREY"}
                },
                {
                        "Василий", "Петров", "Москва, д.12, к. 2", 11, "+7 925 232 22 33", 3,
                        "2020-07-06", "Скорее...", new String[]{"GREY", "BLACK"}
                },
                {
                        "Василий", "Петров", "Москва, д.12, к. 2", 11, "+7 925 232 22 33", 3,
                        "2020-07-06", "Скорее...", new String[]{}
                },

        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Test create order")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка создания заказа")
    public void TestCreateOrder() {
        orderCreatePost(new OrderCreatePostRequest(
                firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color
        )).then().statusCode(201).and().assertThat().body("track", notNullValue());

    }

    @Step("Create order")
    public Response orderCreatePost(OrderCreatePostRequest body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post("/api/v1/orders");
    }

}


