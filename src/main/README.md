# 本项目实现简单的依赖注入

1. 首先在 创建一个 简单的 ioc `com.example.demomvc.core.Container`

2. 在 ContainerConfiguration 中 初始化 容器,
主要实现扫描 类 和 属性 上的自定义ZService、ZRepository注解 并在容器初始化的时候在有ZAutowire注解的属性自动注入需要的实例

```
 public void initContainer() throws Exception {
        // 设置自定义ioc 容器需要扫描的包
        String scanPackage = "com.example.demomvc.service";

        // 扫描
        scanPackage(scanPackage);

        // 装配
        autowire();

    }
```

// 自动注入
```
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
```

3.创建一个controller 测试自定义的 ioc 容器 和 依赖注入是否生效
```
// package 
@RequestMapping("user")
@RestController
public class UserConroller {


    @GetMapping("{name}")
    public String createUser(@PathVariable String name) {
        IUserService userService = ((IUserService) Container.getContainer().get("com.example.demomvc.service.IUserService"));
        return userService.createUser(name);
    }
}

```

4.测试结果
```
# 启动项目并访问 http://localhost:8080/user/hayden
# 结果： user: hayden created...
```

 