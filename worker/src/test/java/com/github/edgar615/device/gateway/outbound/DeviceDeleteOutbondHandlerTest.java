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
public class DeviceDeleteOutbondHandlerTest {

  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }


  @Test
  public void testDelete(TestContext testContext) {
    Map<String, Object> output = new HashMap<>();
    output.put("type", MessageType.DEVICE_DELETED);
    output.put("command", "device");

    Map<String, Object> input = new HashMap<>();
    input.put("type", MessageType.DOWN);
    input.put("command", "device.changed");
    input.put("channel", "niot");
    input.put("deviceId", "12345678");
    input.put("traceId", UUID.randomUUID().toString());
    input.put("data", new HashMap<>());

    AtomicInteger check = new AtomicInteger();
    vertx.eventBus().consumer(Consts.LOCAL_DEVICE_DELETE_ADDRESS, msg -> {
      System.out.println(msg.body());
      check.incrementAndGet();
      msg.reply(null);
    });
    OutboundHandler handler = new DeviceDeletedOutboundHandler();
    Future<Void> future = Future.future();
    handler.handle(vertx, input, Lists.newArrayList(output), future);
    future.setHandler(ar -> {
      check.incrementAndGet();
    });
    Awaitility.await().until(() -> check.get() == 2);
  }
}