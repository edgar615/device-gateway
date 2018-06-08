package com.github.edgar615.device.gateway.outbound;

import com.github.edgar615.device.gateway.core.InnerCommand;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.device.gateway.core.Transmitter;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public class DeviceAddOutboundHandler implements OutboundHandler {

    @Override
    public void handle(Vertx vertx, Transmitter transmitter, List<Map<String, Object>> output,
                       Future<Void> completeFuture) {
        List<Map<String, Object>> devices = output.stream()
                .filter(m -> MessageType.INNER.equals(m.get("type")))
                .filter(m -> InnerCommand.DEVICE_ADDED.equals(m.get("command")))
                .collect(Collectors.toList());
        if (devices.isEmpty()) {
            completeFuture.complete();
            return;
        }

        Map<String, Object> device = devices.stream()
                .map(m -> (Map<String, Object>) m.get("data"))
                .filter(m -> m != null)
                .reduce(new HashMap<>(), (m1, m2) -> {
                    m1.putAll(m2);
                    return m1;
                });
        if (device.isEmpty() || !device.containsKey("encryptKey")) {
            completeFuture.complete();
            return;
        }
        transmitter.updateCache(new JsonObject(device));
        completeFuture.complete();
    }
}
