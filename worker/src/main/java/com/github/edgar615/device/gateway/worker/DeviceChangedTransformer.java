package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;

import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;
import com.github.edgar615.util.base.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/3/19.
 *todo 交由脚本处理
 * @author Edgar  Date 2018/3/19
 */
public class DeviceChangedTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return "device.changed".equals(input.get("command"))
           && MessageType.DOWN.equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input) {
    //设备的唯一标识符，条码或者mac
    String id = (String) input.get("id");
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    List<Map<String, Object>> result = new ArrayList<>();
    if (data.containsKey("defendState")) {
      int defendState = MapUtils.getInteger(data, "defendState", 0);
      Map<String, Object> defendCmd = new HashMap<>();
      defendCmd.put("type", MessageType.CONTROL);
      defendCmd.put("command", "setDefendF1");
      if (defendState == 1) {
        defendCmd.put("defend", 1);
        result.add(defendCmd);
      } else if (defendState == 3) {
        defendCmd.put("defend", 2);
        result.add(defendCmd);
      } else {
        //todo 记录异常日志
      }
    }
    return result;
  }

}
