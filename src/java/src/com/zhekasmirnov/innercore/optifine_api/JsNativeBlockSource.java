package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.apparatus.mcpe.NativeStaticUtils;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.NativeCallback;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import com.zhekasmirnov.innercore.api.runtime.Callback;
import org.mozilla.javascript.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;

public class JsNativeBlockSource extends ScriptableObject implements Wrapper {

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
        Class<?> super_clazz = com.zhekasmirnov.apparatus.mcpe.NativeBlockSource.class;
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

        Callback.addCallback("LevelLeft", new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                regions.clear();
                regionClient = null;
                return null;
            }
        }, 0);
    }
    private final NativeBlockSource self;

    public JsNativeBlockSource(NativeBlockSource self) {
        this.self = self;
        put("setRainLevel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setRainLevel((float) wrapDouble(args, 0));
                return null;
            }
        });
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getClass();
            }
        });
        put("listEntitiesInAABB", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 6) {
                    return self.listEntitiesInAABB((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5));
                }
                if (args.length == 7) {
                    return self.listEntitiesInAABB((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), wrapInt(args, 6));
                }
                if (args.length == 8) {
                    return self.listEntitiesInAABB((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), wrapInt(args, 6), wrapBoolean(args, 7));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setExtraBlock", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 4) {
                    self.setExtraBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState.class));
                    return null;
                }
                if (args.length == 5) {
                    self.setExtraBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapInt(args, 4));
                    return null;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setLightningLevel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setLightningLevel((float) wrapDouble(args, 0));
                return null;
            }
        });
        put("setBlock", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 4) {
                    self.setBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState.class));
                    return null;
                }
                if (args.length == 5) {
                    self.setBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapInt(args, 4));
                    return null;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getDestroyParticlesEnabled", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getDestroyParticlesEnabled();
            }
        });
        put("explode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.explode((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), wrapBoolean(args, 4));
                return null;
            }
        });
        put("getDimension", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getDimension();
            }
        });
        put("setBiome", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setBiome(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return null;
            }
        });
        put("getRainLevel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getRainLevel();
            }
        });
        put("getChunkStateAt", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getChunkStateAt(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        put("canSeeSky", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.canSeeSky(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        put("fetchEntitiesInAABB", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 6) {
                    return self.fetchEntitiesInAABB((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5));
                }
                if (args.length == 7) {
                    return self.fetchEntitiesInAABB((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), wrapInt(args, 6));
                }
                if (args.length == 8) {
                    return self.fetchEntitiesInAABB((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), wrapInt(args, 6), wrapBoolean(args, 7));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("isChunkLoaded", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.isChunkLoaded(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        put("getLightningLevel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getLightningLevel();
            }
        });
        put("getChunkState", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getChunkState(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        put("isChunkLoadedAt", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.isChunkLoadedAt(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        put("getBlockID", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getBlockID(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        put("destroyBlock", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 3) {
                    self.destroyBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                    return null;
                }
                if (args.length == 4) {
                    self.destroyBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapBoolean(args, 3));
                    return null;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getLightLevel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getLightLevel(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        put("getBiomeTemperatureAt", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getBiomeTemperatureAt(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        put("getBlockUpdateAllowed", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getBlockUpdateAllowed();
            }
        });
        put("getBlockEntity", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getBlockEntity(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        put("breakBlockForResult", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 3) {
                    return self.breakBlockForResult(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                }
                if (args.length == 4) {
                    return self.breakBlockForResult(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, com.zhekasmirnov.apparatus.adapter.innercore.game.item.ItemStack.class));
                }
                if (args.length == 5) {
                    return self.breakBlockForResult(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapLong(args, 3), wrapObject(args, 4, com.zhekasmirnov.apparatus.adapter.innercore.game.item.ItemStack.class));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getBlockData", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getBlockData(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        put("notifyAll", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.notifyAll();
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
        put("getBlockId", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getBlockId(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        put("spawnExpOrbs", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.spawnExpOrbs((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), wrapInt(args, 3));
                return null;
            }
        });
        put("hashCode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.hashCode();
            }
        });
        put("spawnEntity", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 4) {
                    return self.spawnEntity((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), wrapString(args, 3));
                }
                if (args.length == 6) {
                    return self.spawnEntity((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), wrapString(args, 3), wrapString(args, 4), wrapString(args, 5));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("breakBlock", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 4) {
                    self.breakBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapBoolean(args, 3));
                    return null;
                }
                if (args.length == 5) {
                    self.breakBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapBoolean(args, 3), wrapObject(args, 4, com.zhekasmirnov.apparatus.adapter.innercore.game.item.ItemStack.class));
                    return null;
                }
                if (args.length == 6) {
                    self.breakBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapBoolean(args, 3), wrapLong(args, 4), wrapObject(args, 5, com.zhekasmirnov.apparatus.adapter.innercore.game.item.ItemStack.class));
                    return null;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getBiome", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getBiome(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        put("listEntitiesOfTypeInAABB", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 7) {
                    return self.listEntitiesOfTypeInAABB((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), wrapString(args, 6));
                }
                if (args.length == 8) {
                    return self.listEntitiesOfTypeInAABB((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), wrapString(args, 6), wrapString(args, 7));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setBlockUpdateType", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setBlockUpdateType(wrapInt(args, 0));
                return null;
            }
        });
        put("spawnDroppedItem", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 6) {
                    return self.spawnDroppedItem((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), wrapInt(args, 3), wrapInt(args, 4), wrapInt(args, 5));
                }
                if (args.length == 7) {
                    return self.spawnDroppedItem((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), wrapInt(args, 3), wrapInt(args, 4), wrapInt(args, 5), wrapObject(args, 6, com.zhekasmirnov.innercore.api.NativeItemInstanceExtra.class));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getBiomeDownfallAt", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getBiomeDownfallAt(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        put("getBlock", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        put("getBlockUpdateType", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getBlockUpdateType();
            }
        });
        put("getGrassColor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getGrassColor(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        put("fetchEntitiesOfTypeInAABB", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 7) {
                    return self.fetchEntitiesOfTypeInAABB((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), wrapString(args, 6));
                }
                if (args.length == 8) {
                    return self.fetchEntitiesOfTypeInAABB((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), wrapString(args, 6), wrapString(args, 7));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getExtraBlock", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getExtraBlock(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        put("getPointer", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getPointer();
            }
        });
        put("setBlockUpdateAllowed", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setBlockUpdateAllowed(wrapBoolean(args, 0));
                return null;
            }
        });
        put("breakBlockForJsResult", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 3) {
                    return self.breakBlockForJsResult(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                }
                if (args.length == 4) {
                    return self.breakBlockForJsResult(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, org.mozilla.javascript.ScriptableObject.class));
                }
                if (args.length == 5) {
                    return self.breakBlockForJsResult(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapLong(args, 3), wrapObject(args, 4, org.mozilla.javascript.ScriptableObject.class));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setDestroyParticlesEnabled", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.setDestroyParticlesEnabled(wrapBoolean(args, 0));
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
        put("addToTickingQueue", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                if (args.length == 4) {
                    self.addToTickingQueue(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3));
                    return null;
                }
                if (args.length == 5) {
                    self.addToTickingQueue(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState.class), wrapInt(args, 4));
                    return null;
                }
                if (args.length == 6) {
                    self.addToTickingQueue(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState.class), wrapInt(args, 4), wrapInt(args, 5));
                    return null;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("clip", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.clip((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), wrapInt(args, 6), wrapObject(args, 7, float[].class));
            }
        });

    }

    @Override
    public String getClassName() {
        return "NativeBlockSource";
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

    // Хеширование NativeBlockSource
    private static final Map<Integer, JsNativeBlockSource> regions = new HashMap<>();

    public static JsNativeBlockSource getDefaultForDimension(int dimension) {
        synchronized (regions) {
            try {
                if(regions.containsKey(dimension))
                    return regions.get(dimension);

                final JsNativeBlockSource region = new JsNativeBlockSource(NativeBlockSource.getDefaultForDimension(dimension));
                regions.put(dimension, region);
                return region;
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public static JsNativeBlockSource getDefaultForActor(long actor) {
        if (!NativeStaticUtils.isExistingEntity(actor)) {
            return null;
        }
        return getDefaultForDimension(NativeAPI.getEntityDimension(actor));
    }

    public static JsNativeBlockSource getCurrentWorldGenRegion() {
        NativeBlockSource region = NativeBlockSource.getCurrentWorldGenRegion();
        return region != null ? new JsNativeBlockSource(region) : null;
    }

    private static JsNativeBlockSource regionClient = null;
    public static JsNativeBlockSource getCurrentClientRegion() {
        if(regionClient == null){
            NativeBlockSource region = NativeBlockSource.getCurrentClientRegion();
            if(region == null) return null;
            regionClient = new JsNativeBlockSource(NativeBlockSource.getCurrentClientRegion());
        }
        return regionClient;
    }



    public static void inject(ScriptableObject scope) {
        final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
        final ScriptableObject global = new BaseScriptableClass() {
            @Override
            public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                if (args.length == 1) {
                    return new JsNativeBlockSource(new com.zhekasmirnov.apparatus.mcpe.NativeBlockSource(wrapInt(args, 0)));
                }
                if (args.length == 3) {
                    return new JsNativeBlockSource(new com.zhekasmirnov.apparatus.mcpe.NativeBlockSource(wrapInt(args, 0), wrapBoolean(args, 1), wrapBoolean(args, 2)));
                }
                if (args.length == 2) {
                    return new JsNativeBlockSource(new com.zhekasmirnov.apparatus.mcpe.NativeBlockSource(wrapLong(args, 0), wrapBoolean(args, 1)));
                }
                throw new RuntimeException("Not method...");
            }

            @Override
            public String getClassName() {
                return "NativeBlockSource";
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

        global.put("getCurrentClientRegion", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return getCurrentClientRegion();
            }
        });
        global.put("resetDefaultBlockSources", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                NativeBlockSource.resetDefaultBlockSources();
                return null;
            }
        });
        global.put("getCurrentWorldGenRegion", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return getCurrentWorldGenRegion();
            }
        });
        global.put("getDefaultForActor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return getDefaultForActor(wrapLong(args, 0));
            }
        });
        global.put("getDefaultForDimension", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return getDefaultForDimension(wrapInt(args, 0));
            }
        });
        global.put("getFromServerCallbackPointer", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return NativeBlockSource.getFromServerCallbackPointer(wrapLong(args, 0));
            }
        });
        global.put("getFromCallbackPointer", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return NativeBlockSource.getFromCallbackPointer(wrapLong(args, 0));
            }
        });
        scope.put("NativeBlockSource", scope, global);
    }

}
