package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.TextureSource;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.FrameTextureSource;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.ConfigVisualizer;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Font;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Texture;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.WindowLocation;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.TabbedWindow;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.AdaptiveWindow;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.StandardWindow;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.StandartWindow;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.WindowGroup;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Window;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Container;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import org.mozilla.javascript.*;

import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;

public class JsUI extends ScriptableObject implements Wrapper {

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

    private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

    static {
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.class;
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
                    fields.put(field.getName(), field);
                }
            }
            super_clazz = super_clazz.getSuperclass();
        }

    }
    private final UI self;

    public JsUI(UI self) {
        this.self = self;
        put("getClass", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.getClass();
            }
        });
        put("hashCode", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.hashCode();
            }
        });
        put("equals", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.equals(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        put("notifyAll", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.notifyAll();
                return null;
            }
        });
        put("toString", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return self.toString();
            }
        });
        put("notify", this, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                self.notify();
                return null;
            }
        });

    }

    @Override
    public String getClassName() {
        return "UI";
    }

    @Override
    public Object unwrap() {
        return this.self;
    }

    @Override
    public void put(String name, Scriptable start, Object value) {
        final Field field = fields.get(name);
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
        final Field field = fields.get(name);
        if (field != null) {
            try {
                return field.get(self);
            } catch (IllegalAccessException e) {
            }
        }
        return super.get(name, start);
    }

    public static void inject(ScriptableObject scope) {
        final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
        final ScriptableObject global = new BaseScriptableClass() {
            @Override
            public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                return new JsUI(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI());
            }

            @Override
            public String getClassName() {
                return "UI";
            }

            @Override
            public void put(String name, Scriptable start, Object value) {
                final Field field = fields.get(name);
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
                final Field field = fields.get(name);
                if (field != null) {
                    try {
                        return field.get(null);
                    } catch (IllegalAccessException e) {
                    }
                }
                return super.get(name, start);
            }

        };

        global.put("getRelMinecraftUiScale", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return UI.getRelMinecraftUiScale();
            }
        });
        global.put("getMinecraftUiScale", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return UI.getMinecraftUiScale();
            }
        });
        global.put("getScreenRelativeHeight", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return UI.getScreenRelativeHeight();
            }
        });
        global.put("getContext", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return UI.getContext();
            }
        });
        global.put("getScreenHeight", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return UI.getScreenHeight();
            }
        });
        JsTextureSource.inject(global);
        JsFrameTextureSource.inject(global);
        JsConfigVisualizer.inject(global);
        JsFont.inject(global);
        JsTexture.inject(global);
        JsWindowLocation.inject(global);
        JsTabbedWindow.inject(global);
        JsAdaptiveWindow.inject(global);
        JsStandardWindow.inject(global);
        JsStandartWindow.inject(global);
        JsWindowGroup.inject(global);
        JsWindow.inject(global);
        JsContainer.inject(global);
        scope.put("UI", scope, global);
    }

    public static class JsTextureSource extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.TextureSource.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final TextureSource self;

        public JsTextureSource(TextureSource self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "TextureSource";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    return new JsTextureSource(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.TextureSource());
                }

                @Override
                public String getClassName() {
                    return "TextureSource";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            global.put("get", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return TextureSource.get(wrapString(args, 0));
                }
            });
            global.put("getNullable", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return TextureSource.getNullable(wrapString(args, 0));
                }
            });
            global.put("put", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    TextureSource.put(wrapString(args, 0), wrapObject(args, 1, java.lang.Object.class));
                    return null;
                }
            });
            scope.put("TextureSource", scope, global);
        }

    }

    public static class JsFrameTextureSource extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.FrameTextureSource.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final FrameTextureSource self;

        public JsFrameTextureSource(FrameTextureSource self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "FrameTextureSource";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    return new JsFrameTextureSource(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.FrameTextureSource());
                }

                @Override
                public String getClassName() {
                    return "FrameTextureSource";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            global.put("get", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return FrameTextureSource.get(wrapString(args, 0));
                }
            });
            scope.put("FrameTextureSource", scope, global);
        }

    }

    public static class JsConfigVisualizer extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.ConfigVisualizer.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final ConfigVisualizer self;

        public JsConfigVisualizer(ConfigVisualizer self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("createVisualContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.createVisualContent(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("clearVisualContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.clearVisualContent(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "ConfigVisualizer";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    if (args.length == 1) {
                        return new JsConfigVisualizer(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.ConfigVisualizer(wrapObject(args, 0, com.zhekasmirnov.innercore.mod.build.Config.class)));
                    }
                    if (args.length == 2) {
                        return new JsConfigVisualizer(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.ConfigVisualizer(wrapObject(args, 0, com.zhekasmirnov.innercore.mod.build.Config.class), wrapString(args, 1)));
                    }
                    throw new RuntimeException("Not method...");
                }

                @Override
                public String getClassName() {
                    return "ConfigVisualizer";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            scope.put("ConfigVisualizer", scope, global);
        }

    }

    public static class JsFont extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Font.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final Font self;

        public JsFont(Font self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("asScriptable", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.asScriptable();
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("drawText", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.drawText(wrapObject(args, 0, android.graphics.Canvas.class), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), wrapString(args, 3), (float) wrapDouble(args, 4));
                    return null;
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("getTextWidth", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getTextWidth(wrapString(args, 0), (float) wrapDouble(args, 1));
                }
            });
            put("getBounds", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getBounds(wrapString(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });
            put("getTextHeight", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getTextHeight(wrapString(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                }
            });

        }

        @Override
        public String getClassName() {
            return "Font";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    if (args.length == 3) {
                        return new JsFont(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Font(wrapInt(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2)));
                    }
                    if (args.length == 1) {
                        return new JsFont(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Font(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class)));
                    }
                    throw new RuntimeException("Not method...");
                }

                @Override
                public String getClassName() {
                    return "Font";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            scope.put("Font", scope, global);
        }

    }

    public static class JsTexture extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Texture.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final Texture self;

        public JsTexture(Texture self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("rescaleAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.rescaleAll((float) wrapDouble(args, 0));
                    return null;
                }
            });
            put("resizeAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.resizeAll((float) wrapDouble(args, 0), (float) wrapDouble(args, 1));
                    return null;
                }
            });
            put("release", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.release();
                    return null;
                }
            });
            put("getFrame", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getFrame();
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("draw", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.draw(wrapObject(args, 0, android.graphics.Canvas.class), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                    return null;
                }
            });
            put("getWidth", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWidth();
                }
            });
            put("drawCutout", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.drawCutout(wrapObject(args, 0, android.graphics.Canvas.class), wrapObject(args, 1, android.graphics.RectF.class), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3), (float) wrapDouble(args, 4));
                    return null;
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });
            put("getHeight", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getHeight();
                }
            });
            put("getBitmap", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getBitmap(wrapInt(args, 0));
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("getBitmapWrap", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getBitmapWrap(wrapInt(args, 0));
                }
            });
            put("readOffset", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.readOffset(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("isAnimated", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isAnimated();
                }
            });
            put("fitAllToOneSize", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.fitAllToOneSize();
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "Texture";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    return new JsTexture(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Texture(wrapObject(args, 0, java.lang.Object.class)));
                }

                @Override
                public String getClassName() {
                    return "Texture";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            scope.put("Texture", scope, global);
        }

    }

    public static class JsWindowLocation extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.WindowLocation.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final WindowLocation self;

        public JsWindowLocation(WindowLocation self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("getWindowWidth", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowWidth();
                }
            });
            put("asScriptable", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.asScriptable();
                }
            });
            put("removeScroll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.removeScroll();
                    return null;
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("setZ", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setZ((float) wrapDouble(args, 0));
                    return null;
                }
            });
            put("getWindowHeight", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowHeight();
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });
            put("updatePopupWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.updatePopupWindow(wrapObject(args, 0, android.widget.PopupWindow.class));
                    return null;
                }
            });
            put("setSize", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setSize(wrapInt(args, 0), wrapInt(args, 1));
                    return null;
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("setupAndShowPopupWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setupAndShowPopupWindow(wrapObject(args, 0, android.widget.PopupWindow.class));
                    return null;
                }
            });
            put("copy", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.copy();
                }
            });
            put("getDrawingScale", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getDrawingScale();
                }
            });
            put("showPopupWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.showPopupWindow(wrapObject(args, 0, android.widget.PopupWindow.class));
                    return null;
                }
            });
            put("getRect", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getRect();
                }
            });
            put("globalToWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.globalToWindow((float) wrapDouble(args, 0));
                }
            });
            put("setPadding", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 2) {
                        self.setPadding(wrapInt(args, 0), wrapInt(args, 1));
                        return null;
                    }
                    if (args.length == 4) {
                        self.setPadding((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("set", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 1) {
                        self.set(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.window.UIWindowLocation.class));
                        return null;
                    }
                    if (args.length == 4) {
                        self.set(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("windowToGlobal", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.windowToGlobal((float) wrapDouble(args, 0));
                }
            });
            put("getScale", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getScale();
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("getLayoutParams", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getLayoutParams(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                }
            });
            put("setScroll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setScroll(wrapInt(args, 0), wrapInt(args, 1));
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "WindowLocation";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    if (args.length == 0) {
                        return new JsWindowLocation(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.WindowLocation());
                    }
                    if (args.length == 1) {
                        return new JsWindowLocation(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.WindowLocation(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class)));
                    }
                    throw new RuntimeException("Not method...");
                }

                @Override
                public String getClassName() {
                    return "WindowLocation";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            scope.put("WindowLocation", scope, global);
        }

    }

    public static class JsTabbedWindow extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.TabbedWindow.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final TabbedWindow self;

        public JsTabbedWindow(TabbedWindow self) {
            this.self = self;
            put("setEventListener", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setEventListener(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.window.IWindowEventListener.class));
                    return null;
                }
            });
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("isOpened", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isOpened();
                }
            });
            put("getContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContent();
                }
            });
            put("getGlobalTabSize", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getGlobalTabSize();
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("invalidateDrawing", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateDrawing(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("setCloseOnBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setCloseOnBackPressed(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });
            put("setDefaultTab", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setDefaultTab(wrapInt(args, 0));
                    return null;
                }
            });
            put("setTab", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 3) {
                        self.setTab(wrapInt(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 2, org.mozilla.javascript.ScriptableObject.class));
                        return null;
                    }
                    if (args.length == 4) {
                        self.setTab(wrapInt(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 2, org.mozilla.javascript.ScriptableObject.class), wrapBoolean(args, 3));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("setBlockingBackground", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setBlockingBackground(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("getStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getStyle();
                }
            });
            put("isInventoryNeeded", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isInventoryNeeded();
                }
            });
            put("getInnerWindowWidth", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getInnerWindowWidth();
                }
            });
            put("setLocation", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setLocation(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.window.UIWindowLocation.class));
                    return null;
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("setTabEventListener", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setTabEventListener(wrapInt(args, 0), wrapObject(args, 1, com.zhekasmirnov.innercore.api.mod.ui.window.IWindowEventListener.class));
                    return null;
                }
            });
            put("getWindowForTab", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowForTab(wrapInt(args, 0));
                }
            });
            put("setStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 1) {
                        self.setStyle(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("close", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.close();
                    return null;
                }
            });
            put("getInnerWindowHeight", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getInnerWindowHeight();
                }
            });
            put("onBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.onBackPressed();
                }
            });
            put("isDynamic", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isDynamic();
                }
            });
            put("getContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContainer();
                }
            });
            put("onTabSelected", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.onTabSelected(wrapInt(args, 0));
                    return null;
                }
            });
            put("setFakeTab", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setFakeTab(wrapInt(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("getStyleSafe", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getStyleSafe();
                }
            });
            put("setDebugEnabled", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setDebugEnabled(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("getElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getElements();
                }
            });
            put("getDefaultTab", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getDefaultTab();
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("invalidateElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateElements(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("setContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setContainer(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.container.UiAbstractContainer.class));
                    return null;
                }
            });
            put("open", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.open();
                    return null;
                }
            });
            put("frame", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.frame(wrapLong(args, 0));
                    return null;
                }
            });
            put("getWindowTabSize", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowTabSize();
                }
            });

        }

        @Override
        public String getClassName() {
            return "TabbedWindow";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    if (args.length == 0) {
                        return new JsTabbedWindow(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.TabbedWindow());
                    }
                    if (args.length == 1) {
                        return new JsTabbedWindow(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.TabbedWindow(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class)));
                    }
                    throw new RuntimeException("Not method...");
                }

                @Override
                public String getClassName() {
                    return "TabbedWindow";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            scope.put("TabbedWindow", scope, global);
        }

    }

    public static class JsAdaptiveWindow extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.AdaptiveWindow.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final AdaptiveWindow self;

        public JsAdaptiveWindow(AdaptiveWindow self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("getAllWindows", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getAllWindows();
                }
            });
            put("setContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setContent(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("isOpened", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isOpened();
                }
            });
            put("getWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindow(wrapString(args, 0));
                }
            });
            put("getContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContent();
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("invalidateDrawing", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateDrawing(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("refreshWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.refreshWindow(wrapString(args, 0));
                    return null;
                }
            });
            put("setCloseOnBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setCloseOnBackPressed(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("addWindowInstance", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.addWindowInstance(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.innercore.api.mod.ui.window.IWindow.class));
                    return null;
                }
            });
            put("removeWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.removeWindow(wrapString(args, 0));
                    return null;
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });
            put("setWindowContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setWindowContent(wrapString(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("getWindowContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowContent(wrapString(args, 0));
                }
            });
            put("setBlockingBackground", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setBlockingBackground(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("getStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getStyle();
                }
            });
            put("isInventoryNeeded", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isInventoryNeeded();
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("refreshAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.refreshAll();
                    return null;
                }
            });
            put("setStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 1) {
                        self.setStyle(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("invalidateAllContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateAllContent();
                    return null;
                }
            });
            put("close", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.close();
                    return null;
                }
            });
            put("onBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.onBackPressed();
                }
            });
            put("setForcedProfile", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setForcedProfile(wrapInt(args, 0));
                    return null;
                }
            });
            put("isDynamic", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isDynamic();
                }
            });
            put("getContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContainer();
                }
            });
            put("setProfile", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setProfile(wrapInt(args, 0));
                    return null;
                }
            });
            put("moveOnTop", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.moveOnTop(wrapString(args, 0));
                    return null;
                }
            });
            put("setDebugEnabled", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setDebugEnabled(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("addWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.addWindow(wrapString(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class));
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("getElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getElements();
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("getWindowNames", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowNames();
                }
            });
            put("invalidateElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateElements(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("setContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setContainer(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.container.UiAbstractContainer.class));
                    return null;
                }
            });
            put("open", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.open();
                    return null;
                }
            });
            put("frame", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.frame(wrapLong(args, 0));
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "AdaptiveWindow";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    if (args.length == 0) {
                        return new JsAdaptiveWindow(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.AdaptiveWindow());
                    }
                    if (args.length == 1) {
                        return new JsAdaptiveWindow(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.AdaptiveWindow(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class)));
                    }
                    throw new RuntimeException("Not method...");
                }

                @Override
                public String getClassName() {
                    return "AdaptiveWindow";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            scope.put("AdaptiveWindow", scope, global);
        }

    }

    public static class JsStandardWindow extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.StandardWindow.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final StandardWindow self;

        public JsStandardWindow(StandardWindow self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("getAllWindows", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getAllWindows();
                }
            });
            put("setContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setContent(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("isOpened", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isOpened();
                }
            });
            put("getWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindow(wrapString(args, 0));
                }
            });
            put("getContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContent();
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("invalidateDrawing", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateDrawing(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("refreshWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.refreshWindow(wrapString(args, 0));
                    return null;
                }
            });
            put("setCloseOnBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setCloseOnBackPressed(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("addWindowInstance", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.addWindowInstance(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.innercore.api.mod.ui.window.IWindow.class));
                    return null;
                }
            });
            put("removeWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.removeWindow(wrapString(args, 0));
                    return null;
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });
            put("setWindowContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setWindowContent(wrapString(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("getWindowContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowContent(wrapString(args, 0));
                }
            });
            put("setBlockingBackground", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setBlockingBackground(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("getStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getStyle();
                }
            });
            put("isInventoryNeeded", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isInventoryNeeded();
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("refreshAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.refreshAll();
                    return null;
                }
            });
            put("setStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 1) {
                        self.setStyle(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("invalidateAllContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateAllContent();
                    return null;
                }
            });
            put("close", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.close();
                    return null;
                }
            });
            put("onBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.onBackPressed();
                }
            });
            put("isDynamic", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isDynamic();
                }
            });
            put("getContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContainer();
                }
            });
            put("moveOnTop", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.moveOnTop(wrapString(args, 0));
                    return null;
                }
            });
            put("getStyleSafe", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getStyleSafe();
                }
            });
            put("setDebugEnabled", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setDebugEnabled(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("addWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.addWindow(wrapString(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class));
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("getElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getElements();
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("getWindowNames", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowNames();
                }
            });
            put("invalidateElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateElements(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("setContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setContainer(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.container.UiAbstractContainer.class));
                    return null;
                }
            });
            put("open", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.open();
                    return null;
                }
            });
            put("frame", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.frame(wrapLong(args, 0));
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "StandardWindow";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    if (args.length == 0) {
                        return new JsStandardWindow(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.StandardWindow());
                    }
                    if (args.length == 1) {
                        return new JsStandardWindow(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.StandardWindow(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class)));
                    }
                    throw new RuntimeException("Not method...");
                }

                @Override
                public String getClassName() {
                    return "StandardWindow";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            scope.put("StandardWindow", scope, global);
        }

    }

    public static class JsStandartWindow extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.StandartWindow.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final StandartWindow self;

        public JsStandartWindow(StandartWindow self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("getAllWindows", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getAllWindows();
                }
            });
            put("setContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setContent(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("isOpened", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isOpened();
                }
            });
            put("getWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindow(wrapString(args, 0));
                }
            });
            put("getContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContent();
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("invalidateDrawing", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateDrawing(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("refreshWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.refreshWindow(wrapString(args, 0));
                    return null;
                }
            });
            put("setCloseOnBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setCloseOnBackPressed(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("addWindowInstance", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.addWindowInstance(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.innercore.api.mod.ui.window.IWindow.class));
                    return null;
                }
            });
            put("removeWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.removeWindow(wrapString(args, 0));
                    return null;
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });
            put("setWindowContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setWindowContent(wrapString(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("getWindowContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowContent(wrapString(args, 0));
                }
            });
            put("setBlockingBackground", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setBlockingBackground(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("getStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getStyle();
                }
            });
            put("isInventoryNeeded", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isInventoryNeeded();
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("refreshAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.refreshAll();
                    return null;
                }
            });
            put("setStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 1) {
                        self.setStyle(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("invalidateAllContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateAllContent();
                    return null;
                }
            });
            put("close", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.close();
                    return null;
                }
            });
            put("onBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.onBackPressed();
                }
            });
            put("isDynamic", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isDynamic();
                }
            });
            put("getContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContainer();
                }
            });
            put("moveOnTop", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.moveOnTop(wrapString(args, 0));
                    return null;
                }
            });
            put("getStyleSafe", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getStyleSafe();
                }
            });
            put("setDebugEnabled", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setDebugEnabled(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("addWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.addWindow(wrapString(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class));
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("getElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getElements();
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("getWindowNames", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowNames();
                }
            });
            put("invalidateElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateElements(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("setContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setContainer(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.container.UiAbstractContainer.class));
                    return null;
                }
            });
            put("open", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.open();
                    return null;
                }
            });
            put("frame", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.frame(wrapLong(args, 0));
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "StandartWindow";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    if (args.length == 0) {
                        return new JsStandartWindow(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.StandartWindow());
                    }
                    if (args.length == 1) {
                        return new JsStandartWindow(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.StandartWindow(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class)));
                    }
                    throw new RuntimeException("Not method...");
                }

                @Override
                public String getClassName() {
                    return "StandartWindow";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            scope.put("StandartWindow", scope, global);
        }

    }

    public static class JsWindowGroup extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.WindowGroup.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final WindowGroup self;

        public JsWindowGroup(WindowGroup self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("getAllWindows", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getAllWindows();
                }
            });
            put("isOpened", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isOpened();
                }
            });
            put("getWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindow(wrapString(args, 0));
                }
            });
            put("getContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContent();
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("invalidateDrawing", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateDrawing(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("refreshWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.refreshWindow(wrapString(args, 0));
                    return null;
                }
            });
            put("setCloseOnBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setCloseOnBackPressed(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("addWindowInstance", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.addWindowInstance(wrapString(args, 0), wrapObject(args, 1, com.zhekasmirnov.innercore.api.mod.ui.window.IWindow.class));
                    return null;
                }
            });
            put("removeWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.removeWindow(wrapString(args, 0));
                    return null;
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });
            put("setWindowContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setWindowContent(wrapString(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("getWindowContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowContent(wrapString(args, 0));
                }
            });
            put("setBlockingBackground", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setBlockingBackground(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("getStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getStyle();
                }
            });
            put("isInventoryNeeded", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isInventoryNeeded();
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("refreshAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.refreshAll();
                    return null;
                }
            });
            put("setStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 1) {
                        self.setStyle(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("invalidateAllContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateAllContent();
                    return null;
                }
            });
            put("close", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.close();
                    return null;
                }
            });
            put("onBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.onBackPressed();
                }
            });
            put("isDynamic", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isDynamic();
                }
            });
            put("getContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContainer();
                }
            });
            put("moveOnTop", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.moveOnTop(wrapString(args, 0));
                    return null;
                }
            });
            put("setDebugEnabled", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setDebugEnabled(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("addWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.addWindow(wrapString(args, 0), wrapObject(args, 1, org.mozilla.javascript.ScriptableObject.class));
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("getElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getElements();
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("getWindowNames", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindowNames();
                }
            });
            put("invalidateElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateElements(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("setContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setContainer(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.container.UiAbstractContainer.class));
                    return null;
                }
            });
            put("open", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.open();
                    return null;
                }
            });
            put("frame", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.frame(wrapLong(args, 0));
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "WindowGroup";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    return new JsWindowGroup(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.WindowGroup());
                }

                @Override
                public String getClassName() {
                    return "WindowGroup";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            scope.put("WindowGroup", scope, global);
        }

    }

    public static class JsWindow extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Window.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final Window self;

        public JsWindow(Window self) {
            this.self = self;
            put("setEventListener", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setEventListener(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.window.IWindowEventListener.class));
                    return null;
                }
            });
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("getLocation", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getLocation();
                }
            });
            put("isNotFocusable", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isNotFocusable();
                }
            });
            put("invalidateForeground", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateForeground();
                    return null;
                }
            });
            put("invalidateDrawing", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateDrawing(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("setCloseOnBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setCloseOnBackPressed(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("removeAdjacentWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.removeAdjacentWindow(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.window.UIWindow.class));
                    return null;
                }
            });
            put("runCachePreparation", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.runCachePreparation(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("setBlockingBackground", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setBlockingBackground(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("isInventoryNeeded", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isInventoryNeeded();
                }
            });
            put("postOpen", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.postOpen();
                    return null;
                }
            });
            put("invalidateAllContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateAllContent();
                    return null;
                }
            });
            put("setDynamic", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setDynamic(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("onBackPressed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.onBackPressed();
                }
            });
            put("postBackgroundRefresh", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.postBackgroundRefresh();
                    return null;
                }
            });
            put("setTouchable", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setTouchable(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("isDynamic", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isDynamic();
                }
            });
            put("getContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContainer();
                }
            });
            put("forceRefresh", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.forceRefresh();
                    return null;
                }
            });
            put("getBackgroundProvider", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getBackgroundProvider();
                }
            });
            put("getContentProvider", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContentProvider();
                }
            });
            put("getScale", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getScale();
                }
            });
            put("getElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getElements();
                }
            });
            put("postElementRefresh", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.postElementRefresh();
                    return null;
                }
            });
            put("getParentWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getParentWindow();
                }
            });
            put("setAsGameOverlay", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setAsGameOverlay(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("frame", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.frame(wrapLong(args, 0));
                    return null;
                }
            });
            put("setContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setContent(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                    return null;
                }
            });
            put("isOpened", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isOpened();
                }
            });
            put("getContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getContent();
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("setInventoryNeeded", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setInventoryNeeded(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("putProperty", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.putProperty(wrapString(args, 0), wrapObject(args, 1, java.lang.Object.class));
                    return null;
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });
            put("getStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getStyle();
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("setStyle", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 1) {
                        self.setStyle(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("close", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.close();
                    return null;
                }
            });
            put("debug", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.debug();
                    return null;
                }
            });
            put("getProperty", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getProperty(wrapString(args, 0));
                }
            });
            put("setBackgroundColor", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setBackgroundColor(wrapInt(args, 0));
                    return null;
                }
            });
            put("setParentWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setParentWindow(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.window.IWindow.class));
                    return null;
                }
            });
            put("getEventListener", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getEventListener();
                }
            });
            put("invalidateBackground", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateBackground();
                    return null;
                }
            });
            put("setDebugEnabled", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setDebugEnabled(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("updateWindowLocation", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.updateWindowLocation();
                    return null;
                }
            });
            put("isBlockingBackground", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isBlockingBackground();
                }
            });
            put("getElementProvider", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getElementProvider();
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("preOpen", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.preOpen();
                    return null;
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("updateScrollDimensions", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.updateScrollDimensions();
                    return null;
                }
            });
            put("invalidateElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.invalidateElements(wrapBoolean(args, 0));
                    return null;
                }
            });
            put("setContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setContainer(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.container.UiAbstractContainer.class));
                    return null;
                }
            });
            put("updateWindowPositionAndSize", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.updateWindowPositionAndSize();
                    return null;
                }
            });
            put("addAdjacentWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.addAdjacentWindow(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.window.UIWindow.class));
                    return null;
                }
            });
            put("isTouchable", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isTouchable();
                }
            });
            put("open", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.open();
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "Window";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    if (args.length == 0) {
                        return new JsWindow(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Window());
                    }
                    if (args.length == 1) {
                        return new JsWindow(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Window(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class)));
                    }
                    throw new RuntimeException("Not method...");
                }

                @Override
                public String getClassName() {
                    return "Window";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            scope.put("Window", scope, global);
        }

    }

    public static class JsContainer extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Container.class;
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
                        fields.put(field.getName(), field);
                    }
                }
                super_clazz = super_clazz.getSuperclass();
            }

        }
        private final Container self;

        public JsContainer(Container self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("invalidateUI", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 0) {
                        self.invalidateUI();
                        return null;
                    }
                    if (args.length == 1) {
                        self.invalidateUI(wrapBoolean(args, 0));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("refreshSlots", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.refreshSlots();
                    return null;
                }
            });
            put("setBinding", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setBinding(wrapString(args, 0), wrapString(args, 1), wrapObject(args, 2, java.lang.Object.class));
                    return null;
                }
            });
            put("getFullSlot", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getFullSlot(wrapString(args, 0));
                }
            });
            put("getBinding", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getBinding(wrapString(args, 0), wrapString(args, 1));
                }
            });
            put("setId", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setId(wrapInt(args, 0));
                    return null;
                }
            });
            put("dropAt", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.dropAt((float) wrapDouble(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2));
                    return null;
                }
            });
            put("getWorkbenchFieldSize", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWorkbenchFieldSize();
                }
            });
            put("getText", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getText(wrapString(args, 0));
                }
            });
            put("setScale", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setScale(wrapString(args, 0), (float) wrapDouble(args, 1));
                    return null;
                }
            });
            put("_removeElement", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self._removeElement(wrapString(args, 0));
                    return null;
                }
            });
            put("isElementTouched", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isElementTouched(wrapString(args, 0));
                }
            });
            put("handleSlotToInventoryTransaction", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.handleSlotToInventoryTransaction(wrapString(args, 0), wrapInt(args, 1));
                    return null;
                }
            });
            put("addElementInstance", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.addElementInstance(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.elements.UIElement.class), wrapString(args, 1));
                    return null;
                }
            });
            put("handleBindingDirty", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.handleBindingDirty(wrapString(args, 0), wrapString(args, 1));
                    return null;
                }
            });
            put("getFieldSlot", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 1) {
                        return self.getFieldSlot(wrapInt(args, 0));
                    }
                    if (args.length == 2) {
                        return self.getFieldSlot(wrapInt(args, 0), wrapInt(args, 1));
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("dropSlot", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.dropSlot(wrapString(args, 0), (float) wrapDouble(args, 1), (float) wrapDouble(args, 2), (float) wrapDouble(args, 3));
                    return null;
                }
            });
            put("asScriptableField", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.asScriptableField();
                }
            });
            put("setWbSlotNamePrefix", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setWbSlotNamePrefix(wrapString(args, 0));
                    return null;
                }
            });
            put("setOnOpenListener", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setOnOpenListener(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.container.Container.OnOpenListener.class));
                    return null;
                }
            });
            put("isOpened", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isOpened();
                }
            });
            put("_addElement", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self._addElement(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.elements.UIElement.class), wrapString(args, 1));
                    return null;
                }
            });
            put("getWindow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getWindow();
                }
            });
            put("validateSlot", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.validateSlot(wrapString(args, 0));
                    return null;
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("getGuiScreen", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getGuiScreen();
                }
            });
            put("setOnCloseListener", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setOnCloseListener(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.container.Container.OnCloseListener.class));
                    return null;
                }
            });
            put("getId", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getId();
                }
            });
            put("sendChanges", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.sendChanges();
                    return null;
                }
            });
            put("notify", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notify();
                    return null;
                }
            });
            put("setSlot", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 4) {
                        self.setSlot(wrapString(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3));
                        return null;
                    }
                    if (args.length == 5) {
                        self.setSlot(wrapString(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapInt(args, 3), wrapObject(args, 4, com.zhekasmirnov.innercore.api.NativeItemInstanceExtra.class));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("clearSlot", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.clearSlot(wrapString(args, 0));
                    return null;
                }
            });
            put("validateAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.validateAll();
                    return null;
                }
            });
            put("hashCode", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.hashCode();
                }
            });
            put("isLegacyContainer", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.isLegacyContainer();
                }
            });
            put("invalidateUIElements", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 0) {
                        self.invalidateUIElements();
                        return null;
                    }
                    if (args.length == 1) {
                        self.invalidateUIElements(wrapBoolean(args, 0));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("setParent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setParent(wrapObject(args, 0, java.lang.Object.class));
                    return null;
                }
            });
            put("close", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.close();
                    return null;
                }
            });
            put("openAs", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.openAs(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.ui.window.IWindow.class));
                    return null;
                }
            });
            put("handleInventoryToSlotTransaction", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.handleInventoryToSlotTransaction(wrapInt(args, 0), wrapString(args, 1), wrapInt(args, 2));
                    return null;
                }
            });
            put("getParent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getParent();
                }
            });
            put("applyChanges", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.applyChanges();
                    return null;
                }
            });
            put("handleSlotToSlotTransaction", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.handleSlotToSlotTransaction(wrapString(args, 0), wrapString(args, 1), wrapInt(args, 2));
                    return null;
                }
            });
            put("getGuiContent", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getGuiContent();
                }
            });
            put("onWindowClosed", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.onWindowClosed();
                    return null;
                }
            });
            put("getSlot", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getSlot(wrapString(args, 0));
                }
            });
            put("getValue", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getValue(wrapString(args, 0));
                }
            });
            put("getSlotVisualImpl", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getSlotVisualImpl(wrapString(args, 0));
                }
            });
            put("getElement", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getElement(wrapString(args, 0));
                }
            });
            put("equals", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.equals(wrapObject(args, 0, java.lang.Object.class));
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("invalidateUIDrawing", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    if (args.length == 0) {
                        self.invalidateUIDrawing();
                        return null;
                    }
                    if (args.length == 1) {
                        self.invalidateUIDrawing(wrapBoolean(args, 0));
                        return null;
                    }
                    throw new RuntimeException("Not method...");
                }
            });
            put("setText", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setText(wrapString(args, 0), wrapString(args, 1));
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "Container";
        }

        @Override
        public Object unwrap() {
            return this.self;
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            final Field field = fields.get(name);
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
            final Field field = fields.get(name);
            if (field != null) {
                try {
                    return field.get(self);
                } catch (IllegalAccessException e) {
                }
            }
            return super.get(name, start);
        }

        public static void inject(ScriptableObject scope) {
            final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();
            final ScriptableObject global = new BaseScriptableClass() {
                @Override
                public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {
                    if (args.length == 0) {
                        return new JsContainer(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Container());
                    }
                    if (args.length == 1) {
                        return new JsContainer(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.UI.Container(wrapObject(args, 0, java.lang.Object.class)));
                    }
                    throw new RuntimeException("Not method...");
                }

                @Override
                public String getClassName() {
                    return "Container";
                }

                @Override
                public void put(String name, Scriptable start, Object value) {
                    final Field field = fields.get(name);
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
                    final Field field = fields.get(name);
                    if (field != null) {
                        try {
                            return field.get(null);
                        } catch (IllegalAccessException e) {
                        }
                    }
                    return super.get(name, start);
                }

            };

            global.put("initSaverId", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    Container.initSaverId();
                    return null;
                }
            });
            scope.put("Container", scope, global);
        }

    }

}
