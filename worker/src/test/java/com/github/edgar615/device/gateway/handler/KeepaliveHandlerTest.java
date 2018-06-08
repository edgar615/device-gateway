package com.github.edgar615.device.gateway.handler;

import com.github.edgar615.device.gateway.core.*;
import com.github.edgar615.device.gateway.worker.DeviceMessageHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2018/6/8.
 */
@RunWith(VertxUnitRunner.class)
public class KeepaliveHandlerTest {
    private Vertx vertx;

    private DeviceMessageHandler handler;

    @Before
    public void setUp() {
        vertx = Vertx.vertx();
        handler = new DeviceMessageHandler(vertx);
        DeviceChannelRegistry.instance().clear();
    }

    @Test
    public void testHandle(TestContext testContext) throws IOException, ScriptException {
        Map<String, Object> data = new HashMap<>();
        data.put("address", "127.0.0.1");

        Map<String, Object> brokerMessage = new HashMap<>();
        brokerMessage.put("productType", "F1");
        brokerMessage.put("topic", "local");
        brokerMessage.put("deviceIdentifier", "123456789");
        brokerMessage.put("traceId", UUID.randomUUID().toString());
        brokerMessage.put("command", "keepalive");
        brokerMessage.put("data", data);
        brokerMessage.put("channel", "local");
        brokerMessage.put("type", MessageType.KEEPALIVE);

        AtomicBoolean check = new AtomicBoolean();
        handler.handle(brokerMessage, ar -> {
            check.set(true);
        });
        Awaitility.await().until(()-> check.get());
        Assert.assertEquals(1, DeviceChannelRegistry.instance().size());
    }

}
