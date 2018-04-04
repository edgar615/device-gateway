package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.MessageTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
public class ConnectTransformer2 implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return "connect".equals(input.get("command"))
           && "up".equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input) {
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    String clientIp = (String) data.get("address");
    Map<String, Object> deviceMap = new HashMap<>();
    deviceMap.put("clientIp", clientIp);
    Map<String, Object> message =
            ImmutableMap.of("type", "reported", "command", "inquireSecondDevice", "data", data);
    return Lists.newArrayList(message);
  }

}
