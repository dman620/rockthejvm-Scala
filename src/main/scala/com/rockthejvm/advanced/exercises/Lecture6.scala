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
  object prime{
    def unapply(n: Int): Boolean = isPrime(n)
  }
  object even{
    def unapply(n: Int): Boolean = n % 2 == 0
  }

  //function returns true if n is prime, otherwise false
  //if n <= 0 the function will return true
  def isPrime(n: Int): Boolean = {
    @tailrec
    def go(num: Int, x: Int, accumulator: Boolean): Boolean = {
      if(x <= 1) accumulator
      else {
        val divisible: Boolean = num % x == 0
        go(num, x - 1, accumulator & !divisible)
      }
    }
    //3 ways of accomplishing the same goal
    val abs = if(n < 0) n * -1 else n
    go(abs, abs - 1, accumulator = true)
/*    if(n < 0)go(n * -1, (n * -1) - 1, accumulator = true)
    else go(n, n - 1, accumulator = true)*/
/*
    go(if(n < 0) n * -1 else n, (if(n < 0)n * -1 else n) - 1, accumulator = true)
*/
  }

  val myNum = -7
  //requires the definition of Int.unapply above to work
  //this is the galaxy brain solution
  val primeStatus = myNum match{
    case prime() if (myNum != 0) => s"Integer: $myNum is a prime number!"
    case even() => s"Integer: $myNum is an even number!"
    case _ => s"Unable to determine properties of $myNum"
  }
  //does not use any supplemental code to work
  //this is the small brain solution
/*  val primeStatus2 = isPrime(myNum) match{
    case true => s"Your integer $myNum is prime"
    case false => s"Your integer $myNum is not prime"
  }*/
  //also does not use any supplemental code to work
  //medium brain solution
/*  val primeStatus3 = myNum match{
    case x if x == 0 => s"Cannot determine the prime-ness of ${x}"
    case x if x < 0 => s"Cannot determine the prime-ness of negative number $x"
    case x if isPrime(x) => s"${x} is prime"
    case x if !isPrime(x) => s"${x} is not prime"
  }*/
  println(primeStatus)
  //println(primeStatus2)
  //println(primeStatus3)

  //extra practice because i want to:
  //make a list with every prime up to max of n

/*  val max = 10
  val listOfNPrimes = List.range(1, max).filter(isPrime)
  println(s"Every prime up to $max is:")
  listOfNPrimes.foreach(x => println("-->" + x))*/


}
