package com.github.edgar615.device.gateway.script;

import com.github.edgar615.util.base.StringUtils;
import com.github.edgar615.util.db.SQLBindings;
import com.github.edgar615.util.db.SqlBuilder;
import com.github.edgar615.util.search.Example;
import io.vertx.core.json.JsonObject;
import scala.Int;

import java.time.Instant;

/**
 * Created by Edgar on 2018/4/18.
 *
 * @author Edgar  Date 2018/4/18
 */
public class ScriptUtils {
  public static DeviceScript fromJson(JsonObject jsonObject) {
    String productType = jsonObject.getString("productType");
    String messageType = jsonObject.getString("messageType");
    String command = jsonObject.getString("command");
    String scriptContent = jsonObject.getString("scriptContent");
    Integer state = jsonObject.getInteger("state");
    DeviceScript deviceScript = new DeviceScript();
    deviceScript.setProductType(productType);
    deviceScript.setMessageType(messageType);
    deviceScript.setCommand(command);
    deviceScript.setScriptContent(scriptContent);
    deviceScript.setAddOn((int) Instant.now().getEpochSecond());
    deviceScript.setLastModifyOn((int) Instant.now().getEpochSecond());
    deviceScript.setState(state);
    return deviceScript;
  }

  public static Example createExample(JsonObject jsonObject) {
    String productType = jsonObject.getString("productType");
    String messageType = jsonObject.getString("messageType");
    String command = jsonObject.getString("command");
    Integer state = jsonObject.getInteger("state");
    Example example = Example.create();
    example.equalsTo("productType", productType)
            .equalsTo("messageType", messageType)
            .equalsTo("command", command)
            .equalsTo("state", state);
    return example;
  }

  public static SQLBindings createSelectSql(Example example) {
    SQLBindings sqlBindings = SqlBuilder.whereSql(example.criteria());
    String sql = "select *  from device_script";
    if (!example.criteria().isEmpty()) {
      sql += " where " + sqlBindings.sql();
    } else {
      sql += "  " + sqlBindings.sql();
    }
    if (!example.orderBy().isEmpty()) {
      sql += SqlBuilder.orderSql(example.orderBy());
    }
    return SQLBindings.create(sql, sqlBindings.bindings());
  }
}
