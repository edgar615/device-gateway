package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.SequentialQueue;
import com.github.edgar615.device.gateway.core.SequentialQueueHelper;
import com.github.edgar615.util.base.Randoms;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Edgar on 2018/3/12.
 *
 * @author Edgar  Date 2018/3/12
 */
@RunWith(VertxUnitRunner.class)
public class WorkerVerticleTest {

  private Vertx vertx;


  private SequentialQueue queue;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    queue = SequentialQueueHelper.createShared(vertx, new JsonObject());
  }

  @Test
  public void testWorker(TestContext testContext) throws InterruptedException {
    vertx.deployVerticle(WorkerVerticle.class.getName(), new DeploymentOptions().setInstances(3));
    for (int i = 0; i < 100; i++) {
      int rnd = Integer.parseInt(Randoms.randomNumber(2));
      EventHead head = EventHead.create("v1.event.device.up", "message")
              .addExt("from", "niot")
              .addExt("__topic", "v1.event.device.up");
      Map<String, Object> data = new HashMap<>();
      data.put("defend", 1);
      data.put("alarm", 1);
      data.put("time", Instant.now().getEpochSecond());
      Message message = Message.create("niot", ImmutableMap.of("id", "" + rnd, "cmd",
                                                               "alarmF1Event", "data", data));
      Event event = Event.create(head, message);
      queue.enqueue(event);
      TimeUnit.MILLISECONDS.sleep(rnd);
    }

    Awaitility.await().until(() -> queue.size() == 0);

  }


}
