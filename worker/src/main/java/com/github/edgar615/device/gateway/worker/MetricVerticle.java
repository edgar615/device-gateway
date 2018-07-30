package com.github.edgar615.device.gateway.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/7/29.
 */
public class MetricVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricVerticle.class);

    @Override
    public void start() throws Exception {
        MetricsService metricsService = MetricsService.create(vertx);
        vertx.setPeriodic(5 * 60 * 1000, t -> {
            JsonObject metrics = metricsService.getMetricsSnapshot("device-gateway");
            LOGGER.info("metrics: {}", metrics);
        });
    }
}
