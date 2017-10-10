package com.wugy.java.java8;

import org.junit.Test;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 使用stream api实现类似python中的range
 * <p>
 * wugy on 2017/10/10 10:23
 */
public class Range {

    public Stream<Long> range(final long start, long length, int step) {
        Supplier<Long> seed = new Supplier<Long>() {

            private long next = start;

            @Override
            public Long get() {
                long _next = next;
                next += step;
                return _next;
            }
        };
        return Stream.generate(seed).limit(length);
    }

    @Test
    public void testRange() {
        range(1, 10, 2).forEach(num -> System.out.println(num));
    }

}
