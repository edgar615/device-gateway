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
    options.setServers("test.ihorn.com.cn:9092");
    EventProducer producer = new KafkaEventProducer(options);
    while (true) {
      EventHead head = EventHead.create("v1.event.device.up.test", "message")
              .addExt("from", "device")
              .addExt("productType", "LH204");
      Map<String, Object> content = new HashMap<>();
      content.put("id", "0123456789AB");
      content.put("code", "horn8006m");
      content.put("pid", "horn8006");
      content.put("cmd", "keepalive");
      Map<String, Object> data = new HashMap<>();
      data.put("cmd", "keepalive");
      content.put("data", data);
      Message message = Message.create("keepalive", content);
      Event event = Event.create(head, message);
      producer.send(event);

      try {
        TimeUnit.SECONDS.sleep(15);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
//      producer.close();

      System.out.println(producer.metrics());
    }

  }

}
