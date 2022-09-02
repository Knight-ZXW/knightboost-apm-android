package com.knightboost.apm.fps

import android.app.Activity
import androidx.annotation.WorkerThread
import com.knightboost.apm.fps.jankstats.FrameData
import java.lang.ref.WeakReference

interface FpsListener {
    @WorkerThread
    fun onFrameAggregate(
        weakReference: WeakReference<Activity>,
        refreshRate: Int,
        frames: List<FrameData>
    );

    @WorkerThread
    fun onJankFrame(
        activityRef: WeakReference<Activity>,
        refreshRate: Int,
        detectedWallTime:Long,
        frameData: FrameData
    );

}