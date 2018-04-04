package com.github.edgar615.device.gateway.core;

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
   *
   * @param input
   * @return
   */
  boolean shouldExecute(Map<String, Object> input);

  List<Map<String, Object>> execute(Map<String, Object> input);

}
