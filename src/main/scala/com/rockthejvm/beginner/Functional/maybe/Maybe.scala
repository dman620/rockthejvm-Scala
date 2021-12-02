package com.rockthejvm.beginner.Functional.maybe

abstract class Maybe[+A]{
  def map[B](transformer: A => B): Maybe[B]
  def flatMap[B](transformer: A => Maybe[B]): Maybe[B]
  def filter(predicate: A => Boolean): Maybe[A]
}
case object Nope extends Maybe[Nothing]{
  def map[B](transformer: Nothing => B): Maybe[B] = Nope
  def flatMap[B](transformer: Nothing => Maybe[B]): Maybe[B] = Nope
  def filter(predicate: Nothing => Boolean): Maybe[Nothing] = Nope
}
case class Just[+A](val data: A) extends Maybe[A]{
  def map[B](transformer: A => B): Maybe[B] = new Just(transformer(data))
  def flatMap[B](transformer: A => Maybe[B]): Maybe[B] = transformer(data)
  def filter(predicate: A => Boolean): Maybe[A] = if(predicate(data))this else Nope
}

object testing extends App{
  val just3 = Just(3)
  println(just3)
  println(just3.map(_ * 2))
  println(just3.flatMap(x => Just(x % 2 == 0)))
  println(just3.filter(_ % 2 == 0))
}