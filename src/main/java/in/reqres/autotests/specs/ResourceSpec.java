package in.reqres.autotests.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static in.reqres.autotests.helpers.CustomApiListener.withCustomTemplates;

public class ResourceSpec extends BaseSpec {

    private static final String BASE_URI = getUrl();

    public static RequestSpecification singleResourceRequestSpec =
            new RequestSpecBuilder()
                    .addFilter(withCustomTemplates())
                    .setBaseUri(BASE_URI)
                    .setBasePath("api/unknown/{resourceId}")
                    .build();

    public static RequestSpecification resourcesListRequestSpec =
            new RequestSpecBuilder()
                    .addFilter(withCustomTemplates())
                    .setBaseUri(BASE_URI)
                    .setBasePath("api/unknown")
                    .build();
}
