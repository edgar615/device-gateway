package com.github.edgar615.device.gateway.worker;

import com.github.edgar615.device.gateway.core.TransformerHolder;
import com.github.edgar615.device.gateway.worker.result.DeviceLogResultHandler;
import com.github.edgar615.device.gateway.worker.result.PingResultHandler;
import com.github.edgar615.device.gateway.worker.result.TransformerResultHandler;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.ArrayList;
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

  private final List<TransformerResultHandler> transformerResultHandlers = new ArrayList<>();

  private final Vertx vertx;

  public EventHandler(Vertx vertx) {
    this.vertx = vertx;
    transformers.add(new TransformerHolder("a", "*", new DeviceAddedTransformer()));
    transformers.add(new TransformerHolder("a", "*", new DeviceDeletedTransformer()));
    transformers.add(new TransformerHolder("a", "*", new KeepaliveTransformer()));
    transformers.add(new TransformerHolder("a", "*", new ConnectTransformer()));
    transformers.add(new TransformerHolder("a", "*", new F1DefendEventTransformer()));
    transformerResultHandlers.add(new PingResultHandler(vertx));
    transformerResultHandlers.add(new DeviceLogResultHandler(vertx));
  }

  public void handle(Map<String, Object> input, Handler<AsyncResult<Void>> resultHandler) {
    //使用script的脚本
    List<Map<String, Object>> output = transformers.stream()
//            .filter(h -> "*".equals(h.deviceType()) || productType.equals(h.deviceType()))
            .map(h -> h.transformer())
            .filter(h -> h.shouldExecute(input))
            .map(t -> t.execute(input))
            .filter(l -> l != null)
            .flatMap(l -> l.stream())
            .collect(Collectors.toList());
    //合并可以放在一起的消息
//    Map<String, List<Map<String, Object>>> groupMessage = output.stream()
//            .collect(Collectors.groupingBy(m -> (String) m.get("type")));
    List<Future> futures = transformerResultHandlers.stream()
            .map(h -> {
              Future<Void> future = Future.future();
              h.handle(input, output, future);
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
