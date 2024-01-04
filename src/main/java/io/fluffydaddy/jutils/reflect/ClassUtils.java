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

import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;
import java.util.Stack;

/**
 * Utility class for reflection-related operations.
 */
public class ClassUtils {
    
    /**
     * Sets the value of a field in an object instance.
     *
     * @param clazz    The class containing the field.
     * @param instance The object instance (can be null for static fields).
     * @param name     The name of the field.
     * @param value    The value to set.
     * @param <V>      The type of the field.
     * @throws NoSuchFieldException If the field is not found.
     */
    public static <V> void setField(Class<?> clazz, Object instance, String name, V value) throws NoSuchFieldException {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sets the value of a static field in a class.
     *
     * @param clazz The class containing the static field.
     * @param name  The name of the field.
     * @param value The value to set.
     * @param <V>   The type of the field.
     * @throws NoSuchFieldException If the field is not found.
     */
    public static <V> void setField(Class<?> clazz, String name, V value) throws NoSuchFieldException {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(null, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sets the value of a field in an object instance using the field name.
     *
     * @param name     The name of the field.
     * @param value    The value to set.
     * @param instance The object instance (can be null for static fields).
     * @param <V>      The type of the field.
     * @throws NoSuchFieldException If the field is not found.
     */
    public static <V> void setField(String name, V value, Object instance) throws NoSuchFieldException {
        setField(instance == null ? null : instance.getClass(), instance, name, value);
    }
    
    /**
     * Gets the value of a field from an object instance using the field name.
     *
     * @param name     The name of the field.
     * @param instance The object instance.
     * @param <V>      The type of the field.
     * @return The value of the field.
     * @throws NoSuchFieldException If the field is not found.
     */
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
    
    /**
     * Gets the value of a static field from a class using the field name.
     *
     * @param name  The name of the field.
     * @param clazz The class containing the static field.
     * @param <V>   The type of the field.
     * @return The value of the field.
     * @throws NoSuchFieldException If the field is not found.
     */
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
    
    /**
     * Gets a field from an object instance using the field name.
     *
     * @param instance The object instance.
     * @param name     The name of the field.
     * @return The field.
     */
    public static Field getField(Object instance, String name) {
        return instance == null ? null : getField(instance.getClass(), name);
    }
    
    /**
     * Gets a field from a class using the field name.
     *
     * @param clazz The class containing the field.
     * @param name  The name of the field.
     * @return The field.
     */
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
    
    /**
     * Gets the value of a static field from a class using the field name and type.
     *
     * @param clazz The class containing the static field.
     * @param type  The expected type of the field.
     * @param name  The name of the field.
     * @param <V>   The type of the field.
     * @return The value of the field.
     */
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
    
    /**
     * Swaps the values of two objects or primitive types.
     *
     * @param arg1 The first object or primitive type.
     * @param arg2 The second object or primitive type.
     * @param <T>  The type of the objects.
     */
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
    
    /**
     * Swaps the values of two objects or primitive types by specifying the field name.
     *
     * @param arg1    The first object or primitive type.
     * @param arg2    The second object or primitive type.
     * @param valName The name of the field to swap.
     * @param <T>     The type of the objects.
     * @throws IllegalArgumentException If the specified field is not found.
     * @throws IllegalAccessException   If access to the field is denied.
     */
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
    
    /**
     * Gets all fields from an object or primitive type.
     *
     * @param o The object or primitive type.
     * @return An array of fields.
     */
    private static Field[] getFields(Object o) {
        if (o == null)
            return new Field[0];
        
        return o.getClass().getDeclaredFields();
    }
    
    /**
     * Gets a method from an object instance using the method name and parameters.
     *
     * @param instance The object instance.
     * @param name     The name of the method.
     * @param params   The method parameters.
     * @return The method.
     */
    public static Method getMethod(Object instance, String name, Object... params) {
        Objects.requireNonNull(instance);
        Objects.requireNonNull(name);
        
        return getMethod(instance.getClass(), name, params);
    }
    
    /**
     * Gets a method from a class using the method name and parameters.
     *
     * @param clazz  The class.
     * @param name   The name of the method.
     * @param params The method parameters.
     * @return The method.
     */
    public static Method getMethod(Class<?> clazz, String name, Object... params) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(name);
        
        Method method = null;
        
        if (params == null || params.length == 0) {
            try {
                method = clazz.getDeclaredMethod(name);
                method.setAccessible(true);
            } catch (NoSuchMethodException ignored) {
                try {
                    method = clazz.getMethod(name);
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
    
    /**
     * Invokes a static method from a class using the method name and parameters.
     *
     * @param cls        The class.
     * @param name       The name of the method.
     * @param paramTypes The parameter types.
     * @param args       The method arguments.
     * @param <R>        The return type of the method.
     * @return The result of the method invocation.
     * @throws InvocationTargetException If the method invocation results in an exception.
     * @throws NoSuchMethodException     If the method is not found.
     */
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
    
    /**
     * Invokes an instance method from an object instance using the method name and parameters.
     *
     * @param instance   The object instance.
     * @param name       The name of the method.
     * @param paramTypes The parameter types.
     * @param args       The method arguments.
     * @param <R>        The return type of the method.
     * @return The result of the method invocation.
     * @throws InvocationTargetException If the method invocation results in an exception.
     */
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
    
    /**
     * Finds and returns a method in the specified class with the given name and parameter types.
     *
     * @param clazz      The class to search for the method.
     * @param name       The name of the method.
     * @param paramTypes An array of parameter types of the method.
     * @return The found method.
     * @throws NoSuchMethodException If the method is not found.
     * @throws SecurityException     If a security manager, s, is present and any of the following conditions is met:
     *                               - the caller's class loader is not the same as or an ancestor of the class loader for
     *                               the current class and invocation of s.checkPermission method with
     *                               RuntimePermission("accessDeclaredMembers") denies access to the declared method.
     *                               - the caller's class loader is not the same as or an ancestor of the class loader for
     *                               the current class and invocation of s.checkPackageAccess() denies access to the
     *                               package of this class.
     */
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
    
    
    /**
     * Helper method to find and return a method in the specified array of methods with the given name and parameter types.
     *
     * @param methods    The array of methods to search.
     * @param name       The name of the method.
     * @param paramTypes An array of parameter types of the method.
     * @return The found method.
     */
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
            
            for (int j = 0; j < params.length; j++) {
                Class oparam = params[j];
                Class mparam = paramTypes[j];
                
                if (oparam.isAssignableFrom(mparam)) {
                    isEqual = true;
                    break;
                }
            }
            
            if (isEqual) break;
        }
        
        return method;
    }
    
    
    /**
     * Invokes a method on the specified instance with the given name and arguments.
     *
     * @param instance The instance on which the method should be invoked.
     * @param cls      The class containing the method.
     * @param name     The name of the method.
     * @param args     The arguments to be passed to the method.
     * @param <R>      The return type of the method.
     * @return The result of invoking the method.
     * @throws InvocationTargetException If the underlying method throws an exception.
     * @throws IllegalArgumentException  If the specified class is null or the method is not found.
     */
    public static <R> R invokeMethod(Object instance, Class<?> cls, String name, Object... args) throws InvocationTargetException {
        Objects.requireNonNull(instance);
        Objects.requireNonNull(cls);
        Objects.requireNonNull(name);
        
        Method method = getMethod(cls, name, args);
        
        try {
            return (R) method.invoke(instance, args);
        } catch (IllegalArgumentException e) {
            if (cls.getSuperclass() == null) {
                e.printStackTrace();
                return null;
            }
            return (R) getMethod(cls.getSuperclass(), name, args);
        } catch (IllegalAccessException ignored) {
        }
        
        return null;
    }
    
    
    /**
     * Creates a new instance of the specified class with no arguments.
     *
     * @param clazz The class to instantiate.
     * @param <V>   The type of the created instance.
     * @return A new instance of the specified class.
     * @throws InstantiationException If this Class represents an abstract class, an interface, an array class, or a
     *                                primitive type; or if the class has no nullary constructor; or if the instantiation
     *                                fails for some other reason.
     */
    public static <V> V instance(Class<?> clazz) throws InstantiationException {
        return instance(clazz, null);
    }
    
    /**
     * Creates a new instance of the specified class with the given parameter values.
     *
     * @param clazz       The class to instantiate.
     * @param paramValues An array of parameter values to be passed to the constructor.
     * @param <V>         The type of the created instance.
     * @return A new instance of the specified class.
     * @throws InstantiationException If this Class represents an abstract class, an interface, an array class, or a
     *                                primitive type; or if the class has no constructor with the specified parameter types;
     *                                or if the instantiation fails for some other reason.
     */
    public static <V> V instance(Class<?> clazz, Object[] paramValues) throws InstantiationException {
        if (paramValues == null) return instance(clazz, null, null);
        Class<?>[] paramTypes = new Class<?>[paramValues.length];
        for (int i = 0; i < paramValues.length; i++) paramTypes[i] = paramValues[i].getClass();
        return instance(clazz, paramTypes, paramValues);
    }
    
    /**
     * Creates a new instance of the specified class with the given parameter types and values.
     *
     * @param clazz       The class to instantiate.
     * @param paramTypes  An array of parameter types for the constructor.
     * @param paramValues An array of parameter values to be passed to the constructor.
     * @param <V>         The type of the created instance.
     * @return A new instance of the specified class.
     * @throws InstantiationException If this Class represents an abstract class, an interface, an array class, or a
     *                                primitive type; or if the class has no constructor with the specified parameter types;
     *                                or if the instantiation fails for some other reason.
     */
    public static <V> V instance(Class<?> clazz, Class<?>[] paramTypes, Object[] paramValues) throws InstantiationException {
        try {
            return (V) clazz.getConstructor(paramTypes).newInstance(paramValues);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets the Class object for the specified object.
     *
     * @param obj The object for which to get the Class.
     * @param <T> The type of the object.
     * @return The Class object for the specified object.
     */
    public static <T> Class<? extends T> getClass(T obj) {
        if (obj == null) return null;
        
        if (obj instanceof Class<?>)
            return ((Class<? extends T>) obj);
        else
            return (Class<? extends T>) obj.getClass();
    }
    
    /**
     * Gets the generic type of the specified object.
     *
     * @param obj The object for which to get the generic type.
     * @return The generic type of the specified object.
     */
    public static Class<?> getGenericType(Object obj) {
        return getGenericType(0, obj);
    }
    
    /**
     * Gets the generic type of the specified object at the specified index.
     *
     * @param index The index of the generic type to retrieve.
     * @param obj   The object for which to get the generic type.
     * @return The generic type of the specified object at the specified index.
     */
    public static Class<?> getGenericType(int index, Object obj) {
        return getGenericType(index, getClass(obj));
    }
    
    /**
     * Gets the generic type of the specified class.
     *
     * @param clazz The class for which to get the generic type.
     * @return The generic type of the specified class.
     */
    public static Class<?> getGenericType(Class<?> clazz) {
        return getGenericType(0, clazz);
    }
    
    /**
     * Gets the generic type of the specified class at the specified index.
     *
     * @param index The index of the generic type to retrieve.
     * @param clazz The class for which to get the generic type.
     * @return The generic type of the specified class at the specified index.
     */
    public static Class<?> getGenericType(int index, Class<?> clazz) {
        return (Class<?>) ((ParameterizedType) clazz
                .getGenericSuperclass()).getActualTypeArguments()[index];
    }
    
    /**
     * Gets the generic class of the specified object.
     *
     * @param obj The object for which to get the generic class.
     * @return The generic class of the specified object.
     */
    public static Class<?> getGenericClass(Object obj) {
        return getGenericClass(0, obj);
    }
    
    /**
     * Gets the generic class of the specified object at the specified index.
     *
     * @param index     The index of the generic class to retrieve.
     * @param actualObj The actual object for which to get the generic class.
     * @return The generic class of the specified object at the specified index.
     */
    public static Class<?> getGenericClass(int index, Object actualObj) {
        Class<?> clazz;
        if (actualObj instanceof Class)
            clazz = (Class<?>) actualObj;
        else
            clazz = actualObj.getClass();
        
        return getGenericParameterClass(clazz, clazz, index);
    }
    
    /**
     * Gets the generic class of the specified class.
     *
     * @param clazz The class for which to get the generic class.
     * @return The generic class of the specified class.
     */
    public static Class<?> getGenericClass(Class<?> clazz) {
        return getGenericType(0, clazz);
    }
    
    /**
     * Gets the generic class of the specified class at the specified index.
     *
     * @param index The index of the generic class to retrieve.
     * @param clazz The class for which to get the generic class.
     * @return The generic class of the specified class at the specified index.
     */
    public static Class<?> getGenericClass(int index, Class<?> clazz) {
        return (Class<?>) ((ParameterizedType) clazz
                .getGenericSuperclass()).getActualTypeArguments()[index];
    }
    
    /**
     * Determines the parameterized class for a given class by analyzing its ancestors with generic parameters.
     *
     * @param actualClass    The class being analyzed.
     * @param genericClass   The class for which to determine the parameterized value.
     * @param parameterIndex The index of the generic parameter.
     * @param <T>            The type of the class being analyzed.
     * @return The class that is the parameter at the specified index in the genericClass.
     * @throws IllegalArgumentException If genericClass is not a superclass of actualClass.
     * @throws IllegalStateException    If unable to determine the actual parameter type due to type erasure or other reasons.
     */
    public static <T> Class<? super T> getGenericParameterClass(final Class<? super T> actualClass, final Class<?> genericClass, final int parameterIndex) {
        // Stop execution if genericClass is not a superclass of actualClass.
        if (actualClass.isInterface()) {
            if (!genericClass.isAssignableFrom(actualClass)) {
                throw new IllegalArgumentException("Interface " + genericClass.getName() + " is not an implementor of "
                        + actualClass.getName() + ".");
            }
        } else {
            if ((!genericClass.isAssignableFrom(actualClass.getSuperclass()))) {
                throw new IllegalArgumentException("Class " + genericClass.getName() + " is not a superclass of "
                        + actualClass.getName() + ".");
            }
        }
        
        // We need to find a class for which genericClass is the immediate parent.
        // We will ascend the hierarchy until we find the desired class.
        // During ascent, we will keep track of all classes in genericClasses - they will be needed for descent.
        
        // Traversed classes - used for descent.
        Stack<ParameterizedType> genericClasses = new Stack<>();
        
        // Current class being considered.
        Class<? super T> clazz = actualClass;
        
        while (true) {
            Type genericSuperclass = clazz.getGenericSuperclass();
            boolean isParameterizedType = genericSuperclass instanceof ParameterizedType;
            if (isParameterizedType) {
                // If the ancestor is a parameterized class, remember it - it may be useful for descent.
                genericClasses.push((ParameterizedType) genericSuperclass);
            } else {
                // In the hierarchy, an unparameterized class is encountered.
                // All previously saved parameterized classes will be useless.
                genericClasses.clear();
            }
            // Check if we have reached the desired ancestor.
            Type rawType = isParameterizedType ? ((ParameterizedType) genericSuperclass).getRawType() : genericSuperclass;
            if (!rawType.equals(genericClass)) {
                // genericClass is not the immediate parent for the current class.
                // Ascend the hierarchy further.
                clazz = clazz.getSuperclass();
            } else {
                // We have ascended to the desired class. Stop.
                break;
            }
        }
        
        // The desired class is found. Now we can determine with which types it is parameterized.
        Type result = genericClasses.pop().getActualTypeArguments()[parameterIndex];
        
        while (result instanceof TypeVariable && !genericClasses.empty()) {
            // It seems our parameter is set somewhere lower in the hierarchy. Descend.
            
            // Get the index of the parameter in the class where it is set.
            int actualArgumentIndex = getParameterTypeDeclarationIndex((TypeVariable<?>) result);
            // Get the corresponding class containing metadata about our parameter.
            ParameterizedType type = genericClasses.pop();
            // Get information about the parameter value.
            result = type.getActualTypeArguments()[actualArgumentIndex];
        }
        
        if (result instanceof TypeVariable) {
            // We have descended to the lowest level, but even there the desired parameter is not explicitly defined.
            // Therefore, due to "Type erasure," it is impossible to determine the class for the parameter.
            throw new IllegalStateException("Unable to resolve type variable " + result + "."
                    + " Try to replace instances of parameterized class with its non-parameterized subtype.");
        }
        
        if (result instanceof ParameterizedType) {
            // The parameter itself turned out to be parameterized.
            // Discard information about its parameters, as we do not need it.
            result = ((ParameterizedType) result).getRawType();
        }
        
        if (result == null) {
            // Should never happen. :)
            throw new IllegalStateException("Unable to determine the actual parameter type for "
                    + actualClass.getName() + ".");
        }
        
        if (!(result instanceof Class)) {
            // It seems that the parameter is an array or something else that is not a class.
            throw new IllegalStateException("Actual parameter type for " + actualClass.getName() + " is not a Class.");
        }
        
        return (Class<? super T>) result;
    }
    
    /**
     * Gets the generic parameter class for the specified class at the specified index.
     *
     * @param actualClass    The class being analyzed.
     * @param parameterIndex The index of the generic parameter.
     * @param <T>            The type of the class being analyzed.
     * @return The generic parameter class at the specified index.
     */
    public static <T> Class<? super T> getGenericParameterClass(Class<? super T> actualClass, int parameterIndex) {
        return getGenericParameterClass(actualClass, actualClass, parameterIndex);
    }
    
    /**
     * Gets the index of the type variable declaration for the specified TypeVariable.
     *
     * @param typeVariable The TypeVariable for which to get the index.
     * @return The index of the type variable declaration.
     * @throws IllegalStateException If the type variable is not found in the generic declaration.
     */
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
            throw new IllegalStateException("Argument " + typeVariable + " is not found in "
                    + genericDeclaration + ".");
        }
    }
}