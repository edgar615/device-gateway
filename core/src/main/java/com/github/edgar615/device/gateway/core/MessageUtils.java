package com.github.edgar615.device.gateway.core;

import com.google.common.base.Strings;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.Message;
import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Edgar on 2018/4/4.
 *
 * @author Edgar  Date 2018/4/4
 */
public class MessageUtils {

  public static String messageType(int type) {
    if (type == 1) {
      return MessageType.DOWN;
    }
    if (type == 2) {
      return MessageType.UP;
    }
    if (type == 3) {
      return MessageType.CONTROL;
    }
    if (type == 4) {
      return MessageType.REPORT;
    }
    if (type == 5) {
      return MessageType.EVENT;
    }
    if (type == 6) {
      return MessageType.INNER;
    }
    return "unkown";
  }

//  public static Map<String, Object> createMessage(Event event) {
//    String productType = event.head().ext("productType");
//    Objects.requireNonNull(productType);
//    if ("up".equals(event.head().ext("type"))) {
//      //将下游服务向网关发送的消息转换为内部格式
//      String topic = event.head().ext("__topic");
//      Message message = (Message) event.action();
//      Map<String, Object> content = message.content();
//      String deviceIdentifier = (String) content.get("id");
//      String command = (String) message.content().get("cmd");
//      Objects.requireNonNull(deviceIdentifier);
//      Map<String, Object> brokerMessage = new HashMap<>();
//      brokerMessage.put("productType", productType);
//      brokerMessage.put("topic", topic);
//      brokerMessage.put("deviceIdentifier", deviceIdentifier);
//      brokerMessage.put("traceId", event.head().id());
//      brokerMessage.put("command", command);
//      brokerMessage.put("data", content.get("data"));
//      brokerMessage.put("type", "up");
//      brokerMessage.put("channel", event.head().ext("from"));
//      return brokerMessage;
//    }
//    String topic = event.head().ext("__topic");
//    Message message = (Message) event.action();
//    Map<String, Object> data = message.content();
//    String command = message.resource();
//    Map<String, Object> brokerMessage = new HashMap<>();
//    brokerMessage.put("topic", topic);
//    brokerMessage.put("traceId", event.head().id());
//    String deviceIdentifier = (String) data.get("deviceIdentifier");
//    Objects.requireNonNull(deviceIdentifier);
//    brokerMessage.put("productType", productType);
//    brokerMessage.put("deviceIdentifier", deviceIdentifier);
//    brokerMessage.put("command", command);
//    brokerMessage.put("data", data);
//    brokerMessage.put("type", event.head().ext("type"));
//    return brokerMessage;
//  }
}
