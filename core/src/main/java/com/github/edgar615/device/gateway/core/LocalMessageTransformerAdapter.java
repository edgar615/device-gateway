package com.github.edgar615.device.gateway.core;

import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/4/26.
 *
 * @author Edgar  Date 2018/4/26
 */
public class LocalMessageTransformerAdapter implements LocalMessageTransformer {
  /**
   * 注册ID
   */
  private final String registration;

  /**
   * 设备类型
   */
  private final String productType;

  /**
   * up, on
   */
  private final String messageType;

  private final String command;

  /**
   * 协议转换类
   */
  private final MessageTransformer transformer;

  public LocalMessageTransformerAdapter(String registration, String productType, String messageType,
                                        String command, MessageTransformer transformer) {
    this.registration = registration;
    this.productType = productType;
    this.messageType = messageType;
    this.command = command;
    this.transformer = transformer;
  }

  public String registration() {
    return registration;
  }

  public String productType() {
    return productType;
  }

  public String command() {
    return command;
  }

  public String messageType() {
    return messageType;
  }

  @Override
  public List<Map<String, Object>> execute(Map<String, Object> input, ScriptLogger logger) {
    return transformer.execute(input, logger);
  }
}
