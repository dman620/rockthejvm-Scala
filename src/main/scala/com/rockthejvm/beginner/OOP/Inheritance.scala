package com.rockthejvm.beginner.OOP

import scala.language.postfixOps

object Inheritance extends App {
  class Animal {
    protected val creatureType: String = "wild"
    def eat = println("nom nom an animal is eating")
    //private makrks the method only usable from this calss (cat2 can access cat cannot)
    private def example = println("example")
    //protected is only accessible in sub classes (NOT THE SUPER CLASS)
  }

  //the parenths here basically means to override that member as a parameter.
  //it's the same as if we took a normal parameter for the class and then on the override inside the class,
  //override val creatureType: String = creatureTypeParameter
  class Cat(override val creatureType: String) extends Animal {
    /*override val creatureType: String = "Domesticated"*/
    override def eat = {
      //super used to reference super class
      super.eat
      println("nom nom a cat is eating")
    }
    def crunch = {
      eat
      println("crunch")
    }
  }

/*  val cat = new Cat("feline")
  cat eat
  val cat2 = new Animal
  cat2 eat*/

  //type substitution:  polymorphism
  val unknown_animal: Animal = new Cat("Feline")
  unknown_animal.eat

  //preventing overrides
  /*1. keyword final
  *     this prevents overriding of the methods in sub classes
  *     can be used on the class, or on the members
  *     if used on the class, it prevents the class from being extended
  * 2. seal the class
  *     software restriction - you can extend the class in this file but not in other files
  *     keyword: sealed class Dog {....}
  *     if, for example, you wanted the only two types of animals to be cats and dogs, then seal the animal class
  *     and define cat/dog in this file.  Then no other sub classes can exist.
  *  */

  final class Computer
  sealed class Dude

  class Person(name: String, age: Int){
    def this(name: String) = this(name, 0)
    def this() = this("John", 0)
  }
  //this is the correct way to extend a class with parameters.  If you don't do this the compiler will get confused as it looks for the constructor.
  //you can do extends Person, but you need to have a zero parameter constructor if so
  class Adult(name: String, age: Int, idCard: String) extends Person



}
