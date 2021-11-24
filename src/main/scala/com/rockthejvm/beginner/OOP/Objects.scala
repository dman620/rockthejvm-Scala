package com.rockthejvm.beginner.OOP

object Objects {
  //SCALA does not have class - level functionality ("static")
  object Person {
    //static or class level functionality
    val N_EYES = 2

    def canFly(): Boolean = false

    //factory method: its purpose is to build a new person given parameteres
    //factory methods do not have to be called apply, but they often are
    def apply(): Person = new Person("John")
    def apply(name: String): Person = new Person(name)
    def apply(mother: Person, father: Person, name: String): Person = Person(name)
  }

  class Person(val name: String) {
    //instance level functionality

  }
  //practice of writing classes and objects of same type is called companions

  def main(args: Array[String]): Unit = {
    println(Person.N_EYES)


    println(Person.canFly())
    //Scala object is a Singleton Instance
    //Person is its own type and its only instance

    val mary = Person("mary")
    val john = new Person("John")
    println(mary == john) // true if they are both references to singleton.  if they are new instances then false
    val bobby = Person.apply(mary, john, "Bobby")

    println(mary.name)
    //bobby = Person(mary, john) //equivalent
    //person singleton object called like a function

    //Scala Applications = Scala object with
    //def main(args: Array[String]): Unit = {...}
  }
}
