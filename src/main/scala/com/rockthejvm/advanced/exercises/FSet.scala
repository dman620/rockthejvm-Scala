package com.rockthejvm.advanced.exercises

import scala.annotation.tailrec

//exercise implement a functional set
trait FSet[A] extends (A => Boolean){
  def apply(elem: A): Boolean = contains(elem)
  def contains(elem: A): Boolean
  def +(elem: A): FSet[A]
  def ++(anotherSet: FSet[A]): FSet[A]
  def map[B](f: A => B): FSet[B]
  def flatMap[B](f: A => FSet[B]): FSet[B]
  def filter(predicate: A => Boolean): FSet[A]
  def foreach(f: A => Unit): Unit
}

class Empty[A] extends FSet[A]{
  def contains(elem: A): Boolean = false
  def +(elem: A): FSet[A] = new Cons(elem, this)
  def ++(anotherSet: FSet[A]): FSet[A] = anotherSet
  def map[B](f: A => B): FSet[B] = new Empty[B]
  def flatMap[B](f: A => FSet[B]): FSet[B] = new Empty[B]
  def filter(predicate: A => Boolean): FSet[A] = this
  def foreach(f: A => Unit): Unit = ()
}

class Cons[A](val head: A, tail: FSet[A]) extends FSet[A]{
  def contains(elem: A): Boolean = {
    elem == head || tail.contains(elem)
  }
  def +(elem: A): FSet[A] = {
    if (this.contains(elem)) this
    else new Cons[A](elem, this)
  }
  def ++(anotherSet: FSet[A]): FSet[A] = {
    tail ++ anotherSet + head
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
    (tail map f) + f(head)
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
    (tail flatMap f) ++ f(head)
  }
  def filter(predicate: A => Boolean): FSet[A] = {
    val filteredTail = tail filter predicate
    if(predicate(head)) filteredTail + head
    else filteredTail
  }
  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
}

object Cons{
  def apply[A](values: A*): FSet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], accumulatedSet: FSet[A]): FSet[A] = {
      if(valSeq.isEmpty) accumulatedSet
      else buildSet(valSeq.tail, accumulatedSet + valSeq.head)
    }
    buildSet(values, new Empty[A])
  }
}

class Person(val name: String, val age: Int){

  def canEqual(other: Any): Boolean = other.isInstanceOf[Person]

  override def equals(other: Any): Boolean = other match {
    case that: Person =>
      (that canEqual this) &&
        name == that.name &&
        age == that.age
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(name, age)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object MySetPlayground extends App {
  val theSet = Cons(1, 2, 3, 26, 21, 102, 32, 53, 2, 10, 11, 15, 67, 314159)
  theSet + 3 + 7 + 7 ++ Cons(1, 89, 30) + 34  filter isPrime foreach println

  for {
    char <- Cons[Char]('a', 'b', 'c')
    num <- theSet
  } yield print( " " + num + " - " + char)

  val set1 = Cons[Int](4, 5, 6)
  val set2 = Cons[Int](1, 2, 3)
  set1.filter(set2).foreach(println)

  def isPrime(n: Int): Boolean = {
    @tailrec
    def go(num: Int, x: Int, accumulator: Boolean): Boolean = {
      if(x <= 1) accumulator
      else {
        val divisible: Boolean = num % x == 0
        go(num, x - 1, accumulator & !divisible)
      }
    }
    val abs = if(n < 0) n * -1 else n
    go(abs, abs - 1, accumulator = true)
  }
}