package com.github.edgar615.device.gateway.core;

import com.github.edgar615.device.gateway.core.MessageTransformer;

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
  private final String deviceType;

  /**
   * 协议转换类
   */
  private final MessageTransformer transformer;

  public TransformerHolder(String registration, String deviceType, MessageTransformer transformer) {
    this.registration = registration;
    this.deviceType = deviceType;
    this.transformer = transformer;
  }

  public String registration() {
    return registration;
  }

  public String deviceType() {
    return deviceType;
  }

  public MessageTransformer transformer() {
    return transformer;
  }
}
