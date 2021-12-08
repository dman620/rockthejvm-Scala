package com.rockthejvm.beginner.patternmatching

import com.rockthejvm.beginner.OOP.mylist.{Empty, MyList, Node}

object AllThePatterns extends App {
  //1. Constants
  val x: Any = "Scala"
  val constants = x match{
    case 1 => "the number one"
    case "Scala" => "the string literal \"Scala\""
    case true => "boolean value of true"
    case AllThePatterns => "a singleton object of AllThePatterns type"
    case _ => "something else"
  }
  println(constants)

  //2. Match anything
  //2.1 wildcard
  val matchAnything = x match{
    case _ =>
  }
  //2.2 variable
  val matchAVariable = x match{
    case something => s"I've found $something"
  }

  //3 tuples
  val aTuple = 1 -> 2
  val matchATuple = aTuple match{
    case 1 -> 2 =>
    case (2, 0) =>
    case (something, 2) =>
  }
  val nestedTuple = (1, (3, 4)) match{
    case (1, (3, 4)) =>
  }
  //PMs can be nested

  //case classes -- constructor pattern
  val aList: MyList[Int] = Node(1, Node(2, Empty))
  val matchAList = aList match{
    case Empty =>
    case Node(head, tail) => println(head); tail.forEach(println)
    case Node(head, Node(subhead, subtail)) => "whoa"
  }

  //list patterns
  val aStandardList = List(1, 2, 3, 42)
  val standardListMatching = aStandardList match{
    case List(1, _, _, _) => //this is known as an extractor - advanced
    case List(1, _*) => //a list of arbitrary length - advanced
    case 1 :: List() => //infix pattern
    case List(1, 2, 3) :+ 42 => //infix pattern
  }

  //type specifiers
  val unknown: Any = 2
  val unknownMatch = unknown match{
    case list: List[Int] => list.foreach(println) //explicit type specifier
    case _ =>
  }

  //name binding
  val nameBindingMatch = aList match {
    case nonEmptyList @ Node(_, _) => //this case has a name "nonEmptyList"
    case Node(1, rest @ Node(2, _)) => //name binding inside of nested patterns
  }

  //multi patterns
  val multiPattern = aList match {
    case Empty | Node(0, _) =>  //compound, or multi pattern
    case _ =>
  }

  //if guards
  val secondElement = aList match {
    case Node(_, Node(specialElement, _)) if specialElement % 2 == 0 => //does not match if the predicate is false
    case _ =>
  }


  //Exercise
  val numbers = List(1, 2, 3)
  val numbersMatch = numbers match {
    case listOfStrings: List[String] => "a list of strings"
    case listOfNumbers: List[Int] => "a list of numbers"
    case _ => ""
  }
  println(numbersMatch) //"a list of strings"
  //JVM trick question
  /*
  * the JVM is written with backwards compatibility, thus a java 17 JVM can run programs written with java 1
  * in java 1, generic parameters did not exist
  * generics were introduced in java 5
  * to make the compiler compatible with java 1, the compiler erases all generic type AFTER type checking
  * which makes the JVM oblivious to generic types in this context.  the [String] and [Int] on line 85/6
  * is erased completely after the types are checked
  * this is called type erasure
  * */
}
