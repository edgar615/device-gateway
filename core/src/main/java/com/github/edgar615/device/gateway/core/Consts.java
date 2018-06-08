package com.github.edgar615.device.gateway.core;

/**
 * Created by Edgar on 2018/3/19.
 *
 * @author Edgar  Date 2018/3/19
 */
public class Consts {
  public static final String LOCAL_EVENT_HANDLER
          = "__com.github.edgar615.device.gateway.event.handler";

  public static final String LOCAL_KAFKA_PRODUCER_ADDRESS
          = "__com.github.edgar615.device.gateway.kafka.producer";

  public static final String LOCAL_DEVICE_HEARTBEAT_ADDRESS
          = "__com.github.edgar615.keepalive.heartbeat";

  public static final String LOCAL_DEVICE_TOTAL_ADDRESS
          = "__com.github.edgar615.keepalive.total";

  public static final String LOCAL_DEVICE_ADDRESS
          = "__v1.event.device.local";

  public static final String LOCAL_DEVICE_ADD_ADDRESS
          = "__com.github.edgar615.device.gateway.device.add";

  public static final String LOCAL_DEVICE_DELETE_ADDRESS
          = "__com.github.edgar615.device.gateway.device.delete";

  public static final String LOCAL_DEVICE_LOG_ADDRESS
          = "__com.github.edgar615.device.gateway.device.log";

  public static final String LOCAL_SCRIPT_ADDRESS
          = "__com.github.edgar615.device.gateway.script";

  public static final String LOCAL_SCRIPT_ADD_ADDRESS
          = "__com.github.edgar615.device.gateway.script.created";

  public static final String LOCAL_SCRIPT_UPDATE_ADDRESS
          = "__com.github.edgar615.device.gateway.script.updated";

  public static final String LOCAL_SCRIPT_DELETE_ADDRESS
          = "__com.github.edgar615.device.gateway.script.deleted";

  public static final String LOCAL_SCRIPT_LOAD_ADDRESS
          = "__com.github.edgar615.device.gateway.script.load";

  public static final String LOCAL_SCRIPT_LIST_ADDRESS
          = "__com.github.edgar615.device.gateway.script.list";
}
