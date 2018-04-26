package com.github.edgar615.device.gateway.script;

import com.github.edgar615.util.vertx.jdbc.JdbcVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Edgar on 2018/4/26.
 *
 * @author Edgar  Date 2018/4/26
 */
@RunWith(VertxUnitRunner.class)
public class LoadScriptTest {

  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    JsonObject
            mySQLConfig = new JsonObject().put("host", "test.ihorn.com.cn").put
            ("username", "admin").put("password", "csst").put("database", "om_new");
    JsonObject persistentConfig = new JsonObject()
            .put("address", "database-service-address")
            .put("tables", new JsonArray().add("device_script"));
    JsonObject loadAllConfig = new JsonObject()
            .put("address", "__com.github.edgar615.util.vertx.jdbc.loadAll")
            .put("class", "com.github.edgar615.util.vertx.jdbc.LoadAllMessageConsumer")
            .put("config", mySQLConfig);
    JsonArray jsonObjectConsumer = new JsonArray().add(loadAllConfig);
    AtomicBoolean check1 = new AtomicBoolean();
    DeploymentOptions options = new DeploymentOptions()
            .setConfig(new JsonObject().put("mysql", mySQLConfig)
                               .put("persistent", persistentConfig)
                               .put("eventbusConsumer", jsonObjectConsumer));
    vertx.deployVerticle(JdbcVerticle.class, options, ar -> check1.set(true));
    Awaitility.await().until(() -> check1.get());

    JsonObject scriptEbConfig = new JsonObject()
            .put("address", "__com.github.edgar615.device.gateway.script.loadAll")
            .put("class", "com.github.edgar615.device.gateway.script.ScriptMessageConsumer")
            .put("config", mySQLConfig);
    jsonObjectConsumer = new JsonArray().add(scriptEbConfig);
    AtomicBoolean check2 = new AtomicBoolean();
    options = new DeploymentOptions().setConfig(new JsonObject()
                                                        .put("eventbusConsumer",
                                                             jsonObjectConsumer));
    vertx.deployVerticle(ScriptVerticle.class, options, ar -> check2.set(true));
    Awaitility.await().until(() -> check2.get());
  }

  @Test
  public void testListScript(TestContext testContext) {
    JsonObject data = new JsonObject()
            .put("resource", "device_script")
            .put("limit", 1);
    JsonObject jsonObject = new JsonObject()
            .put("data", data)
            .put("publishAddress", "__com.github.edgar615.device.gateway.script.loadAll");
    vertx.eventBus().send("__com.github.edgar615.util.vertx.jdbc.loadAll", jsonObject);
    Async async = testContext.async();

  }
}
