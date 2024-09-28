package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Level;
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

public class JsAdaptedScriptAPILevel extends ScriptableObject implements Wrapper, IClassInstance {

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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Level.class;
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
        JsTypesInit.register(new JsTypesInit.DefJsClass(Level.class, new JsTypesInit.IBuilderClass() {
            @Override
            public Object call(Object arg) {
                return new JsAdaptedScriptAPILevel((Level) arg);
            }
        }));
    }
    private final Level self;

    public JsAdaptedScriptAPILevel(Level self) {
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
                return new JsAdaptedScriptAPILevel(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Level());
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

        global.put("resetFogColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.resetFogColor();
                return Undefined.instance;
            }
        });
        global.put("setRainLevel", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setRainLevel(wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        global.put("setFogColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setFogColor(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        global.put("setGrassColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setGrassColor(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        global.put("setLightningLevel", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setLightningLevel(wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        global.put("getBiomeMap", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getBiomeMap(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("setRespawnCoords", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setRespawnCoords(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        global.put("addParticle", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.addParticle(wrapInt(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapDouble(args, 3), wrapDouble(args, 4), wrapDouble(args, 5), wrapDouble(args, 6), wrapInt(args, 7));
                return Undefined.instance;
            }
        });
        global.put("setSpawn", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setSpawn(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        global.put("dropItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.dropItem(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapInt(args, 3), wrapInt(args, 4), wrapInt(args, 5), wrapInt(args, 6), wrapObject(args, 7, java.lang.Object.class));
            }
        });
        global.put("getWorldDir", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getWorldDir();
            }
        });
        global.put("explode", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.explode(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapDouble(args, 3), wrapBoolean(args, 4));
                return Undefined.instance;
            }
        });
        global.put("setSkyColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setSkyColor(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        global.put("resetCloudColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.resetCloudColor();
                return Undefined.instance;
            }
        });
        global.put("setBiome", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setBiome(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        global.put("setTime", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setTime(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        global.put("getRainLevel", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getRainLevel();
            }
        });
        global.put("getTile", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getTile(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        global.put("spawnMob", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.spawnMob(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapInt(args, 3), wrapString(args, 4));
            }
        });
        global.put("getBrightness", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getBrightness(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        global.put("getChunkStateAt", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getChunkStateAt(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        global.put("playSoundEnt", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.playSoundEnt(wrapObject(args, 0, java.lang.Object.class), wrapString(args, 1), wrapDouble(args, 2), wrapDouble(args, 3));
                return Undefined.instance;
            }
        });
        global.put("setGameMode", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setGameMode(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        global.put("isChunkLoaded", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.isChunkLoaded(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("getLightningLevel", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getLightningLevel();
            }
        });
        global.put("getChunkState", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getChunkState(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("isChunkLoadedAt", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.isChunkLoadedAt(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        global.put("destroyBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.destroyBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapBoolean(args, 3));
                return Undefined.instance;
            }
        });
        global.put("setNightMode", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setNightMode(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        global.put("getTemperature", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getTemperature(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        global.put("setCloudColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setCloudColor(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        global.put("getTime", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getTime();
            }
        });
        global.put("getGameMode", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getGameMode();
            }
        });
        global.put("resetFogDistance", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.resetFogDistance();
                return Undefined.instance;
            }
        });
        global.put("getTileAndData", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getTileAndData(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        global.put("setDifficulty", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setDifficulty(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        global.put("getWorldName", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getWorldName();
            }
        });
        global.put("biomeIdToName", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.biomeIdToName(wrapInt(args, 0));
            }
        });
        global.put("setBiomeMap", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setBiomeMap(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        global.put("spawnExpOrbs", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.spawnExpOrbs(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapInt(args, 3));
                return Undefined.instance;
            }
        });
        global.put("setFogDistance", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setFogDistance(wrapDouble(args, 0), wrapDouble(args, 1));
                return Undefined.instance;
            }
        });
        global.put("getTileEntity", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsNativeTileEntity(Level.getTileEntity(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2)));
            }
        });
        global.put("playSound", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.playSound(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapString(args, 3), wrapDouble(args, 4), wrapDouble(args, 5));
                return Undefined.instance;
            }
        });
        global.put("resetSunsetColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.resetSunsetColor();
                return Undefined.instance;
            }
        });
        global.put("getBiome", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getBiome(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("getData", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getData(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        global.put("setTile", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setTile(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapInt(args, 4));
                return Undefined.instance;
            }
        });
        global.put("resetUnderwaterFogColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.resetUnderwaterFogColor();
                return Undefined.instance;
            }
        });
        global.put("getSeed", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getSeed();
            }
        });
        global.put("getGrassColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getGrassColor(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("setSunsetColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setSunsetColor(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        global.put("resetSkyColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.resetSkyColor();
                return Undefined.instance;
            }
        });
        global.put("setBlockChangeCallbackEnabled", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setBlockChangeCallbackEnabled(wrapInt(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        global.put("addFarParticle", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.addFarParticle(wrapInt(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapDouble(args, 3), wrapDouble(args, 4), wrapDouble(args, 5), wrapDouble(args, 6), wrapInt(args, 7));
                return Undefined.instance;
            }
        });
        global.put("setUnderwaterFogDistance", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setUnderwaterFogDistance(wrapDouble(args, 0), wrapDouble(args, 1));
                return Undefined.instance;
            }
        });
        global.put("setUnderwaterFogColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.setUnderwaterFogColor(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        global.put("getDifficulty", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.getDifficulty();
            }
        });
        global.put("addBreakingItemParticle", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.addBreakingItemParticle(wrapInt(args, 0), wrapInt(args, 1), wrapDouble(args, 2), wrapDouble(args, 3), wrapDouble(args, 4));
                return Undefined.instance;
            }
        });
        global.put("clip", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Level.clip(wrapDouble(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapDouble(args, 3), wrapDouble(args, 4), wrapDouble(args, 5), wrapInt(args, 6));
            }
        });
        global.put("resetUnderwaterFogDistance", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Level.resetUnderwaterFogDistance();
                return Undefined.instance;
            }
        });
        scope.put("AdaptedScriptAPILevel", scope, global);
    }

}
