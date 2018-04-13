package com.github.edgar615.device.gateway.inbound;

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
   * 协议转换类
   */
  private final MessageTransformer transformer;

  public TransformerHolder(String registration, String productType, MessageTransformer transformer) {
    this.registration = registration;
    this.productType = productType;
    this.transformer = transformer;
  }

  public String registration() {
    return registration;
  }

  public String deviceType() {
    return productType;
  }

  public MessageTransformer transformer() {
    return transformer;
  }
}
