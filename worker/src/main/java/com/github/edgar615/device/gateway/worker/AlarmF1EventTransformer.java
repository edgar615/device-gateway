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
 * 事件.
 * todo 交由脚本处理
 *
 * @author Edgar  Date 2018/3/19
 */
public class AlarmF1EventTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return "alarmF1Event".equals(input.get("command"))
           && MessageType.UP.equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input) {
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    Map<String, Object> event = new HashMap<>();
    event.put("alarmType", MapUtils.getInteger(data, "alarm"));
    event.put("alarmTime", MapUtils.getInteger(data, "time"));
    int defend = MapUtils.getInteger(data, "defend");
    if (defend == 1) {
      event.put("type", 42201);
    } else {
      event.put("type", 42200);
    }
    return Lists.newArrayList(ImmutableMap.of("type", MessageType.EVENT, "command", "newEvent",
                                              "data", event));
  }

}
