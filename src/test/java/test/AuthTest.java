package test;

import com.codeborne.selenide.Condition;
import data.RegistrationData;
import data.RegistrationInfo;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

public class AuthTest {

    RegistrationData registrationData;

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private void setUpAll() {
        this.registrationData = RegistrationInfo.Registration.generateInfo("en");
        Gson gson = new Gson();
        String jsonData = gson.toJson(this.registrationData);
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(jsonData) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    @Test
    void shouldPass() {
        setUpAll();
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(this.registrationData.getLogin());
        $("[data-test-id=password] input").setValue(this.registrationData.getPassword());
        $("[data-test-id=action-login]").click();
        if (Objects.equals(this.registrationData.getStatus(), "active")) {
            $("h2").shouldHave(Condition.text("Личный кабинет"), Duration.ofSeconds(5));
        } else {
            $("[data-test-id=error-notification]").shouldHave(Condition.text("Пользователь заблокирован"), Duration.ofSeconds(5));
        }
    }
}
