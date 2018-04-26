package com.github.edgar615.device.gateway.core;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by Edgar on 2018/3/14.
 *
 * @author Edgar  Date 2018/3/14
 */
public class TransformerRegistry {

  private static final List<LocalMessageTransformer> transformers = new CopyOnWriteArrayList<>();

  private static final List<LocalMessageTransformer> localTransformers
          = Lists.newArrayList(ServiceLoader.load(LocalMessageTransformer.class));

  private static final TransformerRegistry INSTANCE = new TransformerRegistry();

  private TransformerRegistry() {
    for (LocalMessageTransformer transformer : localTransformers) {
      LocalMessageTransformer holder = new LocalMessageTransformerAdapter(transformer.registration(),
                                                       transformer.productType(),
                                                       transformer.messageType(),
                                                       transformer.command(), transformer);
      transformers.add(holder);
    }
  }

  public static TransformerRegistry instance() {
    return INSTANCE;
  }

  /**
   * 根据设备类型，读取出所有的转换对象
   *
   * @param productType
   * @return
   */
  public List<MessageTransformer> deviceTransformers(String productType, String messageType,
                                                     String command) {
    return transformers.stream()
            .filter(h -> "*".equalsIgnoreCase(h.productType())
                         || h.productType().equals(productType))
            .filter(h -> "*".equalsIgnoreCase(h.messageType())
                         || h.messageType().equals(messageType))
            .filter(h -> "*".equalsIgnoreCase(h.command())
                         || h.command().equals(command))
            .collect(Collectors.toList());
  }

  public void register(String registration, String productType, String messageType, String command,
                       MessageTransformer transformer) {
    remove(registration);
    transformers.add(new LocalMessageTransformerAdapter(registration, productType, messageType, command,
                                           transformer));
  }

  public void remove(String registration) {
    transformers.removeIf(t -> t.registration().equals(registration));
  }

  public int size() {
    return transformers.size();
  }

  public void clear() {
    transformers.clear();
  }
}
