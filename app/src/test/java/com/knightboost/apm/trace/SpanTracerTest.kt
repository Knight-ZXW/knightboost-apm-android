package com.knightboost.apm.trace

import com.knightboost.apm.trace.api.tree.generateSpanTree
import org.junit.Assert.*

import org.junit.Test

class SpanTracerTest {

    @Test
    fun createTracer() {
        val tracer = SpanTracer.createTracer("pageLaunch")
        val onCreateSpan = tracer.childSpan("onCreate")
            .startSpan()
        Thread.sleep(1000L)
        onCreateSpan.finish()

        val onStartSpan = tracer.childSpan("onStart")
            .startSpan()
        Thread.sleep(500L)
        onStartSpan.finish()

        Thread.sleep(100L)


        val onResumeSpan = tracer.childSpan("onResume")
            .startSpan()
        Thread.sleep(100L)
        val fetchDisCacheSpan = onResumeSpan
            .childSpan("fetchDiskCache")
            .startSpan()
        Thread.sleep(100L)
        fetchDisCacheSpan.finish()
        Thread.sleep(100L)
        onResumeSpan.finish()

        onResumeSpan as SdkSpan
        assertTrue(onResumeSpan.costMs()>=300)


        tracer.finish()
        tracer.toString()
        assertEquals(tracer.children.size,4)
        assertEquals(tracer.isFinished(),true)

        val treeSpan = tracer.generateSpanTree()
        assertEquals(treeSpan.children.size,3)
    }
}