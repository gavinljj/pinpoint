package com.navercorp.pinpoint.collector.util;

import junit.framework.Assert;

import org.junit.Test;

import com.navercorp.pinpoint.collector.util.ConcurrentCounterMap;

import java.util.Map;

/**
 * @author emeroad
 */
public class ConcurrentCounterMapTest {
    @Test
    public void testIncrement() throws Exception {
        ConcurrentCounterMap<String> cache = new ConcurrentCounterMap<String>();
        cache.increment("a", 1L);
        cache.increment("a", 2L);
        cache.increment("b", 5L);


        Map<String,ConcurrentCounterMap.LongAdder> remove = cache.remove();
        Assert.assertEquals(remove.get("a").get(), 3L);
        Assert.assertEquals(remove.get("b").get(), 5L);

        cache.increment("a", 1L);
        Map<String, ConcurrentCounterMap.LongAdder> remove2 = cache.remove();
        Assert.assertEquals(remove2.get("a").get(), 1L);
    }

    @Test
    public void testIntegerMax() throws Exception {
        ConcurrentCounterMap<String> cache = new ConcurrentCounterMap<String>(16, Integer.MAX_VALUE);
        cache.increment("a", 1L);
        cache.increment("a", 2L);
        cache.increment("b", 5L);

    }

    @Test
    public void testIntegerMin() throws Exception {
        ConcurrentCounterMap<String> cache = new ConcurrentCounterMap<String>(16, Integer.MIN_VALUE);
        cache.increment("a", 1L);
        cache.increment("a", 2L);
        cache.increment("b", 5L);

    }

}