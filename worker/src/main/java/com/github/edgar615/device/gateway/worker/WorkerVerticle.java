package com.github.edgar615.device.gateway.worker;

import com.github.edgar615.device.gateway.core.DeviceBrokerMessage;
import com.github.edgar615.device.gateway.core.SequentialQueue;
import com.github.edgar615.device.gateway.core.SequentialQueueHelper;
import com.github.edgar615.util.base.Randoms;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.Message;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Edgar on 2018/3/12.
 *
 * @author Edgar  Date 2018/3/12
 */
public class WorkerVerticle extends AbstractVerticle {
  private SequentialQueue queue;

  private AtomicBoolean running = new AtomicBoolean(true);

  @Override
  public void start(Future<Void> startFuture) throws Exception {

    queue = SequentialQueueHelper.createShared(vertx, new JsonObject());
    //每个2秒检查一下是否有待处理的事件
    vertx.setPeriodic(2000l, l -> {
      //再次开始扫描队列
      handleEventInOrder();
    });
  }

  private void handleEventInOrder() {
    Event event;
    try {
      while ((event = queue.dequeue()) != null) {
        //处理event
        Future<Void> future = Future.future();
        handle(event, future);
        final  Event finalEvent = event;
        future.setHandler(ar -> {
          queue.complete(finalEvent);
          //尝试将worker的事件处理状态从false修改为true，如果成功说明之前worker没处理事件，开启新一轮处理
          if (running.compareAndSet(false, true)) {
            handleEventInOrder();
          }
        });
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void handle(Event event, Future<Void> completedFuture) {
    Map<String, Object> brokerMessage = MessageUtils.createMessage(event);
    if (brokerMessage == null) {
      completedFuture.complete();
    }
    EventHandler eventHandler = new EventHandler(vertx);
    eventHandler.handle(brokerMessage, ar -> {});
    //设置一个随机事件，测试等待
    long time = Long.parseLong(Randoms.randomNumber(3));
    vertx.setTimer(time, l -> {
      completedFuture.complete();
    });
  }

}
