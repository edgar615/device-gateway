package com.github.edgar615.device.gateway.inbound;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.ScriptLogger;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
public class KeepaliveTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return "keepalive".equals(input.get("command"))
           && MessageType.UP.equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input, ScriptLogger logger) {
    //reported表示由网关向平台传递
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    String clientIp = (String) data.get("address");
    Map<String, Object> deviceMap = new HashMap<>();
    deviceMap.put("clientIp", clientIp);
    String channel = (String) input.get("channel");
    if (Strings.isNullOrEmpty(channel)) {
      logger.error("ping, channel required");
      return Lists.newArrayList();
    }
    logger.info("connect, clientIp:" + clientIp);
    return Lists.newArrayList(ImmutableMap.of("type", MessageType.PING, "command", "ping", "data",
                                              data));
  }

}
