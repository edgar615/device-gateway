package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.DeviceBrokerMessage;
import com.github.edgar615.device.gateway.core.SequentialQueue;
import com.github.edgar615.device.gateway.core.SequentialQueueHelper;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import com.github.edgar615.util.vertx.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.Instant;
import java.util.Map;

/**
 * Created by Edgar on 2018/3/12.
 *
 * @author Edgar  Date 2018/3/12
 */
public class MasterVerticle extends AbstractVerticle {
  private static final String DEFAULT_FIRST_CONN_ADDRESS
          = "__com.github.edgar615.keepalive.firstconnected";

  private static final String DEFAULT_DISCONN_ADDRESS
          = "__com.github.edgar615.keepalive.disconnected";

  private SequentialQueue queue;

  @Override
  public void start(Future<Void> startFuture) throws Exception {

    queue = SequentialQueueHelper.createShared(vertx, new JsonObject());

    //接受设备上下线的消息，入队
    vertx.eventBus().<JsonObject>consumer(DEFAULT_FIRST_CONN_ADDRESS, msg -> {
      JsonObject jsonObject = msg.body();
      System.out.println(jsonObject);
      //包装为event
      String id = jsonObject.getString("id");
      Long time = jsonObject.getLong("time", Instant.now().getEpochSecond());
      EventHead eventHead = EventHead.create(Consts.LOCAL_DEVICE_ADDRESS, "message")
              .addExt("__topic", Consts.LOCAL_DEVICE_ADDRESS);
      Message message = Message.create("device.online", ImmutableMap.of("id", id, "time", time));
      Event event = Event.create(eventHead, message);
      try {
        queue.enqueue(event);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    //掉线
    vertx.eventBus().<JsonObject>consumer(DEFAULT_DISCONN_ADDRESS, msg -> {
      JsonObject jsonObject = msg.body();
      JsonArray ids = jsonObject.getJsonArray("ids", new JsonArray());
      Long time = jsonObject.getLong("time", Instant.now().getEpochSecond());
      for (int i = 0; i < ids.size(); i++) {
        String id = ids.getString(i);
        //包装为event
        EventHead eventHead = EventHead.create(Consts.LOCAL_DEVICE_ADDRESS, "message")
                .addExt("topic", Consts.LOCAL_DEVICE_ADDRESS);
        Message message = Message.create("device.offline", ImmutableMap.of("id", id, "time", time));
        Event event = Event.create(eventHead, message);
        try {
          queue.enqueue(event);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    //kafka的消息
    vertx.eventBus().<JsonObject>consumer(Consts.LOCAL_KAFKA_CONSUMER_ADDRESS, msg -> {
      try {
        JsonObject jsonObject = msg.body();
        Map<String, Object> map = JsonUtils.toMap(jsonObject);
        Event event = Event.fromMap(map);
        queue.enqueue(event);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
  }

}
