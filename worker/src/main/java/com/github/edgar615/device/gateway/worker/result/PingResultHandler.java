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
public class PingResultHandler implements TransformerResultHandler {

  private final Vertx vertx;

  public PingResultHandler(Vertx vertx) {this.vertx = vertx;}

  @Override
  public void handle(Map<String, Object> input, List<Map<String, Object>> output, Future<Void> completeFuture) {
    Map<String, Object> keepalive = output.stream()
            .filter(m -> MessageType.PING.equals(m.get("type")))
            .map(m -> (Map<String, Object>) m.get("data"))
            .reduce(new HashMap<>(), (m1, m2) -> {
              m1.putAll(m2);
              return m1;
            });
    if (keepalive.isEmpty()) {
      completeFuture.complete();
      return;
    }
    vertx.eventBus().send(Consts.LOCAL_DEVICE_HEARTBEAT_ADDRESS,
                          new JsonObject(keepalive));

    completeFuture.complete();
  }
}