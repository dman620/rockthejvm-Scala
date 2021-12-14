package com.rockthejvm.advanced.exercises

import scala.annotation.tailrec


object Lecture6 extends App {
  val MAX_NOTES = 400
  val n: Int = 56
  val mathProperty = n match{ //small brain
    case x if x < 10 => "single digit"
    case x if x % 2 == 0 => "even"
    case _ => "no property"
  }
  /* Exercise:
  *  devise custom pattern matching solution for these conditions
  */
  object IsPrime{
    def unapply(n: Int): Option[String] = {
      if(n == 0) None
      else if(n < 0) Some("Integer cannot be negative")
      else Some("Your integer " + (if(isPrime(n)) "is prime." else "is not prime."))
    }
  }
  object IsEven{
    def unapply(n: Int): Option[String] = {
      if(n % 2 == 0) Some(s"${n} is even")
      else Some(s"${n} is not even")
    }
  }

  //function returns true if n is prime, otherwise false
  //if n <= 0 the function will return true
  def isPrime(n: Int): Boolean = {
    @tailrec
    def go(n: Int, x: Int, accumulator: Boolean): Boolean = {
      if(x <= 1) accumulator
      else {
        val divisible: Boolean = n % x == 0
        go(n, x - 1, accumulator & !divisible)
      }
    }
    go(n, n - 1, accumulator = true)
  }

  val myNum = -2
  //requires the definition of Int.unapply above to work
  //this is the galaxy brain solution
  val primeStatus = myNum match{
    case IsPrime(status) => s"Integer: $myNum \n" + status
    case IsEven(status) => s"Integer: $myNum \n" + status
    case _ => s"Unable to determine prime-ness of $myNum"
  }
  //does not use any supplemental code to work
  //this is the small brain solution
  val primeStatus2 = isPrime(myNum) match{
    case true => s"Your integer $myNum is prime"
    case false => s"Your integer $myNum is not prime"
  }
  //also does not use any supplemental code to work
  //medium brain solution
  val primeStatus3 = myNum match{
    case x if x == 0 => s"Cannot determine the prime-ness of ${x}"
    case x if x < 0 => s"Cannot determine the prime-ness of negative number $x"
    case x if isPrime(x) => s"${x} is prime"
    case x if !isPrime(x) => s"${x} is not prime"
  }
  println(primeStatus)
  println(primeStatus2)
  println(primeStatus3)

  //extra practice because i want to:
  //make a list with every prime up to max of n

  val max = 10
  val listOfNPrimes = List.range(1, max).filter(isPrime)
  println(s"Every prime up to $max is:")
  listOfNPrimes.foreach(x => println("-->" + x))


}
