package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.ui.window.UIWindowLocation;
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

public class JsUIWindowLocation
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
            com.zhekasmirnov.innercore.api.mod.ui.window.UIWindowLocation.class;
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
            UIWindowLocation.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsUIWindowLocation((UIWindowLocation) arg);
                }
            }));
    }
    private final UIWindowLocation self;
    public JsUIWindowLocation(UIWindowLocation self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("getWindowWidth", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getWindowWidth();
            }
        });
        put("asScriptable", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.asScriptable();
            }
        });
        put("removeScroll", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.removeScroll();
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
        put("setZ", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setZ((float) wrapDouble(args, 0));
                return Undefined.instance;
            }
        });
        put("getWindowHeight", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getWindowHeight();
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
        put("updatePopupWindow", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.updatePopupWindow(
                    wrapObject(args, 0, android.widget.PopupWindow.class));
                return Undefined.instance;
            }
        });
        put("setSize", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setSize(wrapInt(args, 0), wrapInt(args, 1));
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
        put("setupAndShowPopupWindow", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setupAndShowPopupWindow(
                    wrapObject(args, 0, android.widget.PopupWindow.class));
                return Undefined.instance;
            }
        });
        put("copy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsUIWindowLocation(self.copy());
            }
        });
        put("getDrawingScale", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getDrawingScale();
            }
        });
        put("showPopupWindow", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.showPopupWindow(
                    wrapObject(args, 0, android.widget.PopupWindow.class));
                return Undefined.instance;
            }
        });
        put("getRect", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getRect();
            }
        });
        put("globalToWindow", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.globalToWindow((float) wrapDouble(args, 0));
            }
        });
        put("setPadding", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 2) {
                    self.setPadding(wrapInt(args, 0), wrapInt(args, 1));
                    return Undefined.instance;
                }
                if (args.length == 4) {
                    self.setPadding((float) wrapDouble(args, 0),
                        (float) wrapDouble(args, 1),
                        (float) wrapDouble(args, 2),
                        (float) wrapDouble(args, 3));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("set", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    self.set(wrapObject(args, 0,
                        com.zhekasmirnov.innercore.api.mod.ui.window
                            .UIWindowLocation.class));
                    return Undefined.instance;
                }
                if (args.length == 4) {
                    self.set(wrapInt(args, 0), wrapInt(args, 1),
                        wrapInt(args, 2), wrapInt(args, 3));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("windowToGlobal", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.windowToGlobal((float) wrapDouble(args, 0));
            }
        });
        put("getScale", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getScale();
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
        put("getLayoutParams", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getLayoutParams(
                    wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        put("setScroll", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setScroll(wrapInt(args, 0), wrapInt(args, 1));
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
                if (args.length == 0) {
                    return new JsUIWindowLocation(
                        new com.zhekasmirnov.innercore.api.mod.ui.window
                            .UIWindowLocation());
                }
                if (args.length == 1) {
                    return new JsUIWindowLocation(
                        new com.zhekasmirnov.innercore.api.mod.ui.window
                            .UIWindowLocation(wrapObject(args, 0,
                                org.mozilla.javascript.ScriptableObject
                                    .class)));
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

        scope.put("UIWindowLocation", scope, global);
    }
}