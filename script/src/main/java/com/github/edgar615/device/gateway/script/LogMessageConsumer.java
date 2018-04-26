package com.github.edgar615.device.gateway.script;

import com.github.edgar615.util.vertx.jdbc.PersistentService;
import com.github.edgar615.util.vertx.jdbc.dataobj.InsertData;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Edgar on 2018/4/26.
 *
 * @author Edgar  Date 2018/4/26
 */
public class LogMessageConsumer implements Handler<Message<JsonObject>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(LogMessageConsumer.class);

  private final PersistentService persistentService;

  private LogMessageConsumer(PersistentService persistentService) {
    this.persistentService = persistentService;
  }

  public static LogMessageConsumer create(PersistentService persistentService) {
    return new LogMessageConsumer(persistentService);
  }

  @Override
  public void handle(Message<JsonObject> msg) {
    JsonObject jsonObject = msg.body();
    System.out.println(jsonObject);
    InsertData insertData = new InsertData();
    insertData.setResource("deviceLog");
    insertData.setData(new JsonObject().put("logContent", jsonObject.encode()));
    persistentService.insert(insertData, ar ->{
      if (ar.failed()) {
        LOGGER.error("insert log failed", ar.cause());
      } else {
        LOGGER.debug("insert log succeed", ar.cause());
      }
    });
//    LOGGER.info("log:{}", jsonObject);
  }
}
