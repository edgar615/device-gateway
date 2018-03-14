package com.github.edgar615.device.gateway.core;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Edgar on 2018/3/13.
 *
 * @author Edgar  Date 2018/3/13
 */
public class TransformerTest {


  @Test
  public void testKeepalive() {
    EventHead eventHead = EventHead.create("DeviceControlEvent_4", "message");
    Message message = Message.create("niot", ImmutableMap.of("id", "000000000000", "cmd",
                                                             "keepalive"));
    Event event = Event.create(eventHead, message);
  }

  @Test
  public void testHeartBeat() throws ScriptException, IOException {
    EventHead eventHead = EventHead.create("DeviceControlEvent_4", "message").addExt("from",
                                                                                     "eaere");
    Message message = Message.create("niot", ImmutableMap.of("id", "000000000000", "cmd",
                                                             "keepalive", "address", "13434353"));
    Event event = Event.create(eventHead, message);
    Map<String, Object> input = event.toMap();

    String scriptPath = "H:/dev/workspace/device-gateway/core/src/test/resources/script/keepalive.js";
    MessageTransformer transformer = compile(scriptPath);

    System.out.println(transformer.heartbeat(input));
  }

  @Test
  public void testDeviceControl() throws ScriptException, IOException {
    EventHead eventHead = EventHead.create("DeviceControlEvent_4", "message").addExt("from",
                                                                                     "eaere");
    Message message = Message.create("niot", ImmutableMap.of("id", "000000000000", "cmd",
                                                             "keepalive", "defendState", 1,
                                                             "sirenDuration", 20));
    Event event = Event.create(eventHead, message);
    Map<String, Object> input = event.toMap();

    String scriptPath = "H:/dev/workspace/device-gateway/core/src/test/resources/script/keepalive.js";
    MessageTransformer transformer = compile(scriptPath);

    System.out.println(transformer.controlPrimaryDevice(input));
  }

  private MessageTransformer compile(String scriptPath) throws IOException, ScriptException {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("JavaScript");

    if (!(engine instanceof Invocable)) {
      System.out.println("Interface implementation in script"
                         + "is not supported.");
      return null;
    }
    if (!(engine instanceof Compilable)) {
      System.out.println("Script compilation not supported.");
      return null;
    }

    Reader scriptReader = Files.newBufferedReader(Paths.get(scriptPath));

    Compilable comp = (Compilable) engine;
    CompiledScript cScript = comp.compile(scriptReader);
    Invocable inv = (Invocable) engine;
    cScript.eval();
    MessageTransformer transformer = inv.getInterface(MessageTransformer.class);
    if (transformer == null) {
      System.err.println("MessageTransformer interface "
                         + "implementation not found.");
      return null;
    }
    return transformer;
  }
}
