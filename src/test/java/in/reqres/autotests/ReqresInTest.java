package in.reqres.autotests;

import in.reqres.autotests.config.UserConfig;
import in.reqres.autotests.models.SingleResource;
import in.reqres.autotests.models.SingleUser;
import io.qameta.allure.Feature;
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
import static io.qameta.allure.Allure.step;
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
        var user = step("Отправляем запрос на поиск пользователя", () ->
                given()
                        .spec(singleUserRequestSpec)
                        .pathParam("userId", userId)
                        .when()
                        .get()
                        .then()
                        .extract()
                        .body()
                        .as(SingleUser.class)
                        .getData()
        );

        step("Проверяем, что данные пользователя консистенты", () -> {
            assertThat(user.getId(), equalTo(2));
            assertThat(user.getEmail(), equalTo(userConfig.email()));
            assertThat(user.getFirstName(), equalTo(userConfig.firstName()));
            assertThat(user.getLastName(), equalTo(userConfig.lastName()));
            assertThat(user.getAvatar(), equalTo(userConfig.avatar()));
        });
    }

    @Test
    @DisplayName("User not found")
    public void singleUserNotFoundTest() {
        String userId = RandomStringUtils.random(5);
        var response = step("Отправляем запрос на поиск пользователя", () ->
                given()
                        .spec(singleUserRequestSpec)
                        .pathParam("userId", userId)
                        .get()
        );

        step("Проверяем, что пользователь не найден", () -> {
            assertThat(response.statusCode(), equalTo(404));
            assertThat(response.body().asString(), equalTo("{}"));
        });
    }

    @Test
    @DisplayName("Create user")
    public void createUserTest() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "morpheus");
        body.put("job", "leader");

        var response = step("Отправляем запрос на создание пользователя", () ->
                given()
                        .spec(createUserRequestSpec)
                        .body(body)
                        .when()
                        .post()
        );

        step("Проверяем, что пользователь создан корректно", () -> {
            assertThat(response.statusCode(), equalTo(201));
            assertThat(response.body().path("name"), equalTo("morpheus"));
            assertThat(response.body().path("job"), equalTo("leader"));
            assertThat(response.body().path("id"), not(emptyOrNullString()));
            assertThat(response.body().path("createdAt"), not(emptyOrNullString()));
        });
    }

    @Test
    @DisplayName("Get single resource")
    public void getSingleResourceTest() {
        int resourceId = 2;
        var resource = step("Отправляем запрос на получение ресурса", () ->
                given()
                        .spec(singleResourceRequestSpec)
                        .pathParam("resourceId", resourceId)
                        .when()
                        .get()
                        .then()
                        .extract()
                        .body()
                        .as(SingleResource.class)
                        .getData()
        );

        step("Проверяем, что данные консистентны", () -> {
            assertThat(resource.getId(), equalTo(2));
            assertThat(resource.getName(), equalTo("fuchsia rose"));
            assertThat(resource.getYear(), equalTo(2001));
            assertThat(resource.getColor(), equalTo("#C74375"));
            assertThat(resource.getPantoneValue(), equalTo("17-2031"));
        });
    }

    @Test
    @DisplayName("Get resources list")
    public void getResourcesListTest() {
        var response = step("Отправляем запрос на получение списка ресурсов", () ->
                given()
                        .spec(resourcesListRequestSpec)
                        .get()
        );

        step("Проверяем каждый объект, в ответе не должно быть пустых полей", () -> {
            assertThat(response.body().path("data.id"), everyItem(notNullValue()));
            assertThat(response.body().path("data.name"), everyItem(not(emptyOrNullString())));
            assertThat(response.body().path("data.year"), everyItem(notNullValue()));
            assertThat(response.body().path("data.color"), everyItem(not(emptyOrNullString())));
            assertThat(response.body().path("data.pantone_value"), everyItem(not(emptyOrNullString())));
        });
    }
}
