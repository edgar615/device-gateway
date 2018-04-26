package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.MessageUtils;
import com.github.edgar615.device.gateway.inbound.AbstractTransformerTest;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.TransformerRegistry;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.script.ScriptException;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
@RunWith(VertxUnitRunner.class)
public class EventHandlerTest extends AbstractTransformerTest {

  private Vertx vertx;

  private EventHandler handler;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    TransformerRegistry.instance().clear();
    handler = new EventHandler(vertx);
  }

  @Test
  public void testScript(TestContext testContext) throws IOException, ScriptException {
    EventHead head = EventHead.create("v1.event.device.up", "message")
            .addExt("type", "up")
            .addExt("__topic", "v1.event.device.up");
    Map<String, Object> data = new HashMap<>();
    data.put("defend", 1);
    Message message = Message.create("niot", ImmutableMap.of("id", "123456789", "cmd",
                                                             "setDefendF1Response", "data", data));
    Event event = Event.create(head, message);
    String scriptPath = "H:/dev/workspace/device-gateway/worker/src/test/resources/script"
                        + "/setF1DefendRespEvent.js";
    MessageTransformer transformer = compile(vertx, scriptPath);
    TransformerRegistry.instance().register("a", "LHF1", "up", "setF1DefendRespEvent",  transformer);
    Map<String, Object> input = MessageUtils.createMessage(event);
    AtomicBoolean check = new AtomicBoolean();
   handler.handle(input, ar -> {
    check.set(true);
    });
    Awaitility.await().until(()-> check.get());
  }

}
