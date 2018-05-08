package com.github.edgar615.device.gateway.worker;

import com.github.edgar615.device.gateway.core.MessageUtils;
import com.github.edgar615.device.gateway.core.SequentialQueue;
import com.github.edgar615.device.gateway.core.SequentialQueueHelper;
import com.github.edgar615.util.event.Event;
import com.github.edgar615.util.log.Log;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Edgar on 2018/3/12.
 *
 * @author Edgar  Date 2018/3/12
 */
public class WorkerVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(WorkerVerticle.class);

  private SequentialQueue queue;

  private AtomicBoolean running = new AtomicBoolean(true);

  private EventHandler eventHandler;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    eventHandler = new EventHandler(vertx);
    queue = SequentialQueueHelper.createShared(vertx, new JsonObject());
    //每个2秒检查一下是否有待处理的事件
    vertx.setPeriodic(2000l, l -> {
      //再次开始扫描队列
      handleEventInOrder();
    });
  }

  private void handleEventInOrder() {
    Event event;
      while ((event = queue.dequeue()) != null) {
        //处理event
        Future<Void> future = Future.future();
        handle(event, future);
        final Event finalEvent = event;
        future.setHandler(ar -> {
          if (ar.failed()) {
            Log.create(LOGGER)
                    .setTraceId(finalEvent.head().id())
                    .setLogType("device-gateway")
                    .setEvent("failed")
                    .setThrowable(ar.cause())
                    .setMessage("Error occcured during handle event")
                    .error();
          } else {
            Log.create(LOGGER)
                    .setTraceId(finalEvent.head().id())
                    .setLogType("device-gateway")
                    .setEvent("completed")
                    .info();
          }
          queue.complete(finalEvent);
          //尝试将worker的事件处理状态从false修改为true，如果成功说明之前worker没处理事件，开启新一轮处理
          if (running.compareAndSet(false, true)) {
            handleEventInOrder();
          }
        });
      }
  }

  private void handle(Event event, Future<Void> completedFuture) {
    try {
      Map<String, Object> brokerMessage = MessageUtils.createMessage(event);
      if (brokerMessage == null) {
        completedFuture.complete();
      }
      eventHandler.handle(brokerMessage, completedFuture.completer());
    } catch (Exception e) {
      completedFuture.tryFail(e);
    }
  }

}
