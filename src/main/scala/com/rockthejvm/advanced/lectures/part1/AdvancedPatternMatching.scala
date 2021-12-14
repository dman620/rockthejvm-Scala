package com.rockthejvm.advanced.lectures.part1

object AdvancedPatternMatching extends App {
  val numbers = List(1)
  val description = numbers match{
    case head :: Nil => println(s"the only element is $head")
    case _ =>
  }
  /*
  * -constants
  * -wildcards
  * -case classes
  * -tuples
  * -some special magic, like above
  */
  class Person(val name: String, val age: Int)//not a case class, so can't pattern match with it (no boilerplate map, flatmap, filter)
  object PersonPattern{
    def unapply(person: Person): Option[(String, Int)] = {
      if(person.age < 21) Some(person.name -> person.age)
      else None
    }
    def unapply(age: Int): Option[String] = {
      Some(if(age < 21) "minor" else "major")
    }
  }
  val bob = new Person("Bob", 20)
  val greeting = bob match{
    case PersonPattern(n, a) => s"Hi, my name is $n, and I am $a years old."
    case _ =>
  }
  println(greeting) //works though Person is not a case class

  val legalStatus = bob.age match{
    case PersonPattern(status) => s"My Legal Status is $status"
    case _ =>
  }
  println(legalStatus)



}
