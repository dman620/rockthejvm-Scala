package com.rockthejvm.beginner.OOP
import scala.language.postfixOps

object MethodNotationsExercise extends App{
  class Person(val name: String, val favoriteMovie: String, age: Int = 0){
    def likes(movie: String): Boolean = movie == favoriteMovie
    def +(person: Person): String = s"$name is hanging our with ${person.name}."
    def +(nickname: String): Person = new Person(name + s"($nickname)", favoriteMovie, age)
    def unary_+ : Person = new Person(name, favoriteMovie, age + 1)
    def unary_! : String = s"$name, what the heck?!"
    def isAlive: Boolean = true
    def learns(skill: String): String = s"$name learns $skill"
    def learnsScala(): String = learns("Scala")
    def apply(): String = s"Hi, my name is $name, I am $age years old, and I like $favoriteMovie"
    def apply(num: Int): String = s"$name watched $favoriteMovie $num times!"
  }
  val mary = new Person("Mary", "Aliens", 35)
  println(mary())
  val mary2 = (+mary) + "Betelgeuse"
  println(mary2())

  println(mary learns "knitting")
  println(mary(3))
}


/*
* 1. overload the + operator
*     mary + "the rockstar" => new person "Mary (the rockstar) same fav movie
  2. add age to person class default 0
  *   add unary + operator which increments value of age with new person
  3. add learns method in the person class
  *   takes string and sends message "Mary learns $skill"
  *   add learn scalla method that takes no parameters but learns scala
  *   us it in postfix notation
  4. overload the apply method to recieve a number and return a string
  *   mary(2) => mary watched bla bla 2 times
  * */

