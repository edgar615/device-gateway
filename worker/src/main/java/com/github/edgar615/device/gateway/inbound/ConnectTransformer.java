package com.github.edgar615.device.gateway.inbound;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.ScriptLogger;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备连上平台.
 * 负责统一部分，还有每个设备的不同操作需要由脚本实现
 * @author Edgar  Date 2018/3/19
 */
public class ConnectTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return MessageType.CONNECT.equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input, ScriptLogger logger) {
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    String clientIp = (String) data.get("address");
    logger.info("connect, clientIp:" + clientIp);
    Map<String, Object> report =
            ImmutableMap.of("type", MessageType.REPORT, "command",
                            "device.reported", "data", ImmutableMap.of("isOnline", true,
                                                                       "clientIp", clientIp));
    return Lists.newArrayList(report);
  }

}
