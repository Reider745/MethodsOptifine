package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Player;
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

public class JsAdaptedScriptAPIPlayer extends ScriptableObject implements Wrapper, IClassInstance {

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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Player.class;
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
        JsTypesInit.register(new JsTypesInit.DefJsClass(Player.class, new JsTypesInit.IBuilderClass() {
            @Override
            public Object call(Object arg) {
                return new JsAdaptedScriptAPIPlayer((Player) arg);
            }
        }));
    }
    private final Player self;

    public JsAdaptedScriptAPIPlayer(Player self) {
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
                return new JsAdaptedScriptAPIPlayer(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Player());
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

        global.put("addExp", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.addExp(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        global.put("setFov", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setFov(wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        global.put("getFloatAbility", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getFloatAbility(wrapString(args, 0));
            }
        });
        global.put("setCameraEntity", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setCameraEntity(wrapObject(args, 0, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        global.put("setLevel", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setLevel(wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        global.put("addItemInventory", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.addItemInventory(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapBoolean(args, 3), wrapObject(args, 4, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        global.put("setFlying", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setFlying(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        global.put("getPointed", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getPointed();
            }
        });
        global.put("getDimension", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getDimension();
            }
        });
        global.put("getHunger", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getHunger();
            }
        });
        global.put("getServer", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getServer();
            }
        });
        global.put("getCarriedItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getCarriedItem();
            }
        });
        global.put("getExhaustion", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getExhaustion();
            }
        });
        global.put("getSelectedSlotId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getSelectedSlotId();
            }
        });
        global.put("localPlayerTurn", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.localPlayerTurn(wrapDouble(args, 0), wrapDouble(args, 1));
                return Undefined.instance;
            }
        });
        global.put("getArmorSlot", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getArmorSlot(wrapInt(args, 0));
            }
        });
        global.put("setExperience", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setExperience(wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        global.put("setSelectedSlotId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setSelectedSlotId(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        global.put("setOffhandItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setOffhandItem(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        global.put("getInventorySlot", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getInventorySlot(wrapInt(args, 0));
            }
        });
        global.put("isFlying", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.isFlying();
            }
        });
        global.put("isPlayer", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.isPlayer(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("resetCamera", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.resetCamera();
                return Undefined.instance;
            }
        });
        global.put("setCanFly", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setCanFly(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        global.put("getExperience", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getExperience();
            }
        });
        global.put("getSaturation", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getSaturation();
            }
        });
        global.put("getOffhandItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getOffhandItem();
            }
        });
        global.put("setArmorSlot", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setArmorSlot(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapObject(args, 4, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        global.put("get", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.get();
            }
        });
        global.put("setAbility", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setAbility(wrapString(args, 0), wrapObject(args, 1, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        global.put("setExp", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setExp(wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        global.put("setHunger", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setHunger(wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        global.put("addItemCreativeInv", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.addItemCreativeInv(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        global.put("resetFov", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.resetFov();
                return Undefined.instance;
            }
        });
        global.put("getExp", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getExp();
            }
        });
        global.put("getLevel", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getLevel();
            }
        });
        global.put("setInventorySlot", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setInventorySlot(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapObject(args, 4, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        global.put("getX", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getX();
            }
        });
        global.put("addExperience", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.addExperience(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        global.put("getPosition", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getPosition();
            }
        });
        global.put("getY", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getY();
            }
        });
        global.put("getZ", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getZ();
            }
        });
        global.put("getLocal", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getLocal();
            }
        });
        global.put("setCarriedItem", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setCarriedItem(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        global.put("canFly", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.canFly();
            }
        });
        global.put("setExhaustion", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setExhaustion(wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        global.put("getBooleanAbility", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getBooleanAbility(wrapString(args, 0));
            }
        });
        global.put("getScore", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Player.getScore();
            }
        });
        global.put("setSaturation", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Player.setSaturation(wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        scope.put("AdaptedScriptAPIPlayer", scope, global);
    }

}
