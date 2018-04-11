package com.github.edgar615.device.gateway.inbound;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Created by Edgar on 2018/4/11.
 *
 * @author Edgar  Date 2018/4/11
 */
@Deprecated
public interface InboundHandler {

  List<Map<String, Object>> execute(Map<String, Object> input);

}
