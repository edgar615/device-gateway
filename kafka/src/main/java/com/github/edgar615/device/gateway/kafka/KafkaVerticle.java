package com.github.edgar615.device.gateway.kafka;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.eventbus.EventProducer;
import com.github.edgar615.util.eventbus.KafkaConsumerOptions;
import com.github.edgar615.util.eventbus.KafkaEventProducer;
import com.github.edgar615.util.eventbus.KafkaProducerOptions;
import com.github.edgar615.util.eventbus.vertx.KafkaVertxEventbusConsumer;
import com.github.edgar615.util.vertx.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Edgar on 2016/7/11.
 *
 * @author Edgar  Date 2016/7/11
 */
public class KafkaVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    JsonObject eventbusConfig = config().getJsonObject("eventbus");

    String eventbusGroup = eventbusConfig.getString("group", "device-msg");
    String eventbusServers = eventbusConfig.getString("servers", "localhost:9092");
    JsonArray upTopics = eventbusConfig.getJsonArray("up.topics", new JsonArray());
    JsonArray downTopics = eventbusConfig.getJsonArray("down.topics", new JsonArray());
    KafkaProducerOptions producerOptions = new KafkaProducerOptions()
            .setServers(eventbusServers);
    EventProducer eventProducer = new KafkaEventProducer(producerOptions);

    KafkaConsumerOptions consumerOptions = new KafkaConsumerOptions()
            .setGroup(eventbusGroup)
            .setWorkerPoolSize(1)//用单线程来保证消息按序读取
            .setServers(eventbusServers);
    for (int i = 0; i < upTopics.size(); i++) {
      consumerOptions.addTopic(upTopics.getString(i));
    }
    for (int i = 0; i < downTopics.size(); i++) {
      consumerOptions.addTopic(downTopics.getString(i));
    }
    JsonArray scriptTopics = eventbusConfig.getJsonArray("script.topics", new JsonArray());
    for (int i = 0; i < scriptTopics.size(); i++) {
      consumerOptions.addTopic(scriptTopics.getString(i));
    }
    KafkaVertxEventbusConsumer.create(vertx, consumerOptions);
    startFuture.complete();


    JsonObject mappingConfig = config().getJsonObject("productMapping", new JsonObject());
    //通过eventbus，将消息转发到Master处理
    new EventHandler(vertx, eventbusConfig, mappingConfig).register((t, r) -> true);

    //接收发送消息的eventbus，将消息发送到kafka
    vertx.eventBus().<JsonObject>consumer(Consts.LOCAL_KAFKA_PRODUCER_ADDRESS, msg -> {
      JsonObject jsonObject = msg.body();
      Event event = Event.fromMap(JsonUtils.toMap(jsonObject));
      eventProducer.send(event);
    });
  }

}
