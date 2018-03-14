package com.github.edgar615.device.gateway.core;

import com.github.edgar615.util.event.Event;

import java.util.function.Function;

/**
 * Created by Edgar on 2018/3/12.
 *
 * @author Edgar  Date 2018/3/12
 */
public interface IdentificationExtractor extends Function<Event, String> {
}
