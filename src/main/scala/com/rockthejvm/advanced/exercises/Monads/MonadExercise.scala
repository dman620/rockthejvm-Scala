package com.rockthejvm.advanced.exercises.Monads

object MonadExercise extends App {
  /*
  * 1. Implement a Lazy[T] monad
  *   a computation which will only be executed when it is needed.
  *   unit/apply in a companion object that implements a lazy trait
  *   flatMap which flatmaps a lazy monad with a function that transforms a value into another lazy instance
  *
  * 2. how would you implement map/flatten given an implementation of flatMap, such that m.map().flatten() gives
  *   the same value as its flatMap?*/

  class Lazy[+A](value: => A) {
    def use: A = value
    def flatMap[B](f: A => Lazy[B]): Lazy[B] = f(value)
  }
  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy{
    println("Lazy instance generated")
    5
  }
  println("Lazy Instance Instantiated.")
  println("Printing Lazy Instance:")
  println(lazyInstance.use)
  println("Printing flatmapped lazy instance:")
  println(lazyInstance.flatMap(x => Lazy {x * 2 + 5}).use)
}
