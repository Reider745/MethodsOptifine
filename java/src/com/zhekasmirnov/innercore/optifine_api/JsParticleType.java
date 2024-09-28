package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleType;
import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;
import org.mozilla.javascript.*;

public class JsParticleType extends ScriptableObject implements Wrapper {
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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.particles
                                   .ParticleRegistry.ParticleType.class;
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
    private final ParticleType self;
    public JsParticleType(ParticleType self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("setRenderType", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setRenderType(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("setAnimator", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setAnimator(wrapString(args, 0),
                    wrapObject(args, 1,
                        com.zhekasmirnov.innercore.api.particles
                            .ParticleRegistry.ParticleAnimator.class));
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
        put("setCollisionParams", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setCollisionParams(wrapBoolean(args, 0),
                    wrapBoolean(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        put("setDefaultAcceleration", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setDefaultAcceleration((float) wrapDouble(args, 0),
                    (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
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
        put("notify", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.notify();
                return Undefined.instance;
            }
        });
        put("setRebuildDelay", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setRebuildDelay(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("setColor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 4) {
                    self.setColor((float) wrapDouble(args, 0),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2),
                        (float) wrapDouble(args, 3));
                    return Undefined.instance;
                }
                if (args.length == 8) {
                    self.setColor((float) wrapDouble(args, 0),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2),
                        (float) wrapDouble(args, 3),
                        (float) wrapDouble(args, 4),
                        (float) wrapDouble(args, 5),
                        (float) wrapDouble(args, 6),
                        (float) wrapDouble(args, 7));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setSize", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setSize(
                    (float) wrapDouble(args, 0), (float) wrapDouble(args, 1));
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
        put("equals", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.equals(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        put("setLifetime", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setLifetime(wrapInt(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        put("setSubEmitter", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setSubEmitter(wrapString(args, 0),
                    wrapObject(args, 1,
                        com.zhekasmirnov.innercore.api.particles
                            .ParticleRegistry.ParticleSubEmitter.class));
                return Undefined.instance;
            }
        });
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.toString();
            }
        });
        put("setDefaultVelocity", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setDefaultVelocity((float) wrapDouble(args, 0),
                    (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        put("setFriction", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setFriction(
                    (float) wrapDouble(args, 0), (float) wrapDouble(args, 1));
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
        final ConcurrentHashMap<String, Field> fields =
            new ConcurrentHashMap<>();
        final ScriptableObject global = new BaseScriptableClass() {
            @Override
            public Scriptable construct(
                Context ctx, Scriptable scope, Object[] args) {
                if (args.length == 1) {
                    return new JsParticleType(
                        new com.zhekasmirnov.innercore.api.particles
                            .ParticleRegistry.ParticleType(wrapObject(args, 0,
                                org.mozilla.javascript.Scriptable.class)));
                }
                if (args.length == 5) {
                    return new JsParticleType(
                        new com.zhekasmirnov.innercore.api.particles
                            .ParticleRegistry.ParticleType(wrapString(args, 0),
                                wrapBoolean(args, 1),
                                wrapObject(args, 2, float[].class),
                                wrapInt(args, 3), wrapInt(args, 4)));
                }
                if (args.length == 8) {
                    return new JsParticleType(
                        new com.zhekasmirnov.innercore.api.particles
                            .ParticleRegistry.ParticleType(wrapString(args, 0),
                                (float) wrapDouble(args, 1),
                                (float) wrapDouble(args, 2),
                                (float) wrapDouble(args, 3),
                                (float) wrapDouble(args, 4), wrapInt(args, 5),
                                wrapInt(args, 6), wrapBoolean(args, 7)));
                }
                throw new RuntimeException("Not method...");
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

        scope.put("ParticleType", scope, global);
    }
}