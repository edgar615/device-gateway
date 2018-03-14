package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.SequentialQueue;
import com.github.edgar615.util.base.Randoms;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.Message;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Edgar on 2018/3/12.
 *
 * @author Edgar  Date 2018/3/12
 */
@RunWith(VertxUnitRunner.class)
public class WorkerVerticleTest {

  private Vertx vertx;

  private String queueLocalName = "com.github.edgar615.device.queue";

  private SequentialQueue queue;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    queue = new SequentialQueue(event -> {
      Message message = (Message) event.action();
      return (String) message.content().getOrDefault("id", UUID.randomUUID().toString());
    });

    vertx.sharedData().getLocalMap(queueLocalName).put("DEFAULT_DEVICE_SEQ_QUEUE", queue);
  }

  @Test
  public void testWorker(TestContext testContext) throws InterruptedException {
    vertx.deployVerticle(WorkerVerticle.class.getName(), new DeploymentOptions().setInstances(3));
    for (int i = 0; i < 100; i ++) {
      int rnd = Integer.parseInt(Randoms.randomNumber(2));
      Event event = Event.create("test", Message.create("test", ImmutableMap.of("id", "" + rnd)));
      queue.enqueue(event);
      TimeUnit.MILLISECONDS.sleep(rnd);
    }

    Awaitility.await().until(() -> queue.size() == 0);

  }


}
