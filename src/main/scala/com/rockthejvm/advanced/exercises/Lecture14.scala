package com.rockthejvm.advanced.exercises

object Lecture14 extends App {
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  //exercise: implement an "add7" function in as many different ways as I can
  //Int => Int = y => 7 + y      <-- default implementation

  //method
  def mAdd7(x: Int): Int = x + 7
  //function
  val fAdd7: Int => Int = (x: Int) => x + 7
  //curried method
  def mCAdd7(x: Int)(y: Int): Int = x + y + 7
  //curried function
  val fCAdd7: Int => Int => Int = x => y => x + y + 7

  val add7 = (x: Int) => simpleAddFunction(7, x)
  val add7_2 = simpleAddFunction.curried(7)
  val add7_7 = simpleAddFunction(7, _)

  //these all do the same thing with alt syntax
  val add7_3 = curriedAddMethod(7) _
  val add7_4 = curriedAddMethod(7)(_)
  val add7_5 = (x: Int) => curriedAddMethod(7)(x)

  //another alternate syntax to ask the compiler to do ita expansion, turning the method into a func
  val add7_6 = simpleAddMethod(7, _: Int)
  //val add7_6 = simpleAddMethod(7, _) //same thing (the type is not needed)
  println(add7_6(5))

  /*
  * 1. process a list of numbers and return their string representations with different formats
  *   use the %4.2f %8.6f %14.12f with curried formatter function
  * 2. difference between
  *   -functions vs methods
  *   -parameters: by name vs 0-lambda
  * */

  //exercise 1
  val formatter: String => (Double => String) = {
    formatString => number =>
        formatString.format(number)
  }
  val format2 = formatter("%4.2f")
  val format6 = formatter("%8.6f")
  val format12 = formatter("%14.12f")

  val testVec = Vector(43.203462345235234234, 156.32234523452349080, 9187.293234523081, 29.390214801)
  println("-------Exercise1-------")
  println("-----------------------")
  testVec.foreach(x => println(format2(x)))
  println("-----------------------")
  testVec.foreach(x => println(format6(x)))
  println("-----------------------")
  testVec.foreach(x => println(format12(x)))
  testVec.foreach((x: Double) => println(format12(x))) //valid syntax

  //exercise 2
  def byName(n: Int): Int = n + 1
  def byFunction(f: () => Int): Int = f() + 1
  def method: Int = 42
  def parenMethod(): Int = 42
  //use above functions
  println("------Exercise2--------")
  println("-----------------------")
  println("------int--------------")
  println(byName(10))
  println("------method-----------")
  println(byName(method))
  println("------parenmethod------")
  println(byName(parenMethod()))

  //direct the compiler to do eta
  println(byFunction(method _))
  //automatic eta
  println(byFunction(parenMethod))

  //we pass a function [() => 42] and then call it with ()
  println(byName((() => 42)()))
  println(byName(((x: Int) => x + 1)(41)))
  //println(byName((x => x + 1)(3)))
  //we pass a function but no need to call it
  println(byFunction(() => 42))

  //test lambda
  val lamb: Int => Int =
    (x: Int) =>
      x + 1
}
