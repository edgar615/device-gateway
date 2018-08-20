package com.github.edgar615.device.gateway.worker;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.KeepaliveCommand;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.util.vertx.JsonUtils;
import com.google.common.base.Splitter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	startFuture.complete();
  }

}
