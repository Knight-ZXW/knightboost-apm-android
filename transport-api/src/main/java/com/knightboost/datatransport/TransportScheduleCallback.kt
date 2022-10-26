package com.knightboost.datatransport

/**
 * An callback interface for the scheduling an Event
 */
interface TransportScheduleCallback {

    /**
     * Called when the process of scheduling data was finished successful
     */
    fun onSuccess();

    /**
     * Called when the process of scheduling data was failed
     */
    fun onFailure(exception: java.lang.Exception?, message:String?)
}