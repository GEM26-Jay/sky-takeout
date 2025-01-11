package com.sky.context;

public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
//        System.out.println("添加threadlocal值");
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
//        System.out.println("删除threadlocal的值: "+threadLocal.get());
        threadLocal.remove();
    }

}
