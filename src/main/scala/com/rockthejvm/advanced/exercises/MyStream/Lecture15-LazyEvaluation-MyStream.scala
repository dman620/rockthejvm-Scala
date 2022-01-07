package com.rockthejvm.advanced.exercises.MyStream

import scala.annotation.tailrec

abstract class MyStream[+A] {
  def apply(n: Int): A

  def isEmpty: Boolean

  def head: A

  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B] //prepend operator

  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] //concatenation

  def foreach(f: A => Unit): Unit

  def map[B](f: A => B): MyStream[B]

  def flatMap[B](f: A => MyStream[B]): MyStream[B]

  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] //takes the first n elements out of this stream

  def asList: List[A] //returns the stream as a list
}

case object Empty extends MyStream[Nothing] {
  def apply(n: Int): Nothing = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def head: Nothing = throw new NoSuchElementException

  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](element: B): MyStream[B] = new Cons(element, this) //prepend operator

  def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream //concatenation

  def foreach(f: Nothing => Unit): Unit = ()

  def map[B](f: Nothing => B): MyStream[B] = this

  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this

  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this //takes the first n elements out of this stream

  def asList: List[Nothing] = Nil //returns the stream as a list
}

class Cons[+A](h: A, t: => MyStream[A]) extends MyStream[A] {
  def apply(n: Int): A = {
    @tailrec
    def go(current: MyStream[A], n: Int): A = {
      if(n <= 0) current.head
      else go(current.tail, n - 1)
    }
    go(this, n)
  }
  def isEmpty: Boolean = false

  val head: A = h

  lazy val tail: MyStream[A] = t

  def #::[B >: A](element: B): MyStream[B] = new Cons(element, this)

  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream)

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

  def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f))

  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)

  def filter(predicate: A => Boolean): MyStream[A] = {
    if(predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate)
  }

  def take(n: Int): MyStream[A] = {
    if(n <= 0) Empty
    else new Cons(head, tail.take(n-1))
  }

  def asList: List[A] = {
    @tailrec
    def go(current: MyStream[A], accumulatedList: List[A]): List[A] = {
      if(current.isEmpty) accumulatedList
      else go(current.tail, accumulatedList :+ current.head)
    }
    go(this, List())
  }
  def asVector: Vector[A] = this.asList.toVector
}

object Cons {
  def from[A](start: A)(generator: A => A): MyStream[A] = {
    new Cons(start, from(generator(start))(generator))
  }
}


object isPrime{
  def apply(n: Int): Boolean = {
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
  def apply(n: BigInt): Boolean = {
    @tailrec
    def go(num: BigInt, x: BigInt, accumulator: Boolean): Boolean = {
      if (x <= 1) accumulator
      else {
        val divisible: Boolean = num % x == 0
        go(num, x - 1, accumulator && !divisible)
      }
    }
    if(n == 1 || n == 0) false else {
      val abs = if (n < 0) n * -1 else n
      go(abs, abs - 1, accumulator = true)
    }
  }
}
//implementation of an isPrime filter using Eratospthene's sieve method
object sievePrime{
  def apply(n: Int): Boolean = {
    @tailrec
    def go(numberInQuestion: Int, comparedNumber: Int, accumulatedBoolean: Boolean): Boolean = {
      if(comparedNumber <= 2 || !accumulatedBoolean){
        if(numberInQuestion % 2 == 0 && numberInQuestion != 2) false else accumulatedBoolean
      }
      else{
        val divisible: Boolean = numberInQuestion % comparedNumber == 0
        go(numberInQuestion, comparedNumber - 1, accumulatedBoolean && !divisible)
      }
    }
    if(n <= 1) false
    else {
      val square: Int = Math.sqrt(n).ceil.asInstanceOf[Int]
      go(n, square, accumulatedBoolean = true)
    }
  }
}
//stream of all fibonacci numbers
//using BigInt due to how quickly this series grows
object fibStream{
  def apply: MyStream[BigInt] = {
    def go(previous: BigInt, current: BigInt): MyStream[BigInt] = {
      new Cons(previous, go(current, previous + current))
    }
    go(previous = 0, current = 1)
  }
}
//prime stream implementation of Eratosthenes sieve
object primeStream{
  def apply: MyStream[Int] = {
    def go(accumulatedStream: MyStream[Int]): MyStream[Int] = {
      new Cons(accumulatedStream.head, go(accumulatedStream.tail.filter(x => x % accumulatedStream.head != 0)))
    }
    val naturals = Cons.from(2)(_ + 1)
    go(naturals)
  }
}

object Lecture15LazyEvaluation extends App {
  /*
  * Exercise: implement a lazily evaluated, singly linked STREAM of elements
  * examples:
  * naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers
  * naturals.take(100) //lazily evaluated stream of the first 100 naturals (finite stream)
  * naturals.foreach(println) //will crash - infinite loop!
  * naturals.map(_ * 2) //stream of all even numbers (potentially infinite)
  * */
  val naturals = Cons.from(1)(x => x + 1)
  val first100N = naturals.take(100)
  //first100N.foreach(println) //prints 1 - 100
  //naturals.foreach(println) //stack overflow (it actually prints up to 12233 first though)

  val evens = naturals.map(_ * 2)
  val first100E = evens.take(100)
  //first100E.foreach(println) //prints 2 - 200
  //evens.foreach(println) //stack overflow

  val first100Primes = naturals.filter(isPrime(_)).take(100)
  //first100Primes.foreach(println)

  /*naturals filtered < 100 is still a theoretically infinite collection.  the compiler has no way of knowing
    that only 100 elements will go in there.  To figure that out with current tools, the compiler will attempt to examine
    every element, resulting in a stack overflow if we do something like this:
    naturals.filter(_ < 10).toList().foreach(println)
    even though WE can figure the stream would be [1,2,3,4,5,6,7,8,9], the compiler can't figure it out.  So we must use
    take:
    naturals.filter(_ < 10).take(10).toList().foreach(println)
    also, the line:
      naturals.filter(_ < 100).take(50).take(4564).foreach(println)
    compiles, while the line:
      naturals.filter(_ < 10).take(50)
    will fail is because in the former line, the take(50) converts the stream into a finite stream.  While in the latter
    line, we are still working with an "infinite" stream.  The compiler is unable to use Empty's implementations because
    it has no way of knowing that the collection is actually finite.
    But in the former, when it takes 50, the collection is finite.  So when we take 4564, the compiler simply crosses
    over to Empty's implementation once it runs out of elements to take, and returns Empty.  So no error.*/


  //print the first n primes with a label i.e.
  /*
  place | prime
  * 1 ---- 2
  * 2 ---- 3
  * 3 ---- 5
  * 4 ---- 7
  * etc...
  * */
  def printFirstNPrimes(max: Int): Unit = {
    val nat: MyStream[Int] = Cons.from(1)(x => x + 1)
    val prime = nat.filter(isPrime(_))
    @tailrec
    def go(n: Int, current: MyStream[Int]): Unit = {
      println(s"Prime # $n is: -${current.head}-")
      if(n < max)go(n + 1, current.tail)
    }
    go(1, prime)
  }
  //printFirstNPrimes(1000000000)
  //10000: 14:71 with old method
  //println(naturals.filter(isPrime(_))(Int.MaxValue))

  //testing ++ (works)
 /* val first10nat = naturals.take(10)
  val first10prime = naturals.filter(isPrime(_)).take(10)
  (first10nat ++ first10prime).foreach(println)*/

  //testing #:: (works)
/*  val natPlusnum = 112 #:: naturals
  println(natPlusnum(0))
  natPlusnum.take(12).foreach(println)*/

  //testing asList
  val aList: List[Int] = naturals.filter(isPrime(_)).take(10).asList  ++ List(1,2,3)
  //aList.map(_ + 2).foreach(println)


  val startFrom0 = 0 #:: naturals
  val s = List(0, 1, 2, 3)
  val r = List(1, 2, 3, 4)

  //startFrom0.flatMap(x => new Cons(x, new Cons(x + 1, Empty))).take(10).foreach(println)
  //startFrom0.filter(_ < 10).foreach(println) //can't do this because it will try to filter every natural number

  //startFrom0.take(10).foreach(println)

  /*EXERCIZES # 2
  * 1. stream of fibonacci numbers
  * 2. stream of prime numbers with Eratospthenes's sieve
  * */

  //FIBONACCI NUMBERS
  println("FIBONACCI DEMONSTRATION")
  //fibStream.apply.take(10).foreach(println)
  fibonacci(0, 1).take(10).foreach(println)
  println()

  def fibonacci(first: BigInt, second: BigInt): MyStream[BigInt] = {
    Cons.from(first -> second){
      case (curr, next) => (next, curr + next)
    }.map(_._1)
  }

  //ERATOSPTHENES SIEVE
  println("ERATOPTHENES SIEVE DEMONSTRATION")
  primeStream.apply.take(10).foreach(println)
  println()


  //a -sort of- failed attempt:
 /* val oneto100: List[Int] = Range(1, 10000).toList
  val time1 = System.nanoTime()
  val isPrimeFilter: List[Int] = oneto100.filter(isPrime(_))
  val time2 = System.nanoTime()
  val sievePrimeFilter: List[Int] = oneto100.filter(sievePrime(_))
  val time3 = System.nanoTime()


  val isPrimeTime = time2 - time1
  val sievePrimeTime = time3 - time2
  val agreed: Boolean = sievePrimeFilter == isPrimeFilter
  println(s"The two algorithms are ${if(!agreed)"not"} in agreement.")
  println(s"time of isPrime: ${isPrimeTime}ns, ${isPrimeTime/1000000}ms, or ${isPrimeTime/1000000000}s.")
  println(isPrimeFilter)
  println(s"time of sievePrime: ${sievePrimeTime}ns, ${sievePrimeTime/1000000}ms, or ${sievePrimeTime/1000000000}s.")
  println(sievePrimeFilter)
  println()
  val difference = sievePrimeTime - isPrimeTime
  if(difference > 0) println(s"isPrime is faster than sievePrime by ${difference/1000000}ms,or ${difference/1000000000}s.")
  else println(s"sievePrime is faster than isPrime by ${-1 * difference/1000000}ms, or ${-1 * difference/1000000000}s.")
  println()*/
  println("done - " + System.nanoTime())
}
