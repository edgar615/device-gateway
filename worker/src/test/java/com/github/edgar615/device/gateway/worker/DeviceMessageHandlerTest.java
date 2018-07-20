package com.github.edgar615.device.gateway.worker;

import com.github.edgar615.device.gateway.core.MessageType;

import com.github.edgar615.device.gateway.ScriptUtils;
import com.github.edgar615.device.gateway.inbound.AbstractTransformerTest;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.TransformerRegistry;
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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.script.ScriptException;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
@RunWith(VertxUnitRunner.class)
public class DeviceMessageHandlerTest extends AbstractTransformerTest {

  private Vertx vertx;

  private DeviceMessageHandler handler;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    TransformerRegistry.instance().clear();
    handler = new DeviceMessageHandler(vertx);
  }

  @Test
  public void testScript(TestContext testContext) throws IOException, ScriptException {
    String scriptPath = "h:/dev/workspace/device-gateway/worker/src/test/resources/script"
                        + "/setF1DefendRespEvent.js";
    MessageTransformer transformer = ScriptUtils.compile(vertx, scriptPath);
    TransformerRegistry.instance().register("a", "LHF1", "up", "setF1DefendRespEvent",  transformer);
    Map<String, Object> data = new HashMap<>();
    data.put("defend", 1);
    Map<String, Object> brokerMessage = new HashMap<>();
    brokerMessage.put("productType", "F1");
    brokerMessage.put("topic", "local");
    brokerMessage.put("deviceIdentifier", "123456789");
    brokerMessage.put("traceId", UUID.randomUUID().toString());
    brokerMessage.put("command", "setDefendF1Response");
    brokerMessage.put("data", data);
    brokerMessage.put("type", MessageType.UP);

    AtomicBoolean check = new AtomicBoolean();
   handler.handle(brokerMessage, ar -> {
    check.set(true);
    });
    Awaitility.await().until(()-> check.get());
  }

}
