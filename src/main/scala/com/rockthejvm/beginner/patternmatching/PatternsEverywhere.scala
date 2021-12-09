package com.rockthejvm.beginner.patternmatching

object PatternsEverywhere extends App {

  //big idea # 1
  try{
    //code
  }catch{
    case e: RuntimeException => "runtime"
    case npe: NullPointerException => "npe"
    case _ => "something else"
  }
  //catches are actually matches

  //big idea 2
  val list = List(1,2,3,4)
  val evenOnes = for{
    x <- list if x % 2 == 0
  } yield 10*x
  //generators are also based on pattern matching
  //inception-horn.wav
  val tuples = List(1->2, 2->4)
  val filterTuples = for{
    (first, second) <- tuples
  }yield first * 2 -> second * 2
  filterTuples.foreach(x => println(x._1 + " - " + x._2))

  //big idea # 3
  val tuple = (1, 2, 3)
  val (a, b, c) = tuple
  println(a + " " + 2 + " " + 3)
  //multiple value definitions based on PATTERN MATCHING
  //not just for tuples though

  val dereksBigOlHead :: tail = list
  println(dereksBigOlHead)
  println(tail)

  //big idea #4
  //partial function
  val mappedList = list.map {
    case v if v % 2 == 0 => v + " is even"
    case 1 => "the one"
    case _ => "something else"
  } //same as list.map{x => x match { ...
  println(mappedList)
}
