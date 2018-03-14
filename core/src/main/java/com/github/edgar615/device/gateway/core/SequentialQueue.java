package com.github.edgar615.device.gateway.core;

import com.github.edgar615.util.event.Event;
import io.vertx.core.shareddata.Shareable;

import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 从消息服务器读取的消息.
 *
 * @author Edgar  Date 2017/11/3
 */
public class SequentialQueue implements Shareable {
  /**
   * 任务列表
   */
  private final LinkedList<Event> tasks = new LinkedList<>();

  /**
   * 消息注册表，用于确保同一个设备只有一个事件在执行
   */
  private final Set<String> registry = new ConcurrentSkipListSet<>();

  private final IdentificationExtractor extractor;

  private int limit = Integer.MAX_VALUE;

  public SequentialQueue(IdentificationExtractor extractor, int limit) {
    this.extractor = extractor;
    this.limit = limit;
  }

  public SequentialQueue(IdentificationExtractor extractor) {
    this.extractor = extractor;
  }

  /**
   * 取出一个可以执行的任务
   *
   * @return
   */
  public synchronized Event dequeue() throws InterruptedException {
    if (tasks.isEmpty()) {
      return null;
    }
    for (Event event : tasks) {
      String deviceId = extractor.apply(event);
      if (!registry.contains(deviceId)) {
        tasks.remove(event);
        registry.add(deviceId);
        return event;
      }
    }
    return null;
  }

  public synchronized boolean enqueue(Event task) throws InterruptedException {
    if (this.tasks.size() == this.limit) {
      return false;
    }
    tasks.add(task);
    //发出广播，通知工作线程读取消息
    return true;
  }

  public synchronized void complete(Event event) {
    registry.remove(extractor.apply(event));
  }

  public synchronized int size() {
    return tasks.size();
  }
}
