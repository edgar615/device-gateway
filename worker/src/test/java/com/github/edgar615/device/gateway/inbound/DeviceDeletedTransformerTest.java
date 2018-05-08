package com.github.edgar615.device.gateway.inbound;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.MessageUtils;
import com.github.edgar615.device.gateway.core.ScriptLogger;
import com.github.edgar615.device.gateway.worker.EventHandler;
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

import java.util.List;
import java.util.Map;

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
            .addExt("type", "down")
            .addExt("__topic", "v1.event.device.down");
    Message message = Message.create("device.deleted", ImmutableMap.of("id", "1234"));
    Event event = Event.create(head, message);
    ScriptLogger logger = ScriptLogger.create();
    MessageTransformer transformer = new DeviceDeletedTransformer();
    List<Map<String, Object>> output =
            transformer.execute(MessageUtils.createMessage(event), logger);

    System.out.println(output);
    Assert.assertEquals(1, output.size());
    Map<String, Object> out1 = output.get(0);
    Assert.assertEquals(MessageType.DEVICE_DELETED, out1.get("type"));
//    Map<String, Object> deviceMap = (Map<String, Object>) output.get(0).get("data");
//    Assert.assertEquals(true, deviceMap.get("isOnline"));

//    Map<String, Object> out2 = output.get(1);
//    Assert.assertEquals(MessageType.LOG, out2.get("type"));
//    Assert.assertEquals("connect", out2.get("command"));


  }

}
