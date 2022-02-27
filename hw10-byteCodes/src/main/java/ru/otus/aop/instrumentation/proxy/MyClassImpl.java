package ru.otus.aop.instrumentation.proxy;


public class MyClassImpl {

    @Log
    public void secureAccess(String param, String param2, Integer i) {
        System.out.println("secureAccess, param:" + param + " " + param2 + " " + i);
    }

    @Log
    public void secureAccess2() {
        System.out.println("secureAccess, without params");
    }

    @Override
    public String toString() {
        return "MyClassImpl{}";
    }
}
