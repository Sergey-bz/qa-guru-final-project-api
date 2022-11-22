package in.reqres.autotests.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static in.reqres.autotests.helpers.CustomApiListener.withCustomTemplates;

public class UserSpecs {

    private static final String BASE_URI = "https://reqres.in/";

    public static RequestSpecification singleUserRequestSpec =
            new RequestSpecBuilder()
                    .addFilter(withCustomTemplates())
                    .setBaseUri(BASE_URI)
                    .setBasePath("api/users/{userId}")
                    .build();

    public static RequestSpecification createUserRequestSpec =
            new RequestSpecBuilder()
                    .addFilter(withCustomTemplates())
                    .setBaseUri(BASE_URI)
                    .setBasePath("api/users")
                    .setContentType(ContentType.JSON)
                    .build();
}
