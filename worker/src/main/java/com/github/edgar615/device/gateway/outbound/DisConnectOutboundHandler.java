package com.github.edgar615.device.gateway.outbound;

import com.github.edgar615.device.gateway.core.KeepaliveCommand;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.Transmitter;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public class DisConnectOutboundHandler implements OutboundHandler {

  @Override
  public void handle(Vertx vertx, Transmitter transmitter, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
    List<Map<String, Object>> keepalives = output.stream()
            .filter(m -> MessageType.KEEPALIVE.equals(m.get("type")))
            .filter(m -> KeepaliveCommand.DIS_CONNECT.equals(m.get("command")))
            .collect(Collectors.toList());
    if (keepalives.isEmpty()) {
      completeFuture.complete();
      return;
    }
    transmitter.disConnect();
    completeFuture.complete();
  }
}
