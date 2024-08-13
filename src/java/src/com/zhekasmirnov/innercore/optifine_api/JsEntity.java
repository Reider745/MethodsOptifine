package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Entity;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import org.mozilla.javascript.*;

import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;

public class JsEntity extends ScriptableObject implements Wrapper {

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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Entity.class;
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
    private final Entity self;

    public JsEntity(Entity self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getClass();
            }
        });
        put("hashCode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.hashCode();
            }
        });
        put("equals", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.equals(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        put("notifyAll", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.notifyAll();
                return null;
            }
        });
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.toString();
            }
        });
        put("notify", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.notify();
                return null;
            }
        });

    }

    @Override
    public String getClassName() {
        return "Entity";
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
                return new JsEntity(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Entity());
            }

            @Override
            public String getClassName() {
                return "Entity";
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

        global.put("getProjectileItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getProjectileItem(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("setRotationAxis", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setRotationAxis(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1), wrapDouble(args, 2));
                return null;
            }
        });
        global.put("getRotation", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getRotation(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("unwrapEntity", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.unwrapEntity(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("removeAllEffects", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.removeAllEffects(wrapObject(args, 0, java.lang.Object.class));
                return null;
            }
        });
        global.put("getPathNavigation", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getPathNavigation(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getRenderType", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getRenderType(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("setPosition", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setPosition(wrapObject(args, 0, java.lang.Object.class), wrapDouble(args, 1), wrapDouble(args, 2), wrapDouble(args, 3));
                return null;
            }
        });
        global.put("setFireTicks", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setFireTicks(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1), wrapBoolean(args, 2));
                return null;
            }
        });
        global.put("getTarget", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getTarget(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getFireTicks", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getFireTicks(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("setRot", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setRot(wrapObject(args, 0, java.lang.Object.class), wrapDouble(args, 1), wrapDouble(args, 2));
                return null;
            }
        });
        global.put("setVelocity", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setVelocity(wrapObject(args, 0, java.lang.Object.class), wrapDouble(args, 1), wrapDouble(args, 2), wrapDouble(args, 3));
                return null;
            }
        });
        global.put("getDimension", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getDimension(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getEntityTypeId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getEntityTypeId(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("setDroppedItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setDroppedItem(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapObject(args, 4, java.lang.Object.class));
                return null;
            }
        });
        global.put("setVelocityAxis", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setVelocityAxis(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1), wrapDouble(args, 2));
                return null;
            }
        });
        global.put("getHealth", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getHealth(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getCarriedItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getCarriedItem(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("setMaxHealth", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setMaxHealth(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1));
                return null;
            }
        });
        global.put("getArmor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getArmor(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1));
            }
        });
        global.put("getArmorSlot", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getArmorSlot(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1));
            }
        });
        global.put("setRotation", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setRotation(wrapObject(args, 0, java.lang.Object.class), wrapDouble(args, 1), wrapDouble(args, 2));
                return null;
            }
        });
        global.put("setNameTag", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setNameTag(wrapObject(args, 0, java.lang.Object.class), wrapString(args, 1));
                return null;
            }
        });
        global.put("getMobSkin", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getMobSkin(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("rideAnimal", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.rideAnimal(wrapObject(args, 0, java.lang.Object.class), wrapObject(args, 1, java.lang.Object.class));
                return null;
            }
        });
        global.put("setSkin", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setSkin(wrapObject(args, 0, java.lang.Object.class), wrapString(args, 1));
                return null;
            }
        });
        global.put("getYaw", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getYaw(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("setOffhandItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setOffhandItem(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapObject(args, 4, java.lang.Object.class));
                return null;
            }
        });
        global.put("setCollisionSize", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setCollisionSize(wrapObject(args, 0, java.lang.Object.class), wrapDouble(args, 1), wrapDouble(args, 2));
                return null;
            }
        });
        global.put("getType", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getType(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getNameTag", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getNameTag(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("isSneaking", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.isSneaking(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getAnimalAge", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getAnimalAge(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getAllLocal", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getAllLocal();
            }
        });
        global.put("getRiding", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getRiding(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getMaxHealth", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getMaxHealth(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getPlayerEnt", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getPlayerEnt();
            }
        });
        global.put("getVelocity", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getVelocity(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getCompoundTag", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getCompoundTag(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("setArmor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setArmor(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapInt(args, 4), wrapObject(args, 5, java.lang.Object.class));
                return null;
            }
        });
        global.put("removeEffect", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.removeEffect(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1));
                return null;
            }
        });
        global.put("setAnimalAge", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setAnimalAge(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1));
                return null;
            }
        });
        global.put("addEffect", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.addEffect(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapBoolean(args, 4), wrapBoolean(args, 5), wrapBoolean(args, 6));
                return null;
            }
        });
        global.put("dealDamage", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.dealDamage(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, org.mozilla.javascript.ScriptableObject.class));
                return null;
            }
        });
        global.put("getOffhandItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getOffhandItem(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("remove", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.remove(wrapObject(args, 0, java.lang.Object.class));
                return null;
            }
        });
        global.put("setCompoundTag", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setCompoundTag(wrapObject(args, 0, java.lang.Object.class), wrapObject(args, 1, java.lang.Object.class));
                return null;
            }
        });
        global.put("setArmorSlot", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setArmorSlot(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapInt(args, 4), wrapObject(args, 5, java.lang.Object.class));
                return null;
            }
        });
        global.put("getRider", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getRider(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getAttribute", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getAttribute(wrapObject(args, 0, java.lang.Object.class), wrapString(args, 1));
            }
        });
        global.put("getVelY", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getVelY(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getAllArrayList", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getAllArrayList();
            }
        });
        global.put("getVelZ", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getVelZ(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("setPositionAxis", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setPositionAxis(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1), wrapDouble(args, 2));
                return null;
            }
        });
        global.put("getVelX", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getVelX(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("setSneaking", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setSneaking(wrapObject(args, 0, java.lang.Object.class), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("setTarget", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setTarget(wrapObject(args, 0, java.lang.Object.class), wrapObject(args, 1, java.lang.Object.class));
                return null;
            }
        });
        global.put("getTypeName", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getTypeName(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("hasEffect", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.hasEffect(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1));
            }
        });
        global.put("setHealth", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setHealth(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1));
                return null;
            }
        });
        global.put("getAll", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getAll();
            }
        });
        global.put("setRenderType", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setRenderType(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1));
                return null;
            }
        });
        global.put("isValid", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.isValid(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("setMobSkin", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setMobSkin(wrapObject(args, 0, java.lang.Object.class), wrapString(args, 1));
                return null;
            }
        });
        global.put("getEffectDuration", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getEffectDuration(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1));
            }
        });
        global.put("getX", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getX(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getPitch", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getPitch(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getPosition", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getPosition(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getY", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getY(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getEntitiesInsideBox", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getEntitiesInsideBox(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapDouble(args, 3), wrapDouble(args, 4), wrapDouble(args, 5), wrapInt(args, 6), wrapBoolean(args, 7));
            }
        });
        global.put("getZ", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getZ(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("isImmobile", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.isImmobile(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("getEffectLevel", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getEffectLevel(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1));
            }
        });
        global.put("getSkin", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getSkin(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("setCarriedItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setCarriedItem(wrapObject(args, 0, java.lang.Object.class), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapObject(args, 4, java.lang.Object.class));
                return null;
            }
        });
        global.put("setImmobile", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Entity.setImmobile(wrapObject(args, 0, java.lang.Object.class), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("getDroppedItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Entity.getDroppedItem(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        scope.put("Entity", scope, global);
    }

}
