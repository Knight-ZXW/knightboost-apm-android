// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.knightboost.datatransport

/**
 * Provides means of recording events of interest.
 *
 * <p> Event destinations is unspecified and depends on concreate implementations of this interface.
 */
interface Transport<T> {

    /**
     * Sends the event of type T.
     *
     * <p> The event may not be send immediately. It's up to the implementation to define the time
     * when events are actually sent.
     *
     * @param event The event with the payload that needs to be sent
     *
     */
    fun send(event:Event<T>)

    /**
     *
     */
    fun send(event: Event<T>,callback: TransportScheduleCallback)

    /**
     * Flushes events queued up.
     */
    fun flush();
}