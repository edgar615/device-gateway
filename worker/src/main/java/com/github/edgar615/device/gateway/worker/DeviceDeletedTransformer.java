package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.MessageTransformer;

import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
public class DeviceDeletedTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return "device.deleted".equals(input.get("command"))
           && "down".equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input) {
    //设备的唯一标识符，条码或者mac
    return Lists.newArrayList(ImmutableMap.of("type", "device.deleted", "command", "device.deleted",
                                              "data",
                                              input.get("data")));
  }
}
