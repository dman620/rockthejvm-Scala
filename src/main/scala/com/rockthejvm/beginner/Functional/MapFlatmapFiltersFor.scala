package com.rockthejvm.beginner.Functional
import com.rockthejvm.beginner.OOP.mylist.Node
import com.rockthejvm.beginner.OOP.mylist.Empty
import com.sun.tools.sjavac.Transformer

import scala.runtime.Nothing$

object MapFlatmapFiltersFor extends App {
  val numbers = List(1,2,3,4)
  val chars = List('a','b','c','d')
  val colors = List("blue", "grey")
  //this is how we do "iterations" this is essentially a nested loop
  println(numbers.flatMap(n => chars.map(c => n + "-" + c)))

  //this is essentially 3 nested loops:
  println(numbers.flatMap(n => chars.flatMap(c => colors.map(col => n + "-" + c + "-" + col))))

  //these chains are hard to read, so to solve this we have for-comprehension
  val forCombinations = for{
    n <- numbers if n % 2 == 0 //this is used in the filter function
    c <- chars
    color <- colors
  } yield n + "-" + c + "-" + color
  //the above block evaluates to flatmap/map/filter chains but it's much more readable
  println(forCombinations)

  for{
    n <- numbers
  } println(n)
  //you can use for-comprehensions for side effects too

  //syntax overload.  it's poopy syntax but some ppl write this way, so watch out:
  println(List(1, 2, 3, 4).map { x =>
    x * 2
  })

  val num2 = new Node(1, new Node(2, new Node(3)))
  val char2 = new Node('a', new Node('b', new Node('c')))
  println(for{
    n <- num2
    c <- char2
  } yield n + "-" + c)



}
