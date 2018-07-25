package com.github.edgar615.device.gateway.outbound;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.EventCommand;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.Transmitter;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.time.Instant;
import java.util.*;
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
        //新警情
        for (Map<String, Object> map : mapList) {
            String command = (String) map.get("command");
            if (EventCommand.NEW_EVENT.equalsIgnoreCase(command)) {
                sendNewEvent(vertx, transmitter, map);
            } else if (EventCommand.UPDATE_IMAGE.equalsIgnoreCase(command)) {
                sendUpdateImage(vertx, transmitter, map);
            } else if (EventCommand.UPDATE_VIDEO.equalsIgnoreCase(command)) {
                sendUpdateVideo(vertx, transmitter, map);
            } else {
                transmitter.error("Undefined event command:" + command);
            }
        }
        completeFuture.complete();
    }

    private void sendNewEvent(Vertx vertx, Transmitter transmitter, Map<String, Object> map) {
        Map<String, Object> data = new HashMap<>((Map<String, Object>) map.get("data"));
        ///todo校验格式
        data.putIfAbsent("originId", transmitter.traceId());
        data.putIfAbsent("time", Instant.now().getEpochSecond());
        data.putIfAbsent("type", 0);
        data.putIfAbsent("alarm", 1);
        data.putIfAbsent("defend", false);
        transmitter.logOut(MessageType.EVENT, EventCommand.NEW_EVENT, data);
        sendEvent(vertx, transmitter, EventCommand.NEW_EVENT, data);
    }

    private void sendUpdateImage(Vertx vertx, Transmitter transmitter, Map<String, Object> map) {
        Map<String, Object> data = new HashMap<>((Map<String, Object>) map.get("data"));
        if (!(data.get("originId") instanceof String)) {
            transmitter.error("originId(String) required");
            return;
        }

        if (!(data.get("url") instanceof List)) {
            transmitter.error("url required");
            return;
        }
        data.putIfAbsent("time", Instant.now().getEpochSecond());
        transmitter.logOut(MessageType.EVENT, EventCommand.UPDATE_IMAGE, data);
        sendEvent(vertx, transmitter, EventCommand.UPDATE_IMAGE, data);
    }

    private void sendUpdateVideo(Vertx vertx, Transmitter transmitter, Map<String, Object> map) {
        Map<String, Object> data = new HashMap<>((Map<String, Object>) map.get("data"));
        if (!(data.get("originId") instanceof String)) {
            transmitter.error("originId(String) required");
            return;
        }

        if (!(data.get("url") instanceof List)) {
            transmitter.error("url required");
            return;
        }
        data.putIfAbsent("time", Instant.now().getEpochSecond());
        transmitter.logOut(MessageType.EVENT, EventCommand.UPDATE_VIDEO, data);
        sendEvent(vertx, transmitter, EventCommand.UPDATE_VIDEO, data);
    }

    private void sendEvent(Vertx vertx, Transmitter transmitter, String resource, Map<String, Object> data) {
        EventHead head =
                EventHead.create(transmitter.nextTraceId(), "v1.event.device.event", "message")
                .addExt("productType",transmitter.productType() );
        Map<String, Object> content = new HashMap<>(data);
        content.put("deviceIdentifier", transmitter.deviceIdentifier());
        Message message = Message.create(resource, removeNull(content));
        Event event = Event.create(head, message);
        vertx.eventBus().send(Consts.LOCAL_KAFKA_PRODUCER_ADDRESS, new JsonObject(event.toMap()));
    }
}
