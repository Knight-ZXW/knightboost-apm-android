package com.knightboost.apm.common.util;


/**
 * A utility for returning wall times anchored to a given point in time. Wall time measurements will
 * not be taken from the system, but instead are computed by adding {@linkplain System#nanoTime()
 * monotonic time} to the anchor point.
 *
 * <p>This is needed because Java has lower granularity for epoch times and tracing events are
 * recorded more often. There is also a performance improvement in avoiding referencing the system's
 * wall time where possible. Instead of computing a true wall time for every timestamp within a
 * trace, we compute it once at the local root and then anchor all descendant span timestamps to
 * this root's timestamp.
 */
public class AnchoredClock {

    private final long epochNanos;
    private final  long nanoTime;
    public AnchoredClock(long epochNanos,long nanoTime){
        this.epochNanos = epochNanos;
        this.nanoTime = nanoTime;
    }

    public static AnchoredClock create(){
        return new AnchoredClock(Clock.now(),Clock.nanoTime());
    }

    public static AnchoredClock create(long epochNanos,long nanoTime){
        return  new AnchoredClock(epochNanos,nanoTime);
    }

    public long now(){
        long deltaNanos = Clock.nanoTime()-this.nanoTime;
        return epochNanos+deltaNanos;
    }

    public long nowInMills(){
        long deltaNanos = Clock.nanoTime()-this.nanoTime;
        return (epochNanos+deltaNanos)/1000;
    }

    /** Returns the start time in nanos of this {@link AnchoredClock} */
    public long startTime(){
        return epochNanos;
    }

}
