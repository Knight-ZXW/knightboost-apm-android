/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.knightboost.apm.fps.jankstats



import android.os.Message
import android.view.Choreographer
import android.view.View
import androidx.annotation.RequiresApi
import com.knightboost.apm.fps.jankstats.*
import com.knightboost.apm.fps.jankstats.DelegatingOnPreDrawListener
import com.knightboost.apm.fps.jankstats.JankStatsApi16Impl
import com.knightboost.apm.fps.jankstats.OnFrameListenerDelegate

/**
 * This impl class exists only to provide extra asynchronous Message behavior available on API 22.
 */
@RequiresApi(22)
internal open class JankStatsApi22Impl(
    jankStats: JankStats,
    view: View
) : JankStatsApi16Impl(jankStats, view) {

    override fun createDelegatingOnDrawListener(
        view: View,
        choreographer: Choreographer,
        delegates: MutableList<OnFrameListenerDelegate>
    ): DelegatingOnPreDrawListener {
        return DelegatingOnPreDrawListener22(view, choreographer, delegates)
    }
}

@RequiresApi(22)
internal class DelegatingOnPreDrawListener22(
    decorView: View,
    choreographer: Choreographer,
    delegates: MutableList<OnFrameListenerDelegate>
) : DelegatingOnPreDrawListener(decorView, choreographer, delegates) {

    override fun setMessageAsynchronicity(message: Message) {
        message.isAsynchronous = true
    }
}