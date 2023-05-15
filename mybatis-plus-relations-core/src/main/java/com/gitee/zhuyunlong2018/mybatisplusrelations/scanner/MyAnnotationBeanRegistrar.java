package com.gitee.zhuyunlong2018.mybatisplusrelations.scanner;

import com.gitee.zhuyunlong2018.mybatisplusrelations.annotations.RelationScan;
import com.gitee.zhuyunlong2018.mybatisplusrelations.exceptions.ScanAnnotationsException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class MyAnnotationBeanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes =
                importingClassMetadata.getAnnotationAttributes(RelationScan.class.getName());
        if (annotationAttributes == null) {
            throw new ScanAnnotationsException("请在启动程序入口添加@RelationsScan");
        }
        String[] basePackages = (String[]) annotationAttributes.get("value");
        if (basePackages.length == 0) {
            basePackages = (String[]) annotationAttributes.get("basePackages");
        }
        if (basePackages.length > 0) {
            HashSet<String> packages = new HashSet<>();
            for (String basePackage : basePackages) {
                Set<String> subPackages = findPackages(basePackage);
                packages.addAll(subPackages);
            }
            // 扫描类文件
            new ScanRelationsAnnotations(packages);
        } else {
            throw new ScanAnnotationsException("请正确填写@RelationsScan要扫描的包路劲");
        }
    }

    public Set<String> findPackages(String packageName) {
        if (packageName.endsWith(".*")) {
            // 扫描包下一级目录
            String packagePrefix = packageName.substring(0, packageName.lastIndexOf(".*"));
            return getSubPackageNames(packagePrefix, false);
        }
        if (packageName.endsWith(".**")) {
            // 递归扫描所有包下的类
            String packagePrefix = packageName.substring(0, packageName.lastIndexOf(".**"));
            return getSubPackageNames(packagePrefix, true);
        }
        String[] parts = packageName.split("\\.\\*\\.");
        if (parts.length == 2) {
            String prefix = parts[0];
            String suffix = parts[1];
            return getSubPackageNames(prefix, false)
                    .stream()
                    .map(item -> getSubPackageNames(item, true))
                    .flatMap(Set::stream)
                    .filter(name -> name.endsWith("." + suffix))
                    .collect(Collectors.toSet());
        }
        String[] deepParts = packageName.split("\\.\\*\\*\\.");
        if (deepParts.length == 2) {
            String prefix = deepParts[0];
            String suffix = deepParts[1];
            return getSubPackageNames(prefix, true)
                    .stream().filter(name -> name.endsWith("." + suffix))
                    .collect(Collectors.toSet());
        }
        HashSet<String> set = new HashSet<>();
        set.add(packageName);
        return set;
    }

    public Set<String> getSubPackageNames(String packageName, Boolean deep) {
        String packagePath = packageName.replace('.', '/');
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            throw new ScanAnnotationsException("找不到包解析器" + packageName);
        }
        java.net.URL resource = loader.getResource(packagePath);
        if (resource == null) {
            throw new ScanAnnotationsException("找不到包路劲: " + packagePath);
        }
        String fullPath = resource.getFile();
        if (fullPath.contains("!")) {
            // 如果路径中包含 "!" 字符，则说明这是一个 Jar 包文件。
            JarURLConnection jarURLConnection = null;
            try {
                jarURLConnection = (JarURLConnection) resource.openConnection();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                throw new ScanAnnotationsException("jar包路径错误:" + packageName);
            }
//            String jarFilePath = fullPath.substring(0, fullPath.indexOf('!'));
            return getSubPackagesFromJar(jarURLConnection, packagePath);
        } else {
            // 否则，路径应该对应于一个目录。
            return getSubPackagesFromDir(fullPath, packageName, deep);
        }
    }

    private Set<String> getSubPackagesFromDir(String fullPath, String packageName, Boolean deep) {
        Set<String> subPackageNames = new HashSet<>();
        java.io.File dir = new java.io.File(fullPath);
        java.io.File[] files = dir.listFiles();
        for (java.io.File file : files) {
            if (file.isDirectory()) {
                String subPackageName = packageName + "." + file.getName();
                subPackageNames.add(subPackageName);
                if (deep) {
                    subPackageNames.addAll(getSubPackagesFromDir(file.getAbsolutePath(), subPackageName, deep));
                }
            }
        }
        return subPackageNames;
    }

    private Set<String> getSubPackagesFromJar(JarURLConnection jarURLConnection, String packagePath) {
        JarFile jarFile = null;
        try {
            jarFile = jarURLConnection.getJarFile();//only get "XXX.jar!/BOOT-INF/classes" ??
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ScanAnnotationsException("扫描jar路劲错误" + packagePath);
        }
        Set<String> subPackageNames = new HashSet<>();
        java.util.Enumeration<java.util.jar.JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            java.util.jar.JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.endsWith("/") && name.startsWith(packagePath)) {
                String subPackageName = name.substring(0, name.length() - 1).replace('/', '.');
                subPackageNames.add(subPackageName);
            }
        }
        return subPackageNames;
    }
}
