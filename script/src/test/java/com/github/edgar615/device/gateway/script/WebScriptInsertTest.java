package com.github.edgar615.device.gateway.script;

import com.github.edgar615.device.gateway.core.Consts;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Edgar on 2018/4/16.
 *
 * @author Edgar  Date 2018/4/16
 */
@RunWith(VertxUnitRunner.class)
public class WebScriptInsertTest {
  private Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @Test
  public void testInsert(TestContext testContext) throws IOException {
    String scriptPath = "H:/dev/workspace/device-gateway/script/src/test/resources/script"
                        + "/alarmF1Event.js";
//    Reader scriptReader = Files.newBufferedReader(Paths.get(scriptPath));
    String script = new String(Files.readAllBytes(Paths.get(scriptPath)));
    System.out.println(script);
    JsonObject jsonObject = new JsonObject()
            .put("productType", "F1")
            .put("messageType", 2)
            .put("command", "connect")
            .put("scriptContent", script);
    request(jsonObject);
  }

  @Test
  public void testInsertConnect(TestContext testContext) throws IOException {
    String scriptPath = "H:/dev/workspace/device-gateway/script/src/test/resources/script"
                        + "/connect.js";
//    Reader scriptReader = Files.newBufferedReader(Paths.get(scriptPath));
    String script = new String(Files.readAllBytes(Paths.get(scriptPath)));
    System.out.println(script);
    JsonObject jsonObject = new JsonObject()
            .put("productType", "F1")
            .put("messageType", 6)
            .put("command", "connect")
            .put("scriptContent", script);
    request(jsonObject);
  }

  @Test
  public void testInsertDisConnect(TestContext testContext) throws IOException {
    String scriptPath = "H:/dev/workspace/device-gateway/script/src/test/resources/script"
                        + "/disConnect.js";
//    Reader scriptReader = Files.newBufferedReader(Paths.get(scriptPath));
    String script = new String(Files.readAllBytes(Paths.get(scriptPath)));
    System.out.println(script);
    JsonObject jsonObject = new JsonObject()
            .put("productType", "F1")
            .put("messageType", 7)
            .put("command", "disConnect")
            .put("scriptContent", script);
    request(jsonObject);
  }

  @Test
  public void testInsertDeviceChanged(TestContext testContext) throws IOException {
    String scriptPath = "H:/dev/workspace/device-gateway/script/src/test/resources/script"
                        + "/f1DeviceChanged.js";
//    Reader scriptReader = Files.newBufferedReader(Paths.get(scriptPath));
    String script = new String(Files.readAllBytes(Paths.get(scriptPath)));
    System.out.println(script);
    JsonObject jsonObject = new JsonObject()
            .put("productType", "F1")
            .put("messageType", 1)
            .put("command", "device.changed")
            .put("scriptContent", script);
    request(jsonObject);
  }

  @Test
  public void testF1DefendResp(TestContext testContext) throws IOException {
    String scriptPath = "H:/dev/workspace/device-gateway/script/src/test/resources/script"
                        + "/setF1DefendRespEvent.js";
//    Reader scriptReader = Files.newBufferedReader(Paths.get(scriptPath));
    String script = new String(Files.readAllBytes(Paths.get(scriptPath)));
    System.out.println(script);
    JsonObject jsonObject = new JsonObject()
            .put("productType", "F1")
            .put("messageType", 2)
            .put("command", "setF1DefendRespEvent")
            .put("scriptContent", script);
    request(jsonObject);
  }

  private void request(JsonObject jsonObject) {AtomicBoolean check = new AtomicBoolean();
    vertx.createHttpClient().post(9000, "localhost", "/product/script", resp -> {
      resp.bodyHandler(body -> {
        System.out.println(body);
        check.set(true);
      });
    }).setChunked(true)
            .putHeader("content-type", "application/json")
            .end(jsonObject.encode());
    Awaitility.await().until(() -> check.get());
  }
}
