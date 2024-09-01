package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Particles;
import com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleEmitter;
import com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleSubEmitter;
import com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleAnimator;
import com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleType;
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

public class JsAdaptedScriptAPIParticles extends ScriptableObject implements Wrapper, IClassInstance {

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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Particles.class;
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
        JsTypesInit.register(new JsTypesInit.DefJsClass(Particles.class, new JsTypesInit.IBuilderClass() {
            @Override
            public Object call(Object arg) {
                return new JsAdaptedScriptAPIParticles((Particles) arg);
            }
        }));
        JsParticleRegistryParticleEmitter.init();
        JsParticleRegistryParticleSubEmitter.init();
        JsParticleRegistryParticleAnimator.init();
        JsParticleRegistryParticleType.init();
    }
    private final Particles self;

    public JsAdaptedScriptAPIParticles(Particles self) {
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
                return new JsAdaptedScriptAPIParticles(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Particles());
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

        global.put("nativeParticleEmitterRelease", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleEmitterRelease(wrapLong(args, 0));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleSubEmitterSetKeepEmitter", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleSubEmitterSetKeepEmitter(wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetAnimators", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetAnimators(wrapLong(args, 0), wrapLong(args, 1), wrapLong(args, 2), wrapLong(args, 3));
                return Undefined.instance;
            }
        });
        global.put("addParticle", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.addParticle(wrapInt(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapDouble(args, 3), wrapDouble(args, 4), wrapDouble(args, 5), wrapDouble(args, 6), wrapInt(args, 7));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetCollisionParams", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetCollisionParams(wrapLong(args, 0), wrapBoolean(args, 1), wrapBoolean(args, 2), wrapInt(args, 3));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetColorNew", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetColorNew(wrapLong(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), (float) wrapDouble(args, 6), (float) wrapDouble(args, 7), (float) wrapDouble(args, 8));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleEmitterMoveTo", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleEmitterMoveTo(wrapLong(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetFriction", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetFriction(wrapLong(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetLifetime", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetLifetime(wrapLong(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetSize", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetSize(wrapLong(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        global.put("registerParticleType", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Particles.registerParticleType(wrapObject(args, 0, org.mozilla.javascript.Scriptable.class));
            }
        });
        global.put("nativeParticleEmitterGetPosition", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleEmitterGetPosition(wrapLong(args, 0), wrapObject(args, 1, float[].class));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeGetID", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Particles.nativeParticleTypeGetID(wrapLong(args, 0));
            }
        });
        global.put("nativeParticleEmitterMove", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleEmitterMove(wrapLong(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleEmitterSetVelocity", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleEmitterSetVelocity(wrapLong(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                return Undefined.instance;
            }
        });
        global.put("nativeRegisterNewParticleType", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Particles.nativeRegisterNewParticleType(wrapString(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), wrapInt(args, 5), wrapInt(args, 6), wrapBoolean(args, 7));
            }
        });
        global.put("nativeParticleSubEmitterSetKeepVelocity", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleSubEmitterSetKeepVelocity(wrapLong(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetDefaultVelocity", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetDefaultVelocity(wrapLong(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetDefaultAcceleration", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetDefaultAcceleration(wrapLong(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetRenderType", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetRenderType(wrapLong(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        global.put("nativeNewParticleSubEmitter", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Particles.nativeNewParticleSubEmitter((float) wrapDouble(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3));
            }
        });
        global.put("nativeParticleTypeSetSubEmitters", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetSubEmitters(wrapLong(args, 0), wrapLong(args, 1), wrapLong(args, 2), wrapLong(args, 3));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetRebuildDelay", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetRebuildDelay(wrapLong(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleEmit1", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleEmit1(wrapLong(args, 0), wrapInt(args, 1), wrapInt(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleEmitterDetach", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleEmitterDetach(wrapLong(args, 0));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetAnimatorsNew", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetAnimatorsNew(wrapLong(args, 0), wrapLong(args, 1), wrapLong(args, 2), wrapLong(args, 3), wrapLong(args, 4));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleEmit3", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleEmit3(wrapLong(args, 0), wrapInt(args, 1), wrapInt(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), (float) wrapDouble(args, 6), (float) wrapDouble(args, 7), (float) wrapDouble(args, 8), (float) wrapDouble(args, 9), (float) wrapDouble(args, 10), (float) wrapDouble(args, 11));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleEmit2", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleEmit2(wrapLong(args, 0), wrapInt(args, 1), wrapInt(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), (float) wrapDouble(args, 6), (float) wrapDouble(args, 7), (float) wrapDouble(args, 8));
                return Undefined.instance;
            }
        });
        global.put("getParticleTypeById", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsParticleRegistryParticleType(Particles.getParticleTypeById(wrapInt(args, 0)));
            }
        });
        global.put("nativeParticleEmitterAttachTo", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleEmitterAttachTo(wrapLong(args, 0), wrapLong(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleTypeSetColor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleTypeSetColor(wrapLong(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4));
                return Undefined.instance;
            }
        });
        global.put("nativeNewParticleEmitter", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Particles.nativeNewParticleEmitter((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
            }
        });
        global.put("addFarParticle", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.addFarParticle(wrapInt(args, 0), wrapDouble(args, 1), wrapDouble(args, 2), wrapDouble(args, 3), wrapDouble(args, 4), wrapDouble(args, 5), wrapDouble(args, 6), wrapInt(args, 7));
                return Undefined.instance;
            }
        });
        global.put("nativeParticleSubEmitterSetRandom", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.nativeParticleSubEmitterSetRandom(wrapLong(args, 0), (float) wrapDouble(args, 1));
                return Undefined.instance;
            }
        });
        global.put("nativeNewParticleAnimator", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return Particles.nativeNewParticleAnimator((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), wrapInt(args, 4));
            }
        });
        global.put("addBreakingItemParticle", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Particles.addBreakingItemParticle(wrapInt(args, 0), wrapInt(args, 1), wrapDouble(args, 2), wrapDouble(args, 3), wrapDouble(args, 4));
                return Undefined.instance;
            }
        });
        JsParticleRegistryParticleEmitter.inject(global);
        JsParticleRegistryParticleSubEmitter.inject(global);
        JsParticleRegistryParticleAnimator.inject(global);
        JsParticleRegistryParticleType.inject(global);
        scope.put("AdaptedScriptAPIParticles", scope, global);
    }

    public static class JsParticleRegistryParticleEmitter extends ScriptableObject implements Wrapper, IClassInstance {

        private static final ConcurrentHashMap<String, JsFieldOpti> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleEmitter.class;
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
            JsTypesInit.register(new JsTypesInit.DefJsClass(ParticleEmitter.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsParticleRegistryParticleEmitter((ParticleEmitter) arg);
                }
            }));
        }
        private final ParticleEmitter self;

        public JsParticleRegistryParticleEmitter(ParticleEmitter self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    return self.getClass();
                }
            });
            put("move", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.move((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
                    return Undefined.instance;
                }
            });
            put("release", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.release();
                    return Undefined.instance;
                }
            });
            put("setEmitRelatively", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.setEmitRelatively(wrapBoolean(args, 0));
                    return Undefined.instance;
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.notifyAll();
                    return Undefined.instance;
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.notify();
                    return Undefined.instance;
                }
            });
            put("getPosition", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    return self.getPosition();
                }
            });
            put("stop", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.stop();
                    return Undefined.instance;
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
            put("setVelocity", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.setVelocity((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
                    return Undefined.instance;
                }
            });
            put("detach", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.detach();
                    return Undefined.instance;
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    return self.toString();
                }
            });
            put("attachTo", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    if (args.length == 1) {
                        self.attachTo(wrapObject(args, 0, java.lang.Object.class));
                        return Undefined.instance;
                    }
                    if (args.length == 4) {
                        self.attachTo(wrapObject(args, 0, java.lang.Object.class), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                        return Undefined.instance;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("emit", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    if (args.length == 5) {
                        self.emit(wrapInt(args, 0), wrapInt(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4));
                        return Undefined.instance;
                    }
                    if (args.length == 8) {
                        self.emit(wrapInt(args, 0), wrapInt(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), (float) wrapDouble(args, 6), (float) wrapDouble(args, 7));
                        return Undefined.instance;
                    }
                    if (args.length == 11) {
                        self.emit(wrapInt(args, 0), wrapInt(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), (float) wrapDouble(args, 6), (float) wrapDouble(args, 7), (float) wrapDouble(args, 8), (float) wrapDouble(args, 9), (float) wrapDouble(args, 10));
                        return Undefined.instance;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("getPositionArray", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    return self.getPositionArray();
                }
            });
            put("moveTo", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.moveTo((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
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
                    return new JsParticleRegistryParticleEmitter(new com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleEmitter((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2)));
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

            scope.put("ParticleRegistryParticleEmitter", scope, global);
        }

    }

    public static class JsParticleRegistryParticleSubEmitter extends ScriptableObject implements Wrapper, IClassInstance {

        private static final ConcurrentHashMap<String, JsFieldOpti> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleSubEmitter.class;
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
            JsTypesInit.register(new JsTypesInit.DefJsClass(ParticleSubEmitter.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsParticleRegistryParticleSubEmitter((ParticleSubEmitter) arg);
                }
            }));
        }
        private final ParticleSubEmitter self;

        public JsParticleRegistryParticleSubEmitter(ParticleSubEmitter self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    return self.getClass();
                }
            });
            put("setKeepVelocity", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.setKeepVelocity(wrapBoolean(args, 0));
                    return Undefined.instance;
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
            put("setKeepEmitter", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.setKeepEmitter(wrapBoolean(args, 0));
                    return Undefined.instance;
                }
            });
            put("setRandomVelocity", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.setRandomVelocity((float) wrapDouble(args, 0));
                    return Undefined.instance;
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
                    if (args.length == 1) {
                        return new JsParticleRegistryParticleSubEmitter(new com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleSubEmitter(wrapObject(args, 0, org.mozilla.javascript.Scriptable.class)));
                    }
                    if (args.length == 4) {
                        return new JsParticleRegistryParticleSubEmitter(new com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleSubEmitter((float) wrapDouble(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3)));
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

            scope.put("ParticleRegistryParticleSubEmitter", scope, global);
        }

    }

    public static class JsParticleRegistryParticleAnimator extends ScriptableObject implements Wrapper, IClassInstance {

        private static final ConcurrentHashMap<String, JsFieldOpti> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleAnimator.class;
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
            JsTypesInit.register(new JsTypesInit.DefJsClass(ParticleAnimator.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsParticleRegistryParticleAnimator((ParticleAnimator) arg);
                }
            }));
        }
        private final ParticleAnimator self;

        public JsParticleRegistryParticleAnimator(ParticleAnimator self) {
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
                    if (args.length == 1) {
                        return new JsParticleRegistryParticleAnimator(new com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleAnimator(wrapObject(args, 0, org.mozilla.javascript.Scriptable.class)));
                    }
                    if (args.length == 5) {
                        return new JsParticleRegistryParticleAnimator(new com.zhekasmirnov.innercore.api.particles.ParticleRegistry.ParticleAnimator(wrapInt(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4)));
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

            scope.put("ParticleRegistryParticleAnimator", scope, global);
        }

    }

}
