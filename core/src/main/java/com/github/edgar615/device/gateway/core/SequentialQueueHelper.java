package com.github.edgar615.device.gateway.core;

import com.github.edgar615.util.event.Message;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;

import java.util.Queue;
import java.util.UUID;

public class SequentialQueueHelper {

  private static final String QUEUE_LOCAL_MAP_NAME_BASE = "__com.github.edgar615.device.queue";


  private static final String DEFAULT_QUEUE_NAME = "DEFAULT_DEVICE_SEQ_QUEUE";

  public static SequentialQueue createShared(Vertx vertx, JsonObject config) {
    return createShared(vertx, DEFAULT_QUEUE_NAME);
  }

  public static SequentialQueue createShared(Vertx vertx, String queueName) {
    synchronized (vertx) {
      LocalMap<String, SequentialQueue>
              map = vertx.sharedData().getLocalMap(QUEUE_LOCAL_MAP_NAME_BASE);

      SequentialQueue queue = map.get(queueName);

      if (queue == null) {
        queue = new SequentialQueue(event -> {
          Message message = (Message) event.action();
          return (String) message.content().getOrDefault("id", UUID.randomUUID().toString());
        });
        map.put(queueName, queue);
      }
      return queue;
    }
  }

  private static void removeFromMap(Vertx vertx, LocalMap<String, Queue> map,
                                    String poolName) {
    synchronized (vertx) {
      map.remove(poolName);
      if (map.isEmpty()) {
        map.close();
      }
    }
  }
}