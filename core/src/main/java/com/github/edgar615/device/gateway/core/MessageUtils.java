package com.github.edgar615.device.gateway.core;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Edgar on 2018/4/4.
 *
 * @author Edgar  Date 2018/4/4
 */
public class MessageUtils {

  public static Map<String, Object> createMessage(Event event) {
     if ("up".equals(event.head().ext("type"))) {
      //将下游服务向网关发送的消息转换为内部格式
      String topic = event.head().ext("__topic");
      Message message = (Message) event.action();
      Map<String, Object> content = message.content();
      String deviceId = (String) content.get("id");
      String command = (String) message.content().get("cmd");
      Map<String, Object> brokerMessage = new HashMap<>();
      brokerMessage.put("topic", topic);
      brokerMessage.put("deviceId", deviceId);
      brokerMessage.put("traceId", event.head().id());
      brokerMessage.put("command", command);
      brokerMessage.put("data", content.get("data"));
      brokerMessage.put("type", "up");
      //todo channel
      return brokerMessage;
    }
    String topic = event.head().ext("__topic");
    Message message = (Message) event.action();
    Map<String, Object> data = message.content();
    String command = message.resource();
    Map<String, Object> brokerMessage = new HashMap<>();
    brokerMessage.put("topic", topic);
    brokerMessage.put("traceId", event.head().id());
    String deviceId = (String) data.get("id");
    brokerMessage.put("deviceId", deviceId);
    brokerMessage.put("command", command);
    brokerMessage.put("data", data);
    brokerMessage.put("type", event.head().ext("type"));
    //todo channel
    return brokerMessage;
  }
}
