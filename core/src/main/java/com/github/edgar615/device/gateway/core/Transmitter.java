package com.github.edgar615.device.gateway.core;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.log.Log;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Edgar on 2018/4/13.
 *
 * @author Edgar  Date 2018/4/13
 */
public class Transmitter {
  private static final Logger LOGGER = LoggerFactory.getLogger(Transmitter.class);

  private final Vertx vertx;

  private final String traceId;

  private final String productType;

  private final String deviceIdentifier;

  private final Map<String, Object> input;

  private final String channel;

  private final AtomicInteger seq = new AtomicInteger();

  private Transmitter(Vertx vertx, Map<String, Object> input) {
    this.vertx = vertx;
    this.productType = (String) input.get("productType");
    this.traceId = (String) input.get("traceId");
    this.deviceIdentifier = (String) input.get("deviceIdentifier");
    this.input = input;
    this.channel = (String) input.get("channel");
  }

  public static Transmitter create(Vertx vertx, Map<String, Object> input) {
    return new Transmitter(vertx, input);
  }

  public String traceId() {
    return traceId;
  }

  public String productType() {
    return productType;
  }

  public String deviceIdentifier() {
    return deviceIdentifier;
  }

  public Map<String, Object> input() {
    return input;
  }

  public String nextTraceId() {
    return traceId + "." + nextSeq();
  }

  public int nextSeq() {
    return seq.incrementAndGet();
  }

  public String registration() {
    return productType + ":" + deviceIdentifier;
  }

  public void keepalive(Map<String, Object> keepalive) {
    //用productType和deviceIdentifier组合成心跳的监测
    JsonObject jsonObject = new JsonObject()
            .put("traceId", traceId)
            .put("id", registration())
            .put("data", keepalive);
    vertx.eventBus().send(Consts.LOCAL_DEVICE_HEARTBEAT_ADDRESS,
                          jsonObject);
    DeviceChannelRegistry.instance().register(registration(), channel);
  }

  public void logInput(String type, String command, Map<String, Object> data) {
    Log log = Log.create(LOGGER)
            .setLogType("device-gateway")
            .setEvent("in")
            .setTraceId(traceId)
            .setMessage("[{}] [{}] [{}] [{}]")
            .addArg(productType)
            .addArg(deviceIdentifier)
            .addArg(type)
            .addArg(command);
    if (data != null && !data.isEmpty()) {
      data.forEach((k, v) -> log.addData(k, v));
    }
    log.info();
  }

  public void logOut(String type, String command, Map<String, Object> data) {
    Log log = Log.create(LOGGER)
            .setLogType("device-gateway")
            .setEvent("out")
            .setTraceId(traceId)
            .setMessage("[{}] [{}] [{}] [{}]")
            .addArg(productType)
            .addArg(deviceIdentifier)
            .addArg(type)
            .addArg(command);
    if (data != null && !data.isEmpty()) {
      data.forEach((k, v) -> log.addData(k, v));
    }
    log.info();
  }

  public void sendEvent(Event event) {
    vertx.eventBus().send(Consts.LOCAL_KAFKA_PRODUCER_ADDRESS, new JsonObject(event.toMap()));
  }

  public void updateCache(JsonObject jsonObject) {
    JsonObject device = jsonObject.copy();
    device.put("deviceIdentifier", deviceIdentifier);
    vertx.eventBus().send(Consts.LOCAL_DEVICE_ADD_ADDRESS, device, ar -> {
    });
  }

  public void deleteCache() {
    JsonObject device = new JsonObject()
            .put("deviceIdentifier", deviceIdentifier);
    vertx.eventBus().send(Consts.LOCAL_DEVICE_DELETE_ADDRESS,
                          device, ar -> {
            });
    DeviceChannelRegistry.instance().remove(registration());
  }

  public void info(String message) {
    info(message, null);
  }

  public void error(String message) {
    error(message, null);
  }

  public void info(String message, Map<String, Object> data) {
    log(message, data).info();
  }

  public void error(String message, Map<String, Object> data) {
    log(message, data).error();
  }


  private Log log(String message, Map<String, Object> data) {
    Log log = Log.create(LOGGER)
            .setLogType("device-gateway")
            .setEvent("script")
            .setTraceId(traceId)
            .setMessage("[{}] [{}] [{}]")
            .addArg(productType)
            .addArg(deviceIdentifier)
            .addArg(message);
    if (data != null && !data.isEmpty()) {
      data.forEach((k, v) -> log.addData(k, v));
    }
    return log;
  }

}
