package com.rockthejvm.beginner.Functional

import scala.util.Random

object Options extends App {
  val myFirstOption: Option[Int] = Some(4)
  val noOption: Option[Int] = None

  println(myFirstOption.head)
  println(noOption)

  def unsafeMethod(): String = null
  //val result = Some(unsafeMethod()) //wrong way to do it, Some should NEVER contain null
  val result = Option(unsafeMethod()) //it will be a Some or a None depending on if unsafeMethod returns null
  //the point, is that we are not doing the null check ourselves, the Option type is doing it for us
  println(result)

  //work with unsafe APIs
  def backupMethod(): String = "valid result"
  val chainedResult = Option(unsafeMethod()) orElse Option(backupMethod())
  //since unsafeMethod is null, .orElse tells it to create the option with backupMethod instead
  println(chainedResult)

  //design unsafe APIS
  def betterUnsafeMethod(): Option[String] = None
  def betterBackupMethod(): Option[String] = Some("a valid result")

  val betterChainedResult = betterUnsafeMethod() orElse betterUnsafeMethod()

  //functions on Options
  println(myFirstOption.isEmpty)//false, since it contains 4
  println(myFirstOption.get) //UNSAFE, do not do this.  It voids the whole purpose of using options
  println(myFirstOption.map(_ * 2)) //Some(8)
  println(myFirstOption.filter(_ > 6)) //None, since 4 did not match predicate
  println(myFirstOption.flatMap(x => Option(x * 10))) //Some(40)

  //for comprehensions
  //exercise

  val config: Map[String, String] = Map(
    //fetched from elsewhere
    "host" -> "176.45.36.1",
    "port" -> "80"
  )
  class Connection{
    def connect: String = "Connected" //connect to some server
  }
  object Connection{
    val random = new Random(System.nanoTime())
    def apply(host: String, port: String): Option[Connection] =
      if(random.nextBoolean()) Some(new Connection)
      else None
  }

  //try to establish a connection, if so print the connect method
  val host: Option[String] = config.get("host")
  val port: Option[String] = config.get("port")
  val connection: Option[Connection] = host.flatMap(h => port.flatMap(p => Connection.apply(h, p)))
  /*val connection = for{//this does not work because I assume it uses flatmap/map mix, which will not work with Options
    h <- host
    p <- port
  }yield Connection.apply(h, p)*/


  //achieve the printing of the connection status with vals and separate lines
  /*  val connectionStatus = connection.map(c => c.connect)
  print("Connection Status: ")
  connectionStatus.foreach(println)*/

  //same thing but it's all on one line
/*  config.get("host") //the final boss of un-readability for this section
    .flatMap(host => config.get("port").flatMap(port => Connection(host, port))
    .map(connection => connection.connect))
    .foreach(x => println("Connection Status: " + x))*/

  //using for comprehensions
  for{
    host <- config.get("host")
    port <- config.get("port")
    connection <- Connection(host, port)
  }yield println("Connection Status: " + connection.connect)
}
