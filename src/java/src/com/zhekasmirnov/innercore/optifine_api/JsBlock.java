package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Block;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import org.mozilla.javascript.*;

import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;

public class JsBlock extends ScriptableObject implements Wrapper {

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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Block.class;
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
    private final Block self;

    public JsBlock(Block self) {
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
        return "Block";
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
                return new JsBlock(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Block());
            }

            @Override
            public String getClassName() {
                return "Block";
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

        global.put("setRedstoneTile", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setRedstoneTile(wrapInt(args, 0), wrapObject(args, 1, java.lang.Object.class), wrapBoolean(args, 2));
                return null;
            }
        });
        global.put("setRandomTickCallback", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setRandomTickCallback(wrapInt(args, 0), wrapObject(args, 1, org.mozilla.javascript.Function.class));
                return null;
            }
        });
        global.put("createBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.createBlock(wrapInt(args, 0), wrapString(args, 1), wrapObject(args, 2, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 3, java.lang.Object.class));
                return null;
            }
        });
        global.put("getRenderType", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.getRenderType(wrapInt(args, 0));
            }
        });
        global.put("getLightOpacity", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.getLightOpacity(wrapInt(args, 0));
            }
        });
        global.put("isSolid", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.isSolid(wrapInt(args, 0));
            }
        });
        global.put("setRedstoneConnector", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setRedstoneConnector(wrapInt(args, 0), wrapObject(args, 1, java.lang.Object.class), wrapBoolean(args, 2));
                return null;
            }
        });
        global.put("setTempDestroyTime", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setTempDestroyTime(wrapInt(args, 0), wrapDouble(args, 1));
                return null;
            }
        });
        global.put("canContainLiquid", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.canContainLiquid(wrapInt(args, 0));
            }
        });
        global.put("getRenderLayer", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.getRenderLayer(wrapInt(args, 0));
            }
        });
        global.put("getDestroyTime", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.getDestroyTime(wrapInt(args, 0));
            }
        });
        global.put("getFriction", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.getFriction(wrapInt(args, 0));
            }
        });
        global.put("setEntityInsideCallbackEnabled", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setEntityInsideCallbackEnabled(wrapInt(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("getExplosionResistance", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.getExplosionResistance(wrapInt(args, 0));
            }
        });
        global.put("getMapColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.getMapColor(wrapInt(args, 0));
            }
        });
        global.put("createSpecialType", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.createSpecialType(wrapString(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class));
            }
        });
        global.put("createLiquidBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.createLiquidBlock(wrapInt(args, 0), wrapString(args, 1), wrapInt(args, 2), wrapString(args, 3), wrapObject(args, 4, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 5, java.lang.Object.class), wrapInt(args, 6), wrapBoolean(args, 7));
                return null;
            }
        });
        global.put("getBlockAtlasTextureCoords", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.getBlockAtlasTextureCoords(wrapString(args, 0), wrapInt(args, 1));
            }
        });
        global.put("setShape", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setShape(wrapInt(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapDouble(args, 3), wrapDouble(args, 4), wrapDouble(args, 5), wrapDouble(args, 6), wrapObject(args, 7, java.lang.Object.class));
                return null;
            }
        });
        global.put("canBeExtraBlock", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.canBeExtraBlock(wrapInt(args, 0));
            }
        });
        global.put("getMaterial", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.getMaterial(wrapInt(args, 0));
            }
        });
        global.put("getTranslucency", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.getTranslucency(wrapInt(args, 0));
            }
        });
        global.put("setBlockChangeCallbackEnabled", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setBlockChangeCallbackEnabled(wrapInt(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("setRedstoneEmitter", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setRedstoneEmitter(wrapInt(args, 0), wrapObject(args, 1, java.lang.Object.class), wrapBoolean(args, 2));
                return null;
            }
        });
        global.put("setEntityStepOnCallbackEnabled", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setEntityStepOnCallbackEnabled(wrapInt(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("setDestroyTime", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setDestroyTime(wrapInt(args, 0), wrapDouble(args, 1));
                return null;
            }
        });
        global.put("setNeighbourChangeCallbackEnabled", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setNeighbourChangeCallbackEnabled(wrapInt(args, 0), wrapBoolean(args, 1));
                return null;
            }
        });
        global.put("getLightLevel", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Block.getLightLevel(wrapInt(args, 0));
            }
        });
        global.put("setAnimateTickCallback", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Block.setAnimateTickCallback(wrapInt(args, 0), wrapObject(args, 1, org.mozilla.javascript.Function.class));
                return null;
            }
        });
        scope.put("Block", scope, global);
    }

}
