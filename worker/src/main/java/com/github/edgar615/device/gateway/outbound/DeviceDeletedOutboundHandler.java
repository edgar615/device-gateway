package com.github.edgar615.device.gateway.outbound;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageType;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public class DeviceDeletedOutboundHandler implements OutboundHandler {

  @Override
  public void handle(Vertx vertx, Map<String, Object> input, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
    boolean needDeleted = output.stream()
            .anyMatch(m -> MessageType.DEVICE_DELETED.equals(m.get("type")));
    if (!needDeleted) {
      completeFuture.complete();
      return;
    }
    String deviceId = (String) input.get("deviceId");
    JsonObject jsonObject = new JsonObject()
            .put("deviceId", deviceId);
    vertx.eventBus().send(Consts.LOCAL_DEVICE_DELETE_ADDRESS,
                          jsonObject, ar -> {
              if (ar.failed()) {
                completeFuture.fail(ar.cause());
                return;
              }
              completeFuture.complete();
            });
  }
}
