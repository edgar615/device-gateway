package com.github.edgar615.device.gateway.script;

import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageUtils;
import com.github.edgar615.device.gateway.core.ScriptCompiler;
import com.github.edgar615.device.gateway.core.TransformerRegistry;
import com.github.edgar615.util.log.Log;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Edgar on 2018/4/26.
 *
 * @author Edgar  Date 2018/4/26
 */
public class LoadScriptMessageConsumer implements Handler<Message<JsonArray>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoadScriptMessageConsumer.class);

  public LoadScriptMessageConsumer(Vertx vertx, JsonObject config) {
  }


  @Override
  public void handle(Message<JsonArray> msg) {
    JsonArray jsonArray = msg.body();
    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject jsonObject = jsonArray.getJsonObject(i);
      Integer id = jsonObject.getInteger("productScriptId");
      String productType = jsonObject.getString("productType");
      Integer messageType = jsonObject.getInteger("messageType");
      String command = jsonObject.getString("command");
      String scriptContent = jsonObject.getString("scriptContent");
      try {
        MessageTransformer transformer = ScriptCompiler.compile(scriptContent);
        TransformerRegistry.instance()
                .register(id + "", productType, MessageUtils.messageType(messageType), command,
                          transformer);
        Log.create(LOGGER)
                .setLogType("product-script")
                .setEvent("add")
                .addData("id", id)
                .info();
      } catch (Exception e) {
        Log.create(LOGGER)
                .setLogType("product-script")
                .setEvent("add")
                .addData("id", id)
                .setThrowable(e)
                .error();
      }
    }
  }
}
