package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.util.base.MapUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件.
 * todo 交由脚本处理
 *
 * @author Edgar  Date 2018/3/19
 */
public class F1DefendEventTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return "setDefendF1Response".equals(input.get("command"))
           && MessageType.UP.equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input) {
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    Map<String, Object> event = new HashMap<>();
    event.put("alarmType", 1);
    event.put("alarmTime", Instant.now().getEpochSecond());
    int defend = MapUtils.getInteger(data, "defend");
    List<Map<String, Object>> result = new ArrayList<>();
    if (1 == defend) {
      event.put("type", 43001);
      result.add(ImmutableMap.of("type", MessageType.EVENT, "command", "newEvent",
                                 "data", event));
    } else if (2 == defend) {
      event.put("type", 43003);
      result.add(ImmutableMap.of("type", MessageType.EVENT, "command", "newEvent",
                                 "data", event));
    } else {
      event.put("type", 42200);
      result.add(ImmutableMap.of("type", MessageType.LOG, "command", "undefinedDefend",
                                 "data", data));
    }
return result;
  }

}
