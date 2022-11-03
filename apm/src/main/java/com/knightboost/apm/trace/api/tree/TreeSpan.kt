package com.knightboost.apm.trace.api.tree

import com.knightboost.apm.trace.*
import com.knightboost.apm.trace.api.trace.Span
import java.util.LinkedList

fun SpanTracer.generateSpanTree(): TreeSpan {
    val spanTracer = this
    val children = spanTracer.children
    val root = spanTracer.root
    val map = mutableMapOf<String, MutableList<Span>>()
    val orphans = mutableListOf<Span>()
    for (span in children) {
        val parentSpanId = span.getSpanContext().parentSpanId
        if (parentSpanId != null) {
            if (map[parentSpanId] != null) {
                map[parentSpanId]!!.add(span)
            } else {
                map[parentSpanId] = mutableListOf()
                map[parentSpanId]!!.add(span)
            }
        } else {
            orphans.add(span)
        }
    }

    val rootNode = TreeSpan(root)
    val nodeQueue = LinkedList<TreeSpan>()
    nodeQueue.add(rootNode)
    while (nodeQueue.size > 0) {
        val treeSpan = nodeQueue.pollFirst()!!
        val childSpans = map.get(treeSpan.getSpanContext().spanId)
        childSpans?.let {
            for (span in it) {
                val childSpan = TreeSpan(span)
                treeSpan.children.add(childSpan)
                nodeQueue.add(childSpan)
            }
        }
    }

    return rootNode;
}

class TreeSpan(val span: Span) : Span by span {

    val children = mutableListOf<TreeSpan>()

}