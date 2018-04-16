package com.github.edgar615.device.gateway.core;

import java.io.IOException;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Edgar on 2018/4/16.
 *
 * @author Edgar  Date 2018/4/16
 */
public class ScriptCompiler {
  public static MessageTransformer compile(String script) throws IOException,
          ScriptException {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("JavaScript");
    if (!(engine instanceof Invocable)) {
      System.out.println("Interface implementation in script"
                         + "is not supported.");
      return null;
    }
    if (!(engine instanceof Compilable)) {
      System.out.println("Script compilation not supported.");
      return null;
    }

    Compilable comp = (Compilable) engine;
    CompiledScript cScript = comp.compile(script);
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
