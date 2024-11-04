package com.zhekasmirnov.innercore.optifine_api.codegen;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.HashMap;

public class JsTypesInit {
    public interface IConvertType {
        String getName();
        Object jsFromJava(Object arg, Scriptable scope);
        Object javaFromJs(Object arg, Class<?> clazz);
    }

    private static final IConvertType DEF = new IConvertType() {
        @Override
        public String getName() {
            return "ABOBA!";
        }

        @Override
        public Object jsFromJava(Object arg, Scriptable scope) {
            return Context.javaToJS(arg, scope);
        }

        @Override
        public Object javaFromJs(Object arg, Class<?> clazz) {
            return Context.jsToJava(arg, clazz);
        }
    };

    public interface IBuilderClass {
        Object call(Object arg);
    }

    public static class DefJsClass implements IConvertType {
        private final String name;
        private final IBuilderClass builder;

        public DefJsClass(Class<?> clazz, IBuilderClass builder){
            this.name = clazz.getName();
            this.builder = builder;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object jsFromJava(Object arg, Scriptable scope) {
            return builder.call(arg);
        }

        @Override
        public Object javaFromJs(Object arg, Class<?> clazz) {
            return ((IClassInstance) arg).getSelf();
        }
    }

    private static final HashMap<String, IConvertType> types = new HashMap<>();

    public static void register(IConvertType convertType){
        types.put(convertType.getName(), convertType);
    }

    public static Object jsFromJava(Class<?> clazz, Object arg, Scriptable scope){
        if(arg == null)
            return null;
        return types.getOrDefault(clazz.getName(), DEF).jsFromJava(arg, scope);
    }

    public static Object javaFromJs(Class<?> clazz, Object arg){
        return types.getOrDefault(clazz.getName(), DEF).javaFromJs(arg, clazz);
    }
}
