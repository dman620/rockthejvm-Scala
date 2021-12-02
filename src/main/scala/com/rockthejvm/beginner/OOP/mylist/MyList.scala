package com.rockthejvm.beginner.OOP.mylist

import scala.annotation.tailrec
import scala.language.postfixOps
import scala.runtime.Nothing$


abstract class MyList[+A] {
  def head: A

  def tail: MyList[A]

  def isEmpty: Boolean

  def add[B >: A](element: B): MyList[B]

  def printElements(delimiter: String = " "): String

  override def toString: String = "[" + printElements(", ") + "]"

  def map[B](transformer: A => B): MyList[B]

  def flatMap[B](transformer: A => MyList[B]): MyList[B]

  def filter(predicate: A => Boolean): MyList[A]

  def ++[B >: A](list: MyList[B]): MyList[B]

  def forEach(func: A => Unit): Unit

  def sort(compare: (A, A) => Boolean): MyList[A]

  def zipWith[B, C](list: MyList[B], zip: (A, B) => C): MyList[C]

  def fold[B](start: B)(operator: (B, A) => B): B
}

case object Empty extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException

  def tail: MyList[Nothing] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def add[B >: Nothing](element: B): MyList[B] = new Node(element, Empty)

  def printElements(delimiter: String): String = ""

  def map[B](transformer: Nothing => B): MyList[B] = Empty

  def flatMap[B](transformer: Nothing => MyList[B]): MyList[B] = Empty

  def filter(predicate: Nothing => Boolean): MyList[Nothing] = Empty

  def ++[B >: Nothing](list: MyList[B]): MyList[B] = list

  def forEach(func: Nothing => Unit): Unit = ()

  def sort(compare: (Nothing, Nothing) => Boolean): MyList[Nothing] = Empty

  def zipWith[B, C](list: MyList[B], zip: (Nothing, B) => C): MyList[C] = {
    if(!list.isEmpty) throw new RuntimeException("Lists do not have the same length")
    else Empty
  }

  def fold[B](start: B)(operator: (B, Nothing) => B): B = start
}

case class Node[+A](private val h: A, private val t: MyList[A] = Empty) extends MyList[A] {
  def head: A = h

  def tail: MyList[A] = t

  def isEmpty: Boolean = false

  def add[B >: A](element: B): MyList[B] = new Node(element, this)

  //a tail recursive implementation that allows the user to specify a delimiter.  And the function does not add
  //the delimiter before the first element, which would be ugly looking i.e. -> this[1, 2, 3] not this[, 1, 2, 3]
  def printElements(delimiter: String): String = {
    @tailrec
    def go(accumulatedString: String, currentNode: MyList[A]): String = {
      if (currentNode.isEmpty) accumulatedString
      else go(accumulatedString + (if (currentNode == this) "" else delimiter) + currentNode.head, currentNode.tail)
    }

    go("", this)
  }
  def map[B](transformer: A => B): MyList[B] = {
    new Node(transformer(head), tail.map(transformer))
  }

  def flatMap[B](transformer: A => MyList[B]): MyList[B] = {
    transformer(head) ++ tail.flatMap(transformer)
  }

  def filter(predicate: A => Boolean): MyList[A] = {
    if(predicate(head)) new Node(head, tail.filter(predicate))
    else tail.filter(predicate)
  }

  def ++[B >: A](list: MyList[B]): MyList[B] = {
    new Node(head, tail ++ list)
  }

  def forEach(func: A => Unit): Unit = {
    @tailrec
    def go(currentNode: MyList[A]): Unit = {
      if(currentNode.isEmpty) ()
      else{
        func(currentNode.head)
        go(currentNode.tail)
      }
    }
    go(this)
  }


 /* def forEach(func: A => Unit): Unit = {
    func(head)
    tail.forEach(func)
  }*/
  def sort(compare: (A, A) => Boolean): MyList[A] = {
    def insert(elem: A, sortedList: MyList[A]): MyList[A] = {
      if(sortedList.isEmpty) new Node(elem, Empty)
      else if(compare(elem, sortedList.head)) new Node(elem, sortedList)
      else new Node(sortedList.head, insert(elem, sortedList.tail))
    }

    val sortedTail = tail.sort(compare)
    insert(head, sortedTail)
  }

  /*def zipWith[B, C](func: (A, B) => C, otherList: MyList[B]): MyList[C] = {
    @tailrec
    def go(currNode1: MyList[A], currNode2: MyList[B], accumulatedList: MyList[C]): MyList[C] = {
      if(currNode2.isEmpty || currNode1.isEmpty) accumulatedList
      else go(currNode1.tail, currNode2.tail, accumulatedList.add(func(currNode1.head, currNode2.head)))
    }
    go(this, otherList, Empty)
  }*/ // this doe not work atm

  def zipWith[B, C](list: MyList[B], zip: (A, B) => C): MyList[C] = {
    if(list.isEmpty) throw new RuntimeException("Lists do not have the same length")
    else new Node(zip(head, list.head), tail.zipWith(list.tail, zip))
  }

  def fold[B](start: B)(operator: (B, A) => B): B = {
    tail.fold(operator(start, head))(operator)
  }
}
