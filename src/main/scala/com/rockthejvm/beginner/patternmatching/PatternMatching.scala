package com.rockthejvm.beginner.patternmatching

import scala.util.Random

object PatternMatching extends App {
  //switch on steroids
  val random = new Random
  val x = random.nextInt(10)//a random # from 0-10

  val description = x match{
    case 1 => "the one"
    case 2 => "double or nothing"
    case 3 => "third time's the charm"
    case 4 => "four is death"
    case 5 => "five amigos"
    case 6 => "dinner time!"
    case 7 => "lucky number 7"
    case 8 => "eight's too late"
    case 9 => "nine inches"
    case 10 => "ten corners"
    case _ => "something else" //wildcard
  }
  println(x)
  println(description)

  //pattern matching can
  //Decompose values
  case class Person(name: String, age: Int)
  val bob = Person("Bob", 20)

  val greeting = bob match {
    case Person(n, a) if a < 21 => s"hi, my name is $n and I am $a years old, and I cannot drink alcohol!"
    case Person(n, a) => s"hi, my name is $n and I am $a years old"
    case _ => "I don't know who I am"
  }
  //the first pattern that matches returns its expression
  //if we don't match anything then there is an error thrown (scala.MatchError)
  //type of the match expression will be the closest unification of all types that could be returned.
  //    if there are many types, this is likely Any.  If all expressions are the same type then that will be the type
  //PM works really well with case classes
  println(greeting)

  //PM on sealed hierarchies
  sealed class Animal
  case class Dog(breed: String) extends Animal
  case class Parrot(greeting: String) extends Animal

  val animal: Animal = new Dog("Terra Nova")
  animal match{
    case Dog(someBreed) => println(s"Matched a dog of the $someBreed breed")
  }

  //match everything
  val isEven = x match{
    case n if n % 2 == 0 => true
    case _ => false
  }//this is totally overkill, but is syntactically correct
  val isEvenCond = if(x%2==0)true else false //this is also overkill lol
  val commonSense = x % 2 == 0 //there ya go!

  //EXERCISE
  trait Expr
  case class Number(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Prod(e1: Expr, e2: Expr) extends Expr

  def show(e: Expr): String = e match{
    case Number(n) => s"$n"
    case Sum(e1, e2) => show(e1) + " + " + show(e2)
    case Prod(e1, e2) => {
      def maybeShowParentheses(exp: Expr): String =  exp match{
        //the _ here means match anything we don't care about it
        case Prod(_, _) => show(exp)
        case Number(_) => show(exp)
        case _ => "(" + show(exp) + ")"
      }
      maybeShowParentheses(e1) + " * " + maybeShowParentheses(e2)
    }
    case _ => "error"
  }
  println(show(Prod(Sum(Number(3), Number(9)), Number(20))))
  val myExp: Expr = Prod(Sum(Number(20), Prod(Number(90), Sum(Number(3), Sum(Number(32), Number(30))))), Sum(Number(3), Number(8)))
  println(show(myExp))
  // (20+ 90 * (3 + 32 + 30) ) * (3 + 8)
  //It works!  Holy cow!
}
