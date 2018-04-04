package com.github.edgar615.device.gateway.worker;

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
    if ("v1.event.device.down".equals(event.head().ext("__topic"))) {
      //将有上游服务向网关发送的消息转换为内部格式
      String topic = event.head().ext("__topic");
      Message message = (Message) event.action();
      String command = message.resource();
      Map<String, Object> data = message.content();
      Map<String, Object> brokerMessage = new HashMap<>();
      brokerMessage.put("topic", topic);
      brokerMessage.put("traceId", event.head().id());
      brokerMessage.put("command", command);
      brokerMessage.put("data", data);
      brokerMessage.put("type", "down");
      return brokerMessage;
    } else if ("v1.event.device.up".equals(event.head().ext("__topic"))) {
      //将下游服务向网关发送的消息转换为内部格式
      String topic = event.head().ext("__topic");
      Message message = (Message) event.action();
      Map<String, Object> data = message.content();
      String command = (String) message.content().get("cmd");
      Map<String, Object> brokerMessage = new HashMap<>();
      brokerMessage.put("topic", topic);
      brokerMessage.put("traceId", event.head().id());
      brokerMessage.put("command", command);
      brokerMessage.put("data", data);
      brokerMessage.put("type", "up");
      //todo channel
      return brokerMessage;
    }
    return null;
  }
}
