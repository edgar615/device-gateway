package com.github.edgar615.device.gateway.outbound;

import com.github.edgar615.device.gateway.core.EventCommand;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.Transmitter;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Edgar on 2018/4/11.
 *
 * @author Edgar  Date 2018/4/11
 */
@RunWith(VertxUnitRunner.class)
public class EventOutbondHandlerTest {

  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }


  @Test
  public void testThreeEvent(TestContext testContext) {
    Map<String, Object> output1 = new HashMap<>();
    output1.put("type", MessageType.EVENT);
    output1.put("command", EventCommand.NEW_EVENT);
    output1.put("data", ImmutableMap.of("foo", "bar"));

    Map<String, Object> output2 = new HashMap<>();
    output2.put("type", MessageType.EVENT);
    output2.put("command", EventCommand.UPDATE_IMAGE);
    output2.put("data", ImmutableMap.of("originId", "bar", "url", Lists.newArrayList("url")));


    Map<String, Object> output3 = new HashMap<>();
    output3.put("type", MessageType.EVENT);
    output3.put("command", EventCommand.UPDATE_VIDEO);
    output3.put("data", ImmutableMap.of("originId", "bar", "url", Lists.newArrayList("url")));

    Map<String, Object> input = new HashMap<>();
    input.put("type", MessageType.UP);
    input.put("command", EventCommand.UPDATE_VIDEO);
    input.put("channel", "niot");
    input.put("deviceIdentifier", "12345678");
    input.put("traceId", UUID.randomUUID().toString());
    input.put("productType", "LH010");
    input.put("data", new HashMap<>());
    Transmitter transmitter = Transmitter.create(vertx, input);
    AtomicInteger check = new AtomicInteger();
    vertx.eventBus().consumer(Consts.LOCAL_KAFKA_PRODUCER_ADDRESS, msg -> {
      System.out.println(msg.body());
      check.incrementAndGet();
    });
    OutboundHandler handler = new EventOutboundHandler();
    Future<Void> future = Future.future();
    handler.handle(vertx, transmitter, Lists.newArrayList(output1, output2, output3), future);
    future.setHandler(ar -> {
      check.incrementAndGet();
    });
    Awaitility.await().until(() -> check.get() == 4);
  }
}
