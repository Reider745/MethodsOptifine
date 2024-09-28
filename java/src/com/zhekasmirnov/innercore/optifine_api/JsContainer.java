package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.ui.container.Container;
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

public class JsContainer extends ScriptableObject implements Wrapper, IClassInstance {

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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.ui.container.Container.class;
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
        JsTypesInit.register(new JsTypesInit.DefJsClass(Container.class, new JsTypesInit.IBuilderClass() {
            @Override
            public Object call(Object arg) {
                return new JsContainer((Container) arg);
            }
        }));
    }
    private final Container self;

    public JsContainer(Container self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("invalidateUI", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 0) {
                    self.invalidateUI();
                    return Undefined.instance;
                }
                if (args.length == 1) {
                    self.invalidateUI(wrapBoolean(args, 0));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("refreshSlots", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.refreshSlots();
                return Undefined.instance;
            }
        });
        put("setBinding", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setBinding(wrapString(args, 0), wrapString(args, 1), wrapObject(args, 2, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        put("getFullSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getFullSlot(wrapString(args, 0));
            }
        });
        put("getBinding", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getBinding(wrapString(args, 0), wrapString(args, 1));
            }
        });
        put("setId", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setId(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("dropAt", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.dropAt((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
                return Undefined.instance;
            }
        });
        put("getWorkbenchFieldSize", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getWorkbenchFieldSize();
            }
        });
        put("getText", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getText(wrapString(args, 0));
            }
        });
        put("setScale", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setScale(wrapString(args, 0), (float) wrapDouble(args, 1));
                return Undefined.instance;
            }
        });
        put("_removeElement", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self._removeElement(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("isElementTouched", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.isElementTouched(wrapString(args, 0));
            }
        });
        put("handleSlotToInventoryTransaction", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.handleSlotToInventoryTransaction(wrapString(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        put("addElementInstance", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.addElementInstance(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.elements.UIElement.class), wrapString(args, 1));
                return Undefined.instance;
            }
        });
        put("handleBindingDirty", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.handleBindingDirty(wrapString(args, 0), wrapString(args, 1));
                return Undefined.instance;
            }
        });
        put("getFieldSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return self.getFieldSlot(wrapInt(args, 0));
                }
                if (args.length == 2) {
                    final com.zhekasmirnov.innercore.api.mod.ui.container.AbstractSlot res = self.getFieldSlot(wrapInt(args, 0), wrapInt(args, 1));
                    return ctx.getWrapFactory().wrap(ctx, scope, res, res.getClass());
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("dropSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.dropSlot(wrapString(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                return Undefined.instance;
            }
        });
        put("asScriptableField", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.asScriptableField();
            }
        });
        put("setWbSlotNamePrefix", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setWbSlotNamePrefix(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("setOnOpenListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setOnOpenListener(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.container.Container.OnOpenListener.class));
                return Undefined.instance;
            }
        });
        put("isOpened", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.isOpened();
            }
        });
        put("_addElement", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self._addElement(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.elements.UIElement.class), wrapString(args, 1));
                return Undefined.instance;
            }
        });
        put("getWindow", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                final com.zhekasmirnov.innercore.api.mod.ui.window.IWindow res = self.getWindow();
                return ctx.getWrapFactory().wrap(ctx, scope, res, res.getClass());
            }
        });
        put("validateSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.validateSlot(wrapString(args, 0));
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
        put("getGuiScreen", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                final com.zhekasmirnov.innercore.api.mod.ui.window.IWindow res = self.getGuiScreen();
                return ctx.getWrapFactory().wrap(ctx, scope, res, res.getClass());
            }
        });
        put("setOnCloseListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setOnCloseListener(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.container.Container.OnCloseListener.class));
                return Undefined.instance;
            }
        });
        put("getId", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getId();
            }
        });
        put("sendChanges", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.sendChanges();
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
        put("setSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 4) {
                    self.setSlot(wrapString(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3));
                    return Undefined.instance;
                }
                if (args.length == 5) {
                    self.setSlot(wrapString(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapObject(args, 4, com.zhekasmirnov.innercore.api.NativeItemInstanceExtra.class));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("clearSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.clearSlot(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("validateAll", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.validateAll();
                return Undefined.instance;
            }
        });
        put("hashCode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.hashCode();
            }
        });
        put("isLegacyContainer", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.isLegacyContainer();
            }
        });
        put("invalidateUIElements", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 0) {
                    self.invalidateUIElements();
                    return Undefined.instance;
                }
                if (args.length == 1) {
                    self.invalidateUIElements(wrapBoolean(args, 0));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setParent", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setParent(wrapObject(args, 0, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        put("close", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.close();
                return Undefined.instance;
            }
        });
        put("openAs", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.openAs(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.window.IWindow.class));
                return Undefined.instance;
            }
        });
        put("handleInventoryToSlotTransaction", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.handleInventoryToSlotTransaction(wrapInt(args, 0), wrapString(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        put("getParent", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getParent();
            }
        });
        put("applyChanges", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.applyChanges();
                return Undefined.instance;
            }
        });
        put("handleSlotToSlotTransaction", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.handleSlotToSlotTransaction(wrapString(args, 0), wrapString(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        put("getGuiContent", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getGuiContent();
            }
        });
        put("onWindowClosed", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.onWindowClosed();
                return Undefined.instance;
            }
        });
        put("getSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getSlot(wrapString(args, 0));
            }
        });
        put("getValue", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getValue(wrapString(args, 0));
            }
        });
        put("getSlotVisualImpl", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                final com.zhekasmirnov.innercore.api.mod.ui.container.UiVisualSlotImpl res = self.getSlotVisualImpl(wrapString(args, 0));
                return ctx.getWrapFactory().wrap(ctx, scope, res, res.getClass());
            }
        });
        put("getElement", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                final com.zhekasmirnov.innercore.api.mod.ui.elements.UIElement res = self.getElement(wrapString(args, 0));
                return ctx.getWrapFactory().wrap(ctx, scope, res, res.getClass());
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
        put("invalidateUIDrawing", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 0) {
                    self.invalidateUIDrawing();
                    return Undefined.instance;
                }
                if (args.length == 1) {
                    self.invalidateUIDrawing(wrapBoolean(args, 0));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setText", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setText(wrapString(args, 0), wrapString(args, 1));
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
                if (args.length == 0) {
                    return new JsContainer(new com.zhekasmirnov.innercore.api.mod.ui.container.Container());
                }
                if (args.length == 1) {
                    return new JsContainer(new com.zhekasmirnov.innercore.api.mod.ui.container.Container(wrapObject(args, 0, java.lang.Object.class)));
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

        global.put("initSaverId", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                Container.initSaverId();
                return Undefined.instance;
            }
        });
        scope.put("Container", scope, global);
    }

}
