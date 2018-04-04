package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
@RunWith(VertxUnitRunner.class)
public class HeartBeatTest {

  private Vertx vertx;

  private EventHandler eventHandler;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    eventHandler = new EventHandler(vertx);
//    vertx.deployVerticle(MasterVerticle.class.getName());
//    try {
//      TimeUnit.SECONDS.sleep(3);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//    vertx.deployVerticle(WorkerVerticle.class.getName());
  }

  @Test
  public void testHeartbeat(TestContext testContext) {
    EventHead head = EventHead.create("v1.event.device.up", "message")
            .addExt("__topic", "v1.event.device.up");
    Message message = Message.create("niot", ImmutableMap.of("id", "123456789", "cmd",
                                                             "keepalive"));
    Event event = Event.create(head, message);

    vertx.eventBus().consumer(Consts.LOCAL_DEVICE_HEARTBEAT_ADDRESS, msg -> {
      System.out.println(msg.body());
      msg.reply(null);
    });
    Async async = testContext.async();
    eventHandler.handle(MessageUtils.createMessage(event), ar -> {
      System.out.println("complete");
      async.complete();
    });

  }

}
