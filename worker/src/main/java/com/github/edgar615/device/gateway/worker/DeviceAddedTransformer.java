package com.github.edgar615.device.gateway.worker;

import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备添加到平台.
 *
 * @author Edgar  Date 2018/3/19
 */
public class DeviceAddedTransformer implements MessageTransformer {

  @Override
  public boolean shouldExecute(Map<String, Object> input) {
    return "device.added".equals(input.get("command"))
           && MessageType.DEVICE_ADDED.equals(input.get("type"));
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input) {
    //设备的唯一标识符，条码或者mac
    String id = (String) input.get("id");
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    //TODO 数据校验
    //密钥
    String encryptKey = (String) data.get("encryptKey");
    //产品类型
    String productType = (String) data.get("productType");
    //缓存中增加设备
    Map<String, Object> deviceMap = new HashMap<>();
    deviceMap.put("id", id);
    deviceMap.put("encryptKey", encryptKey);
    deviceMap.put("productType", productType);

    Map<String, Object> newMsg = new HashMap<>(input);
    newMsg.put("data", deviceMap);
    newMsg.put("type", MessageType.DEVICE_ADDED);
    return Lists.newArrayList(newMsg);
  }

}
