package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import org.mozilla.javascript.*;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import com.zhekasmirnov.innercore.optifine_api.codegen.JsFieldOpti;
import com.zhekasmirnov.innercore.optifine_api.codegen.IClassInstance;
import com.zhekasmirnov.innercore.optifine_api.codegen.JsTypesInit;
import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;

public class JsBlockState extends ScriptableObject implements Wrapper, IClassInstance {

    private static int wrapInt(Object[] args, int index) {
        try {
            return ((Number) args[index]).intValue();
        } catch (ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }

    private static double wrapDouble(Object[] args, int index) {
        try {
            return ((Number) args[index]).doubleValue();
        } catch (ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            return 0.0;
        }
    }

    private static boolean wrapBoolean(Object[] args, int index) {
        try {
            return ((Boolean) args[index]).booleanValue();
        } catch (ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            return false;
        }
    }

    private static long wrapLong(Object[] args, int index) {
        try {
            return ((Number) args[index]).longValue();
        } catch (ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            return 0L;
        }
    }

    private static <T> T wrapObject(Object[] args, int index, Class<T> clazz) {
        try {
            Object object = args[index];
            while (object instanceof Wrapper) {
                object = ((Wrapper) object).unwrap();
            }
            return (T) Context.jsToJava(object, clazz);
        } catch (ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    private static String wrapString(Object[] args, int index) {
        Object value = args[index];
        try {
            if (value instanceof NativeJavaObject) {
                value = ((NativeJavaObject) value).unwrap();
            }
            return ((CharSequence) value).toString();
        } catch (ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            return value == null ? "null" : value.toString();
        }
    }

    private static final ConcurrentHashMap<String, JsFieldOpti> fields = new ConcurrentHashMap<>();

    static {
        Class<?> super_clazz = com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState.class;
        while (super_clazz != null) {
            Method[] methods = super_clazz.getMethods();
            for (Field field : super_clazz.getDeclaredFields()) {
                boolean added = true;
                for (Method method : methods) {
                    if (method.getName().equals(field.getName())) {
                        added = false;
                        break;
                    }
                }
                if (added) {
                    field.setAccessible(true);
                    fields.put(field.getName(), new JsFieldOpti(field));
                }
            }
            super_clazz = super_clazz.getSuperclass();
        }

    }

    public static void init() {
        JsTypesInit.register(new JsTypesInit.DefJsClass(BlockState.class, new JsTypesInit.IBuilderClass() {
            @Override
            public Object call(Object arg) {
                return new JsBlockState((BlockState) arg);
            }
        }));
    }
    private final BlockState self;

    public JsBlockState(BlockState self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("addStates", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsBlockState(self.addStates(wrapObject(args, 0, org.mozilla.javascript.Scriptable.class)));
            }
        });
        put("notifyAll", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.notifyAll();
                return Undefined.instance;
            }
        });
        put("getId", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getId();
            }
        });
        put("hasState", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.hasState(wrapInt(args, 0));
            }
        });
        put("getNamedStatesScriptable", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getNamedStatesScriptable();
            }
        });
        put("notify", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.notify();
                return Undefined.instance;
            }
        });
        put("getNamedStates", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getNamedStates();
            }
        });
        put("getStatesScriptable", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getStatesScriptable();
            }
        });
        put("getState", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getState(wrapInt(args, 0));
            }
        });
        put("isValidState", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.isValidState();
            }
        });
        put("hashCode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.hashCode();
            }
        });
        put("equals", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.equals(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        put("getRuntimeId", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getRuntimeId();
            }
        });
        put("addStatesMap", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsBlockState(self.addStatesMap(wrapObject(args, 0, java.util.Map.class)));
            }
        });
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.toString();
            }
        });
        put("getStates", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getStates();
            }
        });
        put("addState", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsBlockState(self.addState(wrapInt(args, 0), wrapInt(args, 1)));
            }
        });
        put("getData", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getData();
            }
        });

    }

    @Override
    public String getClassName() {
        return "Object";
    }

    @Override
    public Object unwrap() {
        return this.self;
    }

    @Override
    public Object getSelf() {
        return self;
    }

    @Override
    public boolean hasInstance(Scriptable value) {
        if (value instanceof Wrapper) {
            return com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.PlayerActor.class.isInstance(((Wrapper) value).unwrap());
        }
        return false;
    }

    @Override
    public void put(String name, Scriptable start, Object value) {
        final JsFieldOpti field = fields.get(name);
        if (field != null) {
            try {
                field.set(self, value);
            } catch (IllegalAccessException e) {
            }
        }
        super.put(name, start, value);
    }

    @Override
    public Object get(String name, Scriptable start) {
        final JsFieldOpti field = fields.get(name);
        if (field != null) {
            try {
                return field.get(self, this);
            } catch (IllegalAccessException e) {
            }
        }
        return super.get(name, start);
    }

    public static void inject(ScriptableObject scope) {
        final ConcurrentHashMap<String, JsFieldOpti> fields = new ConcurrentHashMap<>();
        final ScriptableObject global = new BaseScriptableClass() {
            @Override
            public Scriptable construct(Context ctx, Scriptable scope, Object[] args) {
                if (args.length == 1) {
                    return new JsBlockState(new com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState(wrapLong(args, 0)));
                }
                if (args.length == 2) {
                    return new JsBlockState(new com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState(wrapInt(args, 0), wrapObject(args, 1, org.mozilla.javascript.Scriptable.class)));
                }
                throw new RuntimeException("Not method...");
            }

            @Override
            public String getClassName() {
                return "JavaClass";
            }

            @Override
            public void put(String name, Scriptable start, Object value) {
                final JsFieldOpti field = fields.get(name);
                if (field != null) {
                    try {
                        field.set(null, value);
                    } catch (IllegalAccessException e) {
                    }
                }
                super.put(name, start, value);
            }

            @Override
            public Object get(String name, Scriptable start) {
                final JsFieldOpti field = fields.get(name);
                if (field != null) {
                    try {
                        return field.get(null, this);
                    } catch (IllegalAccessException e) {
                    }
                }
                return super.get(name, start);
            }

        };

        global.put("getIdFromRuntimeId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return BlockState.getIdFromRuntimeId(wrapInt(args, 0));
            }
        });
        global.put("runtimeIdByStates", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return BlockState.runtimeIdByStates(wrapInt(args, 0), wrapObject(args, 1, int[].class));
            }
        });
        global.put("getDataFromRuntimeId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return BlockState.getDataFromRuntimeId(wrapInt(args, 0));
            }
        });
        scope.put("BlockState", scope, global);
    }

}
