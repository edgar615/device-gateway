package com.github.edgar615.device.gateway.inbound;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.log.LogVerticle;
import com.github.edgar615.device.gateway.worker.EventHandler;
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

  private EventHandler eventHandler;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    eventHandler = new EventHandler(vertx);
    vertx.deployVerticle(LogVerticle.class.getName());
  }

  @Test
  public void testUndefinedDefend(TestContext testContext) throws IOException, ScriptException {
    EventHead head = EventHead.create("v1.event.device.up", "message")
            .addExt("__topic", "v1.event.device.up");
    Map<String, Object> data = new HashMap<>();
    data.put("defend", 3);
    Message message = Message.create("niot", ImmutableMap.of("id", "123456789", "cmd",
                                                             "setDefendF1Response", "data", data));
    Event event = Event.create(head, message);
    String scriptPath = "H:/dev/workspace/device-gateway/worker/src/test/resources/script"
                        + "/setF1DefendRespEvent.js";
    MessageTransformer transformer = compile(scriptPath);
    Map<String, Object> input = MessageUtils.createMessage(event);
    List<Map<String, Object>> output = transformer.execute(input);
    System.out.println(output);
    Assert.assertEquals(1, output.size());
  }

  @Test
  public void testTransformer(TestContext testContext) throws IOException, ScriptException {
    EventHead head = EventHead.create("v1.event.device.up", "message")
            .addExt("__topic", "v1.event.device.up");
    Map<String, Object> data = new HashMap<>();
    data.put("defend", 1);
    Message message = Message.create("niot", ImmutableMap.of("id", "123456789", "cmd",
                                                             "setDefendF1Response", "data", data));
    Event event = Event.create(head, message);
    String scriptPath = "H:/dev/workspace/device-gateway/worker/src/test/resources/script"
                        + "/setF1DefendRespEvent.js";
    MessageTransformer transformer = compile(scriptPath);
    Map<String, Object> input = MessageUtils.createMessage(event);
    List<Map<String, Object>> output = transformer.execute(input);
    System.out.println(output);
    Assert.assertEquals(2, output.size());
  }

}
