package com.github.edgar615.device.gateway.cache;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.util.vertx.redis.RedisClientHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Edgar on 2017/9/5.
 *
 * @author Edgar  Date 2017/9/5
 */
public class RedisVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
//    Log.create(LOGGER)
//            .setEvent("redis.deploying")
//            .addData("config", config())
//            .info();
    JsonObject redisConfig = config().getJsonObject("redis", new JsonObject());
    RedisClient redisClient = RedisClientHelper.createShared(vertx, redisConfig);
    redisClient.ping(ar -> {
      if (ar.succeeded()) {
        startFuture.complete();
      } else {
        startFuture.fail(ar.cause());
      }
    });

    vertx.eventBus().<JsonObject>consumer(Consts.LOCAL_DEVICE_DELETE_ADDRESS, msg -> {
      String deviceId = msg.body().getString("deviceId");
      redisClient.del("device:" + deviceId, ar -> {
        if (ar.failed()) {
          msg.reply(ar.cause());
        } else {
          msg.reply(new JsonObject().put("result", "ok"));
        }
      });
    });
    vertx.eventBus().<JsonObject>consumer(Consts.LOCAL_DEVICE_ADD_ADDRESS, msg -> {
      JsonObject device = msg.body();
      String deviceId = device.getString("deviceId");
      redisClient.hmset("device:" + deviceId, device, ar -> {
        if (ar.failed()) {
          msg.reply(ar.cause());
        } else {
          msg.reply(new JsonObject().put("result", "ok"));
        }
      });
      //todo 设置过期时间
    });

  }
}