package com.github.edgar615.device.gateway.outbound;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.Transmitter;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Request;
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
public class ControlOutboundHandler implements OutboundHandler {

  @Override
  public void handle(Vertx vertx, Transmitter transmitter, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
    List<Map<String, Object>> mapList = output.stream()
            .filter(m -> MessageType.CONTROL.equals(m.get("type")))
            .collect(Collectors.toList());
    if (!transmitter.input().containsKey("channel")) {
      transmitter.error("channel required");
      completeFuture.complete();
      return;
    }
    //封装event\
    for (Map<String, Object> control : mapList) {

      String command = (String) control.get("command");
      Map<String, Object> data = (Map<String, Object>) control.get("data");
      if (data == null) {
        data = new HashMap<>();
      }
      //记录日志
      transmitter.logEvent(MessageType.CONTROL, command, data);
      //发送事件
      String channel = (String) transmitter.input().get("channel");
      EventHead head = EventHead.create(transmitter.nextTraceId(), channel, "request");
      Request request =
              Request.create("niot", "set", ImmutableMap.of("cmd", command, "data", data));
      Event event = Event.create(head, request);
      transmitter.sendEvent(event);
    }
    completeFuture.complete();
  }
}
