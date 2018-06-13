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
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public class ReportOutboundHandler implements OutboundHandler {

  @Override
  public void handle(Vertx vertx, Transmitter transmitter, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
    List<Map<String, Object>> reportList = output.stream()
            .filter(m -> MessageType.REPORT.equals(m.get("type")))
            .collect(Collectors.toList());
    if (reportList.isEmpty()) {
      completeFuture.complete();
      return;
    }

    Map<String, List<Map<String, Object>>> grouping =
            reportList.stream()
                    .filter(m -> m.get("command") instanceof String)
                    .collect(Collectors.groupingBy(o -> (String) o.get("command")));
    for (Map.Entry<String, List<Map<String, Object>>> entry : grouping.entrySet()) {
      Map<String, Object> content = entry.getValue().stream()
              .map(m -> (Map<String, Object>) m.get("data"))
              .filter(m -> m != null)
              .reduce(new HashMap<>(), (m1, m2) -> {
                m1.putAll(m2);
                return m1;
              });
      transmitter.logOut(MessageType.REPORT, entry.getKey(), content);
      content.put("deviceIdentifier", transmitter.deviceIdentifier());
      EventHead head = EventHead.create(transmitter.nextTraceId(),
              "v1.event.device.report", "message")
              .addExt("productType", transmitter.productType());
      Message message = Message.create(entry.getKey(), removeNull(content));
      Event event = Event.create(head, message);
      transmitter.sendEvent(event);

    }

    completeFuture.complete();
  }
}
