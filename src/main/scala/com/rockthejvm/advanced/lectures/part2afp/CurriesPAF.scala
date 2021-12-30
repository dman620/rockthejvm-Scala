package com.rockthejvm.advanced.lectures.part2afp

object CurriesPAF extends App {
  //curried functions
  //funcs that take or return functions
  val superAdder: Int => Int => Int = {
    x => y => x + y
  }
  val add3: Int => Int = superAdder(3)
  println(add3(5))
  println(superAdder(3)(5))

  //curried method
  def curriedAdder(x: Int)(y: Int): Int = x + y

  //following must be type annotated
  val add4: Int => Int = curriedAdder(4)
}
