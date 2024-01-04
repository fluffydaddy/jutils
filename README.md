# JUtils
[![](https://jitpack.io/v/fluffydaddy/jutils.svg)](https://jitpack.io/#fluffydaddy/jutils)

***

## Using in:

### Gradle
``` groovy
    repositories {
        maven { url 'https://jitpack.io' }
        // or
        maven { url 'https://repo1.maven.org/maven2' }
    }
    
    dependencies {
        // from jitpack.io
        implementation 'com.github.fluffydaddy:jutils:1.0.2'
        // or maven central
        implementation 'io.github.fluffydaddy:jutils:1.0.2'
    }
```
### Maven

``` xml
<dependency>
        <groupId>com.github.fluffydaddy</groupId>
        <arifactId>jutils</arifactId>
        <version>1.0.2</version>
</dependency>
<!-- OR -->
<dependency>
        <groupId>io.github.fluffydaddy</groupId>
        <arifactId>jutils</arifactId>
        <version>1.0.2</version>
</dependency>
```

### A lightweight library that makes it easier to work with collections in java.

* Use Stream API.

***

``` java
Array<Integer> items = new Array<Integer>()
                .insert(0, 4, 9, 12, 2, 8, 3, 6)
                .sortedBy(it -> it % 2 == 0);
System.out.println(items.joinToString(Object::toString, ",", "[", "]"));
// or
Array<Integer> items = new Array<Integer>)
                .set(3, 7, 1, 9, 5, 34, 15, 29, 0)
                .sortedWith(Integer:compare);
System.out.println(items.joinToString(Object::toString, ",", "[", "]"));
```
* Use Reflect API.
* * *
``` java
// Example Reflect Invocation
System.out.println("Reflect Invocation - " + ClassUtils.invokeMethod(this, ReflectTest.class, "toString"));
// Example Reflect Instance
System.out.println("Reflect Instance - " + ClassUtils.instance(ReflectTest.class).toString());
```
* Use String Formatter.
***
``` java
MessageFormatter msg = new MessageFormatter();
msg.println("The %s to day, hello user %s!");
msg.println("Hello, %s!");
System.out.println(msg.format(Calendar.getInstance().get(Calendar.YEAR), "You", "World"));
```
* Use Byte Queue IO.
* And Other...