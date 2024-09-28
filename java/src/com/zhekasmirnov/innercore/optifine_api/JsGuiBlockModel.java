package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel;
import com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.VanillaRenderType;
import com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Builder;
import com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Box;
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

public class JsGuiBlockModel extends ScriptableObject implements Wrapper, IClassInstance {

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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.class;
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
        JsTypesInit.register(new JsTypesInit.DefJsClass(GuiBlockModel.class, new JsTypesInit.IBuilderClass() {
            @Override
            public Object call(Object arg) {
                return new JsGuiBlockModel((GuiBlockModel) arg);
            }
        }));
        JsGuiBlockModelVanillaRenderType.init();
        JsGuiBlockModelBuilder.init();
        JsGuiBlockModelBox.init();
    }
    private final GuiBlockModel self;

    public JsGuiBlockModel(GuiBlockModel self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.getClass();
            }
        });
        put("addToMesh", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.addToMesh(wrapObject(args, 0, com.zhekasmirnov.innercore.api.NativeRenderMesh.class), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
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
        put("clear", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.clear();
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
        put("genTexture", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.genTexture();
            }
        });
        put("setShadow", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.setShadow(wrapBoolean(args, 0));
                return Undefined.instance;
            }
        });
        put("addToRenderModelPart", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.addToRenderModelPart(wrapObject(args, 0, com.zhekasmirnov.innercore.api.NativeRenderer.ModelPart.class), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
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
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return self.toString();
            }
        });
        put("updateShape", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.updateShape(wrapObject(args, 0, com.zhekasmirnov.innercore.api.unlimited.BlockShape.class));
                return Undefined.instance;
            }
        });
        put("addBox", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                self.addBox(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Box.class));
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
                    return new JsGuiBlockModel(new com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel());
                }
                if (args.length == 1) {
                    return new JsGuiBlockModel(new com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel(wrapInt(args, 0)));
                }
                if (args.length == 2) {
                    return new JsGuiBlockModel(new com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel(wrapObject(args, 0, java.lang.String[].class), wrapObject(args, 1, int[].class)));
                }
                if (args.length == 3) {
                    return new JsGuiBlockModel(new com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel(wrapObject(args, 0, java.lang.String[].class), wrapObject(args, 1, int[].class), wrapObject(args, 2, com.zhekasmirnov.innercore.api.unlimited.BlockShape.class)));
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

        global.put("createModelForBlockVariant", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                return new JsGuiBlockModel(GuiBlockModel.createModelForBlockVariant(wrapObject(args, 0, com.zhekasmirnov.innercore.api.unlimited.BlockVariant.class)));
            }
        });
        JsGuiBlockModelVanillaRenderType.inject(global);
        JsGuiBlockModelBuilder.inject(global);
        JsGuiBlockModelBox.inject(global);
        scope.put("GuiBlockModel", scope, global);
    }

    public static class JsGuiBlockModelVanillaRenderType extends ScriptableObject implements Wrapper, IClassInstance {

        private static final ConcurrentHashMap<String, JsFieldOpti> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.VanillaRenderType.class;
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
            JsTypesInit.register(new JsTypesInit.DefJsClass(VanillaRenderType.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsGuiBlockModelVanillaRenderType((VanillaRenderType) arg);
                }
            }));
        }
        private final VanillaRenderType self;

        public JsGuiBlockModelVanillaRenderType(VanillaRenderType self) {
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
            put("buildModelFor", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    if (args.length == 1) {
                        return new JsGuiBlockModel(self.buildModelFor(wrapObject(args, 0, java.util.List.class)));
                    }
                    if (args.length == 2) {
                        return new JsGuiBlockModel(self.buildModelFor(wrapObject(args, 0, java.lang.String[].class), wrapObject(args, 1, int[].class)));
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

            global.put("getFor", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    return new JsGuiBlockModelVanillaRenderType(VanillaRenderType.getFor(wrapInt(args, 0)));
                }
            });
            scope.put("GuiBlockModelVanillaRenderType", scope, global);
        }

    }

    public static class JsGuiBlockModelBuilder extends ScriptableObject implements Wrapper, IClassInstance {

        private static final ConcurrentHashMap<String, JsFieldOpti> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Builder.class;
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
            JsTypesInit.register(new JsTypesInit.DefJsClass(Builder.class, new JsTypesInit.IBuilderClass() {
                @Override
                public Object call(Object arg) {
                    return new JsGuiBlockModelBuilder((Builder) arg);
                }
            }));
            JsGuiBlockModelBuilderPrecompiledBox.init();
        }
        private final Builder self;

        public JsGuiBlockModelBuilder(Builder self) {
            this.self = self;
            put("add", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    if (args.length == 1) {
                        self.add(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Builder.class));
                        return Undefined.instance;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    return self.getClass();
                }
            });
            put("build", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    return new JsGuiBlockModel(self.build(wrapBoolean(args, 0)));
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
            put("clear", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    self.clear();
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
                    return new JsGuiBlockModelBuilder(new com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Builder());
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

            JsGuiBlockModelBuilderPrecompiledBox.inject(global);
            scope.put("GuiBlockModelBuilder", scope, global);
        }

        public static class JsGuiBlockModelBuilderPrecompiledBox extends ScriptableObject implements Wrapper, IClassInstance {

            private static final ConcurrentHashMap<String, JsFieldOpti> fields = new ConcurrentHashMap<>();

            static {
                Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Builder.PrecompiledBox.class;
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
                JsTypesInit.register(new JsTypesInit.DefJsClass(Builder.PrecompiledBox.class, new JsTypesInit.IBuilderClass() {
                    @Override
                    public Object call(Object arg) {
                        return new JsGuiBlockModelBuilderPrecompiledBox((Builder.PrecompiledBox) arg);
                    }
                }));
            }
            private final Builder.PrecompiledBox self;

            public JsGuiBlockModelBuilderPrecompiledBox(Builder.PrecompiledBox self) {
                this.self = self;
                put("addTexture", this, new ScriptableFunctionImpl() {
                    @Override
                    public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        return new JsGuiBlockModelBuilderPrecompiledBox(self.addTexture(wrapString(args, 0), wrapInt(args, 1)));
                    }
                });
                put("getClass", this, new ScriptableFunctionImpl() {
                    @Override
                    public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        return self.getClass();
                    }
                });
                put("inFrontOf", this, new ScriptableFunctionImpl() {
                    @Override
                    public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        return self.inFrontOf(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Builder.PrecompiledBox.class));
                    }
                });
                put("intersects", this, new ScriptableFunctionImpl() {
                    @Override
                    public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        return self.intersects(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Builder.PrecompiledBox.class));
                    }
                });
                put("notifyAll", this, new ScriptableFunctionImpl() {
                    @Override
                    public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        self.notifyAll();
                        return Undefined.instance;
                    }
                });
                put("setBlock", this, new ScriptableFunctionImpl() {
                    @Override
                    public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        return new JsGuiBlockModelBuilderPrecompiledBox(self.setBlock(wrapInt(args, 0), wrapInt(args, 1)));
                    }
                });
                put("inside", this, new ScriptableFunctionImpl() {
                    @Override
                    public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        return self.inside(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Builder.PrecompiledBox.class));
                    }
                });
                put("notify", this, new ScriptableFunctionImpl() {
                    @Override
                    public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        self.notify();
                        return Undefined.instance;
                    }
                });
                put("compile", this, new ScriptableFunctionImpl() {
                    @Override
                    public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        return new JsGuiBlockModelBox(self.compile());
                    }
                });
                put("disableSide", this, new ScriptableFunctionImpl() {
                    @Override
                    public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        return new JsGuiBlockModelBuilderPrecompiledBox(self.disableSide(wrapInt(args, 0)));
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
                put("toString", this, new ScriptableFunctionImpl() {
                    @Override
                    public Object call(Context ctx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        return self.toString();
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
                        return new JsGuiBlockModelBuilderPrecompiledBox(new com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Builder.PrecompiledBox(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.GuiBlockModel.Builder.PrecompiledBox.class), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4), (float) wrapDouble(args, 5), (float) wrapDouble(args, 6)));
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

                scope.put("GuiBlockModelBuilderPrecompiledBox", scope, global);
            }

        }

    }

}
