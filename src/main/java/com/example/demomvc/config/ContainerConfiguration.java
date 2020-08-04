package com.example.demomvc.config;

import com.example.demomvc.annotation.ZAutowire;
import com.example.demomvc.annotation.ZRepository;
import com.example.demomvc.annotation.ZService;
import com.example.demomvc.core.Container;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Map;

/**
 * @author hayden zhou
 * @date 2020/8/4 8:35 上午
 */
@Component
public class ContainerConfiguration {
    // 这里应该是在 dispatcherServlet 的 init 方法中实现。这里直接写到PostContruct里面 借助spring 的ioc 容器初始化当前实例之前调用
    @PostConstruct
    public void initContainer() throws Exception {
        // 设置自定义ioc 容器需要扫描的包
        String scanPackage = "com.example.demomvc.service";

        // 扫描
        scanPackage(scanPackage);

        // 装配
        autowire();

    }

    private void autowire() throws IllegalAccessException {
        for (Map.Entry<String, Object> entry : Container.getContainer().entrySet()) {
            if (entry.getValue().getClass().isAnnotationPresent(ZService.class)){
                Field[] fields = entry.getValue().getClass().getFields(); // 读取 Zrepository类中的filed
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ZAutowire.class)){ // 如果filed 存在Zautowire 注解 则注入 属性
                        ZAutowire annotation = field.getAnnotation(ZAutowire.class);

                        String identify = annotation.value();
                        if ("".equals(identify)){
                            identify = field.getType().getName();
                        }

                        field.setAccessible(true);
                        field.set(entry.getValue(), Container.getInstance().get(identify));
                    }
                }
            }
        }
    }

    private void scanPackage(String scanPackage) throws Exception  {
        // 读取改路径下的所有文件和目录
        URL url = this.getClass().getClassLoader().getResource(  scanPackage.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            String className = scanPackage + "." + file.getName().replaceAll(".class", "");
            if (file.getName().endsWith(".class")){ // 如果以class结尾
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(ZService.class)){ // 如果是Zservice
                    // 判断service value 是否存在
                    ZService annotation = clazz.getAnnotation(ZService.class);
                    String identify = annotation.value();
                    if (identify.equals("")) {
                        identify = clazz.getName();

                    }
                    Object instance =  clazz.newInstance();
                    Container.getInstance().put(identify,instance);

                    // 将interface 也作为identify 存入ioc 容器
                    for (Class<?> anInterface : clazz.getInterfaces()) {
                        Container.getInstance().put(anInterface.getName(), clazz.newInstance());
                    }
                }

                if (clazz.isAnnotationPresent(ZRepository.class)){ // 如果是ZRepository
                    // 判断service value 是否存在
                    ZRepository annotation = clazz.getAnnotation(ZRepository.class);
                    String identify = annotation.value();
                    if (identify.equals("")) {
                        identify = clazz.getName();
                    }
                    Container.getInstance().put(identify, clazz.newInstance());

                    // 将interface 也作为identify 存入ioc 容器
                    for (Class<?> anInterface : clazz.getInterfaces()) {
                        Container.getInstance().put(anInterface.getName(), clazz.newInstance());
                    }
                }
            }



            if (file.isDirectory()){
                scanPackage(className);
            }
        }
    }
}
