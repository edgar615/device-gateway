package com.github.edgar615.device.gateway.worker;

import com.github.edgar615.device.gateway.core.SequentialQueue;
import com.github.edgar615.util.base.Randoms;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.event.Message;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.shareddata.LocalMap;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Edgar on 2018/3/12.
 *
 * @author Edgar  Date 2018/3/12
 */
public class WorkerVerticle extends AbstractVerticle {

  private static final String QUEUE_LOCAL_MAP_NAME_BASE = "com.github.edgar615.device.queue";


  private static final String DEFAULT_QUEUE_NAME = "DEFAULT_DEVICE_SEQ_QUEUE";

  //编写DEMO的时候worker使用的一个队列，实际上要使用一个自定义的队列
  private SequentialQueue queue;

  private AtomicBoolean running = new AtomicBoolean(true);

  @Override
  public void start(Future<Void> startFuture) throws Exception {

    LocalMap<String, SequentialQueue> map = vertx.sharedData().getLocalMap(QUEUE_LOCAL_MAP_NAME_BASE);

     queue = map.get(DEFAULT_QUEUE_NAME);
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
    System.out.println(this + ":" + event);
    //设置一个随机事件，测试等待
    long time = Long.parseLong(Randoms.randomNumber(3));
    vertx.setTimer(time, l -> {
      completedFuture.complete();
    });
  }
}
