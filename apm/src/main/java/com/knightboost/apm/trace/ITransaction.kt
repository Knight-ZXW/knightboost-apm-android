package com.knightboost.apm.trace

import com.knightboost.apm.trace.api.trace.Span

interface ITransaction : Span {
    /**
     * Sets the transaction name
     * @param name transaction name
     */
    fun setName(name:String)

    /**
     * Returns transaction name
     */
    fun getName(): String

}