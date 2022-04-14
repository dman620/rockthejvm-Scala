package com.rockthejvm.advanced.lectures.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {
  /*
  * interface Runnable {
  *   public void run()
  * }*/
  //JVM threads
  val runnable = new Runnable {
    override def run(): Unit = println("running in parallel.")
  }
  val aThread = new Thread(runnable)
  //.start() creates a JVM thread => OS thread

  /*Distinction:
  * Thread instance: the object that we see and can operate on
  * JVM thread: the actual thread that is running operations on the JVM
  * */
  aThread.start() //gives the signal to the JVM to start the JVM thread, which will invoke its runnable's run()
  runnable.run() //does nothing in parallel
  aThread.join() //blocks until the thread has finished running.  this is how you make sure that a thread has run before you
        //do some computation
  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodBye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  threadHello.start()
  threadGoodBye.start()
  //different runs produce different results

  //executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })
  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })
  pool.shutdown()
  //pool.execute(() => println("throws an exception in calling thread"))

  //pool.shutdownNow()
  println(pool.isShutdown)//true even if the threads are still running
      //this means that the pool will not accept any more actions, but can still continue to act

}
