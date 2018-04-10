package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备连上平台.
 *todo 交由脚本处理
 * @author Edgar  Date 2018/3/19
 */
public class ConnectTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return MessageType.CONNECT.equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input) {
    //对于上行消息，记录IP和channel
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    String clientIp = (String) data.get("address");
    Map<String, Object> deviceMap = new HashMap<>();
    deviceMap.put("clientIp", clientIp);
    Map<String, Object> message =
            ImmutableMap.of("type", MessageType.CONTROL, "command",
                            "inquiryF1Version", "data", data);
    return Lists.newArrayList(message);
  }

}
