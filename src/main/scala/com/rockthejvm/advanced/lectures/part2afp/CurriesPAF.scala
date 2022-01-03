package com.rockthejvm.advanced.lectures.part2afp

object CurriesPAF extends App {
  //curried functions
  //funcs that take or return functions
  val superAdder: Int => Int => Int = {
    x => y => x + y
  }
  val add3: Int => Int = superAdder(3)
  println(add3(5))          //8
  println(superAdder(3)(5)) //8

  //curried method
  def curriedAdder(x: Int)(y: Int): Int = x + y

  //following must be type annotated
  val add4: Int => Int = curriedAdder(4)

  //lifting
/*  def add5(x: Int) = x + 5
  def isEven(x: Int) = x % 2 == 0 //these are methods
  (add5 andThen isEven)(3)
  val fAdd5 = add5 _
  val fIsEven = isEven _  //these are functions */
  //lifting refers to a couple things:
  /*
  * 1. changing a partial function into a total function, which returns an Option (see PartialFunctions.scala in this unit)
  * 2. changing a method into a function, which we are doing now (eta-expansion)
  * why do we need to lift methods into functinos?  Because methods cannot be used in higher order functions
  * (sometimes done for us by the compiler)*/
  def inc(x: Int) = x + 1
  List(1, 2, 3).map(inc) //eta-expansion done for us
  //then rewrites as ...map(x => inc(x))

  //partial function applications
  val add5 = curriedAdder(5) _
  //underscore means to lift the method into a function using eta expansion

  //underscores are powerful
  def concatenator(s1: String, s2: String, s3: String): String = s1 + s2 + s3
  val insertName = concatenator("Hello, I'm ", _, ". How are you?")
  println(insertName("Derek"))
  //putting the underscore there means to return a function which requires that as a parameter.

}
