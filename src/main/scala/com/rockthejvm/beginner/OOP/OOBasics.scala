package com.rockthejvm.beginner.OOP

import java.time.Year

object OOBasics extends App {
  val myPerson = new Person("John", 26)
  println(myPerson.age)
  myPerson.greet("Daniel")
  myPerson.greet()
}

//name, string is a class parameter, but not a field or member.  You cannot access it in the program with a dot operator
//if you put "val" in front of the arg name then it is a member and you may access via dot operator
//constructor
class Person(name: String, val age: Int) {
  //body value of the code block is ignored, but you can do anything in there that you can do in a normal code block
  val x = 2 //this is a member and may be dot operator accessed
  println(1 + 3)

  def greet(name: String): Unit = {
    println(s"${this.name} says: Hi, $name")
  }

  def greet(): Unit = println(s"Hi, I am $name")

  //multiple constructors
  def this(name: String) = this(name, 0) //call primary constructor with these parameters

  def this() = this("John Doe") //the only thing that can be in the body of the overloaded constructors is another constructor call
  //this makes them pretty useless, because you could achieve the same goals by just giving the default constructor default values

  /*EXERCISES*/
  /*1. Novel and writer class
  *   Writer: first name, surname, year of birth
  *       -fullname()
  *   Novel: name, year of release, author
  *       -authorAge() = authors age at time of writing
  *       -isWrittenby()
  *       -copy(new year of release) = new instance of novel with new year
  * 2. Counter Class
  *     -recieves an int
  *     -method current count
  *     -method to increment and decrement counter by 1 => returns new counter
  *     -overload inc/dec to recieve an amount to inc/dec => new counter
  * */


  //PROBLEM 1: Novel and Writer classes

  trait Printable {
    def print(): Unit
  }

  class Writer(name: String, surName: String, val dob: Int) extends Printable{
    def fullName(): String = name + " " + surName

    def age(): Int = Year.now.getValue - dob

    def write(aname: String): Novel = new Novel(aname, Year.now.getValue, this)

    def print(): Unit = println(s"$name $surName, born in $dob.")
  }

  class Novel(name: String, yor: Int, val author: Writer) extends Printable {
    def authorAge(): Int = yor - this.author.dob

    def isWrittenBy(author: Writer): Boolean = this.author == author

    def copy(nyor: Int): Novel = new Novel(this.name, nyor, this.author)

    def print(): Unit = println(s"$name, released $yor, written by ${author.fullName()}\n    ${author.fullName()} was ${authorAge()} years old at the time of writing")
  }

  val derek = new Writer("Derek", "Mandl", 1994)
  val vasil = new Writer("Vasil", "Diteri", 1823)
  val david = new Writer("David", "Turner", 2000)

  val parsingin2021 = new Novel("Parsing In 2021", 2021, derek)
  val parenting = new Novel("Parenting in the Cold War", 1847, vasil)
  val why = new Novel("Why oh Why, God", 1895, vasil)
  val how = new Novel("How to Suck at Fishing", 2019, david)

  val writerList = List[Writer](derek, vasil, david)
  val novelList = List[Novel](parsingin2021, parenting, why, how)

  def printList(l: List[Printable]): Unit = {
    l.head.print()
    if (l.length > 1) printList(l.tail)
  }

  printList(writerList)
  println()
  printList(novelList)

  //PROBLEM 2: Counter Class
  class Counter(val i: Int = 0) {
    def currentCount(): Int = i

    def ++(): Counter = new Counter(i + 1)

    def --(): Counter = new Counter(i - 1)

    def +(n: Int): Counter = new Counter(i + n)

    def -(n: Int): Counter = new Counter(i - n)

    override def toString(): String = currentCount().toString
  }

  val myCounter = new Counter()

  def incrementTo(i: Int, c: Counter = myCounter): Unit = {
    println(c)
    if (i > 1) incrementTo(i - 1, c.++)
  }

  incrementTo(10)
}