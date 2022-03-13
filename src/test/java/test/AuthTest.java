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
import static data.RegistrationInfo.registrateUser;
import static io.restassured.RestAssured.given;

public class AuthTest {


    @Test
    void wrongLogin() {
        RegistrationData registrationData = RegistrationInfo.Registration.generateInfo("en");
        registrateUser(registrationData);
        open("http://localhost:9999");
        RegistrationData newRegistrationData = RegistrationInfo.Registration.generateInfo("en");
        $("[data-test-id=login] input").setValue(newRegistrationData.getLogin());
        $("[data-test-id=password] input").setValue(registrationData.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан"), Duration.ofSeconds(5));
    }

    @Test
    void wrongPassword() {
        RegistrationData registrationData = RegistrationInfo.Registration.generateInfo("en");
        registrateUser(registrationData);
        open("http://localhost:9999");
        RegistrationData newRegistrationData = RegistrationInfo.Registration.generateInfo("en");
        $("[data-test-id=login] input").setValue(registrationData.getLogin());
        $("[data-test-id=password] input").setValue(newRegistrationData.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("Неверно указан"), Duration.ofSeconds(5));
    }

    @Test
    void userBlocked() {
        RegistrationData registrationData = RegistrationInfo.Registration.generateInfo("qwerty", "147852369", "blocked");
        registrateUser(registrationData);
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(registrationData.getLogin());
        $("[data-test-id=password] input").setValue(registrationData.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldHave(Condition.text("заблокирован"), Duration.ofSeconds(5));
    }

    @Test
    void changePassword() {
        RegistrationData registrationData = RegistrationInfo.Registration.generateInfo("en");
        registrateUser(registrationData);
        RegistrationData newRegistrationData = RegistrationInfo.Registration.generateInfo(registrationData.getLogin(), "147852369", "active");
        registrateUser(newRegistrationData);
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(newRegistrationData.getLogin());
        $("[data-test-id=password] input").setValue(newRegistrationData.getPassword());
        $("[data-test-id=action-login]").click();
        $("h2").shouldHave(Condition.text("Личный кабинет"), Duration.ofSeconds(10));
    }

    @Test
    void shouldPass() {
        RegistrationData registrationData = RegistrationInfo.Registration.generateInfo("en");
        registrateUser(registrationData);
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(registrationData.getLogin());
        $("[data-test-id=password] input").setValue(registrationData.getPassword());
        $("[data-test-id=action-login]").click();
        $("h2").shouldHave(Condition.text("Личный кабинет"), Duration.ofSeconds(10));
    }
}
