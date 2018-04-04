package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
@RunWith(VertxUnitRunner.class)
public class DeviceAddedTransformerTest {

  private Vertx vertx;

  private  EventHandler eventHandler;
  @Before
  public void setUp() {
    vertx = Vertx.vertx();
   eventHandler = new EventHandler(vertx);
  }

  @Test
  public void testAddDevice(TestContext testContext) {
    EventHead head = EventHead.create("v1.event.device.down", "message")
            .addExt("__topic", "v1.event.device.down");
    Message message = Message.create("device.added", ImmutableMap.of("foo", "bar"));
    Event event = Event.create(head, message);
    Async async = testContext.async();
    vertx.eventBus().consumer(Consts.LOCAL_DEVICE_ADD_ADDRESS, msg -> {
      System.out.println(msg.body());
      msg.reply(null);
    });

    eventHandler.handle(MessageUtils.createMessage(event), ar -> {
      System.out.println("complete");
      async.complete();
    });


  }

}
