package com.github.edgar615.device.gateway.heartbeat;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Edgar on 2018/3/13.
 *
 * @author Edgar  Date 2018/3/13
 */
@RunWith(VertxUnitRunner.class)
public class HeartbeatVerticleTest {

  private Vertx vertx;

  private final String heartbeatAddr = "__com.github.edgar615.keepalive.heartbeat";

  private final String totalAddr = "__com.github.edgar615.keepalive.total";

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @Test
  public void testHeartbeat(TestContext testContext) {
    Async async = testContext.async();
    JsonObject config = new JsonObject()
            .put("interval", 5);
    vertx.deployVerticle(HeartbeatVerticle.class.getName(), new DeploymentOptions().setConfig(config));
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


    vertx.eventBus().<JsonObject>consumer("__com.github.edgar615.keepalive.disconnected", msg -> {
      JsonObject jsonObject = msg.body();
     System.out.println(msg.body());
    });

    vertx.eventBus().<JsonObject>consumer("__com.github.edgar615.keepalive.firstconnected", msg -> {
      JsonObject jsonObject = msg.body();
      System.out.println(msg.body());
    });

    vertx.eventBus().send(heartbeatAddr, new JsonObject().put("id", "a"));
    vertx.eventBus().send(heartbeatAddr, new JsonObject().put("id", "b"));
    vertx.setPeriodic(3, l -> {
      vertx.eventBus().send(heartbeatAddr, new JsonObject().put("id", "a"));
    });

    vertx.eventBus().<Integer>send(totalAddr, new JsonObject(), ar -> {
      testContext.assertEquals(2, ar.result().body());
    });

    try {
      TimeUnit.SECONDS.sleep(7);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    vertx.eventBus().<Integer>send(totalAddr, new JsonObject(), ar -> {
      testContext.assertEquals(1, ar.result().body());
    });
    async.complete();
  }
}
