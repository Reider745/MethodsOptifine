package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntityType;
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

public class JsNetworkEntityType
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
                                   .entity.NetworkEntityType.class;
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
            NetworkEntityType.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsNetworkEntityType((NetworkEntityType) arg);
                }
            }));
    }
    private final NetworkEntityType self;
    public JsNetworkEntityType(NetworkEntityType self) {
        this.self = self;
        put("addClientPacketListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNetworkEntityType(self.addClientPacketListener(
                    wrapString(args, 0),
                    wrapObject(args, 1,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntityType.OnClientPacketListener.class)));
            }
        });
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getClass();
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
        put("setClientRemovePacketFactory", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNetworkEntityType(
                    self.setClientRemovePacketFactory(wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntityType.ClientRemovePacketFactory
                            .class)));
            }
        });
        put("onClientEntityAdded", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.onClientEntityAdded(
                    wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntity.class),
                    wrapObject(args, 1, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        put("setClientListSetupListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNetworkEntityType(
                    self.setClientListSetupListener(wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntityType.OnClientListSetupListener
                            .class)));
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
        put("addServerPacketListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNetworkEntityType(self.addServerPacketListener(
                    wrapString(args, 0),
                    wrapObject(args, 1,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntityType.OnServerPacketListener.class)));
            }
        });
        put("newClientAddPacket", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.newClientAddPacket(
                    wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntity.class),
                    wrapObject(args, 1,
                        com.zhekasmirnov.apparatus.multiplayer.server
                            .ConnectedClient.class));
            }
        });
        put("setClientEntityRemovedListener", this,
            new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    return new JsNetworkEntityType(
                        self.setClientEntityRemovedListener(wrapObject(args, 0,
                            com.zhekasmirnov.apparatus.multiplayer.util.entity
                                .NetworkEntityType.OnClientEntityRemovedListener
                                .class)));
                }
            });
        put("setupClientList", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setupClientList(
                    wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.list
                            .ConnectedClientList.class),
                    wrapObject(args, 1,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntity.class));
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
        put("setEntitySetupListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNetworkEntityType(
                    self.setEntitySetupListener(wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntityType.OnEntitySetupListener.class)));
            }
        });
        put("onServerPacket", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.onServerPacket(wrapObject(args, 0,
                                        com.zhekasmirnov.apparatus.multiplayer
                                            .util.entity.NetworkEntity.class),
                    wrapObject(args, 1,
                        com.zhekasmirnov.apparatus.multiplayer.server
                            .ConnectedClient.class),
                    wrapString(args, 2),
                    wrapObject(args, 3, java.lang.Object.class),
                    wrapString(args, 4));
                return Undefined.instance;
            }
        });
        put("getTypeName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getTypeName();
            }
        });
        put("setupEntity", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setupEntity(wrapObject(args, 0,
                    com.zhekasmirnov.apparatus.multiplayer.util.entity
                        .NetworkEntity.class));
                return Undefined.instance;
            }
        });
        put("onClientPacket", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.onClientPacket(wrapObject(args, 0,
                                        com.zhekasmirnov.apparatus.multiplayer
                                            .util.entity.NetworkEntity.class),
                    wrapString(args, 1),
                    wrapObject(args, 2, java.lang.Object.class),
                    wrapString(args, 3));
                return Undefined.instance;
            }
        });
        put("setClientEntityAddedListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNetworkEntityType(
                    self.setClientEntityAddedListener(wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntityType.OnClientEntityAddedListener
                            .class)));
            }
        });
        put("setClientAddPacketFactory", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNetworkEntityType(
                    self.setClientAddPacketFactory(wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntityType.ClientAddPacketFactory.class)));
            }
        });
        put("onClientEntityRemoved", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.onClientEntityRemoved(
                    wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntity.class),
                    wrapObject(args, 1, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        put("onClientEntityRemovedDueShutdown", this,
            new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    self.onClientEntityRemovedDueShutdown(wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntity.class));
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
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.toString();
            }
        });
        put("newClientRemovePacket", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.newClientRemovePacket(
                    wrapObject(args, 0,
                        com.zhekasmirnov.apparatus.multiplayer.util.entity
                            .NetworkEntity.class),
                    wrapObject(args, 1,
                        com.zhekasmirnov.apparatus.multiplayer.server
                            .ConnectedClient.class));
            }
        });
        put("isDuplicateAddPacketAllowed", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isDuplicateAddPacketAllowed();
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
                return new JsNetworkEntityType(
                    new com.zhekasmirnov.apparatus.multiplayer.util.entity
                        .NetworkEntityType(wrapString(args, 0)));
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

        global.put("getByName", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNetworkEntityType(
                    NetworkEntityType.getByName(wrapString(args, 0)));
            }
        });
        scope.put("NetworkEntityType", scope, global);
    }
}