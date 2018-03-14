package com.github.edgar615.device.gateway.kafka;

import com.github.edgar615.util.event.Message;
import com.github.edgar615.util.eventbus.EventConsumer;
import com.github.edgar615.util.eventbus.EventHandler;
import com.github.edgar615.util.eventbus.EventProducer;
import com.github.edgar615.util.eventbus.KafkaConsumerOptions;
import com.github.edgar615.util.eventbus.KafkaEventConsumer;
import com.github.edgar615.util.eventbus.KafkaEventProducer;
import com.github.edgar615.util.eventbus.KafkaProducerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Edgar on 2016/7/11.
 *
 * @author Edgar  Date 2016/7/11
 */
public class KafkaVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    //vert.x eventbus
    io.vertx.core.eventbus.EventBus eb = vertx.eventBus();

    String eventbusGroup = config().getString("eventbus.group", "device-msg");
    String eventbusServers = config().getString("eventbus.servers", "localhost:9092");
    JsonArray topics = config().getJsonArray("eventbus.topics");
    if (topics == null) {
      topics = new JsonArray();
    }

    KafkaProducerOptions producerOptions = new KafkaProducerOptions()
            .setServers(eventbusServers);
    EventProducer eventProducer = new KafkaEventProducer(producerOptions);

    KafkaConsumerOptions consumerOptions = new KafkaConsumerOptions()
            .setGroup(eventbusGroup)
            .setWorkerPoolSize(1)
            .setServers(eventbusServers);
    for (int i = 0; i < topics.size(); i++) {
      consumerOptions.addTopic(topics.getString(i));
    }
    //黑名单
    JsonArray macBlackList = config().getJsonArray("mac.backlist", new JsonArray());
    Set<String> macs = new HashSet<>();
    for (int i = 0; i < macBlackList.size(); i++) {
      macs.add(macBlackList.getString(i));
    }
    consumerOptions.setBlackListFilter(e -> {
      if (e.head().to().startsWith("DeviceControlEvent")
          && e.action() instanceof Message) {
        Message message = (Message) e.action();
        String mac = (String) message.content().getOrDefault("id", "");
        return macs.contains(mac);
      }
      return false;
    });
    EventConsumer eventConsumer = new KafkaEventConsumer(consumerOptions);
    startFuture.complete();

    //将kafka的消息转移到vert.x的eventbus处理
    eventConsumer.consumer((t, r) -> true, event -> {
      eb.send("com.github.edgar615.device.event", new JsonObject(event.toMap()));
    });

//    eventConsumer.consumer(
//            (t, r) -> t.startsWith("DeviceControlEvent") || t.startsWith("DeviceChangeEvent"),
//            event -> eventHandler.handle(event));

//    //发送消息
//    eb.<JsonObject>consumer(Address.EB_SEND_EVENT, msg -> {
//      JsonObject jsonObject = msg.body();
//      sendEvent(eventProducer, jsonObject);
//    });
//
//    eb.<JsonObject>consumer(Address.EB_BATCH_SEND_EVENT, msg -> {
//      JsonObject jsonObject = msg.body();
//      int deviceId = jsonObject.getInteger("deviceId");
//      JsonArray eventArray = jsonObject.getJsonArray("events", new JsonArray());
//      int delay = jsonObject.getInteger("delay", 300);
//      String transcationId = jsonObject.getString("transcationId");
//      for (int i = 0; i < eventArray.size(); i++) {
//        final long index = i;
//        JsonObject eventJson = eventArray.getJsonObject((int) index);
//        if (index == 0) {
//          sendEvent(eventProducer, new JsonObject()
//                  .put("deviceId", deviceId)
//                  .put("transcationId", transcationId)
//                  .put("event", eventJson));
//        } else {
//          vertx.setTimer(index * delay, l -> {
//            sendEvent(eventProducer, new JsonObject()
//                    .put("deviceId", deviceId)
//                    .put("transcationId", transcationId)
//                    .put("event", eventJson));
//          });
//        }
//      }
//    });
  }

//  private void sendEvent(EventProducer producer, JsonObject jsonObject) {
//    JsonObject eventJson = jsonObject.getJsonObject("event");
//    Event event = Event.fromMap(JsonUtils.toMap(eventJson));
//    producer.send(event);
//  }

}
