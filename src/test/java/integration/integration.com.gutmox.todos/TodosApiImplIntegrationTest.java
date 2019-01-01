package integration.integration.com.gutmox.todos;


import com.gutmox.todos.verticles.MainVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class TodosApiImplIntegrationTest {

    private static final int PORT = 8080;

    private Vertx vertx;

    @Before
    public void before(TestContext context) {
        vertx = Vertx.vertx();
    }

    @Test
    public void some_test(TestContext context) {

        System.out.println(">>>> Integration test");

        final Async async = context.async();
        MainVerticle mainVerticle = new MainVerticle();
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setConfig(new JsonObject().put("http.port", PORT));

        vertx.deployVerticle(mainVerticle, deploymentOptions, running -> {
            System.out.println(running.succeeded());
            vertx.close(context.asyncAssertSuccess());
            async.complete();
        });
    }
}