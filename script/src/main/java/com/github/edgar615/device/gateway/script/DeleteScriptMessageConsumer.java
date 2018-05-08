package com.github.edgar615.device.gateway.script;

import com.github.edgar615.device.gateway.core.TransformerRegistry;
import com.github.edgar615.util.log.Log;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Edgar on 2018/4/26.
 *
 * @author Edgar  Date 2018/4/26
 */
public class DeleteScriptMessageConsumer implements Handler<Message<JsonObject>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeleteScriptMessageConsumer.class);

  public DeleteScriptMessageConsumer(Vertx vertx, JsonObject config) {
  }


  @Override
  public void handle(Message<JsonObject> msg) {
    JsonObject jsonObject = msg.body();
    Integer id = jsonObject.getInteger("productScriptId");
    TransformerRegistry.instance().remove(id + "");
    Log.create(LOGGER)
            .setLogType("product-script")
            .setEvent("remove")
            .addData("id", id)
            .info();
  }
}
