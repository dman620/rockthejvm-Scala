package com.rockthejvm.beginner.OOP.mylist





object Main extends App {
/*  val myNode = new MyListNode(23)
  val newNode = myNode.add(23).add(34).add(346)
  val newNode2 = newNode.add(32)
  println("newNode: " + newNode)
  println("NewNode head: " + newNode.head)
  println("newnode tail: " + newNode.tail)
  println("newnode delimited commas: " + newNode.printElements())*/

/*  val listOfInts  = new MyListNode(1, new MyListNode(2, new MyListNode(3))).add(4).add(19).add(32).add(22)
  val listOfStrings = (new MyListNode("one", new MyListNode("two", new MyListNode("three")))).add("four")
  val listOfChars = new MyListNode('a', new MyListNode('b', new MyListNode('c')))

  println("listOfInts: " + listOfInts.filter(x => x % 2 == 0))
  println("listOfStrings: " + listOfStrings)
  println("listOfChars: " + listOfChars)*/
  val list1 = new Node(1, new Node(2, new Node(3)))
  val list2 = new Node("Hello", new Node("Scala"))
  println(list1)
  println(list2)
}
