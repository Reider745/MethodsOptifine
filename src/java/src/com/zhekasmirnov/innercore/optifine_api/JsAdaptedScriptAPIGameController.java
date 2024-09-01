package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.GameController;
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

public class JsAdaptedScriptAPIGameController extends ScriptableObject implements Wrapper, IClassInstance {

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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.GameController.class;
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
        JsTypesInit.register(new JsTypesInit.DefJsClass(GameController.class, new JsTypesInit.IBuilderClass() {
            @Override
            public Object call(Object arg) {
                return new JsAdaptedScriptAPIGameController((GameController) arg);
            }
        }));
    }
    private final GameController self;

    public JsAdaptedScriptAPIGameController(GameController self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getClass();
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
        put("notifyAll", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.notifyAll();
                return Undefined.instance;
            }
        });
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.toString();
            }
        });
        put("notify", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.notify();
                return Undefined.instance;
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
                return new JsAdaptedScriptAPIGameController(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.GameController());
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

        global.put("continueBuildBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                GameController.continueBuildBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3));
                return Undefined.instance;
            }
        });
        global.put("stopBuildBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                GameController.stopBuildBlock();
                return Undefined.instance;
            }
        });
        global.put("continueDestroyBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return GameController.continueDestroyBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3));
            }
        });
        global.put("buildBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                GameController.buildBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3));
                return Undefined.instance;
            }
        });
        global.put("interact", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 4) {
                    GameController.interact(wrapObject(args, 0, java.lang.Object.class), wrapDouble(args, 1), wrapDouble(args, 2), wrapDouble(args, 3));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        global.put("releaseUsingItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                GameController.releaseUsingItem();
                return Undefined.instance;
            }
        });
        global.put("startDestroyBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return GameController.startDestroyBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3));
            }
        });
        global.put("attack", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    GameController.attack(wrapObject(args, 0, java.lang.Object.class));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        global.put("stopDestroyBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                GameController.stopDestroyBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        global.put("destroyBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                GameController.destroyBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3));
                return Undefined.instance;
            }
        });
        global.put("startBuildBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                GameController.startBuildBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3));
                return Undefined.instance;
            }
        });
        global.put("onItemUse", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                GameController.onItemUse(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapInt(args, 3));
                return Undefined.instance;
            }
        });
        global.put("useItemOn", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                GameController.useItemOn(wrapLong(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), wrapInt(args, 4), wrapLong(args, 5));
                return Undefined.instance;
            }
        });
        scope.put("AdaptedScriptAPIGameController", scope, global);
    }

}
