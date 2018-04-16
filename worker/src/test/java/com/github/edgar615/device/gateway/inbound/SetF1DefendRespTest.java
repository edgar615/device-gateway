package com.github.edgar615.device.gateway.inbound;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.ScriptLogger;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageUtils;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.ScriptException;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
@RunWith(VertxUnitRunner.class)
public class SetF1DefendRespTest extends AbstractTransformerTest {

  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @Test
  public void testUndefinedDefend(TestContext testContext) throws IOException, ScriptException {
    EventHead head = EventHead.create("v1.event.device.up", "message")
            .addExt("type", "up")
            .addExt("__topic", "v1.event.device.up");
    Map<String, Object> data = new HashMap<>();
    data.put("defend", 3);
    Message message = Message.create("niot", ImmutableMap.of("id", "123456789", "cmd",
                                                             "setDefendF1Response", "data", data));
    Event event = Event.create(head, message);
    ScriptLogger logger = new ScriptLogger(vertx, event.head().id(), "123456789");
    String scriptPath = "H:/dev/workspace/device-gateway/worker/src/test/resources/script"
                        + "/setF1DefendRespEvent.js";
    MessageTransformer transformer = compile(vertx, scriptPath);
    Map<String, Object> input = MessageUtils.createMessage(event);
    List<Map<String, Object>> output = transformer.execute(input, logger);
    System.out.println(output);
    Assert.assertEquals(0, output.size());
  }

  @Test
  public void testTransformer(TestContext testContext) throws IOException, ScriptException {
    EventHead head = EventHead.create("v1.event.device.up", "message")
            .addExt("type", "up")
            .addExt("__topic", "v1.event.device.up");
    Map<String, Object> data = new HashMap<>();
    data.put("defend", 1);
    Message message = Message.create("niot", ImmutableMap.of("id", "123456789", "cmd",
                                                             "setDefendF1Response", "data", data));
    Event event = Event.create(head, message);
    ScriptLogger logger = new ScriptLogger(vertx, event.head().id(), "123456789");
    String scriptPath = "H:/dev/workspace/device-gateway/worker/src/test/resources/script"
                        + "/setF1DefendRespEvent.js";
    MessageTransformer transformer = compile(vertx, scriptPath);
    Map<String, Object> input = MessageUtils.createMessage(event);
    List<Map<String, Object>> output = transformer.execute(input, logger);
    System.out.println(output);
    Assert.assertEquals(2, output.size());
  }

}
