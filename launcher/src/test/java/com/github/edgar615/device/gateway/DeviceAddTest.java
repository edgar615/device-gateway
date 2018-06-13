package com.github.edgar615.device.gateway;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import com.github.edgar615.util.eventbus.EventProducer;
import com.github.edgar615.util.eventbus.KafkaEventProducer;
import com.github.edgar615.util.eventbus.KafkaProducerOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/6/13.
 */
public class DeviceAddTest {
    public static void main(String[] args) throws InterruptedException {
        KafkaProducerOptions options = new KafkaProducerOptions();
        options.setServers("test.ihorn.com.cn:9092");
        EventProducer producer = new KafkaEventProducer(options);
        EventHead head = EventHead.create("v1.job.device.down", "message")
                .addExt("from", "device")
                .addExt("productType", "LH204");
        Map<String, Object> data = new HashMap<>();
        data.put("deviceIdentifier", "0123456789AB");
        data.put("encryptKey", "0000000000000000");
        Message message = Message.create("deviceAdded", data);
        Event event = Event.create(head, message);
        producer.send(event);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producer.close();

        System.out.println(producer.metrics());
    }
}
