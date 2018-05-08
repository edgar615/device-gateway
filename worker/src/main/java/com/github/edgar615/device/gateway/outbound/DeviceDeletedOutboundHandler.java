package com.github.edgar615.device.gateway.outbound;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.Transmitter;
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
  public void handle(Vertx vertx, Transmitter transmitter, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
    boolean needDeleted = output.stream()
            .anyMatch(m -> MessageType.DEVICE_DELETED.equals(m.get("type")));
    if (!needDeleted) {
      completeFuture.complete();
      return;
    }
    transmitter.deleteCache();
    completeFuture.complete();
  }
}
