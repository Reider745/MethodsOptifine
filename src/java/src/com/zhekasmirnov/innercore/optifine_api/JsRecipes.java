package com.zhekasmirnov.innercore.optifine_api;

import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Recipes;
import com.zhekasmirnov.innercore.api.mod.recipes.RecipeRegistry.WorkbenchUIHandler;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import org.mozilla.javascript.*;

import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;

public class JsRecipes extends ScriptableObject implements Wrapper {

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
        Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Recipes.class;
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
    private final Recipes self;

    public JsRecipes(Recipes self) {
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
        return "Recipes";
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
                return new JsRecipes(new com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.Recipes());
            }

            @Override
            public String getClassName() {
                return "Recipes";
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

        global.put("addFurnaceFuel", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Recipes.addFurnaceFuel(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return null;
            }
        });
        global.put("addShapeless", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.addShapeless(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 1, org.mozilla.javascript.NativeArray.class), wrapObject(args, 2, org.mozilla.javascript.Function.class), wrapString(args, 3));
            }
        });
        global.put("getAllFurnaceRecipes", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.getAllFurnaceRecipes();
            }
        });
        global.put("getRecipeResult", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.getRecipeResult(wrapObject(args, 0, java.lang.Object.class), wrapString(args, 1));
            }
        });
        global.put("getWorkbenchRecipesByIngredient", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.getWorkbenchRecipesByIngredient(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("provideRecipeForPlayer", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.provideRecipeForPlayer(wrapObject(args, 0, java.lang.Object.class), wrapString(args, 1), wrapObject(args, 2, java.lang.Object.class));
            }
        });
        global.put("getAllWorkbenchRecipes", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.getAllWorkbenchRecipes();
            }
        });
        global.put("addShapelessVanilla", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.addShapelessVanilla(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 1, org.mozilla.javascript.NativeArray.class), wrapObject(args, 2, org.mozilla.javascript.Function.class), wrapString(args, 3));
            }
        });
        global.put("getFuelBurnDuration", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.getFuelBurnDuration(wrapInt(args, 0), wrapInt(args, 1));
            }
        });
        global.put("getFurnaceRecipeResult", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.getFurnaceRecipeResult(wrapInt(args, 0), wrapInt(args, 1), wrapString(args, 2));
            }
        });
        global.put("addShapedGeneric", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.addShapedGeneric(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 1, org.mozilla.javascript.NativeArray.class), wrapObject(args, 2, org.mozilla.javascript.NativeArray.class), wrapObject(args, 3, org.mozilla.javascript.Function.class), wrapString(args, 4), wrapBoolean(args, 5));
            }
        });
        global.put("addShaped2", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.addShaped2(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, org.mozilla.javascript.NativeArray.class), wrapObject(args, 4, org.mozilla.javascript.NativeArray.class), wrapObject(args, 5, org.mozilla.javascript.Function.class), wrapString(args, 6));
            }
        });
        global.put("getFurnaceRecipesByResult", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.getFurnaceRecipesByResult(wrapInt(args, 0), wrapInt(args, 1), wrapString(args, 2));
            }
        });
        global.put("__placeholder", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Recipes.__placeholder();
                return null;
            }
        });
        global.put("addShapedVanilla", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.addShapedVanilla(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 1, org.mozilla.javascript.NativeArray.class), wrapObject(args, 2, org.mozilla.javascript.NativeArray.class), wrapObject(args, 3, org.mozilla.javascript.Function.class), wrapString(args, 4));
            }
        });
        global.put("getWorkbenchRecipesByResult", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.getWorkbenchRecipesByResult(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
            }
        });
        global.put("addShapelessGeneric", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.addShapelessGeneric(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 1, org.mozilla.javascript.NativeArray.class), wrapObject(args, 2, org.mozilla.javascript.Function.class), wrapString(args, 3), wrapBoolean(args, 4));
            }
        });
        global.put("addFurnace", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Recipes.addFurnace(wrapObject(args, 0, java.lang.Object.class), wrapObject(args, 1, java.lang.Object.class), wrapObject(args, 2, java.lang.Object.class), wrapObject(args, 3, java.lang.Object.class), wrapObject(args, 4, java.lang.Object.class));
                return null;
            }
        });
        global.put("removeWorkbenchRecipe", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Recipes.removeWorkbenchRecipe(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2));
                return null;
            }
        });
        global.put("getRecipeByUid", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.getRecipeByUid(wrapObject(args, 0, java.lang.Object.class));
            }
        });
        global.put("removeFurnaceRecipe", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Recipes.removeFurnaceRecipe(wrapInt(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        global.put("provideRecipe", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.provideRecipe(wrapObject(args, 0, java.lang.Object.class), wrapString(args, 1));
            }
        });
        global.put("getRecipeByField", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.getRecipeByField(wrapObject(args, 0, java.lang.Object.class), wrapString(args, 1));
            }
        });
        global.put("addShaped", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.addShaped(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 1, org.mozilla.javascript.NativeArray.class), wrapObject(args, 2, org.mozilla.javascript.NativeArray.class), wrapObject(args, 3, org.mozilla.javascript.Function.class), wrapString(args, 4));
            }
        });
        global.put("addShapeless2", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                return Recipes.addShapeless2(wrapInt(args, 0), wrapInt(args, 1), wrapInt(args, 2), wrapObject(args, 3, org.mozilla.javascript.NativeArray.class), wrapObject(args, 4, org.mozilla.javascript.Function.class), wrapString(args, 5));
            }
        });
        global.put("deleteRecipe", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Recipes.deleteRecipe(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class));
                return null;
            }
        });
        global.put("removeFurnaceFuel", global, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                Recipes.removeFurnaceFuel(wrapInt(args, 0), wrapInt(args, 1));
                return null;
            }
        });
        JsWorkbenchUIHandler.inject(global);
        scope.put("Recipes", scope, global);
    }

    public static class JsWorkbenchUIHandler extends ScriptableObject implements Wrapper {

        private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();

        static {
            Class<?> super_clazz = com.zhekasmirnov.innercore.api.mod.recipes.RecipeRegistry.WorkbenchUIHandler.class;
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
        private final WorkbenchUIHandler self;

        public JsWorkbenchUIHandler(WorkbenchUIHandler self) {
            this.self = self;
            put("getClass", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.getClass();
                }
            });
            put("notifyAll", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.notifyAll();
                    return null;
                }
            });
            put("refresh", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.refresh();
                }
            });
            put("deselectCurrentRecipe", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.deselectCurrentRecipe();
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
            put("setPrefix", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setPrefix(wrapString(args, 0));
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
            put("refreshAsync", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.refreshAsync();
                    return null;
                }
            });
            put("setOnRefreshListener", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setOnRefreshListener(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchUIHandler.IRefreshListener.class));
                    return null;
                }
            });
            put("toString", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    return self.toString();
                }
            });
            put("setMaximumRecipesToShow", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setMaximumRecipesToShow(wrapInt(args, 0));
                    return null;
                }
            });
            put("setOnSelectionListener", this, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    self.setOnSelectionListener(wrapObject(args, 0, com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchUIHandler.ISelectionListener.class));
                    return null;
                }
            });

        }

        @Override
        public String getClassName() {
            return "WorkbenchUIHandler";
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
                    return new JsWorkbenchUIHandler(new com.zhekasmirnov.innercore.api.mod.recipes.RecipeRegistry.WorkbenchUIHandler(wrapObject(args, 0, org.mozilla.javascript.ScriptableObject.class), wrapObject(args, 1, com.zhekasmirnov.innercore.api.mod.ui.container.Container.class), wrapObject(args, 2, com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchField.class)));
                }

                @Override
                public String getClassName() {
                    return "WorkbenchUIHandler";
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

            global.put("__placeholder", global, new ScriptableFunctionImpl() {
                @Override
                public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                    WorkbenchUIHandler.__placeholder();
                    return null;
                }
            });
            scope.put("WorkbenchUIHandler", scope, global);
        }

    }

}