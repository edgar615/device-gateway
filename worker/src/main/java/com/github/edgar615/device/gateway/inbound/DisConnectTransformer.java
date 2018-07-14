package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.core.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 设备断开.
 * 负责统一部分，还有每个设备的不同操作需要由脚本实现
 *
 * @author Edgar  Date 2018/3/19
 */
public class DisConnectTransformer implements LocalMessageTransformer {

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input, ScriptLogger logger) {
    logger.info("disConnect");
    Map<String, Object> report =
            ImmutableMap.of("type", MessageType.REPORT, "command",
                            ReportCommand.DEVICE_DISCONN, "data", new HashMap<>());

    Map<String, Object> keepalive =
            ImmutableMap.of("type", MessageType.KEEPALIVE, "command",
                            KeepaliveCommand.CONNECT, "data", new HashMap<>());
    //掉线事件
    Map<String, Object> eventData = new HashMap<>();
    eventData.putIfAbsent("originId", input.getOrDefault("traceId", UUID.randomUUID().toString()));
    eventData.putIfAbsent("time", Instant.now().getEpochSecond());
    eventData.putIfAbsent("type", 40021);
    eventData.putIfAbsent("level", 1);
    eventData.putIfAbsent("push", true);
    eventData.putIfAbsent("defend", false);
    Map<String, Object> event =
            ImmutableMap.of("type", MessageType.EVENT, "command",
                    EventCommand.NEW_EVENT, "data", eventData);
    return Lists.newArrayList(report, keepalive, event);
  }

  @Override
  public String registration() {
    return UUID.randomUUID().toString();
  }

  @Override
  public String productType() {
    return "*";
  }

  @Override
  public String command() {
    return KeepaliveCommand.DIS_CONNECT;
  }

  @Override
  public String messageType() {
    return MessageType.KEEPALIVE;
  }
}
