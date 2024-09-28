package com.zhekasmirnov.innercore.optifine_api.codegen;

import org.mozilla.javascript.Scriptable;

import java.lang.reflect.Field;

public class JsFieldOpti {
    private final Field field;
    private final Class<?> type;

    public JsFieldOpti(Field field){
        this.field = field;
        type = field.getType();
    }

    public void set(Object self, Object value) throws IllegalAccessException {
        final Class<?> clazz = value.getClass();

        if(clazz == type){
            field.set(self, value);
            return;
        }else if(value instanceof Number && Number.class.isAssignableFrom(type)){
            final Number value_ = (Number) value;
            switch (type.getName()){
                case "int":
                    value = value_.intValue();
                    break;
                case "double":
                    value = value_.doubleValue();
                    break;
                case "float":
                    value = value_.floatValue();
                    break;
                case "long":
                    value = value_.longValue();
                    break;
                case "short":
                    value = value_.shortValue();
                    break;
            }
            field.set(self, value);
            return;
        }else if(value instanceof CharSequence && String.class.isAssignableFrom(type)){
            field.set(self, ((CharSequence) value).toString());
            return;
        }

        field.set(self, JsTypesInit.javaFromJs(type, value));
    }

    public Object get(Object self, Scriptable scope) throws IllegalAccessException {
        return JsTypesInit.jsFromJava(type, field.get(self), scope);
    }
}
