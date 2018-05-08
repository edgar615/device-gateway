package com.github.edgar615.device.gateway.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Edgar on 2018/4/13.
 *
 * @author Edgar  Date 2018/4/13
 */
public class ScriptLogger {
  private static final Logger LOGGER = LoggerFactory.getLogger(ScriptLogger.class);

  /**
   * 限制每个脚本的日志不超过20条，避免被人恶意刷脚本
   */
  private final int limit = 20;

  private final List<LogMessage> logMessages = new CopyOnWriteArrayList<>();

  /**
   * 如果阀门=false，不在增加日志
   */
  private boolean throttle = true;

  private ScriptLogger() {
  }

  private synchronized boolean check() {
    if (throttle && logMessages.size() >= limit) {
      long now = Instant.now().getEpochSecond();
      logMessages.add(LogMessage.create(now, 1, "too many log"));
      throttle = false;
    }
    return throttle;
  }

  public static ScriptLogger create() {
    return new ScriptLogger();
  }

  public void info(String message) {
    if (check()) {
      long now = Instant.now().getEpochSecond();
      LogMessage logMessage = LogMessage.create(now, 1, message);
      logMessages.add(logMessage);
    }
  }

  public void error(String message) {
    if (check()) {
      long now = Instant.now().getEpochSecond();
      LogMessage logMessage = LogMessage.create(now, 2, message);
      logMessages.add(logMessage);
    }
  }

}
