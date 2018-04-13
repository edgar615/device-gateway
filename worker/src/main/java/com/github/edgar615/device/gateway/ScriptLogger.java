package com.github.edgar615.device.gateway;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.Log;
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

//  private final Vertx vertx;

//  public ScriptLogger(Vertx vertx) {this.vertx = vertx;}

  public void debug(String traceId, String message, Object... args) {
    log(traceId, message, args).debug();
  }

  public void info(String traceId, String message, Object... args) {
    log(traceId, message, args).info();
  }

  public void warn(String traceId, String message, Object... args) {
    log(traceId, message, args).warn();
  }

  public void error(String traceId, String message, Object... args) {
    log(traceId, message, args).error();
  }

  private Log log(String traceId, String message, Object[] args) {
    //广播
//    JsonObject jsonObject = new JsonObject()
//            .put("traceId", traceId)
////            .put("deviceId", deviceId)
//            .put("type", command)
//            .put("data", data);
//    vertx.eventBus().send(Consts.LOCAL_DEVICE_LOG_ADDRESS, jsonObject);
    Log log = Log.create(LOGGER)
            .setEvent("script")
            .setTraceId(traceId)
            .setMessage(message);
    if (args != null) {
      for (Object arg : args) {
        log.addArg(arg);
      }
    }
    return log;
  }
}
