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
public class ConnectTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return "connect".equals(input.get("command"))
           && "up".equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input) {
    //对于上行消息，记录IP和channel
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    String clientIp = (String) data.get("address");
    Map<String, Object> deviceMap = new HashMap<>();
    deviceMap.put("clientIp", clientIp);
    Map<String, Object> message =
            ImmutableMap.of("type", "reported", "command",
                            "inquirePrimaryDevice", "data", data);
    return Lists.newArrayList(message);
  }

}
