package com.github.edgar615.device.gateway.script;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.ScriptCompiler;
import com.github.edgar615.device.gateway.core.TransformerRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Created by Edgar on 2018/4/16.
 *
 * @author Edgar  Date 2018/4/16
 */
public class MySQLVerticle extends AbstractVerticle {

  private static final String SQL_INSERT =
          "insert into device_script(device_script_id, product_type, message_type, command, "
          + "script_content, state, add_on, last_modify_on) "
          + "values(?, ?, ?, ?, ?, ?, ?, ?)";

  private static final String SQL_SELECT =
          "select * from device_script";

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
      String productType = jsonObject.getString("productType");
      String messageType = jsonObject.getString("messageType");
      String command = jsonObject.getString("command");
      String content = jsonObject.getString("content");
      String registration = UUID.randomUUID().toString();
      jsonObject.put("registration", registration);
      //编译content
      MessageTransformer transformer;
      try {
        transformer = ScriptCompiler.compile(content);
      } catch (Exception e) {
        //todo 错误
        msg.fail(-1, e.getMessage());
        return;
      }
      Future<Void> competeFuture = Future.future();
      competeFuture.setHandler(ar -> {
        if (ar.succeeded()) {
          TransformerRegistry.instance().register(registration, productType,
                                                  messageType, command, transformer);
          msg.reply(new JsonObject().put("registration", registration));
        } else {
          msg.fail(-1, ar.cause().getMessage());
        }
      });
      insertDb(sqlClient, jsonObject, competeFuture);
    });
  }

  private void listConsumer() {
    vertx.eventBus().<JsonObject>consumer(Consts.LOCAL_SCRIPT_LIST_ADDRESS, msg -> {
      Future<List<JsonObject>> competeFuture = Future.future();
      competeFuture.setHandler(ar -> {
        if (ar.succeeded()) {
          msg.reply(new JsonArray(ar.result()));
        } else {
          msg.fail(-1, ar.cause().getMessage());
        }
      });
      listDb(sqlClient, new JsonObject(), competeFuture);
    });
  }

  private void createSqlClient() {
    JsonObject
            mySQLClientConfig = new JsonObject().put("host", "test.ihorn.com.cn").put
            ("username", "admin").put("password", "csst").put("database", "om_new");
    this.sqlClient = MySQLClient.createShared(vertx, mySQLClientConfig);
  }

  private void insertDb(AsyncSQLClient sqlClient, JsonObject jsonObject,
                        Future<Void> completeFuture) {
    sqlClient.getConnection(ar -> {
      if (ar.failed()) {
        completeFuture.fail(ar.cause());
        return;
      }
      SQLConnection connection = ar.result();
      String productType = jsonObject.getString("productType");
      String messageType = jsonObject.getString("messageType");
      String command = jsonObject.getString("command");
      String content = jsonObject.getString("content");
      String registration = jsonObject.getString("registration");
      JsonArray params = new JsonArray()
              .add(registration).add(productType)
              .add(messageType).add(command)
              .add(content).add(1)
              .add(Instant.now().getEpochSecond())
              .add(Instant.now().getEpochSecond());
      connection.updateWithParams(SQL_INSERT,
                                  params, result -> {
                if (result.succeeded()) {
//                  UpdateResult updateResult = result.result();
                  completeFuture.complete();
                } else {
                  completeFuture.fail(result.cause());
                }
              });
    });
  }

  private void listDb(AsyncSQLClient sqlClient, JsonObject jsonObject,
                      Future<List<JsonObject>> completeFuture) {
    sqlClient.getConnection(ar -> {
      if (ar.failed()) {
        completeFuture.fail(ar.cause());
        return;
      }
      sqlClient.getConnection(res -> {
        if (res.succeeded()) {
          SQLConnection sqlConnection = res.result();
          sqlConnection.query(SQL_SELECT, result -> {
            if (result.failed()) {
              completeFuture.fail(ar.cause());
              return;
            }
            ResultSet resultSet = result.result();
            List<JsonObject> results = resultSet.getRows();
            completeFuture.complete(results);
          });
        } else {
          completeFuture.fail(res.cause());
        }
      });
    });
  }
}
