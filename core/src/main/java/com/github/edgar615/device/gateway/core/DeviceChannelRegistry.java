package com.github.edgar615.device.gateway.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Edgar on 2018/3/14.
 *
 * @author Edgar  Date 2018/3/14
 */
public class DeviceChannelRegistry {

  private static final Map<String, String> channels = new ConcurrentHashMap<>();

  private static final DeviceChannelRegistry INSTANCE = new DeviceChannelRegistry();

  private DeviceChannelRegistry() {
  }

  public static DeviceChannelRegistry instance() {
    return INSTANCE;
  }

  public void register(String registration, String channel) {
    channels.put(registration, channel);
  }

  public void remove(String registration) {
    channels.remove(registration);
  }

  public String get(String registration) {
    return channels.get(registration);
  }

  public int size() {
    return channels.size();
  }

  public void clear() {
    channels.clear();
  }
}
