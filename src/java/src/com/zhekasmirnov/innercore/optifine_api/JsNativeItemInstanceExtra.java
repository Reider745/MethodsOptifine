package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.NativeItemInstanceExtra;
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

public class JsNativeItemInstanceExtra
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
            com.zhekasmirnov.innercore.api.NativeItemInstanceExtra.class;
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
            NativeItemInstanceExtra.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsNativeItemInstanceExtra(
                        (NativeItemInstanceExtra) arg);
                }
            }));
    }
    private final NativeItemInstanceExtra self;
    public JsNativeItemInstanceExtra(NativeItemInstanceExtra self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("asJson", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.asJson();
            }
        });
        put("getCompoundTag", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeCompoundTag(self.getCompoundTag());
            }
        });
        put("getEnchants", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getEnchants();
            }
        });
        put("removeEnchant", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.removeEnchant(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("setCustomName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setCustomName(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("getEnchantName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return self.getEnchantName(wrapInt(args, 0));
                }
                if (args.length == 2) {
                    return self.getEnchantName(
                        wrapInt(args, 0), wrapInt(args, 1));
                }
                throw new RuntimeException("Not method...");
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
        put("getId", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getId();
            }
        });
        put("getRawEnchants", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getRawEnchants();
            }
        });
        put("getFloat", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return self.getFloat(wrapString(args, 0));
                }
                if (args.length == 2) {
                    return self.getFloat(
                        wrapString(args, 0), wrapDouble(args, 1));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("putLong", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstanceExtra(
                    self.putLong(wrapString(args, 0), wrapLong(args, 1)));
            }
        });
        put("isFinalizableInstance", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isFinalizableInstance();
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
        put("addEnchant", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.addEnchant(wrapInt(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        put("hashCode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.hashCode();
            }
        });
        put("setId", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setId(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("getBoolean", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return self.getBoolean(wrapString(args, 0));
                }
                if (args.length == 2) {
                    return self.getBoolean(
                        wrapString(args, 0), wrapBoolean(args, 1));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("copy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstanceExtra(self.copy());
            }
        });
        put("getAllEnchantNames", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getAllEnchantNames();
            }
        });
        put("putBoolean", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstanceExtra(
                    self.putBoolean(wrapString(args, 0), wrapBoolean(args, 1)));
            }
        });
        put("getCustomName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getCustomName();
            }
        });
        put("putInt", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstanceExtra(
                    self.putInt(wrapString(args, 0), wrapInt(args, 1)));
            }
        });
        put("isEnchanted", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isEnchanted();
            }
        });
        put("putString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstanceExtra(
                    self.putString(wrapString(args, 0), wrapString(args, 1)));
            }
        });
        put("isEmpty", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isEmpty();
            }
        });
        put("getString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return self.getString(wrapString(args, 0));
                }
                if (args.length == 2) {
                    return self.getString(
                        wrapString(args, 0), wrapString(args, 1));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getAllCustomData", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getAllCustomData();
            }
        });
        put("getEnchantLevel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getEnchantLevel(wrapInt(args, 0));
            }
        });
        put("getLong", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return self.getLong(wrapString(args, 0));
                }
                if (args.length == 2) {
                    return self.getLong(wrapString(args, 0), wrapLong(args, 1));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("applyTo", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.applyTo(wrapObject(
                    args, 0, org.mozilla.javascript.Scriptable.class));
                return Undefined.instance;
            }
        });
        put("getInt", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return self.getInt(wrapString(args, 0));
                }
                if (args.length == 2) {
                    return self.getInt(wrapString(args, 0), wrapInt(args, 1));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getSerializable", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getSerializable(wrapString(args, 0));
            }
        });
        put("getValue", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getValue();
            }
        });
        put("putFloat", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstanceExtra(
                    self.putFloat(wrapString(args, 0), wrapDouble(args, 1)));
            }
        });
        put("removeAllEnchants", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.removeAllEnchants();
                return Undefined.instance;
            }
        });
        put("putSerializable", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstanceExtra(
                    self.putSerializable(wrapString(args, 0),
                        wrapObject(args, 1, java.lang.Object.class)));
            }
        });
        put("setAllCustomData", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setAllCustomData(wrapString(args, 0));
                return Undefined.instance;
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
        put("getEnchantCount", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getEnchantCount();
            }
        });
        put("removeCustomData", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.removeCustomData();
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
                if (args.length == 0) {
                    return new JsNativeItemInstanceExtra(
                        new com.zhekasmirnov.innercore.api
                            .NativeItemInstanceExtra());
                }
                if (args.length == 1) {
                    return new JsNativeItemInstanceExtra(
                        new com.zhekasmirnov.innercore.api
                            .NativeItemInstanceExtra(wrapObject(args, 0,
                                com.zhekasmirnov.innercore.api
                                    .NativeItemInstanceExtra.class)));
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

        global.put("cloneExtra", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstanceExtra(
                    NativeItemInstanceExtra.cloneExtra(wrapObject(args, 0,
                        com.zhekasmirnov.innercore.api.NativeItemInstanceExtra
                            .class)));
            }
        });
        global.put("unwrapValue", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItemInstanceExtra.unwrapValue(
                    wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("unwrapObject", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstanceExtra(
                    NativeItemInstanceExtra.unwrapObject(
                        wrapObject(args, 0, java.lang.Object.class)));
            }
        });
        global.put("getValueOrNullPtr", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItemInstanceExtra.getValueOrNullPtr(
                    wrapObject(args, 0,
                        com.zhekasmirnov.innercore.api.NativeItemInstanceExtra
                            .class));
            }
        });
        global.put("constructClone", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItemInstanceExtra.constructClone(
                    wrapLong(args, 0));
            }
        });
        global.put("fromJson", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstanceExtra(
                    NativeItemInstanceExtra.fromJson(
                        wrapObject(args, 0, org.json.JSONObject.class)));
            }
        });
        global.put("getExtraOrNull", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemInstanceExtra(
                    NativeItemInstanceExtra.getExtraOrNull(wrapLong(args, 0)));
            }
        });
        global.put("initSaverId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItemInstanceExtra.initSaverId();
                return Undefined.instance;
            }
        });
        scope.put("NativeItemInstanceExtra", scope, global);
    }
}