/*
 * Copyright © 2024 fluffydaddy
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

package io.fluffydaddy.jutils.queue;

/**
 * A listener interface for tracking updates in a ByteQueue.
 * <p>
 * This interface is intended for classes that want to be notified when new
 * input data is written into a ByteQueue. </p>
 */
public interface ByteQueueListener {
    /**
     * Called when the input in the associated ByteQueue is updated.
     * <p>
     * This method is invoked when new data is written into the ByteQueue.
     * Implementing classes should perform any necessary actions in response to
     * the input update. </p>
     *
     * @see ByteWriter
     */
    void onInputUpdate();
}
