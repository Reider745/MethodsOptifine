package com.zhekasmirnov.innercore.optifine_api.codegen;

import com.zhekasmirnov.innercore.utils.FileTools;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
                "                    fields.put(field.getName(), field);\n" +
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
                "        final Field field = fields.get(name);\n" +
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
                "        final Field field = fields.get(name);\n" +
                "        if(field != null){\n" +
                "            try {\n" +
                "                return field.get("+name+");\n" +
                "            } catch (IllegalAccessException e) {\n" +
                "            }\n" +
                "        }\n" +
                "        return super.get(name, start);\n" +
                "    }\n\n";
        return code;
    }
    public static String gen(Class<?> clazz, boolean declaring){
        final String name = clazz.getSimpleName();
        String code = "";

        if(!declaring){
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
            code += "import org.mozilla.javascript.ScriptableObject;\n";
            code += "import org.mozilla.javascript.Context;\n";
            code += "import org.mozilla.javascript.Scriptable;\n";
            code += "import org.mozilla.javascript.Wrapper;\n";
            code += "import org.mozilla.javascript.UniqueTag;\n";
            code += "import org.mozilla.javascript.NativeJavaObject;\n";
            code += "import java.util.concurrent.ConcurrentHashMap;\n";
            code += "import java.lang.reflect.Field;\n";
            code += "import java.lang.reflect.Modifier;\n";
            code += "import java.lang.reflect.Method;\n";
            code += "import com.zhekasmirnov.innercore.optifine_api.codegen.BaseScriptableClass;\n\n";
        }

        if(declaring)
            code += "public static class Js"+name+" extends ScriptableObject implements Wrapper {\n\n";
        else {
            code += "public class Js" + name + " extends ScriptableObject implements Wrapper {\n\n";

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

        code += "private static final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();\n" +
                "    \n" +
                "    static {\n" +
                importFields(clazz.getName().replaceAll("\\$", ".")+".class")+"\n"+
                "    }";

        // not static method
        code += "private final "+name+" self;\n";
        code += "public Js"+name+"("+name+" self){\n";
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
                    code_methods.set(code_methods.get() + "put(\"" + method_name + "\", this, " + BuildMethodCode.genFullMethod("self.%s;", list) + ");\n");
            }
        });

        code += code_methods.get()+"\n";
        //code += importFields("self.getClass()", false)+"\n";
        code+="}\n\n";


        code += "@Override\n";
        code += "public String getClassName(){\n";
        code += "return \""+name+"\";\n";
        code += "}\n\n";

        code += "@Override\n";
        code += "public Object unwrap() {\n";
        code += "return this.self;\n";
        code += "}\n";

        code += getOverrideFields("self");


        // static method
        code += "public static void inject(ScriptableObject scope){\n";
        code += "final ConcurrentHashMap<String, Field> fields = new ConcurrentHashMap<>();\n";
        code += "final ScriptableObject global = new BaseScriptableClass(){\n";
        code += "@Override\n";
        code += "public Scriptable construct(Context context, Scriptable scriptable, Object[] args) {\n";
        code += BuildMethodCode.genMethod("new Js"+clazz.getSimpleName()+"(new %s);", Arrays.asList(clazz.getConstructors()));
        code += "}\n\n";
        code += "@Override\n";
        code += "public String getClassName(){\n";
        code += "return \""+name+"\";\n";
        code += "}\n";
        code += getOverrideFields("null");
        code += "};\n\n";

        AtomicReference<String> finalCode_methods = new AtomicReference<>("");
        staticMethods.forEach(new BiConsumer<String, ArrayList<Executable>>() {
            @Override
            public void accept(String method_name, ArrayList<Executable> list) {
                if(!method_name.equals("wait"))
                    finalCode_methods.set(finalCode_methods.get() + "global.put(\""+method_name+"\", global, "+BuildMethodCode.genFullMethod(clazz.getSimpleName()+".%s;", list)+");\n");
            }
        });
        code += finalCode_methods.get();

        Class<?> superClass = clazz;
        while(superClass != null) {
            for (Class<?> clazz_dec : superClass.getDeclaredClasses()) {
                if(!clazz_dec.isInterface() && !clazz_dec.isEnum() && (clazz_dec.getModifiers() & Modifier.PUBLIC) != 0)
                    code += "Js"+clazz_dec.getSimpleName()+".inject(global);\n";
            }
            superClass = superClass.getSuperclass();
        }

        code += "scope.put(\""+name+"\", scope, global);\n";
        code += "}\n\n";

        superClass = clazz;
        while(superClass != null) {
            for (Class<?> clazz_dec : superClass.getDeclaredClasses()) {
                if(!clazz_dec.isInterface() && !clazz_dec.isEnum() && (clazz_dec.getModifiers() & Modifier.PUBLIC) != 0)
                    code += ClassCode.gen(clazz_dec, true)+"\n\n\n";
            }
            superClass = superClass.getSuperclass();
        }

        code += "}";//end class
        return code;
    }

    public static void save(Class<?> clazz) throws IOException {
        FileTools.writeFileText(FileTools.DIR_PACK+"Js"+clazz.getSimpleName()+".java", ClassCode.gen(clazz, false));
    }
}
