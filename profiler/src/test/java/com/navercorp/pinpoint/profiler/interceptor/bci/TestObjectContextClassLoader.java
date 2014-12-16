package com.navercorp.pinpoint.profiler.interceptor.bci;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author emeroad
 */
public class TestObjectContextClassLoader {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private int callA;

    public int callA() {
        logger.info("callA");
        int i = callA++;
        return i;
    }

    public String hello(String a) {
        System.out.println("a:" + a);
        System.out.println("test");
//        throw new RuntimeException("test");
        return "a";
    }

}