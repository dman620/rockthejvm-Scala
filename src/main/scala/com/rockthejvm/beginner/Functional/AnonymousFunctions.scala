package com.rockthejvm.beginner.Functional

object AnonymousFunctions extends App {
  val doubler = (x: Int) => x * 2
  val adder: (Int, Int) => Int = (x: Int, y: Int) => x + y
  val justDoSomething = () => 3

  //be careful!
  println(justDoSomething) //prints the function itself
  println(justDoSomething()) //calls the function

  //curly braces, common style of creating lambdas
  val stringToInt = { (str: String) =>
    str.toInt
  }

  //MOAR SYNTACTIC SUGAR!!!
  //you need the type for the compiler to know what each underscore means
  val niceIncrementer: Int => Int = _ + 1
  val niceAdder: (Int, Int) => Int = _ + _

  //rewrite this as an anonymous func:
/*  val weirdFunc = new (Int => (Int => Int)){
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
  }*/
  val weirdFunc: Int => Int => Int = (x: Int) => if(x < 50){(y: Int) => y * 2}else{(y: Int) => y/2}
  println(weirdFunc)//should print the function as a string
  println(weirdFunc(2))//should print the sub func as a string for y*2
  println(weirdFunc(56))//prints the sub func as a string for y/2
  println(weirdFunc(56)(100))//50
  println(weirdFunc(2)(100))//200
}
