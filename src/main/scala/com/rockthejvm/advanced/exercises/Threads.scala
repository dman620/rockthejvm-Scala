package com.rockthejvm.advanced.exercises

object Threads extends App {
  def inceptionThreads(n: Int): Unit = {
    if (n <= 0) println("done")
    else {
      inceptionThreads(n - 1)
      val theThread = new Thread(() => println(s"hello from thread $n"))
      theThread.start()
      theThread.join()
    }
  }
  def test(n: Int): Unit = {
    if(n <= 0) println("done")
    else {
      test(n - 1)
      println(n)
    }
  }

  test(3)
}
