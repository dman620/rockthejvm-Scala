package com.rockthejvm.advanced.lectures.part1.part2afp

import scala.annotation.tailrec

object PartialFunctions extends App {

  val aFunction: Int => Int =
    (x: Int) => x + 1

  //problem: sometimes we may want a function that does not accept certain values.
  //example: isPrime() returns boolean, but let's pretend we don't want any negative inputs, how do we do that?

  //this is a little clunky but it works
  val isPrimeF: Int => Boolean = (x: Int) => {
    if (x == 0) false
    else if (x > 0) isPrime(x)
    else throw new FunctionNotApplicableException
  }

  class FunctionNotApplicableException extends RuntimeException

  println(isPrimeF(31))
  //println(isPrimeF(-2))

  val aNicerIsPrimeF: Int => Boolean = (x: Int) => x match {
    case 0 => false
    case i if i > 0 => isPrime(i)
    //no default case, it will crash if neither case is reached just like last implementation, but this one is a bit nicer
  }

  //this is daniel's "best syntax" option
  val aPartialIsPrime: PartialFunction[Int, Boolean] = {
    case 0 => false
    case i if i > 0 => isPrime(i)
  } //partial function value

  //but this actually looks nicer IMO
  //note: this is not a partial function type
  val bestIsPrime: Int => Boolean = {
    case 0 => false
    case i if i > 0 => isPrime(i)
  }
  println(bestIsPrime(65213))
  //difference between aPartialIsPrime and bestIsPrime?
  //aPartialIsPrime is of type PartialFunction[Int, Boolean]
  //bestIsPrime is a Function1[Int, Boolean]
  //they are different types, that is the main difference.
  //other differences, is utilities for the partial functions are not available for full functions.
  //PF extend normal functions

  //isDefinedAt: boolean, returns whether the function is defined at the given value
  println(aPartialIsPrime.isDefinedAt(-34))

  //lift: function, returns a function.  This function is essentially a wrapper that has the same behavior
  //as the unlifted function but returns an option.
  val liftedIsPrime = aPartialIsPrime.lift

  println(liftedIsPrime(-213))//None
  println(liftedIsPrime(5))//Some(true)

  //chain
  val pfChain = aPartialIsPrime.orElse[Int, Boolean]{
    case i if i < 0 => aPartialIsPrime(i * -1)
  }//implementation on the fly: "I actually want negative primes to return true"
  println(pfChain(90))//false
  println(pfChain(-53))//true

  //since PF extends anonymous functions, we can assign PF to an AF
  val anAnonymous: Int => Int = {
    case 1 => 3
    case 2 => 4
    case 8 => 39
  } //this is a partial function definition, assigned to a normal function
  println(anAnonymous(2))//4

  //HOFs can accept partial functions as well
  val aMappedList = List(1, 2, 3).map{
    case 1 => 30
    case 2 => 400
    case 3 => 40
  }

  //partial functions can only have one parameter

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
    go(n, n - 1, accumulator = true)
  }




}
