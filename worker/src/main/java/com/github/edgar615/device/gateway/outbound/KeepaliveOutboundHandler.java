package com.github.edgar615.device.gateway.outbound;

import com.github.edgar615.device.gateway.core.InnerCommand;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.Transmitter;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public class KeepaliveOutboundHandler implements OutboundHandler {

  @Override
  public void handle(Vertx vertx, Transmitter transmitter, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
      List<Map<String, Object>> keepalives = output.stream()
              .filter(m -> MessageType.INNER.equals(m.get("type")))
              .filter(m -> InnerCommand.KEEPALIVE.equals(m.get("command")))
              .collect(Collectors.toList());
      if (keepalives.isEmpty()) {
          completeFuture.complete();
          return;
      }
    Map<String, Object> keepalive = keepalives.stream()
            .map(m -> (Map<String, Object>) m.get("data"))
            .filter(m -> m != null)
            .reduce(new HashMap<>(), (m1, m2) -> {
              m1.putAll(m2);
              return m1;
            });
      //记录日志，更新心跳记录
    transmitter.logOut(MessageType.KEEPALIVE, "keepalive", keepalive);
    transmitter.keepalive(keepalive);
    completeFuture.complete();
  }
}
