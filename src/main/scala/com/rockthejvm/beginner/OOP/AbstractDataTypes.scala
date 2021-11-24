package com.rockthejvm.beginner.OOP

object AbstractDataTypes extends App {
  //abstract
  abstract class Animal {
    val creatureType: String = "Wild"

    def eat: Unit
  }

  class Dog extends Animal {
    override val creatureType: String = "Canine"

    override def eat: Unit = println("This is a dog eating")

  }

  //traits are like interfaces, the ultimate abstract type
  trait carnivore {
    def eat(animal: Animal): Unit
    val preferedMeal = "Raw Meat"
  }

  trait coldBlooded

  class Crocodile extends Animal with carnivore with coldBlooded {
    override val creatureType: String = "croc"

    def eat: Unit = println("this is a croc eating")

    def eat(animal: Animal): Unit = println(s"this is a croc eating ${animal.creatureType}")
  }

  val dog = new Dog
  val aCroc = new Crocodile
  aCroc.eat(dog)

  /*
  * Traits VS Abstract classes
  * 1. traits do not have constructor params
  * 2. multiple traits may be inherited by the same class
  * 3. traits are used for types of behavior, while abstract classes are a type of thing
  * */

  //TYPE HIERARCHY
  //for diagram go to the video and go to 11:10
  //Scala & functional programming essentials > OOP > 16. Inheritance continued abstract classes and traits

}
