package in.reqres.autotests;

import in.reqres.autotests.config.UserConfig;
import in.reqres.autotests.models.ResourceData;
import in.reqres.autotests.models.SingleResource;
import in.reqres.autotests.models.SingleUser;
import in.reqres.autotests.models.UserData;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static in.reqres.autotests.specs.ResourceSpec.resourcesListRequestSpec;
import static in.reqres.autotests.specs.ResourceSpec.singleResourceRequestSpec;
import static in.reqres.autotests.specs.UserSpecs.createUserRequestSpec;
import static in.reqres.autotests.specs.UserSpecs.singleUserRequestSpec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Feature("ReqresIn")
public class ReqresInTest {

    private final UserConfig userConfig = ConfigFactory.create(UserConfig.class);

    @Test
    @DisplayName("Get single user")
    public void getSingleUserTest() {
        int userId = 2;
        UserData user = given()
                .spec(singleUserRequestSpec)
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .extract()
                .body()
                .as(SingleUser.class)
                .getData();

        assertThat(user.getId(), equalTo(2));
        assertThat(user.getEmail(), equalTo(userConfig.email()));
        assertThat(user.getFirstName(), equalTo(userConfig.firstName()));
        assertThat(user.getLastName(), equalTo(userConfig.lastName()));
        assertThat(user.getAvatar(), equalTo(userConfig.avatar()));
    }

    @Test
    @DisplayName("User not found")
    public void singleUserNotFoundTest() {
        String userId = RandomStringUtils.random(5);
        Response response = given()
                .spec(singleUserRequestSpec)
                .pathParam("userId", userId)
                .get();

        assertThat(response.statusCode(), equalTo(404));
        assertThat(response.body().asString(), equalTo("{}"));
    }

    @Test
    @DisplayName("Create user")
    public void createUserTest() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "morpheus");
        body.put("job", "leader");

        Response response = given()
                .spec(createUserRequestSpec)
                .body(body)
                .when()
                .post();

        assertThat(response.statusCode(), equalTo(201));
        assertThat(response.body().path("name"), equalTo("morpheus"));
        assertThat(response.body().path("job"), equalTo("leader"));
        assertThat(response.body().path("id"), not(emptyOrNullString()));
        assertThat(response.body().path("createdAt"), not(emptyOrNullString()));
    }

    @Test
    @DisplayName("Get single resource")
    public void getSingleResourceTest() {
        int resourceId = 2;
        ResourceData resource = given()
                .spec(singleResourceRequestSpec)
                .pathParam("resourceId", resourceId)
                .when()
                .get()
                .then()
                .extract()
                .body()
                .as(SingleResource.class)
                .getData();

        assertThat(resource.getId(), equalTo(2));
        assertThat(resource.getName(), equalTo("fuchsia rose"));
        assertThat(resource.getYear(), equalTo(2001));
        assertThat(resource.getColor(), equalTo("#C74375"));
        assertThat(resource.getPantoneValue(), equalTo("17-2031"));
    }

    @Test
    @DisplayName("Get resources list")
    public void getResourcesListTest() {
        Response response = given()
                .spec(resourcesListRequestSpec)
                .get();

        assertThat(response.body().path("data.id"), everyItem(notNullValue()));
        assertThat(response.body().path("data.name"), everyItem(not(emptyOrNullString())));
        assertThat(response.body().path("data.year"), everyItem(notNullValue()));
        assertThat(response.body().path("data.color"), everyItem(not(emptyOrNullString())));
        assertThat(response.body().path("data.pantone_value"), everyItem(not(emptyOrNullString())));
    }
}
