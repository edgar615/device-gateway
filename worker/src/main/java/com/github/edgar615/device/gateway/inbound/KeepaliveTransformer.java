package com.github.edgar615.device.gateway.inbound;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.LocalMessageTransformer;
import com.github.edgar615.device.gateway.core.ScriptLogger;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
public class KeepaliveTransformer implements LocalMessageTransformer {

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
    return "keepalive";
  }

  @Override
  public String messageType() {
    return MessageType.UP;
  }

}
