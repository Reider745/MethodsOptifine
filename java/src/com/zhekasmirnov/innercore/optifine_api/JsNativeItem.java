package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.NativeItem;
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

public class JsNativeItem
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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.NativeItem.class;
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
            NativeItem.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsNativeItem((NativeItem) arg);
                }
            }));
    }
    private final NativeItem self;
    public JsNativeItem(NativeItem self) {
        this.self = self;
        put("setUseAnimation", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setUseAnimation(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("setEnchantType", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    self.setEnchantType(wrapInt(args, 0));
                    return Undefined.instance;
                }
                if (args.length == 2) {
                    self.setEnchantType(wrapInt(args, 0), wrapInt(args, 1));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setAllowedInOffhand", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setAllowedInOffhand(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("setMaxDamage", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setMaxDamage(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("setMaxStackSize", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setMaxStackSize(wrapInt(args, 0));
                return Undefined.instance;
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
        put("setProperties", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setProperties(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("setMaxUseDuration", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setMaxUseDuration(wrapInt(args, 0));
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
        put("setStackedByData", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setStackedByData(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("setExplodable", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setExplodable(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("setCreativeCategory", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setCreativeCategory(wrapInt(args, 0));
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
        put("addRepairItems", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.addRepairItems(wrapObject(args, 0, int[].class));
                return Undefined.instance;
            }
        });
        put("setFireResistant", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setFireResistant(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("setEnchantability", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setEnchantability(wrapInt(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        put("setGlint", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setGlint(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("setLiquidClip", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setLiquidClip(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("setShouldDespawn", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setShouldDespawn(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("setArmorDamageable", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setArmorDamageable(wrapBoolean(args, 0));
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
        put("setHandEquipped", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setHandEquipped(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("addRepairItem", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.addRepairItem(wrapInt(args, 0));
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

        global.put("setUseAnimation", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setUseAnimation(wrapLong(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        global.put("overrideItemIcon", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.overrideItemIcon(
                    wrapString(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        global.put("getMaxDamageForId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItem.getMaxDamageForId(
                    wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("setAllowedInOffhand", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setAllowedInOffhand(
                    wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        global.put("setMaxDamage", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setMaxDamage(wrapLong(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        global.put("setMaxStackSize", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setMaxStackSize(wrapLong(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        global.put("setProperties", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setProperties(
                    wrapLong(args, 0), wrapString(args, 1));
                return Undefined.instance;
            }
        });
        global.put("setItemRequiresIconOverride", global,
            new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    NativeItem.setItemRequiresIconOverride(
                        wrapInt(args, 0), wrapBoolean(args, 1));
                    return Undefined.instance;
                }
            });
        global.put("constructArmorItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItem.constructArmorItem(wrapInt(args, 0),
                    wrapString(args, 1), wrapString(args, 2),
                    wrapString(args, 3), wrapInt(args, 4), wrapString(args, 5),
                    wrapInt(args, 6), wrapInt(args, 7), wrapInt(args, 8),
                    (float) wrapDouble(args, 9));
            }
        });
        global.put("setMaxUseDuration", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setMaxUseDuration(
                    wrapLong(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        global.put("addToCreative", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.addToCreative(wrapInt(args, 0), wrapInt(args, 1),
                    wrapInt(args, 2),
                    wrapObject(args, 3, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        global.put(
            "getDynamicItemIconOverride", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    return NativeItem.getDynamicItemIconOverride(
                        wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2),
                        wrapObject(args, 3,
                            com.zhekasmirnov.innercore.api
                                .NativeItemInstanceExtra.class));
                }
            });
        global.put("setStackedByData", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setStackedByData(
                    wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        global.put("setExplodable", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setExplodable(
                    wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        global.put("setCreativeCategory", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setCreativeCategory(
                    wrapLong(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        global.put("getMaxStackForId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItem.getMaxStackForId(
                    wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("getNameForId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 2) {
                    return NativeItem.getNameForId(
                        wrapInt(args, 0), wrapInt(args, 1));
                }
                if (args.length == 3) {
                    return NativeItem.getNameForId(
                        wrapInt(args, 0), wrapInt(args, 1), wrapLong(args, 2));
                }
                throw new RuntimeException("Not method...");
            }
        });
        global.put("addRepairItemId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.addRepairItemId(wrapLong(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        global.put("addToCreativeGroup", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.addToCreativeGroup(
                    wrapString(args, 0), wrapString(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        global.put("isGlintItemInstance", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItem.isGlintItemInstance(wrapInt(args, 0),
                    wrapInt(args, 1),
                    wrapObject(args, 2, java.lang.Object.class));
            }
        });
        global.put("setFireResistant", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setFireResistant(
                    wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        global.put("getArmorValue", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItem.getArmorValue(wrapInt(args, 0));
            }
        });
        global.put(
            "getLastIconOverridePath", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    return NativeItem.getLastIconOverridePath();
                }
            });
        global.put("setEnchantability", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setEnchantability(
                    wrapLong(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        global.put(
            "setCreativeCategoryForId", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    NativeItem.setCreativeCategoryForId(
                        wrapInt(args, 0), wrapInt(args, 1));
                    return Undefined.instance;
                }
            });
        global.put(
            "addToCreativeInternal", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    NativeItem.addToCreativeInternal(wrapInt(args, 0),
                        wrapInt(args, 1), wrapInt(args, 2), wrapLong(args, 3));
                    return Undefined.instance;
                }
            });
        global.put("constructItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItem.constructItem(wrapInt(args, 0),
                    wrapString(args, 1), wrapString(args, 2),
                    wrapString(args, 3), wrapInt(args, 4));
            }
        });
        global.put("setGlint", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setGlint(wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        global.put("setCategoryForId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setCategoryForId(wrapInt(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        global.put(
            "constructThrowableItem", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    return NativeItem.constructThrowableItem(wrapInt(args, 0),
                        wrapString(args, 1), wrapString(args, 2),
                        wrapString(args, 3), wrapInt(args, 4));
                }
            });
        global.put("isValid", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItem.isValid(wrapInt(args, 0));
            }
        });
        global.put("addCreativeGroup", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.addCreativeGroup(wrapString(args, 0),
                    wrapString(args, 1),
                    wrapObject(
                        args, 2, org.mozilla.javascript.NativeArray.class));
                return Undefined.instance;
            }
        });
        global.put("createArmorItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItem(NativeItem.createArmorItem(
                    wrapInt(args, 0), wrapString(args, 1), wrapString(args, 2),
                    wrapString(args, 3), wrapInt(args, 4), wrapString(args, 5),
                    wrapInt(args, 6), wrapInt(args, 7), wrapInt(args, 8),
                    wrapDouble(args, 9)));
            }
        });
        global.put("setLiquidClip", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setLiquidClip(
                    wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        global.put("setShouldDespawn", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setShouldDespawn(
                    wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        global.put("setArmorDamageable", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setArmorDamageable(
                    wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        global.put("createItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItem(NativeItem.createItem(wrapInt(args, 0),
                    wrapString(args, 1), wrapString(args, 2),
                    wrapString(args, 3), wrapInt(args, 4)));
            }
        });
        global.put("getItemById", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItem(
                    NativeItem.getItemById(wrapInt(args, 0)));
            }
        });
        global.put("isDynamicIconItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItem.isDynamicIconItem(wrapInt(args, 0));
            }
        });
        global.put("createThrowableItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItem(NativeItem.createThrowableItem(
                    wrapInt(args, 0), wrapString(args, 1), wrapString(args, 2),
                    wrapString(args, 3), wrapInt(args, 4)));
            }
        });
        global.put("setHandEquipped", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItem.setHandEquipped(
                    wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        scope.put("NativeItem", scope, global);
    }
}