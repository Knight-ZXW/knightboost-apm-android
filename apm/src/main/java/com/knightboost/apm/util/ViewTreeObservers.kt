/**
 * copy from papa project
 * related blog: https://dev.to/pyricau/android-vitals-first-draw-time-m1d
 */
package com.knightboost.apm.util

import android.app.Activity
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import curtains.*
import java.lang.ref.WeakReference


/**
 * Calls back exactly once, when the next global layout happens for [this] activity's
 * [android.view.Window].
 */
internal fun Activity.onNextGlobalLayout(callback: () -> Unit) {
    val wrapper = OnGlobalLayoutListenerWrapper()
    ViewTreeObservers.installListener(window, wrapper, callback)
}

/**
 * Calls back exactly once, when the next pre draw happens for [this] window.
 */
internal fun Window.onNextPreDraw(callback: () -> Unit) {
    val wrapper = OnPreDrawListenerWrapper()
    ViewTreeObservers.installListener(this, wrapper, callback)
}

internal fun Activity.onNextTouchEvent(callback: (MotionEvent) -> Unit) {
    window.touchEventInterceptors += object : OnTouchEventListener {
        override fun onTouchEvent(motionEvent: MotionEvent) {
            callback(motionEvent)
            window.touchEventInterceptors -= this
        }
    }
}

private interface ViewTreeObserverListenerWrapper<T>{
    fun wrap(callback:()->Unit):T

    fun addListener(
        viewTreeObserver: ViewTreeObserver,
        listener:T
    )

    fun removeListener(
        viewTreeObserver: ViewTreeObserver,
        listener: T
    )
}

private class OnGlobalLayoutListenerWrapper:
        ViewTreeObserverListenerWrapper<OnGlobalLayoutListener>{
    override fun wrap(callback: () -> Unit): OnGlobalLayoutListener {
        return OnGlobalLayoutListener {
            callback()
        }
    }

    override fun addListener(viewTreeObserver: ViewTreeObserver,
        listener: OnGlobalLayoutListener) {
        viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    override fun removeListener(viewTreeObserver: ViewTreeObserver, listener: OnGlobalLayoutListener) {
        viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }
}

private class OnPreDrawListenerWrapper : ViewTreeObserverListenerWrapper<ViewTreeObserver.OnPreDrawListener> {
    override fun wrap(callback: () -> Unit): ViewTreeObserver.OnPreDrawListener {
        return ViewTreeObserver.OnPreDrawListener {
            callback()
            return@OnPreDrawListener true
        }
    }

    override fun addListener(
        viewTreeObserver: ViewTreeObserver,
        listener: ViewTreeObserver.OnPreDrawListener
    ) {
        viewTreeObserver.addOnPreDrawListener(listener)
    }

    override fun removeListener(
        viewTreeObserver: ViewTreeObserver,
        listener: ViewTreeObserver.OnPreDrawListener
    ) {
        viewTreeObserver.removeOnPreDrawListener(listener)
    }
}


private object ViewTreeObservers {

    fun <T : Any> installListener(
        window: Window,
        wrapper: ViewTreeObserverListenerWrapper<T>,
        callback: () -> Unit
    ) {
        val installListener = {
            val rootView = window.decorView.rootView
            val viewRef = WeakReference(rootView)
            var invoked = false
            lateinit var listener: T
            listener = wrapper.wrap {
                if (invoked) {
                    return@wrap
                }
                invoked = true

                viewRef.get()?.let { view ->
                        viewRef.clear()
                        val viewTreeObserver = view.viewTreeObserver
                        if (viewTreeObserver.isAlive) {
                            wrapper.removeListener(viewTreeObserver, listener)
                        }
                    }
                callback.invoke()
            }
            rootView.onViewTreeObserverReady { viewTreeObserver ->
                wrapper.addListener(viewTreeObserver, listener)
            }
        }
        window.onDecorViewReady {
            installListener()
        }
    }
}

private fun View.onViewTreeObserverReady(block: (ViewTreeObserver) -> Unit) {
    if (viewTreeObserver.isAlive && isAttachedToWindow) {
        block(viewTreeObserver)
    } else {
        // The view isn't attached yet on activity creation so View creates a temporary
        // ViewTreeObserver stored in View.mFloatingTreeObserver. When the view is attached,
        // mFloatingTreeObserver gets merged with mAttachInfo.mTreeObserver and the listeners are
        // copied over. Android has a bug (fixed in API 26) where ViewTreeObserver.OnDrawListener
        // listeners are not copied over in ViewTreeObserver.merge
        // Fix: https://android.googlesource.com/platform/frameworks/base/+
        // /9f8ec54244a5e0343b9748db3329733f259604f3
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View) {
            }

            override fun onViewAttachedToWindow(v: View) {
                // Note: the ViewTreeObserver instance here is difference from the earlier one,
                // so don't start extracting variables.
                block(rootView.viewTreeObserver)
                rootView.removeOnAttachStateChangeListener(this)
            }
        })
    }
}