package com.knightboost.apm.pagestartup

import com.knightboost.apm.model.Span

/**
 * tracer for trace page start up metric
 * 追踪页面启动性能的tracer
 */
interface IPageStartupTracer {

    /**
     * 页面启动流程开始
     */
    fun begin();

    /**
     * 页面启动流程结束
     */
    fun end();

    fun addSubSpan(span: Span)

    fun beginSubSpan(name: String, begin: Long): Span

}