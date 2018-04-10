package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.heartbeat.HeartbeatVerticle;
import com.github.edgar615.device.gateway.log.LogVerticle;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import com.github.edgar615.util.log.Log;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
@RunWith(VertxUnitRunner.class)
public class SetF1DefendRespTest {

  private Vertx vertx;

  private EventHandler eventHandler;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    eventHandler = new EventHandler(vertx);
    vertx.deployVerticle(LogVerticle.class.getName());
  }

  @Test
  public void testUndefinedDefend(TestContext testContext) {
    EventHead head = EventHead.create("v1.event.device.up", "message")
            .addExt("__topic", "v1.event.device.up");
    Map<String, Object> data = new HashMap<>();
    data.put("defend", 3);
    Message message = Message.create("niot", ImmutableMap.of("id", "123456789", "cmd",
                                                             "setDefendF1Response", "data", data));
    Event event = Event.create(head, message);
    Async async = testContext.async();
    eventHandler.handle(MessageUtils.createMessage(event), ar -> {
      System.out.println("complete");
//      async.complete();
    });

  }

}
