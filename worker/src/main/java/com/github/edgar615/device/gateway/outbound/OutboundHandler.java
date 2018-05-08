package com.github.edgar615.device.gateway.outbound;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.device.gateway.core.Transmitter;
import com.github.edgar615.util.event.Event;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public interface OutboundHandler {

  void handle(Vertx vertx, Transmitter transmitter, List<Map<String, Object>> output,
              Future<Void> completeFuture);

}
