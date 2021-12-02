package com.rockthejvm.beginner.Functional

import scala.annotation.tailrec

object HigherOrderFunctions extends App{
  //functions that applies a function n times over x

/*  val applyFuncNTimesToX: Int = (func: Int => Int, n: Int, x: Int) => {
    if(n <= 0) x
    else applyFuncNTimesToX(func, n - 1, func(x))
  }*/
  @tailrec
  def nTimes(f: Int => Int, n: Int, x: Int): Int = {
    if(n <= 0) x
    else nTimes(f, n - 1, f(x))
  }
  val ecks = 1
  val eckstoo = nTimes(x => x + 1, 10, ecks)

  val doubler: Int => Int = (x: Int) => x * 2
  println(nTimes(doubler, 10, ecks))

  //power of 2 func that works on x >= 0
  def pOf2(n: Int): Int = {
    @tailrec
    def go(n: Int, accumulator: Int): Int = {
      if (n <= 0) accumulator
      else go(n - 1, accumulator * 2)
    }
    go(n, 1)
  }
  println(pOf2(3))

  //incrementx
  def funcx(f: Int => Int, n: Int): Int => Int ={
    if(n < 0) (x: Int) => x
    else (x: Int) => funcx(f, n - 1)(f(x))
  }
}
