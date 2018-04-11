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
public class PingOutboundHandler implements OutboundHandler {

  @Override
  public void handle(Vertx vertx, Map<String, Object> input, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
    Map<String, Object> keepalive = output.stream()
            .filter(m -> MessageType.PING.equals(m.get("type")))
            .map(m -> (Map<String, Object>) m.get("data"))
            .filter(m -> m != null)
            .reduce(new HashMap<>(), (m1, m2) -> {
              m1.putAll(m2);
              return m1;
            });
    String traceId = (String) input.get("traceId");
    String deviceId = (String) input.get("deviceId");
    JsonObject jsonObject = new JsonObject()
            .put("traceId", traceId)
            .put("id", deviceId)
            .put("data", keepalive);
    vertx.eventBus().send(Consts.LOCAL_DEVICE_HEARTBEAT_ADDRESS,
                          jsonObject);

    completeFuture.complete();
  }
}
