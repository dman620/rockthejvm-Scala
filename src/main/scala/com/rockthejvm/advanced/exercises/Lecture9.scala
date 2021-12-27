package com.rockthejvm.advanced.exercises

import scala.io.Source.stdin

object Lecture9 extends App {
  /*
  * 1. construct a PF instance yourself (anonymous class)
  * 2. dumb chatbot as a PF
  * */

  //1 need to override apply and isDefinedAt
  val aManualPF = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = v1 match {
      case 1 => 3
      case 2 => 4
      case i if i >= 3 & i <= 5 => 5
      case i if i >= 6 & i <= 100 => 100
    }

    override def isDefinedAt(x: Int): Boolean =
      if(x <= 4 & x >= 0) true
      else false
  }
  println(aManualPF(3))
  println(aManualPF(64))

  //2
  print(">")
  stdin.getLines.map(_.toLowerCase).map{
      case "q" => System.exit(0); ""
      case "hello" => "Hello, yourself!"
      case "what's up" => "Nothing much, how about you?"
      case "man, i forgot my updog at home" => "What's updog?"
      case "nothing much, how about you?" => "Oh man, you got me!"
      case _ => "Sorry bucko, I don't understand."
  }.foreach(line => print(line + "\n>"))
/*  stdin.getLines.map(new PartialFunction[String, String]{
    override def apply(v1: String): String = v1.toLowerCase() match{
      case "q" => System.exit(0); ""
      case "hello" => "Hello, yourself!"
      case "what's up" => "Nothing much, how about you?"
      case "man, i forgot my updog at home" => "What's updog?"
      case "nothing much, how about you?" => "Oh man, you got me!"
      case _ => "Sorry bucko, I don't understand."
    }

    override def isDefinedAt(x: String): Boolean = {
      val absx = x.toLowerCase()
      if(absx == "q" | absx == "hello" | absx == "what's up" | absx == "man, i forgot my updog at home"
            | absx == "nothing much, how about you?") true
      else false
    }
  }).foreach(line => print(line + "\n>"))*/
}
