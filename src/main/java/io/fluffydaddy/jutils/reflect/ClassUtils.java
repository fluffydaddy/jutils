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

import java.lang.reflect.*;
import java.util.Objects;
import java.util.Stack;

public class ClassUtils {
    public static <V> void setField(Class<?> clazz, Object instance, String name, V value) throws NoSuchFieldException {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <V> void setField(Class<?> clazz, String name, V value) throws NoSuchFieldException {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(null, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <V> void setField(String name, V value, Object instance) throws NoSuchFieldException {
        setField(instance == null ? null : instance.getClass(), instance, name, value);
    }


    public static <V> V getField(String name, Object instance) throws NoSuchFieldException {
        if (instance == null || (name == null || name.trim().isEmpty())) return null;

        Class<?> clazz = instance.getClass();

        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return (V) field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <V> V getField(String name, Class<?> clazz) throws NoSuchFieldException {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return (V) field.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field getField(Object instance, String name) {
        return instance == null ? null : getField(instance.getClass(), name);
    }

    public static Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <V> V getStaticField(Class<?> clazz, Class<V> type, String name) {
        Field field = getField(clazz, name);

        if (field == null) return null;

        if (!Modifier.isStatic(field.getModifiers()))
            return null;

        try {
            if (field.getType() != type)
                throw new IllegalStateException("Field founded type " + field.getType() + ", a required type " + type);

            return (V) field.get(null);
        } catch (IllegalAccessException ignored) {
        }

        return null;
    }


    public static <T> void swap(T arg1, T arg2) {
        if (arg1 == null && arg2 == null)
            return;

        T valC;
        T chgC;

        if (arg1 == null) {
            chgC = arg2;
            valC = null;
        } else {
            chgC = arg1;
            valC = arg2;
        }

        if (arg1 instanceof Number || arg2 instanceof Number) {
            try {
                swap(chgC, valC, "value");
            } catch (IllegalAccessException e) {
                System.err.println(e);
            }
        } else {
            Field[] fields = getFields(chgC);

            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    field.set(chgC, field.get(valC));
                    field.setAccessible(false);
                } catch (IllegalAccessException ignored) {
                }
            }
        }
    }

    private static <T> void swap(T arg1, T arg2, String valName) throws IllegalArgumentException, IllegalAccessException {
        if ((arg1 == null && arg2 == null) || valName == null)
            return;

        T valC;
        T chgC;

        if (arg1 == null) {
            chgC = arg2;
            valC = null;
        } else {
            chgC = arg1;
            valC = arg2;
        }


        Field[] fields = getFields(chgC);

        for (Field field : fields) {
            String fldName = field.getName();

            if (fldName.equals(valName)) {
                field.setAccessible(true);
                field.set(chgC, valC);
                field.setAccessible(false);

                break;
            }
        }
    }

    private static Field[] getFields(Object o) {
        if (o == null)
            return new Field[0];

        return o.getClass().getDeclaredFields();
    }


    public static Method getMethod(Object instance, String name, Object... params) {
        Objects.requireNonNull(instance);
        Objects.requireNonNull(name);

        return getMethod(instance.getClass(), name, params);
    }


    public static Method getMethod(Class<?> clazz, String name, Object... params) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(name);

        Method method = null;

        if (params == null || params.length == 0) {
            try {
                method = clazz.getMethod(name);
                method.setAccessible(true);
            } catch (NoSuchMethodException ignored) {
                try {
                    method = clazz.getDeclaredMethod(name);
                    method.setAccessible(true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Class<?>[] classTypes = new Class<?>[params.length];

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];

                Class<?> paramType = param.getClass();

                classTypes[i] = paramType;
            }

            try {
                method = clazz.getMethod(name, classTypes);
                method.setAccessible(true);
            } catch (NoSuchMethodException ignored) {
                try {
                    method = clazz.getDeclaredMethod(name, classTypes);
                    method.setAccessible(true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        return method;
    }

    public static <R> R invokeMethod(Class<?> cls, String name, Class<?>[] paramTypes, Object[] args) throws InvocationTargetException, NoSuchMethodException {
        Objects.requireNonNull(cls);
        Objects.requireNonNull(name);

        Method method = findMethod(cls, name, paramTypes);

        method.setAccessible(true);

        try {
            return (R) method.invoke(null, args);
        } catch (IllegalAccessException ignored) {
        }

        return null;
    }

    public static <R> R invokeMethod(Object instance, String name, Class<?>[] paramTypes, Object... args) throws InvocationTargetException {
        Objects.requireNonNull(instance);
        Objects.requireNonNull(name);

        try {
            Class<?> clz = instance.getClass();
            Method method = findMethod(clz, name, paramTypes);

            method.setAccessible(true);

            try {
                return (R) method.invoke(instance, args);
            } catch (IllegalAccessException ignored) {
            }
        } catch (NoSuchMethodException ignored) {
        }

        return null;
    }

    private static Method findMethod(Class<?> clazz, String name, Class<?>[] paramTypes) throws NoSuchMethodException, SecurityException {
        Objects.requireNonNull(clazz);

        Method method = findMethod(clazz.getMethods(), name, paramTypes);

        if (method == null) {
            method = findMethod(clazz.getDeclaredMethods(), name, paramTypes);
        }

        if (method == null) {
            method = clazz.getMethod(name, paramTypes);
        }

        return method;
    }

    private static Method findMethod(Method[] methods, String name, Class<?>[] paramTypes) {
        Method method = null;

        for (int i = 0; i < methods.length; i++) {
            method = methods[i];
            Class[] params = method.getParameterTypes();
            boolean isEqual = false;

            if (params.length != paramTypes.length) {
                method = null;
                continue;
            }

            for (int j = 0; i < params.length; j++) {
                Class oparam = params[i];
                Class mparam = paramTypes[i];

                if (oparam.isAssignableFrom(mparam)) {
                    isEqual = true;
                    break;
                }
            }

            if (isEqual) break;
        }

        return method;
    }

    public static <R> R invokeMethod(Object instance, Class<?> cls, String name, Object... args) throws InvocationTargetException {
        Objects.requireNonNull(instance);
        Objects.requireNonNull(cls);
        Objects.requireNonNull(name);

        Method method = getMethod(cls, name, args);

        try {
            return (R) method.invoke(instance, args);
        } catch (IllegalAccessException ignored) {
        }

        return null;
    }

    public static <V> V instance(Class<?> clazz) throws InstantiationException {
        return instance(clazz, null);
    }

    public static <V> V instance(Class<?> clazz, Object[] paramValues) throws InstantiationException {
        if (paramValues == null) return instance(clazz, null, null);
        Class<?>[] paramTypes = new Class<?>[paramValues.length];
        for (int i = 0; i < paramValues.length; i++) paramTypes[i] = paramValues[i].getClass();
        return instance(clazz, paramTypes, paramValues);
    }

    public static <V> V instance(Class<?> clazz, Class<?>[] paramTypes, Object[] paramValues) throws InstantiationException {
        try {
            return (V) clazz.getConstructor(paramTypes).newInstance(paramValues);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> Class<? extends T> getClass(T obj) {
        if (obj == null) return null;

        if (obj instanceof Class<?>)
            return ((Class<? extends T>) obj);
        else
            return (Class<? extends T>) obj.getClass();
    }

    public static Class<?> getGenericType(Object obj) {
        return getGenericType(0, obj);
    }

    public static Class<?> getGenericType(int index, Object obj) {
        return getGenericType(index, getClass(obj));
    }

    public static Class<?> getGenericType(Class<?> clazz) {
        return getGenericType(0, clazz);
    }

    public static Class<?> getGenericType(int index, Class<?> clazz) {
        return (Class<?>) ((ParameterizedType) clazz
                .getGenericSuperclass()).getActualTypeArguments()[index];
    }


    public static Class<?> getGenericClass(Object obj) {
        return getGenericClass(0, obj);
    }

    public static Class<?> getGenericClass(int index, Object actualObj) {
        Class<?> clazz;
        if (actualObj instanceof Class)
            clazz = (Class<?>) actualObj;
        else
            clazz = actualObj.getClass();

        return getGenericParameterClass(clazz, clazz, index);
    }

    public static Class<?> getGenericClass(Class<?> clazz) {
        return getGenericType(0, clazz);
    }

    public static Class<?> getGenericClass(int index, Class<?> clazz) {
        return (Class<?>) ((ParameterizedType) clazz
                .getGenericSuperclass()).getActualTypeArguments()[index];
    }


    /*
     * Method From Habr
     * https://habr.com/ru/post/66593/
     */

    /**
     * Для некоторого класса определяет каким классом был параметризован один из его предков с generic-параметрами.
     *
     * @param actualClass    анализируемый класс
     * @param genericClass   класс, для которого определяется значение параметра
     * @param parameterIndex номер параметра
     * @return класс, являющийся параметром с индексом parameterIndex в genericClass
     */

    public static <T> Class<? super T> getGenericParameterClass(final Class<? super T> actualClass, final Class<?> genericClass, final int parameterIndex) {
        // Прекращаем работу если genericClass не является предком actualClass.
        if (actualClass.isInterface()) {
            if (!genericClass.isAssignableFrom(actualClass)) {
                throw new IllegalArgumentException("Interface " + genericClass.getName() + " is not a implement of "
                        + actualClass.getName() + ".");
            }
        } else {
            if ((!genericClass.isAssignableFrom(actualClass.getSuperclass()))) {
                throw new IllegalArgumentException("Class " + genericClass.getName() + " is not a superclass of "
                        + actualClass.getName() + ".");
            }
        }

        // Нам нужно найти класс, для которого непосредственным родителем будет genericClass.
        // Мы будем подниматься вверх по иерархии, пока не найдем интересующий нас класс.
        // В процессе поднятия мы будем сохранять в genericClasses все классы - они нам понадобятся при спуске вниз.

        // Проейденные классы - используются для спуска вниз.
        Stack<ParameterizedType> genericClasses = new Stack<>();

        // clazz - текущий рассматриваемый класс
        Class<? super T> clazz = actualClass;

        while (true) {
            Type genericSuperclass = clazz.getGenericSuperclass();
            boolean isParameterizedType = genericSuperclass instanceof ParameterizedType;
            if (isParameterizedType) {
                // Если предок - параметризованный класс, то запоминаем его - возможно он пригодится при спуске вниз.
                genericClasses.push((ParameterizedType) genericSuperclass);
            } else {
                // В иерархии встретился непараметризованный класс. Все ранее сохраненные параметризованные классы будут бесполезны.
                genericClasses.clear();
            }
            // Проверяем, дошли мы до нужного предка или нет.
            Type rawType = isParameterizedType ? ((ParameterizedType) genericSuperclass).getRawType() : genericSuperclass;
            if (!rawType.equals(genericClass)) {
                // genericClass не является непосредственным родителем для текущего класса.
                // Поднимаемся по иерархии дальше.
                clazz = clazz.getSuperclass();
            } else {
                // Мы поднялись до нужного класса. Останавливаемся.
                break;
            }
        }

        // Нужный класс найден. Теперь мы можем узнать, какими типами он параметризован.
        Type result = genericClasses.pop().getActualTypeArguments()[parameterIndex];

        while (result instanceof TypeVariable && !genericClasses.empty()) {
            // Похоже наш параметр задан где-то ниже по иерархии, спускаемся вниз.

            // Получаем индекс параметра в том классе, в котором он задан.
            int actualArgumentIndex = getParameterTypeDeclarationIndex((TypeVariable<?>) result);
            // Берем соответствующий класс, содержащий метаинформацию о нашем параметре.
            ParameterizedType type = genericClasses.pop();
            // Получаем информацию о значении параметра.
            result = type.getActualTypeArguments()[actualArgumentIndex];
        }

        if (result instanceof TypeVariable) {
            // Мы спустились до самого низа, но даже там нужный параметр не имеет явного задания.
            // Следовательно из-за "Type erasure" узнать класс для параметра невозможно.
            throw new IllegalStateException("Unable to resolve type variable " + result + "."
                    + " Try to replace instances of parametrized class with its non-parameterized subtype.");
        }

        if (result instanceof ParameterizedType) {
            // Сам параметр оказался параметризованным.
            // Отбросим информацию о его параметрах, она нам не нужна.
            result = ((ParameterizedType) result).getRawType();
        }

        if (result == null) {
            // Should never happen. :)
            throw new IllegalStateException("Unable to determine actual parameter type for "
                    + actualClass.getName() + ".");
        }

        if (!(result instanceof Class)) {
            // Похоже, что параметр - массив или что-то еще, что не является классом.
            throw new IllegalStateException("Actual parameter type for " + actualClass.getName() + " is not a Class.");
        }

        return (Class<? super T>) result;
    }

    public static <T> Class<? super T> getGenericParameterClass(Class<? super T> actualClass, int parameterIndex) {
        return getGenericParameterClass(actualClass, actualClass, parameterIndex);
    }

    public static int getParameterTypeDeclarationIndex(final TypeVariable<?> typeVariable) {
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();

        // Ищем наш параметр среди всех параметров того класса, где определен нужный нам параметр.
        TypeVariable<?>[] typeVariables = genericDeclaration.getTypeParameters();
        Integer actualArgumentIndex = null;
        for (int i = 0; i < typeVariables.length; i++) {
            if (typeVariables[i].equals(typeVariable)) {
                actualArgumentIndex = i;
                break;
            }
        }
        if (actualArgumentIndex != null) {
            return actualArgumentIndex;
        } else {
            throw new IllegalStateException("Argument " + typeVariable.toString() + " is not found in "
                    + genericDeclaration.toString() + ".");
        }
    }
}