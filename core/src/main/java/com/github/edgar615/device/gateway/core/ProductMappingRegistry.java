package com.github.edgar615.device.gateway.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Edgar on 2018/3/14.
 *
 * @author Edgar  Date 2018/3/14
 */
public class ProductMappingRegistry {

  private static final List<ProductMapping> mappings = new CopyOnWriteArrayList<>();

  private static final ProductMappingRegistry INSTANCE = new ProductMappingRegistry();

  private ProductMappingRegistry() {
  }

  public static ProductMappingRegistry instance() {
    return INSTANCE;
  }

  public void register(String productType, String pid, String code) {
    mappings.add(ProductMapping.productMapping(productType, pid, code));
  }

  public void remove(String productType) {
    mappings.removeIf(m -> m.productType().equalsIgnoreCase(productType));
  }

  public ProductMapping getByProduct(String productType) {
    return mappings.stream()
            .filter(m -> m.productType().equalsIgnoreCase(productType))
            .findFirst().orElse(null);
  }

  public ProductMapping getByPidAndCode(String pid, String code) {
    return mappings.stream()
            .filter(m -> m.pid().equalsIgnoreCase(pid))
            .filter(m -> m.code().equalsIgnoreCase(code))
            .findFirst().orElse(null);
  }

  public int size() {
    return mappings.size();
  }

  public void clear() {
    mappings.clear();
  }
}
