package com.rockthejvm.beginner.OOP

object Generics extends App {
  class MyList[+A]{
    //works just like templates in C++N
    def add[B >: A](element: B): MyList[B] = ???
    /*
    * A = Cat
    * B = Dog = Animal*/
  }

  class MyMap[Key, Value]
  val listOfIntegers = new MyList[Int]
  val listOfStrings = new MyList[String]

  //generic methods
  //objects CANNOT be typed paremeterized (no generics allowed )
  object MyList {
    def empty[A]: MyList[A] = ???
  }

  //variance problem
  //if cat extends animal, does a list of cats extend a list of animals?
  class Animal
  class Cat extends Animal
  class Dog extends Animal

  // yes, List[Cat] extends List[Animal]
  //this behavior is called covariance
  class CovariantList [+A]
  class InvariantList [A]
  val animal: Animal = new Cat
  val animalList: CovariantList[Animal] = new CovariantList[Cat]
  //animalList.add(new Dog) Is this legal?  Here it is because of the + sign signaling a variant list
  //if it were
  //class InvariantList[A] then it would not be legal to do this
  //example of illegal list:
  //val invariantAnimalList: InvariantList[Animal] = new InvariantList[Cat]
  //it will give type mismatch error

  //Hell no!  Contravariance
  //NOT intuitive for lists
  class ContravariantList[-A]
  val contravariantList: ContravariantList[Cat] = new ContravariantList[Animal]

  //more intuitive for certain situations
  //ex: this trainer knows how to train a cat and thus animals
  class Trainer[-A]
  val trainer: Trainer[Cat] = new Trainer[Animal]

  //bounded types
  //only allows you to use generics that are a sub or superclass of a certain type
  //<: means only accept subtypes of Animal
  class Cage[A <: Animal](animal: A)
  val cage = new Cage(new Dog)

  class Car
  //this will not compile because the Car class is not a subtype of Animal
  // val newCage = new Cage(new Car)

  /*
  *  <: upper bounded: only accept subtypes
  *  >: lower bounded: only accept supertypes
  * */
}
