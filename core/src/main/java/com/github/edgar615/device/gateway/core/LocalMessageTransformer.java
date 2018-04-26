package com.github.edgar615.device.gateway.core;

/**
 * Created by Edgar on 2018/4/26.
 *
 * @author Edgar  Date 2018/4/26
 */
public interface LocalMessageTransformer extends MessageTransformer {
  String registration();

  String productType();

  String command();

  String messageType();

}
