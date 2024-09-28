package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList;
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

public class JsConnectedClientList extends ScriptableObject implements Wrapper, IClassInstance {

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
        Class<?> super_clazz = com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList.class;
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
        JsTypesInit.register(new JsTypesInit.DefJsClass(ConnectedClientList.class, new JsTypesInit.IBuilderClass() {
            @Override
            public Object call(Object arg) {
                return new JsConnectedClientList((ConnectedClientList) arg);
            }
        }));
    }
    private final ConnectedClientList self;

    public JsConnectedClientList(ConnectedClientList self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("setupAllInDimensionPolicy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return new JsConnectedClientList(self.setupAllInDimensionPolicy(wrapInt(args, 0)));
                }
                if (args.length == 2) {
                    return new JsConnectedClientList(self.setupAllInDimensionPolicy(wrapInt(args, 0), wrapInt(args, 1)));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getClientCollection", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getClientCollection();
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
        put("remove", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.remove(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.server.ConnectedClient.class));
                return Undefined.instance;
            }
        });
        put("setRemovePolicyTimeout", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setRemovePolicyTimeout(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("iterator", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.iterator();
            }
        });
        put("hashCode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.hashCode();
            }
        });
        put("setAddPolicy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    self.setAddPolicy(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList.Policy.class));
                    return Undefined.instance;
                }
                if (args.length == 2) {
                    self.setAddPolicy(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList.Policy.class), wrapInt(args, 1));
                    return Undefined.instance;
                }
                if (args.length == 3) {
                    self.setAddPolicy(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList.Policy.class), wrapInt(args, 1), wrapBoolean(args, 2));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("forcedRefresh", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.forcedRefresh();
                return Undefined.instance;
            }
        });
        put("addListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.addListener(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList.Listener.class));
                return Undefined.instance;
            }
        });
        put("add", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.add(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.server.ConnectedClient.class));
                return Undefined.instance;
            }
        });
        put("setAddPolicyTimeout", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setAddPolicyTimeout(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("setupDistancePolicy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 3) {
                    return new JsConnectedClientList(self.setupDistancePolicy(wrapObject(args, 0, com.zhekasmirnov.apparatus.adapter.innercore.game.common.Vector3.class), wrapInt(args, 1), (float) wrapDouble(args, 2)));
                }
                if (args.length == 5) {
                    return new JsConnectedClientList(self.setupDistancePolicy(wrapObject(args, 0, com.zhekasmirnov.apparatus.adapter.innercore.game.common.Vector3.class), wrapInt(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), wrapInt(args, 4)));
                }
                if (args.length == 7) {
                    return new JsConnectedClientList(self.setupDistancePolicy((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), wrapInt(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), wrapInt(args, 6)));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("spliterator", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.spliterator();
            }
        });
        put("forEach", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.forEach(wrapObject(args, 0, java.util.function.Consumer.class));
                return Undefined.instance;
            }
        });
        put("clear", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.clear();
                return Undefined.instance;
            }
        });
        put("refresh", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.refresh();
                return Undefined.instance;
            }
        });
        put("removeListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.removeListener(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList.Listener.class));
                return Undefined.instance;
            }
        });
        put("setRemovePolicy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    self.setRemovePolicy(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList.Policy.class));
                    return Undefined.instance;
                }
                if (args.length == 2) {
                    self.setRemovePolicy(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList.Policy.class), wrapInt(args, 1));
                    return Undefined.instance;
                }
                if (args.length == 3) {
                    self.setRemovePolicy(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList.Policy.class), wrapInt(args, 1), wrapBoolean(args, 2));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("contains", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.contains(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.server.ConnectedClient.class));
            }
        });
        put("setupAllPlayersPolicy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 0) {
                    return new JsConnectedClientList(self.setupAllPlayersPolicy());
                }
                if (args.length == 1) {
                    return new JsConnectedClientList(self.setupAllPlayersPolicy(wrapInt(args, 0)));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("forEachClient", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.forEachClient(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList.ClientConsumer.class));
                return Undefined.instance;
            }
        });
        put("equals", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.equals(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.toString();
            }
        });
        put("dropPoliciesAndClear", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.dropPoliciesAndClear();
                return Undefined.instance;
            }
        });
        put("send", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 2) {
                    self.send(wrapString(args, 0), wrapObject(args, 1, java.lang.Object.class));
                    return Undefined.instance;
                }
                if (args.length == 3) {
                    self.send(wrapString(args, 0), wrapObject(args, 1, java.lang.Object.class), wrapObject(args, 2, java.lang.Class.class));
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
                if (args.length == 0) {
                    return new JsConnectedClientList(new com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList());
                }
                if (args.length == 1) {
                    return new JsConnectedClientList(new com.zhekasmirnov.apparatus.multiplayer.util.list.ConnectedClientList(wrapBoolean(args, 0)));
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

        scope.put("ConnectedClientList", scope, global);
    }

}
