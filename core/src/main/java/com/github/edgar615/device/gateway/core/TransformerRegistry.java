package com.github.edgar615.device.gateway.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/3/14.
 *
 * @author Edgar  Date 2018/3/14
 */
public class TransformerRegistry {

  private static final List<TransformerHolder> transformers = new CopyOnWriteArrayList<>();

  private static final TransformerRegistry INSTANCE = new TransformerRegistry();

  public static TransformerRegistry instance() {
    return INSTANCE;
  }

  /**
   * 根据设备类型，读取出所有的转换对象
   *
   * @param deviceType
   * @return
   */
  public List<MessageTransformer> deviceTransformers(String deviceType) {
    return transformers.stream()
            .filter(h -> h.deviceType().equals(deviceType))
            .map(h -> h.transformer())
            .collect(Collectors.toList());
  }

  public void register(String registration, String deviceType, MessageTransformer transformer) {
    remove(registration);
    transformers.add(new TransformerHolder(registration, deviceType, transformer));
  }

  public void remove(String registration) {
    transformers.removeIf(t -> t.registration().equals(registration));
  }


}
