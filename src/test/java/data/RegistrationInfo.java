package data;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.Random;

import static io.restassured.RestAssured.given;

@UtilityClass
public class RegistrationInfo {

    @UtilityClass
    public static class Registration{
        public static RegistrationData generateInfo (String locale){
            Faker faker = new Faker(new Locale(locale));
            return new RegistrationData(
                    faker.name().firstName(),
                    faker.lorem().fixedString(16),
                    faker.bool().bool() ? "active" : "blocked"
            );
        }
        public static RegistrationData generateInfo (String login, String password, String status){
            return new RegistrationData(
                    login,
                    password,
                    status
            );
        }
    }

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void registrateUser(RegistrationData registrationData) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(registrationData) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }
}
