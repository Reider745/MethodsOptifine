package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
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

public class JsNetworkEntity
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
        Class<?> super_clazz = com.zhekasmirnov.apparatus.multiplayer.util
                                   .entity.NetworkEntity.class;
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
            NetworkEntity.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsNetworkEntity((NetworkEntity) arg);
                }
            }));
    }
    private final NetworkEntity self;
    public JsNetworkEntity(NetworkEntity self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("isServer", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isServer();
            }
        });
        put("setClientExecutor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setClientExecutor(wrapObject(
                    args, 0, com.zhekasmirnov.apparatus.job.JobExecutor.class));
                return Undefined.instance;
            }
        });
        put("setConnectionPlayerListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setConnectionPlayerListener(wrapObject(args, 0,
                    com.zhekasmirnov.apparatus.multiplayer.util.entity
                        .NetworkEntity.IConnectionPlayer.class));
                return Undefined.instance;
            }
        });
        put("setServerExecutor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setServerExecutor(wrapObject(
                    args, 0, com.zhekasmirnov.apparatus.job.JobExecutor.class));
                return Undefined.instance;
            }
        });
        put("getName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getName();
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
        put("refreshClients", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.refreshClients();
                return Undefined.instance;
            }
        });
        put("getServerExecutor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                final com.zhekasmirnov.apparatus.job.JobExecutor res =
                    self.getServerExecutor();
                return ctx.getWrapFactory().wrap(
                    ctx, scope, res, res.getClass());
            }
        });
        put("getClients", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsConnectedClientList(self.getClients());
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
        put("remove", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.remove();
                return Undefined.instance;
            }
        });
        put("getTarget", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getTarget();
            }
        });
        put("isRemoved", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isRemoved();
            }
        });
        put("getType", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNetworkEntityType(self.getType());
            }
        });
        put("hashCode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.hashCode();
            }
        });
        put("setDisconnectionPlayerListener", this,
            new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    self.setDisconnectionPlayerListener(wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntity.IDisconnectionPlayer.class));
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
        put("getClientExecutor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                final com.zhekasmirnov.apparatus.job.JobExecutor res =
                    self.getClientExecutor();
                return ctx.getWrapFactory().wrap(
                    ctx, scope, res, res.getClass());
            }
        });
        put("setTarget", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setTarget(wrapObject(args, 0, java.lang.Object.class));
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
        put("respond", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.respond(wrapString(args, 0),
                    wrapObject(args, 1, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        put("send", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 2) {
                    self.send(wrapString(args, 0),
                        wrapObject(args, 1, java.lang.Object.class));
                    return Undefined.instance;
                }
                if (args.length == 3) {
                    self.send(wrapObject(args, 0,
                                  com.zhekasmirnov.apparatus.multiplayer.server
                                      .ConnectedClient.class),
                        wrapString(args, 1),
                        wrapObject(args, 2, java.lang.Object.class));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
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
                if (args.length == 1) {
                    return new JsNetworkEntity(
                        new com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntity(wrapObject(args, 0,
                                com.zhekasmirnov.apparatus.multiplayer.util
                                    .entity.NetworkEntityType.class)));
                }
                if (args.length == 2) {
                    return new JsNetworkEntity(
                        new com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntity(
                                wrapObject(args, 0,
                                    com.zhekasmirnov.apparatus.multiplayer.util
                                        .entity.NetworkEntityType.class),
                                wrapObject(args, 1, java.lang.Object.class)));
                }
                if (args.length == 3) {
                    return new JsNetworkEntity(
                        new com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntity(
                                wrapObject(args, 0,
                                    com.zhekasmirnov.apparatus.multiplayer.util
                                        .entity.NetworkEntityType.class),
                                wrapObject(args, 1, java.lang.Object.class),
                                wrapString(args, 2)));
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

        global.put("loadClass", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NetworkEntity.loadClass();
                return Undefined.instance;
            }
        });
        global.put(
            "getClientEntityInstance", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    return new JsNetworkEntity(
                        NetworkEntity.getClientEntityInstance(
                            wrapString(args, 0)));
                }
            });
        scope.put("NetworkEntity", scope, global);
    }
}