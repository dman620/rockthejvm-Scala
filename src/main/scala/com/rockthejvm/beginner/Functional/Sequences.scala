package com.rockthejvm.beginner.Functional

import scala.util.Random

//datas about collections is in video 30

object Sequences extends App {
  //sequences have a well defined order and they are indexed
  //meaning you can access any element by index easily
  val aSequence = Seq(1, 2, 3, 4)
  println(aSequence)
  println(aSequence.reverse)
  println(aSequence(2))
  println(aSequence ++ Seq(5, 6, 7))
  println((Seq(4, 5, 6) ++ aSequence).sorted)

  //ranges
  val aRange: Seq[Int] = 1 to 10
  aRange.foreach(print)
  println()
  (1 to 10).foreach(x => print(" " + x))
  println()

  //prepend
  val aList = 1 to 4
  val prepend = 42 +: aList :+ 34
  println(prepend)

  val apples5 = List.fill(5)("apple")
  println(apples5) //list containing "apples" 5 times
  println(aList.mkString("-")) //makes a string with that in middle of elements of the list

  //arrays
  val numbers = Array(1, 2, 3, 4)
  val threeElements = Array.ofDim[Int](3)
  println(threeElements)
  threeElements.foreach(println)

  numbers(2) = 5
  numbers.update(3, 100) //these 2 lines are the same thing
  println(numbers.mkString(" "))

  //arrays and seq
  val numbersSeq: Seq[Int] = numbers // implicit conversion
  println(numbersSeq)

  //vector
  //default implementation for immutable sequences
  //very fast even with large sizes
  val myVector: Vector[Int] = Vector(1, 2, 3)
  println(myVector)


  //vectors vs lists
  val maxCapacity = 1000000000
  val maxRuns = 1000
  def getWriteTime(collection: Seq[Int]): Double = {
    val r = new Random
    val times = for{
      it <- 1 to maxRuns
    }yield{
      val currentTime = System.nanoTime()
      collection.updated(r.nextInt(maxCapacity), r.nextInt(maxCapacity))
      System.nanoTime() - currentTime
    }
    times.sum * 1.0 / maxRuns
  }
  val numbersList = (1 to maxCapacity).toList
  val numbersVector = (1 to maxCapacity).toVector

  //keeps reference to tail
  //updating elements in middle takes a long time
  //println(getWriteTime(numbersList))
  //depth of the tree is small
  //needs to replace an entire 32 element chunk
  print(getWriteTime(numbersVector))

  //how fast can vector vs linkedlist update elements in a list of size n, accessing a random element 1000 times?
  //times are measured in nano seconds.

  //collection of size 10
  //list got 3548
  //vector got 513
  //vector was 7 times faster

  //collection of size 100
  //list got 5509
  //vector got 941
  //vector was 6 times faster

  //collection of size 5000
  //list got 43809.1
  //vector got 1263.8
  //vector was 35 times faster

  //Collection of size 1000000 (1 million)
  //list got 5587926.7
  //vector got 1825.2
  //vector was 473 times faster

  //collection of size 5000000 (5 million)
  //list got 29208428
  //vector got 2452
  //vector was 11,912 times faster
}
