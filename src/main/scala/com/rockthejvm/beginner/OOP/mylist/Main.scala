package com.rockthejvm.beginner.OOP.mylist

import scala.annotation.tailrec
import scala.runtime.Nothing$

object Main extends App {

  val list1 = new Node(1, new Node(2, new Node(3)))
  val list3 = new Node(4, new Node(5, new Node(6)))
  val list2 = new Node("Hello", new Node("Scala", new Node("!")))
  val list4 = list1 ++ list3

  //when underscores are used multiple times, it tells compiler that it's 2 different params.  so if you will need to reference
  //the same param twice, you cannot use the underscore notation and must do it like this:
 /* println((list1 ++ list3).flatMap(elem => new Node(elem, new Node(elem + 1, Empty))))
  println(list2)
  println(list4.map(_ * 7 % 5))
  println(list4.filter(_ * 27 % 5 >= 2))
  println(list2.filter(_ == "Hello"))*/

  list1.forEach((x: Int) => print(x + " "))
  println()
  list4.forEach(println)
  def makeABigList(n: Int): MyList[Int] = {
    @tailrec
    def go(n: Int, accumulatedList: MyList[Int]): MyList[Int] = {
      if(n < 0) accumulatedList
      else go(n - 1, accumulatedList.add(n))
    }
    go(n, new Node(n + 1, Empty))
  }
  def bigListTest(): Unit = {
    val list5 = makeABigList(1000000)
    println()
    list5.forEach(print)
  }
  //bigListTest() //made for testing out the forEach function on huge-ly sized lists

  println(list4.sort(_ < _)) // (x, y) => x < y
  println(list1.zipWith[String, String](list2, (x, y) => x + " " + y))

}
