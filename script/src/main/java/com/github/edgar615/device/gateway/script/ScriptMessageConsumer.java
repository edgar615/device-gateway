package com.github.edgar615.device.gateway.script;

import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.ScriptCompiler;
import com.github.edgar615.device.gateway.core.TransformerRegistry;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by Edgar on 2018/4/26.
 *
 * @author Edgar  Date 2018/4/26
 */
public class ScriptMessageConsumer implements Handler<Message<JsonArray>> {

  public ScriptMessageConsumer(Vertx vertx, JsonObject config) {
  }

  @Override
  public void handle(Message<JsonArray> msg) {
    JsonArray jsonArray = msg.body();
    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject jsonObject = jsonArray.getJsonObject(i);
      String id = jsonObject.getString("deviceScriptId");
      String productType = jsonObject.getString("productType");
      String messageType = jsonObject.getString("messageType");
      String command = jsonObject.getString("command");
      String scriptContent = jsonObject.getString("scriptContent");
      try {
        MessageTransformer transformer = ScriptCompiler.compile(scriptContent);
        TransformerRegistry.instance().register(id, productType, messageType, command, transformer);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    System.out.println("load:" + TransformerRegistry.instance().size());
  }
}
