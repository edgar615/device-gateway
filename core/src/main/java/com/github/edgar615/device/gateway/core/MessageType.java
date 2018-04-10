package com.github.edgar615.device.gateway.core;

/**
 * Created by Edgar on 2018/4/10.
 *
 * @author Edgar  Date 2018/4/10
 */
public class MessageType {

  /**
   * 平台发给网关
   */
  public static final String DOWN = "down";

  /**
   * 网关发给设备
   */
  public static final String CONTROL = "control";

  /**
   * 设备发给网关
   */
  public static final String UP = "up";

  /**
   * 网关发给平台
   */
  public static final String REPORT = "report";

  /**
   * 心跳
   */
  public static final String PING = "ping";

  /**
   * 设备连接
   */
  public static final String CONNECT = "connect";

  /**
   * 设备断开
   */
  public static final String DIS_CONNECT = "dis_connect";

  /**
   * 设备添加
   */
  public static final String DEVICE_ADDED = "device_added";

  /**
   * 设备删除
   */
  public static final String DEVICE_DELETED = "device_deleted";

  /**
   * 事件
   */
  public static final String EVENT = "event";

  /**
   * 日志
   */
  public static final String LOG = "log";
}
