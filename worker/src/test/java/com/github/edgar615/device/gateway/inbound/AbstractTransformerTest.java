package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.script.ScriptVerticle;
import com.github.edgar615.util.vertx.jdbc.JdbcVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.awaitility.Awaitility;
import org.junit.Before;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Edgar on 2018/4/11.
 *
 * @author Edgar  Date 2018/4/11
 */
public class AbstractTransformerTest {

  protected Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    JsonObject
            mySQLConfig = new JsonObject().put("host", "test.ihorn.com.cn").put
            ("username", "admin").put("password", "csst").put("database", "om_new");
    JsonObject persistentConfig = new JsonObject()
            .put("address", "database-service-address")
            .put("tables", new JsonArray().add("product_script").add("device_log"));
    DeploymentOptions options = new DeploymentOptions()
            .setConfig(new JsonObject().put("mysql", mySQLConfig)
                               .put("persistent", persistentConfig));
    AtomicBoolean check1 = new AtomicBoolean();
    vertx.deployVerticle(JdbcVerticle.class, options, ar -> check1.set(true));
    Awaitility.await().until(() -> check1.get());

    AtomicBoolean check2 = new AtomicBoolean();
    vertx.deployVerticle(ScriptVerticle.class, options, ar -> check2.set(true));
    Awaitility.await().until(() -> check2.get());

  }

}
