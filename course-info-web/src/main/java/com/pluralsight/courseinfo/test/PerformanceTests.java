package com.pluralsight.courseinfo.test;

public class PerformanceTests {
    public static long getMemoryUsage() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
