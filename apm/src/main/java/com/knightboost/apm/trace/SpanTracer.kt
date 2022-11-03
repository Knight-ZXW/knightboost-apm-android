package com.knightboost.apm.trace

import com.knightboost.apm.common.util.AnchoredClock
import com.knightboost.apm.trace.api.trace.Span
import com.knightboost.apm.trace.api.trace.SpanBuilder
import com.knightboost.apm.trace.api.trace.SpanContext
import java.util.Date
import java.util.concurrent.CopyOnWriteArrayList

class SpanTracer : ITransaction {

    companion object{

        @JvmStatic
        fun createTracer(name:String): SpanTracer {
            var spanTracer = SpanTracer(
                name, TracerContext(IdGenerator.defaultInstance()), AnchoredClock.create()
            )
            return spanTracer;
        }

    }

    private var name: String = ""
    var root: SdkSpan
     private set

    val clock:AnchoredClock;
    val children: MutableList<SdkSpan> = CopyOnWriteArrayList()

    override fun childSpan(operation: String): SpanBuilder {
        return SdkSpanBuilder(operation,this)
            .setParent(root.getSpanContext())
    }

    fun childSpan(parentSpan: Span,operation: String):SpanBuilder{
        return SdkSpanBuilder(operation,this)
            .setParent(parentSpan)
    }


    constructor(
        name: String,
        tracerSharedState: TracerContext,
        startAnchor: AnchoredClock
    ) {
        val startTime = startAnchor.startTime()
        val traceId = tracerSharedState.idGenerator.generateTraceId()
        this.name = name
        this.clock = startAnchor
        this.root = SdkSpan(
            SpanContext(
                traceId,
                IdGenerator.defaultInstance().generateSpanId(),
                null
            ), this, name,
            startAnchor, startTime
        )
    }

    override fun setName(name: String) {
        this.name = name
    }

    override fun getName():String {
        return name
    }


    fun addChild(span:SdkSpan){
        children.add(span)
    }

    override fun finish() {
        if (!root.isFinished()){
            //TODO check whether all child span is finished
            root.finish()
        }
    }

    override fun setOperation(operation: String) {
        root.setOperation(operation)
    }

    override fun getOperation(): String {
        return root.getOperation()
    }

    override fun setThrowable(throwable: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun getThrowable(): Throwable? {
        TODO("Not yet implemented")
    }

    override fun setTag(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun getTag(key: String) {
        TODO("Not yet implemented")
    }

    override fun isFinished(): Boolean {
        return root.isFinished()
    }

    override fun setData(key: String, value: Any) {
        if (root.isFinished()){
            return
        }
        root.setData(key,value)
    }

    override fun getData(key: String): Any? {
       return  this.root.getData(key)
    }

    override fun getSpanContext(): SpanContext {
        return  this.root.getSpanContext()
    }

    override fun toString(): String {
        return super.toString()
    }

    private fun readableToString():String{
        return """
            begin: ${root.getStartEpoch()} end:${root.getEndEpoch()}}
        """.trimIndent()

    }
}