package com.github.edgar615.device.gateway.inbound;

import com.google.common.collect.Lists;

import com.github.edgar615.device.gateway.core.LocalMessageTransformer;
import com.github.edgar615.device.gateway.core.ScriptLogger;
import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.core.MessageType;

import java.util.*;

/**
 * 设备添加到平台.
 *
 * @author Edgar  Date 2018/3/19
 */
public class DeviceAddedTransformer implements LocalMessageTransformer {

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input, ScriptLogger logger) {
    //设备的唯一标识符，条码或者mac
    String deviceIdentifier = (String) input.get("deviceIdentifier");
    //产品类型
    String productType = (String) input.get("productType");
    Map<String, Object> data = (Map<String, Object>) input.getOrDefault("data", new HashMap<>());
    //密钥
    if (!(data.get("encryptKey") instanceof String)) {
      logger.error("encryptKey required");
      return new ArrayList<>();
    }
    String encryptKey = (String) data.get("encryptKey");

    //缓存中增加设备
    Map<String, Object> deviceMap = new HashMap<>();
    deviceMap.put("deviceIdentifier", deviceIdentifier);
    deviceMap.put("encryptKey", encryptKey);
    deviceMap.put("productType", productType);

    Map<String, Object> newMsg = new HashMap<>(input);
    newMsg.put("data", deviceMap);
    newMsg.put("command", "deviceAdded");
    newMsg.put("type", MessageType.INNER);
    return Lists.newArrayList(newMsg);
  }

  @Override
  public String registration() {
    return UUID.randomUUID().toString();
  }

  @Override
  public String productType() {
    return "*";
  }

  @Override
  public String command() {
    return "deviceAdded";
  }

  @Override
  public String messageType() {
    return MessageType.DOWN;
  }
}
