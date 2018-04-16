package com.github.edgar615.device.gateway.script;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.TransformerRegistry;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Edgar on 2018/4/16.
 *
 * @author Edgar  Date 2018/4/16
 */
@RunWith(VertxUnitRunner.class)
public class ScriptListTest {
  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    vertx.deployVerticle(MySQLVerticle.class.getName());
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testList(TestContext testContext) throws IOException {
    AtomicBoolean check = new AtomicBoolean();
    vertx.eventBus().<JsonArray>send(Consts.LOCAL_SCRIPT_LIST_ADDRESS, new JsonObject(), reply -> {
      if (reply.failed()) {
        reply.cause().printStackTrace();
        testContext.fail();
        return;
      }
      System.out.println(reply.result().body());
      check.set(true);
    });
    Awaitility.await().until(() -> check.get());
//    Assert.assertEquals(1, TransformerRegistry.instance().size());
  }
}
