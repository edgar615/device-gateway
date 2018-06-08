package com.github.edgar615.device.gateway;

import com.github.edgar615.util.vertx.deployment.MainVerticle;
import io.vertx.core.Launcher;

/**
 * Created by Edgar on 2018/5/7.
 *
 * @author Edgar  Date 2018/5/7
 */
public class TestMain {
  public static void main(String[] args) {
    new Launcher().execute("run", MainVerticle.class.getName(),
                           "--conf=e:\\iotp\\device-gateway\\launcher\\src\\main\\conf"
                           + "\\config.json");
  }
}
