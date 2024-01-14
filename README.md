# JUtils
[![Maven Central](https://img.shields.io/maven-central/v/io.github.fluffydaddy/jutils/1.0.3.svg?label=Maven%20Central&color=success)](https://search.maven.org/artifact/io.github.fluffydaddy/jutils/1.0.3)

***

## Using in:

### Gradle
``` groovy
    // repository
    repositories {
        maven { url 'https://repo1.maven.org/maven2' }
    }
    
    // dependency
    dependencies {
        implementation 'io.github.fluffydaddy:jutils:1.0.3'
    }
```
### Maven

``` xml
<!-- REPOSITORY -->
<repository>
    <id>central</id>
    <url>https://repo1.maven.org/maven2</url>
</repository>

<!-- DEPENDENCY -->
<dependency>
    <groupId>io.github.fluffydaddy</groupId>
    <arifactId>jutils</arifactId>
    <version>1.0.3</version>
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
***
``` java
ByteQueue byteQueue = new ByteQueue(ByteQueue.QUEUE_SIZE);
        Thread thread = new Thread(() -> {
            System.out.println("Waiting...");
            byte[] buf = new byte[ByteQueue.QUEUE_SIZE];
            try {
                int read = byteQueue.read(buf, 0, buf.length);
                System.out.println("Received - " + new String(buf, 0, read));
            } catch (InterruptedException e) {
                e.printStackTrace();
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
            e.printStackTrace();
        }
```
* And Other...