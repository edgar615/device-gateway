package com.github.edgar615.device.gateway.kafka;

/**
 * Created by Administrator on 2018/6/8.
 */
public class ProductMapping {

    private final String productType;

    private final String pid;

    private final String code;

    private ProductMapping(String productType, String pid, String code) {
        this.productType = productType;
        this.pid = pid;
        this.code = code;
    }

    public static ProductMapping ProductMapping(String productType, String pid, String code) {
        return new ProductMapping(productType, pid, code);
    }

    public String productType() {
        return productType;
    }

    public String pid() {
        return pid;
    }

    public String code() {
        return code;
    }
}
