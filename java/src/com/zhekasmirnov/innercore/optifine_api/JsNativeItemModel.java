package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.NativeItemModel;
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

public class JsNativeItemModel
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
            com.zhekasmirnov.innercore.api.NativeItemModel.class;
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
            NativeItemModel.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsNativeItemModel((NativeItemModel) arg);
                }
            }));
    }
    private final NativeItemModel self;
    public JsNativeItemModel(NativeItemModel self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("reloadIcon", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 0) {
                    self.reloadIcon();
                    return Undefined.instance;
                }
                if (args.length == 1) {
                    self.reloadIcon(wrapBoolean(args, 0));
                    return Undefined.instance;
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("overridesHand", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.overridesHand();
            }
        });
        put("setGlintMaterial", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setGlintMaterial(wrapString(args, 0)));
            }
        });
        put("updateForBlockVariant", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.updateForBlockVariant(wrapObject(args, 0,
                    com.zhekasmirnov.innercore.api.unlimited.BlockVariant
                        .class));
                return Undefined.instance;
            }
        });
        put("getWorldTextureName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getWorldTextureName();
            }
        });
        put("reloadIconIfDirty", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.reloadIconIfDirty();
                return Undefined.instance;
            }
        });
        put("updateCacheGroupToCurrent", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.updateCacheGroupToCurrent();
                return Undefined.instance;
            }
        });
        put("getShaderUniforms", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeShaderUniformSet(self.getShaderUniforms());
            }
        });
        put("setSpriteHandRender", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setSpriteHandRender(wrapBoolean(args, 0)));
            }
        });
        put("setWorldBlockModel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setWorldBlockModel(wrapObject(args, 0,
                    com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.class));
                return Undefined.instance;
            }
        });
        put("getMeshTextureName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getMeshTextureName();
            }
        });
        put("setHandGlintMaterial", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setHandGlintMaterial(wrapString(args, 0)));
            }
        });
        put("getUiGlintMaterialName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getUiGlintMaterialName();
            }
        });
        put("releaseIcon", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.releaseIcon();
                return Undefined.instance;
            }
        });
        put("getGuiBlockModel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsGuiBlockModel(self.getGuiBlockModel());
            }
        });
        put("getGuiRenderMesh", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsGuiRenderMesh(self.getGuiRenderMesh());
            }
        });
        put("getUiMaterialName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getUiMaterialName();
            }
        });
        put("setItemTexture", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setItemTexture(wrapString(args, 0), wrapInt(args, 1)));
            }
        });
        put("isNonExisting", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isNonExisting();
            }
        });
        put("isSpriteInWorld", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isSpriteInWorld();
            }
        });
        put("setSpriteUiRender", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setSpriteUiRender(wrapBoolean(args, 0)));
            }
        });
        put("setUiModel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return new JsNativeItemModel(
                        self.setUiModel(wrapObject(args, 0,
                            com.zhekasmirnov.innercore.api.NativeRenderMesh
                                .class)));
                }
                if (args.length == 2) {
                    return new JsNativeItemModel(
                        self.setUiModel(wrapObject(args, 0,
                                            com.zhekasmirnov.innercore.api
                                                .NativeRenderMesh.class),
                            wrapString(args, 1)));
                }
                if (args.length == 3) {
                    return new JsNativeItemModel(
                        self.setUiModel(wrapObject(args, 0,
                                            com.zhekasmirnov.innercore.api
                                                .NativeRenderMesh.class),
                            wrapString(args, 1), wrapString(args, 2)));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setItemTexturePath", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setItemTexturePath(wrapString(args, 0)));
            }
        });
        put("isEmptyInWorld", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isEmptyInWorld();
            }
        });
        put("getWorldBlockModel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsGuiBlockModel(self.getWorldBlockModel());
            }
        });
        put("setModUiSpriteBitmap", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(self.setModUiSpriteBitmap(
                    wrapObject(args, 0, android.graphics.Bitmap.class)));
            }
        });
        put("overridesUi", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.overridesUi();
            }
        });
        put("setModel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return new JsNativeItemModel(
                        self.setModel(wrapObject(args, 0,
                            com.zhekasmirnov.innercore.api.NativeRenderMesh
                                .class)));
                }
                if (args.length == 2) {
                    return new JsNativeItemModel(
                        self.setModel(wrapObject(args, 0,
                                          com.zhekasmirnov.innercore.api
                                              .NativeRenderMesh.class),
                            wrapString(args, 1)));
                }
                if (args.length == 3) {
                    return new JsNativeItemModel(
                        self.setModel(wrapObject(args, 0,
                                          com.zhekasmirnov.innercore.api
                                              .NativeRenderMesh.class),
                            wrapString(args, 1), wrapString(args, 2)));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setModelOverrideCallback", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setModelOverrideCallback(wrapObject(args, 0,
                        com.zhekasmirnov.innercore.api.NativeItemModel
                            .IOverrideCallback.class)));
            }
        });
        put("setUiTexture", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setUiTexture(wrapString(args, 0)));
            }
        });
        put("setHandMaterial", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setHandMaterial(wrapString(args, 0)));
            }
        });
        put("getSpriteMesh", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeRenderMesh(self.getSpriteMesh());
            }
        });
        put("removeModUiSpriteTexture", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(self.removeModUiSpriteTexture());
            }
        });
        put("getWorldMaterialName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getWorldMaterialName();
            }
        });
        put("occupy", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(self.occupy());
            }
        });
        put("setCacheKey", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setCacheKey(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("getModelForItemInstance", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(self.getModelForItemInstance(
                    wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2),
                    wrapObject(args, 3,
                        com.zhekasmirnov.innercore.api.NativeItemInstanceExtra
                            .class)));
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
        put("isUsingOverrideCallback", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isUsingOverrideCallback();
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
        put("getIconBitmapNoReload", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getIconBitmapNoReload();
            }
        });
        put("setMaterial", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setMaterial(wrapString(args, 0)));
            }
        });
        put("setCacheGroup", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setCacheGroup(wrapString(args, 0));
                return Undefined.instance;
            }
        });
        put("getItemRenderMesh", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 0) {
                    return new JsNativeRenderMesh(self.getItemRenderMesh());
                }
                if (args.length == 1) {
                    return new JsNativeRenderMesh(
                        self.getItemRenderMesh(wrapInt(args, 0)));
                }
                if (args.length == 2) {
                    return new JsNativeRenderMesh(self.getItemRenderMesh(
                        wrapInt(args, 0), wrapBoolean(args, 1)));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("hashCode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.hashCode();
            }
        });
        put("getWorldGlintMaterialName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getWorldGlintMaterialName();
            }
        });
        put("setHandModel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 1) {
                    return new JsNativeItemModel(
                        self.setHandModel(wrapObject(args, 0,
                            com.zhekasmirnov.innercore.api.NativeRenderMesh
                                .class)));
                }
                if (args.length == 2) {
                    return new JsNativeItemModel(
                        self.setHandModel(wrapObject(args, 0,
                                              com.zhekasmirnov.innercore.api
                                                  .NativeRenderMesh.class),
                            wrapString(args, 1)));
                }
                if (args.length == 3) {
                    return new JsNativeItemModel(
                        self.setHandModel(wrapObject(args, 0,
                                              com.zhekasmirnov.innercore.api
                                                  .NativeRenderMesh.class),
                            wrapString(args, 1), wrapString(args, 2)));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setHandTexture", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setHandTexture(wrapString(args, 0)));
            }
        });
        put("addToMesh", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.addToMesh(
                    wrapObject(args, 0,
                        com.zhekasmirnov.innercore.api.NativeRenderMesh.class),
                    (float) wrapDouble(args, 1), (float) wrapDouble(args, 2),
                    (float) wrapDouble(args, 3));
                return Undefined.instance;
            }
        });
        put("setModUiSpriteName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(self.setModUiSpriteName(
                    wrapString(args, 0), wrapInt(args, 1)));
            }
        });
        put("setTexture", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setTexture(wrapString(args, 0)));
            }
        });
        put("getUiTextureName", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getUiTextureName();
            }
        });
        put("isEmpty", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isEmpty();
            }
        });
        put("setUiBlockModel", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                self.setUiBlockModel(wrapObject(args, 0,
                    com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.class));
                return Undefined.instance;
            }
        });
        put("setUiMaterial", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setUiMaterial(wrapString(args, 0)));
            }
        });
        put("isEmptyInUi", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isEmptyInUi();
            }
        });
        put("getCacheKey", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getCacheKey();
            }
        });
        put("equals", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.equals(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        put("setUiGlintMaterial", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setUiGlintMaterial(wrapString(args, 0)));
            }
        });
        put("isSpriteInUi", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.isSpriteInUi();
            }
        });
        put("getIconBitmap", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.getIconBitmap();
            }
        });
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return self.toString();
            }
        });
        put("queueReload", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                if (args.length == 0) {
                    return self.queueReload();
                }
                if (args.length == 1) {
                    return self.queueReload(wrapObject(args, 0,
                        com.zhekasmirnov.innercore.api.NativeItemModel
                            .IIconRebuildListener.class));
                }
                throw new RuntimeException("Not method...");
            }
        });
        put("setModUiSpritePath", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    self.setModUiSpritePath(wrapString(args, 0)));
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

        global.put("releaseMesh", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                NativeItemModel.releaseMesh(
                    wrapObject(args, 0, java.lang.Object.class));
                return Undefined.instance;
            }
        });
        global.put(
            "getItemMeshTextureFor", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    return NativeItemModel.getItemMeshTextureFor(
                        wrapInt(args, 0), wrapInt(args, 1));
                }
            });
        global.put(
            "setSpriteMeshOptimization", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    NativeItemModel.setSpriteMeshOptimization(
                        wrapBoolean(args, 0), wrapInt(args, 1));
                    return Undefined.instance;
                }
            });
        global.put("newStandalone", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(NativeItemModel.newStandalone());
            }
        });
        global.put("getByPointer", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    NativeItemModel.getByPointer(wrapLong(args, 0)));
            }
        });
        global.put(
            "setCurrentCacheGroup", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    NativeItemModel.setCurrentCacheGroup(
                        wrapString(args, 0), wrapString(args, 1));
                    return Undefined.instance;
                }
            });
        global.put(
            "getEmptyMeshFromPool", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    return new JsNativeRenderMesh(
                        NativeItemModel.getEmptyMeshFromPool());
                }
            });
        global.put("getForWithFallback", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(NativeItemModel.getForWithFallback(
                    wrapInt(args, 0), wrapInt(args, 1)));
            }
        });
        global.put("tryReleaseModelBitmapsOnLowMemory", global,
            new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    NativeItemModel.tryReleaseModelBitmapsOnLowMemory(
                        wrapInt(args, 0));
                    return Undefined.instance;
                }
            });
        global.put("getFor", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return new JsNativeItemModel(
                    NativeItemModel.getFor(wrapInt(args, 0), wrapInt(args, 1)));
            }
        });
        global.put(
            "getItemRenderMeshFor", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope,
                    Scriptable thisObj, Object[] args) {
                    return new JsNativeRenderMesh(
                        NativeItemModel.getItemRenderMeshFor(wrapInt(args, 0),
                            wrapInt(args, 1), wrapInt(args, 2),
                            wrapBoolean(args, 3)));
                }
            });
        global.put("getAllModels", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope,
                Scriptable thisObj, Object[] args) {
                return NativeItemModel.getAllModels();
            }
        });
        scope.put("NativeItemModel", scope, global);
    }
}