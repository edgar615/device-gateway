package com.github.edgar615.device.gateway.core;

import com.github.edgar615.util.log.Log;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Edgar on 2018/4/13.
 *
 * @author Edgar  Date 2018/4/13
 */
@Deprecated
public class DeviceLogger {
  private static final Logger LOGGER = LoggerFactory.getLogger(DeviceLogger.class);

  private final Vertx vertx;

  private final String traceId;

  private final String productType;

  private final String deviceIdentifier;

  private final String script;

  private DeviceLogger(Vertx vertx, String traceId, String productType, String deviceIdentifier,
                       String script) {
    this.vertx = vertx;
    this.traceId = traceId;
    this.deviceIdentifier = deviceIdentifier;
    this.productType = productType;
    this.script = script;
  }

  public static Builder builder() {
    return new Builder();
  }

  public void info(String message) {
    info(message, null);
  }

  public void error(String message) {
    error(message, null);
  }

  public void info(String message, JsonObject data) {
    log(message, data).info();
    pub("info", message, data);
  }

  public void error(String message, JsonObject data) {
    log(message, data).error();
    pub("error", message, data);
  }

  private void pub(String level, String message, JsonObject data) {
    //广播
    JsonObject jsonObject = new JsonObject()
            .put("traceId", traceId)
            .put("productType", productType)
            .put("script", script)
            .put("deviceIdentifier", deviceIdentifier)
            .put("level", level)
            .put("message", message);
    if (data != null && !data.isEmpty()) {
      jsonObject.put("data", data);
    }
    vertx.eventBus().send(Consts.LOCAL_DEVICE_LOG_ADDRESS, jsonObject);
  }

  private Log log(String message, JsonObject data) {
    Log log = Log.create(LOGGER)
            .setEvent(deviceIdentifier)
            .setTraceId(traceId)
            .addData("script", script)
            .setMessage(message);
    if (data != null && !data.isEmpty()) {
      log.addData("data", data);
    }
    return log;
  }

  public static class Builder {
    private Vertx vertx;

    private String traceId;

    private String productType;

    private String deviceIdentifier;

    private String script;

    Builder() {
    }

    public Builder setVertx(Vertx vertx) {
      this.vertx = vertx;
      return this;
    }

    public Builder setTraceId(String traceId) {
      this.traceId = traceId;
      return this;
    }

    public Builder setProductType(String productType) {
      this.productType = productType;
      return this;
    }

    public Builder setDeviceIdentifier(String deviceIdentifier) {
      this.deviceIdentifier = deviceIdentifier;
      return this;
    }

    public Builder setScript(String script) {
      this.script = script;
      return this;
    }

    public DeviceLogger build() {
      return new DeviceLogger(vertx, traceId, productType, deviceIdentifier, script);
    }
  }
}
