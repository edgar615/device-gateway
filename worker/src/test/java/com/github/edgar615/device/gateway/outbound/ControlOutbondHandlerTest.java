package com.github.edgar615.device.gateway.outbound;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.DeviceChannelRegistry;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.ProductMappingRegistry;
import com.github.edgar615.device.gateway.core.Transmitter;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.After;
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
public class ControlOutbondHandlerTest {

  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    DeviceChannelRegistry.instance().register("test_type:12345678", "TEST");
    ProductMappingRegistry.instance().register("test_type", "pid", "code");
  }

  @After
  public void tearDown() {
    DeviceChannelRegistry.instance().clear();
    ProductMappingRegistry.instance().clear();
  }

  @Test
  public void testNochannel(TestContext testContext) {
    Map<String, Object> output = new HashMap<>();
    output.put("type", MessageType.CONTROL);
    output.put("command", "defend");
    output.put("data", ImmutableMap.of("foo", "bar"));

    Map<String, Object> input = new HashMap<>();
    input.put("productType", "test_type");
    input.put("type", MessageType.DOWN);
    input.put("command", "device.changed");
    input.put("deviceIdentifier", UUID.randomUUID().toString());
    input.put("traceId", UUID.randomUUID().toString());
    input.put("data", new HashMap<>());

    Transmitter transmitter = Transmitter.create(vertx, input);

    AtomicInteger check = new AtomicInteger();
    vertx.eventBus().consumer(Consts.LOCAL_KAFKA_PRODUCER_ADDRESS, msg -> {
      System.out.println(msg.body());
      testContext.fail();
    });
    OutboundHandler handler = new ControlOutboundHandler();
    Future<Void> future = Future.future();
    handler.handle(vertx, transmitter, Lists.newArrayList(output), future);
    future.setHandler(ar -> {
          check.incrementAndGet();
    });
    Awaitility.await().until(() -> check.get() == 1);
  }

  @Test
  public void testOneControl(TestContext testContext) {
    Map<String, Object> output = new HashMap<>();
    output.put("type", MessageType.CONTROL);
    output.put("command", "defend");
    output.put("data", ImmutableMap.of("foo", "bar"));

    Map<String, Object> input = new HashMap<>();
    input.put("type", MessageType.DOWN);
    input.put("productType", "test_type");
    input.put("command", "device.changed");
    input.put("deviceIdentifier", "12345678");
    input.put("traceId", UUID.randomUUID().toString());
    input.put("data", new HashMap<>());
    Transmitter transmitter = Transmitter.create(vertx, input);
    AtomicInteger check = new AtomicInteger();
    vertx.eventBus().consumer(Consts.LOCAL_KAFKA_PRODUCER_ADDRESS, msg -> {
      System.out.println(msg.body());
      check.incrementAndGet();
    });
    OutboundHandler handler = new ControlOutboundHandler();
    Future<Void> future = Future.future();
    handler.handle(vertx, transmitter, Lists.newArrayList(output), future);
    future.setHandler(ar -> {
      check.incrementAndGet();
    });
    Awaitility.await().until(() -> check.get() == 2);
  }

  @Test
  public void testTwoControl(TestContext testContext) {
    Map<String, Object> output1 = new HashMap<>();
    output1.put("type", MessageType.CONTROL);
    output1.put("command", "defend");
    output1.put("data", ImmutableMap.of("foo", "bar"));

    Map<String, Object> output2 = new HashMap<>();
    output2.put("type", MessageType.CONTROL);
    output2.put("command", "defend");
    output2.put("data", ImmutableMap.of("foo", "bar"));

    Map<String, Object> input = new HashMap<>();
    input.put("type", MessageType.DOWN);
    input.put("productType", "test_type");
    input.put("command", "device.changed");
    input.put("deviceIdentifier", "12345678");
    input.put("traceId", UUID.randomUUID().toString());
    input.put("data", new HashMap<>());
    Transmitter transmitter = Transmitter.create(vertx, input);
    AtomicInteger check = new AtomicInteger();
    vertx.eventBus().consumer(Consts.LOCAL_KAFKA_PRODUCER_ADDRESS, msg -> {
      System.out.println(msg.body());
      check.incrementAndGet();
    });
    OutboundHandler handler = new ControlOutboundHandler();
    Future<Void> future = Future.future();
    handler.handle(vertx, transmitter, Lists.newArrayList(output1, output2), future);
    future.setHandler(ar -> {
      check.incrementAndGet();
    });
    Awaitility.await().until(() -> check.get() == 3);
  }
}
