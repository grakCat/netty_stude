package com.greak.netty_stude.proto;

import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Created on 2017/7/26.
 *
 * @author Alan
 * @since 1.0
 */
public class ToPb {

    public static void main(String[] args) throws ClassNotFoundException {
        //扫描路径
        String searchPackage = "com.greak.netty_stude.module;com.greak.netty_stude.pb";
        //保存地址
        String genToDir = "D:\\proto2.proto";
        //扫描注解
        String clazzName = "com.greak.netty_stude.proto.ProtobufMessage";
        //需要导入的包（一般不用）
        String importFiles = "com.geek.game.core.msgpb";
//        String searchPackage = args[0];
//        String genToDir = args[1];
//        String clazzName = args[2];
//        String importFiles = null;
//        if (args.length > 3) {
//            importFiles = args[3];
//        }
        java2PbMessage(searchPackage, importFiles, null, genToDir, Class.forName(clazzName).asSubclass(Annotation.class));
    }

    public static void java2PbMessage(String searchPackage, String importFiles, String pkg, String genToDir, Class<? extends Annotation> protoClazz) {
        String fileName = genToDir;
        String[] sps = searchPackage.split(";");
        List<Class<?>> classList = new ArrayList<>();
        for (String pk : sps) {
            Set<Class<?>> classes = ClassUtils.getAllClassByAnnotation(pk, protoClazz);
            classList.addAll(classes);
        }
        Class[] classes1 = classList.toArray(new Class[0]);
        Arrays.sort(classes1, Comparator.comparing(Class::getName));
        StringBuilder sb = new StringBuilder();
        if (importFiles != null) {
            String[] imports = importFiles.split(";");
            for (String imp : imports) {
                sb.append("import \"" + imp + "\";\n");
            }

            sb.append("\n\n");
        }
        //String fileName = dir + "/Protocol.proto";
        Arrays.asList(classes1).forEach(clazz -> {
            System.out.println(clazz);
            Schema<?> schema = RuntimeSchema.getSchema(clazz);

            Annotation annotation = clazz.getAnnotation(protoClazz);
            try {
                Class c = annotation.getClass();
                Object obj1 = c.getMethod("resp").invoke(annotation);
                Object obj2 = c.getMethod("messageType").invoke(annotation);
                Object obj3 = c.getMethod("cmd").invoke(annotation);
                if (!"0".equals(obj2.toString())) {
                    String note = String.format("//%s,messageType=%s,cmd=%s", (boolean) obj1 ? "响应" : "请求", obj2, obj3);
                    sb.append(note).append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            Java2Pb pbGen = new Java2Pb(schema, pkg).gen();
            String content = pbGen.toMesage();
            sb.append(content);
        });
        FileHelper.saveFile(fileName, sb.toString(), false);

    }
}
