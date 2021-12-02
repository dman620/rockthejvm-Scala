package com.rockthejvm.beginner.Functional

import scala.annotation.tailrec

object Exercise25 extends App {
  //1
  val cat = new ((String, String) => String){
    override def apply(s1: String, s2: String): String = s1 + s2
  }

  val str1 = "Hello"
  val str2 = "Scala"
  println(cat(str1, str2))

  //2
  //see myList

  //3 define a func which takes an int and returns another function which takes an int and returns an int
    //what's the type of this function => It is "Int => Int" or Function1
  val weirdFunc = new (Int => (Int => Int)){
    override def apply(v1: Int): Int => Int = {
      if(v1 % 2 == 0)
        new (Int => Int) {
          override def apply(v1: Int): Int = v1 * 2
        }
      else
        new(Int => Int) {
          override def apply(v1: Int): Int = v1 / 2
        }

    }
  }

  @tailrec
  def weirdPrint(n: Int): Unit = {
    if(n < 0) ()
    else {
      println(weirdFunc(n)(n)) //curried functions can be called with 2 parameter lists
      weirdPrint(n - 1)
    }
  }
  weirdPrint(30)
}
