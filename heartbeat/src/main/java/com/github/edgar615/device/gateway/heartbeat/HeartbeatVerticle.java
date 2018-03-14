package com.github.edgar615.device.gateway.heartbeat;

import com.github.edgar615.util.vertx.wheel.KeepaliveChecker;
import com.github.edgar615.util.vertx.wheel.KeepaliveOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Edgar on 2018/3/12.
 *
 * @author Edgar  Date 2018/3/12
 */
public class HeartbeatVerticle extends AbstractVerticle {

  public static final String HEARTBEAT_ADDRESS
          = "__com.github.edgar615.keepalive.heartbeat";

  public static final String TOTAL_ADDRESS
          = "__com.github.edgar615.keepalive.total";

  private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    KeepaliveOptions keepaliveOptions = new KeepaliveOptions(config());
    //监听心跳包
    KeepaliveChecker keepaliveChecker = KeepaliveChecker.create(vertx, keepaliveOptions);

    vertx.eventBus().<JsonObject>consumer(HEARTBEAT_ADDRESS, msg -> {
      JsonObject jsonObject = msg.body();
      String id = jsonObject.getString("id");
      keepaliveChecker.heartbeat(id);
      LOGGER.debug("heartbeat: {} [{}]", id, jsonObject.encode());
    });

    vertx.eventBus().<Integer>consumer(TOTAL_ADDRESS, msg -> {
      msg.reply(keepaliveChecker.size());
    });
  }

}
