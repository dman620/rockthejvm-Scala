package com.rockthejvm.beginner.OOP

object CaseClasses extends App {

  case class Person(name: String, age: Int)

  // 1. Class parameters are fields
  val jim = new Person("jim", 34)
  println(jim.name + ", " + jim.age)
  // 2. sensible toString
  println(jim)
  // 3. equals and hashCode implemented out of box, making them super useful for collections
  val jim2 = new Person("jim", 34)
  println(jim == jim2) //returns true
  //if it was a regular class, it would be false since they are 2 different references, but here the equals() is implemented
  // 4. copy method implemented
  val jim3 = jim.copy(age = 45) //returns instance of person same as jim but with age = 45
  println(jim3)
  // 5. Companion objects
  val thePerson = Person
  val mary = Person("Mary", 23) //no new keyword needed, this calls Person.apply(String, Int)
  // 6. serializable
  //making them useful for distributed systems.
  //useful for aka (akka?) ahkah
  // 7. extractor patterns = CCs can be used in PATTERN MATCHING (one of the most powerful scala features)

  case object UnitedKingdom{
    def name: String = "The UK of GB and NI"
  }
  //case objects are the same as case classes except they don't get a companion object because they are the object (no companion class)
}
