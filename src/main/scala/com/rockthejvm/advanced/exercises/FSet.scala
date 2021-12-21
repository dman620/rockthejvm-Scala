package com.rockthejvm.advanced.exercises

//exercise implement a functional set
trait FSet[A] extends (A => Boolean){
  def contains(elem: A): Boolean
  def +(elem: A): FSet[A]
  def ++(anoterSet: FSet[A]): FSet[A]
  def map[B](f: A => B): FSet[B]
  def flatMap[B](f: A => FSet[B]): FSet[B]
  def filter(predicate: A => Boolean): FSet[A]
  def foreach(f: A => Unit): Unit
}
