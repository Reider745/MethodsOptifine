package com.zhekasmirnov.innercore.optifine_api.codegen;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.horizon.runtime.logger.Logger;
import com.zhekasmirnov.innercore.api.log.ICLog;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI;
import com.zhekasmirnov.innercore.utils.FileTools;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public class ClassCode {
    private static String importFields(String type){
        String code = "Class<?> super_clazz = "+type+";\n";
        code += "while(super_clazz != null) {\n" +
                "            Method[] methods = super_clazz.getMethods();\n" +
                "            for (Field field : super_clazz.getDeclaredFields()) {\n" +
                "                boolean added = true;\n" +
                "                for(Method method : methods)\n" +
                "                    if(method.getName().equals(field.getName())){\n" +
                "                        added = false;\n" +
                "                        break;\n" +
                "                    }\n" +
                "                if(added) {\n" +
                "                    field.setAccessible(true);\n" +
                "                    fields.put(field.getName(), new JsFieldOpti(field));\n" +
                "                }\n" +
                "            }\n" +
                "            super_clazz = super_clazz.getSuperclass();\n" +
                "        }\n";

        return code;
    }

    private static String getOverrideFields(String name){
        String code = "";
        code += "@Override\n" +
                "    public void put(String name, Scriptable start, Object value) {\n" +
                "        final JsFieldOpti field = fields.get(name);\n" +
                "        if(field != null){\n" +
                "            try {\n" +
                "                field.set("+name+", value);\n" +
                "            } catch (IllegalAccessException e) {\n" +
                "            }\n" +
                "        }\n" +
                "        super.put(name, start, value);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public Object get(String name, Scriptable start) {\n" +
                "        final JsFieldOpti field = fields.get(name);\n" +
                "        if(field != null){\n" +
                "            try {\n" +
                "                return field.get("+name+", this);\n" +
                "            } catch (IllegalAccessException e) {\n" +
                "            }\n" +
                "        }\n" +
                "        return super.get(name, start);\n" +
                "    }\n\n";
        return code;
    }

    private static final ArrayList<String> clazzSkip = new ArrayList<>(), initClass = new ArrayList<>();

    public static String getNameClass(Class<?> clazz){
        final String[] packages = clazz.getName().split("\\.");
        return packages[packages.length - 1].replace("$", "");
    }


    public static String getClassConverter(Class<?> retType, String defUse, AtomicBoolean is){
        if(retType == null || !retType.getName().startsWith("com.zhekasmirnov"))
            return defUse;

        if(retType.isPrimitive() || retType.isEnum() || retType.equals(String.class) || Scriptable.class.isAssignableFrom(retType) || Modifier.isPrivate(retType.getModifiers()) || Modifier.isProtected(retType.getModifiers()))
            return "%s";

        if(retType.isArray()){
            return "NativeJavaArray.wrap(scope, %s)";
        }

        if(retType.isInterface() || Modifier.isAbstract(retType.getModifiers())){
            is.set(true);
            return "ctx.getWrapFactory().wrap(ctx, scope, %s, %s.getClass())";
        }

        final String name = retType.getName();
        if(!clazzSkip.contains(name)){
            try{
                save(retType);
            }catch (IOException e){
                Logger.warning(ICLog.getStackTrace(e));
                return "%s";
            }
        }

        return "new Js" + getNameClass(retType) + "(%s)";
    }

    public static String gen(Class<?> clazz, boolean declaring){
        if(clazzSkip.contains(clazz.getName())){
            return null;
        }
        clazzSkip.add(clazz.getName());

        final String name = getNameClass(clazz);

        String code = "";

        if(!declaring){
            initClass.add(name);

            code += "package com.zhekasmirnov.innercore.optifine_api;\n\n";

            code += "import "+clazz.getName().replaceAll("\\$", ".")+";\n";
            Class<?> superClass = clazz;
            while(superClass != null) {
                for (Class<?> clazz_dec : superClass.getDeclaredClasses()) {
                    if(!clazz_dec.isInterface() && !clazz_dec.isEnum() && (clazz_dec.getModifiers() & Modifier.PUBLIC) != 0)
                        code += "import "+clazz_dec.getName().replaceAll("\\$", ".")+";\n";
                }
                superClass = superClass.getSuperclass();
            }
            code += "import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;\n";
            code += "import org.mozilla.javascript.*;\n";
            code += "import java.util.concurrent.ConcurrentHashMap;\n";
            code += "import java.lang.reflect.Field;\n";
            code += "import java.lang.reflect.Modifier;\n";
            code += "import java.lang.reflect.Method;\n";
            code += "import com.zhekasmirnov.innercore.optifine_api.codegen.JsFieldOpti;\n";
            code += "import com.zhekasmirnov.innercore.optifine_api.codegen.IClassInstance;\n";
            code += "import com.zhekasmirnov.innercore.optifine_api.codegen.JsTypesInit;\n";
            code += "import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;\n\n";
        }

        if(declaring)
            code += "public static class Js"+name+" extends ScriptableObject implements Wrapper, IClassInstance {\n\n";
        else {
            code += "public class Js" + name + " extends ScriptableObject implements Wrapper,IClassInstance {\n\n";

            code += "private static int wrapInt(Object[] args, int index){\n";
            code += "try{return ((Number) args[index]).intValue();}catch(ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e){return 0;}\n";
            code += "}\n\n";

            code += "private static double wrapDouble(Object[] args, int index){\n";
            code += "try{return ((Number) args[index]).doubleValue();}catch(ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e){return 0.0;}\n";
            code += "}\n\n";

            code += "private static boolean wrapBoolean(Object[] args, int index){\n";
            code += "try{return ((Boolean) args[index]).booleanValue();}catch(ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e){return false;}\n";
            code += "}\n\n";

            code += "private static long wrapLong(Object[] args, int index){\n";
            code += "try{return ((Number) args[index]).longValue();}catch(ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e){return 0L;}\n";
            code += "}\n\n";

            code += "private static <T>T wrapObject(Object[] args, int index, Class<T> clazz){\n";
            code += "try{Object object = args[index];while (object instanceof Wrapper){object = ((Wrapper) object).unwrap();}return (T) Context.jsToJava(object, clazz);}catch(ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e){return null;}\n";
            code += "}\n\n";

            code += "private static String wrapString(Object[] args, int index){\n";
            code += "Object value = args[index];try{if(value instanceof NativeJavaObject){value = ((NativeJavaObject) value).unwrap();} return ((CharSequence) value).toString();}catch(ClassCastException | ArrayIndexOutOfBoundsException | NullPointerException e){return value == null ? \"null\" : value.toString();}\n";
            code += "}\n\n";
        }

        code += "private static final ConcurrentHashMap<String, JsFieldOpti> fields = new ConcurrentHashMap<>();\n" +
                "    \n" +
                "    static {\n" +
                importFields(clazz.getName().replaceAll("\\$", ".")+".class")+"\n"+
                "    }";

        code += "public static void init(){\n" +
                "        JsTypesInit.register(new JsTypesInit.DefJsClass("+clazz.getSimpleName()+".class, new JsTypesInit.IBuilderClass() {\n" +
                "            @Override\n" +
                "            public Object call(Object arg) {\n" +
                "                return new Js"+name+"(("+clazz.getSimpleName()+") arg);\n" +
                "            }\n" +
                "        }));\n";
        Class<?> superClass = clazz;
        while(superClass != null) {
            for (Class<?> clazz_dec : superClass.getDeclaredClasses()) {
                if(!clazz_dec.isInterface() && !clazz_dec.isEnum() && (clazz_dec.getModifiers() & Modifier.PUBLIC) != 0)
                    code += "Js"+getNameClass(clazz_dec)+".init();\n";
            }
            superClass = superClass.getSuperclass();
        }

        code += "    }\n";

        // not static method
        code += "private final "+clazz.getSimpleName()+" self;\n";
        code += "public Js"+name+"("+clazz.getSimpleName()+" self){\n";
        code += "this.self = self;\n";

        final HashMap<String, ArrayList<Executable>> notStaticMethods = new HashMap<>();
        final HashMap<String, ArrayList<Executable>> staticMethods = new HashMap<>();
        for(Method method : clazz.getMethods()) {
            if((method.getModifiers() & Modifier.STATIC) == 0){
                ArrayList<Executable> list = notStaticMethods.get(method.getName());
                if(list == null)
                    list = new ArrayList<>();
                list.add(method);
                notStaticMethods.put(method.getName(), list);
            }else{
                ArrayList<Executable> list = staticMethods.get(method.getName());
                if(list == null)
                    list = new ArrayList<>();
                list.add(method);
                staticMethods.put(method.getName(), list);
            }

        }

        AtomicReference<String> code_methods = new AtomicReference<>("");
        notStaticMethods.forEach(new BiConsumer<String, ArrayList<Executable>>() {
            @Override
            public void accept(String method_name, ArrayList<Executable> list) {
                if(!method_name.equals("wait"))
                    code_methods.set(code_methods.get() + "put(\"" + method_name + "\", this, " + BuildMethodCode.genFullMethod("self.%s", list) + ");\n");
            }
        });

        code += code_methods.get()+"\n";
        //code += importFields("self.getClass()", false)+"\n";
        code+="}\n\n";


        code += "@Override\n";
        code += "public String getClassName(){\n";
        code += "return \"Object\";";
        code += "}\n\n";

        code += "@Override\n";
        code += "public Object unwrap() {\n";
        code += "return this.self;\n";
        code += "}\n";

        code += "   @Override\n" +
                "    public Object getSelf() {\n" +
                "        return self;\n" +
                "    }\n";

        code += "@Override\n" +
                "            public boolean hasInstance(Scriptable value) {\n" +
                "                if (value instanceof Wrapper) {\n" +
                "                    return com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI.PlayerActor.class.isInstance(((Wrapper) value).unwrap());\n" +
                "                }\n" +
                "                return false;\n" +
                "            }\n";

        code += getOverrideFields("self");


        // static method
        code += "public static void inject(ScriptableObject scope){\n";
        //code += "final ConcurrentHashMap<String, JsFieldOpti> fields = new ConcurrentHashMap<>();\n";
        code += "final ScriptableObject global = new BaseScriptableClass(){\n";
        code += "@Override\n";
        code += "public Scriptable construct(Context ctx, Scriptable scope, Object[] args) {\n";
        code += BuildMethodCode.genMethod("new %s", Arrays.asList(clazz.getConstructors()));
        code += "}\n\n";
        code += "@Override\n";
        code += "public String getClassName(){\n";
        code += "return \"JavaClass\";";
        code += "}\n";
        code += getOverrideFields("null");
        code += "};\n\n";

        AtomicReference<String> finalCode_methods = new AtomicReference<>("");
        staticMethods.forEach(new BiConsumer<String, ArrayList<Executable>>() {
            @Override
            public void accept(String method_name, ArrayList<Executable> list) {
                if(!method_name.equals("wait"))
                    finalCode_methods.set(finalCode_methods.get() + "global.put(\""+method_name+"\", global, "+BuildMethodCode.genFullMethod(clazz.getSimpleName()+".%s", list)+");\n");
            }
        });
        code += finalCode_methods.get();

        superClass = clazz;
        while(superClass != null) {
            for (Class<?> clazz_dec : superClass.getDeclaredClasses()) {
                if(!clazz_dec.isInterface() && !clazz_dec.isEnum() && (clazz_dec.getModifiers() & Modifier.PUBLIC) != 0)
                    code += "Js"+getNameClass(clazz_dec)+".inject(global);\n";
            }
            superClass = superClass.getSuperclass();
        }

        code += "scope.put(\""+name+"\", scope, global);\n";
        code += "}\n\n";

        superClass = clazz;
        while(superClass != null) {
            for (Class<?> clazz_dec : superClass.getDeclaredClasses()) {
                if(!clazz_dec.isInterface() && !clazz_dec.isEnum() && Modifier.isPublic(clazz_dec.getModifiers())) {
                    final String temp = ClassCode.gen(clazz_dec, true);
                    if(temp != null)
                        code += temp + "\n\n\n";
                }
            }
            superClass = superClass.getSuperclass();
        }

        code += "}";//end class
        return code;
    }

    public static String genInitClass(){
        String code = "package com.zhekasmirnov.innercore.optifine_api;\n\n";

        code += "public class InitClasses {\n";

        code += "   public static void init(){\n";
        for(String init : initClass){
            code += "       Js"+init+".init();\n";
        }
        code += "   }\n";

        code += "}\n";

        return code;
    }

    private static void save(Class<?> clazz) throws IOException {
        final String text = ClassCode.gen(clazz, false);
        if(text != null)
            FileTools.writeFileText(FileTools.DIR_PACK+"js/Js"+getNameClass(clazz)+".java", text);
    }

    public static void genApi() throws IOException {
        save(AdaptedScriptAPI.Level.class);
        save(AdaptedScriptAPI.Entity.class);
        save(AdaptedScriptAPI.Translation.class);
        save(AdaptedScriptAPI.Updatable.class);
        save(AdaptedScriptAPI.Particles.class);

        save(AdaptedScriptAPI.GenerationUtils.class);
        save(AdaptedScriptAPI.Player.class);
        save(AdaptedScriptAPI.IDRegistry.class);
        save(AdaptedScriptAPI.Recipes.class);
        save(NativeBlockSource.class);

        save(AdaptedScriptAPI.UI.class);
        save(AdaptedScriptAPI.Block.class);
        save(AdaptedScriptAPI.Item.class);
        save(AdaptedScriptAPI.GameController.class);
        save(AdaptedScriptAPI.Saver.class);

        save(AdaptedScriptAPI.Logger.class);
        save(AdaptedScriptAPI.PlayerActor.class);
        save(AdaptedScriptAPI.ItemContainer.class);

        FileTools.writeFileText(FileTools.DIR_PACK+"js/InitClasses.java", genInitClass());
    }
}
