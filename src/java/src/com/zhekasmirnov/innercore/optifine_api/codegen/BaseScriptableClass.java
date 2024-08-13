package com.zhekasmirnov.innercore.optifine_api.codegen;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public abstract class BaseScriptableClass extends ScriptableObject implements Function {
    @Override
    public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
        return null;
    }
}
