package com.github.edgar615.device.gateway.outbound;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.util.base.MapUtils;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import com.github.edgar615.util.event.Request;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public class ReportOutboundHandler implements OutboundHandler {

  @Override
  public void handle(Vertx vertx, Map<String, Object> input, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
    Map<String, Object> device = output.stream()
            .filter(m -> MessageType.REPORT.equals(m.get("type")))
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
    //封装event
    String traceId = (String) input.get("traceId");
    String channel = (String) input.get("channel");
    String id = traceId + "." + UUID.randomUUID().toString();
    EventHead head = EventHead.create(id, channel, "message");
    Message message = Message.create("device.reported", device);
    Event event = Event.create(head, message);
    vertx.eventBus().send(Consts.LOCAL_KAFKA_PRODUCER_ADDRESS, new JsonObject(event.toMap()));
    completeFuture.complete();
  }
}
