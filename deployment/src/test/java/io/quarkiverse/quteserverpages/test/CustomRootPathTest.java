package io.quarkiverse.quteserverpages.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;

public class CustomRootPathTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest().withApplicationRoot(root -> {
        root.addAsResource(new StringAsset(
                "Hello {name ?: 'world'}!"),
                "templates/hello.txt")
                .addAsResource(new StringAsset(
                        "Index root!"),
                        "templates/index.html")
                .addAsResource(new StringAsset(
                        "Index foo!"),
                        "templates/foo/index.html")
                .addAsResource(new StringAsset(
                        "quarkus.qsp.root-path=ping"),
                        "application.properties");
    });

    @Test
    public void testTemplates() {
        given()
                .when().get("/ping/hello")
                .then()
                .statusCode(200)
                .body(containsString("Hello world!"));
        given()
                .when().get("/ping/")
                .then()
                .statusCode(200)
                .body(containsString("Index root!"));
        given()
                .when().get("/ping/foo/")
                .then()
                .statusCode(200)
                .body(containsString("Index foo!"));
        given()
                .when().get("/ping/foo/index")
                .then()
                .statusCode(200)
                .body(containsString("Index foo!"));

    }
}
