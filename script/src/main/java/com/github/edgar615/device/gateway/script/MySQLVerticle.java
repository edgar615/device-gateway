package com.github.edgar615.device.gateway.script;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.ScriptCompiler;
import com.github.edgar615.device.gateway.core.TransformerRegistry;
import com.github.edgar615.util.search.Example;
import com.github.edgar615.util.vertx.jdbc.JdbcUtils;
import com.github.edgar615.util.vertx.jdbc.VertxJdbc;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/4/16.
 *
 * @author Edgar  Date 2018/4/16
 */
public class MySQLVerticle extends AbstractVerticle {

  private AsyncSQLClient sqlClient;

  @Override
  public void start() throws Exception {
    //todo 赶工写的，后期优化
    createSqlClient();
    insertConsumer();
    listConsumer();
  }

  private void insertConsumer() {
    vertx.eventBus().<JsonObject>consumer(Consts.LOCAL_SCRIPT_ADD_ADDRESS, msg -> {
      JsonObject jsonObject = msg.body();
      DeviceScript deviceScript = ScriptUtils.fromJson(jsonObject);
      String registration = UUID.randomUUID().toString();
      deviceScript.setDeviceScriptId(registration);
      jsonObject.put("registration", registration);
      //编译content
      MessageTransformer transformer;
      try {
        transformer = ScriptCompiler.compile(deviceScript.getScriptContent());
      } catch (Exception e) {
        //todo 错误
        msg.fail(-1, e.getMessage());
        return;
      }
      Future<Void> competeFuture = Future.future();
      competeFuture.setHandler(ar -> {
        if (ar.succeeded()) {
          TransformerRegistry.instance().register(registration, deviceScript.getProductType(),
                                                  deviceScript.getMessageType(),
                                                  deviceScript.getCommand(),
                                                  transformer);
          msg.reply(new JsonObject().put("registration", registration));
        } else {
          msg.fail(-1, ar.cause().getMessage());
        }
      });
      insertDb(deviceScript, competeFuture);
    });
  }

  private void listConsumer() {
    vertx.eventBus().<JsonObject>consumer(Consts.LOCAL_SCRIPT_LIST_ADDRESS, msg -> {
      Future<List<DeviceScript>> competeFuture = Future.future();
      competeFuture.setHandler(ar -> {
        if (ar.succeeded()) {
          List<JsonObject> result = ar.result()
                  .stream()
                  .map(p -> new JsonObject(p.toMap()))
                  .collect(Collectors.toList());
          msg.reply(new JsonArray(result));
        } else {
          msg.fail(-1, ar.cause().getMessage());
        }
      });
      Example example = ScriptUtils.createExample(msg.body());
      listDb(example, competeFuture);
    });
  }

  private void createSqlClient() {
    JsonObject
            mySQLClientConfig = new JsonObject().put("host", "test.ihorn.com.cn").put
            ("username", "admin").put("password", "csst").put("database", "om_new");
    this.sqlClient = MySQLClient.createShared(vertx, mySQLClientConfig);
  }

  private void insertDb(DeviceScript deviceScript,
                        Future<Void> completeFuture) {
    sqlClient.getConnection(ar -> {
      if (ar.failed()) {
        completeFuture.fail(ar.cause());
        return;
      }
      SQLConnection connection = ar.result();
      VertxJdbc jdbc = VertxJdbc.create(connection);
      jdbc.insert(deviceScript, iar -> {
        if (iar.failed()) {
          completeFuture.fail(iar.cause());
          return;
        }
        completeFuture.complete();
      });
    });
  }

  private void listDb(Example example,
                      Future<List<DeviceScript>> completeFuture) {
    sqlClient.getConnection(ar -> {
      if (ar.failed()) {
        completeFuture.fail(ar.cause());
        return;
      }
      SQLConnection connection = ar.result();
      VertxJdbc jdbc = VertxJdbc.create(connection);
      jdbc.findByExample(DeviceScript.class, example,
                         json -> JdbcUtils.convertToPojo(json, DeviceScript.class),
                         far -> {
                           if (far.failed()) {
                             completeFuture.fail(far.cause());
                             return;
                           }
                           completeFuture.complete(far.result());
                         });
    });
  }
}
