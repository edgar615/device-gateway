package com.github.edgar615.device.gateway.core;

import java.util.Map;

/**
 * Created by Edgar on 2018/4/4.
 *
 * @author Edgar  Date 2018/4/4
 */
public class DeviceBrokerMessage {

  private String traceId;

  private String command;

  private Map<String, Object> data;

  private String topic;

//  down 由平台发往网关
//  control 由网关发往设备
//  up 由设备发往网关
//  reported 由网关发往平台
  //AddDevice 新增设备
//  DeleteDevice 删除设备
//  keepalive 心跳
  private String type;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }
}
