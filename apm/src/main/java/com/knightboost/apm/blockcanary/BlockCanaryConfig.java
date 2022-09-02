package com.knightboost.apm.blockcanary;

public class BlockCanaryConfig {
    /**
     * 消息处理阻塞的判定阈值
     */
    private final int blockThresholdTime;
    /**
     * 消息阻塞的最大阈值，超过该阈值时 会立即生成 blocking 事件
     */
    private final int blockMaxThresholdTime;

    /**
     * 堆栈采样间隔
     */
    private final int stackSampleInterval;

    /**
     * 在Debugger的时候是否进行卡顿检测
     */
    private final boolean detectWhenDebuggerConnected;

    private final BlockDetectListener blockDetectListener;

    private BlockCanaryConfig(Builder builder) {
        this.blockThresholdTime =builder.blockThresholdTime;
        this.blockMaxThresholdTime = builder.blockMaxThresholdTime;
        this.stackSampleInterval = builder.stackSampleInterval;
        this.detectWhenDebuggerConnected = builder.detectWhenDebuggerConnected;

        this.blockDetectListener = builder.blockDetectListener;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(BlockCanaryConfig copy) {
        Builder builder = new Builder();
        builder.blockThresholdTime = copy.getBlockThresholdTime();
        builder.blockMaxThresholdTime = copy.getBlockMaxThresholdTime();
        builder.stackSampleInterval = copy.getStackSampleInterval();
        builder.detectWhenDebuggerConnected = copy.isDetectWhenDebuggerConnected();
        return builder;
    }

    public int getBlockThresholdTime() {
        return blockThresholdTime;
    }

    public int getBlockMaxThresholdTime() {
        return blockMaxThresholdTime;
    }

    public int getStackSampleInterval() {
        return stackSampleInterval;
    }

    public boolean isDetectWhenDebuggerConnected() {
        return detectWhenDebuggerConnected;
    }

    public BlockDetectListener getBlockDetectListener() {
        return blockDetectListener;
    }

    public static final class Builder {
        private int blockThresholdTime =16*15;
        private int blockMaxThresholdTime = 5000;
        private int stackSampleInterval =50;
        private boolean detectWhenDebuggerConnected = false;
        private BlockDetectListener blockDetectListener;

        private Builder() {
        }

        public Builder blockThresholdTime(int val) {
            blockThresholdTime = val;
            return this;
        }

        public Builder blockMaxThresholdTime(int val) {
            blockMaxThresholdTime = val;
            return this;
        }

        public Builder stackSampleInterval(int val) {
            stackSampleInterval = val;
            return this;
        }

        public Builder detectWhenDebuggerConnected(boolean val) {
            detectWhenDebuggerConnected = val;
            return this;
        }

        public Builder blockDetectListener(BlockDetectListener blockDetectListener){
            this.blockDetectListener = blockDetectListener;
            return  this;
        }

        public BlockCanaryConfig build() {
            return new BlockCanaryConfig(this);
        }


    }
}