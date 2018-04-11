package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.log.LogVerticle;
import com.github.edgar615.device.gateway.worker.EventHandler;
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
public class ConnectTransformerTest extends AbstractTransformerTest {

  private Vertx vertx;

  private EventHandler eventHandler;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    eventHandler = new EventHandler(vertx);
    vertx.deployVerticle(LogVerticle.class.getName());
  }

  @Test
  public void testTransformer(TestContext testContext) throws IOException, ScriptException {
    Map<String, Object> data = new HashMap<>();
    data.put("address", "127.0.0.1");
    MessageTransformer transformer = new ConnectTransformer();
    Map<String, Object> input = new HashMap<>();
    input.put("type", "connect");
    input.put("command", "connect");
    input.put("id", "123456789");
    input.put("data",data);
    List<Map<String, Object>> output = transformer.execute(input);
    System.out.println(output);
    Assert.assertEquals(2, output.size());
    Map<String, Object> out1 = output.get(0);
    Assert.assertEquals(MessageType.REPORT, out1.get("type"));
    Map<String, Object> deviceMap = (Map<String, Object>) output.get(0).get("data");
    Assert.assertEquals(true, deviceMap.get("isOnline"));

    Map<String, Object> out2 = output.get(1);
    Assert.assertEquals(MessageType.LOG, out2.get("type"));
    Assert.assertEquals("connect", out2.get("command"));
  }

}
