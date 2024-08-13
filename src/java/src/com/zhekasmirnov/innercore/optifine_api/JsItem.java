package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Item;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import org.mozilla.javascript.*;

import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;

public class JsItem extends ScriptableObject implements Wrapper {

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

    private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

    static {
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Item.class;
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
                    fields.put(field.getName(), field);
                }
            }
            super_clazz = super_clazz.getSuperclass();
        }

    }
    private final Item self;

    public JsItem(Item self) {
        this.self = self;
        put("setUseAnimation", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setUseAnimation(wrapInt(args, 0));
                return null;
            }
        });
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getClass();
            }
        });
        put("setEnchantType", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 1) {
                    self.setEnchantType(wrapInt(args, 0));
                    return null;
                }
                if (args.length == 2) {
                    self.setEnchantType(wrapInt(args, 0), wrapInt(args, 1));
                    return null;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setAllowedInOffhand", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setAllowedInOffhand(wrapBoolean(args, 0));
                return null;
            }
        });
        put("setMaxDamage", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setMaxDamage(wrapInt(args, 0));
                return null;
            }
        });
        put("setMaxStackSize", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setMaxStackSize(wrapInt(args, 0));
                return null;
            }
        });
        put("notifyAll", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.notifyAll();
                return null;
            }
        });
        put("setProperties", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setProperties(wrapString(args, 0));
                return null;
            }
        });
        put("setMaxUseDuration", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setMaxUseDuration(wrapInt(args, 0));
                return null;
            }
        });
        put("notify", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.notify();
                return null;
            }
        });
        put("setStackedByData", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setStackedByData(wrapBoolean(args, 0));
                return null;
            }
        });
        put("setExplodable", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setExplodable(wrapBoolean(args, 0));
                return null;
            }
        });
        put("setCreativeCategory", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setCreativeCategory(wrapInt(args, 0));
                return null;
            }
        });
        put("hashCode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.hashCode();
            }
        });
        put("addRepairItems", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.addRepairItems(wrapObject(args, 0, int[].class));
                return null;
            }
        });
        put("setFireResistant", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setFireResistant(wrapBoolean(args, 0));
                return null;
            }
        });
        put("setEnchantability", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setEnchantability(wrapInt(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        put("setGlint", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setGlint(wrapBoolean(args, 0));
                return null;
            }
        });
        put("setLiquidClip", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setLiquidClip(wrapBoolean(args, 0));
                return null;
            }
        });
        put("setShouldDespawn", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setShouldDespawn(wrapBoolean(args, 0));
                return null;
            }
        });
        put("setArmorDamageable", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setArmorDamageable(wrapBoolean(args, 0));
                return null;
            }
        });
        put("equals", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.equals(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.toString();
            }
        });
        put("setHandEquipped", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setHandEquipped(wrapBoolean(args, 0));
                return null;
            }
        });
        put("addRepairItem", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.addRepairItem(wrapInt(args, 0));
                return null;
            }
        });

    }

    @Override
    public String getClassName() {
        return "Item";
    }

    @Override
    public Object unwrap() {
        return this.self;
    }

    @Override
    public void put(String name, Scriptable start, Object value) {
        final Field field = fields.get(name);
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
        final Field field = fields.get(name);
        if (field != null) {
            try {
                return field.get(self);
            } catch (IllegalAccessException e) {
            }
        }
        return super.get(name, start);
    }

    public static void inject(ScriptableObject scope) {
        final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
        final ScriptableObject global = new BaseScriptableClass() {
            @Override
            public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                throw new RuntimeException("Not method...");
            }

            @Override
            public String getClassName() {
                return "Item";
            }

            @Override
            public void put(String name, Scriptable start, Object value) {
                final Field field = fields.get(name);
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
                final Field field = fields.get(name);
                if (field != null) {
                    try {
                        return field.get(null);
                    } catch (IllegalAccessException e) {
                    }
                }
                return super.get(name, start);
            }

        };

        global.put("setUseAnimation", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setUseAnimation(wrapLong(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        global.put("getMaxStackSize", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.getMaxStackSize(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("getMaxDamageForId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.getMaxDamageForId(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("getName", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.getName(wrapInt(args, 0), wrapInt(args, 1), wrapObject(args, 2, java.lang.Object.class));
            }
        });
        global.put("setAllowedInOffhand", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setAllowedInOffhand(wrapLong(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("setMaxDamage", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setMaxDamage(wrapLong(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        global.put("setProperties", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setProperties(wrapLong(args, 0), wrapString(args, 1));
                return null;
            }
        });
        global.put("getDynamicItemIconOverride", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.getDynamicItemIconOverride(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, com.zhekasmirnov.innercore.api.NativeItemInstanceExtra.class));
            }
        });
        global.put("setStackedByData", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setStackedByData(wrapLong(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("getMaxStackForId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.getMaxStackForId(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("setRequiresIconOverride", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setRequiresIconOverride(wrapInt(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("addRepairItemId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.addRepairItemId(wrapLong(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        global.put("setFireResistant", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setFireResistant(wrapLong(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("getLastIconOverridePath", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.getLastIconOverridePath();
            }
        });
        global.put("setEnchantability", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setEnchantability(wrapLong(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return null;
            }
        });
        global.put("setGlint", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setGlint(wrapLong(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("setCategoryForId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setCategoryForId(wrapInt(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        global.put("constructThrowableItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.constructThrowableItem(wrapInt(args, 0), wrapString(args, 1), wrapString(args, 2), wrapString(args, 3), wrapInt(args, 4));
            }
        });
        global.put("createItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.createItem(wrapInt(args, 0), wrapString(args, 1), wrapString(args, 2), wrapString(args, 3), wrapInt(args, 4));
            }
        });
        global.put("isDynamicIconItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.isDynamicIconItem(wrapInt(args, 0));
            }
        });
        global.put("createFoodItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.createFoodItem(wrapInt(args, 0), wrapString(args, 1), wrapString(args, 2), wrapString(args, 3), wrapInt(args, 4), wrapInt(args, 5));
            }
        });
        global.put("setHandEquipped", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setHandEquipped(wrapLong(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("overrideItemIcon", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.overrideItemIcon(wrapString(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        global.put("setMaxStackSize", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setMaxStackSize(wrapLong(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        global.put("setItemRequiresIconOverride", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setItemRequiresIconOverride(wrapInt(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("constructArmorItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.constructArmorItem(wrapInt(args, 0), wrapString(args, 1), wrapString(args, 2), wrapString(args, 3), wrapInt(args, 4), wrapString(args, 5), wrapInt(args, 6), wrapInt(args, 7), wrapInt(args, 8), (float) wrapDouble(args, 9));
            }
        });
        global.put("setMaxUseDuration", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setMaxUseDuration(wrapLong(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        global.put("addToCreative", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.addToCreative(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, java.lang.Object.class));
                return null;
            }
        });
        global.put("setExplodable", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setExplodable(wrapLong(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("setCreativeCategory", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setCreativeCategory(wrapLong(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        global.put("getNameForId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 2) {
                    return Item.getNameForId(wrapInt(args, 0), wrapInt(args, 1));
                }
                if (args.length == 3) {
                    return Item.getNameForId(wrapInt(args, 0), wrapInt(args, 1), wrapLong(args, 2));
                }
                throw new RuntimeException("Not method...");
            }
        });
        global.put("invokeItemUseOn", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.invokeItemUseOn(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, java.lang.Object.class), wrapInt(args, 4), wrapInt(args, 5), wrapInt(args, 6), wrapInt(args, 7), wrapDouble(args, 8), wrapDouble(args, 9), wrapDouble(args, 10), wrapObject(args, 11, java.lang.Object.class));
                return null;
            }
        });
        global.put("addToCreativeGroup", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.addToCreativeGroup(wrapString(args, 0), wrapString(args, 1), wrapInt(args, 2));
                return null;
            }
        });
        global.put("isGlintItemInstance", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.isGlintItemInstance(wrapInt(args, 0), wrapInt(args, 1), wrapObject(args, 2, java.lang.Object.class));
            }
        });
        global.put("getArmorValue", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.getArmorValue(wrapInt(args, 0));
            }
        });
        global.put("getMaxDamage", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.getMaxDamage(wrapInt(args, 0));
            }
        });
        global.put("setCreativeCategoryForId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setCreativeCategoryForId(wrapInt(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        global.put("addToCreativeInternal", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.addToCreativeInternal(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapLong(args, 3));
                return null;
            }
        });
        global.put("constructItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.constructItem(wrapInt(args, 0), wrapString(args, 1), wrapString(args, 2), wrapString(args, 3), wrapInt(args, 4));
            }
        });
        global.put("overrideCurrentIcon", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.overrideCurrentIcon(wrapString(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        global.put("isValid", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.isValid(wrapInt(args, 0));
            }
        });
        global.put("addCreativeGroup", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.addCreativeGroup(wrapString(args, 0), wrapString(args, 1), wrapObject(args, 2, org.mozilla.javascript.NativeArray.class));
                return null;
            }
        });
        global.put("createArmorItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.createArmorItem(wrapInt(args, 0), wrapString(args, 1), wrapString(args, 2), wrapString(args, 3), wrapInt(args, 4), wrapString(args, 5), wrapInt(args, 6), wrapInt(args, 7), wrapInt(args, 8), wrapDouble(args, 9));
            }
        });
        global.put("invokeItemUseNoTarget", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.invokeItemUseNoTarget(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, java.lang.Object.class));
                return null;
            }
        });
        global.put("setLiquidClip", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setLiquidClip(wrapLong(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("setShouldDespawn", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setShouldDespawn(wrapLong(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("setArmorDamageable", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.setArmorDamageable(wrapLong(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("getItemById", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.getItemById(wrapInt(args, 0));
            }
        });
        global.put("createThrowableItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Item.createThrowableItem(wrapInt(args, 0), wrapString(args, 1), wrapString(args, 2), wrapString(args, 3), wrapInt(args, 4));
            }
        });
        global.put("overrideCurrentName", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Item.overrideCurrentName(wrapString(args, 0));
                return null;
            }
        });
        scope.put("Item", scope, global);
    }

}
