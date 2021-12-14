package com.rockthejvm.advanced.lectures.part1

import scala.util.Try

object DarkSyntax extends App {
  //methods with single parameters
  def singleArgMethod(arg: Int): String = s"$arg little duckies"
  val pondSize = 10
  val description = singleArgMethod{
    //I'm not sure how many duckies so I'll do some logic here
    pondSize * 2
  }
  //essentially we're using the code block as the parameter, and omitting the () to make it look cleaner
  println(description)

  val aTryInstance = Try{
    throw new RuntimeException
  }
  println(List(1,2,3).map{ x =>
    val temp = Math.pow(2, 4)
    val temp2 = 20
    x * temp % temp2
  })



  //syntax sugar #2 single abstract method
  trait Action{
    def act(x: Int): Int
  }
  val myAction: Action = new Action{ //small brain
    override def act(x: Int): Int = x+3
  }
  val myOtherAction: Action = (x: Int) => x + 3 //big brain
  println(myAction.act(3) + " " + myOtherAction.act(3))
  //this is saying, create an anonymous class which has the following implementation for the act(x: Int) method,
  //and then assign the value of that class to myOtherAction.

  //runnables
  val aThread = new Thread(new Runnable { //small brain
    override def run(): Unit = println("hello scala")
  })
  val bestThread = new Thread( () => println("heck yeah")) //big brain

  //types that have one unimplemented function:
  abstract class AnAbstractType {
    def implemented: Int = 23
    def betelgeuse(a: Int): Unit
  }
  val aDumbAbstractInstance: AnAbstractType = new AnAbstractType { //small brain
    override def betelgeuse(a: Int): Unit = println(s"im a big dummy, using so many lines, $a")
  }
  val anAbstractInstance: AnAbstractType = (a: Int) => println(s"sweet, ${a}") //big brain
  println(anAbstractInstance.betelgeuse(3))
  //what we're basically saying here is: create a new instance of anAbstractInstance, with the f(a: Int) function having this implementation
  //this only works for types that have exactly one function un-implemented.  The name of the function does not matter



  //Syntax sugar #3: the :: and #:: methods are special
  val prependedList = 2 :: List(3, 4)
  //this is not 2.::(List(3,4)) because there is no :: method in Int
  //this is equivalent to List(3,4).::(2)   (right associative)
  //"the associativity of a method is determined by the operator's last character"
  //if an operator ends in a ":" (including user-defined methods), then it is right associative
  val list1 = 1 :: 2 :: 3 :: List(4, 5)
  val list2 = List(4,5).::(3).::(2).::(1)
  //these expressions are identical
  println(list1.equals(list2)) //true

  class MyStream[T]{
    def -->:(value: T): MyStream[T] = this //actual implementation here
    def -->(value: T): MyStream[T] = this
  }
  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]
  val myStream2 = new MyStream[Int].-->:(3).-->:(2).-->:(1)
  //these are identical, but the first one is much more readable
  //this is also valid, you don't need to use : right associativity unless it is useful for your situation
  val mySteam3 = new MyStream[Int] --> 3 --> 2 --> 1



  //syntax sugar #4: multi-word method naming
  class TeenGirl(name: String){
    def  `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }
  val lilly = new TeenGirl("lilly")
  println(lilly `and then said` "Scala is so sweet")



  //syntax sugar #5: infix types
  class Composite[A, B]
  val comp1: Composite[Int, String] = new Composite[Int, String]// small brain
  val comp2: Int Composite String = new Composite[Int, String] // big brain

  class -->[A, B]
  val towards: Int --> String = new -->
  //seems like an intuitive way to name a transformer class



  //syntax sugar #6: update() is very special, much like apply()
  val anArray = Array(1,2,3)
  anArray(2) = 7 //anArray.update(2, 7)
  //update is used in mutable collections
  anArray.foreach(println) //1, 2, 7
  //even though anArray is immutable, the value of the array can change


  //syntax sugar #7: setters for mutable containers
  class Mutable(private var internalMember: Int = 0){
    //private var internalMember: Int = 0 //private for OOP encapsulation
    def member: Int = internalMember
    def member_=(value: Int): Unit = internalMember = value
  }
  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 //rewritten as aMutableContainer.member_=(42)
  println(aMutableContainer.member)
}