package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.ItemContainer;
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

public class JsAdaptedScriptAPIItemContainer extends ScriptableObject implements Wrapper, IClassInstance {

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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.ItemContainer.class;
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
        JsTypesInit.register(new JsTypesInit.DefJsClass(ItemContainer.class, new JsTypesInit.IBuilderClass() {
            @Override
            public Object call(Object arg) {
                return new JsAdaptedScriptAPIItemContainer((ItemContainer) arg);
            }
        }));
    }
    private final ItemContainer self;

    public JsAdaptedScriptAPIItemContainer(ItemContainer self) {
        this.self = self;
        put("sealSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.sealSlot(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("setClientText", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setClientText(wrapString(args, 0), wrapString(args, 1));
                return Undefined.instance;
            }
        });
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("getNetworkName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getNetworkName();
            }
        });
        put("setBinding", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 2) {
                    self.setBinding(wrapString(args, 0), wrapObject(args, 1, java.lang.Object.class));
                    return Undefined.instance;
                }
                if (args.length == 3) {
                    self.setBinding(wrapString(args, 0), wrapString(args, 1), wrapObject(args, 2, java.lang.Object.class));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("asLegacyContainer", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 0) {
                    return new JsContainer(self.asLegacyContainer());
                }
                if (args.length == 1) {
                    return new JsContainer(self.asLegacyContainer(wrapBoolean(args, 0)));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getFullSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsItemContainerSlot(self.getFullSlot(wrapString(args, 0)));
            }
        });
        put("closeFor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.closeFor(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.server.ConnectedClient.class));
                return Undefined.instance;
            }
        });
        put("setGlobalDirtySlotListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsItemContainer(self.setGlobalDirtySlotListener(wrapObject(args, 0, com.zhekasmirnov.apparatus.api.container.ItemContainer.DirtySlotListener.class)));
            }
        });
        put("getBinding", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return self.getBinding(wrapString(args, 0));
                }
                if (args.length == 2) {
                    return self.getBinding(wrapString(args, 0), wrapString(args, 1));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("getFromSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getFromSlot(wrapString(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapObject(args, 4, com.zhekasmirnov.innercore.api.NativeItemInstanceExtra.class), wrapLong(args, 5));
            }
        });
        put("getGetTransferPolicy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                final com.zhekasmirnov.apparatus.api.container.ItemContainer.TransferPolicy res = self.getGetTransferPolicy(wrapString(args, 0));
                return ctx.getWrapFactory().wrap(ctx, scope, res, res.getClass());
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
                self.dropAt(wrapObject(args, 0, com.zhekasmirnov.apparatus.mcpe.NativeBlockSource.class), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                return Undefined.instance;
            }
        });
        put("getWorkbenchFieldSize", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getWorkbenchFieldSize();
            }
        });
        put("markSlotDirty", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.markSlotDirty(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("setDirtySlotListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsItemContainer(self.setDirtySlotListener(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.apparatus.api.container.ItemContainer.DirtySlotListener.class)));
            }
        });
        put("setSlotGetTransferPolicy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsItemContainer(self.setSlotGetTransferPolicy(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.apparatus.api.container.ItemContainer.TransferPolicy.class)));
            }
        });
        put("setWorkbenchFieldPrefix", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setWorkbenchFieldPrefix(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("setSlotSavingEnabled", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setSlotSavingEnabled(wrapString(args, 0), wrapBoolean(args, 1));
                return Undefined.instance;
            }
        });
        put("getText", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getText(wrapString(args, 0));
            }
        });
        put("sealAllSlots", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.sealAllSlots();
                return Undefined.instance;
            }
        });
        put("setScale", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setScale(wrapString(args, 0), (float) wrapDouble(args, 1));
                return Undefined.instance;
            }
        });
        put("setWorkbenchFieldSize", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setWorkbenchFieldSize(wrapInt(args, 0));
                return Undefined.instance;
            }
        });
        put("setGlobalSlotSavingEnabled", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setGlobalSlotSavingEnabled(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("openFor", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.openFor(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.server.ConnectedClient.class), wrapString(args, 1));
                return Undefined.instance;
            }
        });
        put("getAddTransferPolicy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                final com.zhekasmirnov.apparatus.api.container.ItemContainer.TransferPolicy res = self.getAddTransferPolicy(wrapString(args, 0));
                return ctx.getWrapFactory().wrap(ctx, scope, res, res.getClass());
            }
        });
        put("addServerEventListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.addServerEventListener(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.apparatus.api.container.ItemContainer.ServerEventListener.class));
                return Undefined.instance;
            }
        });
        put("resetSlotSavingEnabled", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.resetSlotSavingEnabled(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("getFieldSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    final com.zhekasmirnov.innercore.api.mod.ui.container.AbstractSlot res = self.getFieldSlot(wrapInt(args, 0));
                    return ctx.getWrapFactory().wrap(ctx, scope, res, res.getClass());
                }
                if (args.length == 2) {
                    final com.zhekasmirnov.innercore.api.mod.ui.container.AbstractSlot res = self.getFieldSlot(wrapInt(args, 0), wrapInt(args, 1));
                    return ctx.getWrapFactory().wrap(ctx, scope, res, res.getClass());
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setClientBinding", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 2) {
                    self.setClientBinding(wrapString(args, 0), wrapObject(args, 1, java.lang.Object.class));
                    return Undefined.instance;
                }
                if (args.length == 3) {
                    self.setClientBinding(wrapString(args, 0), wrapString(args, 1), wrapObject(args, 2, java.lang.Object.class));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("isSlotSavingEnabled", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.isSlotSavingEnabled(wrapString(args, 0));
            }
        });
        put("dropSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.dropSlot(wrapObject(args, 0, com.zhekasmirnov.apparatus.mcpe.NativeBlockSource.class), wrapString(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4));
                return Undefined.instance;
            }
        });
        put("sendClosed", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.sendClosed();
                return Undefined.instance;
            }
        });
        put("sendSlotToInventoryTransaction", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.sendSlotToInventoryTransaction(wrapString(args, 0), wrapInt(args, 1));
                return Undefined.instance;
            }
        });
        put("asScriptableField", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.asScriptableField();
            }
        });
        put("setGlobalAddTransferPolicy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsItemContainer(self.setGlobalAddTransferPolicy(wrapObject(args, 0, com.zhekasmirnov.apparatus.api.container.ItemContainer.TransferPolicy.class)));
            }
        });
        put("sendSlotToSlotTransaction", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.sendSlotToSlotTransaction(wrapString(args, 0), wrapString(args, 1), wrapInt(args, 2));
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
        put("getId", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getId();
            }
        });
        put("getNetworkEntity", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsNetworkEntity(self.getNetworkEntity());
            }
        });
        put("sendChanges", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.sendChanges();
                return Undefined.instance;
            }
        });
        put("setClientScale", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setClientScale(wrapString(args, 0), (float) wrapDouble(args, 1));
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
        put("setBindingValidator", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setBindingValidator(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.apparatus.api.container.ItemContainer.BindingValidator.class));
                return Undefined.instance;
            }
        });
        put("setSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 2) {
                    self.setSlot(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.apparatus.api.container.ItemContainerSlot.class));
                    return Undefined.instance;
                }
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
        put("getUiAdapter", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsItemContainerUiHandler(self.getUiAdapter());
            }
        });
        put("getWindowContent", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getWindowContent();
            }
        });
        put("markAllSlotsDirty", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.markAllSlotsDirty();
                return Undefined.instance;
            }
        });
        put("sendInventoryToSlotTransaction", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.sendInventoryToSlotTransaction(wrapInt(args, 0), wrapString(args, 1), wrapInt(args, 2));
                return Undefined.instance;
            }
        });
        put("clearSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.clearSlot(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("setClientContainerTypeName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setClientContainerTypeName(wrapString(args, 0));
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
        put("runTransaction", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.runTransaction(wrapObject(args, 0, com.zhekasmirnov.apparatus.api.container.ItemContainer.Transaction.class));
                return Undefined.instance;
            }
        });
        put("addServerOpenListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.addServerOpenListener(wrapObject(args, 0, com.zhekasmirnov.apparatus.api.container.ItemContainer.ServerOnOpenListener.class));
                return Undefined.instance;
            }
        });
        put("isLegacyContainer", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.isLegacyContainer();
            }
        });
        put("sendEvent", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                if (args.length == 2) {
                    self.sendEvent(wrapString(args, 0), wrapObject(args, 1, java.lang.Object.class));
                    return Undefined.instance;
                }
                if (args.length == 3) {
                    self.sendEvent(wrapObject(args, 0, com.zhekasmirnov.apparatus.multiplayer.server.ConnectedClient.class), wrapString(args, 1), wrapObject(args, 2, java.lang.Object.class));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("sendResponseEvent", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.sendResponseEvent(wrapString(args, 0), wrapObject(args, 1, java.lang.Object.class));
                return Undefined.instance;
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
        put("setGlobalBindingValidator", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setGlobalBindingValidator(wrapObject(args, 0, com.zhekasmirnov.apparatus.api.container.ItemContainer.BindingValidator.class));
                return Undefined.instance;
            }
        });
        put("getBindingValidator", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                final com.zhekasmirnov.apparatus.api.container.ItemContainer.BindingValidator res = self.getBindingValidator(wrapString(args, 0));
                return ctx.getWrapFactory().wrap(ctx, scope, res, res.getClass());
            }
        });
        put("getParent", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getParent();
            }
        });
        put("setGlobalGetTransferPolicy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsItemContainer(self.setGlobalGetTransferPolicy(wrapObject(args, 0, com.zhekasmirnov.apparatus.api.container.ItemContainer.TransferPolicy.class)));
            }
        });
        put("removeEntity", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.removeEntity();
                return Undefined.instance;
            }
        });
        put("isGlobalSlotSavingEnabled", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.isGlobalSlotSavingEnabled();
            }
        });
        put("getSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsItemContainerSlot(self.getSlot(wrapString(args, 0)));
            }
        });
        put("getValue", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getValue(wrapString(args, 0), (float) wrapDouble(args, 1));
            }
        });
        put("setSlotAddTransferPolicy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsItemContainer(self.setSlotAddTransferPolicy(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.apparatus.api.container.ItemContainer.TransferPolicy.class)));
            }
        });
        put("addServerCloseListener", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.addServerCloseListener(wrapObject(args, 0, com.zhekasmirnov.apparatus.api.container.ItemContainer.ServerOnCloseListener.class));
                return Undefined.instance;
            }
        });
        put("addToSlot", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.addToSlot(wrapString(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapObject(args, 4, com.zhekasmirnov.innercore.api.NativeItemInstanceExtra.class), wrapLong(args, 5));
            }
        });
        put("equals", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.equals(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        put("getClientContainerTypeName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getClientContainerTypeName();
            }
        });
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.toString();
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
                    return new JsAdaptedScriptAPIItemContainer(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.ItemContainer());
                }
                if (args.length == 1) {
                    return new JsAdaptedScriptAPIItemContainer(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.ItemContainer(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.container.Container.class)));
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

        global.put("addClientCloseListener", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                ItemContainer.addClientCloseListener(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.apparatus.api.container.ItemContainer.ClientOnCloseListener.class));
                return Undefined.instance;
            }
        });
        global.put("registerScreenFactory", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                ItemContainer.registerScreenFactory(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.apparatus.api.container.ItemContainer.UiScreenFactory.class));
                return Undefined.instance;
            }
        });
        global.put("getClientContainerInstance", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsItemContainer(ItemContainer.getClientContainerInstance(wrapString(args, 0)));
            }
        });
        global.put("addClientOpenListener", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                ItemContainer.addClientOpenListener(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.apparatus.api.container.ItemContainer.ClientOnOpenListener.class));
                return Undefined.instance;
            }
        });
        global.put("loadClass", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                ItemContainer.loadClass();
                return Undefined.instance;
            }
        });
        global.put("addClientEventListener", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                ItemContainer.addClientEventListener(wrapString(args, 0), wrapString(args, 1), wrapObject(args, 2, com.zhekasmirnov.apparatus.api.container.ItemContainer.ClientEventListener.class));
                return Undefined.instance;
            }
        });
        scope.put("AdaptedScriptAPIItemContainer", scope, global);
    }

}
