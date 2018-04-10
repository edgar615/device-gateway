package com.github.edgar615.device.gateway.worker.result;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageType;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public class DeviceLogResultHandler implements TransformerResultHandler {

  private final Vertx vertx;

  public DeviceLogResultHandler(Vertx vertx) {this.vertx = vertx;}

  @Override
  public void handle(Map<String, Object> input, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
    Map<String, Object> device = output.stream()
            .filter(m -> MessageType.LOG.equals(m.get("type")))
            .map(m -> (Map<String, Object>) m.get("data"))
            .reduce(new HashMap<>(), (m1, m2) -> {
              m1.putAll(m2);
              return m1;
            });
    if (device.isEmpty()) {
      completeFuture.complete();
      return;
    }
    //不关心日志写成功与否
    //todo 日志需要记录traceId, 设备ID，类型，时间
    String traceId = (String) input.get("traceId");
    String deviceId = (String) input.get("deviceId");
    JsonObject jsonObject = new JsonObject()
            .put("traceId", traceId)
            .put("deviceId", deviceId)
            .put("data", device);
    vertx.eventBus().send(Consts.LOCAL_DEVICE_LOG_ADDRESS,
                          jsonObject);
    completeFuture.complete();
  }
}
