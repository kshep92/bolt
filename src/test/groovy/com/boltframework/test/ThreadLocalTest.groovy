package com.boltframework.test

import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * A test class created to understand the concept of ThreadLocals and how to use them.
 */
class ThreadLocalTest {

  @Test
  void basicTest() {
    final Controller controller = new Controller()

    // User A
    Thread thread1 = new Thread({
      controller.threadSafeContext.set("Baz")
      controller.nonThreadSafeContext = "Overwritten by Thread 1"
      println("Thread 1: ${controller.threadSafeContext.get()}")
      println("Thread 1: ${controller.nonThreadSafeContext}")
      assertEquals('Baz', controller.threadSafeContext.get())
    })

    // User B
    Thread thread2 = new Thread({
      controller.threadSafeContext.set("Biff")
      controller.nonThreadSafeContext = "Overwritten by Thread 2"
      println("Thread 2: ${controller.threadSafeContext.get()}")
      println("Thread 2: ${controller.nonThreadSafeContext}")
      sleep(200)
      assertEquals('Biff', controller.threadSafeContext.get())
    })

    // User A makes a request that completes immediately
    thread1.start()

    // User B makes a request that takes a while
    thread2.start()

    // User C makes a request that takes the longest
    Thread.sleep(300)
    assertEquals('Bar', controller.threadSafeContext.get())
    assertEquals('Overwritten by Thread 2', controller.nonThreadSafeContext)
    println("Main thread: ${controller.threadSafeContext.get()}")
    println("Main thread: ${controller.nonThreadSafeContext}")

  }

  static class Controller {
    ThreadLocal<String> threadSafeContext = new ThreadLocal<>()
    String nonThreadSafeContext
  }
}
