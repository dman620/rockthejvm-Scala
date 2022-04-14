package com.rockthejvm.advanced.lectures.part2afp

object Monads extends App {
  /*
    Monad Operations (all monads must do these)
  * 1. unit (also called pure or apply)
  * 2. flatMap (also called bind) from a monad of one type, create a monad of another type*/
  /*
    Monad Laws (these must be true of all monads)
  * 1. left-identity.  If you build a monad with an element (x) and flatmap it with a function (f), it should give you
  *   the function applied to that element. [unit(x).flatMap(f) == f(x)]
  * 2. right-identity.  If you flatmap a monad with the unit function, it should return the same monad.
  *   [aMonadInstance.flatMap(unit) == aMonadInstance]
  * 3. associativity.  If you have a monad instance and flat map it with 2 functions in sequence, it should give you
  *   the same monad as if it were flatmapped with a composite function.
  *   [m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))]
  * */

  //our own try monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }
  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try{
        Success(a)
      } catch {
        case e: Throwable => Failure(e)
      }
  }
  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try{
        f(value)
      } catch {
        case e: Throwable => Failure(e)
      }

  }
  case class Failure(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this

  }
}
