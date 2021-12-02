package com.rockthejvm.beginner.Functional

object Exercise28 extends App {
  def toCurry(f: (Int, Int) => Int): ( Int => Int => Int) =
    x => y => f(x, y)
  def fromCurry(f: Int => Int => Int): (Int, Int) => Int =
    (x, y) => f(x)(y)



  def compose[A](f1: A => A, f2: A => A): A => A = {
    (x: A) => f1(f2(x))
  }
  def andThen[A](f1: A => A, f2: A => A): A => A = {
    compose(f2, f1)
  }

  val f: Int => Int = (x: Int) => x * 2
  val g: Int => Int = (x: Int) => x * 4

  println(compose(f, g)(4))
}
