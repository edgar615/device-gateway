package com.github.edgar615.device.gateway.inbound;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageUtils;
import com.github.edgar615.device.gateway.worker.EventHandler;
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
public class DeviceDeletedTransformerTest {

  private Vertx vertx;

  private EventHandler eventHandler;
  @Before
  public void setUp() {
    vertx = Vertx.vertx();
   eventHandler = new EventHandler(vertx);
  }

  @Test
  public void testDeleteDevice(TestContext testContext) {
    EventHead head = EventHead.create("v1.event.device.down", "message")
            .addExt("__topic", "v1.event.device.down");
    Message message = Message.create("device.deleted", ImmutableMap.of("id", "1234"));
    Event event = Event.create(head, message);

    vertx.eventBus().consumer(Consts.LOCAL_DEVICE_DELETE_ADDRESS, msg -> {
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
