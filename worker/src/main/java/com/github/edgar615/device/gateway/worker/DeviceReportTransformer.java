package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.util.base.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备响应.
 *todo 交由脚本处理
 * @author Edgar  Date 2018/3/19
 */
public class DeviceReportTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return "setDefendF1Response".equals(input.get("command"))
           && MessageType.UP.equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input) {
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    Map<String, Object> content = new HashMap<>();
    int defend = MapUtils.getInteger(data, "defend");
    if (1 == defend) {
      content.put("type", 1);
    } else if (2 == defend) {
      content.put("type", 3);
    } else {
      //todo 记录异常
    }

    return Lists.newArrayList(ImmutableMap.of("type", MessageType.REPORT, "command", "device"
                                                                                     + ".reported",
                                              "data", content));
  }

}
