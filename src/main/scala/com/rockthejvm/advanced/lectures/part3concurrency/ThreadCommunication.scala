package com.rockthejvm.advanced.lectures.part3concurrency

import scala.collection.mutable
import scala.io.StdIn.{readChar, readLine}
import scala.util.Random

object ThreadCommunication extends App {
  /*
  * The producer-consumer problem
  * producer -> [ x ] -> consumer
  * */
  class SimpleContainer{
    private var value: Int = 0

    def isEmpty: Boolean = value == 0
    def get: Int = {
      val result = value
      value = 0
      result
    }
    def set(newValue: Int): Unit = value = newValue
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      while(container.isEmpty){
        println("[consumer] waiting...")
      }
      println("[consumer] I have consumed" + container.get)
    })
    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42
      println("[producer] I have produced the value of " + value)
      container.set(value)
    })
    consumer.start()
    producer.start()
  }

  def smartProdCons(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      container.synchronized{
        container.wait()
      }
      //container must have some value
      println(s"[consumer] I have consumed ${container.get}")
    })
    val producer = new Thread(() => {
      println("[producer] is hard at work")
      Thread.sleep(2000)
      val value = 42
      container.synchronized{
        println(s"[producer] I'm producing $value")
        container.set(value)
        container.notify()
      }
    })
    consumer.start()
    producer.start()
  }

  //naiveProdCons()
  //smartProdCons()

  /*
  * producer -> [ x y z ] -> consumer (indefinitely producing/consuming)
  * they can block eachother now
  * 1. the buffer is full (producer is blocked until values can be extracted)
  * 2. the buffer is empty (consumer is blocked until a value can be created)
  * */

  def prodConsLargeBuffer(): Unit = {
    val  buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3 //enforced capacity of 3

    val consumer = new Thread(() => {
      val random = new Random()
      while(true){
        buffer.synchronized{
          if(buffer.isEmpty){
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }
          //there must be at least one value
          val x = buffer.dequeue()
          println(s"[consumer] i have consumed $x")
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })
    val producer = new Thread(() => {
      val random = new Random()
      var i = 0
      while(true){
        buffer.synchronized{
          if(buffer.size == capacity){
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }
          //must be a slot for an insertion
          buffer.enqueue(i)
          println(s"[producer] i have produced $i")
          buffer.notify()
          i += 1
        }
      }
      Thread.sleep(random.nextInt(500))
    })
    consumer.start()
    producer.start()
  }
  //prodConsLargeBuffer()

  /*
  * producer consumer level 3
  * producerx -> [ x y z ] -> consumerx (arbitrary number of consumers/producers)
  * */

  class Consumer(id: Int, buffer: mutable.Queue[Int], outputBuffer: OutputBuffer) extends Thread{
    private var i = 0
    private val maxSleepTime = 500
    override def run(): Unit = {
      val random = new Random()
      while(true){
        buffer.synchronized{
          while(buffer.isEmpty){
            outputBuffer.theBuffer.synchronized{
              outputBuffer.theBuffer.enqueue(s"[consumer<$id>] buffer empty, waiting...")
            }
            buffer.wait()
          }
          //there must be at least one value

          outputBuffer.theBuffer.synchronized{
            i = buffer.dequeue()
            outputBuffer.theBuffer.enqueue(s"[consumer<$id>] i have consumed $i")
          }
          buffer.notify()
        }
        Thread.sleep(random.nextInt(maxSleepTime))
      }
    }
    override def toString: String = {
      s"[C<$id>]: $i"
    }
    def clear(): Unit = {
      i = 0
    }
  }
  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int, outputBuffer: OutputBuffer) extends Thread{
    private var i = 0
    private val maxSleepTime = 500
    override def run(): Unit = {
      val random = new Random()
      while(true){
        buffer.synchronized{
          while(buffer.size == capacity){
            outputBuffer.theBuffer.synchronized{
              outputBuffer.theBuffer.enqueue(s"[producer<$id>] buffer is full, waiting...")
            }
            buffer.wait()
          }
          //must be a slot for an insertion
          outputBuffer.theBuffer.synchronized{
            buffer.enqueue(i)
            outputBuffer.theBuffer.enqueue(s"[producer<$id>] i have produced $i")
          }
          i += 1
          buffer.notify()
        }
        Thread.sleep(random.nextInt(maxSleepTime))
      }
    }
    override def toString: String = {
      s"[P<$id>]: $i"
    }
    def clear(): Unit = {
      i = 0
    }
  }

  class OutputBuffer() extends Thread {
    val theBuffer = new mutable.Queue[String]
/*  larger delay = less overhead & smoother operation, but if it is TOO large, updates will come in unreadable
    waves, and theoretically the buffer can overflow*/
    val delayMilliSeconds = 100

    override def run(): Unit = {
      while (true) {
        theBuffer.synchronized {
          while (theBuffer.nonEmpty) //ensures the buffer does not become too big
            println(theBuffer.dequeue())
        }
        Thread.sleep(delayMilliSeconds)
      }
    }
  }

  class OptionListener(consumers: Vector[Consumer], producers: Vector[Producer], buffer: mutable.Queue[Int], outputBuffer: OutputBuffer) extends Thread {
    private val menu = "User input menu:\n" +
      "h - help.  This prints the menu again.\n" +
      "p - print. This prints the status of the buffer, consumers, and producers.\n" +
      "b - clear buffer. This clears the value buffer.\n" +
      "w - clear workers. This clears the worker threads' counters to zero.\n" +
      "c - clear all.  This clears the value buffer and the worker threads.\n" +
      "(to make a choice, press ENTER to freeze the program, then type your choice, then press ENTER again.)"
    override def run(): Unit = {
      println(menu)
      while (true) {
        readLine() //wait for user input
        outputBuffer.theBuffer.synchronized{
          print("Enter selection:\n>")
          val input = readChar()
          decision(input)
          println("Press ENTER when ready to continue.")
          readLine()
        }
      }
    }
    def decision(input: Char): Unit = {
      input.toUpper match{
        case 'P' =>
          print("B: [")
          buffer.foreach(x => print(s"-$x-"))
          println("]")
          consumers.foreach(x => {
            print(s"${x.toString} - ")
          })
          println()
          producers.foreach(x => {
            print(s"${x.toString} - ")
          })
          println()
        case 'H' =>
          println(menu)
        case 'B' =>
          println("Buffer cleared!")
          clearBuffer()
        case 'W' =>
          println("Workers cleared!")
          clearWorkers()
        case 'C' =>
          println("Buffer and workers cleared!")
          clearBuffer()
          clearWorkers()
        case _ =>
          println("Invalid selection")
      }
    }
    def clearWorkers(): Unit = {
      consumers.foreach(consumer => consumer.clear())
      producers.foreach(producer => producer.clear())
    }
    def clearBuffer(): Unit = {
      while(buffer.nonEmpty) buffer.dequeue()
    }
  }
  val valueBuffer = new mutable.Queue[Int]
  val numWorkers = 10
  val capacity = 50
  val outputBuffer = new OutputBuffer
  val consumers: Vector[Consumer] = (1 to numWorkers).toVector.map(consumer => new Consumer(consumer, valueBuffer, outputBuffer))
  val producers: Vector[Producer] = (1 to numWorkers).toVector.map(producer => new Producer(producer, valueBuffer, capacity, outputBuffer))
  val printer = new OptionListener(consumers, producers, valueBuffer, outputBuffer)
  outputBuffer.start()
  consumers.foreach(consumer => consumer.start())
  producers.foreach(producer => producer.start())
  printer.start()
}
