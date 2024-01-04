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

package io.fluffydaddy.jutils.reflect;

import java.lang.reflect.InvocationTargetException;

public class ReflectTest {
    public static void main(String[] args) throws InvocationTargetException, InstantiationException {
        new ReflectTest().test();
    }
    
    public void test() throws InvocationTargetException, InstantiationException {
        System.out.println("Reflect Invocation - " + ClassUtils.invokeMethod(this, ReflectTest.class, "toString"));
        System.out.println("Reflect Instance - " + ClassUtils.instance(ReflectTest.class).toString());
    }
    
    @Override
    public String toString() {
        return getClass().getName();
    }
}
