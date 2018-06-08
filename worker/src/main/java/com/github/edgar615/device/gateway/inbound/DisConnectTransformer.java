package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.core.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 设备断开.
 * 负责统一部分，还有每个设备的不同操作需要由脚本实现
 *
 * @author Edgar  Date 2018/3/19
 */
public class DisConnectTransformer implements LocalMessageTransformer {

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input, ScriptLogger logger) {
    logger.info("disConnect");
    Map<String, Object> report =
            ImmutableMap.of("type", MessageType.REPORT, "command",
                            "device.reported", "data", ImmutableMap.of("isOnline", false));
    return Lists.newArrayList(report);
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
    return InnerCommand.DIS_CONNECT;
  }

  @Override
  public String messageType() {
    return MessageType.KEEPALIVE;
  }
}
