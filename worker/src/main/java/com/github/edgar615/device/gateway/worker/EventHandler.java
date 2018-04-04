package com.github.edgar615.device.gateway.worker;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.TransformerHolder;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
public class EventHandler {

  private final List<TransformerHolder> transformers = new ArrayList<>();

  private final Vertx vertx;

  public EventHandler(Vertx vertx) {
    this.vertx = vertx;
    transformers.add(new TransformerHolder("a", "*", new DeviceAddedTransformer()));
    transformers.add(new TransformerHolder("a", "*", new DeviceDeletedTransformer()));
    transformers.add(new TransformerHolder("a", "*", new KeepaliveTransformer()));
  }

  //  down 由平台发往网关
//  control 由网关发往设备
//  up 由设备发往网关
//  reported 由网关发往平台
  //AddDevice 新增设备
//  DeleteDevice 删除设备
//  keepalive 心跳
  public void handle(Map<String, Object> message, Handler<AsyncResult<Void>> resultHandler) {
    //使用script的脚本
    List<Map<String, Object>> messages = transformers.stream()
//            .filter(h -> "*".equals(h.deviceType()) || productType.equals(h.deviceType()))
            .map(h -> h.transformer())
            .filter(h -> h.shouldExecute(message))
            .map(t -> t.execute(message))
            .filter(l -> l != null)
            .flatMap(l -> l.stream())
            .collect(Collectors.toList());
    //合并可以放在一起的消息
//    Map<String, List<Map<String, Object>>> groupMessage = messages.stream()
//            .collect(Collectors.groupingBy(m -> (String) m.get("type")));
    List<Future> futures = new ArrayList<>();
    futures.add(keepalive(messages));
    futures.add(deviceAdded(messages));
    futures.add(deviceDeleted(messages));
    CompositeFuture.all(futures)
            .setHandler(ar -> {
              if (ar.failed()) {
                resultHandler.handle(Future.failedFuture(ar.cause()));
              } else {
                resultHandler.handle(Future.succeededFuture());
              }
            });

  }

  private Future<Void> keepalive(List<Map<String, Object>> messages) {
    Map<String, Object> keepalive = messages.stream()
            .filter(m -> "keepalive".equals(m.get("type")))
            .map(m -> (Map<String, Object>) m.get("data"))
            .reduce(new HashMap<>(), (m1, m2) -> {
              m1.putAll(m2);
              return m1;
            });
    if (keepalive.isEmpty()) {
      return Future.succeededFuture();
    }
    Future<Void> future = Future.future();
    vertx.eventBus().send(Consts.LOCAL_DEVICE_HEARTBEAT_ADDRESS,
                          new JsonObject(keepalive), ar -> {
              if (ar.failed()) {
                future.fail(ar.cause());
                return;
              }
              future.complete();
            });
    return future;
  }

  private Future<Void> deviceAdded(List<Map<String, Object>> messages) {
    Map<String, Object> device = messages.stream()
            .filter(m -> "device.added".equals(m.get("type")))
            .map(m -> (Map<String, Object>) m.get("data"))
            .reduce(new HashMap<>(), (m1, m2) -> {
              m1.putAll(m2);
              return m1;
            });
    if (device.isEmpty()) {
      return Future.succeededFuture();
    }
    Future<Void> future = Future.future();
    vertx.eventBus().send(Consts.LOCAL_DEVICE_ADD_ADDRESS,
                          new JsonObject(device), ar -> {
              if (ar.failed()) {
                future.fail(ar.cause());
                return;
              }
              future.complete();
            });
    return future;
  }

  private Future<Void> deviceDeleted(List<Map<String, Object>> messages) {
    Map<String, Object> device = messages.stream()
            .filter(m -> "device.deleted".equals(m.get("type")))
            .map(m -> (Map<String, Object>) m.get("data"))
            .reduce(new HashMap<>(), (m1, m2) -> {
              m1.putAll(m2);
              return m1;
            });
    if (device.isEmpty()) {
      return Future.succeededFuture();
    }
    Future<Void> future = Future.future();
    vertx.eventBus().send(Consts.LOCAL_DEVICE_DELETE_ADDRESS,
                          new JsonObject(device), ar -> {
              if (ar.failed()) {
                future.fail(ar.cause());
                return;
              }
              future.complete();
            });
    return future;
  }

}
