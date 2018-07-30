package com.github.edgar615.device.gateway.worker;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import io.vertx.core.Vertx;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/7/29.
 */
public class GatewayMetrics {

    private final MetricRegistry registry;

    private final Counter inputMessages;

    public GatewayMetrics() {
        this.registry = SharedMetricRegistries.getOrCreate("device-gateway-registry");
        inputMessages = registry.counter("device-gateway.messages");
    }

    public void messageCounter(Map<String, Object> input) {
        String productType = (String) input.get("productType");
        String command = (String) input.get("command");
        String messageType = (String) input.get("type");
        inputMessages.inc();
        this.registry.counter(MetricRegistry.name("device-gateway.messages", productType)).inc();
        this.registry.counter(MetricRegistry.name("device-gateway.messages", productType, messageType)).inc();
        this.registry.counter(MetricRegistry.name("device-gateway.messages", productType, messageType, command)).inc();
    }

    public void messageTimer(Map<String, Object> input, long duration) {
        String productType = (String) input.get("productType");
        String command = (String) input.get("command");
        String messageType = (String) input.get("type");
        inputMessages.inc();
        this.registry.timer(MetricRegistry.name("device-gateway.timer", productType)).update(duration, TimeUnit.MILLISECONDS);
        this.registry.timer(MetricRegistry.name("device-gateway.timer", productType, messageType)).update(duration, TimeUnit.MILLISECONDS);
        this.registry.timer(MetricRegistry.name("device-gateway.timer", productType, messageType, command)).update(duration, TimeUnit.MILLISECONDS);
    }

    public void scriptTimer(String scriptId, long duration) {
        inputMessages.inc();
        this.registry.timer(MetricRegistry.name("device-gateway.timer.script", scriptId)).update(duration, TimeUnit.MILLISECONDS);
    }

}
