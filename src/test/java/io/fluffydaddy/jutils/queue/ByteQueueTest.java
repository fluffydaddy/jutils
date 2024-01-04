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

import java.nio.charset.StandardCharsets;

public class ByteQueueTest {
    public static void main(String[] args) throws InterruptedException {
        new ByteQueueTest().test();
    }
    
    public void test() throws InterruptedException {
        ByteQueue byteQueue = new ByteQueue(ByteQueue.QUEUE_SIZE);
        Thread thread = new Thread(() -> {
            System.out.println("Waiting...");
            byte[] buf = new byte[ByteQueue.QUEUE_SIZE];
            try {
                int read = byteQueue.read(buf, 0, buf.length);
                System.out.println("Received - " + new String(buf, 0, read));
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getCause());
            }
        });
        System.out.println("Started for reading...");
        thread.start();
        Thread.sleep(2500);
        String msg = "Hello, World!";
        try {
            System.out.println("Writing...");
            byteQueue.write(msg.getBytes(StandardCharsets.UTF_8), 0, msg.length());
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
