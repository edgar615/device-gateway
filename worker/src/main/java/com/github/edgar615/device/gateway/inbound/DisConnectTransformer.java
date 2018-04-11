package com.github.edgar615.device.gateway.inbound;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.MessageType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备断开.
 * 负责统一部分，还有每个设备的不同操作需要由脚本实现
 *
 * @author Edgar  Date 2018/3/19
 */
public class DisConnectTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return MessageType.DIS_CONNECT.equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input) {
    Map<String, Object> log =
            ImmutableMap.of("type", MessageType.LOG, "command",
                            "disConnect", "data", new HashMap<>());
    Map<String, Object> report =
            ImmutableMap.of("type", MessageType.REPORT, "command",
                            "device.reported", "data", ImmutableMap.of("isOnline", false));
    return Lists.newArrayList(report, log);
  }

}
