package com.github.edgar615.device.gateway.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  private static final Logger LOGGER = LoggerFactory.getLogger(ScriptCompiler.class);

  public static MessageTransformer compile(String script) throws IOException,
          ScriptException {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("JavaScript");
    if (!(engine instanceof Invocable)) {
      LOGGER.error("Interface implementation in script"
                   + "is not supported.");
      return null;
    }
    if (!(engine instanceof Compilable)) {
      LOGGER.error("Script compilation not supported.");
      return null;
    }

    Compilable comp = (Compilable) engine;
    CompiledScript cScript = comp.compile(script);
    Invocable inv = (Invocable) engine;
    cScript.eval();
    MessageTransformer transformer = inv.getInterface(MessageTransformer.class);
    if (transformer == null) {
      LOGGER.error("MessageTransformer interface "
                   + "implementation not found.");
      return null;
    }
    return transformer;
  }
}
