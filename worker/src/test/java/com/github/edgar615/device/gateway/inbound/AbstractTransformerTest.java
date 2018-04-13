package com.github.edgar615.device.gateway.inbound;

import com.github.edgar615.device.gateway.ScriptLogger;
import jdk.internal.dynalink.beans.StaticClass;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Consumer;
import javax.script.Bindings;
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
  protected MessageTransformer compile(String scriptPath) throws IOException, ScriptException {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("JavaScript");
    ScriptContext sc = engine.getContext();
//    https://stackoverflow.com/questions/39578020/how-to-write-to-nashorn-error-stream
    sc.setAttribute("stderr", // name is 'stderr'
                                     (Consumer<String>) str -> { // Object is a Consumer<String>
                                       try {
//                                         Bindings bindings = sc.getBindings(ScriptContext
//                                                                                   .ENGINE_SCOPE);
//                                         for (String key: bindings.keySet()) {
//                                           System.out.println(key);
//                                           if (bindings.get(key) instanceof StaticClass) {
//                                             StaticClass staticClass =
//                                                     (StaticClass) bindings.get(key);
//                                           }
//                                           System.out.println(bindings.get(key).getClass());
//                                         }

                                         Writer err = sc.getErrorWriter();
                                         err.write(str);
                                         err.flush();
                                       } catch (Exception e) {
                                         throw new Error(e);
                                       }
                                     },
                                     ScriptContext.ENGINE_SCOPE
// i.e. don't share with other engines
    );
    sc.setAttribute("logger", new ScriptLogger(), ScriptContext.ENGINE_SCOPE);
    sc.setErrorWriter(new PrintWriter(new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        System.err.write(b);
      }
    }));
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
