package com.boltframework.test

import org.junit.Before
import org.junit.Test

class ThreadLocalTest {

  Controller controller

  @Before
  public void setUp() throws Exception {
    controller = new Controller()
  }

  Controller getController() {
    return controller
  }

  @Test
  void basicTest() {
    // User A makes a request that takes a while
    def _controller = getController()
    _controller.threadSafeContext.set("Bar")
    _controller.nonThreadSafeContext = "Original value"
    // User B makes a request that completes immediately
    Thread thread1 = new Thread({
      def _controller2 = getController()
      _controller2.threadSafeContext.set("Baz")
      _controller.nonThreadSafeContext = "Overwritten by Thread 1"
      println("Thread 1: ${_controller.threadSafeContext.get()}")
      println("Thread 1: ${_controller.nonThreadSafeContext}")
    })
    Thread thread2 = new Thread({
      def _controller2 = getController()
      _controller2.threadSafeContext.set("Biff")
      _controller.nonThreadSafeContext = "Overwritten by Thread 2"
      println("Thread 2: ${_controller.threadSafeContext.get()}")
      println("Thread 2: ${_controller.nonThreadSafeContext}")
      sleep(200)
    })
    thread1.start()
    thread2.start()
    Thread.sleep(300)
    println("Main thread: ${_controller.threadSafeContext.get()}")
    println("Main thread: ${_controller.nonThreadSafeContext}")

  }

  static class Controller {
    ThreadLocal<String> threadSafeContext = new ThreadLocal<>()
    String nonThreadSafeContext

    public String context() {
      return threadSafeContext.get()
    }

    public void context(String ctx) {
      threadSafeContext.set(ctx)
    }
  }
}
