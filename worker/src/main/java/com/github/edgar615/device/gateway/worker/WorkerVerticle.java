package com.github.edgar615.device.gateway.worker;

import com.github.edgar615.device.gateway.core.*;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.EventHead;
import com.github.edgar615.util.event.Message;
import com.github.edgar615.util.eventbus.Helper;
import com.github.edgar615.util.log.Log;
import com.github.edgar615.util.vertx.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/3/12.
 *
 * @author Edgar  Date 2018/3/12
 */
public class WorkerVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(WorkerVerticle.class);

  private static final String DEFAULT_FIRST_CONN_ADDRESS
          = "__com.github.edgar615.keepalive.firstconnected";

  private static final String DEFAULT_DISCONN_ADDRESS
          = "__com.github.edgar615.keepalive.disconnected";

  private DeviceMessageHandler deviceMessageHandler;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    deviceMessageHandler = new DeviceMessageHandler(vertx);
    //接受设备上下线的消息，入队
    vertx.eventBus().<JsonObject>consumer(DEFAULT_FIRST_CONN_ADDRESS, msg -> {
      JsonObject jsonObject = msg.body();
      //包装为event
      String id = jsonObject.getString("id");
      List<String> idSplitter = Splitter.on(":").splitToList(id);
      String deviceIdentifier = idSplitter.get(1);
      String productType = idSplitter.get(0);
      Long time = jsonObject.getLong("time", Instant.now().getEpochSecond());
      EventHead eventHead = EventHead.create(Consts.LOCAL_DEVICE_ADDRESS, "message")
              .addExt("productType", productType)
              .addExt("type", MessageType.INNER)
              .addExt("__topic", Consts.LOCAL_DEVICE_ADDRESS);
      Message message = Message.create("device.online", ImmutableMap.of("deviceIdentifier", deviceIdentifier, "time", time));
      Event event = Event.create(eventHead, message);
      Log.create(LOGGER)
              .setLogType("device-gateway")
              .setEvent("connect")
              .setMessage("[{}] [{}] [{}]")
              .setTraceId(id)
              .addArg(id)
              .addArg(Helper.toHeadString(event))
              .addArg(Helper.toActionString(event))
              .info();
//      try {
//        queue.enqueue(event);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
    });

    //掉线
    vertx.eventBus().<JsonObject>consumer(DEFAULT_DISCONN_ADDRESS, msg -> {
      JsonObject jsonObject = msg.body();
      JsonArray ids = jsonObject.getJsonArray("ids", new JsonArray());
      Long time = jsonObject.getLong("time", Instant.now().getEpochSecond());
      for (int i = 0; i < ids.size(); i++) {
        String id = ids.getString(i);
        //包装为event
        List<String> idSplitter = Splitter.on(":").splitToList(id);
        String deviceIdentifier = idSplitter.get(1);
        String productType = idSplitter.get(0);
        EventHead eventHead = EventHead.create(Consts.LOCAL_DEVICE_ADDRESS, "message")
                .addExt("productType", productType)
                .addExt("type", MessageType.INNER)
                .addExt("__topic", Consts.LOCAL_DEVICE_ADDRESS);
        Message message = Message.create("device.offline", ImmutableMap.of("deviceIdentifier", deviceIdentifier, "time", time));
        Event event = Event.create(eventHead, message);
        Log.create(LOGGER)
                .setLogType("device-gateway")
                .setEvent("disConnect")
                .setMessage("[{}] [{}] [{}]")
                .setTraceId(id)
                .addArg(id)
                .addArg(Helper.toHeadString(event))
                .addArg(Helper.toActionString(event))
                .info();
//        try {
//          queue.enqueue(event);
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        }
      }
    });

    //kafka的消息
    vertx.eventBus().<JsonObject>consumer(Consts.LOCAL_EVENT_HANDLER, msg -> {
      try {
        JsonObject jsonObject = msg.body();
        Map<String, Object> brokerMessage = JsonUtils.toMap(jsonObject);
        if (brokerMessage == null) {
          msg.reply(new JsonObject());
        }
        deviceMessageHandler.handle(brokerMessage, ar -> {
          msg.reply(new JsonObject());
        });
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

}
