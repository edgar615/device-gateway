package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.inbound.MessageTransformer;
import com.github.edgar615.device.gateway.outbound.OutboundHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
public class EventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);

  private final List<OutboundHandler> outboundHandlers
          = Lists.newArrayList(ServiceLoader.load(OutboundHandler.class));

  private final Vertx vertx;

  List<MessageTransformer> transformers
          = Lists.newArrayList(ServiceLoader.load(MessageTransformer.class));

  public EventHandler(Vertx vertx) {
    this.vertx = vertx;
  }

  public void handle(Map<String, Object> input, Handler<AsyncResult<Void>> resultHandler) {
    //将input转换为output
    // 因为在执行脚本的过程中，对于异常的脚本要记录日志，所以这里没有使用lambda表达式，而是使用传统的方式
    List<Map<String, Object>> output = new ArrayList<>();
    for (MessageTransformer transformer : transformers) {
      try {
        if (transformer.shouldExecute(input)) {
          List<Map<String, Object>> result = transformer.execute(input);
          if (result != null) {
            output.addAll(result);
          }
        }
      } catch (Exception e) {
        LOGGER.error("transformer failed", e);
        Map<String, Object> logData = new HashMap<>();
        logData.put("message", e.getMessage());
        Map<String, Object> log =
                ImmutableMap.of("type", MessageType.LOG, "command",
                                "error", "data", logData);
        output.add(log);
      }
    }
    //处理output
    List<Future> futures = outboundHandlers.stream()
            .map(h -> {
              Future<Void> future = Future.future();
              h.handle(vertx, input, output, future);
              return future;
            }).collect(Collectors.toList());
    CompositeFuture.all(futures)
            .setHandler(ar -> {
              if (ar.failed()) {
                resultHandler.handle(Future.failedFuture(ar.cause()));
              } else {
                resultHandler.handle(Future.succeededFuture());
              }
            });

  }

}
