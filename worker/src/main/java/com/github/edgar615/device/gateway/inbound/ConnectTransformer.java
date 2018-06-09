package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.core.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 设备连上平台.
 * 负责统一部分，还有每个设备的不同操作需要由脚本实现
 * @author Edgar  Date 2018/3/19
 */
public class ConnectTransformer implements LocalMessageTransformer {

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input, ScriptLogger logger) {
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    String clientIp = (String) data.get("address");
    logger.info("connect, clientIp:" + clientIp);
    Map<String, Object> report =
            ImmutableMap.of("type", MessageType.REPORT, "command",
                            "deviceReport", "data", ImmutableMap.of("isOnline", true,
                                                                       "clientIp", clientIp));
    return Lists.newArrayList(report);
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
    return KeepaliveCommand.CONNECT;
  }

  @Override
  public String messageType() {
    return MessageType.KEEPALIVE;
  }

}
