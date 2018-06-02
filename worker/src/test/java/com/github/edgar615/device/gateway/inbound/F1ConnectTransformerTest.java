package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.ScriptUtils;
import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.MessageUtils;
import com.github.edgar615.device.gateway.core.ScriptLogger;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.script.ScriptException;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
@RunWith(VertxUnitRunner.class)
public class F1ConnectTransformerTest extends AbstractTransformerTest {

  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @Test
  public void testTransformer(TestContext testContext) throws IOException, ScriptException {
    AtomicBoolean check = new AtomicBoolean();
    vertx.eventBus().consumer(Consts.LOCAL_DEVICE_LOG_ADDRESS, msg -> {
      System.out.println(msg.body());
      check.set(true);
    });

    EventHead head = EventHead.create("local", "message")
            .addExt("type", "connect")
            .addExt("productType", "f1")
            .addExt("__topic", "local");
    Map<String, Object> data = new HashMap<>();
    data.put("id", "123456789");
    data.put("address", "127.0.0.1");
    Message message = Message.create("connect", data);
    Event event = Event.create(head, message);

    ScriptLogger logger = ScriptLogger.create();

    String scriptPath = "H:/dev/workspace/device-gateway/worker/src/test/resources/script"
                        + "/connect.js";
    MessageTransformer transformer = ScriptUtils.compile(vertx, scriptPath);
    List<Map<String, Object>> output = transformer.execute(MessageUtils.createMessage(event), logger);
    System.out.println(output);
    Assert.assertEquals(1, output.size());
    Map<String, Object> out1 = output.get(0);
    Assert.assertEquals(MessageType.CONTROL, out1.get("type"));;

    Awaitility.await().until(() -> check.get());
  }

}
