package com.github.edgar615.device.gateway.kafka;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.util.base.MorePreconditions;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.Message;
import com.github.edgar615.util.eventbus.vertx.VertxEventHandler;
import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/6/7.
 */
public class UpEventHandler implements VertxEventHandler {

    private final Vertx vertx;
    private final JsonArray upTopics;
    private final JsonArray downTopics;
    private final JsonArray scriptTopics;
    private List<ProductMapping> mappings;
    public UpEventHandler(Vertx vertx, JsonObject config, JsonObject mappingConfig) {
        this.vertx = vertx;
        this.upTopics = config.getJsonArray("up.topics", new JsonArray());
        this.downTopics = config.getJsonArray("down.topics", new JsonArray());
        this.scriptTopics = config.getJsonArray("script.topics", new JsonArray());
        this.mappings = mappingConfig.stream()
                .map(m -> {
                    JsonObject productConfig = (JsonObject) m.getValue();
                    MorePreconditions.checkInstanceOf(String.class, productConfig.getValue("pid"));
                    MorePreconditions.checkInstanceOf(String.class, productConfig.getValue("code"));
                    return ProductMapping.ProductMapping(m.getKey(), productConfig.getString("pid"), productConfig.getString("code"));
                }).collect(Collectors.toList());
    }

    @Override
    public void handle(Event event, Future<Void> completeFuture) {
        if (upTopics.contains(event.head().ext("__topic"))) {
            Map<String, Object> deviceMessage = createUpMessage(event);
            vertx.eventBus().send(Consts.LOCAL_EVENT_HANDLER, new JsonObject(deviceMessage), msg -> {
                completeFuture.complete();
            });
            return;
        }
        if (downTopics.contains(event.head().ext("__topic"))) {
            Map<String, Object> deviceMessage = createDownMessage(event);
            vertx.eventBus().send(Consts.LOCAL_EVENT_HANDLER, new JsonObject(deviceMessage), msg -> {
                completeFuture.complete();
            });
            return;
        }
        if (scriptTopics.contains(event.head().ext("__topic"))) {
            Message message = (Message) event.action();
            vertx.eventBus().send(Consts.LOCAL_SCRIPT_ADDRESS + "." + message.resource(),
                    new JsonObject(message.content()));
            completeFuture.complete();
            return;
        }
        completeFuture.complete();
    }

    private String checkProductType(Event event) {
        Message message = (Message) event.action();
        String pid = (String) message.content().get("pid");
        String code = (String) message.content().get("code");
        Optional<String> optional = mappings.stream()
                .filter(m -> m.pid().equals(pid))
                .filter(m -> m.code().equals(code))
                .map(m -> m.productType())
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        throw SystemException.create(DefaultErrorCode.RESOURCE_NOT_FOUND)
                .set("pid",pid)
                .set("code", code);
    }

    //将设备接入服务向网关发送的消息转换为内部格式
   private Map<String, Object> createUpMessage(Event event) {
       //根据PID、CODE映射出设备类型
       String productType = checkProductType(event);
       String topic = event.head().ext("__topic");
       Message message = (Message) event.action();
       Map<String, Object> content = message.content();
       String deviceIdentifier = (String) content.get("id");
       Objects.requireNonNull(deviceIdentifier);
       String cmd = (String) message.content().get("cmd");
       String type = MessageType.UP;
       if ("keepalive".equals(cmd)) {
           type = MessageType.KEEPALIVE;
       }
       Map<String, Object> cmdData = (Map<String, Object>) message.content().getOrDefault
               ("data", new HashMap<>());
       String command = (String) cmdData.get("cmd");
       Objects.requireNonNull(command);
       Map<String, Object> brokerMessage = new HashMap<>();
       brokerMessage.put("productType", productType);
       brokerMessage.put("topic", topic);
       brokerMessage.put("deviceIdentifier", deviceIdentifier);
       brokerMessage.put("traceId", event.head().id());
       brokerMessage.put("command", cmd);
       brokerMessage.put("data", cmdData);
       brokerMessage.put("type", type);
       brokerMessage.put("channel", event.head().ext("from"));
       return brokerMessage;
    }

    private Map<String, Object> createDownMessage(Event event) {
        //从头中取出产品类型
        String productType = event.head().ext("productType");
        Objects.requireNonNull(productType);
        String topic = event.head().ext("__topic");
        Message message = (Message) event.action();
        Map<String, Object> data = message.content();
        String command = message.resource();
        Map<String, Object> brokerMessage = new HashMap<>();
        brokerMessage.put("topic", topic);
        brokerMessage.put("traceId", event.head().id());
        String deviceIdentifier = (String) data.get("deviceIdentifier");
        Objects.requireNonNull(deviceIdentifier);
        brokerMessage.put("productType", productType);
        brokerMessage.put("deviceIdentifier", deviceIdentifier);
        brokerMessage.put("command", command);
        brokerMessage.put("data", data);
        brokerMessage.put("type", "down");
        return brokerMessage;
    }
}
