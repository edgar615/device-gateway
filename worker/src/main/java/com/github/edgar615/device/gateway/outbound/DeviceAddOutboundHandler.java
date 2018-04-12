package com.github.edgar615.device.gateway.outbound;

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
public class DeviceAddOutboundHandler implements OutboundHandler {

  @Override
  public void handle(Vertx vertx, Map<String, Object> input, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
    Map<String, Object> device = output.stream()
            .filter(m -> MessageType.DEVICE_ADDED.equals(m.get("type")))
            .map(m -> (Map<String, Object>) m.get("data"))
            .filter(m -> m != null)
            .reduce(new HashMap<>(), (m1, m2) -> {
              m1.putAll(m2);
              return m1;
            });
    if (device.isEmpty()) {
      completeFuture.complete();
      return;
    }
    String deviceId = (String) input.get("deviceId");
    device.put("deviceId", deviceId);
    vertx.eventBus().send(Consts.LOCAL_DEVICE_ADD_ADDRESS,
                          new JsonObject(device), ar -> {
              if (ar.failed()) {
                completeFuture.fail(ar.cause());
                return;
              }
              completeFuture.complete();
            });
  }
}
