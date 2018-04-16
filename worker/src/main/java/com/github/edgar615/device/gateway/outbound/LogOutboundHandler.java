//package com.github.edgar615.device.gateway.outbound;
//
//import com.github.edgar615.device.gateway.core.Consts;
//import com.github.edgar615.device.gateway.core.MessageType;
//import io.vertx.core.Future;
//import io.vertx.core.Vertx;
//import io.vertx.core.json.JsonObject;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
///**
// * Created by Edgar on 2018/4/10.
// *
// * @author Edgar  Date 2018/4/10
// */
//public class LogOutboundHandler implements OutboundHandler {
//
//  @Override
//  public void handle(Vertx vertx, Map<String, Object> input, List<Map<String, Object>> output,
//                     Future<Void> completeFuture) {
//    List<Map<String, Object>> mapList = output.stream()
//            .filter(m -> MessageType.LOG.equals(m.get("type")))
//            .collect(Collectors.toList());
//    if (mapList.isEmpty()) {
//      completeFuture.complete();
//      return;
//    }
//    //不关心日志写成功与否
//    //todo 日志需要记录traceId, 设备ID，类型，时间
//    String traceId = (String) input.get("traceId");
//    String deviceId = (String) input.get("deviceId");
//    for (Map<String, Object> log : mapList) {
//      String command = (String) log.get("command");
//      Map<String, Object> data = (Map<String, Object>) log.get("data");
//      if (data == null) {
//        data = new HashMap<>();
//      }
//      String id = traceId + "." + UUID.randomUUID().toString();
//
//      JsonObject jsonObject = new JsonObject()
//              .put("traceId", id)
//              .put("deviceId", deviceId)
//              .put("type", command)
//              .put("data", data);
//      vertx.eventBus().send(Consts.LOCAL_DEVICE_LOG_ADDRESS, jsonObject);
//    }
//    completeFuture.complete();
//  }
//}
