package com.zyl.mybatisplus.relations.scanner;

import com.zyl.mybatisplus.relations.annotations.BindMany;
import com.zyl.mybatisplus.relations.annotations.BindOne;
import com.zyl.mybatisplus.relations.annotations.ManyBindMany;
import com.zyl.mybatisplus.relations.exceptions.ScanAnnotationsException;
import com.zyl.mybatisplus.relations.resolver.BindManyResolver;
import com.zyl.mybatisplus.relations.resolver.BindOneResolver;
import com.zyl.mybatisplus.relations.resolver.ManyBindManyResolver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包扫描器
 */
public class ScanRelationsAnnotations {

    public ScanRelationsAnnotations(Set<String> entityPackages) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        for (String entityPackage : entityPackages) {
            String packagePath = entityPackage.replace(".", "/");
            URL url = loader.getResource(packagePath);
            if (url == null) {
                continue;
            }
            String protocol = url.getProtocol();
            if (protocol.equals("file")) {
                File file = new File(url.getPath());
                File[] files = file.listFiles();
                assert files != null;
                for (File childFile : files) {
                    String fileName = childFile.getName();
                    if (fileName.endsWith(".class") && !fileName.contains("$")) {
                        String className = entityPackage + "." + fileName.substring(0, fileName.length() - 6);
                        autoMapperBean(className);
                    }
                }
            } else if (protocol.equals("jar")) {//JAR
                JarURLConnection jarURLConnection = null;
                try {
                    jarURLConnection = (JarURLConnection) url.openConnection();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                if (jarURLConnection == null) {
                    continue;
                }
                JarFile jarFile = null;
                try {
                    jarFile = jarURLConnection.getJarFile();//only get "XXX.jar!/BOOT-INF/classes" ??
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (jarFile == null) {
                    continue;
                }
                Enumeration<JarEntry> jarEntries = jarFile.entries();
                while (jarEntries.hasMoreElements()) {
                    JarEntry jarEntry = jarEntries.nextElement();
                    String jarEntryName = jarEntry.getName();
                    if (jarEntryName.startsWith(packagePath) && jarEntryName.endsWith(".class")) {
                        String className =
                                jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll(
                                        "/", ".");
                        autoMapperBean(className);
                    }
                }
            }
        }
    }

    private void autoMapperBean(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length == 0) {
                return;
            }
            for (Field field : fields) {
                BindMany bindMany = field.getAnnotation(BindMany.class);
                if (bindMany != null) {
                    BindManyResolver hasManyResolver = new BindManyResolver(bindMany, clazz);
                    hasManyResolver.resolve(field);
                    continue;
                }
                BindOne bindOne = field.getAnnotation(BindOne.class);
                if (bindOne != null) {
                    BindOneResolver hasOneResolver = new BindOneResolver(bindOne, clazz);
                    hasOneResolver.resolve(field);
                    continue;
                }
                ManyBindMany manyBindMany = field.getAnnotation(ManyBindMany.class);
                if (manyBindMany != null) {
                    ManyBindManyResolver manyBindManyResolver = new ManyBindManyResolver(manyBindMany, clazz);
                    manyBindManyResolver.resolve(field);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ScanAnnotationsException("Error in scan entity bean");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
