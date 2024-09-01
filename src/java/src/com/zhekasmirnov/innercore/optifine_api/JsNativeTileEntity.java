package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.NativeTileEntity;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;
import com.zhekasmirnov.innercore.optifine_api.codegen.IClassInstance;
import com.zhekasmirnov.innercore.optifine_api.codegen.JsFieldOpti;
import com.zhekasmirnov.innercore.optifine_api.codegen.JsTypesInit;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import org.mozilla.javascript.*;

public class JsNativeTileEntity
    extends ScriptableObject implements Wrapper, IClassInstance {
    private static int wrapInt(Object[] args, int index) {
        try {
            return ((Number) args[index]).intValue();
        } catch (ClassCastException | ArrayIndexOutOfBoundsException
            | NullPointerException e) {
            return 0;
        }
    }

    private static double wrapDouble(Object[] args, int index) {
        try {
            return ((Number) args[index]).doubleValue();
        } catch (ClassCastException | ArrayIndexOutOfBoundsException
            | NullPointerException e) {
            return 0.0;
        }
    }

    private static boolean wrapBoolean(Object[] args, int index) {
        try {
            return ((Boolean) args[index]).booleanValue();
        } catch (ClassCastException | ArrayIndexOutOfBoundsException
            | NullPointerException e) {
            return false;
        }
    }

    private static long wrapLong(Object[] args, int index) {
        try {
            return ((Number) args[index]).longValue();
        } catch (ClassCastException | ArrayIndexOutOfBoundsException
            | NullPointerException e) {
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
        } catch (ClassCastException | ArrayIndexOutOfBoundsException
            | NullPointerException e) {
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
        } catch (ClassCastException | ArrayIndexOutOfBoundsException
            | NullPointerException e) {
            return value == null ? "null" : value.toString();
        }
    }

    private static final ConcurrentHashMap<String, JsFieldOpti> fields =
        new ConcurrentHashMap<>();

    static {
        Class<?> super_clazz =
            com.zhekasmirnov.innercore.api.NativeTileEntity.class;
        while (super_clazz != null) {
            Method[] methods = super_clazz.getMethods();
            for (Field field : super_clazz.getDeclaredFields()) {
                boolean added = true;
                for (Method method : methods)
                    if (method.getName().equals(field.getName())) {
                        added = false;
                        break;
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
        JsTypesInit.register(new JsTypesInit.DefJsClass(
            NativeTileEntity.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsNativeTileEntity((NativeTileEntity) arg);
                }
            }));
    }
    private final NativeTileEntity self;
    public JsNativeTileEntity(NativeTileEntity self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("getCompoundTag", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeCompoundTag(self.getCompoundTag());
            }
        });
        put("notifyAll", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.notifyAll();
                return Undefined.instance;
            }
        });
        put("notify", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.notify();
                return Undefined.instance;
            }
        });
        put("setCompoundTag", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setCompoundTag(wrapObject(args, 0,
                    com.zhekasmirnov.innercore.api.nbt.NativeCompoundTag
                        .class));
                return Undefined.instance;
            }
        });
        put("setSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 2) {
                    self.setSlot(wrapInt(args, 0),
                        wrapObject(args, 1,
                            com.zhekasmirnov.innercore.api.NativeItemInstance
                                .class));
                    return Undefined.instance;
                }
                if (args.length == 4) {
                    self.setSlot(wrapInt(args, 0), wrapInt(args, 1),
                        wrapInt(args, 2), wrapInt(args, 3));
                    return Undefined.instance;
                }
                if (args.length == 5) {
                    self.setSlot(wrapInt(args, 0), wrapInt(args, 1),
                        wrapInt(args, 2), wrapInt(args, 3),
                        wrapObject(args, 4, java.lang.Object.class));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstance(self.getSlot(wrapInt(args, 0)));
            }
        });
        put("getSize", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getSize();
            }
        });
        put("getType", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getType();
            }
        });
        put("hashCode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.hashCode();
            }
        });
        put("equals", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.equals(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.toString();
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
            return com.zhekasmirnov.innercore.api.mod.adaptedscript
                .AdaptedScriptAPI.PlayerActor.class.isInstance(
                    ((Wrapper) value).unwrap());
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
        final ConcurrentHashMap<String, JsFieldOpti> fields =
            new ConcurrentHashMap<>();
        final ScriptableObject global = new BaseScriptableClass() {
            @Override
            public Scriptable construct(
                Context ctx, Scriptable scope, Object[] args) {
                return new JsNativeTileEntity(
                    new com.zhekasmirnov.innercore.api.NativeTileEntity(
                        wrapLong(args, 0)));
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

        global.put("setSlot", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeTileEntity.setSlot(wrapLong(args, 0), wrapInt(args, 1),
                    wrapInt(args, 2), wrapInt(args, 3), wrapInt(args, 4),
                    wrapLong(args, 5));
                return Undefined.instance;
            }
        });
        global.put("getSlot", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeTileEntity.getSlot(
                    wrapLong(args, 0), wrapInt(args, 1));
            }
        });
        global.put("setSlot2", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeTileEntity.setSlot2(
                    wrapLong(args, 0), wrapInt(args, 1), wrapLong(args, 2));
                return Undefined.instance;
            }
        });
        global.put("getSize", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeTileEntity.getSize(wrapLong(args, 0));
            }
        });
        global.put("nativeFinalize", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeTileEntity.nativeFinalize(wrapLong(args, 0));
                return Undefined.instance;
            }
        });
        global.put("getCompoundTag", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeTileEntity.getCompoundTag(wrapLong(args, 0));
            }
        });
        global.put("getType", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeTileEntity.getType(wrapLong(args, 0));
            }
        });
        global.put("getTileEntity", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeTileEntity(NativeTileEntity.getTileEntity(
                    wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2)));
            }
        });
        global.put("getInWorld", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeTileEntity.getInWorld(
                    wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        global.put("setCompoundTag", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeTileEntity.setCompoundTag(
                    wrapLong(args, 0), wrapLong(args, 1));
                return Undefined.instance;
            }
        });
        scope.put("NativeTileEntity", scope, global);
    }
}