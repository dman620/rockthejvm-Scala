package com.rockthejvm.beginner.OOP.mylist

import scala.language.postfixOps
import scala.runtime.Nothing$

abstract class MyList[+A] {
  def head: A

  def tail: MyList[A]

  def isEmpty: Boolean

  def add[B >: A](element: B): MyList[B]

  def printElements(delimiter: String = " "): String

  override def toString: String = "[" + printElements(", ") + "]"
}

object Empty extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException

  def tail: MyList[Nothing] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def add[B >: Nothing](element: B): MyList[B] = new Node(element, Empty)

  def printElements(delimiter: String): String = ""
}

class Node[+A](h: A, t: MyList[A] = Empty) extends MyList[A] {
  def head: A = h

  def tail: MyList[A] = t

  def isEmpty: Boolean = false

  def add[B >: A](element: B): MyList[B] = new Node(element, this)

  //a tail recursive implementation that allows the user to specify a delimiter.  And the function does not add
  //the delimiter before the first element, which would be ugly looking i.e. -> this[1, 2, 3] not this[, 1, 2, 3]
  def printElements(delimiter: String): String = {
    def go(accumulatedString: String, currentNode: MyList[A]): String = {
      if (currentNode.isEmpty) accumulatedString
      else go(accumulatedString + (if (currentNode == this) "" else delimiter) + currentNode.head, currentNode.tail)
    }

    go("", this)
  }

  trait myPredicate[-T]{
    def test(elem: T): Boolean
  }
  trait myTransformer[-A, B]{
    def transform(elem: A): B
  }
}