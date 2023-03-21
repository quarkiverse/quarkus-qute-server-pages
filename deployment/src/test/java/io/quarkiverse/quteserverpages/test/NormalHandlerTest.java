package io.quarkiverse.quteserverpages.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;
import io.vertx.core.Context;

public class NormalHandlerTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest().withApplicationRoot(root -> {
        root
                .addAsResource(new StringAsset(
                        "{cdi:bean.isOnWorkerThread}"),
                        "templates/nonblocking.txt")
                .addAsResource(new StringAsset(
                        "quarkus.qsp.use-blocking-handler=false"),
                        "application.properties");
    });

    @Test
    public void testHandler() {
        given()
                .when().get("/qsp/nonblocking")
                .then()
                .statusCode(200)
                .body(containsString("false"));

    }

    @Named
    @Singleton
    public static class Bean {

        public boolean isOnWorkerThread() {
            return Context.isOnWorkerThread();
        }

    }

}
