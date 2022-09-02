package com.knightboost.apm.cpu.data;

import java.util.LinkedHashMap;

/**
 * TODO:
 *  避免装拆箱带来的性能消耗?
 */
public class TimeInState {

    public static final TimeInState EMPTY = new TimeInState();

    private final LinkedHashMap<Long,Long> frequencyTimes = new LinkedHashMap<>();

    public void setTime(long frequency,long time){
        frequencyTimes.put(frequency,time);
    }

    public long getTimeOfFrequency(long frequency){
        Long time = frequencyTimes.get(frequency);
        return time==null?0:time;
    }

    /**
     * 总的 时间
     * @return
     */
    public long cpuTime(){
        long total = 0;
        for (Long value : frequencyTimes.values()) {
            //usertime 时间单位为10ms
            total+=value*10;
        }
        return total;
    }
}
