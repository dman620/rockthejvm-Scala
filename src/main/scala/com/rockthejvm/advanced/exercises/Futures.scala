package com.rockthejvm.advanced.exercises

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success}

object Futures extends App {
  /*
  * 1. fulfill a future IMMEDIATELY with a value
  * 2. function inSequence(future a, future b) it runs future b once it is SURE future b has completed
  * 3. first(future a, future b) return a future containing the earliest value returned between 2 futures.
  * 4. last(future a, future b) opposite of first(...)
  * 5. retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T]
  *     run the action repeatedly until the condition is met (returns true)
  * */

  //ex 1 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  println("EX1----------------------------------------------------------------------------------------------------------")
  def immediateFuture[T](value: T): Future[T] = Future(value)
  println(immediateFuture())

  //ex 2 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  println("\nEX2----------------------------------------------------------------------------------------------------------")
  def inSequence[A, B](a: Future[A], b: Future[B]) = {
    a.flatMap(_ => b)
  }

  //ex 3 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  println("\nEX3----------------------------------------------------------------------------------------------------------")
  def first(a: Future[Int], b: Future[Int]): Future[Int] = {
    val promise = Promise[Int]
    val future = promise.future
    future.onComplete({
      case Success(value) => return Future(value)
    })

    val agent1 = new Thread(() => {
      println("[agent1] processing future...")
      a.onComplete({
        case Success(v) =>
          println(s"[agent1] finished first with ${v}")
          promise.success(v)
      })
    })
    val agent2 = new Thread(() => {
      println("[agent2] processing future...")
      b.onComplete({
        case Success(v) =>
          println(s"[agent2] finished first with ${v}")
          promise.success(v)
      })
    })
    agent1.start()
    agent2.start()

    promise.future
  }
  val random = new Random()
  val future1 = Future{
    Thread.sleep(random.nextInt(500))
    100
  }
  val future2 = Future{
    Thread.sleep(random.nextInt(500))
    200
  }
  val result = first(future1, future2)
  Await.result(result, 2.seconds)
  result.foreach(println)
}
