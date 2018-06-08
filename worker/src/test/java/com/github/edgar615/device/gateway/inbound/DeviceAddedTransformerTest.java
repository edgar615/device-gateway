package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.core.*;
import com.github.edgar615.util.base.Randoms;
import com.google.common.collect.ImmutableMap;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
@RunWith(VertxUnitRunner.class)
public class DeviceAddedTransformerTest {

  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @Test
  public void testAddDevice(TestContext testContext) {
    Map<String, Object> brokerMessage = new HashMap<>();
    brokerMessage.put("productType", "F1");
    brokerMessage.put("topic", "local");
    brokerMessage.put("deviceIdentifier", "123456789");
    brokerMessage.put("traceId", UUID.randomUUID().toString());
    brokerMessage.put("command", InnerCommand.DEVICE_ADDED);
    brokerMessage.put("data", ImmutableMap.of("deviceIdentifier", Randoms.randomNumber(12),"encryptKey", Randoms.randomNumber(12)));
    brokerMessage.put("type", MessageType.DOWN);

    ScriptLogger logger = ScriptLogger.create();
    MessageTransformer transformer = new DeviceAddedTransformer();
    List<Map<String, Object>> output = transformer.execute(brokerMessage, logger);

    System.out.println(output);
    Assert.assertEquals(1, output.size());
    Map<String, Object> out1 = output.get(0);
    Assert.assertEquals(MessageType.INNER, out1.get("type"));
//    Map<String, Object> deviceMap = (Map<String, Object>) output.get(0).get("data");
//    Assert.assertEquals(true, deviceMap.get("isOnline"));

//    Map<String, Object> out2 = output.get(1);
//    Assert.assertEquals(MessageType.LOG, out2.get("type"));
//    Assert.assertEquals("connect", out2.get("command"));


  }

}
