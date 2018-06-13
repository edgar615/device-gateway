package com.github.edgar615.device.gateway.core;

public class KeepaliveExpiredException extends RuntimeException {
  public KeepaliveExpiredException() {
    super("keepalive expired", null, false, false);
  }
}