package com.github.edgar615.device.gateway.core;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Edgar on 2018/4/13.
 *
 * @author Edgar  Date 2018/4/13
 */
public class ScriptLogger {
  private static final Logger LOGGER = LoggerFactory.getLogger(ScriptLogger.class);

  private final Vertx vertx;

  private final String traceId;

  private final String deviceId;

  public ScriptLogger(Vertx vertx, String traceId, String deviceId) {
    this.vertx = vertx;
    this.traceId = traceId;
    this.deviceId = deviceId;
  }

//  public void debug(String traceId, String deviceId, String message) {
//    log(traceId, deviceId, message).debug();
//    pub(traceId, deviceId, "debug", message);
//  }

  public void info(String message) {
    log(message).info();
    pub("info", message);
  }
//
//  public void warn(String traceId, String deviceId, String message) {
//    log(traceId, deviceId, message).warn();
//    pub(traceId, deviceId, "warn", message);
//  }

  public void error(String message) {
    log(message).error();
    pub("error", message);
  }

  private void pub(String level, String message) {
    //广播
    JsonObject jsonObject = new JsonObject()
            .put("traceId", traceId)
            .put("deviceId", deviceId)
            .put("level", level)
            .put("message", message);
    vertx.eventBus().send(Consts.LOCAL_DEVICE_LOG_ADDRESS, jsonObject);
  }

  private Log log(String message) {
    Log log = Log.create(LOGGER)
            .setEvent(deviceId)
            .setTraceId(traceId)
            .setMessage(message);
//    if (args != null) {
//      for (Object arg : args) {
//        log.addArg(arg);
//      }
//    }
    return log;
  }
}
