package com.github.edgar615.device.gateway.worker.result;

import com.github.edgar615.device.gateway.core.MessageType;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public class DeviceEventResultHandler implements TransformerResultHandler {

  private final Vertx vertx;

  public DeviceEventResultHandler(Vertx vertx) {this.vertx = vertx;}

  @Override
  public void handle(Map<String, Object> input, List<Map<String, Object>> output, Future<Void> completeFuture) {
    Map<String, Object> device = output.stream()
            .filter(m -> MessageType.REPORT.equals(m.get("type")))
            .map(m -> (Map<String, Object>) m.get("data"))
            .reduce(new HashMap<>(), (m1, m2) -> {
              m1.putAll(m2);
              return m1;
            });
    if (device.isEmpty()) {
      completeFuture.complete();
      return;
    }
    completeFuture.fail("todo");
  }
}
