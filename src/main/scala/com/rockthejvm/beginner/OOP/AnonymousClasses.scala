package com.rockthejvm.beginner.OOP

object AnonymousClasses extends App {
  abstract class Animal{
    def eat: Unit
  }

  val funnyAnimal = new Animal{
    override def eat: Unit = println("ahahahahahaha")
  }
  println(funnyAnimal.getClass)

  class Person(val name: String = "John"){
    def sayHi: Unit = println(s"Hi, my name is $name, how can I help?")
  }
  val jim = new Person(){
    override def sayHi: Unit = println(s"Hi, my name is $name, how can I be of service?")
  }
  println(jim.getClass)
}
