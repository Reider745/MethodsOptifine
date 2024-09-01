package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.apparatus.adapter.innercore.game.item.ItemStack;
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

public class JsItemStack
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
        Class<?> super_clazz = com.zhekasmirnov.apparatus.adapter.innercore.game
                                   .item.ItemStack.class;
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
            ItemStack.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsItemStack((ItemStack) arg);
                }
            }));
    }
    private final ItemStack self;
    public JsItemStack(ItemStack self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("getMaxStackSize", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getMaxStackSize();
            }
        });
        put("asScriptable", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.asScriptable();
            }
        });
        put("asJson", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.asJson();
            }
        });
        put("isGlint", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isGlint();
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
        put("isEmpty", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isEmpty();
            }
        });
        put("getItemModel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(self.getItemModel());
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
        put("getItemName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getItemName();
            }
        });
        put("getExtraPtr", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getExtraPtr();
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
        put("getMaxDamage", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getMaxDamage();
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
                    return new JsItemStack(
                        new com.zhekasmirnov.apparatus.adapter.innercore.game
                            .item.ItemStack());
                }
                if (args.length == 1) {
                    return new JsItemStack(
                        new com.zhekasmirnov.apparatus.adapter.innercore.game
                            .item.ItemStack(wrapObject(args, 0,
                                org.mozilla.javascript.ScriptableObject
                                    .class)));
                }
                if (args.length == 3) {
                    return new JsItemStack(
                        new com.zhekasmirnov.apparatus.adapter.innercore.game
                            .item.ItemStack(wrapInt(args, 0), wrapInt(args, 1),
                                wrapInt(args, 2)));
                }
                if (args.length == 4) {
                    return new JsItemStack(
                        new com.zhekasmirnov.apparatus.adapter.innercore.game
                            .item.ItemStack(wrapInt(args, 0), wrapInt(args, 1),
                                wrapInt(args, 2),
                                wrapObject(args, 3,
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

        global.put("fromPtr", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsItemStack(ItemStack.fromPtr(wrapLong(args, 0)));
            }
        });
        global.put("parse", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsItemStack(ItemStack.parse(
                    wrapObject(args, 0, java.lang.Object.class)));
            }
        });
        scope.put("ItemStack", scope, global);
    }
}