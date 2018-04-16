package com.github.edgar615.device.gateway.inbound;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.ScriptLogger;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.MessageUtils;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.ScriptException;

/**
 * Created by Edgar on 2018/3/13.
 *
 * @author Edgar  Date 2018/3/13
 */
public class AlarmF1EventTransformerTest extends AbstractTransformerTest {


  @Test
  public void testTransformer() throws ScriptException, IOException {
    EventHead head = EventHead.create("v1.event.device.up", "message")
            .addExt("type", "up")
            .addExt("__topic", "v1.event.device.up");
    Map<String, Object> data = new HashMap<>();
    data.put("defend", 1);
    data.put("alarm", 1);
    data.put("time", Instant.now().getEpochSecond());
    Message message = Message.create("niot", ImmutableMap.of("id", "123456789", "cmd",
                                                             "alarmF1Event", "data", data));
    Event event = Event.create(head, message);
    ScriptLogger logger = new ScriptLogger(vertx, event.head().id(), "123456789");
    Map<String, Object> input = MessageUtils.createMessage(event);
    String scriptPath = "H:/dev/workspace/device-gateway/worker/src/test/resources/script"
                        + "/alarmF1Event.js";
    MessageTransformer transformer = compile(vertx, scriptPath);
    List<Map<String, Object>> output = transformer.execute(input, logger);
    System.out.println(output);
    Assert.assertEquals(1, output.size());
    Map<String, Object> out1 = output.get(0);
    Assert.assertEquals(MessageType.EVENT, out1.get("type"));
  }

  @Test
  public void testUndefinedDefend() throws ScriptException, IOException {
    EventHead head = EventHead.create("v1.event.device.up", "message")
            .addExt("type", "up")
            .addExt("__topic", "v1.event.device.up");
    Map<String, Object> data = new HashMap<>();
    data.put("defend", 6);
    data.put("alarm", 1);
    data.put("time", Instant.now().getEpochSecond());
    Message message = Message.create("niot", ImmutableMap.of("id", "123456789", "cmd",
                                                             "alarmF1Event", "data", data));
    Event event = Event.create(head, message);
    ScriptLogger logger = new ScriptLogger(vertx, event.head().id(), "123456789");
    Map<String, Object> input = MessageUtils.createMessage(event);
    String scriptPath = "H:/dev/workspace/device-gateway/worker/src/test/resources/script"
                        + "/alarmF1Event.js";
    MessageTransformer transformer = compile(vertx, scriptPath);
    List<Map<String, Object>> output = transformer.execute(input, logger);
    System.out.println(output);
    Assert.assertEquals(0, output.size());
  }

}
