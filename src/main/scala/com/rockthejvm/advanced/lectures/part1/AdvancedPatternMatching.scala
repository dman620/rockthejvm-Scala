package com.rockthejvm.advanced.lectures.part1

import scala.annotation.tailrec
import scala.collection.convert.ImplicitConversions.`collection asJava`

object AdvancedPatternMatching extends App {
  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head")
    case _ =>
  }

  /*
  * -constants
  * -wildcards
  * -case classes
  * -tuples
  * -some special magic, like above
  */
  class Person(val name: String, val age: Int) //not a case class, so can't pattern match with it (no boilerplate map, flatmap, filter)

  object PersonPattern {
    def unapply(person: Person): Option[(String, Int)] = {
      if (person.age < 21) Some(person.name -> person.age)
      else None
    }

    def unapply(age: Int): Option[String] = {
      Some(if (age < 21) "minor" else "major")
    }
  }

  val bob = new Person("Bob", 20)
  val greeting = bob match {
    case PersonPattern(n, a) => s"Hi, my name is $n, and I am $a years old."
    case _ =>
  }
  println(greeting) //works though Person is not a case class

  val legalStatus = bob.age match {
    case PersonPattern(status) => s"My Legal Status is $status"
    case _ =>
  }
  println(legalStatus)

  //case head :: Nil => println(s"the only element is $head")
  //this is an infix pattern, with the ::, we will do our own here
  //INFIX PATTERNS
  case class Or[A, B](a: A, b: B) //in scala this is called Either

  val either = Or(2, "two")
  val humanDescription = either match {
    //case Or(number, string) => s"$number is written as $string"
    //same as
    case number Or string => s"$number is written as $string"
  }
  println(humanDescription)

  //decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }
  //the above pattern matches any list that starts with 1.  i.e. Ambiguous length
  //how to decompose a list where there is an unknown number of elements to decompose?
  abstract class MyList[+A] {
    def head: A = ???

    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]

  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList{
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if(list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }
  //in action:
  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  println(myList match{
    case MyList(1, 2, _*) => "starting with 1, 2"
    case _ => "not starting with 1, 2"
  })

  //dereks practice
  def isPrime(n: Int): Boolean = {
    @tailrec
    def go(num: Int, x: Int, accumulator: Boolean): Boolean = {
      if (x <= 1) accumulator
      else {
        val divisible: Boolean = num % x == 0
        go(num, x - 1, accumulator & !divisible)
      }
    }

    //3 ways of accomplishing the same goal
    val abs = if (n < 0) n * -1 else n
    go(abs, abs - 1, accumulator = true)
    /*    if(n < 0)go(n * -1, (n * -1) - 1, accumulator = true)
        else go(n, n - 1, accumulator = true)*/
    /*
        go(if(n < 0) n * -1 else n, (if(n < 0)n * -1 else n) - 1, accumulator = true)
    */
  }

  def findThePrimes(nums: Int*): List[Int] = {
    @tailrec
    def go(thisList: Seq[Int], accumulator: List[Int]): List[Int] = {
      if (thisList.isEmpty) accumulator
      else go(thisList.tail, if (isPrime(thisList.head)) accumulator :+ thisList.head else accumulator)
    }

    go(nums, List[Int]())
  }

  findThePrimes(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 31, 53, 102, 314, 2223423, 234213, 1039, 1234567, 32432234,  314159)
    .forEach(x => println(x + " is prime"))


}
