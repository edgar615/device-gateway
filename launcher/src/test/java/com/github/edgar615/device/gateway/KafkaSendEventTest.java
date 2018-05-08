package com.github.edgar615.device.gateway;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.util.base.Randoms;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import com.github.edgar615.util.eventbus.EventProducer;
import com.github.edgar615.util.eventbus.KafkaEventProducer;
import com.github.edgar615.util.eventbus.KafkaProducerOptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Edgar on 2017/3/22.
 *
 * @author Edgar  Date 2017/3/22
 */
public class KafkaSendEventTest {

  public static void main(String[] args) throws InterruptedException {
    KafkaProducerOptions options = new KafkaProducerOptions();
    options.setServers("120.76.158.7:9092");
    EventProducer producer = new KafkaEventProducer(options);
    for (int i = 0; i < 10; i++) {
      int rnd = Integer.parseInt(Randoms.randomNumber(2));
      EventHead head = EventHead.create("v1.event.device.up", "message")
              .addExt("from", "niot")
              .addExt("type", "up")
              .addExt("productType", "F1")
              .addExt("__topic", "v1.event.device.up");
      Map<String, Object> data = new HashMap<>();
      data.put("defend", 1);
      data.put("alarm", 1);
      data.put("time", Instant.now().getEpochSecond());
      Message message = Message.create("niot", ImmutableMap.of("id", "" + rnd, "cmd",
                                                               "alarmF1Event", "data", data));
      Event event = Event.create(head, message);
      producer.send(event);
    }
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    producer.close();

    System.out.println(producer.metrics());
  }

}
