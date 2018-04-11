package com.github.edgar615.device.gateway.outbound;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageType;
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
public class LogOutbondHandlerTest {

  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }


  @Test
  public void testTwoLog(TestContext testContext) {
    Map<String, Object> output1 = new HashMap<>();
    output1.put("type", MessageType.LOG);
    output1.put("command", "error");
    output1.put("data", ImmutableMap.of("foo", "bar"));

    Map<String, Object> output2 = new HashMap<>();
    output2.put("type", MessageType.LOG);
    output2.put("command", "error");
    output2.put("data", ImmutableMap.of("foo", "bar"));

    Map<String, Object> input = new HashMap<>();
    input.put("type", MessageType.UP);
    input.put("command", "updateVideo");
    input.put("channel", "niot");
    input.put("deviceId", "12345678");
    input.put("traceId", UUID.randomUUID().toString());
    input.put("data", new HashMap<>());

    AtomicInteger check = new AtomicInteger();
    vertx.eventBus().consumer(Consts.LOCAL_DEVICE_LOG_ADDRESS, msg -> {
      System.out.println(msg.body());
      check.incrementAndGet();
    });
    OutboundHandler handler = new LogOutboundHandler();
    Future<Void> future = Future.future();
    handler.handle(vertx, input, Lists.newArrayList(output1, output2), future);
    future.setHandler(ar -> {
      check.incrementAndGet();
    });
    Awaitility.await().until(() -> check.get() == 3);
  }
}
