package com.wei.proxy.myproxy;

public class Client {

    public static void main(String[] args) {
        Man man = new ManEntity();
        MyHandler myHandler = new MyHandler(man);
        Man proxyMan = (Man) MyProxy.newProxyInstance(
                new MyClassLoader("F:\\code\\demo\\dynamic-proxy\\src\\main\\java\\com\\wei\\proxy\\myproxy", "com.wei.proxy.myproxy"),
                Man.class, myHandler);
        System.out.println(proxyMan.getClass().getName());
        try {
            proxyMan.findObject();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
