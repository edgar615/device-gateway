package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.core.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 设备删除.
 *
 * @author Edgar  Date 2018/3/19
 */
public class DeviceDeletedTransformer implements LocalMessageTransformer {

    @Override
    public List<Map<String, Object>> execute(Map<String, Object> input, ScriptLogger logger) {
        //设备的唯一标识符，条码或者mac
        return Lists
                .newArrayList(ImmutableMap.of("type", MessageType.INNER,
                        "command", InnerCommand.DEVICE_DELETED,
                        "data", input.get("data")));
    }

    @Override
    public String registration() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String productType() {
        return "*";
    }

    @Override
    public String command() {
        return "deviceDeleted";
    }

    @Override
    public String messageType() {
        return MessageType.DOWN;
    }

}
