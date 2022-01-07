package com.rockthejvm.advanced.lectures.part2afp


object LazyEvaluation extends App {
  //val x: Int = throw new RuntimeException //the program will crash
  lazy val x: Int = throw new RuntimeException() //does not crash program

  /* Few key points about lazy vals
  * 1. They are evaluated only once, even if called multiple times
  * 2. they are evaluated when needed.  i.e. Just-In-Time
  * */

  lazy val ex: Int = {
    println("Hello")
    42
  }
  println(ex)
  println(ex)

  //examples of implications
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }
  def simpleCondition = false
  lazy val lazyCondition = sideEffectCondition
  println(if(simpleCondition && lazyCondition) "yes" else "no")
  //the "Boo" side effect is  not printed here
  //the reason, is because we used short circuit evaluation "&&"
  //therefore, using side effects like this can cause problems

  //in conjunction with call by name
  def byNameMethod(n: => Int): Int = n + n + n + 1
  def retrieveMagicValue = {
    println("waiting")
    Thread.sleep(1000)
    42
  }
  //println(byNameMethod(retrieveMagicValue)) //we need to run retrieveMagicValue 3 times here
  //making it not call by name, or making retrieveMagicValue lazy would fix this
  def fixedByNameMethod(n: => Int): Int = {
    lazy val t = n
    t + t + t + 1
  }
 // println(fixedByNameMethod(retrieveMagicValue))

  //filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }
  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }
  val numbers = List(1, 25, 40, 5, 23)
/*  val lt30 = numbers.filter(lessThan30)
  val lt30Agt20 = lt30.filter(greaterThan20)
  println(lt30Agt20)*/

  val lt30Lazy = numbers.withFilter(lessThan30) //lazy vals under the hood
  val gt20Lazy = lt30Lazy.withFilter(greaterThan20)
  println(gt20Lazy)
  gt20Lazy.foreach(x => println(x))
  //if guards use lazy vals!
  for{
    a <- List(1,2,3,4) if a % 2 == 0//<---- lazy vals here!
  }yield a + 1
  List(1,2,3,4).withFilter(_ % 2 == 0).map(_ + 1) //same as the for-comprehension

}
