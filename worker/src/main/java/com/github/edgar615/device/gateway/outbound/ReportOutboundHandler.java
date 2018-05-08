package com.github.edgar615.device.gateway.outbound;

import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.Transmitter;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
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
public class ReportOutboundHandler implements OutboundHandler {

  @Override
  public void handle(Vertx vertx, Transmitter transmitter, List<Map<String, Object>> output,
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
    String channel = (String) transmitter.input().get("channel");
    // todo 更新channel
    transmitter.logEvent(MessageType.REPORT, "report", device);
    EventHead head = EventHead.create(transmitter.nextTraceId(), channel, "message");
    Message message = Message.create("device.reported", device);
    Event event = Event.create(head, message);
    transmitter.sendEvent(event);
    completeFuture.complete();
  }
}
