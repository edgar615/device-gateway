package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.core.MessageTransformer;
import com.github.edgar615.device.gateway.script.ScriptVerticle;
import com.github.edgar615.util.vertx.jdbc.JdbcVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.awaitility.Awaitility;
import org.junit.Before;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Edgar on 2018/4/11.
 *
 * @author Edgar  Date 2018/4/11
 */
public class AbstractTransformerTest {

  protected Vertx vertx;

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    JsonObject
            mySQLConfig = new JsonObject().put("host", "test.ihorn.com.cn").put
            ("username", "admin").put("password", "csst").put("database", "om_new");
    JsonObject persistentConfig = new JsonObject()
            .put("address", "database-service-address")
            .put("tables", new JsonArray().add("device_script").add("device_log"));
    DeploymentOptions options = new DeploymentOptions()
            .setConfig(new JsonObject().put("mysql", mySQLConfig)
                               .put("persistent", persistentConfig));
    AtomicBoolean check1 = new AtomicBoolean();
    vertx.deployVerticle(JdbcVerticle.class, options, ar -> check1.set(true));
    Awaitility.await().until(() -> check1.get());

    AtomicBoolean check2 = new AtomicBoolean();
    vertx.deployVerticle(ScriptVerticle.class, options, ar -> check2.set(true));
    Awaitility.await().until(() -> check2.get());

  }

  protected MessageTransformer compile(Vertx vertx, String scriptPath) throws IOException,
          ScriptException {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("JavaScript");
    ScriptContext sc = engine.getContext();
//    https://stackoverflow.com/questions/39578020/how-to-write-to-nashorn-error-stream
//    sc.setAttribute("stderr", // name is 'stderr'
//                                     (Consumer<String>) str -> { // Object is a Consumer<String>
//                                       try {
//                                         Writer err = sc.getErrorWriter();
//                                         err.write(str);
//                                         err.flush();
//                                       } catch (Exception e) {
//                                         throw new Error(e);
//                                       }
//                                     },
//                                     ScriptContext.ENGINE_SCOPE
//// i.e. don't share with other engines
//    );
//    sc.setAttribute("logger", new ScriptLogger(vertx), ScriptContext.ENGINE_SCOPE);
//    sc.setErrorWriter(new PrintWriter(new OutputStream() {
//      @Override
//      public void write(int b) throws IOException {
//        System.err.write(b);
//      }
//    }));
    if (!(engine instanceof Invocable)) {
      System.out.println("Interface implementation in script"
                         + "is not supported.");
      return null;
    }
    if (!(engine instanceof Compilable)) {
      System.out.println("Script compilation not supported.");
      return null;
    }

    Reader scriptReader = Files.newBufferedReader(Paths.get(scriptPath));

    Compilable comp = (Compilable) engine;
    CompiledScript cScript = comp.compile(scriptReader);
    Invocable inv = (Invocable) engine;
    cScript.eval();
    MessageTransformer transformer = inv.getInterface(MessageTransformer.class);
    if (transformer == null) {
      System.err.println("MessageTransformer interface "
                         + "implementation not found.");
      return null;
    }
    return transformer;
  }
}
