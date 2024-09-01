package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.horizon.runtime.logger.Logger;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.PlayerActor;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import com.zhekasmirnov.innercore.api.runtime.Callback;
import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import org.mozilla.javascript.*;

public class JsPlayerActor extends ScriptableObject implements Wrapper {
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

    private static final ConcurrentHashMap<String, Field> fields =
        new ConcurrentHashMap<>();

    static {
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript
                                   .AdaptedScriptAPI.PlayerActor.class;
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
                    fields.put(field.getName(), field);
                }
            }
            super_clazz = super_clazz.getSuperclass();
        }
    }
    private final PlayerActor self;
    public JsPlayerActor(PlayerActor self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("setCanFly", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setCanFly(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("setArmor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setArmor(wrapInt(args, 0), wrapInt(args, 1),
                    wrapInt(args, 2), wrapInt(args, 3),
                    wrapObject(args, 4,
                        com.zhekasmirnov.innercore.api.NativeItemInstanceExtra
                            .class));
                return Undefined.instance;
            }
        });
        put("getExperience", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getExperience();
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
        put("getGameMode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getGameMode();
            }
        });
        put("addItemToInventory", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 3) {
                    self.addItemToInventory(
                        wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                    return Undefined.instance;
                }
                if (args.length == 4) {
                    self.addItemToInventory(wrapInt(args, 0), wrapInt(args, 1),
                        wrapInt(args, 2),
                        wrapObject(args, 3,
                            com.zhekasmirnov.innercore.api
                                .NativeItemInstanceExtra.class));
                    return Undefined.instance;
                }
                if (args.length == 5) {
                    self.addItemToInventory(wrapInt(args, 0), wrapInt(args, 1),
                        wrapInt(args, 2),
                        wrapObject(args, 3,
                            com.zhekasmirnov.innercore.api
                                .NativeItemInstanceExtra.class),
                        wrapBoolean(args, 4));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getSaturation", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getSaturation();
            }
        });
        put("setRespawnCoords", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setRespawnCoords(
                    wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
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
        put("isOperator", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isOperator();
            }
        });
        put("setLevel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setLevel((float) wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        put("getPlayerFloatAbility", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getPlayerFloatAbility(wrapString(args, 0));
            }
        });
        put("setSelectedSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setSelectedSlot(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("setFlying", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setFlying(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("spawnExpOrbs", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.spawnExpOrbs((float) wrapDouble(args, 0),
                    (float) wrapDouble(args, 1), (float) wrapDouble(args, 2),
                    wrapInt(args, 3));
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
        put("setPlayerBooleanAbility", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setPlayerBooleanAbility(
                    wrapString(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        put("getDimension", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getDimension();
            }
        });
        put("setSneaking", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setSneaking(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("getHunger", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getHunger();
            }
        });
        put("setHunger", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setHunger((float) wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        put("addItemToInventoryPtr", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.addItemToInventoryPtr(
                    wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        put("getExhaustion", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getExhaustion();
            }
        });
        put("getArmor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsItemStack(self.getArmor(wrapInt(args, 0)));
            }
        });
        put("isValid", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isValid();
            }
        });
        put("setExperience", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setExperience((float) wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        put("getPointer", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getPointer();
            }
        });
        put("setPlayerFloatAbility", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setPlayerFloatAbility(
                    wrapString(args, 0), (float) wrapDouble(args, 1));
                return Undefined.instance;
            }
        });
        put("getSelectedSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getSelectedSlot();
            }
        });
        put("getItemUseStartupProgress", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getItemUseStartupProgress();
            }
        });
        put("getLevel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getLevel();
            }
        });
        put("setInventorySlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setInventorySlot(wrapInt(args, 0), wrapInt(args, 1),
                    wrapInt(args, 2), wrapInt(args, 3),
                    wrapObject(args, 4,
                        com.zhekasmirnov.innercore.api.NativeItemInstanceExtra
                            .class));
                return Undefined.instance;
            }
        });
        put("getItemUseDuration", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getItemUseDuration();
            }
        });
        put("addExperience", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.addExperience(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("isSneaking", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isSneaking();
            }
        });
        put("equals", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.equals(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        put("getInventorySlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsItemStack(self.getInventorySlot(wrapInt(args, 0)));
            }
        });
        put("invokeUseItemNoTarget", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.invokeUseItemNoTarget(wrapInt(args, 0), wrapInt(args, 1),
                    wrapInt(args, 2),
                    wrapObject(args, 3,
                        com.zhekasmirnov.innercore.api.NativeItemInstanceExtra
                            .class));
                return Undefined.instance;
            }
        });
        put("getPlayerBooleanAbility", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getPlayerBooleanAbility(wrapString(args, 0));
            }
        });
        put("canFly", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.canFly();
            }
        });
        put("getItemUseIntervalProgress", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getItemUseIntervalProgress();
            }
        });
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.toString();
            }
        });
        put("setExhaustion", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setExhaustion((float) wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        put("getScore", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getScore();
            }
        });
        put("isFlying", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isFlying();
            }
        });
        put("setSaturation", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setSaturation((float) wrapDouble(args, 0));
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
        final ConcurrentHashMap<Long, JsPlayerActor> players = new ConcurrentHashMap<>();

        Callback.addCallback("ServerPlayerLeft", new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                final Long player = wrapLong(args, 0);
                players.remove(player);
                Logger.debug("MethodsOptifine", "Remove cache PlayerActor = "+player);
                return null;
            }
        }, 0);

        Callback.addCallback("LevelLeft", new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                players.clear();
                Logger.debug("MethodsOptifine", "Remove full cache PlayerActor");
                return null;
            }
        }, 0);

        final ScriptableObject global = new BaseScriptableClass() {
            @Override
            public Scriptable construct(Context ctx, Scriptable scope, Object[] args) {
                final Long player = wrapLong(args, 0);
                JsPlayerActor actor = players.get(player);
                if(actor != null){
                    Logger.debug("MethodsOptifine", "Added cache PlayerActor = "+player);
                    return actor;
                }
                actor = new JsPlayerActor(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.PlayerActor(player));
                players.put(player, actor);
                return actor;
            }

            @Override
            public String getClassName() {
                return "JavaClass";
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

        scope.put("PlayerActor", scope, global);
    }
}