package com.github.edgar615.device.gateway.outbound;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Request;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public class ControlOutboundHandler implements OutboundHandler {

  @Override
  public void handle(Vertx vertx, Map<String, Object> input, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
    if (!input.containsKey("channel")) {
      //todo log
      completeFuture.complete();
      return;
    }

    List<Map<String, Object>> mapList = output.stream()
            .filter(m -> MessageType.CONTROL.equals(m.get("type")))
            .collect(Collectors.toList());
    //封装event\
    String traceId = (String) input.get("traceId");
    String channel = (String) input.get("channel");
    for (Map<String, Object> control : mapList) {
      String command = (String) control.get("command");
      Map<String, Object> data = (Map<String, Object>) control.get("data");
      if (data == null) {
        data = new HashMap<>();
      }
      String id = traceId + "." + UUID.randomUUID().toString();
      EventHead head = EventHead.create(id, channel, "request");

      Request request =
              Request.create("niot", "set", ImmutableMap.of("cmd", command, "data", data));
      Event event = Event.create(head, request);
      vertx.eventBus().send(Consts.LOCAL_KAFKA_PRODUCER_ADDRESS, new JsonObject(event.toMap()));
    }
    completeFuture.complete();
  }
}
