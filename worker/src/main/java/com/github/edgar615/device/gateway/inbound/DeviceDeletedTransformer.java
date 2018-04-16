package com.github.edgar615.device.gateway.inbound;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.ScriptLogger;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;

import java.util.List;
import java.util.Map;

/**
 * 设备删除.
 *
 * @author Edgar  Date 2018/3/19
 */
public class DeviceDeletedTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return "device".equals(input.get("command"))
           && MessageType.DEVICE_DELETED.equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input, ScriptLogger logger) {
    //设备的唯一标识符，条码或者mac
    return Lists
            .newArrayList(ImmutableMap.of("type", MessageType.DEVICE_DELETED, "command", "device"
                                                                                         + ".deleted",
                                          "data",
                                          input.get("data")));
  }
}
