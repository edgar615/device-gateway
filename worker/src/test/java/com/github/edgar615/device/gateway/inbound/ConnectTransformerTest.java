package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.core.*;
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
public class ConnectTransformerTest extends AbstractTransformerTest {

  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @Test
  public void testTransformer(TestContext testContext) throws IOException, ScriptException {
    Map<String, Object> data = new HashMap<>();
    data.put("id", "123456789");
    data.put("address", "127.0.0.1");
    Map<String, Object> brokerMessage = new HashMap<>();
    brokerMessage.put("productType", "F1");
    brokerMessage.put("topic", "local");
    brokerMessage.put("deviceIdentifier", "123456789");
    brokerMessage.put("traceId", UUID.randomUUID().toString());
    brokerMessage.put("command", KeepaliveCommand.CONNECT);
    brokerMessage.put("data", data);
    brokerMessage.put("type", MessageType.INNER);
    brokerMessage.put("channel", "from_channel");

    ScriptLogger logger = ScriptLogger.create();
    MessageTransformer transformer = new ConnectTransformer();
    List<Map<String, Object>> output = transformer.execute(brokerMessage, logger);
    System.out.println(output);
    Assert.assertEquals(2, output.size());
    Map<String, Object> out1 = output.get(0);
    Assert.assertEquals(MessageType.REPORT, out1.get("type"));
    Assert.assertEquals(ReportCommand.DEVICE_CONN, out1.get("command"));
    Map<String, Object> out2 = output.get(1);
    Assert.assertEquals(MessageType.EVENT, out2.get("type"));
    Assert.assertEquals(EventCommand.NEW_EVENT, out2.get("command"));
  }

}
