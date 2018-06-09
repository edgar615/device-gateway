package com.github.edgar615.device.gateway.handler;

import com.github.edgar615.device.gateway.core.*;
import com.github.edgar615.device.gateway.worker.DeviceMessageHandler;
import com.github.edgar615.util.base.Randoms;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/6/8.
 */
@RunWith(VertxUnitRunner.class)
public class DeviceAddHandlerTest {
    private Vertx vertx;

    private DeviceMessageHandler handler;

    @Before
    public void setUp() {
        vertx = Vertx.vertx();
        handler = new DeviceMessageHandler(vertx);
    }

    @Test
    public void testMissEncryptKey(TestContext testContext) throws IOException, ScriptException {
        vertx.eventBus().consumer(Consts.LOCAL_DEVICE_ADD_ADDRESS, msg -> {
            System.out.println(msg.body());
            testContext.fail();
        });

        Map<String, Object> data = new HashMap<>();
        data.put("address", "127.0.0.1");

        Map<String, Object> brokerMessage = new HashMap<>();
        brokerMessage.put("productType", "F1");
        brokerMessage.put("topic", "local");
        brokerMessage.put("deviceIdentifier", "123456789");
        brokerMessage.put("traceId", UUID.randomUUID().toString());
        brokerMessage.put("command", "deviceAdded");
        brokerMessage.put("data", data);
        brokerMessage.put("type", MessageType.DOWN);

        AtomicInteger check = new AtomicInteger();
        handler.handle(brokerMessage, ar -> {
            check.incrementAndGet();
        });
        Awaitility.await().until(()-> check.get() == 1);
    }

    @Test
    public void testHandle(TestContext testContext) throws IOException, ScriptException {
        AtomicInteger check = new AtomicInteger();
        vertx.eventBus().consumer(Consts.LOCAL_DEVICE_ADD_ADDRESS, msg -> {
            System.out.println(msg.body());
            check.incrementAndGet();
        });

        Map<String, Object> data = new HashMap<>();
        data.put("encryptKey", Randoms.randomAlphabetAndNum(16));

        Map<String, Object> brokerMessage = new HashMap<>();
        brokerMessage.put("productType", "F1");
        brokerMessage.put("topic", "local");
        brokerMessage.put("deviceIdentifier", "123456789");
        brokerMessage.put("traceId", UUID.randomUUID().toString());
        brokerMessage.put("command", "deviceAdded");
        brokerMessage.put("data", data);
        brokerMessage.put("type", MessageType.DOWN);

        handler.handle(brokerMessage, ar -> {
            check.incrementAndGet();
        });
        Awaitility.await().until(()-> check.get() == 2);
    }

}
