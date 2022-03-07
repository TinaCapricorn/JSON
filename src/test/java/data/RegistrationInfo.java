package data;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.Random;

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
    }
}
