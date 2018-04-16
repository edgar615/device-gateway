package com.github.edgar615.device.gateway.core;

/**
 * Created by Edgar on 2018/3/14.
 *
 * @author Edgar  Date 2018/3/14
 */
public class TransformerHolder {

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

  public TransformerHolder(String registration, String productType, String messageType,
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

  public String deviceType() {
    return productType;
  }

  public String command() {
    return command;
  }

  public String messageType() {
    return messageType;
  }

  public MessageTransformer transformer() {
    return transformer;
  }
}
