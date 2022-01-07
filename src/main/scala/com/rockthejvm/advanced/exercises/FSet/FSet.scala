package com.rockthejvm.advanced.exercises.FSet

import scala.annotation.tailrec

//exercise implement a functional set
trait FSet[A] extends (A => Boolean) {
  def apply(elem: A): Boolean = contains(elem)

  def head: A

  def tail: FSet[A]

  def isEmpty: Boolean

  def contains(elem: A): Boolean

  def +(elem: A): FSet[A]

  def ++(anotherSet: FSet[A]): FSet[A]

  def map[B](f: A => B): FSet[B]

  def flatMap[B](f: A => FSet[B]): FSet[B]

  def filter(predicate: A => Boolean): FSet[A]

  def foreach(f: A => Unit): Unit

  def -(elem: A): FSet[A]

  def &(anotherSet: FSet[A]): FSet[A]

  def --(anotherSet: FSet[A]): FSet[A]

  def unary_! : FSet[A]
}

class Empty[A] extends FSet[A] {
  def contains(elem: A): Boolean = false

  def head: A = throw new NoSuchElementException

  def tail: FSet[A] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def +(elem: A): FSet[A] = new Cons(elem, this)

  def ++(anotherSet: FSet[A]): FSet[A] = anotherSet

  def map[B](f: A => B): FSet[B] = new Empty[B]

  def flatMap[B](f: A => FSet[B]): FSet[B] = new Empty[B]

  def filter(predicate: A => Boolean): FSet[A] = this

  def foreach(f: A => Unit): Unit = ()

  def -(elem: A): FSet[A] = this

  def &(anotherSet: FSet[A]): FSet[A] = this

  def --(anotherSet: FSet[A]): FSet[A] = anotherSet

  def unary_! : FSet[A] = new PSet[A](_ => true)
}

class PSet[A](property: A => Boolean) extends FSet[A]{
  override def head: A = politelyFail

  override def tail: FSet[A] = politelyFail

  override def isEmpty: Boolean = politelyFail

  override def contains(elem: A): Boolean = property(elem)

  override def +(elem: A): FSet[A] = new PSet[A](x => property(x) || x == elem)

  override def ++(anotherSet: FSet[A]): FSet[A] = new PSet[A](x => property(x) || anotherSet(x))

  override def map[B](f: A => B): FSet[B] = politelyFail

  override def flatMap[B](f: A => FSet[B]): FSet[B] = politelyFail

  override def filter(predicate: A => Boolean): FSet[A] = new PSet[A](x => property(x) && predicate(x))

  override def foreach(f: A => Unit): Unit = politelyFail

  override def -(elem: A): FSet[A] = filter(x => x != elem)

  override def &(anotherSet: FSet[A]): FSet[A] = filter(anotherSet)

  override def --(anotherSet: FSet[A]): FSet[A] = filter(!anotherSet)

  def unary_! : FSet[A] = new PSet[A](x => !property(x))

  def politelyFail = throw new IllegalArgumentException("mathematically impossible to determine")
}

class Cons[A](h: A, t: FSet[A]) extends FSet[A] {
  def contains(elem: A): Boolean = {
    elem == head || tail.contains(elem)
  }

  def head: A = h

  def tail: FSet[A] = t

  def isEmpty: Boolean = false

  def +(elem: A): FSet[A] = {
    if (this.contains(elem)) this
    else new Cons[A](elem, this)
  }

  def ++(anotherSet: FSet[A]): FSet[A] = {
    t ++ anotherSet + h
  }

  def map[B](f: A => B): FSet[B] = {
    //my implementation
    //new Cons(f(head), tail map f)
    /*map[1 2 3]
    * new Cons(f(1), map[2 3])
    * new Cons(f(1), new Cons(f(2), map[3]))
    * new Cons(f(1), new Cons(f(2), new Cons(f(3), map[])))
    * new Cons(f(1), new Cons(f(2), new Cons(f(3), new Empty)))*/
    //daniel's implementation
    (t map f) + f(h)
    /*map[1 2 3]
    * map[2 3] + f(1)
    * map[3] + f(2) + f(1)
    * map[] + f(3) + f(2) + f(1)
    * new Empty + f(3) + f(2) + f(1)*/
    /*analysis:
    Both implementations will work.
    Daniel's is more abstract*/
  }

  def flatMap[B](f: A => FSet[B]): FSet[B] = {
    (t flatMap f) ++ f(h)
  }

  def filter(predicate: A => Boolean): FSet[A] = {
    val filteredTail = t filter predicate
    if (predicate(h)) filteredTail + h
    else filteredTail
  }

  def foreach(f: A => Unit): Unit = {
    f(h)
    t.foreach(f)
  }

  def -(elem: A): FSet[A] = {
    @tailrec
    def go(elem: A, current: FSet[A], accumulatedSet: FSet[A]): FSet[A] = {
      if (current.isEmpty) accumulatedSet
      else go(elem, current.tail, if (current.head != elem) accumulatedSet + current.head else accumulatedSet)
    }

    if (this.contains(elem)) go(elem, this, new Empty)
    else this
  }

  def &(anotherSet: FSet[A]): FSet[A] = {
    @tailrec
    def go(anotherSet: FSet[A], current: FSet[A], accumulatedSet: FSet[A]): FSet[A] = {
      if (current.isEmpty) accumulatedSet
      else go(anotherSet, current.tail, if (anotherSet.contains(current.head)) accumulatedSet + current.head else accumulatedSet)
    }

    go(anotherSet, this, new Empty)
  }

  def --(anotherSet: FSet[A]): FSet[A] = {
    @tailrec
    def go(anotherSet: FSet[A], current: FSet[A], accumulatedSet: FSet[A]): FSet[A] = {
      if (current.isEmpty) accumulatedSet
      else go(anotherSet, current.tail, if (!anotherSet.contains(current.head)) accumulatedSet + current.head else accumulatedSet)
    }
    go(this, anotherSet, go(anotherSet, this, new Empty))
  }

  def unary_! : FSet[A] = new PSet[A](x => this.contains(x))
}

object Cons {
  def apply[A](values: A*): FSet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], accumulatedSet: FSet[A]): FSet[A] = {
      if (valSeq.isEmpty) accumulatedSet
      else buildSet(valSeq.tail, accumulatedSet + valSeq.head)
    }

    buildSet(values, new Empty[A])
  }
}

object MySetPlayground extends App {
  val theSet = Cons(1, 2, 3, 26, 21, 102, 32, 53, 2, 10, 11, 15, 67, 314159)
  theSet + 3 + 7 + 7 ++ Cons(1, 89, 30) + 34 filter isPrime foreach println

  /*  for {
      char <- Cons[Char]('a', 'b', 'c')
      num <- theSet
    } yield print( " " + num + " - " + char)*/

  val set1 = Cons[Int](4, 5, 6, 7, 10, 11)
  val set2 = Cons[Int](1, 2, 3, 4, 5, 10, 11, 12)
  //since FSet extends Function1, we can use a set in place of a Function1
  //The following line's meaning is unclear, but it is legal!
  //set1.filter(set2).foreach(println)

  println("removal testing:")
  theSet - 2 - 34 - 36 - 67 - 314159 - 102 - 26 - 1 foreach (x => print(x + " - "))
  println("\nintersection testing:")
  set1.&(set2) foreach (x => print(x + " - "))
  println("\ndifference testing:")
  set1.--(set2) foreach (x => print(x + " - "))
  println

  val negative = !set1 //all natural numbers != 4, 5, 6, 7, 10, 11
  println(negative(2)) //false
  println(negative(5)) //true
  val primeNumbers: PSet[Int] = new PSet[Int](x => isPrime(x))
  Range(1, 1000).filter(primeNumbers).foreach(println)
  //Range(0, 10000).filter(x => isPrime(x)).foreach(println)

  printNumOfPrimesPer1000(50)

  def printNumOfPrimesPer1000(depth: Int): Unit = {
    @tailrec
    def go(num: Int, accumulatedPrimes: Int): Unit = {
      if(num > depth * 1000) println("done")
      else go(num + 1, if(num % 1000 == 0 && accumulatedPrimes > 0){
        //if we hit a multiple of 1000, print the values and reset accumulatedPrimes
        println(s"From ${num - 1000} to $num there are $accumulatedPrimes prime numbers.")
        0
      }
      //or else we keep incrementing accumulatedPrimes (if the num is prime)
      else accumulatedPrimes + (if(isPrime(num))1 else 0))
    }
    go(0, 0)
  }


  def isPrime(n: Int): Boolean = {
    @tailrec
    def go(num: Int, x: Int, accumulator: Boolean): Boolean = {
      if (x <= 1) accumulator
      else {
        val divisible: Boolean = num % x == 0
        go(num, x - 1, accumulator & !divisible)
      }
    }
    if(n == 1 || n == 0) false else {
      val abs = if (n < 0) n * -1 else n
      go(abs, abs - 1, accumulator = true)
    }
  }
}