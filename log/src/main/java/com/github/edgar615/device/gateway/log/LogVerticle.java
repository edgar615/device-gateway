package com.github.edgar615.device.gateway.log;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.vertx.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public class LogVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogVerticle.class);

  @Override
  public void start() throws Exception {
    //todo 接受日志信息写日志到存储中
    vertx.eventBus().<JsonObject>consumer(Consts.LOCAL_DEVICE_LOG_ADDRESS, msg -> {
      JsonObject jsonObject = msg.body();
      System.out.println(jsonObject);
      LOGGER.info("log:{}", jsonObject);
    });
  }
}
