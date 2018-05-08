package com.github.edgar615.device.gateway.outbound;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.Transmitter;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 事件的处理类.
 * 事件的command分为几种：
 * newEvent 新事件，一个新事件包括下列属性：
 * originId：事件的原始ID，用于更新事件的信息
 * type: 事件类型 int 默认0
 * time:事件时间  unix 时间戳
 * alarm: 报警标志位 1-普通事件 2-提醒 3-报警, 默认1
 * defend: bool是否布撤防事件，默认否
 * images: 图片地址的字符串数组，如果事件有图片，将图片地址放入images
 * videos: 视频地址的字符串数组，如果事件有视频，将图片地址放入images
 * <p>
 * updateImage 给事件的追加图片，包括下列属性
 * originId：事件的原始ID
 * images: 图片地址的字符串数组
 * <p>
 * updateVideo 给事件的追加图片，包括下列属性
 * originId：事件的原始ID
 * videos: 视频地址的字符串数组
 *
 * @author Edgar  Date 2018/4/10
 */
public class EventOutboundHandler implements OutboundHandler {

  @Override
  public void handle(Vertx vertx, Transmitter transmitter, List<Map<String, Object>> output,
                     Future<Void> completeFuture) {
    List<Map<String, Object>> mapList = output.stream()
            .filter(m -> MessageType.EVENT.equals(m.get("type")))
            .filter(m -> m.get("data") != null)
            .collect(Collectors.toList());
    if (mapList.isEmpty()) {
      completeFuture.complete();
      return;
    }
    for (Map<String, Object> map : mapList) {
      String command = (String) map.get("command");
      Map<String, Object> data = new HashMap<>((Map<String, Object>) map.get("data"));
      if ("newEvent".equalsIgnoreCase(command)) {
        data.putIfAbsent("originId", UUID.randomUUID().toString());
        data.putIfAbsent("time", Instant.now().getEpochSecond());
        data.putIfAbsent("type", 0);
        data.putIfAbsent("alarm", 1);
        data.putIfAbsent("defend", false);
      }

      transmitter.logEvent(MessageType.EVENT, command, data);

      EventHead head =
              EventHead.create(transmitter.nextTraceId(), "v1.event.device.event", "message");
      Message message = Message.create("newEvent", (Map<String, Object>) map.get("data"));
      Event event = Event.create(head, message);
      vertx.eventBus().send(Consts.LOCAL_KAFKA_PRODUCER_ADDRESS, new JsonObject(event.toMap()));
    }
    completeFuture.complete();
  }

//  private void check(Map<String, Object> output) {
//    if (!(output.get("command") instanceof String)) {
//      throw SystemException.create(DefaultErrorCode.UNKOWN)
//              .setDetails("outbound: command[String] required");
//    }
//    if (!(output.get("data") instanceof Map)) {
//      throw SystemException.create(DefaultErrorCode.UNKOWN)
//              .setDetails("outbound: data[Map] required");
//    }
//    String command = (String) output.get("command");
//    Map<String, Object> data = (Map<String, Object>) output.get("data");
//    if ("new".equalsIgnoreCase(command)) {
//      if (!(data.get("type") instanceof Integer)) {
//        throw SystemException.create(DefaultErrorCode.UNKOWN)
//                .setDetails("outbound: type[int] required");
//      }
//      if (!(data.get("time") instanceof Long)) {
//        throw SystemException.create(DefaultErrorCode.UNKOWN)
//                .setDetails("outbound: time[long] required");
//      }
//    }
//  }
}
