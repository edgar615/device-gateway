package com.github.edgar615.device.gateway.core;

import com.github.edgar615.util.event.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/3/12.
 *
 * @author Edgar  Date 2018/3/12
 */
public interface MessageTransformer {

  /**
   * 根据inboud消息判断是否应该执行
   * @param deviceMessage
   * @return
   */
  boolean shouldExecute(DeviceMessage deviceMessage);

  default Map<String, Object> heartbeat(Map<String, Object> input) {
    return null;
  }

  /**
   * 控制主设备（网关）属性.
   * 返回的数据中存放的是协议的data属性，head属性根据配置自动填充，data的格式为
   * <pre>
   *
   * </pre>
   * @param input
   * @return 返回协议的列表
   */
  default  List<Map<String, Object>> controlPrimaryDevice(Map<String, Object> input) {
    return null;
  }

  /**
   *根据设备返回的消息，修改主设备属性
   * @param input
   * @return 主设备的map对象
   */
  default Map<String, Object> primaryDeviceChanged(Map<String, Object> input) {
    return null;
  }

  default List<Map<String, Object>> requestSnapshot(Map<String, Object> input) {
    Map<String, Object> map = new HashMap<>();
    //map.put("channel", "")
    return null;
  }

  default List<Map<String, Object>> reportSnapshot(Map<String, Object> input) {
    Map<String, Object> map = new HashMap<>();
    //map.put("channel", "")
    return null;
  }

  default List<Map<String, Object>> requestUpgrade(Map<String, Object> input) {
    Map<String, Object> map = new HashMap<>();
    //map.put("channel", "")
    return null;
  }

  default List<Map<String, Object>> reportUpgrade(Map<String, Object> input) {
    Map<String, Object> map = new HashMap<>();
    //map.put("channel", "")
    return null;
  }

  default List<Map<String, Object>> reportEvent(Map<String, Object> input) {
    Map<String, Object> map = new HashMap<>();
    //map.put("channel", "")
    return null;
  }
}
