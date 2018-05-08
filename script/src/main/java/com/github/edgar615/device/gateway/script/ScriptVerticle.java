package com.github.edgar615.device.gateway.script;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;

import com.github.edgar615.device.gateway.core.Consts;
import com.github.edgar615.util.vertx.eventbus.EventbusUtils;
import com.github.edgar615.util.vertx.jdbc.PersistentService;
import com.github.edgar615.util.vertx.jdbc.dataobj.FindExample;
import com.github.edgar615.util.vertx.jdbc.table.Table;
import com.github.edgar615.util.vertx.jdbc.table.TableRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.serviceproxy.ServiceProxyBuilder;

import java.util.List;

/**
 * 这个Verticle要在JdbcVerticle启动之后启动.
 *
 * @author Edgar  Date 2018/4/16
 */
public class ScriptVerticle extends AbstractVerticle {

  private AsyncSQLClient sqlClient;

  public static String resourceName(String name) {
    return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name);
  }

  @Override
  public void start() throws Exception {
    ServiceProxyBuilder
            builder = new ServiceProxyBuilder(vertx).setAddress("__com.github.edgar615.util.vertx.jdbc");
    PersistentService persistentService = builder.build(PersistentService.class);
    vertx.eventBus().consumer(Consts.LOCAL_DEVICE_LOG_ADDRESS,
                              LogMessageConsumer.create(persistentService));
    vertx.eventBus().consumer(Consts.LOCAL_SCRIPT_ADD_ADDRESS,
                              new AddScriptMessageConsumer(vertx, new JsonObject()));
    vertx.eventBus().consumer(Consts.LOCAL_SCRIPT_UPDATE_ADDRESS,
                              new UpdateScriptMessageConsumer(vertx, new JsonObject()));
    vertx.eventBus().consumer(Consts.LOCAL_SCRIPT_DELETE_ADDRESS,
                              new DeleteScriptMessageConsumer(vertx, new JsonObject()));
    vertx.eventBus().consumer(Consts.LOCAL_SCRIPT_LOAD_ADDRESS,
                              new LoadScriptMessageConsumer(vertx, new JsonObject()));
    JsonObject data = new JsonObject()
            .put("resource", "product_script")
            .put("limit", 1);
    JsonObject jsonObject = new JsonObject()
            .put("data", data)
            .put("publishAddress", Consts.LOCAL_SCRIPT_LOAD_ADDRESS);
    vertx.eventBus().send("__com.github.edgar615.util.vertx.jdbc.loadAll", jsonObject);


  }

  private void startWeb(PersistentService persistentService) {

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    List<Table> tables = TableRegistry.instance().tables();
    for (Table table : tables) {
      String tableName = table.getName();
      router.get("/" + resourceName(tableName)).handler(rc -> {
        String query = rc.request().getParam("query");
        String start = rc.request().getParam("start");
        String limit = rc.request().getParam("limit");
        FindExample findExample = new FindExample();
        findExample.setQuery(query);
        findExample.setResource(tableName);
        if (!Strings.isNullOrEmpty(start)) {
          findExample.setStart(Integer.parseInt(start));
        }
        if (!Strings.isNullOrEmpty(limit)) {
          findExample.setStart(Integer.parseInt(limit));
        }
        persistentService.findByExample(findExample, ar -> {
          if (ar.failed()) {
            rc.response().end(ar.cause().getMessage());
            return;
          }
          rc.response().end(new JsonArray(ar.result()).encode());
        });
      });
    }
    vertx.createHttpServer().requestHandler(router::accept)
            .listen(9000);
  }

}
