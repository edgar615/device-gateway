package com.github.edgar615.device.gateway.worker.result;

import io.vertx.core.Future;

import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public interface TransformerResultHandler {

  void handle(Map<String, Object> input, List<Map<String, Object>> output,
              Future<Void> completeFuture);
}
