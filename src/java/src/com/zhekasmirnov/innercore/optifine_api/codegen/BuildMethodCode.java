package com.zhekasmirnov.innercore.optifine_api.codegen;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;

public class BuildMethodCode {
    public static boolean checkUniqueParameterCounts(List<Executable> methods) {
        final HashSet<Integer> parameterCountSet = new HashSet<>();

        for (Executable method : methods)
            if (!parameterCountSet.add(method.getParameterCount()))
                return false;

        return true;
    }

    private static HashMap<Integer, ArrayList<Executable>> getMethodsByParameterCount(List<Executable> methods) {
        final HashMap<Integer, ArrayList<Executable>> parameterMap = new HashMap<>();

        for (Executable method : methods)
            parameterMap.compute(method.getParameterCount(), new BiFunction<Integer, ArrayList<Executable>, ArrayList<Executable>>() {
                @Override
                public ArrayList<Executable> apply(Integer integer, ArrayList<Executable> executables) {
                    return new ArrayList<>();
                }
            }).add(method);

        return parameterMap;
    }

    private static Set<Integer> getUniqueParameterTypesByPosition(ArrayList<Executable> methods) {
        Set<Integer> pos = new HashSet<>();

        Class<?>[] parameters = methods.get(0).getParameterTypes();
        for(int i = 0;i < parameters.length;i++)
            for(Executable method : methods)
                if(!parameters[i].getName().equals(method.getParameterTypes()[i].getName()))
                    pos.add(i);

        return pos;
    }

    private static String genUse(String useClass, Executable method){
        String code = "";

        Class<?> retType = null;
        if(method instanceof Method)
            retType = ((Method) method).getReturnType();

        final String methodUse = useClass.replace("%s", method.getName().replaceAll("\\$", ".")+"("+buildArgs(method.getParameterTypes())+")");
        if(retType != null && retType.getSimpleName().equals("void")){
            code+=methodUse+"\n";
            code+="return null;\n";
        }else{
            code+="return "+methodUse+"\n";
        }

        return code;
    }

    private static String getInstanceType(String name){
        switch (name){
            case "java.lang.String":
                return "CharSequence";

            case "int":
            case "double":
            case "float":
            case "long":
            case "short":
                return "Number";
        }
        return name;
    }

    public static String genFullMethod(String useClass, List<Executable> methods){
        String code = "new ScriptableFunctionImpl() {\n";
        code += "@Override\n";
        code += "public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {\n";
        code += genMethod(useClass, methods);
        code += "}\n";
        return code + "}";
    }

    public static String genMethod(String useClass, List<Executable> methods){
        String code = "";

        if(checkUniqueParameterCounts(methods)){
            if(methods.size() == 1){
                code += genUse(useClass, methods.get(0));
            }else{
                for(Executable method : methods){
                    code += "if(args.length == "+method.getParameterCount()+"){\n";
                    code += genUse(useClass, method);
                    code += "}\n";
                }

                code += "throw new RuntimeException(\"Not method...\");\n";
            }
        }else{
            final HashMap<Integer, ArrayList<Executable>> map = getMethodsByParameterCount(methods);
            final HashMap<Integer, ArrayList<Executable>> fuckYou = new HashMap<>();

            for(Integer count : map.keySet()){
                final ArrayList<Executable> list = map.get(count);

                if(list.size() == 1){
                    final Executable method = list.get(0);
                    code += "if(args.length == "+method.getParameterCount()+"){\n";
                    code += genUse(useClass, method);
                    code += "}\n";
                }else
                    fuckYou.put(count, list);
            }


            for(Integer count : fuckYou.keySet()) {
                final ArrayList<Executable> list = fuckYou.get(count);
                final Set<Integer> UniqueParameters = getUniqueParameterTypesByPosition(list);

                code += "if(args.length == "+count+"){\n";
                for(Executable method : list){
                    Class<?>[] types = method.getParameterTypes();

                    String condition = "";
                    for(Integer pos : UniqueParameters){
                        if(!condition.isEmpty())
                            condition += " && ";
                        condition += "args["+pos+"] instanceof "+getInstanceType(types[pos].getName().replaceAll("\\$", "."));
                    }

                    code += "if("+condition+"){\n";
                    code += genUse(useClass, method);
                    code += "}\n";

                }
                code += "}\n";
            }

            code += "throw new RuntimeException(\"Not method...\");\n";
        }

        return code;
    }

    private static final String IntegerName = Integer.class.getName(),
            DoubleName = Double.class.getName(),
            FloatName = Float.class.getName(),
            BooleanName = Boolean.class.getName(),
            ByteName = Byte.class.getName(),
            ShortName = Short.class.getName(),
            LongName = Short.class.getName(),
            CharacterName = Character.class.getName(),
            RetNullName = "null",
            ObjectName = Object.class.getName();


    public static String getConvertCode(String retType, boolean toObject, int index){
        final String getValueCode = "args["+index+"]";
        retType = retType.replaceAll("\\$", ".");

        if(toObject){
            String convert_code = "("+ObjectName+") "+getValueCode;
            switch (retType) {
                case "int": convert_code = "(("+ObjectName+") "+IntegerName+".valueOf("+getValueCode+"))";break;
                case "double": convert_code = "(("+ObjectName+") "+DoubleName+".valueOf("+getValueCode+"))";break;
                case "float": convert_code = "(("+ObjectName+") "+FloatName+".valueOf("+getValueCode+"))";break;
                case "boolean": convert_code = "(("+ObjectName+") "+BooleanName+".valueOf("+getValueCode+"))";break;
                case "byte": convert_code = "(("+ObjectName+") "+ByteName+".valueOf("+getValueCode+"))";break;
                case "short": convert_code = "(("+ObjectName+") "+ShortName+".valueOf("+getValueCode+"))";break;
                case "long": convert_code = "(("+ObjectName+") "+LongName+".valueOf("+getValueCode+"))";break;
                case "char": convert_code = "(("+ObjectName+") "+CharacterName+".valueOf("+getValueCode+"))";break;
            }
            return convert_code;
        }else{
            String convert_code = "wrapObject(args, "+index+", "+retType+".class)";
            switch (retType){
                case "int": convert_code = "wrapInt(args,"+index+")";break;
                case "double": convert_code = "wrapDouble(args,"+index+")";break;
                case "float": convert_code = "(float) wrapDouble(args,"+index+")";break;
                case "boolean": convert_code = "wrapBoolean(args,"+index+")";break;
                case "byte": convert_code = "(("+ByteName+") "+getValueCode+").byteValue()";break;
                case "short": convert_code = "((Number) "+getValueCode+").shortValue()";break;
                case "long": convert_code = "wrapLong(args,"+index+")";break;
                case "char": convert_code = "(("+CharacterName+") "+getValueCode+").charValue()";break;
                case "java.lang.String": convert_code = "wrapString(args,"+index+")";break;
            }
            return convert_code;
        }
    }

    /**
     * В области должен быть массив args
     * @param argsTypes - типы аршументоа
     * @return - код аргументов
     */
    public static String buildArgs(Class<?>[] argsTypes){
        String code = "";
        int i = 0;
        for(Class<?> clazz : argsTypes){
            if(code.isEmpty()){
                code += getConvertCode(clazz.getTypeName(), false, i);
            }else
                code += ", "+getConvertCode(clazz.getTypeName(), false, i);

            i++;
        }
        return code;
    }
}
