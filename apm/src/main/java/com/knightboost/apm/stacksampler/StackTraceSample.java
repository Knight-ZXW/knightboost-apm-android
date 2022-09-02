package com.knightboost.apm.stacksampler;

import java.io.Serializable;

public class StackTraceSample implements Serializable {
    /**
     * 堆栈采集对应的时钟时间
     */
    private final long wallTime;
    public StackTraceElement[] stackTraceElements;

    public StackTraceSample(long wallTime,
                            StackTraceElement[] stackTraceElements) {
        this.wallTime = wallTime;
        this.stackTraceElements = stackTraceElements;
    }

    public long getWallTime() {
        return wallTime;
    }

    public StackTraceElement[] getStackTraceElements() {
        return stackTraceElements;
    }
}