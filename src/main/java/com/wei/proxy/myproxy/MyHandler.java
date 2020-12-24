package com.wei.proxy.myproxy;

import java.lang.reflect.Method;

public class MyHandler implements MyInvocationHandler{

    private Man man;

    public MyHandler(Man man) {
        this.man = man;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object invoke = method.invoke(man, null);
        after();
        return invoke;
    }

    public void before() {
        System.out.println("------------------before invoke-------------------");
    }

    public void after() {
        System.out.println("------------------after invoke-------------------");
    }
}
