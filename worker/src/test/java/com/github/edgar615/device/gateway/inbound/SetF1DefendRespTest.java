package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.core.*;
import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.ScriptUtils;
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
import java.util.UUID;
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
    Map<String, Object> data = new HashMap<>();
    data.put("defend", 3);
    Map<String, Object> brokerMessage = new HashMap<>();
    brokerMessage.put("productType", "F1");
    brokerMessage.put("topic", "local");
    brokerMessage.put("deviceIdentifier", "123456789");
    brokerMessage.put("traceId", UUID.randomUUID().toString());
    brokerMessage.put("command", "setDefendF1Response");
    brokerMessage.put("data", data);
    brokerMessage.put("type", MessageType.UP);

    ScriptLogger logger = ScriptLogger.create();
    String scriptPath = "e:/iotp/device-gateway/worker/src/test/resources/script"
                        + "/setF1DefendRespEvent.js";
    MessageTransformer transformer = ScriptUtils.compile(vertx, scriptPath);
    List<Map<String, Object>> output = transformer.execute(brokerMessage, logger);
    System.out.println(output);
    Assert.assertEquals(0, output.size());
  }

  @Test
  public void testTransformer(TestContext testContext) throws IOException, ScriptException {
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
    ScriptLogger logger = ScriptLogger.create();
    String scriptPath = "e:/iotp/device-gateway/worker/src/test/resources/script"
                        + "/setF1DefendRespEvent.js";
    MessageTransformer transformer = ScriptUtils.compile(vertx, scriptPath);
    List<Map<String, Object>> output = transformer.execute(brokerMessage, logger);
    System.out.println(output);
    Assert.assertEquals(2, output.size());
  }

}
