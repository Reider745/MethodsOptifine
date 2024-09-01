package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.NativeRenderMesh;
import com.zhekasmirnov.innercore.api.NativeRenderMesh.ReadOnlyVertexData;
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

public class JsNativeRenderMesh
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
        Class<?> super_clazz =
            com.zhekasmirnov.innercore.api.NativeRenderMesh.class;
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
            NativeRenderMesh.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsNativeRenderMesh((NativeRenderMesh) arg);
                }
            }));
        JsNativeRenderMeshReadOnlyVertexData.init();
    }
    private final NativeRenderMesh self;
    public JsNativeRenderMesh(NativeRenderMesh self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("rotate", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 3) {
                    self.rotate((float) wrapDouble(args, 0),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2));
                    return Undefined.instance;
                }
                if (args.length == 6) {
                    self.rotate((float) wrapDouble(args, 0),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2),
                        (float) wrapDouble(args, 3),
                        (float) wrapDouble(args, 4),
                        (float) wrapDouble(args, 5));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setLightParams", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setLightParams((float) wrapDouble(args, 0),
                    (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
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
        put("scale", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.scale((float) wrapDouble(args, 0),
                    (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        put("setFoliageTinted", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 0) {
                    self.setFoliageTinted();
                    return Undefined.instance;
                }
                if (args.length == 1) {
                    self.setFoliageTinted(wrapInt(args, 0));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setBlockTexture", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setBlockTexture(wrapString(args, 0), wrapInt(args, 1));
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
        put("translate", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.translate((float) wrapDouble(args, 0),
                    (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        put("getReadOnlyVertexData", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeRenderMeshReadOnlyVertexData(
                    self.getReadOnlyVertexData());
            }
        });
        put("setLightPos", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setLightPos(
                    wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        put("resetColor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.resetColor();
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
        put("fitIn", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 6) {
                    self.fitIn((float) wrapDouble(args, 0),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2),
                        (float) wrapDouble(args, 3),
                        (float) wrapDouble(args, 4),
                        (float) wrapDouble(args, 5));
                    return Undefined.instance;
                }
                if (args.length == 7) {
                    self.fitIn((float) wrapDouble(args, 0),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2),
                        (float) wrapDouble(args, 3),
                        (float) wrapDouble(args, 4),
                        (float) wrapDouble(args, 5), wrapBoolean(args, 6));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("newGuiRenderMesh", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsGuiRenderMesh(self.newGuiRenderMesh());
            }
        });
        put("setLightIgnore", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setLightIgnore(wrapBoolean(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        put("setNoTint", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setNoTint();
                return Undefined.instance;
            }
        });
        put("resetTexture", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.resetTexture();
                return Undefined.instance;
            }
        });
        put("setWaterTinted", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setWaterTinted();
                return Undefined.instance;
            }
        });
        put("rebuild", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.rebuild();
                return Undefined.instance;
            }
        });
        put("setGrassTinted", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setGrassTinted();
                return Undefined.instance;
            }
        });
        put("addMesh", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    self.addMesh(wrapObject(args, 0,
                        com.zhekasmirnov.innercore.api.NativeRenderMesh.class));
                    return Undefined.instance;
                }
                if (args.length == 4) {
                    self.addMesh(wrapObject(args, 0,
                                     com.zhekasmirnov.innercore.api
                                         .NativeRenderMesh.class),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2),
                        (float) wrapDouble(args, 3));
                    return Undefined.instance;
                }
                if (args.length == 7) {
                    self.addMesh(wrapObject(args, 0,
                                     com.zhekasmirnov.innercore.api
                                         .NativeRenderMesh.class),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2),
                        (float) wrapDouble(args, 3),
                        (float) wrapDouble(args, 4),
                        (float) wrapDouble(args, 5),
                        (float) wrapDouble(args, 6));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("clear", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.clear();
                return Undefined.instance;
            }
        });
        put("invalidate", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.invalidate();
                return Undefined.instance;
            }
        });
        put("setLightDir", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setLightDir((float) wrapDouble(args, 0),
                    (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        put("getPtr", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getPtr();
            }
        });
        put("setColor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 3) {
                    self.setColor((float) wrapDouble(args, 0),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2));
                    return Undefined.instance;
                }
                if (args.length == 4) {
                    self.setColor((float) wrapDouble(args, 0),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2),
                        (float) wrapDouble(args, 3));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("importFromFile", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.importFromFile(wrapString(args, 0), wrapString(args, 1),
                    wrapObject(
                        args, 2, org.mozilla.javascript.Scriptable.class));
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
        put("clone", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 0) {
                    return self.clone();
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setNormal", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setNormal((float) wrapDouble(args, 0),
                    (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
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
        put("addVertex", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 3) {
                    self.addVertex((float) wrapDouble(args, 0),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2));
                    return Undefined.instance;
                }
                if (args.length == 5) {
                    self.addVertex((float) wrapDouble(args, 0),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2),
                        (float) wrapDouble(args, 3),
                        (float) wrapDouble(args, 4));
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
                return new JsNativeRenderMesh(
                    new com.zhekasmirnov.innercore.api.NativeRenderMesh());
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

        JsNativeRenderMeshReadOnlyVertexData.inject(global);
        scope.put("NativeRenderMesh", scope, global);
    }
}