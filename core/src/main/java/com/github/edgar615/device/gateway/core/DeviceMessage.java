package com.github.edgar615.device.gateway.core;

import java.util.Map;

/**
 * Created by Edgar on 2018/3/14.
 *
 * @author Edgar  Date 2018/3/14
 */
public class DeviceMessage {

  /**
   * 全局跟踪ID
   */
  private String traceId;

  /**
   * 设备类型
   */
  private String deviceType;

  /**
   * 设备唯一标识符，mac地址
   */
  private String deviceIdentifier;

  /**
   * 消息头
   */
  private Map<String, Object> head;

  /**
   * 消息内容
   */
  private Map<String, Object> body;


  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }

  public String getDeviceIdentifier() {
    return deviceIdentifier;
  }

  public void setDeviceIdentifier(String deviceIdentifier) {
    this.deviceIdentifier = deviceIdentifier;
  }

  public Map<String, Object> getHead() {
    return head;
  }

  public void setHead(Map<String, Object> head) {
    this.head = head;
  }

  public Map<String, Object> getBody() {
    return body;
  }

  public void setBody(Map<String, Object> body) {
    this.body = body;
  }
}
