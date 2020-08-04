package com.example.demomvc.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hayden zhou
 * @date 2020/8/4 8:28 上午
 */
public class Container {
    private static Map<String, Object> container = new HashMap<>();
    private static Container containerInstance = null;

    public synchronized static Container getInstance(){
        if (null == containerInstance) {
            containerInstance = new Container();
        }
        return containerInstance;
    }

    public static Map<String, Object> getContainer() {
        return container;
    }

    public void put(String identify, Object instance){
        container.put(identify, instance);
    }

    public Object get(String identify) {

        Object o = container.get(identify);
        return o;
    }
}
