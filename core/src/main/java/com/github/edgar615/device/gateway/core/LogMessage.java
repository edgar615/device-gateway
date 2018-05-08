package com.github.edgar615.device.gateway.core;

/**
 * Created by Edgar on 2018/5/8.
 *
 * @author Edgar  Date 2018/5/8
 */
public class LogMessage {
  private final long time;

  //1-info 2-error
  private final int type;

  private final String message;

  private LogMessage(long time, int type, String message) {
    this.time = time;
    this.type = type;
    this.message = message;
  }

  public static LogMessage create(long time, int type, String message) {
    return new LogMessage(time, type, message);
  }

  public long time() {
    return time;
  }

  public int type() {
    return type;
  }

  public String message() {
    return message;
  }
}
